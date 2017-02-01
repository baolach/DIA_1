package com.example.baolach.driving_app_3;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

// Main screen showing buttons
public class MainActivity extends Activity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button routes_btn = (Button)findViewById(R.id.routes_btn);
        Button locations_btn = (Button)findViewById(R.id.locations_btn);
        Button logbook_btn = (Button)findViewById(R.id.logbook_btn);
        Button finances_btn = (Button)findViewById(R.id.finances_btn);
        Button statistics_btn = (Button)findViewById(R.id.statistics_btn);
        Button camera_btn = (Button)findViewById(R.id.camera_btn);
        Button admin_btn = (Button)findViewById(R.id.admin_btn);


        // must change intents to match correct pages/actions
        routes_btn.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {

                Intent diary_intent = new Intent(MainActivity.this, Calender.class); // lists all lessons
                startActivity(diary_intent);
            }
        });


        locations_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                Intent new_lesson_intent = new Intent(MainActivity.this, Calender.class); // inserts new lesson
                startActivity(new_lesson_intent);
            }
        });

        // list all clients
        logbook_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {

                Intent clients_intent = new Intent(MainActivity.this, Calender.class); // goes to diary activity and calls the list clients activity
                startActivity(clients_intent);
            }
        });

        finances_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {

                Intent new_client_intent = new Intent(MainActivity.this, Calender.class);
                startActivity(new_client_intent);
            }
        });

        statistics_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {

                Intent calender_intent = new Intent(MainActivity.this, Calender.class);
                startActivity(calender_intent);
            }
        });

        camera_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {

                Intent calender_intent = new Intent(MainActivity.this, Calender.class);
                startActivity(calender_intent);
            }
        });

        // I had to put this in a try catch block for some reason but now it works
        try {
            admin_btn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v)
                {

                    Intent admin_intent = new Intent(MainActivity.this, AdminActivity.class);
                    startActivity(admin_intent);
                }
            });
        } catch ( ActivityNotFoundException e) {
            e.printStackTrace();
        }


    }

//    public void web_btn(View v)
//    {
//        Intent web_intent = new Intent(this, WebActivity.class);
//        startActivity(web_intent);
//        //  Half Button for RSA website
//
//    }


    /* most important during lesson
    Routes
    Locations
    Logbook
    Finances
    Statistics
    Camera



    // admin stuff for after
    Diary
    New Lesson
    List all Client
    Reminders - set reminders or have them automatic for each lesson

*/




}




