package bubbly2017.payfare.com.bubbly2017;

/**
 * Created by philip on 2017/11/07.
 */



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
import static bubbly2017.payfare.com.bubbly2017.MainActivity.bSvrMsgtimerIsActive;
import static bubbly2017.payfare.com.bubbly2017.MainActivity.iTimeOutVal;
import static  bubbly2017.payfare.com.bubbly2017.MainActivity.tvCount;

public class ServerMSGTimer {

    public myFile_Logger TransactionLog_File = new myFile_Logger();
    public File logFileDir;
    public String filePath="";

    public Intent mpIntent;
    public String TAG = "Upload_Timer";

    ServerMSGTimer.MyCountDownTimer Tmr1 = new ServerMSGTimer.MyCountDownTimer(5000,1000);


    ServerMSGTimer.newlibInstanceListener mCallback;
    public interface newlibInstanceListener{
        void SetCurrentlibInterface(boolean bStartStopForeGroundDispatch);

    }


    public void ClosePayTimer(){

        ExitTmr();
    }

    public void ExitTmr(){

        //SaveTrans();

        bSvrMsgtimerIsActive=false;


        // currrent day's log
        Tmr1.cancel();
    }

    public void TmrStart(){

        Tmr1.start();
        iTimeOutVal = 5;
    }









    public void setCurrentTimeOutValue(){
        iTimeOutVal--;

        return;

    }




    public class MyCountDownTimer extends CountDownTimer {

        private Bundle args = new Bundle();

        public MyCountDownTimer(long startTime, long interval) {

            super(startTime, interval);
        }

        @Override
        public void onFinish() {


            // Go Back to Menu


            ExitTmr();
        }

        @Override
        public void onTick(long millisUntilFinished) {


            if(!bSvrMsgtimerIsActive)
                bSvrMsgtimerIsActive=true;
            setCurrentTimeOutValue();

        }



    }


}

