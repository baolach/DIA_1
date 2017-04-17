package com.example.baolach.driving_app_3;
// this is the locations tab on the main screen - this will show the hillstart/turns locations etc once clicked


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class MapsActivity extends AppCompatActivity {

    MapView mv;
    MapViewHelper mvHelper;
    Point pt;

    private Button add_btn; // posts to db

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_maps);

        // this refers to the mapView map1 in the content_maps.xml
        // uses xml to show the map
        mv = (MapView) findViewById(R.id.map1);
        mvHelper = new MapViewHelper(mv);


        // sets home pin as limekiln road - need to set to current location
        mv.setOnStatusChangedListener(new OnStatusChangedListener() {
            @Override
            public void onStatusChanged(Object o, STATUS status) {
                // this is for the home pin - need to change to current location

                //Envelope myEnv = new Envelope(-6.31925242098891271,53.30991193101343,-6.31925242098891271,53.3049648969255259); mv.setExtent(myEnv);

                mv.centerAndZoom(53.304679, -6.330082, 16); // Limekiln road
                String title = "Location";
                String detail = "Limekiln Road";
                mvHelper.addMarkerGraphic(53.304679, -6.330082, title, detail, "",
                        ContextCompat.getDrawable(getApplicationContext(), R.drawable.tree40), false, 0);

            }
        });




        // if map clicked to add a pin
        mv.setOnSingleTapListener(new OnSingleTapListener() {
            @Override
            public void onSingleTap(final float x, final float y) {

                    // posts to database
                add_btn = (Button) findViewById(R.id.add_btn);

                // on click, draw the pin but dont add to the database until the user clicks add - double check this works
                pt = mv.toMapPoint(x, y);
                // once a point is tapped, makes a new point calling geometryEngine
                Point wgsPoint =  (Point) GeometryEngine.project(pt,mv.getSpatialReference(),SpatialReference.create(4326));
                // draws to the map
                mvHelper.addMarkerGraphic(wgsPoint.getY(), wgsPoint.getX(),"","",R.drawable.pinimage,
                        ContextCompat.getDrawable(getApplicationContext(), R.drawable.pin20),false,0);



                // confirms the instructor wants to add this location
                    final AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
                    builder.setTitle("Are you sure you want to add this location to your database?");
                    builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            pt = mv.toMapPoint(x, y);

                            // once a point is tapped, makes a new point calling geometryEngine
                            Point wgsPoint = (Point) GeometryEngine.project(pt, mv.getSpatialReference(), SpatialReference.create(4326));
                            final Double lon = wgsPoint.getX();
                            final Double lat = wgsPoint.getY();

                            if(lat != null){
                                Intent i = new Intent(MapsActivity.this, InsertLocation.class);
                                i.putExtra("thelocationx", lat);
                                i.putExtra("thelocationy", lon);
                                startActivity(i);
                                finish();
                            }else{
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        Toast.makeText(getBaseContext(), "You must select a coordinate", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                            System.out.println("lon: " + lon);
                            System.out.println("lat: " + lat);
                            System.out.println("pt: " + pt);
                            System.out.println("mv: " + mv);

                        }

                    });

                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Do nothing
                            dialog.dismiss();
                        }
                    });
                    add_btn.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            builder.show();
                        }
                    });

            } // end single tap
        });

        // clears check box and outputs what button was click
        // I was to put this text in a variable and post it to the database in a locations table along with the location of a pin
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioGroup.clearCheck();
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                final RadioButton selected = (RadioButton) group.findViewById(checkedId);

                // if a button is selected and its in the radioGroup
                if (null != selected && checkedId > -1) {
                    Thread thread = new Thread() {

                        public void run() {
                            try {

                                PreparedStatement st = null;
                                Class.forName("org.postgresql.Driver");
                                String url = "jdbc:postgresql://138.68.141.18:5432/fypdia2"; // uses driver to interact with database
                                Connection conn = DriverManager.getConnection(url, "root", "Cassie2007"); // connects to database
                                // prepares the sql statement
                                String select = "select * from getdata_getlocation where location_type = '" + selected.getText().toString() + "';"; //
                                System.out.println("select stmt: " + select);

                                st = conn.prepareStatement(select);
                                ResultSet rs = st.executeQuery();
                                System.out.println("rs: " + rs);

                                while (rs.next()) {
                                    final int g = 0;
                                    final String loType = rs.getString("location_type");
                                    final double loX = rs.getDouble("location_x");
                                    final double loY = rs.getDouble("location_y");
                                    final String loDetail = rs.getString("location_detail");

                                    // draws to the map
                                    mvHelper.addMarkerGraphic(loX, loY, loType, loDetail, R.drawable.pinimage,
                                            ContextCompat.getDrawable(getApplicationContext(), R.drawable.pin20), false, 0);

                                }

                                st.close();
                                conn.close();
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }

                    };
                    thread.start();

                } // end if
            }
        }); // end radioGroup
        // to enable map continuously
        mv.enableWrapAround(true);


    } // end onCreate


    // inflates the xml file, the gps image to the view
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.maps, menu);

        return true;
    }

    // whatever menu item is clicked and performs case
    @Override public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId(); // gets menu item id if there are more than 1
        // in case I add other menu items later
        switch (id) {
            case R.id.my_gps:
                // turn on/off the gps
                if (mv.getLocationDisplayManager().isStarted()){
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

    public void goBackScreen(View view) {
        try {
            Intent lastScreen = new Intent(MapsActivity.this, MainActivity.class);
            startActivity(lastScreen);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}


