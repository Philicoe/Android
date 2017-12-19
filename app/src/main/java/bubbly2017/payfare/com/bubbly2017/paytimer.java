package bubbly2017.payfare.com.bubbly2017;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by philip on 2017/11/01.
 */

import static bubbly2017.payfare.com.bubbly2017.MainActivity.bPayTimerIsActive;
import static  bubbly2017.payfare.com.bubbly2017.MainActivity.tvCount;
public class paytimer  {

    public myFile_Logger TransactionLog_File = new myFile_Logger();
    public File logFileDir;
    public String filePath="";

    public Intent mpIntent;
    public String TAG = "PayFare_PayTimer";

    MyFragCountDownTimer Tmr1 = new MyFragCountDownTimer(10000,1000);


    newlibInstanceListener mCallback;
    public interface newlibInstanceListener{
        void SetCurrentlibInterface(boolean bStartStopForeGroundDispatch);

    }


    public void ClosePayTimer(){

        ExitTmr();
    }

    public void ExitTmr(){

        //SaveTrans();


        // currrent day's log
        Tmr1.cancel();
    }

    public void TmrStart(){

        Tmr1.start();
    }









    public void setCurrentTimerTextValue(String TmrStr){
        tvCount.setText(TmrStr);

        return;

    }




    public class MyFragCountDownTimer extends CountDownTimer {

        private Bundle args = new Bundle();

        public MyFragCountDownTimer(long startTime, long interval) {

            super(startTime, interval);
        }

        @Override
        public void onFinish() {


            // Go Back to Menu


            ExitTmr();
        }

        @Override
        public void onTick(long millisUntilFinished) {
            //TextView txtVw = (TextView) getView().findViewById(R.id.textviewCounter);
            // Log.d(TAG,"bNewIntentFound=" + Boolean.toString(MainActivity.bNewIntentFound));
            // Log.d(TAG,"bPaymentComplete=" + Boolean.toString(MainActivity.bPaymentComplete));
            //Log.d(TAG,"bCardConnected=" + Boolean.toString(MainActivity.bCardConnected));
            //if(!MainActivity.bNewIntentFound || !MainActivity.bPaymentComplete || !MainActivity.bCardConnected) {
            if(!bPayTimerIsActive)
                bPayTimerIsActive=true;

            tvCount.setText("" + millisUntilFinished / 1000);
            // }
            // else {

            // txtVw.setText("Payment Done Thanks!");
            //   Log.d(TAG,"Payfare-Payment-Timer_Canceled");
            // ExitTmr();
            //Tmr1.cancel();
            //onFinish();
            //}
        /*
        args.putString("TMR","" + millisUntilFinished / 1000);
        Paytmr.setArguments(args);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.StatsFragment,Paytmr);
        transaction.addToBackStack(null);
        transaction.commit();*/
            //Paytmr.ge
            //findViewById(R.id.textviewCounter).setText("" + millisUntilFinished / 1000);

        }

    }


}