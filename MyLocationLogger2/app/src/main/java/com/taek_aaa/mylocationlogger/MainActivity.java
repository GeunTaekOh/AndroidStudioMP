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
    public static ArrayList<Double> alistlatitude = null;
    public static ArrayList<Double> alistlongitude = null;
    public static ArrayList<LatLng> alistlocation = null;

   // int int_syear, int_smonth, int_sdate, int_shour, int_sminute;
   // Calendar cal_start;

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
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 0, mll);
                Toast.makeText(this, "GPS로 좌표값을 가져옵니다", Toast.LENGTH_SHORT).show();
            } else if (isNetwork) {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 3000, 0, mll);  //3000 -> 3초
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

   /* public class AddTodo {
        public void onAddClicked(View view) {    //일정 추가 버튼

            DisplayMetrics dm = getResources().getDisplayMetrics();
            int sizeDpi = Math.round(1 * dm.densityDpi); //DIP 설정법

            final Calendar today;
            today = Calendar.getInstance();

            LinearLayout linear_Summary = new LinearLayout(MainActivity.this);  //일정제목 레이아웃
            linear_Summary.setPadding(sizeDpi / 2, sizeDpi / 10, sizeDpi / 2, 0);
            final EditText edit_Summary = new EditText(MainActivity.this);  //일정제목 입력받을 에딧텍스트
            edit_Summary.setHint("일정 제목을 입력하세요.");
            edit_Summary.setHintTextColor(0x50000000);
            edit_Summary.setEms(12);
            linear_Summary.addView(edit_Summary);
            // editscreen.addView(linear_Summary);

            int_syear = today.get(Calendar.YEAR);   //시작 날짜 및 시간 초기화
            int_smonth = today.get(Calendar.MONTH);
            int_sdate = today.get(Calendar.DAY_OF_MONTH);
            int_shour = today.get(Calendar.HOUR_OF_DAY);
            int_sminute = today.get(Calendar.MINUTE);

            cal_start = Calendar.getInstance();
            cal_start.set(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH), today.get(Calendar.HOUR_OF_DAY), today.get(Calendar.MINUTE), 0);   //현재 날짜 및 시간으로 세팅

          /*  DateTime dt = new DateTime(cal_start.getTimeInMillis());

            update_start = new EventDateTime();
            update_start.setDateTime(dt);

            LinearLayout linear_Start = new LinearLayout(MainActivity.this);    //시작시간 표시할 레이아웃
            linear_Start.setPadding(sizeDpi/3,0,sizeDpi/3,0);
            TextView text_start = new TextView(MainActivity.this);
            text_start.setText("시작\t\t\t");
            linear_Start.addView(text_start);
            final Button button_startdate = new Button(MainActivity.this);  //시작 날짜 데이트피커 띄울 버튼
            String string_startdate;
            SimpleDateFormat tmpDF = new SimpleDateFormat("yyyy/MM/dd(E)");
            Date tmpD = new Date(update_start.getDateTime().getValue());
            string_startdate = tmpDF.format(tmpD);  //오늘 날짜 스트링
            button_startdate.setText(string_startdate);
            button_startdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {


                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            // TODO Auto-generated method stub

                            cal_start = Calendar.getInstance();
                            cal_start.set(year, monthOfYear, dayOfMonth, int_shour, int_sminute, 0);   //데이트피커에서 받은 정보 저장
                            int_syear = year;
                            int_smonth = monthOfYear;
                            int_sdate = dayOfMonth;

                            DateTime dt = new DateTime(cal_start.getTimeInMillis());

                            update_start.setDateTime(dt);   //DateTime 세팅

                            SimpleDateFormat tmpDF = new SimpleDateFormat("yyyy/MM/dd(E)");
                            Date tmpD = new Date(dt.getValue());
                            String str = tmpDF.format(tmpD);
                            button_startdate.setText(str);  //데이트피커에서 받은 정보로 버튼텍스트 변경

                        }
                    };

                    new DatePickerDialog(MainActivity.this, dateSetListener, int_syear, int_smonth, int_sdate).show();

                }
            });
            linear_Start.addView(button_startdate);
            final Button button_starttime = new Button(MainActivity.this);  //시작 시간 타임피커 띄울 버튼
            String string_starttime;
            SimpleDateFormat tmpDF2 = new SimpleDateFormat("aa hh:mm");
            Date tmpD2 = new Date(update_start.getDateTime().getValue());
            string_starttime = tmpDF2.format(tmpD2);    //현재 시간으로 초기화
            button_starttime.setText(string_starttime);
            button_starttime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            // TODO Auto-generated method stub

                            cal_start.set(Calendar.HOUR_OF_DAY, hourOfDay); //타임피커에서 받은 정보로 업데이트
                            cal_start.set(Calendar.MINUTE, minute);
                            int_shour = hourOfDay;
                            int_sminute = minute;
                            DateTime dt = new DateTime(cal_start.getTimeInMillis());

                            update_start.setDateTime(dt);

                            SimpleDateFormat tmpDF = new SimpleDateFormat("aa hh:mm");
                            Date tmpD = new Date(dt.getValue());
                            String str = tmpDF.format(tmpD);
                            button_starttime.setText(str);

                        }


                    };
                    new TimePickerDialog(MainActivity.this, timeSetListener, int_shour, int_sminute, false).show();

                }
            });
            linear_Start.addView(button_starttime);
            editscreen.addView(linear_Start);

            int_eyear = today.get(Calendar.YEAR);   //종료 날짜 및 시간 초기화, 시간만 +1
            int_emonth = today.get(Calendar.MONTH);
            int_edate = today.get(Calendar.DAY_OF_MONTH);
            int_ehour = today.get(Calendar.HOUR_OF_DAY) + 1;
            int_eminute = today.get(Calendar.MINUTE);

            cal_end = Calendar.getInstance();
            cal_end.set(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH), (today.get(Calendar.HOUR_OF_DAY) + 1), today.get(Calendar.MINUTE), 0);

            DateTime dt2 = new DateTime(cal_end.getTimeInMillis());

            update_end = new EventDateTime();
            update_end.setDateTime(dt2);

            LinearLayout linear_End = new LinearLayout(MainActivity.this);
            linear_End.setPadding(sizeDpi/3,0,sizeDpi/3,0);
            TextView text_end = new TextView(MainActivity.this);
            text_end.setText("종료\t\t\t");
            linear_End.addView(text_end);
            final Button button_enddate = new Button(MainActivity.this);    //종료 날짜 데이트피커 띄울 버튼
            String string_enddate;
            SimpleDateFormat tmpDF3 = new SimpleDateFormat("yyyy/MM/dd(E)");
            Date tmpD3 = new Date(update_end.getDateTime().getValue());
            string_enddate = tmpDF3.format(tmpD3);
            button_enddate.setText(string_enddate);
            button_enddate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {


                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            // TODO Auto-generated method stub

                            cal_end = Calendar.getInstance();
                            cal_end.set(year, monthOfYear, dayOfMonth, today.get(Calendar.HOUR_OF_DAY) + 1, today.get(Calendar.MINUTE), 0);
                            int_eyear = year;
                            int_emonth = monthOfYear;
                            int_edate = dayOfMonth;

                            DateTime dt = new DateTime(cal_end.getTimeInMillis());

                            update_end.setDateTime(dt);

                            SimpleDateFormat tmpDF = new SimpleDateFormat("yyyy/MM/dd(E)");
                            Date tmpD = new Date(dt.getValue());
                            String str = tmpDF.format(tmpD);
                            button_enddate.setText(str);

                        }
                    };

                    new DatePickerDialog(MainActivity.this, dateSetListener, int_syear, int_smonth, int_sdate).show();

                }
            });
            linear_End.addView(button_enddate);
            final Button button_endtime = new Button(MainActivity.this);    //종료 시간 타임피커 띄울 버튼
            String string_endtime;
            SimpleDateFormat tmpDF4 = new SimpleDateFormat("aa hh:mm");
            Date tmpD4 = new Date(update_end.getDateTime().getValue());
            string_endtime = tmpDF4.format(tmpD4);
            button_endtime.setText(string_endtime);
            button_endtime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            // TODO Auto-generated method stub

                            cal_end.set(Calendar.HOUR_OF_DAY, hourOfDay);
                            cal_end.set(Calendar.MINUTE, minute);

                            int_ehour = hourOfDay;
                            int_eminute = minute;

                            DateTime dt = new DateTime(cal_end.getTimeInMillis());

                            update_end.setDateTime(dt);

                            SimpleDateFormat tmpDF = new SimpleDateFormat("aa hh:mm");
                            Date tmpD = new Date(dt.getValue());
                            String str = tmpDF.format(tmpD);
                            button_endtime.setText(str);

                        }


                    };
                    new TimePickerDialog(MainActivity.this, timeSetListener, int_shour, int_sminute, false).show();

                }
            });
            linear_End.addView(button_endtime);
            editscreen.addView(linear_End);



            LinearLayout linear_Allday = new LinearLayout(MainActivity.this);
            linear_Allday.setPadding(sizeDpi + sizeDpi/3,0,sizeDpi/5,0);
            TextView text_allday = new TextView(MainActivity.this);
            text_allday.setText("하루종일");
            linear_Allday.addView(text_allday);
            final CheckBox check_allday = new CheckBox(MainActivity.this);  //하루종일 체크박스

            check_allday.setChecked(false); //추가시엔 체크안됨으로 초기설정.

            linear_Allday.addView(check_allday);
            editscreen.addView(linear_Allday);


            android.support.v7.app.AlertDialog ad = new android.support.v7.app.AlertDialog.Builder(MainActivity.this)

                    .setView(editscreen)
                    .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {   //취소시엔 아무 동작없이 창 종료

                        }
                    })
                    .setPositiveButton("추가", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {   //추가버튼 클릭 시

                            bc = tmpEvents.getItems().get(0).getId();
                            update_summary = edit_Summary.getText().toString(); //일정 제목 get

                            if(check_allday.isChecked()){   //하루종일 체크 시
                                cal_start.set(Calendar.HOUR_OF_DAY, 0);
                                cal_start.set(Calendar.MINUTE, 0);

                                DateTime dt = new DateTime(cal_start.getTimeInMillis());

                                update_start.setDateTime(dt);

                                cal_end.set(Calendar.HOUR_OF_DAY, 23);
                                cal_end.set(Calendar.MINUTE, 59);

                                DateTime dt2 = new DateTime(cal_end.getTimeInMillis());

                                update_end.setDateTime(dt2);
                            }

                            try {   //인서트 실행
                                insertItem ii = new insertItem();
                                ii.execute();
                            } catch (Exception e) {
                            }

                        }
                    })
                    .show();
        }*/
}



