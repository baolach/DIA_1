package com.example.baolach.driving_app_3;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

// Main screen showing buttons
public class MainActivity extends Activity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button admin_btn = (Button)findViewById(R.id.admin_btn);
        Button maps_btn = (Button)findViewById(R.id.maps_btn);
        Button directions_btn = (Button)findViewById(R.id.directions_btn);
        Button web_btn = (Button)findViewById(R.id.web_btn);
        Button logout_btn = (Button)findViewById(R.id.logout_btn);

        try {
            admin_btn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v)
                {
                    Intent admin_intent = new Intent(MainActivity.this, AdminActivity.class);
                    startActivity(admin_intent);
                    finish();

                }
            });
        } catch ( ActivityNotFoundException e) {
            e.printStackTrace();
        }

        // sets onClick listeners for maps_btn
        maps_btn.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {

                Intent maps = new Intent(MainActivity.this, MapsActivity.class); // Once clicked goes to map activity
                Toast.makeText(getApplicationContext(), "To add location tap on the map at the location and tap the add button", Toast.LENGTH_LONG).show();
                startActivity(maps);
                finish();
            }
        });




        // list all clients
        directions_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                Intent traffic_intent = new Intent(MainActivity.this, TrafficActivity.class);
                startActivity(traffic_intent);
                finish();
            }
        });



        web_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                Intent web_intent = new Intent(MainActivity.this, WebActivity.class);
                startActivity(web_intent);
                finish();

            }
        });

        logout_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                Intent logout_intent = new Intent(MainActivity.this, Login.class); // inserts new lesson
                Toast.makeText(getApplicationContext(), "Logged out", Toast.LENGTH_LONG).show();
                startActivity(logout_intent);
                finish();

            }
        });

    } // end onCreate
}




