package com.example.baolach.driving_app_3;
// this is the locations tab on the main screen - this will show the hillstart/turns locations etc once clicked


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.esri.android.map.MapView;




public class MapsActivity extends AppCompatActivity {

    MapView mv;

    // /private GoogleMap mMap;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_maps);


        // this refers to the mapView map1 in the content_maps.xml
        // uses xml to show the map
        mv = (MapView) findViewById(R.id.map1);
        mv.getLocationDisplayManager().setShowLocation(true);
        mv.getLocationDisplayManager().start();



        /*
         // Adds the different feature layers to the map if you want eg. rivers, roads, mountains
        // uses java to show the map
        // create layer object to be wrapped with a basemap url
        ArcGISTiledMapServiceLayer baseMap = new ArcGISTiledMapServiceLayer(
                this.getResources().getString(R.string.basemapUrl));
        // call the function add layer to display the basemap
        mv.addLayer(baseMap);

        // create another map layer - wrap with url2
        ArcGISTiledMapServiceLayer MapLayer2 = new ArcGISTiledMapServiceLayer(
                this.getResources().getString(R.string.basemapUrl2));
        mv.addLayer(MapLayer2);

        */



        // to enable map continuously
        mv.enableWrapAround(true);

        // creates envelope object to hold two sets of coordinates which form a rectangle map extent
//        Envelope myEnv = new Envelope(-14029650,3560436,-12627306,5430229); // sets extent to california - 53.33866341,-6.36983871,53.30605714,-6.26083374
//        mv.setExtent(myEnv);


        /*
        // java way of implementing web map
        // because the map is set to public anyone can see - may change that
        mv = new MapView(this, this.getResources().getString(R.string.webUrl),null,null);
        setContentView(mv);
        */

    }





} // end class




