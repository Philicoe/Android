package bubbly2017.payfare.com.bubbly2017;
import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.FileNameMap;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.io.FileOutputStream;

import static android.content.Context.MODE_PRIVATE;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
public class myFile_Logger{

    public Log LogCatlog;
    public boolean bTodaysLogExists=false;
    public boolean bHeaderwriten=false;
    public boolean bLineUpdated=false;
    FileOutputStream outputStream;
    public File storageDirectory;
    public FileOutputStream fout ;//= new FileOutputStream(storageDirectory);
    public FileInputStream fin;
    public OutputStreamWriter myoutwriter;//
    public InputStreamReader myinreader;
    public static final String TAG="PayFare_FileIo-Classs";
    public static String AppGeneralLogFilePath;
    FileInputStream FileIoStream;
    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }
    public void setLogPath(String str){

        AppGeneralLogFilePath = str;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }


    public void CreateInternalTransactionfile(String strFilename,Context contextView){

        try {
            FileOutputStream fileout=contextView.openFileOutput(strFilename, MODE_PRIVATE);
            OutputStreamWriter outputWriter=new OutputStreamWriter(fileout);
            outputWriter.write("Test Data to write...\n");
            outputWriter.close();

            //display file saved message
            Toast.makeText(contextView, strFilename + ", File saved successfully!",
                    Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            //display file saved message
            Toast.makeText(contextView, "File saved successfully!\n" + e.getMessage() ,
                    Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }




}

    public boolean CreateAppFile(String filePath,Context contextView){
        Context thisContext =  contextView;



        try {
            storageDirectory = new File (filePath);
            if(!storageDirectory.exists()){
                AppGeneralLogFilePath = filePath;
                storageDirectory.createNewFile();
                fout = new FileOutputStream(storageDirectory,true);
                myoutwriter = new OutputStreamWriter(fout);
               // myoutwriter.write("HEADER:" + strCurrentDate);
                myoutwriter.flush();
                myoutwriter.close();
                bHeaderwriten=true;
                bTodaysLogExists=true;
                //IOException

                Log.d(TAG,"new File created: " + filePath);
                Toast.makeText(contextView,"logCreated!\nUpdated Saved...",Toast.LENGTH_SHORT).show();
                // Toast.makeText(thisContext,"Updated Saved...", Toast.LENGTH_SHORT).show();
                return true;
            }
            else{
                bHeaderwriten=true;
                bTodaysLogExists=true;
                Log.d(TAG,"new File exists, updated: " + filePath);
               // Toast.makeText(contextView,"DailyLog!\nExists...",Toast.LENGTH_SHORT).show();
                //Toast.makeText(thisContext,"Updated Saved...", Toast.LENGTH_SHORT).show();
                return true;
            }

            //outputStream = thisContext.openFileOutput(filePath, Context.MODE_APPEND);
            //outputStream.write(string.getBytes());
            //outputStream.close();

        } catch(IOException ioe){
            bTodaysLogExists=false;
            Toast toast = Toast.makeText(contextView, ioe.getMessage(), Toast.LENGTH_LONG);
            toast.show();
            ioe.printStackTrace();


        } catch (Exception e) {
            bTodaysLogExists=false;
            Toast toast = Toast.makeText(contextView, e.getMessage(), Toast.LENGTH_LONG);
            toast.show();
            e.printStackTrace();
        }

        return false;
    }






    public boolean DeleteAppFile(String filePath,Context contextView){
        Context thisContext =  contextView;

        Calendar c = Calendar.getInstance();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        String strCurrentDate = df.format(c.getTime());

        try {
            storageDirectory = new File (filePath);
            if(storageDirectory.exists()){
                AppGeneralLogFilePath = filePath;


                storageDirectory.delete();

                bHeaderwriten=false;
                bTodaysLogExists=false;


                Log.d(TAG,"Log File Deleted: " + filePath);
                Toast.makeText(contextView,"Daily Log file!\nDeleted...",Toast.LENGTH_LONG).show();
                // Toast.makeText(thisContext,"Updated Saved...", Toast.LENGTH_SHORT).show();
                return true;
            }
            else{
                bHeaderwriten=false;
                bTodaysLogExists=false;

                Toast.makeText(contextView,"DailyLog!\nDelete failed\n No file to Delete!...",Toast.LENGTH_SHORT).show();
                //Toast.makeText(thisContext,"Updated Saved...", Toast.LENGTH_SHORT).show();
                return false;
            }

            //outputStream = thisContext.openFileOutput(filePath, Context.MODE_APPEND);
            //outputStream.write(string.getBytes());
            //outputStream.close();

        } catch (Exception e) {
            bTodaysLogExists=false;
            Toast toast = Toast.makeText(contextView, e.getMessage(), Toast.LENGTH_LONG);
            toast.show();
            e.printStackTrace();
        }

        return false;
    }







    public boolean SaveALine(String FileName, String StringToSave,Context thisContext){
        long iStartPos = 0; //storageDirectory.length();
        int ilengthToSave =0;// StringToSave.length();
        String strTmp = StringToSave;

        try
        {
            storageDirectory= new File(FileName);
            if(storageDirectory.exists()) {
                ilengthToSave = StringToSave.length();
                iStartPos = storageDirectory.length();
                //storageDirectory = new File (FileName);

                FileOutputStream fout = new FileOutputStream(storageDirectory, true);
                myoutwriter = new OutputStreamWriter(fout);
                myoutwriter.append(strTmp);//, (int) iStartPos + 1, ilengthToSave);
                myoutwriter.close();

                //Toast.makeText(thisContext,"Updated Saved...", Toast.LENGTH_SHORT).show();
                return true;
            }
        }catch(Exception e){

            Toast.makeText(thisContext,e.getMessage(),Toast.LENGTH_LONG).show();
            e.printStackTrace();

        }
        return false;
    }

    public boolean SaveALine(String FileName, String StringToSave){
        long iStartPos = 0; //storageDirectory.length();
        int ilengthToSave =0;// StringToSave.length();
        String strTmp = StringToSave;

        try
        {
            storageDirectory= new File(FileName);
            if(storageDirectory.exists()) {
                ilengthToSave = StringToSave.length();
                iStartPos = storageDirectory.length();

                FileOutputStream fout = new FileOutputStream(storageDirectory, true);
                myoutwriter = new OutputStreamWriter(fout);
                myoutwriter.append(strTmp);//, (int) iStartPos + 1, ilengthToSave);
                myoutwriter.close();

                //Toast.makeText(thisContext,"Updated Saved...", Toast.LENGTH_SHORT).show();
                return true;
            }
        }catch(Exception e){

            Log.d(TAG,"Save a line - no context issue\n" + e.getMessage());

            //Toast.makeText(thisContext,e.getMessage(),Toast.LENGTH_LONG).show();
            e.printStackTrace();

        }
        return false;
    }
    public boolean SaveATransaction(String FileName, String StringToSave){
        long iStartPos = 0;//storageDirectory.length();
        int ilengthToSave = 0;//StringToSave.length();
        String strTmp = "";//StringToSave;

        try
        {
            strTmp = StringToSave;
            ilengthToSave = StringToSave.length();
            //
            storageDirectory = new File (FileName);
            iStartPos = storageDirectory.length();
            FileOutputStream fout = new FileOutputStream(storageDirectory,true);
            myoutwriter = new OutputStreamWriter(fout);
            myoutwriter.append(strTmp);//, (int) iStartPos + 1, ilengthToSave);
            myoutwriter.close();

            //Toast.makeText(thisContext,"Updated Saved...", Toast.LENGTH_SHORT).show();
            return true;
        }catch(Exception e){

            //Toast.makeText(thisContext,e.getMessage(),Toast.LENGTH_LONG).show();
            Log.d(TAG,e.getMessage() + "\n");
            e.printStackTrace();
            //payfareAPK.log
            SaveALine(AppGeneralLogFilePath,e.getMessage().toString());

        }
        return false;
    }



    public String ReadAppFileLine(Context appContext,int iOffsetToRead,int iSizeToRead,String Filepath){
        char[] buffer = new char[iSizeToRead];// Bubby size record size is 303 bytes

        long iStartPos = 0;//storageDirectory.length();

        FileInputStream InputStream=null;
        String strDataIn="";

        try{

            storageDirectory = new File (Filepath);
            iStartPos = storageDirectory.length();
            FileInputStream fin = new FileInputStream(storageDirectory);
            myinreader = new InputStreamReader(fin);
            myinreader.read(buffer,iOffsetToRead,iSizeToRead);
            myinreader.close();


            strDataIn = new String(buffer);
            return strDataIn;

        } catch (FileNotFoundException e) {

            Toast toast = Toast.makeText(appContext, e.getMessage(), Toast.LENGTH_LONG);
            toast.show();
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
             return "";

        }catch (Exception e){

            Toast toast = Toast.makeText(appContext, e.getMessage(), Toast.LENGTH_LONG);
            toast.show();
            e.printStackTrace();
        }
        return "";
    }

    public boolean UpdateAppFileLine(int iPossitionToUpdate,int iLentoWrite,String filePath , Context contextView,String dataToUpdate){
        Context thisContext =  contextView;


        char [] dtaout = new char[1];

        dtaout = dataToUpdate.toCharArray();

        try {
            storageDirectory = new File (filePath);
            if(storageDirectory.exists()){

                fout = new FileOutputStream(storageDirectory,true);
                myoutwriter = new OutputStreamWriter(fout);
                myoutwriter.write(dtaout,iPossitionToUpdate,iLentoWrite);
                myoutwriter.flush();
                myoutwriter.close();
                bLineUpdated=true;

            }
            else{
                bLineUpdated=false;


            }
            return bLineUpdated;

        } catch(IOException ioe){
            bTodaysLogExists=false;
            Toast toast = Toast.makeText(contextView, ioe.getMessage(), Toast.LENGTH_LONG);
            toast.show();
            ioe.printStackTrace();
        } catch (Exception e) {
            bLineUpdated=false;
            Toast toast = Toast.makeText(contextView, e.getMessage(), Toast.LENGTH_LONG);
            toast.show();
            e.printStackTrace();
        }

        return bLineUpdated;
    }

    public File getAlbumStorageDir(String albumName) {
        // Get the directory for the user's public pictures directory.
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS), albumName);
        if (!file.mkdirs()) {
            // LogCatlog.e(Log. LOG_TAG, "Directory not created");
        }
        return file;
    }






}