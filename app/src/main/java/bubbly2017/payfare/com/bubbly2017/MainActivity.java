package bubbly2017.payfare.com.bubbly2017;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.net.nsd.NsdServiceInfo;
import android.net.wifi.WifiManager;
import android.nfc.tech.MifareClassic;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.print.PrinterCapabilitiesInfo;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.text.InputFilter;
import android.text.InputType;
import android.text.SpannableStringBuilder;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;

import android.widget.NumberPicker.OnValueChangeListener;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import java.lang.reflect.Field;
import com.nxp.nfclib.classic.MFClassic;
import com.nxp.nfcliblite.Interface.NxpNfcLibLite;
import com.nxp.nfcliblite.Interface.Nxpnfcliblitecallback;

import java.io.File;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.os.Build.getSerial;
import static bubbly2017.payfare.com.bubbly2017.ServerSetup.Default_IP;
import static bubbly2017.payfare.com.bubbly2017.ServerSetup.Default_PRT;
import static bubbly2017.payfare.com.bubbly2017.ServerSetup.Default_Server_Timeout;

import static java.lang.System.err;


public class MainActivity extends Activity {

    NumberPicker np;
    TextView tv1, tv2;
    public String strSaleValuePicked;
    public static int iSaleValuePicked;
    Button btnGoSale;
    private static final int isCancelButton=0;
    public static boolean PayTimerIsActive = false;
    static paytimer pmtTmr = new paytimer();

    private static int merchantDisplayMaxLength=16;
    public static ImageView imgPayWave;

    public static TextView tvCount;
    public static String strAdminPwd="";
    final String DefaultAdminPwd = "2007";
    public static NxpNfcLibLite libInstance = null;
    public myNXP MyNXP ;
    public myFile_Logger Log_File = new myFile_Logger();
    public File logFileDir;
    public String filePath="";
    public Intent mIntent;
    private MFClassic classic;

    public static boolean bCardConnected=false;
    public static final String  TAG = "Payfare main activity";
    public static boolean ForeGroundDispatchisStarted=false;
    public static boolean bPaymentComplete = false;
    public static boolean bNewIntentFound = false;
    public static String mCurCardUID;
    public static String mCurrentKeyB;
    public static String strResult;
    public static boolean AuthresGood = false;
    public boolean bIsConnected;
    public static String striFinalIp;
    public static String strCurrPort,strCurTimout;
    public String strServerDataLine;

    public static String Cardbal = "";
    public static int iCardBal=0;
    public boolean bUserSelectedOk=false;
    public static boolean bIsmanagerMode=false;
    public static boolean bIsActiveSaleRequest=false;
    public static boolean bPayTimerIsActive=false;

    public static final String MerchTotalSales = "MERCHANT_TOTAL_SALES";
    public static final String MerchDisplay = "MERCHANT_DISP";
    public static final String MerchConfig = "MERCHANT_Config";
    public static final String MerchName = "MERCHANT_NAME";
    public static final String MerchConfStatus= "MERCHANT_CONFSTAT";

    public static String strCurPosTotalSales="";
    public static int   iCurPosTotalSales=0;
    public String strMFSectorReadREsult="";
    public static boolean isGoodSale=false;
    public static String strCurCardSN="";

    public myFile_Logger myFileLogger = new myFile_Logger();
    public static String sHost = "11.10.10.2";
    public static String sSchema ="bubbly2017";

    public static String configDb = "|" + sHost + "|bubbly2017|termsetup|payfare|p4yf4r3|";   //Changes made here from |11.10.10.2|payfare|termsetup|payfare|p4yf4r3| to... |11.10.10.2|bubbly2017|termsetup|payfare|p4yf4r3|

    public static String transDb = "|" + sHost + "|bubbly2017|tran|payfare|p4yf4r3|";
    public static String strMerchantName="";
    public static String strMerchantDisplay="";
    public static String strTERMSN="";
    public static boolean bIsFirstTimeConfig=true;
    public static String strTempMerchantID="";
    public static final String strTransDateTimeformat = "hhmmssyyMMdd";
    public static final String strTransDatabaseTablepostFixDateformat = "yyyyMMdd";
    TextView tvMerchantDisplay;
    NsdHelper mNsdHelper;
    private Handler mUpdateHandler;
    public boolean bSendMessagePossible=false;
    public static String strCurServerResponse="";

    public static boolean bSvrUploadRequest=false;

    public static String strUniquMerchantID="";
    public static boolean bValidUniqueID = false;

    public static boolean bGetConfigMsgSent=false;
    public static int iTimeOutVal=0;
    public static boolean bSvrMsgtimerIsActive=false;


    public static final int  iBubblyRecordSize=303;
    public static boolean bSvrMessageIsConfigRequestSent=false;
    public static String strDBDataOut="";

    public static boolean bIsFirstConnection=true;

    ChatConnection mSVRConnection;

    public BubblyDBHandler db;

    public boolean isLastMessageSentGood=false;
    public boolean isMessageUpLoadType=false;
    public String picURI="";
    static List<String> trxDataReceived = new ArrayList<String>();

    public ResetDisplayTask rstDispTask;// = new ResetDisplayTask();
    public boolean bResetFlagCancelled=false;


    public static   List<String> strTRXUploadList = new ArrayList<String>();
    public static  ArrayList<String> struploadXMLList= new ArrayList<String>();
    public static  ArrayList<String> strUploadID = new ArrayList<String>();


    public static Map transactionResultsMap = new HashMap();
    public int iNumberMSGSent=0;
    public int iNumberRSPReived=0;
    public static boolean bIsFinishedSending=false;
    int trxUploadFailCount=0;
    ProgressBar progb;
    int progbarFinalValue=0;
    long uploadConfirmedCount=0;
    long uploadSentCount=0;
    public static boolean bISConfigRequest=false;
    public static boolean bIsNougatAndroid=false;
    public static boolean bisHuaweiDevice=false;
    private int requestCode;
    private int grantResults[];
    public static int termSnMaxLength=10;
    public static String strAppversion;

    @SuppressLint("HandlerLeak")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        bIsConnected=false;

        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            strAppversion = String.valueOf(pInfo.versionCode);
            strAppversion =  "r"+ strAppversion  + "."+ pInfo.versionName;


            getActionBar().setTitle("Bubbly 2017 - " + strAppversion);




        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }



        libInstance = NxpNfcLibLite.getInstance();
        libInstance.registerActivity(this);


        //libInstance.stopForeGroundDispatch();
        np = findViewById(R.id.numberPicker1);
        //tv1 = (TextView) findViewById(R.id.textView2);
        tv2 = findViewById(R.id.textView3);
        btnGoSale = findViewById(R.id.btnGoSale);
        tvMerchantDisplay = findViewById(R.id.tvMerchantDisplay);
        imgPayWave = findViewById(R.id.imageView1);
        tvCount = findViewById(R.id.tvCount);
        progb = findViewById(R.id.progressBar);
        progb.setVisibility(View.INVISIBLE);
        progb.getProgressDrawable().setColorFilter(
                Color.RED, android.graphics.PorterDuff.Mode.SRC_IN);

        setNumberPickerTextColorAndSize(np,R.color.fullblack, 25.0f);
        np.setMinValue(0);
        np.setMaxValue(10);
        np.setWrapSelectorWheel(false);

        //setNumberPickerTextColorAndSize(np,R.color.SBSA_white, 45.0f);
        //np.setDividerColor(ContextCompat.getColor(this, R.color.colorPrimary));


        np.setOnValueChangedListener(new OnValueChangeListener() {

            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

               // String Old = "Previous Value : ";
                String New = "Current Sale Value : ";


                setNumberPickerTextColorAndSize(np,R.color.fullblack, 25.0f);
               // iSaleValuePicked= newVal;
               // tv1.setText(Old.concat(String.valueOf(oldVal)));
                tv2.setText(New.concat(String.valueOf(newVal)));

                SetCurrentlibInterface(false);//disable nfc reader
                bIsActiveSaleRequest = iSaleValuePicked > 0;
                imgPayWave.setVisibility(View.INVISIBLE);
                tvCount.setVisibility(View.INVISIBLE);

                picURI = "@drawable/paywave_transparent";
                setIconPicture(picURI);
                if(bPayTimerIsActive)
                    pmtTmr.ExitTmr();

            }
        });

        np.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               // showToast(String.valueOf(iSaleValuePicked));
                SetCurrentlibInterface(false);//disable nfc reader
                setNumberPickerTextColorAndSize(np,R.color.fullblack, 25.0f);
                picURI = "@drawable/paywave_transparent";
                setIconPicture(picURI);
                bIsActiveSaleRequest = iSaleValuePicked > 0;
                if(bPayTimerIsActive)
                    pmtTmr.ExitTmr();
                //iSaleValuePicked= newVal;
            }
        });


        np.setOnValueChangedListener(new OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                String New = "Current Sale Value : ";
                //showToast(String.valueOf(iSaleValuePicked));
                tv2.setText(New.concat(String.valueOf(i1)));
                iSaleValuePicked= i1;
                imgPayWave.setVisibility(View.INVISIBLE);
                tvCount.setVisibility(View.INVISIBLE);
                SetCurrentlibInterface(false);//disable nfc reader
                setNumberPickerTextColorAndSize(np,R.color.fullblack, 25.0f);

                picURI = "@drawable/paywave_transparent";
                setIconPicture(picURI);
                if(iSaleValuePicked>0){
                    btnGoSale.setText("Go Sale!");
                    bIsActiveSaleRequest=true;
                }else{
                    btnGoSale.setText("Balance Enquiry");
                    bIsActiveSaleRequest=false;



                }
                if(bPayTimerIsActive)
                    pmtTmr.ExitTmr();

            }
        });

        //np.getSolidColor(R.color.fullblack);


        btnGoSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                picURI = "@drawable/paywave_transparent";
                setIconPicture(picURI);
                imgPayWave.setVisibility(View.INVISIBLE);
                tvCount.setVisibility(View.INVISIBLE);

                bIsActiveSaleRequest = iSaleValuePicked > 0;

                SetCurrentlibInterface(true);//enable nfc reader
                pmtTmr.TmrStart();
                imgPayWave.setVisibility(View.VISIBLE);
                tvCount.setVisibility(View.VISIBLE);




            }
        });


        getDeviceHardwareDetails();

         strTERMSN = getDeviceSN();

         //this line below is to build dummy transaction to store
        //buildTansactionXMLandStore(strTERMSN,"AABBCCDD",1,19);



        getStoredCFG();
        formatMerchantDisplay(strMerchantDisplay);


        iCurPosTotalSales = ReadMerchantTotalSaleCounter();

       // myFileLogger.CreateTransactionFile("BubblyTransaActions.txt",this);
        //myFileLogger.CreateInternalTransactionfile("BubblyTransaActions.txt",this);

        //SetCurrentlibInterface(false);
        bIsmanagerMode=false;
        //******************************************************************************************//
        /// Receiving thread data from server client will be passed back from received
        /// thread to below chatline
       // public static boolean bSvrMessageIsConfigRequest=false;
        //public static boolean bSvrMessageIsTxUploadRequest=false;

        ///*****************************************************************************************//

        mUpdateHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                strServerDataLine = msg.getData().getString("msg");


                if(!bSendMessagePossible){SendMessagePossible();}


                addDBServerResponseLine(strServerDataLine);
                if(isMessageUpLoadType && strServerDataLine.contains("<RESP>") && strServerDataLine.contains("</RESP>")){


                    if(strServerDataLine.trim().length()== 19 ) {
                        if(strServerDataLine.trim().contains("OK")){

                            db.updateTransactionUploadStatus(Integer.parseInt(strServerDataLine.substring(8, 12)), "T");
                            uploadConfirmedCount++;
                        }
                        iNumberRSPReived++;
                    }else{
                        transactionResultsMap.put(strServerDataLine.substring(8, 12), strServerDataLine.substring(6, 8));
                        iNumberRSPReived++;

                    }

                }

                if(bISConfigRequest && strServerDataLine.contains("<RESP><DATA>")){

                    parseGetCONFIGData(strServerDataLine);
                }


                //showToast(mSVRConnection.biSConnected +"\n" + strServerDataLine);
                bSendMessagePossible = mSVRConnection.biSConnected && strServerDataLine.contains(">") && strServerDataLine.trim().length() < 2;


                //Log.d("BUBBLY", "\n\n\n\n\n"+  mSVRConnection.biSConnected + "\n\n\n\n\n"+ strServerDataLine);



            }


        };


        mSVRConnection = new ChatConnection(mUpdateHandler);

        mNsdHelper = new NsdHelper(this);
        mNsdHelper.initializeNsd();

        db = new BubblyDBHandler(this);
        db.setFinalDBTableName(buildDBTableNamePostfix());// currently Table Dated Postfix Is disabled


        try {
            boolean dailyTableExist = db.isTableExists(db.getWritableDatabase(),true);
            if(!dailyTableExist){
                db.onCreate(db.getWritableDatabase());
            }

        }catch(android.database.sqlite.SQLiteException aie){
            showBIGToast(aie.getMessage());
            aie.printStackTrace();
        }
        catch (Exception ie){

        }

        //db.onUpgrade(db.getWritableDatabase(),1,2);
        //db.onUpgrade(db.,1,2);

        picURI = "@drawable/paywave_transparent";
        setIconPicture(picURI);



        if(bIsNougatAndroid==true || bisHuaweiDevice==true){

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,},requestCode);
            onRequestPermissionsResult(requestCode,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},grantResults);

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_WIFI_STATE},requestCode);
            onRequestPermissionsResult(requestCode,new String[]{Manifest.permission.ACCESS_WIFI_STATE},grantResults);

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE},requestCode);
            onRequestPermissionsResult(requestCode,new String[]{Manifest.permission.READ_PHONE_STATE},grantResults);

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_BOOT_COMPLETED},requestCode);
            onRequestPermissionsResult(requestCode,new String[]{Manifest.permission.RECEIVE_BOOT_COMPLETED},grantResults);

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},requestCode);
            onRequestPermissionsResult(requestCode,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},grantResults);

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.VIBRATE},requestCode);
            onRequestPermissionsResult(requestCode,new String[]{Manifest.permission.VIBRATE},grantResults);

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_NETWORK_STATE},requestCode);
            onRequestPermissionsResult(requestCode,new String[]{Manifest.permission.ACCESS_NETWORK_STATE},grantResults);

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET},requestCode);
            onRequestPermissionsResult(requestCode,new String[]{Manifest.permission.INTERNET},grantResults);

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH},requestCode);
            onRequestPermissionsResult(requestCode,new String[]{Manifest.permission.BLUETOOTH},grantResults);

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},requestCode);
            onRequestPermissionsResult(requestCode,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},grantResults);

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},requestCode);
            onRequestPermissionsResult(requestCode,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},grantResults);
            //ACCESS_FINE_LOCATION or ACCESS_COARSE_LOCATION



        }

        CreateDailyTransLogFile();

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }





    @Override // android recommended class to handle permissions
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Log.d("permission", "granted");
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.uujm
                    Toast.makeText(MainActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();

                    //app cannot function without this permission for now so close it...
                    onDestroy();
                }
                return;
            }

            // other 'case' line to check fosr other
            // permissions this app might request
        }

    }


    public void setIconPicture(String strPictureName){

       // String uri = "@drawable/check";


        int ress = getResources().getIdentifier(strPictureName, null, getPackageName());
        Drawable res = getResources().getDrawable(ress);
        imgPayWave.setImageDrawable(res);
    }



    public void formatMerchantDisplay(String strDisp){
        String tmp;
        if(strDisp.length()>merchantDisplayMaxLength){
            tmp  = strDisp.substring(0,merchantDisplayMaxLength) ;//+ "\n" + strDisp.substring(12,strDisp.length()-1);

            tvMerchantDisplay.setText(tmp);
        }
        else{
            tmp = strDisp;
            tvMerchantDisplay.setText(tmp);
        }



    }


    public void SendMessagePossible(){
        try{


            //radButtonSvrConnected.setButtonDrawable(R.drawab
            bSendMessagePossible = mSVRConnection.biSConnected;
        }catch(Exception e){
            bSendMessagePossible=false;

        }


    }

    public String getDeviceSN(){

        String res="";
        if(bisHuaweiDevice||bIsNougatAndroid){

            try {
                List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
                for (NetworkInterface nif : all) {
                    if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                    byte[] macBytes = nif.getHardwareAddress();
                    if (macBytes == null) {
                        return "";
                    }

                    StringBuilder res1 = new StringBuilder();
                    for (byte b : macBytes) {
                        //res1.append(Integer.toHexString(b & 0xFF) + ":");
                        res1.append(Integer.toHexString(b & 0xFF));
                    }

                    if (res1.length() > 0) {
                        res1.deleteCharAt(res1.length() - 1);
                    }
                    if(res1.length()<10)
                      res = String.format("%" + 10 + "s",res1.toString()).replace(" ","F");
                    else
                        res = res1.toString();
                    return res;
                }
            } catch (Exception ex) {
                //handle exception
                Log.d("BUBBLY","Get Device serial number err:" + ex.getMessage());
                showToast("Get Device serial number err:" + ex.getMessage());
                ex.printStackTrace();
            }


            return "";
        }else {
            try {
                String serialNumber = "";

                WifiManager wm = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                serialNumber = wm.getConnectionInfo().getMacAddress();

                if(serialNumber.length()<10)
                    res = String.format("%F10",serialNumber);
                else
                    res = serialNumber;


                return serialNumber;

            } catch (Exception ie) {
                Log.d("BUBBLY","Get Device serial number err:" + ie.getMessage());

                showToast(ie.getMessage());

            }

            return "";
        }
    }


    public static boolean setNumberPickerTextColorAndSize(NumberPicker numberPicker, int color,float size)
    {
        final int count = numberPicker.getChildCount();
        for(int i = 0; i < count; i++){
            View child = numberPicker.getChildAt(i);
            if(child instanceof EditText){
                try{
                    Field selectorWheelPaintField = numberPicker.getClass()
                            .getDeclaredField("mSelectorWheelPaint");
                    selectorWheelPaintField.setAccessible(true);
                    ((Paint)selectorWheelPaintField.get(numberPicker)).setColor(color);
                    ((EditText)child).setTextColor(color);
                    ((EditText)child).setTextSize(size);
                    numberPicker.invalidate();
                    return true;
                }
                catch(NoSuchFieldException e){
                    Log.w("setNumPickerTxtClr", e);
                }
                catch(IllegalAccessException e){
                    Log.w("setNumPickerTxtClr", e);
                }
                catch(IllegalArgumentException e){
                    Log.w("setNumPickerTxtClr", e);
                }
            }
        }
        return false;
    }
    @Override
    public void onPause(){
        super.onPause();
        iNumberMSGSent=0;
       iNumberRSPReived=0;
       trxUploadFailCount=0;
        bISConfigRequest=false;
        progbarFinalValue=0;
        uploadConfirmedCount=0;
        uploadSentCount=0;

        progb.setMax(0);
        progb.setVisibility(View.INVISIBLE);
        getStoredCFG();
        bIsActiveSaleRequest=false;
        SetCurrentlibInterface(false);
        bIsmanagerMode=false;
      //  ResetDisplay();

    }
    @Override
    public void onResume(){
        super.onResume();
        iNumberMSGSent=0;
        iNumberRSPReived=0;
        trxUploadFailCount=0;
        bISConfigRequest=false;
        progbarFinalValue=0;
        uploadConfirmedCount=0;
        uploadSentCount=0;

        progb.setMax(0);
        progb.setVisibility(View.INVISIBLE);

        getStoredCFG();
        bIsActiveSaleRequest=false;
        bIsmanagerMode=false;
       // ResetDisplay();
      SetCurrentlibInterface(false);

    }

    @Override
    public void onBackPressed() {
    }




    public void SetCurrentlibInterface(boolean bStartStopForeGroundDispatch){

        try {
            if (bStartStopForeGroundDispatch) {
                if (ForeGroundDispatchisStarted == false) {
                    Log.d(TAG,"Payfare-Starting Foreground Dispatch");
                    libInstance.startForeGroundDispatch();
                    ForeGroundDispatchisStarted = true;
                    Log.d(TAG,"Payfare-Starting Foreground Dispatch done");
                }
            } else {
                if (ForeGroundDispatchisStarted == true) {
                    Log.d(TAG,"Payfare-Stopping Foreground Dispatch");
                    libInstance.stopForeGroundDispatch();
                    ForeGroundDispatchisStarted = false;
                    Log.d(TAG,"Payfare-Stopping Foreground Dispatch Done");
                }
            }
        }catch (Exception e){
            Log.d(TAG,"tried to Start or Stop ForeGroundDispatch, but failed");
        }
    }

    public void showToast(String msgToShow){


        Context context = getApplicationContext();
        CharSequence text = msgToShow;
        int duration = Toast.LENGTH_LONG;

        Toast toast = Toast.makeText(context, text, duration);
        toast.setGravity(Gravity.CENTER_VERTICAL| Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.show();

    }

    public void showBIGToast(String msgToShow){

        SpannableStringBuilder biggerText = new SpannableStringBuilder(msgToShow);
        biggerText.setSpan(new RelativeSizeSpan(2.35f), 0, msgToShow.length(), 0);
        Context context = getApplicationContext();
        CharSequence text = msgToShow;
        int duration = Toast.LENGTH_LONG;

        for(int i=0;i<2;i++) {

            Toast toast = Toast.makeText(context, biggerText, duration);
            toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
        }

    }
    public void showMEDLongToast(String msgToShow){

        SpannableStringBuilder biggerText = new SpannableStringBuilder(msgToShow);
        biggerText.setSpan(new RelativeSizeSpan(1.35f), 0, msgToShow.length(), 0);
        Context context = getApplicationContext();
        CharSequence text = msgToShow;
        int duration = Toast.LENGTH_LONG;

        for(int i=0;i<5;i++) {

            Toast toast = Toast.makeText(context, biggerText, duration);
            toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
        }

    }

    public void showBIGLONGToast(String msgToShow){

        SpannableStringBuilder biggerText = new SpannableStringBuilder(msgToShow);
        biggerText.setSpan(new RelativeSizeSpan(2.35f), 0, msgToShow.length(), 0);
        Context context = getApplicationContext();
        CharSequence text = msgToShow;
        int duration = Toast.LENGTH_LONG;

        for(int i=0;i<4;i++) {

            Toast toast = Toast.makeText(context, biggerText, duration);
            toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
        }

    }





    public void doUpLoadTransx(){



        //First Simulate Transaction log fole update    public boolean UpDateTransActionLine(int offset,int iLenToWrite,String strDatain){

        String [] trxSplit;
        String strserverFinalXML="";
        int trxUploadFailCount=0;

        if(!bIsmanagerMode)
            return;
        else
            bIsmanagerMode=false;

        try {

            transactionResultsMap.clear();

            db.setFinalDBTableName(buildDBTableNamePostfix()); // currently Table Dated Postfix Is disabled


            //if(db.)
            int iNoRecordsToUpdate=0;
            if(db.geTrxCount()>0) {

                strTRXUploadList = db.getTransActionsToUpload();
                iNoRecordsToUpdate =strTRXUploadList.size();
                if(iNoRecordsToUpdate<1){
                    showBIGToast("Uploads done\nNothing to Upload");
                    return;
                }
            }else{
                showBIGToast("Nothing to Upload");
                return;
            }


            String logtx = "";
            //if(trxDataReceived.size()>0)
             //   trxDataReceived.clear();

            for (String trx : strTRXUploadList) {

                trxSplit = trx.split(",");
                logtx  = "\n************************************************************************************************************\n";
                logtx += "\nID:" + trxSplit[0] + "  TRXDATA:" + trxSplit[1] + "  cardSN:" + trxSplit[2] + "  IS UPLOADED:" + trxSplit[3];
                logtx += "\n************************************************************************************************************\n";
                Log.d("BUBBLY",logtx);


                getStoredCFG();

                if(trxSplit[3].trim().contentEquals("F")){
                    strserverFinalXML = buildTrxXMLStrForUploadfromSQLDB( trxSplit[2], trxSplit[1],String.format("%04d",Integer.parseInt(trxSplit[0])));
                    struploadXMLList.add(strserverFinalXML);
                    strUploadID.add(String.format("%04d",Integer.parseInt(trxSplit[0])));
                    Log.d("BUBBLY",strserverFinalXML);

                }


                 if(!mSVRConnection.biSConnected) {
                     ConnectToDBServer(false);
                     try{
                         Thread.sleep(100);
                     }catch (Exception i){
                         i.printStackTrace();
                     }

                     if(!mSVRConnection.biSConnected){
                         showMEDLongToast("Can not Connect to server\nCheck app wifi settings page &\nDevice is conncted to Bubbly wifi");


                         return;

                     }
                 }
                    isMessageUpLoadType = true;
                isLastMessageSentGood = false;

                   // SendDBMessage(strserverFinalXML);

                            //  db.updateTransactionUploadStatus(Integer.parseInt(trxSplit[0]), "T");
                        //strServerDataLine = "";
                        //isLastMessageRespGood = true;




            }

            progb.setMax(iNoRecordsToUpdate*2);
            progb.setVisibility(View.VISIBLE);

            uploadSentCount=0;
            uploadConfirmedCount=0;
            new RequestTransxUploadsTask().execute(struploadXMLList);
           // new ProcessUploadStatusesTask().execute(strUploadID);



        }catch(android.database.sqlite.SQLiteException aSQLe){
            showBIGToast(aSQLe.getMessage());
            aSQLe.printStackTrace();
        }
            catch (Exception e){
            e.printStackTrace();
        }







    }

    public void getPOSDBConfig(){


        String strFinalSNToSend="";

        if(!bIsmanagerMode)
            return;
        else
            bIsmanagerMode=false;

        if(bIsFirstTimeConfig && strUniquMerchantID.length()==3)
            strFinalSNToSend = strUniquMerchantID + getDeviceSN().replace(":","").substring(0,10);
        else{
            strFinalSNToSend =  getDeviceSN().replace(":","").substring(0,10);
        }

        String strDataOut = buildGetConfigRequestXMLToSend(strFinalSNToSend);
        strDBDataOut="";
        strDBDataOut =  strDataOut;

        if(!mSVRConnection.biSConnected)
            ConnectToDBServer(true);

        try{
            Thread.sleep(200);

        }catch (Exception ie){
            ie.printStackTrace();
        }

        if(!mSVRConnection.biSConnected)
        {
            showMEDLongToast("Can not Connect to server\nCheck app wifi settings page &\nDevice is conncted to Bubbly wifi");
            return;
        }
        bISConfigRequest=true;
        try {
            Thread.sleep(100);
            Log.d("BUBBLY","\n**********\n" + strDataOut.toString());

            new RequestConfigTask().execute(strDataOut);

        }catch (Exception ie){

            ie.printStackTrace();
        }

    }


    public void showAlert( ) {
        new AlertDialog.Builder(this)
                .setTitle("MessageDemo")
                .setMessage("Let's raise a toast!")

                .setNeutralButton("Hear, hear!", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dlg, int sumthin) {
                        Toast
                                .makeText(MainActivity.this, "<clink, clink>",
                                        Toast.LENGTH_SHORT)
                                .show();
                    }
                })
                .show();
    }




    public void  ConnectToDBServer(boolean isConfigRequest ){

        getStoredCFG();
        //PrepareForServerConnection();
        String err;
      //  NsdServiceInfo service = mNsdHelper.getChosenServiceInfo();
       // SaveLogLine("\nConnect Click Start:" + service);
        try {
            InetAddress inetIP = InetAddress.getByName(striFinalIp);
            Integer iCurPort = Integer.parseInt(strCurrPort);

            Log.d("SVR", "Connecting." + striFinalIp + "  - " + strCurrPort );



            mSVRConnection.bIsConfRequest=isConfigRequest;
            mSVRConnection.connectToServer(inetIP,iCurPort);

            if(mSVRConnection.biSConnected){
                Log.d("SVR", "Connected.");

               // SvrMsgTmr.TmrStart();
                bSendMessagePossible=true;
                return;

            }
        }catch(UnknownHostException uHe)
        {

            err = uHe.getMessage() + "\n" + "Err Code:" + uHe.toString();
            addDBServerResponseLine("CON_ERR\n" + err);
            showToast(uHe.getMessage());
            Log.d(TAG, err);
            Log.d(TAG, "No server to connect to!\nERR:" + uHe.getMessage());
            //SaveLogLine("\nNo server to connect to!\nERR:" + uHe.getMessage());
            bSendMessagePossible=false;
        }
        catch (Exception e){

            err = e.getMessage() + "\n" + "Err Code:" + e.toString();
           showToast(err);
            Log.d(TAG, err);
            Log.d(TAG, "No server to connect to!\nERR:" + e.getMessage());
            bSendMessagePossible=false;

        }
        bSendMessagePossible=false;



    }
    public void addDBServerResponseLine(String line) {
        //This function gets data recived from Server and adds it to the line
        strCurServerResponse = line;
        trxDataReceived.add(line);
        Log.d("BUBBLY","\n\n\n\nSVR TCP MSG:" + line + "\n\n\n\n");
        Log.d("BUBBLY","\n\n\n\nSVR trxRved:" + String.valueOf(trxDataReceived.size()) + "\n\n\n\n");

    }


    public void SendDBMessage(String strDataToSend){


        try {

                if (!strDataToSend.isEmpty()) {
                    mSVRConnection.sendMessage(strDataToSend.toString());
                    isLastMessageSentGood=true;

            }
        }catch (Exception ie){
            Log.d("BUBBLY",ie.getMessage());
            showToast(ie.getMessage());
            ie.printStackTrace();
        }

    }

    public void parseGetCONFIGData(String strDatain){
        //Sample Data:
        /*<DATA>0027000100012016NPTMDNNE00</DATA>
        <HOST>11.10.10.2</HOST>
        <SCHEMA>bubbly2017</SCHEMA>
        <TABLE></TABLE>
        <DISP>BLACK ELEPHANT VINTNERS</DISP>
        <CLIENT>bubbly2017</CLIENT>
        <MERCH>Black Elephant Vintners</MERCH>*/

        try {
            showBIGToast("POS UPDATE RECVD");
            strMerchantDisplay = strDatain.substring(strDatain.indexOf("<DISP>")+6,strDatain.indexOf("</DISP>"));
            formatMerchantDisplay(strMerchantDisplay);
            strMerchantName= strDatain.substring(strDatain.indexOf("<MERCH>")+7,strDatain.indexOf("</MERCH>"));
            setStoredCFg(strMerchantDisplay,strMerchantName);
            bISConfigRequest=false;
           // mSVRConnection.tearDown();// close svr conn
            bIsmanagerMode=false;

        }catch (Exception ie){
           showToast( ie.getMessage());
            bIsmanagerMode=false;
            bISConfigRequest=false;
           // mSVRConnection.tearDown();// close svr conn
            bIsmanagerMode=false;
        }



    }


    public void getStoredCFG(){

        strMerchantDisplay="";
        strMerchantName="";
        bIsFirstTimeConfig=true;
            Context context = getApplicationContext();
            SharedPreferences sharedPref = context.getSharedPreferences(MerchConfig, Context.MODE_PRIVATE);

        SharedPreferences sharedPrefSVR = context.getSharedPreferences(
                getString(R.string.PayfareServerDefaults), Context.MODE_PRIVATE);


            strMerchantDisplay =sharedPref.getString(MerchDisplay,"Sales Merchant");
            strMerchantName =sharedPref.getString(MerchName,"Sales Merchant Name" );
            bIsFirstTimeConfig  = sharedPref.getBoolean(MerchConfStatus,true);
            striFinalIp =sharedPrefSVR.getString(Default_IP,sHost);
            if(striFinalIp.substring(0,2).contentEquals("10")){

                striFinalIp="11.10.10.2";
            }
            strCurrPort = sharedPrefSVR.getString(Default_PRT,getString(R.string.default_port));
            strCurTimout = sharedPrefSVR.getString(Default_Server_Timeout,getString(R.string.Server_Default_Timeout));
            sHost=striFinalIp;
            configDb = "|" + sHost + "|bubbly2017|termsetup|payfare|p4yf4r3|";   //Changes made here from |11.10.10.2|payfare|termsetup|payfare|p4yf4r3| to... |11.10.10.2|bubbly2017|termsetup|payfare|p4yf4r3|
            transDb = "|" + sHost + "|bubbly2017|tran|payfare|p4yf4r3|";




    }


    public boolean setStoredCFg(String strMerchDisp,String strMerchName){

        Context context = getApplicationContext();
        SharedPreferences sharedPref = context.getSharedPreferences(
                MerchConfig, Context.MODE_PRIVATE);

        // SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString( MerchDisplay, strMerchDisp);
        editor.putString( MerchName, strMerchName);
        editor.putBoolean(MerchConfStatus,false);
        bIsFirstTimeConfig=false;
        Log.d(TAG,MerchName + "-Merchant CFG Saved:" + strCurPosTotalSales);
        editor.commit();
        return true;

    }




    public boolean GetManagerPassword(final int iChoice){



        Context context = getApplicationContext();
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);


        int maxLength = 4;
        int idMaxlen=3;
         // DefaultAdminPwd = "2007";
        strAdminPwd="";

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD|InputType.TYPE_NUMBER_FLAG_DECIMAL|InputType.TYPE_CLASS_NUMBER);
        input.setFilters(new InputFilter[] {new InputFilter.LengthFilter(maxLength)});
       // input.setHint("Manger password");

        final EditText uniUQeId = new EditText(this);
        uniUQeId.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL|InputType.TYPE_CLASS_NUMBER);
        uniUQeId.setFilters(new InputFilter[] {new InputFilter.LengthFilter(idMaxlen)});
        uniUQeId.setHint("Please enter 3 digit Merchant ID");

        switch (iChoice){
            case 0:
                input.setHint("Enter Manager Password for wifi page:");
                break;
            case 1:
                input.setHint("Enter Manager Password to clear totals:");
                break;
            case 2:
                input.setHint("Enter Manager Password to get config:");
                break;
            case 3:
                input.setHint("Enter Manager Password to exit app:");
                break;
            case 4:
                input.setHint("Enter Manager password to upLoad:");
            default:
                input.setHint("Please enter a Manager Password:");
                break;
        }

        layout.addView(input);
        if(iChoice==2&& bIsFirstTimeConfig){
            layout.addView(uniUQeId);
        }



        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setCancelable(false);


        b.setView(layout);

        b.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                strAdminPwd = input.getText().toString();

                if(strAdminPwd.length()>0 &&  strAdminPwd.trim().contentEquals(DefaultAdminPwd) ) {
                    bIsmanagerMode = true;
                    switch (iChoice){
                        case 0:
                            openWifiPage();
                            break;
                        case 1:
                            ClearMerchantTotalSales();
                            break;
                        case 2:
                            strUniquMerchantID = uniUQeId.getText().toString();
                            getPOSDBConfig();
                            break;
                        case 3:
                            ExitApp();
                        case 4:
                            doUpLoadTransx();
                        default:
                            break;
                    }


                }
                else {
                    bIsmanagerMode = false;

                }
            }
        });
        b.setNegativeButton("CANCEL", null);
        b.create();
        b.show();


        return bIsmanagerMode;


    }

    public void RequestOpenNetComms(){


        final CharSequence text = "Opening Wifi page now..";
        final int duration = Toast.LENGTH_LONG;


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.appwifiopenquestion)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        bUserSelectedOk = true;
                        openWifiPage();

                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        bUserSelectedOk=false;
                    }
                });
        // Create the AlertDialog object and return it
         builder.create();

         builder.show();
    }


    public void openWifiPage(){
        final Context context = getApplicationContext();



        if(bIsmanagerMode==true) {


                bIsmanagerMode=false;
            //Intent intent = new Intent(context, NsdChatActivity.class);
            Intent intent = new Intent(context, ServerSetup.class);
            startActivity(intent);
        }



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_UploadTransactions:
                GetManagerPassword(4);
                if(bIsmanagerMode) {doUpLoadTransx();}
                return true;
            case R.id.action_wifi_conf:
                GetManagerPassword(0);
                if(bIsmanagerMode) {openWifiPage();}
                return true;
            case R.id.action_getSaleTotals:
                ReadMerchantTotalSaleCounter();
                showBIGToast("Current Sale Total:" + String.valueOf(iCurPosTotalSales));
                return true;
            case R.id.action_ClearSaleTotals:
                GetManagerPassword(1);
                if(bIsmanagerMode) {ClearMerchantTotalSales();}
                return true;
            case R.id.action_GetConfig:
                GetManagerPassword(2);
                if(bIsmanagerMode) {getPOSDBConfig();}
                return true;
            case R.id.action_exit_app:
                GetManagerPassword(3);
                if(bIsmanagerMode) {ExitApp();}
                return true;
            case R.id.action_showTERMSN:
                showBIGLONGToast(getDeviceSN().trim().replace(":","").substring(0,termSnMaxLength));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void ExitApp(){
        if(!bIsmanagerMode)
            return;
        else
            bIsmanagerMode=false;

        System.exit(0);
    }


    public void ResetDisplay(){



            bPaymentComplete = false;
            imgPayWave.setVisibility(View.INVISIBLE);

            tvCount.setText("0");
            bIsActiveSaleRequest = false;
            iSaleValuePicked = 0;
        picURI = "@drawable/paywave_transparent";
        setIconPicture(picURI);
        imgPayWave.setVisibility(View.INVISIBLE);
         tvCount.setVisibility(View.INVISIBLE);
            btnGoSale.setText("Balance Enquiry");
            bIsActiveSaleRequest=false;
            imgPayWave.setVisibility(View.INVISIBLE);
            tvCount.setVisibility(View.INVISIBLE);
            iCardBal=0;
            Cardbal="";

            if(bPayTimerIsActive)
                pmtTmr.ExitTmr();
            isGoodSale=false;

           // rstDispTask.cancel(true);
            SetCurrentlibInterface(false);

    }


    public void UpdateMerchantTotalSaleCounter(int curenrSaleAmountDone){

            /*
            strCurPosTotalSales="";
                public static int   iCurPosTotalSales=0;
             */

        iCurPosTotalSales = ReadMerchantTotalSaleCounter();
        iCurPosTotalSales+=curenrSaleAmountDone;
        strCurPosTotalSales="";
        strCurPosTotalSales = Integer.toString(iCurPosTotalSales);

        Context context = getApplicationContext();
            SharedPreferences sharedPref = context.getSharedPreferences(
                    "MerchantSaleTotals", Context.MODE_PRIVATE);

            // SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();

            editor.putString( MerchTotalSales, strCurPosTotalSales);
            Log.d(TAG,MerchTotalSales + "-Saved:" + strCurPosTotalSales);
            editor.commit();
            return;



    }




    public void ClearMerchantTotalSales(){


        if(bIsmanagerMode ==true) {

                bIsmanagerMode=false;

            strCurPosTotalSales = "";
            strCurPosTotalSales = Integer.toString(iCurPosTotalSales);

            iCurPosTotalSales = 0;
            strCurPosTotalSales = "";
            try {
                Context context = getApplicationContext();
                SharedPreferences sharedPref = context.getSharedPreferences(
                        "MerchantSaleTotals", Context.MODE_PRIVATE);

                // SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();

                editor.putString(MerchTotalSales, "");
                Log.d(TAG, MerchTotalSales + "-Sales Cleared:" + "");
                editor.commit();

            } catch (Exception ie) {
                ie.printStackTrace();
            }

            ClearAllTransActionsFromFiles();

            try {
                db.setFinalDBTableName(buildDBTableNamePostfix());// currently Table Dated Postfix Is disabled

                db.onUpgrade(db.getWritableDatabase(), 1, 1);

            }catch (android.database.sqlite.SQLiteException ae){
                ae.printStackTrace();
                showToast(ae.getMessage());

            }catch (Exception ie){

                ie.printStackTrace();
            }
            iCurPosTotalSales = ReadMerchantTotalSaleCounter();
            if (iCurPosTotalSales == 0 && ClearAllTransActionsFromFiles()==true){
                CreateDailyTransLogFile();
                showBIGToast("Sales Cleared,\nTotal Sales:0");
            }



        }

    }

    public int ReadMerchantTotalSaleCounter(){

        strCurPosTotalSales="";

        Context context = getApplicationContext();
        SharedPreferences sharedPref = context.getSharedPreferences("MerchantSaleTotals", Context.MODE_PRIVATE);
        strCurPosTotalSales =sharedPref.getString(MerchTotalSales,"0");

        if(strCurPosTotalSales.length()<=0 || strCurPosTotalSales.contentEquals("0"))
            iCurPosTotalSales=0;
        else
            iCurPosTotalSales = Integer.parseInt(strCurPosTotalSales);


        return iCurPosTotalSales;


    }



    @Override
    public void onNewIntent(final Intent intent){
        String finalMessage="";
        Log.d(TAG,"new inent found");

        Vibrator vib = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        long[] duration = { 50, 100, 200, 300 };
        vib.vibrate(duration, -1);

        bNewIntentFound = true;
        bPaymentComplete=false;
        //ShowPaymentStart();
        mIntent = intent;


        isGoodSale=false;
        iCardBal=0;
        Cardbal="";

        try {
            if(libInstance==null){
                libInstance = NxpNfcLibLite.getInstance();
                MyNXP = new myNXP(libInstance,classic);
                libInstance.registerActivity(this);
                libInstance.startForeGroundDispatch();
            }

            libInstance.filterIntent(intent, new Nxpnfcliblitecallback() {


                @Override
                public void onClassicCardDetected(final MFClassic objMFCCard) {
                    classic = objMFCCard;
				/* Insert your logic here by commenting the function call below. */
                   // ShowPaymentStart();

                    try {
                        Log.d(TAG, "PayFare trying MF Connect");
                        classic.setTimeout(10000);

                        classic.connect();

                        bCardConnected=true;

                        MyNXP = new myNXP(libInstance,classic);

                        strMFSectorReadREsult="";

                        if(bPayTimerIsActive) {
                            bPayTimerIsActive = false;
                            pmtTmr.ExitTmr();
                        }

                        strMFSectorReadREsult="";
                        strMFSectorReadREsult=   MyNXP.bubblyCardBalanceRequest();
                        strCurCardSN = myNXP.mCurCardUID;
                        if(bIsActiveSaleRequest|| iSaleValuePicked>0) {


                            if(iSaleValuePicked> myNXP.iCardBalance){

                                iCardBal = myNXP.iCardBalance;
                                Cardbal = String.valueOf(iCardBal);

                                Vibrator vib = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                                long[] duration = { 50, 100, 200, 300 };
                                vib.vibrate(duration, -1);
                                showBIGToast("Can not make sale, insufficient tokens!");
                                bPaymentComplete=false;
                                iSaleValuePicked=0;

                                classic.close();
                                picURI = "@drawable/processnok"; //  Not good balance ,
                                setIconPicture(picURI);



                            }else{

                                strMFSectorReadREsult = MyNXP.bubblyCardDecrement(iSaleValuePicked);

                                iCardBal = myNXP.iCardBalance;
                                Cardbal = String.valueOf(iCardBal);

                                isGoodSale = MyNXP.isGoodSale;
                                    if(isGoodSale) {
                                        picURI = "@drawable/processok"; // = ok good sale
                                        setIconPicture(picURI);
                                        buildTansactionXMLandStore(strTERMSN,strCurCardSN,iSaleValuePicked,iCardBal);
                                        UpdateMerchantTotalSaleCounter(iSaleValuePicked);

                                        //  bubblyCardUpdateCardHistory(String strTransActionDetails,String strMerchantDisp){

                                        MyNXP.bubblyCardUpdateCardHistory(formatcardHistorySectorAData(),formatcardHistorySectorBData(iSaleValuePicked,iCardBal));


                                    }


                            }


                        }
                        else {



                            iCardBal = myNXP.iCardBalance;
                            Cardbal = String.valueOf(iCardBal);

                            if(iCardBal==0){
                                //strMFSectorReadREsult = MyNXP.bubblyCardBalanceRequest();
                                picURI = "@drawable/processnok"; //  Not good balance ,
                                setIconPicture(picURI);
                            }else{
                                //strMFSectorReadREsult = MyNXP.bubblyCardBalanceRequest();
                                picURI = "@drawable/processok"; //  Not good balance ,
                                setIconPicture(picURI);

                            }



                        }

                        imgPayWave.setVisibility(View.VISIBLE);
                        tvCount.setVisibility(View.VISIBLE);
                        tvCount.setText("");
                        tvCount.setText("B:" + Cardbal);


                        classic.close();
                        bCardConnected = false;

                        libInstance.stopForeGroundDispatch();
                        rstDispTask = new ResetDisplayTask();
                        rstDispTask.execute("");




                    } catch (Throwable t) {
                        Log.d(TAG,"TAP Throwable Error\n\"Unknown Error Tap Again!\nErr:" + t.getMessage());
                        //showMessage("Unknown Error Tap Again!", 't');
                    }
                }
            });
        }catch (Exception e){
            Log.d(TAG,"Tried Excuting libInstance classic card detected");
        }



    }

    public String formatcardHistorySectorAData(){
        //Formats MerchantDisplay for Card History



        if(strMerchantDisplay.length()>16) {


            return  toHex(strMerchantDisplay.trim().substring(0, 16));
        }
        else
            return  toHex(String.format("%" + 16 + "s",strMerchantDisplay.trim()));


    }

    public String toHex(String arg) {
        try {
            return String.format("%02x", new BigInteger(1, arg.getBytes(/*YOUR_CHARSET?*/)));
        }catch (Exception ie){
            ie.printStackTrace();
            return "";
        }
    }

    public String formatcardHistorySectorBData(int iAMT,int iCardBal){
        //Formats Transaction data for Card History
        //format: time-date-termsn-transamt-card-bal
        //075337 171117 1130011431 000004 0153


        Calendar c = Calendar.getInstance();

        SimpleDateFormat df = new SimpleDateFormat("hhmmssddmmyy");
        String strCurrentTimeandDate = df.format(c.getTime());


        try {
            return strCurrentTimeandDate + getDeviceSN().replace(":","").trim().substring(0, 10) + String.format("%06d", iAMT) + String.format("%04d", iAMT);
        }catch (Exception i){

            i.printStackTrace();
            return "";
        }
    }

    public boolean buildTansactionXMLandStore(String strTermSN,String strCardSN,int iTransAMT,int iCardBAL){

        String strDATA="";

        //Sample line:
        //"<PF>
        // <TERM>1130011468</TERM>
        // <FLAG>T062NNN</FLAG>
        // <PURSE>T1</PURSE>
        // <SER0>15E181AE</SER0>
        // <SER1></SER1>
        // <ID0>0000            </ID0>
        // <ID1>                </ID1>
        // <LP></LP>
        // <DB>|11.10.10.2|bubbly2017|tran|payfare|p4yf4r3|</DB>
        // <DATA>00 171223 051117 00000000 00000005 00000000 00000000 00000005 00000112</DATA>
        // </PF>";


        /*  this is the above DATA string to store / send
           tType       //2 0 == 10 if credit / perso app else / value is 00
           strcat(trans,tranTime);     //6 2 like 180339
           strcat(trans,tranDate);     //6 8 like 051117
           strcat(trans,tRef);         //8 14 fixed val 00000000
           strcat(trans,tGross);       //8 22  tans value gross amt like 00000002
           strcat(trans,tDisc);        //8 30 fixed 00000000
           strcat(trans,tTip);         //8 30 fixed 00000000
           strcat(trans,tNett);        //8 38 tans value gross amt like 00000002
           strcat(trans,tBal);         //8 46 card ballance goes here like 00000016

            if(uploaded==TRUE)
                strcat(trans,"0");
            else
                strcat(trans,"F");
             strcat(trans,"#");
         */
        //  Completet build the Transaction XML

        try {
            strDATA = "00";

            Calendar c = Calendar.getInstance();

            SimpleDateFormat df = new SimpleDateFormat(strTransDateTimeformat);


            String strCurrentTimeandDate = df.format(c.getTime());
            strDATA += (strCurrentTimeandDate);
            strDATA += ("00000000");

            strDATA += (String.format("%08d", iTransAMT));
            strDATA += ("00000000");
            strDATA += ("00000000");
            strDATA += (String.format("%08d", iTransAMT));
            strDATA += (String.format("%08d", iCardBAL));

            String dataaToSend="";

            dataaToSend = "\n<PF><TERM>" + strTermSN.trim().replace(":", "").substring(0, termSnMaxLength) + "</TERM><FLAG>T062NNN</FLAG><PURSE>P1</PURSE>" +
                    "<SER0>" + strCardSN.trim() + "</SER0><SER1></SER1><ID0>" + strCardSN.trim().substring(0, 4) + "            </ID0><ID1>                </ID1><LP></LP>" +
                    "<DB>" + transDb + "</DB>" +
                    "<DATA>" + strDATA + "</DATA></PF><U>F</U>";


            saveTransActionLine(dataaToSend);
            db.setFinalDBTableName(buildDBTableNamePostfix()); // currently Table Dated Postfix Is disabled
            db.addTrx(strDATA, strCardSN.trim(),"F");

            dataaToSend="";

            return true;
        }catch (android.database.sqlite.SQLiteException aie){
            aie.printStackTrace();
            showToast(aie.getMessage());

        }
        catch (Exception ie){

            showToast(ie.getMessage());
            ie.printStackTrace();
        }
        return false;

    }

    public String buildTrxXMLStrForUploadfromSQLDB(String strCardSN,String strData,String strtrxID){

        String dataToSend="";

        //String.format("%08d", iTransAMT)

        dataToSend = "<PF><TERM>" + getDeviceSN().trim().replace(":", "").substring(0, termSnMaxLength) + "</TERM><FLAG>T062NNN</FLAG><PURSE>T1</PURSE>" +
                "<SER0>" + strCardSN.trim() + "</SER0><SER1></SER1><ID0>"+ strtrxID + "            </ID0><ID1>                </ID1><LP></LP>" +
                "<DB>" + transDb + "</DB>" +
                "<DATA>" + strData + "</DATA></PF>";

        return dataToSend;

    }

    public String buildDBTableNamePostfix(){

        try {
            Calendar c = Calendar.getInstance();

            SimpleDateFormat df = new SimpleDateFormat(strTransDatabaseTablepostFixDateformat);


            String strCurrentDate = df.format(c.getTime());
            return strCurrentDate;
        }catch (Exception e){

            e.printStackTrace();
            return "";
        }

    }


    public String buildGetConfigRequestXMLToSend( String strTermSN){
        //Sample line:

        /*<PF>
            <TERM>9091130011431</TERM>
            <FLAG>C000NNN</FLAG>
            <DB>|11.10.10.2|bubbly2017|termsetup|payfare|p4yf4r3|</DB>
        </PF>*/

        //"|11.10.10.2|bubbly2017|tran|payfare|p4yf4r3|";

            try {

                Calendar c = Calendar.getInstance();

                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                String strCurrentDate = df.format(c.getTime());
                String dataaToSend = "<PF><TERM>" + strTermSN + "</TERM><FLAG>C000NNN</FLAG><DB>" + configDb.replace("IPADDRR",striFinalIp)+
                        "</DB></PF>";


                return dataaToSend;
            }catch (Exception ie){
                ie.printStackTrace();
                return "";
            }

    }






    public void CreateDailyTransLogFile(){


        Calendar c = Calendar.getInstance();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String strCurrentDate = df.format(c.getTime());

        if(Log_File.isExternalStorageWritable()){

            logFileDir = Log_File.getAlbumStorageDir("Bubbly");
            filePath = logFileDir + "/BB" + strCurrentDate +".txt";
            if(Log_File.CreateAppFile(filePath,getApplicationContext())) {
                if (!Log_File.bTodaysLogExists) {
                    Log_File.SaveALine(filePath, "HEADER:" + strCurrentDate, getApplicationContext());
                    Log_File.bHeaderwriten=true;
                }
            }
        }


    }


    public void getDeviceHardwareDetails() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;

        /*
            public static boolean bIsNougatAndroid=false;
            public static boolean bisHuaweiDevice=false;
         */
        // Do something for lollipop and above versions
// do something for phones running an SDK before lollipop
        bIsNougatAndroid = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;

        if (model.startsWith(manufacturer)) {

            //return capitalize(model);
        } else {
            //return " is Huawei Device" + capitalize(manufacturer) + " " + model + " " + "Nougat";
//return "is NOT HuaweiDevice" + capitalize(manufacturer) + " " + model + " " + "Nougat";
            bisHuaweiDevice = capitalize(manufacturer).contains("HUAWEI");

        }
    }


    private String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }


    //bSvrUploadRequest
    public void saveTransActionLine(String strLine){

        Calendar c = Calendar.getInstance();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String strCurrentDate = df.format(c.getTime());

        if(Log_File.isExternalStorageWritable()){

            logFileDir = Log_File.getAlbumStorageDir("Bubbly");
            filePath = logFileDir + "/BB" + strCurrentDate +".txt";
            myFile_Logger.AppGeneralLogFilePath = filePath;
            Log_File.setLogPath(filePath);
            if(Log_File.CreateAppFile(filePath,getApplicationContext())){
                // Log_File.setLogPath(filePath);
                //Log_File.AppGeneralLogFilePath = filePath;
                Log_File.SaveALine(filePath,strLine,getApplicationContext());
            }

        }
    }


    public String ReadTransActionLine(int offset){

        Calendar c = Calendar.getInstance();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String strCurrentDate = df.format(c.getTime());
        String oneLineRead="";

        if(Log_File.isExternalStorageWritable()){

            logFileDir = Log_File.getAlbumStorageDir("Bubbly");
            filePath = logFileDir + "/BB" + strCurrentDate +".txt";
            myFile_Logger.AppGeneralLogFilePath = filePath;
            Log_File.setLogPath(filePath);
            oneLineRead = Log_File.ReadAppFileLine(getApplicationContext(),offset,iBubblyRecordSize,filePath);
            if(oneLineRead.length() >0){
                // Log_File.setLogPath(filePath);
                //Log_File.AppGeneralLogFilePath = filePath;
                return oneLineRead;

            }

        }
        return "";
    }


    public boolean UpDateTransActionLine(int offset,int iLenToWrite,String strDatain){

        Calendar c = Calendar.getInstance();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String strCurrentDate = df.format(c.getTime());


        if(Log_File.isExternalStorageWritable()){

            logFileDir = Log_File.getAlbumStorageDir("Bubbly");
            filePath = logFileDir + "/BB" + strCurrentDate +".txt";
            myFile_Logger.AppGeneralLogFilePath = filePath;
            Log_File.setLogPath(filePath);

            if(Log_File.UpdateAppFileLine(offset,iLenToWrite,filePath,getApplicationContext(),strDatain)){
                // Log_File.setLogPath(filePath);
                //Log_File.AppGeneralLogFilePath = filePath;
                return true;

            }

        }
        return false;
    }

    public boolean ClearAllTransActionsFromFiles(){

        Calendar c = Calendar.getInstance();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String strCurrentDate = df.format(c.getTime());

        if(Log_File.isExternalStorageWritable()){

            logFileDir = Log_File.getAlbumStorageDir("Bubbly");
            filePath = logFileDir + "/BB" + strCurrentDate +".txt";
            myFile_Logger.AppGeneralLogFilePath = filePath;
            Log_File.setLogPath(filePath);
            return Log_File.DeleteAppFile(filePath, getApplicationContext());

        }
        return false;
    }


    private class RequestConfigTask extends AsyncTask<String, Integer, Long> {

        protected Long doInBackground(String... strupLoadData) {
            int count = strupLoadData.length;
            long totalSize = 0;
            String curSVRDataLine="";
            for (int i = 0; i < count; i++) {
                totalSize += strupLoadData[i].length();
                if(mSVRConnection.biSConnected) {
                    SendDBMessage(strupLoadData[i].toString());
                    publishProgress((int) ((i / (float) count) * 100));
                }
                // Escape early if cancel() is called
                if (isCancelled()) break;
            }


            for(int i=0;i<trxDataReceived.size();i++){

                curSVRDataLine = trxDataReceived.get(i);
                if(curSVRDataLine.contains("<RESP>")){
                    isLastMessageSentGood=true;

                }
            }

            return totalSize;
        }

        protected void onProgressUpdate(Integer... progress) {
            // showToast(String.valueOf(progress[0]));
        }

        protected void onPostExecute(Long result) {

            showToast("Config Requested " + result + " bytes");
            //mSVRConnection.tearDown();

        }

    }



    private class ResetDisplayTask extends AsyncTask<String, Integer, Long> {

        protected Long doInBackground(String... strupLoadData) {

            try{
                if (isCancelled() || bResetFlagCancelled == true)
                    return null;

                Thread.sleep(2000);

                return (long) 0;
            }catch (Exception ie){
                ie.printStackTrace();
            }

            return (long) -1;
        }

        protected void onProgressUpdate(Integer... progress) {

        }

        protected void onPostExecute(Long result) {
            bResetFlagCancelled = true;
            ResetDisplay();

        }

    }


    private class RequestTransxUploadsTask extends AsyncTask<ArrayList<String>, Integer, Long> {

        protected Long doInBackground(ArrayList<String>... strupLoadData) {
            int count = strupLoadData.length;
            long totalSize = 0;
            uploadSentCount=0;
            String curSVRDataLine="";

            if (isCancelled() ) return (long) 0;
            bIsFinishedSending=false;
            for (ArrayList<String> l : strupLoadData) {
                for(String strDataToSend : l){
                        totalSize += strDataToSend.toString().length();
                        if(mSVRConnection.biSConnected) {

                            isMessageUpLoadType=true;
                            isLastMessageSentGood=false;

                            SendDBMessage(strDataToSend.toString());
                            if(isLastMessageSentGood) {
                                uploadSentCount++;
                                progbarFinalValue = (int) uploadConfirmedCount + (int) uploadSentCount;
                            }else
                            {

                            }


                          //  showToast("Record ID:" + totalNumRecordsSent + "Sent." );


                            publishProgress((int) uploadSentCount);
                        }

                    }

                    if(trxUploadFailCount>3) {
                        if (!mSVRConnection.biSConnected) {
                           // mSVRConnection.tearDown();
                            showToast("Too many uploads failed,\nplease check sever setup \n& try again.");
                            break;
                        }
                    }

                // Escape early if cancel() is called
                if (isCancelled() ) break;
            }


            return uploadSentCount;
        }

        protected void onProgressUpdate(Integer... progress) {
            //showToast("Uploading TRX :" + String.valueOf(progress[0]));
            progb.setProgress(progbarFinalValue);
            //progb.setProgress();
        }

        protected void onPostExecute(Long result) {

            showBIGToast("# Records Uploaded: " + result );

            bIsFinishedSending=true;
            uploadSentCount=0;
            new ProcessUploadStatusesTask().execute(strUploadID);
            //mSVRConnection.tearDown();
        }

    }

    private class ProcessUploadStatusesTask extends  AsyncTask<ArrayList<String>, Integer, Long> {

        protected Long doInBackground(ArrayList<String>... strupLoadData) {

            String currentStringToCheck = "";
            String currentTransactionID="";

            if (isCancelled()) return (long) 0;

            if(!mSVRConnection.biSConnected){
                return (long) 0;
            }
            if(trxUploadFailCount>5) {
                return (long) -5;
            }

            while(!bIsFinishedSending){
                //Wait for alll msgs to upload then process responses
                Log.d("SVR","Wating for Upload to complete");
                if(bIsFinishedSending)
                    break;

            }


            try {
                for (ArrayList<String> l : strupLoadData) {
                    for (String strDataRead : l) {
                        // for (int i = 0; i < iNumberRSPReived; i++) {
                        if(!mSVRConnection.biSConnected){
                            return (long) 0;
                        }
                        if(trxUploadFailCount>5) {
                            return (long) -5;
                        }

                        currentTransactionID = strDataRead.toString();
                        currentStringToCheck = transactionResultsMap.get(currentTransactionID).toString();

                        if (currentStringToCheck.contains("OK")) {
                            uploadConfirmedCount++;
                            progbarFinalValue = (int) uploadConfirmedCount + (int) uploadSentCount;
                            Log.d("SVR","SVR SQL UPDATE---->" +  currentTransactionID + " ACK:" + currentStringToCheck);

                                 db.updateTransactionUploadStatus(Integer.parseInt(currentTransactionID), "T");
                        }
                        else {
                            trxUploadFailCount++;
                        }



                        publishProgress((int) uploadConfirmedCount);
                        if (isCancelled()) break;
                    }
                }

            }catch (Exception ie){
                ie.printStackTrace();
            }



            return uploadConfirmedCount;
        }

        protected void onProgressUpdate(Integer... progress) {
             //showToast("# UL Vaidated: " + String.valueOf(progress[0]));
             progb.setProgress(progbarFinalValue);

        }

        protected void onPostExecute(Long result) {

            if((result + uploadConfirmedCount)>0){
                showBIGToast("# Uploads Validated: " + uploadConfirmedCount);
            }

            trxUploadFailCount=0;

            progbarFinalValue=0;
            uploadConfirmedCount=0;
            uploadSentCount=0;

            progb.setMax(0);
            progb.setVisibility(View.INVISIBLE);
        }

    }



}






