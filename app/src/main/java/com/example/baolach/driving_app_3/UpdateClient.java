package com.example.baolach.driving_app_3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Calendar;

/**
 * Created by Baolach on 28/03/2017.
 */

public class UpdateClient extends Activity {


    private DatePicker datePicker;
    private Calendar calendar;
    private EditText clientName,clientPhone,clientAddress,clientLogNo,clientDriverNo,clientDob,clientNoOfLessons, clientsComments, clientBalance;
    private Button btnPost;
    private int year, month, day;

    String clientname, clientphone, clientaddress, lognumber, drivernumber, dob, numberoflessons, balancedue, comments, clientid; // for the intent coming in



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_client_details);

//        calendar = Calendar.getInstance();
//
//        year = calendar.get(Calendar.YEAR);
//        month = calendar.get(Calendar.MONTH);
//        day = calendar.get(Calendar.DAY_OF_MONTH);
//        showDate(year, month+1, day);



        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        // bundle captures the parameters form the intent
        if (bundle != null) {
            // if bundle has data in it - read in the intent data into these newly made variables eg. lessonname

            clientname = bundle.getString("theclientname");
            clientphone = bundle.getString("theclientphone");
            clientaddress = bundle.getString("theclientaddress");
            lognumber = bundle.getString("thelognumber");
            drivernumber = bundle.getString("thedriverno");
            dob = bundle.getString("thedob");
            numberoflessons = bundle.getString("thenumberoflessons");
            balancedue = bundle.getString("thebalance");
            comments = bundle.getString("theclientscomments");
            clientid = bundle.getString("id");




            // then set the editTexts to these values that just came in
            clientName = (EditText) findViewById(R.id.editText_clientName);
            clientPhone = (EditText) findViewById(R.id.editText_clientPhone);
            clientAddress = (EditText) findViewById(R.id.editText_clientAddress);
            clientLogNo = (EditText) findViewById(R.id.editText_clientLogNo);
            clientDriverNo = (EditText) findViewById(R.id.editText_clientDriverNo);
            clientDob = (EditText) findViewById(R.id.editText_clientDob);
            clientNoOfLessons = (EditText) findViewById(R.id.editText_clientNoOfLessons);
            clientBalance = (EditText) findViewById(R.id.editText_clientBalance);
            clientsComments = (EditText) findViewById(R.id.editText_clientComments);

//            System.out.println("Insert lesson after intent is sent- lessonName: " + lessonName);
//            System.out.println("Insert lesson after intent is sent- lessonname: " + lessonName);

            // setting the editTexts the data that was passed in order to help the user know what they want to update
            clientName.setText(clientname);
            clientPhone.setText(clientphone);
            clientAddress.setText(clientaddress);
            clientLogNo.setText(lognumber);
            clientDriverNo.setText(drivernumber);
            clientDob.setText(dob);
            clientNoOfLessons.setText(numberoflessons);
            clientBalance.setText(balancedue);
            clientsComments.setText(comments);




            System.out.println("data has been entered again");


        }



        btnPost = (Button) findViewById(R.id.button_update); // update button

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
                    final String c_name = clientName.getText().toString();
                    String c_phone = clientPhone.getText().toString();
                    String c_address = clientAddress.getText().toString();
                    String c_log = clientLogNo.getText().toString();
                    String c_d_no = clientDriverNo.getText().toString();
                    String c_dob = clientDob.getText().toString();
                    String c_no_l = clientNoOfLessons.getText().toString();
                    String c_comm = clientsComments.getText().toString();
                    String c_bal = clientBalance.getText().toString();
                    String c_id = clientid;

                    PreparedStatement insertdb = null;
                    Class.forName("org.postgresql.Driver");
                    String url = "jdbc:postgresql://138.68.141.18:5432/fypdia2"; // uses driver to interact with database
                    Connection conn = DriverManager.getConnection(url, "root", "Cassie2007"); // connects to database

                    // prepares the sql statement
                    //String insert = "insert into getdata_getclient values (?,?,?,?,?,?,?,?,?)";
                    String update = "update getdata_getclient set client_name = ?, client_phone =?, client_address = ?, " +
                            "log_no = ?, driver_no = ?, dob = ?, no_of_lessons = ?, balance_due = ?, comments = ?" +
                            "where id='" + c_id + "';";


                    insertdb = conn.prepareStatement(update);
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

                            Toast.makeText(getBaseContext(), c_name + "'s details are updated!", Toast.LENGTH_LONG).show();
                            Intent c = new Intent(UpdateClient.this, ListClients.class); // lists all lessoninfo
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
}
