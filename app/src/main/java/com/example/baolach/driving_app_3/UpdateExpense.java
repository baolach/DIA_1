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
import java.util.Calendar;

/**
 * Created by Baolach on 20/04/2017.
 */

public class UpdateExpense extends Activity {

    EditText expenseName;
    EditText expenseAmount;
    TextView expenseDate;

    private TextView lessonDate;
    private Button btnPost;

    // calendar
    private DatePicker datePicker;
    private Calendar calendar;
    private int year, month, day, dayofweek;

    String expensename,expenseamount, expensedate, expenseid;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_expense_details);

        expenseName = (EditText) findViewById(R.id.editText_expenseName);
        expenseAmount = (EditText) findViewById(R.id.editText_expenseAmount);
        expenseDate = (TextView) findViewById(R.id.editText_expenseDate);


        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH); // defaults to 4 = April instead of defaulting to null
        day = calendar.get(Calendar.DAY_OF_MONTH);
        dayofweek = calendar.get(Calendar.DAY_OF_WEEK);


        // posts to database
        btnPost = (Button) findViewById(R.id.button_submit);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        // bundle captures the parameters form the intent
        if (bundle != null) {
            // if bundle has data in it - read in the intent data into these newly made variables eg. lessonname

            expensename = bundle.getString("theexpensename");
            expenseamount = bundle.getString("theexpenseamount");
            expensedate = bundle.getString("theexpensedate");
            expenseid = bundle.getString("theexpenseid");

            // then set the editTexts to these values that just came in
            expenseName = (EditText) findViewById(R.id.editText_expenseName);
            expenseAmount = (EditText) findViewById(R.id.editText_expenseAmount);
            expenseDate = (TextView) findViewById(R.id.editText_expenseDate);

            // setting the editTexts the data that was passed in order to help the user know what they want to update
            expenseName.setText(expensename);
            expenseAmount.setText(expenseamount);
            expenseDate.setText(expensedate);

        }


        btnPost = (Button) findViewById(R.id.button_submit); // update button

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



            void insert() {
                try {
                    String e_name = expenseName.getText().toString();
                    String e_amount = expenseAmount.getText().toString();
                    String e_date = expenseDate.getText().toString();

                    PreparedStatement insertdb = null;
                    Class.forName("org.postgresql.Driver");
                    String url = "jdbc:postgresql://138.68.141.18:5432/fypdia2"; // uses driver to interact with database
                    Connection conn = DriverManager.getConnection(url, "root", "Cassie2007"); // connects to database

                    // prepares the sql statement
                    String update = "update getdata_getexpense set expense_name = ?, expense_amount =?, expense_date = ? " +
                            "where id='" + expenseid + "';";
                    insertdb = conn.prepareStatement(update);
                    insertdb.setString(1, e_name);
                    insertdb.setString(2, e_amount);
                    insertdb.setString(3, e_date);

                    insertdb.execute();
                    insertdb.close(); // close connection must be done

                    // once inserted into database goes back to listLessons to show it in the db
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getBaseContext(), "Expense updated! ", Toast.LENGTH_LONG).show();
                            Intent i = new Intent(UpdateExpense.this, Finances.class); // goes back to finance page
                            startActivity(i);
                            finish();

                        }
                    });

                    insertdb.close();
                    conn.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });


    } // end onCreate


    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
        Toast.makeText(getApplicationContext(), "Select date of the expense", Toast.LENGTH_SHORT).show();
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

            showDate(arg3, arg2, arg1);

        }
    };

    private void showDate(int arg3, int arg2, int arg1){
//    private void showDate(int arg3, String mon, String dayofwk) {
        // sets textView of CLient dob to the calendar input
        expenseDate.setText(new StringBuilder().append(arg3).append("-").append(arg2).append("-").append(arg1));


    }

    // An intent for the user to go back to the main screen.
    public void goBackScreen(View view) {
        try {
            Intent lastScreen = new Intent(this, Finances.class);
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
