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

    String lessonname, lessondate, lessontime, lessonlocation, lessoncomments; // for the intent coming in




    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_lesson_details);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        // bundle captures the parameters form the intent
        if (bundle != null) {
            // if bundle has data in it - read in the data into these variables eg. lessonname
            lessonname = bundle.getString("thelessonname");
            lessondate = bundle.getString("thelessondate");
            lessontime = bundle.getString("thelessontime");
            lessonlocation = bundle.getString("thelessonlocation");
            lessoncomments = bundle.getString("thelessoncomments");



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
                    String l_name = lessonName.getText().toString();
                    String l_phone = lessonDate.getText().toString();
                    String c_address = lessonTime.getText().toString();
                    String c_log = lessonLocation.getText().toString();
                    String c_d_no = lessonComments.getText().toString();

                    PreparedStatement insertdb = null;
                    Class.forName("org.postgresql.Driver");
                    String url = "jdbc:postgresql://138.68.141.18:5432/fypdia2"; // uses driver to interact with database
                    Connection conn = DriverManager.getConnection(url, "root", "Cassie2007"); // connects to database

                    // prepares the sql statement

                    String insert = "insert into getdata_getlesson values (?,?,?,?,?)";
                    insertdb = conn.prepareStatement(insert);
                    insertdb.setString(1, l_name);
                    insertdb.setString(2, l_phone);
                    insertdb.setString(3, c_address);
                    insertdb.setString(4,  c_log);
                    insertdb.setString(5, c_d_no);

                    insertdb.execute();
                    insertdb.close(); // close connection must be done

                    // once inserted into database goes back to listLessons to show it in the db
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getBaseContext(), "Lesson updated in database! ", Toast.LENGTH_LONG).show();
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
