package com.taek_aaa.mylocationlogger;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    // ArrayList<LatLng> alistlocation = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        MapsInitializer.initialize(getApplicationContext());
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        MainActivity mact = new MainActivity();
       // alistlocation = new ArrayList<LatLng>();

        //double latitude = mact.latitudedouble;
        //double longitude = mact.longitudedouble;

       // double latitude = mact.dbManager.curlatitude;
        //double longitude = mact.dbManager.curlongitude;

        int j=0;
        int ttt = mact.alistlongitude.size();
        Log.d("test",String.valueOf(ttt));
        for(int i=0; i<ttt; i++) {
            int te = mact.alistlocation.size();
            String test = String.valueOf(te);
            int te2 = mact.alistlongitude.size();
            String test2 = String.valueOf(te2);
            Log.d("test",test);
            Log.d("test",test2);


            MarkerOptions opt = new MarkerOptions();
            opt.position(mact.alistlocation.get(i));
            opt.title("here you are!"+(i+1));
            mMap.addMarker(opt).showInfoWindow();
            //mMap.addMarker(new MarkerOptions().position(whereyouat).title("Found you!"));
            //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(whereyouat, 15));
            Log.d("test","한바퀴돔");
            j=i;
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLng(mact.alistlocation.get(j-1)));
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.animateCamera(CameraUpdateFactory.zoomTo(14));


        // LatLng whereyouat = new LatLng(latitude, longitude);
        // mMap.addMarker(new MarkerOptions().position(whereyouat).title("Found you!"));
        // mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(whereyouat, 15));
        // mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Toast.makeText(getApplicationContext(), marker.getTitle() + "클릭했음", Toast.LENGTH_SHORT).show();
                return false;

            }
        });


    }
}
