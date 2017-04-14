package com.example.baolach.driving_app_3;
// this is the locations tab on the main screen - this will show the hillstart/turns locations etc once clicked


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class MapsActivity extends AppCompatActivity {

//    public static void main (String[] args){
//        DownloadWebPageTask onPostExecute = new DownloadWebPageTask();
//
//        double[] array = onPostExecute();
//
//    }



    MapView mv;
    MapViewHelper mvHelper;
    Point pt;

    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private Button add_btn; // posts to db
    String insertPoint;

    EditText title, detail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_maps);




//////////////////////////////////////////////////////////////////////////// needs to be in onCreate
        // connects to the url containing the lessons in the database
        String url = "http://138.68.141.18:8006/locations/?format=json";
        ConnectivityManager connmgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connmgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            new MapsActivity.DownloadWebPageTask().execute(url);
        } else {
            System.out.println("No network available");
        }
////////////////////////////////////////////////////////////////////////////



        // this refers to the mapView map1 in the content_maps.xml
        // uses xml to show the map
        mv = (MapView) findViewById(R.id.map1);
        mvHelper = new MapViewHelper(mv);


        // sets home pin as limekiln road - need to set to current location
        mv.setOnStatusChangedListener(new OnStatusChangedListener() {
            @Override
            public void onStatusChanged(Object o, STATUS status) {


                // this is for the home pin - need to change to current location
                mv.centerAndZoom(53.304679, -6.330082, 16); // Limekiln road
                String title = "Location";
                String detail = "Limekiln Road";

                mvHelper.addMarkerGraphic(53.304679, -6.330082,title, detail, "",
                        ContextCompat.getDrawable(getApplicationContext(), R.drawable.tree40),false,0);


////////////////////////////////////////////////////////////////////////////////
                double[] lat = new double[3];
                lat = new double[]{53.30828189727125,53.30643561067037,53.3056150132202,53.304422554441004};

                double[] lon = new double[3];
                lon = new double[]{-6.329502642852769, -6.328172267181346,-6.336841166717707, -6.323945105773764};

                for(int i = 0; i <4 ; i++){
                    // this is for the home pin - need to change to current location
                    //mv.centerAndZoom(lat[i], lon[i], 16); // Limekiln road
                    String newtitle = "Reverse";
                    String newdetail = "Limekiln area";

                    mvHelper.addMarkerGraphic(lat[i], lon[i],newtitle, newdetail, "",
                            ContextCompat.getDrawable(getApplicationContext(), R.drawable.pin20),false,0);
                }

////////////////////////////////////////////////////////////////////////////////


            } // end setOnStatusChangedListener
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
                double lon = wgsPoint.getX();
                double lat = wgsPoint.getY();

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

//                    hillstart_btn.setOnCl




                    // posts to database
                    add_btn = (Button) findViewById(R.id.add_btn);

                    add_btn.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            // used for inserting into database
                            new Thread(new Runnable()
                            {
                                public void run() {
                                    insert();
                                }

                            }).start();
                        }

                        protected void insert() {

                            try {
                                String type = selected.getText().toString();
                                String insertPoint;

                                PreparedStatement insertdb;
                                Class.forName("org.postgresql.Driver");
                                String url = "jdbc:postgresql://138.68.141.18:5432/fypdia2"; // uses driver to interact with database
                                Connection conn = DriverManager.getConnection(url, "root", "Cassie2007"); // connects to database

                                // prepares the sql statement
                                String insert = "insert into getdata_getlocation values (?, ?)"; // , ?)";
                                insertdb = conn.prepareStatement(insert);
                                insertdb.setString(1, type);
                                insertdb.setString(2, "coordinate");


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
                                System.out.println("type" + type);


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




    // works in the background
    private class DownloadWebPageTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                System.out.println("DOWNLOAD URL: " + params[0]);
                return downloadURL(params[0]);
            } catch (IOException e) {
                return "Unable to retrieve web page. Check your URL";
            }
        }


        // connects to send the GET request
        // this is executed second then onPostExectute
        private String downloadURL(String myurl) throws IOException {
            InputStream is = null;
            int len = 5000;

            try {
                URL url = new URL(myurl);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);

                conn.connect();
                int response = conn.getResponseCode();
                Log.d("DEBUG", "Response code is " + response);
                is = conn.getInputStream();
                String content = parse(is, len);
                //String test = new DrawMap(content);
                return content;
            } finally {
                if (is != null)
                    is.close();
            }
        }
        protected void onPostExecute(String result) {
            // the url is declared in onCreate, here the JSONObject is created and the data from the JSON string from the url is added to the appropriate variable
                try {
                    JSONArray json = new JSONArray(result);


////////////////////////////////////////////////////////////////////// this works but it cant draw the points because its not in the onCreate

                    // send this to the draw method to draw on the map the coordinates
                    //DrawMap drawM = new DrawMap();
                    double[] location_x = new double[5];
                    double[] location_y = new double[5];

                    mv = (MapView) findViewById(R.id.map1);
                    mvHelper = new MapViewHelper(mv);

                    System.out.println("result being read in from /locations/: " + json);
                    // pass locations to be drawn in another class in the onCreate
                    //DrawMap draw = new DrawMap(location_xy);

                    // this forloop gets the data into the JSONArray json for each client lesson and displays in list
                    for (int i = 0; i < 3; i++) {
                        try {

                            JSONObject object = json.getJSONObject(i); // reads the array into the JSONOBject object
                            String location_type = object.optString("type").toString();
                            String location_co = object.optString("coordinates").toString();
                            location_x[i] = object.optDouble("x");
                            location_y[i] = object.optDouble("y");
                            String location_id = object.optString("id").toString(); // id from the db is sent to keep unique



                            System.out.println("location_type: " + location_type);
                            System.out.println("location_co: " + location_co);
                            System.out.println("id: " + location_id);


                            String newtitle = "Reverse";
                            String newdetail = "Limekiln area";

                            mvHelper.addMarkerGraphic(location_x[i], location_y[i], newtitle, newdetail, "",
                                    ContextCompat.getDrawable(getApplicationContext(), R.drawable.pin20), false, 0);

                            System.out.println("Lat- " + location_x[i]);
                            System.out.println("Lon- " + location_y[i]);

                            //return sendPoints(location_x[],location_y[]);


                            //pt = mv.toMapPoint(location_x, location_y);
//                        String title = "Reverse";
//                        String detail = "Reverse around the corner";

//                        // once a point is tapped, makes a new point calling geometryEngine
//                        Point wgsPoint =  (Point) GeometryEngine.project(pt,mv.getSpatialReference(),SpatialReference.create(4326));
//                        double lon = wgsPoint.getX();
//                        double lat = wgsPoint.getY();

                            // draws to the map
//                        mvHelper.addMarkerGraphic(location_y, location_y,title,detail,R.drawable.car,
//                                ContextCompat.getDrawable(getApplicationContext(), R.drawable.pin20),false,0);

//////////////////////////////////////////////////////////////////////

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } // end for loop

                } catch (JSONException e) {
                    e.printStackTrace();
                }



        }
    }

//    public class DrawMap(String content){
//        void draw(String content) {
//            //plot these on the map
//            //System.out.println("location_x = " + location_xy);
//            System.out.println("result" + content);
//        }
//    }

    // parses the url and reads the JSON in as a string which is readable
    private String parse(InputStream is, int len) throws IOException {
        return readIt(is);
    }

    private String readIt(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }


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

    public void goBackScreen(View view) {
        try {
            Intent lastScreen = new Intent(this, MainActivity.class);
            startActivity(lastScreen);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}



//                String newPoint = wgsPoint.toString();
//
//                // splits this array in to the part before the "=" and after
//                String[] result = newPoint.split("=");
//                if (result.length != 2)
//                {
//                    // only interested in 1 and 2, result[3] is "com.esri.core.geometry.VertexDescription@7c5d0f85]" which we dont want
//                    for (int i = 0; i < 2; i++)
//                    {
//                        System.out.println("point split:" + result[i]); // prints out to log so we can see whats produced
//                    }
//                }
//
//                //System.out.println("result[1]:" + result[1]); // check that this is just the coordinates
//
//                // now we need to split "[-6.334276974899407, 53.3099229738916], m_description" to just the coodinates
//                String[] co = result[1].split(","); // result[2] is the coordinates plus ... m_description so we need to get rid of that
//                if (co.length != 2)
//                {
//                    // only interested in 1 and 2, result[3] is "com.esri.core.geometry.VertexDescription@7c5d0f85]" which we dont want
//                    for (int i = 0; i < 2; i++)
//                    {
//                        // should print out just the coordinates
//                        System.out.println("co split:" + co[i]); // prints out to log so we can see whats produced
//                    }
//                }
//
//                // concatenate the array points so that they can be added into the database as coordinates (x,y)
//                final String insertPoint = co[0].toString() + "," + co[1].toString();
//                System.out.println("insertPoint:" + insertPoint);

