package com.example.baolach.driving_app_3;
// this is the locations tab on the main screen - this will show the hillstart/turns locations etc once clicked


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class MapsActivity extends AppCompatActivity {

    MapView mv;
    MapViewHelper mvHelper;
    Point pt;

    private RadioButton radioButton;
    private Button add_btn; // posts to db

    EditText title, detail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_maps);

        // this refers to the mapView map1 in the content_maps.xml
        // uses xml to show the map
        mv = (MapView) findViewById(R.id.map1);
        mvHelper = new MapViewHelper(mv);


//        mv.getLocationDisplayManager().setShowLocation(true);
//        mv.getLocationDisplayManager().start();

        // sets home pin as limekiln road - need to set to current location
        mv.setOnStatusChangedListener(new OnStatusChangedListener() {
            @Override
            public void onStatusChanged(Object o, STATUS status) {
                // this is for the home pin - need to change to current location
                mv.centerAndZoom(53.304679, -6.330082, 16); // Limekiln road
                String title = "Location";
                String detail = "Limekiln Road";
                mvHelper.addMarkerGraphic(53.304679, -6.330082, title, detail, "",
                        ContextCompat.getDrawable(getApplicationContext(), R.drawable.tree40), false, 0);


////////////////////////////////////////////////////////////////////////////////
//                double[] lat = new double[3];
//                lat = new double[]{53.30828189727125,53.30643561067037,53.3056150132202,53.304422554441004};
//
//                double[] lon = new double[3];
//                lon = new double[]{-6.329502642852769, -6.328172267181346,-6.336841166717707, -6.323945105773764};
//
//                for(int i = 0; i <4 ; i++){
//                    // this is for the home pin - need to change to current location
//                    //mv.centerAndZoom(lat[i], lon[i], 16); // Limekiln road
//                    String newtitle = "Reverse";
//                    String newdetail = "Limekiln area";
//
//                    mvHelper.addMarkerGraphic(lat[i], lon[i],newtitle, newdetail, "",
//                            ContextCompat.getDrawable(getApplicationContext(), R.drawable.pin20),false,0);
//                }

                                              ////////////////////////////////////////////////////////////////////////////////

            }
        });




        // if map clicked to add a pin
        mv.setOnSingleTapListener(new OnSingleTapListener() {
            @Override
            public void onSingleTap(float x, float y) {

                // this is when you're adding new pins
                // I want these added to the database
                pt = mv.toMapPoint(x, y);
                String title = "Reverse";
                String detail = "Reverse around the corner";

                // once a point is tapped, makes a new point calling geometryEngine
                Point wgsPoint =  (Point) GeometryEngine.project(pt,mv.getSpatialReference(),SpatialReference.create(4326));
                final double lon = wgsPoint.getX();
                final double lat = wgsPoint.getY();

                //Point p = wgsPoint.getX();

                System.out.println("lon: " +lon);
                System.out.println("lat: " +lat);
                System.out.println("pt: " + pt);
                System.out.println("mv: " + mv);


                // draws to the map
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

//                                    System.out.println("loType: " + loType);
//                                    System.out.println("loX: " + loX);
//                                    System.out.println("loY: " + loY);


                                    // draws to the map
                                    mvHelper.addMarkerGraphic(loX, loY, loType, "add detail column to location type", R.drawable.pinimage,
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
//
                    ///////////////////////////
                    // add
                    // posts to database
                    add_btn = (Button) findViewById(R.id.add_btn);

                    add_btn.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            // used for inserting into database
                            new Thread(new Runnable() {
                                public void run() {
                                    insert();
                                }

                            }).start();
                        }

                        protected void insert() {

                            try {
                                String type = selected.getText().toString();
                                double loc_x = 53.30828189727125;
                                double loc_y = -6.329502642852769;

                                String insertPoint;
                                // i need x and y to go into the db when adding a location by tap


                                PreparedStatement insertdb;
                                Class.forName("org.postgresql.Driver");
                                String url = "jdbc:postgresql://138.68.141.18:5432/fypdia2"; // uses driver to interact with database
                                Connection conn = DriverManager.getConnection(url, "root", "Cassie2007"); // connects to database

                                // prepares the sql statement
                                String insert = "insert into getdata_getlocation values (?, ?, ?) ;";//, ?, ?)"; // , ?)";
                                insertdb = conn.prepareStatement(insert);
                                System.out.println("insertdb: " + insertdb);
                                insertdb.setString(1, type);
                                System.out.println("type: " + type);

                                insertdb.setDouble(2, loc_x );
                                System.out.println("locx: " + loc_x);

                                insertdb.setDouble(3, loc_y );
                                System.out.println("locy: " + loc_y);



                                insertdb.execute();
                                insertdb.close(); // close connection must be done

                                // once inserted into database goes back to maps to show it in the db
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        Toast.makeText(getBaseContext(), "Location added to database! ", Toast.LENGTH_LONG).show();
                                        Intent l = new Intent(MapsActivity.this, MapsActivity.class); //refreshes the map
                                        startActivity(l);

                                    }
                                });


                                insertdb.close();
                                conn.close();

                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }


                        }
                    });

            }
        });

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
                if (mv.getLocationDisplayManager().isStarted()){ // && id == R.id.my_gps) {
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


