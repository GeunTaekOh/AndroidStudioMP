package com.taek_aaa.mylocationlogger;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import static java.lang.System.exit;

public class MainActivity extends Activity {

    final DBManager dbManager = new DBManager(this, "GPS.db", null, 1);
    private long lastTimeBackPressed;
    private LocationManager locationManager;
    private GoogleMap mMap;
    ScrollView scroll;
    public static double latitudedouble;
    public static double longitudedouble;
    TextView mDisplayDbEt;
    int iter = 0;
    MapsActivity mapAct = null;
    MyLocationListener mll = null;
    Location mLocation;
    SQLiteDatabase db;
    final static int interval_time =1000*60*10;
    public static ArrayList<Double> alistlatitude = null;
    public static ArrayList<Double> alistlongitude = null;
    public static ArrayList<LatLng> alistlocation = null;
    public static ArrayList<String> alisttodo = null;
    public static ArrayList<String> alisttext = null;
    public static ArrayList<String> alistTime = null;
    public static ArrayList<String> alistcategory = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mll = new MyLocationListener();
        mDisplayDbEt = (TextView) findViewById(R.id.dbtv);

        alistlatitude = new ArrayList<Double>();
        alistlongitude = new ArrayList<Double>();
        alistlocation = new ArrayList<LatLng>();
        alisttodo = new ArrayList<String>();
        alisttext = new ArrayList<String>();
        alistTime = new ArrayList<String>();
        alistcategory = new ArrayList<String>();
        scroll = (ScrollView) findViewById(R.id.scrollview);
        scroll.setVerticalScrollBarEnabled(true);

        final Intent mapitt = new Intent(this, MapsActivity.class);
        Button mapbtn = (Button) findViewById(R.id.viewMapbtn);

        //- SDK 23버전 이상 (마시멜로우 이상부터)부터는 아래 처럼 권한을 사용자가 직접 허가해주어야 GPS기능을 사용가능 GPS 기능을 사용하기전 위치에 추가해야함
        //체크 퍼미션
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }

        boolean isGps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
////
        if (!isGps && !isNetwork) {
            new AlertDialog.Builder(MainActivity.this)
                    .setMessage("GPS가 꺼져있습니다.\n ‘위치 서비스’에서 ‘Google 위치 서비스’를 체크해주세요")
                    .setPositiveButton("설정", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(intent);
                            //startActivityForResult(intent,);
                        }

                    })
                    .setNegativeButton("취소", null).show();
            Toast.makeText(getBaseContext(), "Gps turned off ", Toast.LENGTH_LONG).show();
        } else {
            if (isGps) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, interval_time, 0, mll);
                Toast.makeText(this, "GPS로 좌표값을 가져옵니다", Toast.LENGTH_SHORT).show();
            } else if (isNetwork) {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, interval_time, 0, mll);  //3000 -> 3초
                Toast.makeText(this, "네트워크로 좌표값을 가져옵니다", Toast.LENGTH_SHORT).show();
            } else {
                exit(1);
            }
        }
        mapbtn.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                startActivity(mapitt);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - lastTimeBackPressed < 1500) {
            finish();
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            }
            locationManager.removeUpdates(mll);
            return;
        }
        Toast.makeText(MainActivity.this, "'뒤로' 버튼을 한번 더 누르면 종료됩니다", Toast.LENGTH_SHORT).show();
        lastTimeBackPressed = System.currentTimeMillis();
    }

    public void onClick(View v) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }
        locationManager.removeUpdates(mll);
        Toast.makeText(MainActivity.this, "더이상 GPS 정보롤 받아오지 않습니다", Toast.LENGTH_SHORT).show();
    }

    public class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            mLocation = location;
            String str = "Latitude: " + location.getLatitude() + "\n" + "Longitude: " + location.getLongitude() + "\n";
            TextView tv = (TextView) findViewById(R.id.textview);
            tv.append(str);
            Toast.makeText(getBaseContext(), str, Toast.LENGTH_SHORT).show();

            latitudedouble = location.getLatitude();
            longitudedouble = location.getLongitude();

            dbManager.insert(latitudedouble, longitudedouble);

            alistlatitude.add(iter, latitudedouble);
            alistlongitude.add(iter, longitudedouble);
            alistlocation.add(iter, new LatLng(alistlatitude.get(iter), alistlongitude.get(iter)));
            alisttodo.add(iter, iter+"");
            alisttext.add(iter, "");
            alistTime.add(iter,"");
            alistcategory.add(iter,"");
            iter++;
            Log.i("저장", "성공");
            dbManager.getResult();
            Toast.makeText(MainActivity.this, "DB에 입력 되었습니다.", Toast.LENGTH_SHORT).show();

            mLocation = location;
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {
            Toast.makeText(getBaseContext(), "Gps turned on ", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onProviderDisabled(String provider) {
            new AlertDialog.Builder(MainActivity.this)
                    .setMessage("GPS가 꺼져있습니다.\n ‘위치 서비스’에서 ‘Google 위치 서비스’를 체크해주세요")
                    .setPositiveButton("설정", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(intent);
                        }

                    })
                    .setNegativeButton("취소", null).show();
            Toast.makeText(getBaseContext(), "Gps turned off ", Toast.LENGTH_LONG).show();
        }
    }

    public void onclicklocationbtn(View v) {
        class mLocationListener implements LocationListener {
            @Override
            public void onLocationChanged(Location location) {
                Double la = location.getLatitude();
                Double lo = location.getLongitude();
                String str = "Latitude: " + location.getLatitude() + "\n" + "Longitude: " + location.getLongitude() + "\n";
                TextView tv = (TextView) findViewById(R.id.textview);
                tv.append(str);
                Toast.makeText(getBaseContext(), str, Toast.LENGTH_SHORT).show();
                dbManager.insert(la, lo);

                alistlatitude.add(iter, la);
                alistlongitude.add(iter, lo);
                alistlocation.add(iter, new LatLng(alistlatitude.get(iter), alistlongitude.get(iter)));
                alisttodo.add(iter, iter+"");
                alisttext.add(iter,"");
                alistTime.add(iter,"");
                alistcategory.add(iter,"");
                iter++;
                Log.d("lis", "한바퀴돔");
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
            }
        }
    }


}



