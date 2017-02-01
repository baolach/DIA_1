package com.example.baolach.driving_app_3;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class WebActivity extends AppCompatActivity
{

    // Reference: A couple of StackOverflow examples googleing web views
    @Override
    public void onCreate(Bundle website)
    {
        super.onCreate(website);
        setContentView(R.layout.activity_web);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("http://activeschoolofmotoring.com"));
        startActivity(intent);
    }
}
