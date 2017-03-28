package com.example.baolach.driving_app_3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/* DELETE BUTTON DOESNT WORK */

public class LessonInfo extends Activity {

    // these 2 added
//    DBManager db = new DBManager(this);
//    String lesson_name;

    String lessonname, lessondate, lessontime, lessonlocation, lessoncomments;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_lesson_info);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        // bundle captures the parameters form the intent
        if (bundle != null) {
            lessonname = bundle.getString("thelessonname");
            lessondate = bundle.getString("thelessondate");
            lessontime = bundle.getString("thelessontime");
            lessonlocation = bundle.getString("thelessonlocation");
            lessoncomments = bundle.getString("thelessoncomments");


        }

        // apply to textViews
        TextView nameTextView = (TextView) findViewById(R.id.thelessonname);
        TextView dateTextView = (TextView) findViewById(R.id.thelessondate);
        TextView timeTextView = (TextView) findViewById(R.id.thelessontime);
        TextView locationTextView = (TextView) findViewById(R.id.thelessonlocation);
        TextView commentsTextView = (TextView) findViewById(R.id.thelessoncomments);


        nameTextView.setText(lessonname);
        dateTextView.setText(lessondate);
        timeTextView.setText(lessontime);
        locationTextView.setText(lessonlocation);
        commentsTextView.setText(lessoncomments);



        Button btnDelete  = (Button) findViewById(R.id.delete_client_btn);

        // delete button
        btnDelete.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                new Thread(new Runnable() {

                    public void run() {
                        delete();
                    }

                }).start();
            }

            protected void delete() {

                //Connection c = null;
                Statement deletedb = null;
                try {
                    Class.forName("org.postgresql.Driver");
                    String url = "jdbc:postgresql://138.68.141.18:5432/fypdia2"; // uses driver to interact with database
                    Connection conn = DriverManager.getConnection(url, "root", "Cassie2007"); // connects to database
                    conn.setAutoCommit(false);
                    System.out.println("Opened database successfully");

                    deletedb = conn.createStatement();
                    System.out.println("About to delete: " + lessonname );
                    // where "table_name_in postgres_db = the_variable_the_intent_sent_over"
                    String sql = "DELETE from getdata_getlesson where lesson_name = '" + lessonname + "' AND lesson_date= '" + lessondate + "' AND lesson_time = '" + lessontime + "';";

                    //where lesson_name = 'Stephen' AND lesson_date = '2017-01-19' AND lesson_time='14:20';
                    //Toast.makeText(getBaseContext(), "Client deleted from database! ", Toast.LENGTH_LONG).show();

                    deletedb.executeUpdate(sql);
                    conn.commit();


                    // once inserted into database all the edittexts resert to ""
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getBaseContext(), "Lesson deleted from database! ", Toast.LENGTH_LONG).show();
                            Intent maps = new Intent(LessonInfo.this, ListLessons.class); // lists all lessoninfo
                            startActivity(maps);

                        }
                    });

                    deletedb.close(); // close connection must be done
                    conn.close();


                }catch(Exception e){
                    e.printStackTrace();
                }
                System.out.println("Delete successful");

            }
        });

    }









        // old sqlite way
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_lesson_info);
//
//        Intent lessonData = getIntent();
//
//        //Bundle clientData = getIntent().getExtras();
//
//        //put data from list clients activity into new string
//        lesson_name = lessonData.getStringExtra("thelessonname");
//        //clientsName = lessonData.getStringExtra("thelessonname");
//        String TheLessonDate= lessonData.getStringExtra("thelessondate");
//        String TheLessonTime = lessonData.getStringExtra("thelessontime");
//        String TheLessonLocation = lessonData.getStringExtra("thelessonlocation");
//        String TheLessonComments = lessonData.getStringExtra("thelessoncomments");
//
//        //create variable which references output field
//        final TextView nameTextView = (TextView) findViewById(R.id.theclientname);
//        final TextView dateTextView = (TextView) findViewById(R.id.theclientphone);
//        final TextView timeTextView = (TextView) findViewById(R.id.theclientaddress);
//        final TextView locationTextView = (TextView) findViewById(R.id.thelognumber);
//        final TextView commentsTextView = (TextView) findViewById(R.id.thedriverno);
//
//        //use setText to change text
//        nameTextView.setText(lesson_name);
//        dateTextView.setText(TheLessonDate);
//        timeTextView.setText(TheLessonTime);
//        locationTextView.setText(TheLessonLocation);
//        commentsTextView.setText(TheLessonComments);
//
//
//        Button deleteButton = (Button)findViewById(R.id.delete_client_btn);
//        deleteButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v)
//            {
//                DBManager dbManager = new DBManager(LessonInfo.this);
//                try {
//                    dbManager.open();
//                    dbManager.deleteClient(lesson_name);
//                    //Toast.makeText(getApplicationContext(), "Client deleted", Toast.LENGTH_SHORT).show();
//
//                } catch (SQLException e) {
//                    Toast.makeText(getApplicationContext(), "Client could not be deleted", Toast.LENGTH_SHORT).show();
//                } finally {
//                    dbManager.close();
//
//                    Intent intent = new Intent(LessonInfo.this, ListLessons.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // clears previous screens
//                    startActivity(intent); // this loads new intent (ListClients
//
//                }
//
//            }
//        });



    public void goBackScreen(View view) {
        try {
            Intent lastScreen = new Intent(this, ListLessons.class);
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





}
