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
 * Created by Baolach on 15/04/2017.
 */

public class InsertExpense extends Activity {
    EditText expenseName;
    EditText expenseAmount;
    TextView expenseDate;

    private TextView lessonDate;
    private Button btnPost;

    // calendar
    private DatePicker datePicker;
    private Calendar calendar;
    private int year, month, day, dayofweek;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.insert_expense_details);




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
        btnPost.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // makes sure no null values
                if ( ( !expenseName.getText().toString().equals("")) && ( !expenseDate.getText().toString().equals(""))
                        && ( !expenseAmount.getText().toString().equals(""))){
                    new Thread(new Runnable() {

                        public void run() {
                            insert();
                        }
                    }).start();
                }
                else{
                    Toast.makeText(getBaseContext(), "Values cannot be empty ", Toast.LENGTH_LONG).show();
                }

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
                    String insert = "insert into getdata_getexpense values (?,?,?)";
                    insertdb = conn.prepareStatement(insert);
                    insertdb.setString(1, e_name);
                    insertdb.setString(2, e_amount);
                    insertdb.setString(3, e_date);

                    insertdb.execute();
                    insertdb.close(); // close connection must be done

                    // once inserted into database goes back to listLessons to show it in the db
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getBaseContext(), "Expense added to database! ", Toast.LENGTH_LONG).show();
                            Intent i = new Intent(InsertExpense.this, Finances.class); // goes back to finance page
                            startActivity(i);
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
                dayofwk = "Tues";

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

//            showDate(arg3, mon, dayofwk);
            showDate(arg3, arg2, arg1);

        }
    };

    private void showDate(int arg3, int arg2, int arg1){
//    private void showDate(int arg3, String mon, String dayofwk) {
        // sets textView of CLient dob to the calendar input
//        expenseDate.setText(new StringBuilder().append(dayofwk).append(" - ").append(arg3).append(" - ").append(mon));
        expenseDate.setText(new StringBuilder().append(arg3).append(" - ").append(arg2).append(" - ").append(arg1));


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
