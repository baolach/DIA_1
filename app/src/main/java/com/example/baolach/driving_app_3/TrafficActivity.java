package com.example.baolach.driving_app_3;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

// brings user to Google Maps where the instructor insserts a location and driections are given along
// with traffic alerts. This activity is also opened when an address of any sort is clicked where
// the address is passed and displayed on the map

public class TrafficActivity extends AppCompatActivity{
    @Override
    public void onCreate(Bundle website)
    {
        super.onCreate(website);
        setContentView(R.layout.activity_web);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://www.google.ie/maps/place/Dublin/@53.308301,-6.3245127,13.49z/data=!4m5!3m4!1s0x48670e80ea27ac2f:0xa00c7a9973171a0!8m2!3d53.3498053!4d-6.2603097!5m1!1e1"));
        startActivity(intent);
    }
}
