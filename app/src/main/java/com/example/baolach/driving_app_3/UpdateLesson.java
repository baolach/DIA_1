package com.example.baolach.driving_app_3;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Calendar;

/**
 * Created by Baolach on 28/03/2017.
 */

public class UpdateLesson extends Activity {

    EditText lessonName;
    EditText lessonTime;
    EditText lessonLocation;
    EditText lessonComments;
    private TextView lessonDate;
    private Button btnPost;

    String lessonname, lessondate, lessontime, lessonlocation, lessoncomments, lessonid;

    // calendar
    private DatePicker datePicker;
    private Calendar calendar;
    private int year, month, day, dayofweek;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_lesson_details);

        lessonDate = (TextView) findViewById(R.id.editText_lessonDate);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        dayofweek = calendar.get(Calendar.DAY_OF_WEEK);


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
                lessonTime = (EditText) findViewById(R.id.editText_lessonTime);
                lessonLocation = (EditText) findViewById(R.id.editText_lessonLocation);
                lessonComments = (EditText) findViewById(R.id.editText_lessonComments);

                lessonName.setText(lessonname);
                lessonDate.setText(lessondate);
                lessonTime.setText(lessontime);
                lessonLocation.setText(lessonlocation);
                lessonComments.setText(lessoncomments);

                System.out.println("Data transfered from lesson info to updateLesson successful");
            }
        } catch(Exception e){
            Toast.makeText(getBaseContext(), "Error! Update not successful ", Toast.LENGTH_SHORT).show();
            System.out.println("update error");


        }

        // update button to push to database
        btnPost = (Button) findViewById(R.id.button_update);
        btnPost.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // used for inserting into database
                new Thread(new Runnable() {
                    public void run() {
                        insert();
                    }
                }).start();
            }

            void insert() {

                try {
                    final String l_name = lessonName.getText().toString();
                    String l_date = lessonDate.getText().toString();
                    String l_time = lessonTime.getText().toString();
                    String l_location = lessonLocation.getText().toString();
                    String l_comments = lessonComments.getText().toString();
                    String l_id = lessonid; // gets lessonid from what is passed over into bundle, not the edit texts like the others

                    PreparedStatement insertdb;
                    Class.forName("org.postgresql.Driver");
                    String url = "jdbc:postgresql://138.68.141.18:5432/fypdia2"; // uses driver to interact with database
                    Connection conn = DriverManager.getConnection(url, "root", "Cassie2007"); // connects to database

                    // prepares the sql statement
                    String update = "update getdata_getlesson set lesson_name = ?, lesson_date =?, lesson_time = ?, " +
                            "lesson_location = ?, lesson_comments = ? where id='" + l_id + "';";

                    insertdb = conn.prepareStatement(update);
                    insertdb.setString(1, l_name);
                    insertdb.setString(2, l_date);
                    insertdb.setString(3, l_time);
                    insertdb.setString(4,  l_location);
                    insertdb.setString(5, l_comments);

                    insertdb.execute();
                    insertdb.close(); // close connection must be done
                    conn.close();

                    // once inserted into database goes back to listLessons to show it in the db
                    runOnUiThread(new Runnable() {
                        public void run() {

                            Toast.makeText(getBaseContext(), "Lesson with " + l_name + " updated in database! ", Toast.LENGTH_LONG).show();
                            Intent l = new Intent(UpdateLesson.this, ListLessons.class); // lists all lessoninfo
                            startActivity(l);
                            finish();
                        }
                    });



                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    @SuppressWarnings("deprecation")

    public void setDate(View view) {
        showDialog(999);
        Toast.makeText(getApplicationContext(), "Update this lesson", Toast.LENGTH_SHORT).show();
    }

    // calendar methods
    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert, myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {

                    String mon;
                    String dayofwk;

                    // day of week
                    if(arg3 == 3)
                        dayofwk = "Mon";
                    else if(arg3 == 4)
                        dayofwk = "Tue";
                    else if(arg3 == 5)
                        dayofwk = "Wed";
                    else if(arg3 == 6)
                        dayofwk = "Thur";
                    else if(arg3 == 7)
                        dayofwk = "Fri";
                    else if(arg3 == 8)
                        dayofwk = "Sat";
                    else if(arg3 == 9)
                        dayofwk = "Sun";
                    else
                        dayofwk = "mon";


                    // month
                    if(arg2 == 0)
                        mon = "Jan";
                    else if(arg2 == 1)
                        mon = "Feb";
                    else if(arg2 == 2)
                        mon = "Mar";
                    else if(arg2 == 3)
                        mon = "Apr";
                    else if(arg2 == 4)
                        mon = "May";
                    else if(arg2 == 5)
                        mon = "June";
                    else if(arg2 == 6)
                        mon = "July";
                    else if(arg2 == 7)
                        mon = "Aug";
                    else if(arg2 == 8)
                        mon = "Sep";
                    else if(arg2 == 9)
                        mon = "Oct";
                    else if(arg2 == 10)
                        mon = "Nov";
                    else if(arg2 == 11)
                        mon = "Dec";
                    else
                        mon = "month";

                    showDate(arg3, mon, dayofwk);
                }
            };

    private void showDate(int arg3, String mon, String dayofwk) {
        Toast.makeText(getApplicationContext(), "Lesson date has been updated", Toast.LENGTH_SHORT).show();
        lessonDate.setText(new StringBuilder().append(dayofwk).append(" - ").append(arg3).append(" - ").append(mon));
    }


    // An intent for the user to go back to the main screen.
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
}
