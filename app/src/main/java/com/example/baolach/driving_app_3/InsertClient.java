package com.example.baolach.driving_app_3;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class InsertClient extends Activity{

        DBManager db = new DBManager(this);

        EditText clientName;
        EditText clientPhone;
        EditText clientAddress;
        EditText clientLogNo;
        EditText clientDriverNo;
        EditText clientDob;
        EditText clientNoOfLessons;
        EditText clientComments;


        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.insert_client_details);

            Button setButton = (Button) findViewById(R.id.button_submit);

            setButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v) {
                    try
                    {
                        db.open();

                        clientName = (EditText) findViewById(R.id.editText_clientName);
                        clientPhone = (EditText) findViewById(R.id.editText_clientPhone);
                        clientAddress = (EditText) findViewById(R.id.editText_clientAddress);
                        clientLogNo = (EditText) findViewById(R.id.editText_clientLogNo);
                        clientDriverNo = (EditText) findViewById(R.id.editText_clientDriverNo);
                        clientDob = (EditText) findViewById(R.id.editText_clientDob);
                        clientNoOfLessons = (EditText) findViewById(R.id.editText_clientNoOfLessons);
                        clientComments = (EditText) findViewById(R.id.editText_clientComments);

                        db.insertClient(clientName.getText().toString(),
                                clientPhone.getText().toString(),
                                clientAddress.getText().toString(),
                                clientLogNo.getText().toString(),
                                clientDriverNo.getText().toString(),
                                clientDob.getText().toString(),
                                clientNoOfLessons.getText().toString(),
                                clientComments.getText().toString()
                                );


                        db.close();

                        // return to the main screen
                        Intent homeScreen = new Intent(InsertClient.this, AdminActivity.class);
                        startActivity(homeScreen);
                    } catch (Exception e)
                    {

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

        // An intent for the user to go back to the main screen
        public void goBackScreen(View view) {
            try {
                Intent lastScreen = new Intent(this, AdminActivity.class);
                startActivity(lastScreen);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }