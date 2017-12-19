package bubbly2017.payfare.com.bubbly2017;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.nsd.NsdServiceInfo;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class ServerSetup extends Activity {


    public boolean bIsConnected;
    public myFile_Logger Log_File = new myFile_Logger();
    public File logFileDir;
    public String filePath="";
    public static final String Default_IP = "DEFAULT_IP";
    public static final String Default_PRT = "DEFAULT_PORT";
    public static final String Default_Server_Timeout = "DEFAULT_TIMEOUT";

    NsdHelper mNsdHelper;
    public boolean bServerPageNeedsSaving = false;

    public boolean bSendMessagePossible=false;
    private TextView ed_Test_Server_Response;
    private static Handler mUpdateHandler;
    Button btnTestConnection,btnSendTestMessage;
    public static final String TAG = "Payfare_Server_Setting";
    public EditText edCurrIP,edCurrIP2,edCurrIP3,edCurrIP4;
    public EditText edCurrPort,edServerTimeout;
    public static String strCurrIP,strCurrIP2,strCurrIP3,strCurrIP4,striFinalIp;
    public static String strCurrPort,strCurTimout;
    public String strServerDataLine;
    public RadioButton radButtonSvrConnected;


    ChatConnection mSVRConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_setup);

        bIsConnected=false;
        btnTestConnection = findViewById(R.id.btnTestConnection);
        ed_Test_Server_Response = findViewById(R.id.ed_Test_Server_Response);
        ed_Test_Server_Response.setMovementMethod(new ScrollingMovementMethod());
        edCurrIP = findViewById(R.id.ed_default_server_ip);
        edCurrIP2 = findViewById(R.id.ed_default_server_ip2);
        edCurrIP3 = findViewById(R.id.ed_default_server_ip3);
        edCurrIP4 = findViewById(R.id.ed_default_server_ip4);
        radButtonSvrConnected = findViewById(R.id.radButtonConncected);
        btnSendTestMessage = findViewById(R.id.btnSendTestString);
        edCurrPort = findViewById(R.id.ed_default_server_port);
        edServerTimeout = findViewById(R.id.ed_Connection_Timeout);

        edCurrIP.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

                // you can call or do what you want with your EditText here
                bServerPageNeedsSaving = true;

                CombineIpSubnets();
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        edCurrIP2.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

                // you can call or do what you want with your EditText here
                bServerPageNeedsSaving = true;

                CombineIpSubnets();
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        edCurrIP3.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

                // you can call or do what you want with your EditText here
                bServerPageNeedsSaving = true;

                CombineIpSubnets();
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        edCurrIP4.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

                // you can call or do what you want with your EditText here
                bServerPageNeedsSaving = true;

                CombineIpSubnets();
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });


        edCurrPort.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

                // you can call or do what you want with your EditText here
                bServerPageNeedsSaving = true;
                strCurrPort = edCurrPort.getText().toString();

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });


        edServerTimeout.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

                // you can call or do what you want with your EditText here
                bServerPageNeedsSaving = true;
                strCurTimout = edServerTimeout.getText().toString();
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        btnTestConnection.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                CheckPayfareServerConenction(v);
            }
        });

        btnSendTestMessage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                SendTestString();
            }
        });
        //******************************************************************************************//
        /// Receiving thread data from server client will be passed back from received
        /// thread to below chatline
        ///*****************************************************************************************//

        mUpdateHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                strServerDataLine = msg.getData().getString("msg");

                if(!bSendMessagePossible){SendMessagePossible();}

                if (mSVRConnection.biSConnected && strServerDataLine.contains(">") && strServerDataLine.trim().length() < 2) {
                    ed_Test_Server_Response.setText("");
                    ed_Test_Server_Response.setText("Server Connected:\n");
                    addServerResponseLine(strServerDataLine);
                } else {
                    addServerResponseLine(strServerDataLine);

                }
            }
        };

        //******************************************************************************************//
        // SaveLogLine("Starting Wifi page");
        mSVRConnection = new ChatConnection(mUpdateHandler);

        mNsdHelper = new NsdHelper(this);
        mNsdHelper.initializeNsd();

    }


    public void CombineIpSubnets(){
        strCurrIP =edCurrIP.getText().toString();
        strCurrIP2 =edCurrIP2.getText().toString();
        strCurrIP3 =edCurrIP3.getText().toString();
        strCurrIP4 =edCurrIP4.getText().toString();
        striFinalIp = strCurrIP + "." + strCurrIP2 + "." + strCurrIP3+ "." + strCurrIP4;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_server_setup, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.Server_Settings_menu_action_exit) {
            bServerPageNeedsSaving=true;
            onBackPressed();

            return true;
        }

        if(id==R.id.Server_Settings_menu_action_exit_no_Save){

            bServerPageNeedsSaving=false;
            onBackPressed();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    //***************************************************************************//
    //************           NEXT Methods is added for network functions ********//
    //**************************************************************************//
    public void ReadServerSavedDefaultSettings(){
        int IpSub1Pos,IpSub2Pos,IpSub3Pos;
        String strIpSub1,strIpSub2,strIpSub3,strIpSub4;

        Context context = getApplicationContext();
        SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.PayfareServerDefaults), Context.MODE_PRIVATE);
        striFinalIp =sharedPref.getString(Default_IP,getString(R.string.DEFIP));
        strCurrPort = sharedPref.getString(Default_PRT,getString(R.string.default_port));
        strCurTimout = sharedPref.getString(Default_Server_Timeout,getString(R.string.Server_Default_Timeout));

        //striFinalIp = "197.242.146.179";
        IpSub1Pos = striFinalIp.indexOf('.');
        IpSub2Pos = striFinalIp.indexOf('.',IpSub1Pos+1);
        IpSub3Pos = striFinalIp.indexOf('.',IpSub2Pos+1);


        strIpSub1=striFinalIp.substring(0,IpSub1Pos);
        strIpSub2=striFinalIp.substring(IpSub1Pos+1,IpSub2Pos);
        strIpSub3=striFinalIp.substring(IpSub2Pos+1,IpSub3Pos);
        strIpSub4=striFinalIp.substring(IpSub3Pos+1,striFinalIp.length());

        edCurrIP.setText(strIpSub1);
        edCurrIP2.setText(strIpSub2);
        edCurrIP3.setText(strIpSub3);
        edCurrIP4.setText(strIpSub4);

        edCurrPort.setText(strCurrPort);
        edServerTimeout.setText(strCurTimout);

    }

    public void SaveCurrentServerSettings(){


        if(bServerPageNeedsSaving) {
            Context context = getApplicationContext();
            SharedPreferences sharedPref = context.getSharedPreferences(
                    getString(R.string.PayfareServerDefaults), Context.MODE_PRIVATE);

            // SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();

            CombineIpSubnets();
            strCurrPort = edCurrPort.getText().toString();
            strCurTimout = edServerTimeout.getText().toString();

            editor.putString(Default_IP, striFinalIp);
            Log.d(TAG,Default_IP + "-Saved:" + striFinalIp);
            editor.commit();
            editor.putString(Default_PRT, strCurrPort);
            Log.d(TAG,Default_PRT + "-Saved:" + strCurrPort);
            editor.commit();
            editor.putString(Default_Server_Timeout, strCurTimout);
            Log.d(TAG,Default_Server_Timeout + "-Saved:" + strCurTimout);
            editor.commit();

        }
        //PayfareServerDefaults


    }
    public void CheckPayfareServerConenction (View v){

        ConnectToServer();
        return;
    }



    public void ConnectToServer(){
        //PrepareForServerConnection();
        String err;
        NsdServiceInfo service = mNsdHelper.getChosenServiceInfo();
        SaveLogLine("\nConnect Click Start:" + service);
        try {
            InetAddress inetIP = InetAddress.getByName(striFinalIp);
            //197.242.146.179

            Log.d(TAG, "Connecting.");
            SaveLogLine("Connecting... to IP:" + striFinalIp + "\nPort:" + strCurrPort);

            mSVRConnection.connectToServer(inetIP,Integer.parseInt(strCurrPort));
            // connectedTimer.start();
            if(mSVRConnection.biSConnected){

                radButtonSvrConnected.setChecked(true);
                //radButtonSvrConnected.setButtonDrawable(R.drawable.serverbtn_ok150);

            }
        }catch(UnknownHostException uHe)
        {

            err = uHe.getMessage() + "\n" + "Err Code:" + uHe.toString();
            addServerResponseLine("CONerr\n" + err);
            Log.d(TAG, err);
            Log.d(TAG, "No server to connect to!\nERR:" + uHe.getMessage());
            SaveLogLine("\nNo server to connect to!\nERR:" + uHe.getMessage());
        }



    }





    public void SendMessagePossible(){
        try{


            if(mSVRConnection.biSConnected){
                radButtonSvrConnected.setChecked(true);
                //radButtonSvrConnected.setButtonDrawable(R.drawable.serverbtn_ok150);
                btnSendTestMessage.setEnabled(true);
                btnSendTestMessage.setVisibility(View.VISIBLE);
                bSendMessagePossible=true;
                btnSendTestMessage.setTextColor(Color.WHITE);

            }else {
                bSendMessagePossible=false;
                btnSendTestMessage.setBackgroundColor(Color.GRAY);
                radButtonSvrConnected.setChecked(false);
                //radButtonSvrConnected.setButtonDrawable(R.drawable.serverbtn_nok);;
                btnSendTestMessage.setEnabled(false);
            }
        }catch(Exception e){
            radButtonSvrConnected.setChecked(false);
            //radButtonSvrConnected.setButtonDrawable(R.drawable.serverbtn_nok);

        }


    }

    public void SaveLogLine(String strLine){
        Log.d(TAG,strLine);

        return;
    }

    public void CloseServerConenction(){
        try {
            mSVRConnection.tearDown();
        }catch (Exception e){
            e.printStackTrace();
        }

        return;
    }

    //getStoredCFG();

    public void SendTestString(){

        String dataaToSend = "<PF><TERM>1130011468</TERM><FLAG>T062NNN</FLAG><PURSE>P1</PURSE><SER0>15E181AE</SER0><SER1></SER1><ID0>00033</ID0><ID1></ID1><LP>00000000000000</LP><DB>|197.242.146.179|payfare|mautran|payfare|p4yf4r3|</DB><DATA>00152609210715000000000000250000000000000000000000250000131843</DATA></PF>";

        EditText messageView = this.findViewById(R.id.ed_Test_Message);
        if (messageView != null) {
            String messageString = messageView.getText().toString();
            if (!messageString.isEmpty()) {
                mSVRConnection.sendMessage(dataaToSend);
            }
            //messageView.setText("");
        }

    }

    public void addServerResponseLine(String line) {
        //This function gets data recived from Server and adds it to the line
        ed_Test_Server_Response.append("\n" + line);
    }

    @Override
    protected void onPause() {
        //if (mNsdHelper != null) {
        //   mNsdHelper.stopDiscovery();
        // }
        SaveCurrentServerSettings();
        CloseServerConenction();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ReadServerSavedDefaultSettings();

        //ConnectToServer();
        //if (mNsdHelper != null) {
        //  mNsdHelper.registerService(mConnection.getLocalPort());
        //mNsdHelper.discoverServices();
        // }
    }

    @Override
    protected void onDestroy() {
        // mNsdHelper.tearDown();

        CloseServerConenction();

        super.onDestroy();
    }

    public class ConnectedTimer extends CountDownTimer {
        // private Bundle args = new Bundle();
        private Bundle args = new Bundle();
        public ConnectedTimer (long startTime, long interval) {

            super(startTime, interval);
        }

        @Override
        public void onFinish() {


            radButtonSvrConnected.setBackground(Drawable.createFromPath("serverbtn_nok"));
        }

        @Override
        public void onTick(long millisUntilFinished) {
            if(mSVRConnection.biSConnected){
                radButtonSvrConnected.setBackground(Drawable.createFromPath("serverbtn_ok"));
            }else {
                radButtonSvrConnected.setBackground(Drawable.createFromPath("serverbtn_nok"));
            }

        }


    }
}