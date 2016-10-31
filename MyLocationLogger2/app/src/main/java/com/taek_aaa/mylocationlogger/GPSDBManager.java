package com.taek_aaa.mylocationlogger;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by taek_aaa on 2016. 10. 31..
 */

public class GPSDBManager {
    static final String DB_GPS = "GPS.db";
    static final String TABLE_GPS = "GPS";

    Context mContext = null;

    private static GPSDBManager mGpsdbmanager = null;
    private SQLiteDatabase mdatabase = null;

    public static GPSDBManager getInstance(Context context){
        if(mGpsdbmanager==null){
            mGpsdbmanager= new GPSDBManager(context);
        }
        return mGpsdbmanager;
    }

    private GPSDBManager(Context context){
        mContext = context;

        mdatabase = context.openOrCreateDatabase(DB_GPS,context.MODE_PRIVATE,null);

        mdatabase.execSQL("CREATE TABLE IF NOT EXISTS "+TABLE_GPS+"("+"_id INTEGER PRIMARY KRY AUTOINCREMENT, " + "latitude   TEXT, "+"longitude   TEXT );");

        
    }


    public long insert(ContentValues addRowValue){
        return mdatabase.insert(TABLE_GPS,null,addRowValue);
    }
}

