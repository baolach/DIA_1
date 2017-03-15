package com.example.baolach.driving_app_3;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.IBinder;
import android.provider.Settings;
import android.renderscript.Sampler;
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
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    // added
//    private SensorManager sensorManager;
//    private Sensor accelerometer;
//    long lastUpdate = 0;
//    private float last_x, last_y, last_z;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();

//        // added - set up accelerometer
//        // sets up sensorManage declared above
//        sensorManager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);
//        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
//        sensorManager.registerListener((SensorEventListener) this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
//
//        // register a listener so it knows to listen for accelerometer
//

    }

//    //added
//    public void onSensorChanged(SensorEvent event){
//        Sensor mySensor = event.sensor;
//
//        if(mySensor.getType() == Sensor.TYPE_ACCELEROMETER){
//            float x = event.values[0]; // x value
//            float y = event.values[1];
//            float z = event.values[2];
//
//            long curTime = System.currentTimeMillis();
//
//            if(Math.abs(curTime - lastUpdate) >2000){
//                SimpleDateFormat date = new SimpleDateFormat("dd-MM-yyyy");
//                String currentDateTime = date.format(new Date());
//
//                lastUpdate = curTime;
//                if(Math.abs(last_x - x) > 10){
//                    mMap.addMarker(new MarkerOptions()
//                    .position(new LatLng(37.23062, -00.42176))
//                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
//                            .title("Hey x axis"  + currentDateTime));
//                }
//
//                if(Math.abs(last_y - y) > 10){
//                    mMap.addMarker(new MarkerOptions()
//                            .position(new LatLng(37.26062, -00.4218))
//                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
//                            .title("Hey y axis"  + currentDateTime));
//                }
//
//                if(Math.abs(last_z - z) > 10){
//                    mMap.addMarker(new MarkerOptions()
//                            .position(new LatLng(37.23062, -00.43176))
//                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
//                            .title("Hey z axis"  + currentDateTime));
//                }
//
//                last_x = x;
//                last_y = y;
//                last_z = z;
//
//            }
//
//
//
//        }
//    }
//    public void onAccuracyChanged(Sensor sensor, int accuracy){
//
//    }


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
        //mMap.addMarker(new MarkerOptions().position(new LatLng(37.229, -80.424)).title("Marker Test"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(37.229, -80.424), 14.9f));
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
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(37.229, -80.424)).title("Marker Test"));

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


