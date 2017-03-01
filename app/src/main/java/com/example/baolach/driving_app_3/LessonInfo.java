package com.example.baolach.driving_app_3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;

/* DELETE BUTTON DOESNT WORK */

public class LessonInfo extends Activity {

    // these 2 added
    DBManager db = new DBManager(this);
    String lesson_name;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_info);

        Intent lessonData = getIntent();

        //Bundle clientData = getIntent().getExtras();

        //put data from list clients activity into new string
        lesson_name = lessonData.getStringExtra("thelessonname");
        //clientsName = lessonData.getStringExtra("thelessonname");
        String TheLessonDate= lessonData.getStringExtra("thelessondate");
        String TheLessonTime = lessonData.getStringExtra("thelessontime");
        String TheLessonLocation = lessonData.getStringExtra("thelessonlocation");
        String TheLessonComments = lessonData.getStringExtra("thelessoncomments");

        //create variable which references output field
        final TextView nameTextView = (TextView) findViewById(R.id.thelessonname);
        final TextView dateTextView = (TextView) findViewById(R.id.thelessondate);
        final TextView timeTextView = (TextView) findViewById(R.id.thelessontime);
        final TextView locationTextView = (TextView) findViewById(R.id.thelessonlocation);
        final TextView commentsTextView = (TextView) findViewById(R.id.thelessoncomments);

        //use setText to change text
        nameTextView.setText(lesson_name);
        dateTextView.setText(TheLessonDate);
        timeTextView.setText(TheLessonTime);
        locationTextView.setText(TheLessonLocation);
        commentsTextView.setText(TheLessonComments);


        Button deleteButton = (Button)findViewById(R.id.delete_client_btn);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                DBManager dbManager = new DBManager(LessonInfo.this);
                try {
                    dbManager.open();
                    dbManager.deleteClient(lesson_name);
                    //Toast.makeText(getApplicationContext(), "Client deleted", Toast.LENGTH_SHORT).show();

                } catch (SQLException e) {
                    Toast.makeText(getApplicationContext(), "Client could not be deleted", Toast.LENGTH_SHORT).show();
                } finally {
                    dbManager.close();

                    Intent intent = new Intent(LessonInfo.this, ListLessons.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // clears previous screens
                    startActivity(intent); // this loads new intent (ListClients

                }

            }
        });

    }

    public void goBackScreen(View view) {
        try {
            Intent lastScreen = new Intent(this, ListLessons.class);
            startActivity(lastScreen);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }




}
