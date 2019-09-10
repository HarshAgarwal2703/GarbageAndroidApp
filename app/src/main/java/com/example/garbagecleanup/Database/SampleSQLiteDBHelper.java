package com.example.garbagecleanup.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.garbagecleanup.Issue_Model_Class;

import java.util.ArrayList;

public class SampleSQLiteDBHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 2;
    public static final String ID = "ID";

    public static final String DATABASE_NAME = "LOCAL_POST1";
    public static final String IMAGE = "IMAGE";
    public static final String LATITUDE = "LATITUDE";
    public static final String LONGITUDE = "LONGITUDE";
    public static final String DATE = "DATE";

    public SampleSQLiteDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + DATABASE_NAME + " (" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                IMAGE + " BLOB, " +
                DATE + " TEXT, " +
                LATITUDE + " TEXT, " +
                LONGITUDE + " TEXT" + ")" );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DATABASE_NAME);
        onCreate(sqLiteDatabase);
    }

    public ArrayList getRegistrationData(){
        String selectQuery = "SELECT  * FROM " + DATABASE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(selectQuery,null,null);
        ArrayList dataModelArrayList = new ArrayList();
        if(cursor.moveToFirst()){
            do {
                Issue_Model_Class regdm = new Issue_Model_Class();
                regdm.setId(cursor.getString(cursor.getColumnIndex(ID)));
                regdm.setImg(cursor.getBlob(cursor.getColumnIndex(IMAGE)));
                regdm.setLatitude(cursor.getString(cursor.getColumnIndex(LATITUDE)));
                regdm.setLongitude(cursor.getString(cursor.getColumnIndex(LONGITUDE)));
                regdm.setDate(cursor.getString(cursor.getColumnIndex(DATE)));
                dataModelArrayList.add(regdm);

            } while (cursor.moveToNext());

        }
        cursor.close();
        db.close();


        return dataModelArrayList;
    }


}