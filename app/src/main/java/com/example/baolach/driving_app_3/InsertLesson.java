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


public class InsertLesson extends Activity {

    EditText lessonName;
    EditText lessonTime;
    EditText lessonLocation;
    EditText lessonComments;

    private TextView lessonDate;
    String clientname, clientid;// for the intent coming in
    private Button btnPost;

    // calendar
    private DatePicker datePicker;
    private Calendar calendar;
    private int year, month, day, dayofweek;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.insert_lesson_details);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        // bundle captures the parameters from the intent fron SelectClient
        if (bundle != null) {
            clientname = bundle.getString("theclientname");
            clientid = bundle.getString("id");
            System.out.println("id sent:" + clientid);

            // then set the editTexts to these values that just came in
            lessonName = (EditText) findViewById(R.id.editText_lessonName);
            // setting the editTexts the data that was passed in order to help the user know what they want to update
            lessonName.setText(clientname);

        } // end of bundle


        lessonName = (EditText) findViewById(R.id.editText_lessonName);
        lessonDate = (TextView) findViewById(R.id.editText_lessonDate);
        lessonTime = (EditText) findViewById(R.id.editText_lessonTime);
        lessonLocation = (EditText) findViewById(R.id.editText_lessonLocation);
        lessonComments = (EditText) findViewById(R.id.editText_lessonComments);


        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH); // defaults to 4 = April instead of defaulting to null
        day = calendar.get(Calendar.DAY_OF_MONTH);
        dayofweek = calendar.get(Calendar.DAY_OF_WEEK);


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

            void insert() {
                try {
                    String l_name = lessonName.getText().toString();
                    System.out.println("l_name: " +l_name);
                    String l_date = lessonDate.getText().toString();
                    System.out.println("l_name: " +l_date);

                    String l_time = lessonTime.getText().toString();
                    System.out.println("l_time: " +l_time);

                    String l_location = lessonLocation.getText().toString();
                    String l_comments = lessonComments.getText().toString();

                    PreparedStatement insertdb = null;
                    Class.forName("org.postgresql.Driver");
                    String url = "jdbc:postgresql://138.68.141.18:5432/fypdia2"; // uses driver to interact with database
                    Connection conn = DriverManager.getConnection(url, "root", "Cassie2007"); // connects to database

                    // prepares the sql statement
                    String insert = "insert into getdata_getlesson values (?,?,?,?,?)";
                    insertdb = conn.prepareStatement(insert);
                    insertdb.setString(1, l_name);
                    insertdb.setString(2, l_date);
                    insertdb.setString(3, l_time);
                    insertdb.setString(4,  l_location);
                    insertdb.setString(5, l_comments);

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
    } // end onCreate



//    public void spinner(View view)
//    {
//
//        // dropdown list to select the client for the lesson
//        // Spinner element
//        Spinner spinner = (Spinner) findViewById(R.id.spinner);
//
//
//
//        // Spinner click listener
//        //spinner.setOnItemSelectedListener(this);
//        // Spinner Drop down elements
//        List<String> categories = new ArrayList<String>();
//        categories.add("Automobile");
//        categories.add("Business Services");
//        categories.add("Computers");
//        categories.add("Education");
//        categories.add("Personal");
//        categories.add("Travel");
//
//        // Creating adapter for spinner
//        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
//
//        // Drop down layout style - list view with radio button
//        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//        // attaching data adapter to spinner
//        spinner.setAdapter(dataAdapter);
//
//    }



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
                dayofwk = "Wedd";

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
                mon = "jan";

            showDate(arg3, mon, dayofwk);
        }
    };

    private void showDate(int arg3, String mon, String dayofwk) {
        // sets textView of CLient dob to the calendar input
        lessonDate.setText(new StringBuilder().append(dayofwk).append(" - ").append(arg3).append(" - ").append(mon));

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
