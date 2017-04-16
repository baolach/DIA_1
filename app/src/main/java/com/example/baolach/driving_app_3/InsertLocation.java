package com.example.baolach.driving_app_3;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Baolach on 16/04/2017.
 */

public class InsertLocation extends Activity {

    EditText locationType;
    TextView locationX, locationY;
    EditText locationDetail;

    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.insert_location_details);
//
//        Intent intent = getIntent();
//        Bundle bundle = intent.getExtras();
//        // bundle captures the parameters from the intent fron SelectClient
//        if (bundle != null) {
//            locationtype = bundle.getString("thelocationtype");
//            locationx = bundle.getString("thelocationx");
//            locationy = bundle.getString("thelocationy");
//            locationdetail = = bundle.getString("thelocationdetail");
//
//            System.out.println("id sent:" + clientid);
//
//            // then set the editTexts to these values that just came in
////            locationType = (EditText) findViewById(R.id.editText_locationType);
//            locationX = (TextView) findViewById(R.id.editText_locationX);
//            locationY = (TextView) findViewById(R.id.editText_locationY);
////            locationDetail = (EditText) findViewById(R.id.editText_locationDetail);
//
//            locationX.setText(locationx);
//            locationY.setText(locationy);
//
//        } // end of bundle
//
//        locationType = (EditText) findViewById(R.id.editText_lessonName);
//        locationDetail = (EditText) findViewById(R.id.editText_lessonTime);
//
//
//        // confirms the instructor wants to add this location
//        final AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
//        builder.setTitle("Are you sure you want to add this location to your database?");
//        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                // used for inserting into database
//                new Thread(new Runnable() {
//                    public void run() {
//                        insert();
//                    }
//
//                }).start();
//            }
//
//
//            protected void insert() {
//
//                try {
//                    // this is when you're adding new pins
//                    // I want these added to the database
//                    pt = mv.toMapPoint(x, y);
//                    String title = "Reverse";
//                    String detail = "Reverse around the corner";
//
//                    // once a point is tapped, makes a new point calling geometryEngine
//                    Point wgsPoint =  (Point) GeometryEngine.project(pt,mv.getSpatialReference(), SpatialReference.create(4326));
//                    final double lon = wgsPoint.getX();
//                    final double lat = wgsPoint.getY();
//
//                    //Point p = wgsPoint.getX();
//
//                    System.out.println("lon: " +lon);
//                    System.out.println("lat: " +lat);
//                    System.out.println("pt: " + pt);
//                    System.out.println("mv: " + mv);
//
//
//                    // draws to the map
//                    mvHelper.addMarkerGraphic(wgsPoint.getY(), wgsPoint.getX(),title,detail,R.drawable.pinimage,
//                            ContextCompat.getDrawable(getApplicationContext(), R.drawable.pin20),false,0);
//
//
//                    PreparedStatement insertdb;
//                    Class.forName("org.postgresql.Driver");
//                    String url = "jdbc:postgresql://138.68.141.18:5432/fypdia2"; // uses driver to interact with database
//                    Connection conn = DriverManager.getConnection(url, "root", "Cassie2007"); // connects to database
//
//                    // prepares the sql statement
//                    String insert = "insert into getdata_getlocation values (?, ?, ?) ;";
//                    insertdb = conn.prepareStatement(insert);
//                    System.out.println("insertdb: " + insertdb);
//                    insertdb.setString(1, title); // change to the selected radio button
//                    System.out.println("type: " + title);
//
//                    insertdb.setDouble(2, lat );
//                    System.out.println("lat: " + lat);
//
//                    insertdb.setDouble(3, lon );
//                    System.out.println("lon: " + lon);
//
//
//
//                    insertdb.execute();
//                    insertdb.close(); // close connection must be done
//
//                    // once inserted into database goes back to maps to show it in the db
//                    runOnUiThread(new Runnable() {
//                        public void run() {
//                            Toast.makeText(getBaseContext(), "Location added to database! ", Toast.LENGTH_LONG).show();
//                            Intent l = new Intent(MapsActivity.this, MapsActivity.class); //refreshes the map
//                            startActivity(l);
//
//                        }
//                    });
//
//
//                    insertdb.close();
//                    conn.close();
//
//                } catch (ClassNotFoundException e) {
//                    e.printStackTrace();
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//
//
//            }
//        });
//
//        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                // Do nothing
//                dialog.dismiss();
//            }
//        });
//        add_btn.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                builder.show();
//            }
//        });

    } // end onCreate


}
