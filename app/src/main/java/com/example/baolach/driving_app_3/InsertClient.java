package com.example.baolach.driving_app_3;

// http://hmkcode.com/android-send-json-data-to-server/
// https://www.tutorialspoint.com/android/android_datepicker_control.htm


import android.app.Activity;
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

public class InsertClient extends Activity {

    private DatePicker datePicker;
    private Calendar calendar;
    private EditText clientName,clientPhone,clientAddress,clientLogNo,clientDriverNo,clientDob,clientNoOfLessons, clientsComments, clientBalance;
    private Button btnPost;


    // calendar
    private TextView tvDisplayDate;
    private DatePicker dpResult;
    private Button btnChangeDate;

    private int year;
    private int month;
    private int day;

    static final int DATE_DIALOG_ID = 999;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.insert_client_details);

//        calendar = Calendar.getInstance();
//
//        year = calendar.get(Calendar.YEAR);
//        month = calendar.get(Calendar.MONTH);
//        day = calendar.get(Calendar.DAY_OF_MONTH);
//        showDate(year, month+1, day);



        clientName = (EditText) findViewById(R.id.editText_clientName);
        clientPhone = (EditText) findViewById(R.id.editText_clientPhone);
        clientAddress = (EditText) findViewById(R.id.editText_clientAddress);
        clientLogNo = (EditText) findViewById(R.id.editText_clientLogNo);
        clientDriverNo = (EditText) findViewById(R.id.editText_clientDriverNo);
        clientDob = (EditText) findViewById(R.id.editText_clientDob);
        clientNoOfLessons = (EditText) findViewById(R.id.editText_clientNoOfLessons);
        clientsComments = (EditText) findViewById(R.id.editText_clientComments);
        clientBalance = (EditText) findViewById(R.id.editText_clientBalance);

        btnPost = (Button) findViewById(R.id.button_submit);
//        Button btndob = (Button) findViewById(R.id.editText_clientDob);
//
//        btndob.setOnClickListener(new View.OnClickListener(){
//            public void onClick(View v) {
//                setCurrentDateOnView();
//                addListenerOnButton();
//            }
//        });


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
                    String c_name = clientName.getText().toString();
                    String c_phone = clientPhone.getText().toString();
                    String c_address = clientAddress.getText().toString();
                    String c_log = clientLogNo.getText().toString();
                    String c_d_no = clientDriverNo.getText().toString();
                    String c_dob = clientDob.getText().toString();
                    String c_no_l = clientNoOfLessons.getText().toString();
                    String c_comm = clientsComments.getText().toString();
                    String c_bal = clientBalance.getText().toString();

                    PreparedStatement insertdb = null;
                    Class.forName("org.postgresql.Driver");
                    String url = "jdbc:postgresql://138.68.141.18:5432/fypdia2"; // uses driver to interact with database
                    Connection conn = DriverManager.getConnection(url, "root", "Cassie2007"); // connects to database

                    // prepares the sql statement
                    String insert = "insert into getdata_getclient values (?,?,?,?,?,?,?,?,?)";
                    insertdb = conn.prepareStatement(insert);
                    insertdb.setString(1, c_name);
                    insertdb.setString(2, c_phone);
                    insertdb.setString(3, c_address);
                    insertdb.setString(4,  c_log);
                    insertdb.setString(5, c_d_no);
                    insertdb.setString(6, c_dob);
                    insertdb.setString(7, c_no_l);
                    insertdb.setString(8, c_bal);
                    insertdb.setString(9, c_comm);

                    insertdb.execute();
                    insertdb.close(); // close connection must be done

                    // once inserted into database goes back to listClients to show it in the db
                    runOnUiThread(new Runnable() {
                        public void run() {

                            Toast.makeText(getBaseContext(), "Client added to database! ", Toast.LENGTH_LONG).show();
                            Intent c = new Intent(InsertClient.this, ListClients.class); // lists all lessoninfo
                            startActivity(c);

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
    } // end on create


    // calendar stuff
    // display current date
//    public void setCurrentDateOnView() {

//        //tvDisplayDate = (TextView) findViewById(R.id.tvDate);
//        dpResult = (DatePicker) findViewById(R.id.dpResult);
//
//        final Calendar c = Calendar.getInstance();
//        year = c.get(Calendar.YEAR);
//        month = c.get(Calendar.MONTH);
//        day = c.get(Calendar.DAY_OF_MONTH);
//
//        // set current date into textview
//        clientDob.setText(new StringBuilder()
//                // Month is 0 based, just add 1
//                .append(month + 1).append("-").append(day).append("-")
//                .append(year).append(" "));
//
//        // set current date into datepicker
//        dpResult.init(year, month, day, null);

 //   }

 //   public void addListenerOnButton() {

//        btnChangeDate = (Button) findViewById(R.id.btnChangeDate);
//
//        btnChangeDate.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//
//                showDialog(DATE_DIALOG_ID);
//
//            }
//
//        });

//    }

//    @Override
//    protected Dialog onCreateDialog(int id) {
//        switch (id) {
//            case DATE_DIALOG_ID:
//                // set date picker as current date
//                return new DatePickerDialog(this, datePickerListener,
//                        year, month,day);
//        }
//        return null;
//    }
//
//    private DatePickerDialog.OnDateSetListener datePickerListener
//            = new DatePickerDialog.OnDateSetListener() {
//
//        // when dialog box is closed, below method will be called.
//        public void onDateSet(DatePicker view, int selectedYear,
//                              int selectedMonth, int selectedDay) {
//            year = selectedYear;
//            month = selectedMonth;
//            day = selectedDay;
//
//            // set selected date into textview
//            tvDisplayDate.setText(new StringBuilder().append(month + 1)
//                    .append("-").append(day).append("-").append(year)
//                    .append(" "));
//
//            // set selected date into datepicker also
//            dpResult.init(year, month, day, null);
//
//        }
//    };










    // An intent for the user to go back to the main screen
    public void goBackScreen(View view) {
        try {
            Intent lastScreen = new Intent(this, ListClients.class);
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