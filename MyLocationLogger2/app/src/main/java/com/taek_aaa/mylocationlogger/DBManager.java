package com.taek_aaa.mylocationlogger;

/**
 * Created by taek_aaa on 2016. 11. 8..
 */

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBManager extends SQLiteOpenHelper {
    public static double curlatitude;
    public static double curlongitude;
    public static Cursor c;

    public DBManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        // 새로운 Table 생성
        db.execSQL("CREATE TABLE database (_id INTEGER PRIMARY KEY AUTOINCREMENT, latitude DOUBLE , longitude DOUBLE);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void insert(double latitude, double longitude) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO database VALUES(NULL, " + latitude + ", " + longitude + ");");
        String str = "Latitude: " + latitude + "\n" + "Longitude: " + longitude + "\n";
        db.close();
    }

    public void getResult() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM database", null);
        c = cursor;
        while (cursor.moveToNext()) {
            double latitudecur = cursor.getDouble(cursor.getColumnIndex("latitude"));
            double longitudecur = cursor.getDouble(cursor.getColumnIndex("longitude"));
            Log.i("SQLDB ", "select : " + "(Latitude" + latitudecur + ")(Longitude:" + longitudecur + ")");
            curlatitude = latitudecur;
            curlongitude = longitudecur;
        }
    }
}
