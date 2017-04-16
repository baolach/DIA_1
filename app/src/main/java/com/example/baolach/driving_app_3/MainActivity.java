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

        Button maps_btn = (Button)findViewById(R.id.maps_btn);
        Button locations_btn = (Button)findViewById(R.id.locations_btn);
        Button google_maps_btn = (Button)findViewById(R.id.google_maps_btn);
        Button finances_btn = (Button)findViewById(R.id.finances_btn);
        Button admin_btn = (Button)findViewById(R.id.admin_btn);

        // sets onClick listeners for maps_btn
        maps_btn.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {

                Intent maps = new Intent(MainActivity.this, MapsActivity.class); // Once clicked goes to map activity
                startActivity(maps);
                finish();
            }
        });


        locations_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                Intent locations_intent = new Intent(MainActivity.this, AndroidGPSTrackingActivity.class); // inserts new lesson
                //setContentView(R.layout.content_maps);
                startActivity(locations_intent);
                finish();

            }
        });

        // list all clients
        google_maps_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                Intent traffic_intent = new Intent(MainActivity.this, TrafficActivity.class);
                startActivity(traffic_intent);
                finish();
               //Intent clients_intent = new Intent(MainActivity.this, HttpURLConnectionExample.class); // goes to diary activity and calls the list clients activity
                //startActivity(clients_intent);
            }
        });

        finances_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {

                Intent finances_intent = new Intent(MainActivity.this, Finances.class);
                startActivity(finances_intent);
            }
        });



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

    } // end onCreate

    public void web_btn(View v)
    {
        Intent web_intent = new Intent(this, WebActivity.class);
        startActivity(web_intent);

    }

}




