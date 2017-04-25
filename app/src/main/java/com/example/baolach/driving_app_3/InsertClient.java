package com.example.baolach.driving_app_3;

// http://hmkcode.com/android-send-json-data-to-server/
// https://www.tutorialspoint.com/android/android_datepicker_control.htm


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
import java.util.Calendar;

public class InsertClient extends Activity {


    private EditText clientName,clientPhone,clientAddress,clientLogNo,clientDriverNo,clientNoOfLessons, clientsComments, clientBalance;
    private Button btnPost;
    private TextView clientDob;

    // calendar
    private DatePicker datePicker;
    private Calendar calendar;
    private int year, month, day;
    private String mon;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.insert_client_details);

        clientDob = (TextView) findViewById(R.id.editText_clientDob);
        calendar = Calendar.getInstance();

        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH); // defaults to 4 = April instead of defaulting to null
        mon = "Apr";
        day = calendar.get(Calendar.DAY_OF_MONTH);

        //showDate(year, mon, day); // default is current date


        clientName = (EditText) findViewById(R.id.editText_clientName);
        clientPhone = (EditText) findViewById(R.id.editText_clientPhone);
        clientAddress = (EditText) findViewById(R.id.editText_clientAddress);
        clientLogNo = (EditText) findViewById(R.id.editText_clientLogNo);
        clientDriverNo = (EditText) findViewById(R.id.editText_clientDriverNo);

        clientNoOfLessons = (EditText) findViewById(R.id.editText_clientNoOfLessons);
        clientsComments = (EditText) findViewById(R.id.editText_clientComments);
        clientBalance = (EditText) findViewById(R.id.editText_clientBalance);

        // insert client button
        btnPost = (Button) findViewById(R.id.button_post);
        btnPost.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if ( ( !clientName.getText().toString().equals("")) && ( !clientPhone.getText().toString().equals(""))
                        && ( !clientLogNo.getText().toString().equals("")) && ( !clientDriverNo.getText().toString().equals(""))) {

                    new Thread(new Runnable() {

                        public void run() {
                            insert();
                        }

                    }).start();
                }else{
                    Toast.makeText(getBaseContext(), "Client name, phone, log no or driver no cannot be null ", Toast.LENGTH_LONG).show();
                }

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

                } catch (Exception e) {
//                    Helper.displayExceptionMessage(this);
                    e.printStackTrace();

                }

            }


        });
    } // end on create

// helper class to send a toast
//    public static void displayExceptionMessage(Context context) {
//        Toast.makeText(context, "Driver Number or Log Number already in the database!" , Toast.LENGTH_LONG).show();
//    }

    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
        Toast.makeText(getApplicationContext(), "Select the DOB of the client", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this,android.R.style.Theme_DeviceDefault_Light_Dialog_Alert, myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
                    String mon;
                    System.out.println("arg1: " + arg1);
                    System.out.println("arg2: " + arg2);
                    System.out.println("arg3: " + arg3);
                    System.out.println(clientDob.getText().toString());

                    // this is used to display a better DOB format to the instructor in words rather than numbers
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
        clientDob.setText(new StringBuilder().append(day).append("-").append(mon).append("-").append(year));

    }


    // An intent for the user to go back to the main screen
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