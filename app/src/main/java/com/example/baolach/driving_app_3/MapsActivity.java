package com.example.baolach.driving_app_3;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();


    }

    private void setUpMapIfNeeded() {
//        if(mMap == null){
//            mMap((SupportMapFragment).getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
//
//            if(mMap !=null){
//                setUpMap();
//            }
//        }
    }

    private void setUpMap() {
        mMap.addMarker(new MarkerOptions().position(new LatLng(0,0)).title("Marker"));
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            //your code here
        }
    };



} // end class


//    // Obtain the SupportMapFragment and get notified when the map is ready to be used.
//    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//            .findFragmentById(R.id.map);
//mapFragment.getMapAsync(this);
//
//
////        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
////
////        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_REFRESH_TIME,
////                LOCATION_REFRESH_DISTANCE, mLocationListener);
//
//        LocationManager locationManager = (LocationManager)
//        getSystemService(Context.LOCATION_SERVICE);
//
//        GoogleApiClient mGoogleApiClient = new GoogleApiClient.Builder(this)
//        .enableAutoManage(this /* FragmentActivity */,
//        this /* OnConnectionFailedListener */)
//        .addApi(Drive.API)
//        .addScope(Drive.SCOPE_FILE)
//        .build();
//
//        // Create an instance of GoogleAPIClient.
//        if (mGoogleApiClient == null) {
//        mGoogleApiClient = new GoogleApiClient.Builder(this)
//        .addConnectionCallbacks(this)
//        .addOnConnectionFailedListener(this)
//        .addApi(LocationServices.API)
//        .build();
//        }


