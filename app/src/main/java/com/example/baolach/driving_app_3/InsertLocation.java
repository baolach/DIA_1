package com.example.baolach.driving_app_3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class InsertLocation extends Activity {

    EditText locationDetail;
    private Button btnPost;
    double locationx, locationy;// for the intent coming in


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.insert_location_details);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        // bundle captures the parameters from the intent from mapsactivity
        if (bundle != null) {
            // only need the coordinates so I can set the locationa and add type and details
            locationx = bundle.getDouble("thelocationx");
            locationy = bundle.getDouble("thelocationy");

        } // end of bundle

        // selects the type of location to be added
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioGroup.clearCheck();
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                final RadioButton selected = (RadioButton) group.findViewById(checkedId);
                // if a button is selected and its in the radioGroup
                if (null != selected && checkedId > -1) {
                    System.out.println("selected: "+ selected.getText().toString());

                }

                locationDetail = (EditText) findViewById(R.id.editText_locationDetail);

                // posts to database
                btnPost = (Button) findViewById(R.id.button_post);
                btnPost.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View v) {
                            new Thread(new Runnable() {
                                public void run() {
                                    insert();
                                }
                            }).start();

                    }

                    void insert() {
                        try {

                            String l_detail = locationDetail.getText().toString();

                            // inserts location into db
                            PreparedStatement insertdb;
                            Class.forName("org.postgresql.Driver");
                            String url = "jdbc:postgresql://138.68.141.18:5432/fypdia2"; // uses driver to interact with database
                            Connection conn = DriverManager.getConnection(url, "root", "Cassie2007"); // connects to database

                            // prepares the sql statement
                            String insert = "insert into getdata_getlocation values (?, ?, ?, ?) ;";
                            insertdb = conn.prepareStatement(insert);

                            insertdb.setString(1, selected.getText().toString()); // change to the selected radio button
                            insertdb.setDouble(2, locationx );
                            insertdb.setDouble(3, locationy );
                            insertdb.setString(4, l_detail );

                            insertdb.execute();
                            insertdb.close(); // close connection must be done

                            // once inserted into database goes back to maps to show it in the db
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    Toast.makeText(getBaseContext(), "Location added to database!", Toast.LENGTH_LONG).show();
                                    Intent l = new Intent(InsertLocation.this, MapsActivity.class); //refreshes the map

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
            } // end radio group
        }); // end radio listner

    } // end onCreate

    // An intent for the user to go back to the main screen.
    public void goBackScreen(View view) {
        try {
            Intent lastScreen = new Intent(this, MapsActivity.class);
            startActivity(lastScreen);
            finish();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void home(View view)
    {
        try {
            Intent home_intent = new Intent(this, MainActivity.class);
            startActivity(home_intent);
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
