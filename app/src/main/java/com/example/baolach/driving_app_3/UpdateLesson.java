package com.example.baolach.driving_app_3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by Baolach on 28/03/2017.
 */

public class UpdateLesson extends Activity {

    EditText lessonName;
    EditText lessonDate;
    EditText lessonTime;
    EditText lessonLocation;
    EditText lessonComments;
    private Button btnPost;

    String lessonname, lessondate, lessontime, lessonlocation, lessoncomments, lessonid;




    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_lesson_details);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        try {


            // bundle captures the parameters form the LessonInfo intent
            if (bundle != null) {
                // if bundle has data in it - read in the data into these variables eg. lessonname
                lessonname = bundle.getString("thelessonname");
                lessondate = bundle.getString("thelessondate");
                lessontime = bundle.getString("thelessontime");
                lessonlocation = bundle.getString("thelessonlocation");
                lessoncomments = bundle.getString("thelessoncomments");
                lessonid = bundle.getString("id");



                // then set the editTexts to these values that just came in
                lessonName = (EditText) findViewById(R.id.editText_lessonName);
                lessonDate = (EditText) findViewById(R.id.editText_lessonDate);
                lessonTime = (EditText) findViewById(R.id.editText_lessonTime);
                lessonLocation = (EditText) findViewById(R.id.editText_lessonLocation);
                lessonComments = (EditText) findViewById(R.id.editText_lessonComments);

                //            System.out.println("Insert lesson after intent is sent- lessonName: " + lessonName);
                //            System.out.println("Insert lesson after intent is sent- lessonname: " + lessonName);

                lessonName.setText(lessonname);
                lessonDate.setText(lessondate);
                lessonTime.setText(lessontime);
                lessonLocation.setText(lessonlocation);
                lessonComments.setText(lessoncomments);


                System.out.println("data has been entered again");
            }
        } catch(Exception e){
            Toast.makeText(getBaseContext(), "Error! Update not successful ", Toast.LENGTH_SHORT).show();
            System.out.println("update error");


        }

//        else{
//            // else if no data is sent ie. the update button isnt pressed - set t
//            lessonName = (EditText) findViewById(R.id.editText_lessonName);
//            lessonDate = (EditText) findViewById(R.id.editText_lessonDate);
//            lessonTime = (EditText) findViewById(R.id.editText_lessonTime);
//            lessonLocation = (EditText) findViewById(R.id.editText_lessonLocation);
//            lessonComments = (EditText) findViewById(R.id.editText_lessonComments);
//
//        }


        btnPost = (Button) findViewById(R.id.button_update);

        // submit button
        btnPost.setOnClickListener(new View.OnClickListener() {

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
                    final String l_name = lessonName.getText().toString();
                    String l_date = lessonDate.getText().toString();
                    String l_time = lessonTime.getText().toString();
                    String l_location = lessonLocation.getText().toString();
                    String l_comments = lessonComments.getText().toString();
                    String l_id = lessonid; // gets lessonid from what is passed over into bundle, not the edit texts like the others
                    System.out.println("UpdateLesson.java id: " + l_id);

                    PreparedStatement insertdb;
                    Class.forName("org.postgresql.Driver");
                    String url = "jdbc:postgresql://138.68.141.18:5432/fypdia2"; // uses driver to interact with database
                    Connection conn = DriverManager.getConnection(url, "root", "Cassie2007"); // connects to database

                    // prepares the sql statement

                    String update = "update getdata_getlesson set lesson_name = '" + l_name + "', lesson_date ='" +  l_date + "', lesson_time = '"+ l_time + "', lesson_location = '"+ l_location +"', lesson_comments = '" + l_comments +"' where id='"+ l_id +"';";


//                            "update getdata_getlesson set lesson_name = l_name, lesson_date = l_date, lesson_time = l_time, " +
//                            "lesson_location = l_location, lesson_comments = l_comments where l_id='" + l_id + "';";

                    System.out.println("UpdateLesson.java ln 130 - id: " + l_id);
                    System.out.println("UpdateLesson.java ln 133 - update : " + update);

                    insertdb = conn.prepareStatement(update);
                    System.out.println("UpdateLesson to be inserted: " + insertdb);

                    insertdb.setString(1, l_name);
                    insertdb.setString(2, l_date);
                    insertdb.setString(3, l_time);
                    insertdb.setString(4,  l_location);
                    insertdb.setString(5, l_comments);
                    //insertdb.setString(6, l_id);


                    insertdb.execute();
                    insertdb.close(); // close connection must be done

                    // once inserted into database goes back to listLessons to show it in the db
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getBaseContext(), "Lesson with " + l_name + " updated in database! ", Toast.LENGTH_LONG).show();
                            Intent l = new Intent(UpdateLesson.this, ListLessons.class); // lists all lessoninfo
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

    // An intent for the user to go back to the main screen.
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
