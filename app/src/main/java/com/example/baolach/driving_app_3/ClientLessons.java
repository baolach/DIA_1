package com.example.baolach.driving_app_3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class ClientLessons extends Activity{

    // making an array list to put the lesson variables being read in from the database
    static ArrayList<String> lessonName = new ArrayList<String>();
    static ArrayList<String> lessonDate = new ArrayList<String>();
    static ArrayList<String> lessonTime = new ArrayList<String>();
    static ArrayList<String> lessonLocation = new ArrayList<String>();
    static ArrayList<String> lessonComments = new ArrayList<String>();
    static ArrayList<String> lessonId = new ArrayList<String>();

    ListView listView; // blue listview
    ArrayList<Lesson> list;
    LessonAdapter adapter = null;

    String clientname, clientid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_client_lessons);

        lessonName.clear();
        lessonDate.clear();
        lessonTime.clear();
        lessonLocation.clear();
        lessonComments.clear();
        lessonId.clear();

        // creates variables to be shown and interacted with in the activity
        // sets up listView and Adapter to accept the data from the url
        listView = (ListView) findViewById(R.id.listView_lessons); // the listview ID in list_clients.xml
        list = new ArrayList<>();
        adapter = new LessonAdapter(this, R.layout.lessoninfo, list); // this sets adapter to the ClientAdapter which uses client.xml
        listView.setAdapter(adapter); // sets the listview in ListLesson activity output the adapter within the listView


        // gets intent from clientInfo when lessons button is click - select all lessons with name and id of
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        // bundle captures the parameters from ClientInfo the intent
        if (bundle != null) {
            clientname = bundle.getString("theclientname");
            clientid = bundle.getString("id");

        }
        // now I want to do a select statement to list lessons with this name and id only

    Thread thread = new Thread() {

        public void run() {
            try {
                PreparedStatement st = null;
                Class.forName("org.postgresql.Driver");
                String url = "jdbc:postgresql://138.68.141.18:5432/fypdia2"; // uses driver to interact with database
                Connection conn = DriverManager.getConnection(url, "root", "Cassie2007"); // connects to database
                // prepares the sql statement

                String select = "select * from getdata_getlesson where lesson_name=? ;" ; // AND id = ?;";

                st = conn.prepareStatement(select);
                st.setString(1, clientname);
                //st.setString(2, clientid);
                ResultSet rs = st.executeQuery();

                while (rs.next()) {
                    final int g = 0;
                    final String lessonname = rs.getString("lesson_name");
                    final String lessondate = rs.getString("lesson_date");
                    final String lessontime = rs.getString("lesson_time");
                    final String lessonlocation = rs.getString("lesson_location");
                    final String lessoncomments = rs.getString("lesson_comments");
                    final String lessonid = rs.getString("id");


                    runOnUiThread(new Runnable() {
                        public void run() {

                            lessonName.add(lessonname);
                            lessonDate.add(lessondate);
                            lessonTime.add(lessontime);
                            lessonLocation.add(lessonlocation);
                            lessonComments.add(lessoncomments);
                            lessonId.add(lessonid);

                            list.add(new Lesson(g, lessonname, lessondate, lessontime, lessonlocation, lessoncomments, lessonid));
                            adapter.notifyDataSetChanged();

                        }
                    });
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


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // itemId keeps track of where in the list it is
                int itemId = (int) id;

                String lessonname = lessonName.get(itemId);
                String lessondate = lessonDate.get(itemId);
                String lessontime = lessonTime.get(itemId);
                String lessonlocation = lessonLocation.get(itemId);
                String lessoncomments = lessonComments.get(itemId);
                String lessonid = lessonId.get(itemId);

                // creates new intent and sends over the lesson information when item is clicked
                // accepted by Bundle in LessonInfo
                Intent i = new Intent(ClientLessons.this, LessonInfo.class);
                i.putExtra("thelessonname", lessonname);
                i.putExtra("thelessondate", lessondate);
                i.putExtra("thelessontime", lessontime);
                i.putExtra("thelessonlocation", lessonlocation);
                i.putExtra("thelessoncomments", lessoncomments);
                i.putExtra("id", lessonid);

                startActivity(i);

            }
        });


    } // end onCreate

    public void newLesson(View view)
    {
        try {
            Intent client_name_intent = new Intent(this, SelectClient.class);
            Toast.makeText(getBaseContext(), "Select who the lesson is with", Toast.LENGTH_LONG).show();
            startActivity(client_name_intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void goBackScreen(View view) {

        try {
            Intent lastScreen = new Intent(this, AdminActivity.class);
            startActivity(lastScreen);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void home(View view)
    {
        try {
            Intent home_intent = new Intent(this, MainActivity.class);
            startActivity(home_intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


} // end class
