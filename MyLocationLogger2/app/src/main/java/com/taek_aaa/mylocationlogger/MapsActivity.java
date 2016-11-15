package com.taek_aaa.mylocationlogger;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.Date;

import static com.google.android.gms.maps.CameraUpdateFactory.newLatLng;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Spinner spinner;
    String type_str = "";
    String[] memo_arr = {"공부", "식사", "카페", "산책"};
    EditText editText;
    LinearLayout type_ll;
    static String outermemo ;
    int slistsize;
    static int temp;
    static int temp2;
    //static MarkerOptions opt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        MapsInitializer.initialize(getApplicationContext());
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //opt = new MarkerOptions();


    }
////////////////////////
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        final MainActivity mact = new MainActivity();
        //Marker mark = new Marker();
        //MarkerOptions opt = new ;


        int listsize = mact.alistlongitude.size();
        slistsize=listsize;
        for (int i = 0; i < listsize; i++) {
            MarkerOptions opt = new MarkerOptions();
            opt.position(mact.alistlocation.get(i));
            opt.title(mact.alisttodo.get(i));
            Log.d("dddd",mact.alisttodo.get(i));
            mMap.addMarker(opt).showInfoWindow();
            if (i != 0) {
                mMap.addPolyline(new PolylineOptions().geodesic(true).add(new LatLng(Double.valueOf(mact.alistlatitude.get(i - 1)), Double.valueOf(mact.alistlongitude.get(i - 1))), new LatLng(Double.valueOf(mact.alistlatitude.get(i)), Double.valueOf(mact.alistlongitude.get(i)))).width(5).color(Color.RED));
            }
        }

        mMap.moveCamera(newLatLng(mact.alistlocation.get(0)));
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.animateCamera(CameraUpdateFactory.zoomTo(14));
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                marker.remove();

                for (int i = 0; i < slistsize; i++){
                    MarkerOptions opt = new MarkerOptions();

                    opt.position(mact.alistlocation.get(i));
                    opt.title(mact.alisttodo.get(i));
                    if(mact.alisttodo.get(i)==null){
                        mact.alisttodo.set(slistsize,""+slistsize);
                        opt.title(mact.alisttodo.get(slistsize));
                        Log.d("dddd",mact.alisttodo.get(slistsize));

                        mMap.addMarker(opt).showInfoWindow();
                    }

                    if (i != 0) {
                        mMap.addPolyline(new PolylineOptions().geodesic(true).add(new LatLng(Double.valueOf(mact.alistlatitude.get(i - 1)), Double.valueOf(mact.alistlongitude.get(i - 1))), new LatLng(Double.valueOf(mact.alistlatitude.get(i)), Double.valueOf(mact.alistlongitude.get(i)))).width(5).color(Color.RED));
                    }
                    /*
                    temp2 =  Integer.valueOf(marker.getTitle());
                    MarkerOptions opt = new MarkerOptions();
                    try{
                        Integer.parseInt(mact.alisttodo.get(i));
                    }catch(Exception e){
                        opt.title(mact.alisttodo.get(temp2));

                    }*/



                }
/*
                temp2 = Integer.valueOf(marker.getTitle());
                MarkerOptions opt = new MarkerOptions();
                try{

                    Integer.parseInt(mact.alisttodo.get(temp2));
                }catch(Exception e){

                    opt.position(mact.alistlocation.get(temp2));
                    opt.title(mact.alisttodo.get(temp2));
                    mMap.addMarker(opt).showInfoWindow();

                }*/


                Toast.makeText(getApplicationContext(), marker.getTitle() + "클릭했음", Toast.LENGTH_SHORT).show();

                return false;
            }
        });

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Date clsTime = new Date();


                int a = Integer.valueOf(marker.getTitle());
//지금 일정제목안받아와지고 일정시간이 현재시간으로계속다받아짐..
                temp=a;

                AlertDialog.Builder adb = new AlertDialog.Builder(MapsActivity.this);
                type_ll = new LinearLayout(MapsActivity.this);
                setSpinner();
                setEditText();
                type_ll.addView(spinner);
                type_ll.addView(editText);
                type_ll.setPadding(50, 0, 0, 0);

                adb
                        .setTitle("메모")
                        .setCancelable(false)
                        .setMessage("메모를 입력해 주세요.")
                        .setView(type_ll)
                        .setPositiveButton("저장", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                outermemo = editText.getText().toString();
                                mact.alisttodo.set(temp,outermemo);
                                temp=0;

                                //outermemo=memo;
                                type_str = "";
                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });


                //mact.alisttodo.iterator().equals().add(outermemo);


                AlertDialog ad = adb.create();
                ad.show();
            }

        });
    }

    public void setSpinner() {
        spinner = new Spinner(this);
        ArrayAdapter memoAdapter = new ArrayAdapter(MapsActivity.this, android.R.layout.simple_spinner_item, memo_arr);
        memoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(memoAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                type_str = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
    public void setEditText(){
        editText = new EditText(this);
        editText.setHint("메모를 입력하세요.");
        editText.setHintTextColor(0x50000000);
        editText.setEms(12);
    }
}
