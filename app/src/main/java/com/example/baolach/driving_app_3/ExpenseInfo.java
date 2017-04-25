package com.example.baolach.driving_app_3;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;


public class ExpenseInfo extends Activity {

    private Button btnDelete;
    String expensename, expenseamount, expensedate, expenseid;// coming from the intent from Finances;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_expenses_info);


        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        // bundle captures the parameters from the intent sent from Finances.java which lists the clients
        if (bundle != null) {
            expensename = bundle.getString("theexpensename");
            expenseamount = bundle.getString("theexpenseamount");
            expensedate = bundle.getString("theexpensedate");
            expenseid = bundle.getString("theexpenseid");

        }

        // apply to textViews
        TextView expenseTextView = (TextView) findViewById(R.id.theexpensename);
        TextView amountTextView = (TextView) findViewById(R.id.theexpenseamount);
        TextView dateTextView = (TextView) findViewById(R.id.theexpensedate);



        expenseTextView.setText(expensename);
        amountTextView.setText(expenseamount);
        dateTextView.setText(expensedate);

//        // needed for scrollbar on name
//        nameTextView.setMovementMethod(new ScrollingMovementMethod());
//        commentsTextView.setMovementMethod(new ScrollingMovementMethod());
//
//        // needed for marquee scroll
//        addressTextView.setSelected(true);

        btnDelete  = (Button) findViewById(R.id.delete_client_btn);

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Are you sure you want to delete this expense?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                new Thread(new Runnable(){

                    public void run()
                    {
                        delete();
                    }

                }).start();

                finish();
            }

            protected void delete() {

                Statement deletedb = null;
                try {
                    Class.forName("org.postgresql.Driver");
                    String url = "jdbc:postgresql://138.68.141.18:5432/fypdia2"; // uses driver to interact with database
                    Connection conn = DriverManager.getConnection(url, "root", "Cassie2007"); // connects to database
                    conn.setAutoCommit(false);

                    deletedb = conn.createStatement();

                    String sql = "DELETE from getdata_getexpense where expense_name='" + expensename  +
                            "' AND expense_amount='" + expenseamount + "' AND expense_date='" +expensedate + "';" ;

                    System.out.println(sql);

                    deletedb.executeUpdate(sql);
                    conn.commit();

                    // once inserted into database all the edittexts resert to ""
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getBaseContext(), "Expense deleted from database! ", Toast.LENGTH_LONG).show();
                            Intent maps = new Intent(ExpenseInfo.this, Finances.class); // lists all lessoninfo
                            startActivity(maps);

                        }
                    });

                    deletedb.close(); // close connection must be done
                    conn.close();


                }catch(Exception e){
                    e.printStackTrace();
                }
                System.out.println("Delete successful");

            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Do nothing
                dialog.dismiss();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                builder.show();
            }});


    } // end onCreate



//// update expense incase a mistake is made
    public void update(View view)
    {
        try{

            Intent i = new Intent(ExpenseInfo.this, UpdateExpense.class);
            i.putExtra("theexpensename", expensename);
            i.putExtra("theexpenseamount", expenseamount);
            i.putExtra("theexpensedate", expensedate);
            i.putExtra("theexpenseid", expenseid);

            startActivity(i);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public void goBackScreen(View view) {
        try {
            Intent lastScreen = new Intent(this, Finances.class);
            startActivity(lastScreen);
        } catch (Exception e) {
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
