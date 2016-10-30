package com.taek_aaa.mylocationlogger;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity {

    private long lastTimeBackPressed;
    private LocationManager locationManager;
    MyLocationListener mll = new MyLocationListener();
    SQLiteDatabase db;
    double latitudedouble;
    double longitudedouble;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //- SDK 23버전 이상 (마시멜로우 이상부터)부터는 아래 처럼 권한을 사용자가 직접 허가해주어야 GPS기능을 사용가능 GPS 기능을 사용하기전 위치에 추가해야함
        //체크 퍼미션
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 10, mll);  //3000 -> 3초

        try{
            db=SQLiteDatabase.openDatabase("/data/data/cis493.sqldatabases/myfriendsDB",null,SQLiteDatabase.CREATE_IF_NECESSARY);
            db.execSQL("create table location(id INTEGER PRIMARY KEY autoincrement, latitude double, longitude double);");
            db.execSQL("insert into location(latitude, longitude) values(latitudedouble, longitudedouble);");
            //db.setTransactionSuccessful();      //이게 커밋하는건데 저장은뭐지
            db.close();

        }catch(SQLiteException e){
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();
        }




    }
    @Override
    public void onBackPressed(){
        if(System.currentTimeMillis()-lastTimeBackPressed<1500){
            finish();
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            }
            locationManager.removeUpdates(mll);
            return;

        }

        Toast.makeText(MainActivity.this,"'뒤로' 버튼을 한번 더 누르면 종료됩니다",Toast.LENGTH_SHORT).show();
        lastTimeBackPressed=System.currentTimeMillis();

    }


    public void onClick(View v) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }
        locationManager.removeUpdates(mll);
        Toast.makeText(MainActivity.this, "더이상 GPS 정보롤 받아오지 않습니다", Toast.LENGTH_SHORT).show();  
    }
    public class MyLocationListener implements LocationListener{

        @Override
        public void onLocationChanged(Location location) {
            String str = "Latitude: "+location.getLatitude()+"\n"+"Longitude: "+location.getLongitude()+"\n";
            TextView tv = (TextView)findViewById(R.id.textview);
            tv.append(str);
            Toast.makeText(getBaseContext(), str, Toast.LENGTH_SHORT).show();
            latitudedouble = location.getLatitude();
            longitudedouble = location.getLongitude();


        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            }
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,3000,10,mll);
        }

        @Override
        public void onProviderEnabled(String provider) {
            Toast.makeText(getBaseContext(), "Gps turned on ", Toast.LENGTH_LONG).show();

        }

        @Override
        public void onProviderDisabled(String provider) {
            new AlertDialog.Builder(MainActivity.this)
                    .setMessage("GPS가 꺼져있습니다.\n ‘위치 서비스’에서 ‘Google 위치 서비스’를 체크해주세요")
                    .setPositiveButton("설정",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(intent);
                        }

                    })
                    .setNegativeButton("취소", null).show();

            /******** Called when User off Gps *********/
            Toast.makeText(getBaseContext(), "Gps turned off ", Toast.LENGTH_LONG).show();

        }
    }

}
