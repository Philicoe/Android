package bubbly2017.payfare.com.bubbly2017;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by philip on 2017/11/08.
 */

public class BubblyDBHandler  extends SQLiteOpenHelper {

    //DB Ver
    private static final int DATABASE_VERSION=1;
    //DB Name
    private static final String DATABASE_NAME="Bubbly2017";
    //Table names
    private static final String DATABASE_TABLE_Transactions = "tran";
    private static String DATABASE_TABLE_TRX_Final="";

    //Table Column Names
    private static final String KEY_ID = "id";
    private static final String trxDetails = "TrxDetail";
    private static final String strCARDSn = "CardN";
    private static final String isUploaded = "bIsUploaded";

    public BubblyDBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }


    public void setFinalDBTableName(String strDate){


        //Date format must be yyyymmdd
        //table name must be trans20171109 if date is 2017-11-09

        DATABASE_TABLE_TRX_Final = DATABASE_TABLE_Transactions;// + strDate.trim();
    }



    // Adding new contact
    public void addTrx(String trx,String strCardSN,String isUbloaded) {
        /*
        //Table Column Names
         KEY_ID = "id";
         trxDetails = "TrxDetail";
         strCARDSn = "CardN";
         isUploaded = "bIsUploaded";
         */

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(trxDetails,trx); //
        values.put(strCARDSn, strCardSN);
        values.put(isUploaded, isUbloaded);//
        // Inserting Row
        db.insert(DATABASE_TABLE_TRX_Final, null, values);
        db.close(); // Closing database connection

    }

    // Getting single contact
    public String getTransAction(int id) {

        /*
        //Table Column Names
         KEY_ID = "id";
         trxDetails = "TrxDetail";
         strCARDSn = "CardN";
         isUploaded = "bIsUploaded";
         */

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(DATABASE_TABLE_TRX_Final, new String[] { KEY_ID,
                        trxDetails,strCARDSn, isUploaded }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        return cursor.getString(0) + cursor.getString(1) + cursor.getString(2);


    }



    // Getting All Transactions
    public List<String> getAllTransActions() {

        /*
        //Table Column Names
         KEY_ID = "id";
         trxDetails = "TrxDetail";
         strCARDSn = "CardN";
         isUploaded = "bIsUploaded";
         */

        List<String> trxList = new ArrayList<String>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + DATABASE_TABLE_TRX_Final;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);


        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                String trxDetails = new String();
                trxDetails += cursor.getString(0) + ",";
                trxDetails += cursor.getString(1) + ",";
                trxDetails += cursor.getString(2) + ",";
                trxDetails += cursor.getString(3);

                trxList.add(trxDetails);
            } while (cursor.moveToNext());
        }

        // return contact list
        return trxList;
    }


    //Getting only transactions where upload status is = 'F
    public List<String> getTransActionsToUpload() {

        /*
        //Table Column Names
         KEY_ID = "id";
         trxDetails = "TrxDetail";
         strCARDSn = "CardN";
         isUploaded = "bIsUploaded";
         */

        List<String> trxList = new ArrayList<String>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + DATABASE_TABLE_TRX_Final + " WHERE " + isUploaded + "='F'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);


        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                String trxDetails = new String();
                trxDetails += cursor.getString(0) + ",";
                trxDetails += cursor.getString(1) + ",";
                trxDetails += cursor.getString(2) + ",";
                trxDetails += cursor.getString(3);

                trxList.add(trxDetails);
            } while (cursor.moveToNext());
        }

        // return contact list
        return trxList;
    }

    // Getting contacts Count
    public int geTrxCount() {

        /*
        //Table Column Names
         KEY_ID = "id";
         trxDetails = "TrxDetail";
         strCARDSn = "CardN";
         isUploaded = "bIsUploaded";
         */

        int iNumRecrods=0;
        String countQuery = "SELECT  * FROM " + DATABASE_TABLE_TRX_Final;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        iNumRecrods = cursor.getCount();
        cursor.close();

        // return count
        return iNumRecrods;

    }
    // Updating single contact
    public int updateTransactionUploadStatus(int trxiD,String isuploaded) {
        /*
        //Table Column Names
         KEY_ID = "id";
         trxDetails = "TrxDetail";
         strCARDSn = "CardN";
         isUploaded = "bIsUploaded";
         */


        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(isUploaded, isuploaded);

        // updating row
        return db.update(DATABASE_TABLE_TRX_Final, values, KEY_ID + " = ?",
                new String[] { String.valueOf(trxiD) });

    }

    // Deleting single contact
    public void deleteTransactin(int trxID) {

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DATABASE_TABLE_TRX_Final, KEY_ID + " = ?",
                new String[] { String.valueOf(trxID) });
        db.close();

    }








    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
         /*
        //Table Column Names
         KEY_ID = "id";
         trxDetails = "TrxDetail";
         strCARDSn = "CardN";
         isUploaded = "bIsUploaded";
         */
        String CREATE_TRANSACTION_TABLE = "CREATE TABLE " + DATABASE_TABLE_TRX_Final + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + trxDetails + " TEXT," + strCARDSn + " TEXT,"
                + isUploaded + " TEXT" + ")";
        db.execSQL(CREATE_TRANSACTION_TABLE);
    }


    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_TRX_Final);

        // Create tables again
        onCreate(db);
    }


    public boolean isTableExists(SQLiteDatabase db, boolean openDb) {
        if(openDb) {
            if(db == null || !db.isOpen()) {
                db = getReadableDatabase();
            }

            if(!db.isReadOnly()) {
                db.close();
                db = getReadableDatabase();
            }
        }

        Cursor cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '"+DATABASE_TABLE_TRX_Final+"'", null);
        if(cursor!=null) {
            if(cursor.getCount()>0) {
                cursor.close();
                return true;
            }
            cursor.close();
        }
        return false;
    }



}



