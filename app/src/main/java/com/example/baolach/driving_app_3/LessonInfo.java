package com.example.baolach.driving_app_3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
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
        TextView nameTextView = (TextView) findViewById(R.id.theclientname);
        TextView dateTextView = (TextView) findViewById(R.id.theclientphone);
        TextView timeTextView = (TextView) findViewById(R.id.theclientaddress);
        TextView locationTextView = (TextView) findViewById(R.id.thelognumber);
        TextView commentsTextView = (TextView) findViewById(R.id.thedrivernumber);


        nameTextView.setText(lessonname);
        dateTextView.setText(lessondate);
        timeTextView.setText(lessontime);
        locationTextView.setText(lessonlocation);
        commentsTextView.setText(lessoncomments);

        // need this for the vertical scroll view
        nameTextView.setMovementMethod(new ScrollingMovementMethod());
        commentsTextView.setMovementMethod(new ScrollingMovementMethod());

        // for marquee scroll
//        nameTextView.setSelected(true);
//        dateTextView.setSelected(true);
//        timeTextView.setSelected(true);
          locationTextView.setSelected(true);
//        commentsTextView.setSelected(true);



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



                    deletedb.executeUpdate(sql);
                    conn.commit();


                    // once inserted into database all the edittexts resert to ""
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getBaseContext(), "Lesson with " + lessonname + " deleted from database! ", Toast.LENGTH_LONG).show();
                            Intent l = new Intent(LessonInfo.this, ListLessons.class); // lists all lessoninfo
                            startActivity(l);

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

//
//        // update
//        Button btnUpdate  = (Button) findViewById(R.id.update_client_btn);
//
//        // delete button
//        btnUpdate.setOnClickListener(new View.OnClickListener() {
//
//            public void onClick(View v) {
//                new Thread(new Runnable() {
//
//                    public void run() {
//                        update();
//                    }
//
//                }).start();
//            }
//
//            protected void update() {
//
//                //Connection c = null;
//                Statement updatedb = null;
//                try {
//                    // uses the JDBC driver to interact with the database
//                    Class.forName("org.postgresql.Driver");
//                    String url = "jdbc:postgresql://138.68.141.18:5432/fypdia2"; // uses driver to interact with database at this url
//                    Connection conn = DriverManager.getConnection(url, "root", "Cassie2007"); // connects to postgres
//                    conn.setAutoCommit(false);
//                    System.out.println("Opened database successfully");
//
//                    updatedb = conn.createStatement();
//                    System.out.println("About to update: " + lessonname );
//
//                    String sql = "Update getdata_getlesson set '" + lessonname + "' AND lesson_date= '" + lessondate + "' AND lesson_time = '" + lessontime + "';";
//                    UPDATE COMPANY set SALARY = 25000.00 where ID=1;
//
//                    updatedb.executeUpdate(sql);
//                    conn.commit();
//
//
//                    // once inserted into database all the edittexts resert to ""
//                    runOnUiThread(new Runnable() {
//                        public void run() {
//                            Toast.makeText(getBaseContext(), "Lesson updated in database! ", Toast.LENGTH_LONG).show();
//                            Intent u = new Intent(LessonInfo.this, ListLessons.class); // lists all lessoninfo
//                            startActivity(u);
//
//                        }
//                    });
//
//                    updatedb.close(); // close connection must be done
//                    conn.close();
//
//
//                }catch(Exception e){
//                    e.printStackTrace();
//                }
//                System.out.println("Delete successful");
//
//            }
//        });



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

    // when clicked brings you to the insertLesson page
    // passes the inputs in an intent and fills the editTexts with those values
    // then performs the insert again with those same params
    public void update(View view)
    {
        try{
            // creates new intent and sends over the client information when update is clicked
            // this adds these into the editTexts and then you can re-save the lesson
            Intent i = new Intent(this, UpdateLesson.class);
            i.putExtra("thelessonname", lessonname);
            i.putExtra("thelessondate", lessondate);
            i.putExtra("thelessontime", lessontime);
            i.putExtra("thelessonlocation", lessonlocation);
            i.putExtra("thelessoncomments", lessoncomments);

            System.out.println("update function before intent is sent");

            startActivity(i);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }





}
