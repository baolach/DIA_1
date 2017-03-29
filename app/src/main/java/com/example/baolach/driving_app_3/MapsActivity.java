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
import java.sql.SQLException;


public class MapsActivity extends AppCompatActivity {

    MapView mv;
    MapViewHelper mvHelper;
    Point pt;

    private RadioGroup radioGroup;
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
                System.out.println("wgs Point: " + wgsPoint);
                System.out.println("pt: " + pt);
                //System.out.println("mv.getSpatialReference: " + mv.getSpatialReference());


                String title = "Reverse";
                String detail = "Reverse around the corner";
                System.out.println("title: " + title);
                System.out.println("detail: " + detail);


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

        // clears check box and outputs what button was click
        // I was to put this text in a variable and post it to the database in a locations table along with the location of a pin
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioGroup.clearCheck();
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                final RadioButton selected = (RadioButton) group.findViewById(checkedId);
                // if a button is selected and its in the radioGroup
                if (null != selected && checkedId > -1)
                {
                    // post to the database
                    Toast.makeText(MapsActivity.this, selected.getText(), Toast.LENGTH_SHORT).show();

                    // posts to database
                    add_btn = (Button) findViewById(R.id.add_btn);

                    // submit button
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
                                //Point co = pt;

                                PreparedStatement insertdb;
                                Class.forName("org.postgresql.Driver");
                                String url = "jdbc:postgresql://138.68.141.18:5432/fypdia2"; // uses driver to interact with database
                                Connection conn = DriverManager.getConnection(url, "root", "Cassie2007"); // connects to database

                                // prepares the sql statement
                                String insert = "insert into getdata_locations values (?)"; // , ?)";
                                insertdb = conn.prepareStatement(insert);
                                insertdb.setString(1, type);
                                //insertdb.set(2, co);


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

            }
        });

        // to enable map continuously
        mv.enableWrapAround(true);


    } // end onCreate


    public void addListenerOnButton() {

        radioGroup = (RadioGroup) findViewById(R.id.radio);
        //radioGroup.clearCheck();
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






