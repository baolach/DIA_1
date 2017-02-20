package com.example.baolach.driving_app_3;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;

// Main screen showing buttons
public class MainActivity extends Activity
{
    GoogleMap mMap; // m - member variable
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9001;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        if(servicesOK()){
            //setContentView(R.layout.activity_maps);
            setContentView(R.layout.activity_main);
            Toast.makeText(this, "Ready to map!", Toast.LENGTH_SHORT).show();
        }


        Button routes_btn = (Button)findViewById(R.id.routes_btn);
        Button locations_btn = (Button)findViewById(R.id.locations_btn);
        Button logbook_btn = (Button)findViewById(R.id.logbook_btn);
        Button finances_btn = (Button)findViewById(R.id.finances_btn);
        Button statistics_btn = (Button)findViewById(R.id.statistics_btn);
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
                Intent locations_intent = new Intent(MainActivity.this, MapsActivity.class); // inserts new lesson
                setContentView(R.layout.activity_main);
                startActivity(locations_intent);
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

                Intent finances_intent = new Intent(MainActivity.this, Finances.class);
                startActivity(finances_intent);
            }
        });

        statistics_btn.setOnClickListener(new View.OnClickListener() {
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

    public boolean servicesOK(){
//        int isAvailable = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
//        if(isAvailable == ConnectionResult.SUCCESS){
//            return true;
//        }else if(GooglePlayServicesUtil.isUserRecoverableError(isAvailable)){
//            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(isAvailable), this, ERROR_DIALOG_REQUEST);
//            dialog.show();
//        }else{
//            Toast.makeText(this, "Can't connect to mapping service", Toast.LENGTH_SHORT).show();
//        }
//
//        return false;



        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(this);
        if(result != ConnectionResult.SUCCESS) {
            if(googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(this, result,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            }

            return false;
        }

        return true;
    }

//    public void web_btn(View v)
//    {
//        Intent web_intent = new Intent(this, WebActivity.class);
//        startActivity(web_intent);
//        //  Half Button for RSA website
//
//    }






}




