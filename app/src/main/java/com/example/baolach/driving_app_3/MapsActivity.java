package com.example.baolach.driving_app_3;
// this is the locations tab on the main screen - this will show the hillstart/turns locations etc once clicked


import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.esri.android.map.MapView;
import com.esri.android.map.event.OnSingleTapListener;
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.android.toolkit.map.MapViewHelper;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;


public class MapsActivity extends AppCompatActivity {

    MapView mv;
    MapViewHelper mvHelper;
    Point pt;
    // /private GoogleMap mMap;
    GestureDetector gestureDetector;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private Button add_btn;
    EditText title, detail;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_maps);


        // this refers to the mapView map1 in the content_maps.xml
        // uses xml to show the map
        mv = (MapView) findViewById(R.id.map1);
        mvHelper = new MapViewHelper(mv);
        addListenerOnButton();


        // mapview set to the listener
        mv.setOnStatusChangedListener(new OnStatusChangedListener() {
            @Override
            public void onStatusChanged(Object o, STATUS status) {

                // this is if you want an automatic pin - maybe to show your current locaion
                // or maybe you want to output all the stored pins from the db
                mv.centerAndZoom(53.304679, -6.330082, 16); // Limekiln road
                      String title = "Location";
                      String detail = "Limekiln Road";

                mvHelper.addMarkerGraphic(53.304679, -6.330082,title, detail, "",
                            ContextCompat.getDrawable(getApplicationContext(), R.drawable.tree40),false,0);


            } // end setOnStatusChangedListener
        });

        mv.setOnSingleTapListener(new OnSingleTapListener() {
            @Override
            public void onSingleTap(float x, float y) {

                // this is when you're adding new pins
                // I want these added to the database
                pt = mv.toMapPoint(x, y);
                // once a point is tapped, makes a new point calling geometryEngine
                Point wgsPoint =  (Point) GeometryEngine.project(pt,mv.getSpatialReference(),SpatialReference.create(4326));


                String title = "Reverse";
                String detail = "Reverse around the corner";

                // adds to the map
                mvHelper.addMarkerGraphic(wgsPoint.getY(), wgsPoint.getX(),title,detail,R.drawable.car,
                        ContextCompat.getDrawable(getApplicationContext(), R.drawable.pin20),false,0);

            }

            // when you click the picture you should be able to edit the title and detail
//                mv.setOnGraphicClickListener(new OnGraphicClickListener() {
//                    @Override
//                    public void onGraphicClick(Graphic graphic) {
//                        double gX, gY;
//                        mvHelper.setShowGraphicCallout(false);
//
////                        Toast.makeText(ctx, String.valueOf(newPoint.getX()) + " "
////                                + String.valueOf(newPoint.getY()), Toast.LENGTH_LONG).show();
//                    }
//                });


        });





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



    } // end onCreate


    public void addListenerOnButton() {

        radioGroup = (RadioGroup) findViewById(R.id.radio);
        add_btn = (Button) findViewById(R.id.add_btn);

        add_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                System.out.println("selectedId about to run");
                try {
                    // get selected radio button from radioGroup
                    int selectedId = radioGroup.getCheckedRadioButtonId();

                    //W/System.err: java.lang.NullPointerException: Attempt to invoke virtual method 'int android.widget.RadioGroup.getCheckedRadioButtonId()' on a null object reference
                    System.out.println("selectedId: " + selectedId);


                    // find the radiobutton by returned id
                    radioButton = (RadioButton) findViewById(selectedId);
                    System.out.println("radio button: " + radioButton);

                    if(radioButton!=null) {
                        Toast.makeText(MapsActivity.this,
                                radioButton.getText(), Toast.LENGTH_SHORT).show();
                    }


                } catch(Exception e){
                    System.out.println("######### radio button not read in as selected");
                    e.printStackTrace();
                }

            }

        });
    }


    // inflates the xml file, the gps image to the view
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.maps, menu);

        return true;
    }

    @Override public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId(); // gets menu item id if there are more than 1
        // in case I add other menu items later
        switch (id) {
            case R.id.my_gps:
                // turn on/off the gps
                if (id == R.id.my_gps && mv.getLocationDisplayManager().isStarted()) {
                    Toast.makeText(getApplicationContext(), "GPS is de-activated!", Toast.LENGTH_SHORT).show();
                    mv.getLocationDisplayManager().setShowLocation(false);
                    mv.getLocationDisplayManager().stop();
                    return true;
                } else {
                    Toast.makeText(getApplicationContext(), "GPS is activated!", Toast.LENGTH_SHORT).show();
                    mv.getLocationDisplayManager().setShowLocation(true);
                    mv.getLocationDisplayManager().start();
                }

            case R.id.clear:
                mvHelper.removeAllGraphics();

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}






