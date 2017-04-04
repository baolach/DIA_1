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
 */
public class InsertLesson extends Activity {

    EditText lessonName;
    //EditText lessonDate;
    EditText lessonTime;
    EditText lessonLocation;
    EditText lessonComments;
    private TextView lessonDate;
    private Button btnPost;

    String lessonname, lessondate, lessontime, lessonlocation, lessoncomments; // for the intent coming in

    // calendar
    private DatePicker datePicker;
    private Calendar calendar;
    private int year, month, day;
    private String mon;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.insert_lesson_details);

        lessonDate = (TextView) findViewById(R.id.editText_lessonDate);
        calendar = Calendar.getInstance();

        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH); // defaults to 4 = April instead of defaulting to null
        mon = "Apr";
        day = calendar.get(Calendar.DAY_OF_MONTH);

        showDate(year, mon, day); // default is current date


        // then set the editTexts to these values that just came in
        lessonName = (EditText) findViewById(R.id.editText_lessonName);
//      lessonDate = (EditText) findViewById(R.id.editText_lessonDate);
        lessonTime = (EditText) findViewById(R.id.editText_lessonTime);
        lessonLocation = (EditText) findViewById(R.id.editText_lessonLocation);
        lessonComments = (EditText) findViewById(R.id.editText_lessonComments);


        // posts to database
        btnPost = (Button) findViewById(R.id.button_submit);
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
                            Toast.makeText(getBaseContext(), "Lesson added to database! ", Toast.LENGTH_LONG).show();
                            Intent l = new Intent(InsertLesson.this, ListLessons.class); // lists all lessoninfo
                            startActivity(l);
                            finish();

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

    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
        Toast.makeText(getApplicationContext(), "Select the date of the lesson", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert,  myDateListener, year, month, day); // Theme_DeviceDefault_Dialog_Alert
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub

                    String mon;
                    System.out.println("arg1: " + arg1);
                    System.out.println("arg2: " + arg2);
                    System.out.println("arg3: " + arg3);
                    System.out.println(lessonDate.getText().toString());

                    // this is used to display a better DOB format to the instructor
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

                    showDate(arg1, mon, arg3);
                }
            };

    private void showDate(int year, String mon, int day) {
        // sets textView of CLient dob to the calendar input
        lessonDate.setText(new StringBuilder().append(day).append("-").append(mon).append("-").append(year));

    }






    // An intent for the user to go back to the main screen.
    public void goBackScreen(View view) {
        try {
            Intent lastScreen = new Intent(this, AdminActivity.class);
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


