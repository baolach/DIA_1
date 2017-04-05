package com.example.baolach.driving_app_3;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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


public class LessonInfo extends Activity {

    private Button btnDelete;
    String lessonname, lessondate, lessontime, lessonlocation, lessoncomments, lessonid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_lesson_info);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        // bundle captures the parameters form the intent from listLessons.java from onItemClickListener
        if (bundle != null) {
            lessonname = bundle.getString("thelessonname");
            lessondate = bundle.getString("thelessondate");
            lessontime = bundle.getString("thelessontime");
            lessonlocation = bundle.getString("thelessonlocation");
            lessoncomments = bundle.getString("thelessoncomments");
            lessonid = bundle.getString("id");
        }

        // apply to textViews in LessonInfo but dont need id
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
        locationTextView.setSelected(true);


        btnDelete = (Button) findViewById(R.id.delete_client_btn);

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Are you sure you want to delete " + lessonname + "'s lesson?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                new Thread(new Runnable(){

                    public void run()
                    {
                        delete();
                    }

                }).start();

                finish();
            }

            protected void delete() {

                Statement deletedb = null;
                try {
                    Class.forName("org.postgresql.Driver");
                    String url = "jdbc:postgresql://138.68.141.18:5432/fypdia2"; // uses driver to interact with database
                    Connection conn = DriverManager.getConnection(url, "root", "Cassie2007"); // connects to database
                    conn.setAutoCommit(false);
                    deletedb = conn.createStatement();

                    String sql = "DELETE from getdata_getlesson where lesson_name = '" + lessonname +
                            "' AND lesson_date= '" + lessondate + "' AND lesson_time = '" + lessontime + "';";

                    deletedb.executeUpdate(sql);
                    conn.commit();


                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getBaseContext(), "Lesson with " + lessonname + " deleted from database! ", Toast.LENGTH_LONG).show();
                            Intent l = new Intent(LessonInfo.this, ListLessons.class); // lists all lessoninfo
                            startActivity(l);
                            finish();

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

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing
                dialog.dismiss();
            }
        });


        btnDelete.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                builder.show();

            }
        }); // end onClickListener for delete
    } // end onCreate

    public void goBackScreen(View view) {
        try {
            Intent lastScreen = new Intent(this, ListLessons.class);
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

    // when clicked brings you to the insertLesson page
    // passes the inputs in an intent and fills the editTexts with those values
    // then performs the insert again with those same params
    public void update(View view)
    {
        try{
            // creates new intent and sends over the client information when update is clicked
            // this adds these into the editTexts and then you can update the lesson
            Intent i = new Intent(this, UpdateLesson.class);
            i.putExtra("thelessonname", lessonname);
            i.putExtra("thelessondate", lessondate);
            i.putExtra("thelessontime", lessontime);
            i.putExtra("thelessonlocation", lessonlocation);
            i.putExtra("thelessoncomments", lessoncomments);
            i.putExtra("id", lessonid);

            startActivity(i);
            finish();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
