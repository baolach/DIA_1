package com.example.baolach.driving_app_3;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Baolach on 04/02/2017.
 *
 * I dont want to insert all these into the database
 * I think this should be the calculate activity so when I input the details
 * the payment is calculated, that price is then put into the database in the client
 * table and is saved to be shown in the financesActivity listview
 *
 */

public class InsertFinances extends Activity {

    /*
    DBManager db = new DBManager(this);

    EditText clientName;
    EditText driverNo;
    EditText noOfLessons;
    EditText lessonsToBePaid;
    EditText pricePerLesson;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.insert_finance_details);

        Button setButton = (Button) findViewById(R.id.calculate);

        setButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    db.open();

                    clientName = (EditText) findViewById(R.id.editText_clientName);
                    driverNo = (EditText) findViewById(R.id.editText_driverNo);
                    noOfLessons = (EditText) findViewById(R.id.editText_noOfLessons);
                    lessonsToBePaid = (EditText) findViewById(R.id.editText_lessonsToBePaid);
                    pricePerLesson = (EditText) findViewById(R.id.editText_pricePerLesson);


                    ///////// Should I convert them all to strings?
                    db.insertPayment(
                            clientName.getText().toString(),
                            driverNo.getText().toString(),
                            Integer.parseInt(noOfLessons.getText().toString()),
                            Integer.parseInt(lessonsToBePaid.getText().toString()),
                            Integer.parseInt(pricePerLesson.getText().toString())
                            );


                    db.close();

                    // return to the home screen.
                    Intent homeScreen = new Intent(InsertFinances.this, AdminActivity.class);
                    startActivity(homeScreen);
                } catch (Exception e) {

                    e.printStackTrace();

                    Context context = getApplicationContext();
                    CharSequence text = "Error opening database";
                    int duration = Toast.LENGTH_LONG;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }


            }
        });

    }

    // An intent for the user to go back to the main screen.
    public void goBackScreen(View view) {
        try {
            Intent lastScreen = new Intent(this, AdminActivity.class);
            startActivity(lastScreen);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    */
}
