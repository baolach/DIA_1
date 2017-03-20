package com.example.baolach.driving_app_3;
// this is the locations tab on the main screen - this will show the hillstart/turns locations etc once clicked


import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISTiledMapServiceLayer;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.GoogleMap;




public class MapsActivity extends AppCompatActivity {

    MapView mv;

    // /private GoogleMap mMap;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_maps);
        setUpMapIfNeeded();

        // this refers to the mapView map1 in the content_maps.xml
        // uses xml to show the map
        mv = (MapView) findViewById(R.id.map1);

        // uses hava to show the map
        // create layer object to be wrapped with a basemap url
        ArcGISTiledMapServiceLayer baseMap = new ArcGISTiledMapServiceLayer(
                this.getResources().getString(R.string.basemapUrl));
        // call the function add layer to display the basemap
        mv.addLayer(baseMap);

        // create another map layer - wrap with url2
        ArcGISTiledMapServiceLayer MapLayer2 = new ArcGISTiledMapServiceLayer(
                this.getResources().getString(R.string.basemapUrl2));
        mv.addLayer(MapLayer2);

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
//    @Override
    public void onMapReady(GoogleMap googleMap) {
//        mMap = googleMap;
//
//        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.addMarker(new MarkerOptions().position(new LatLng(37.229, -80.424)).title("Marker Test"));
//
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            //your code here
        }
    };



} // end class




