package com.example.baolach.driving_app_3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class LessonInfo extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_info);

        Bundle lessonData = getIntent().getExtras();

        //put data from list clients activity into new string
        String TheLessonName = lessonData.getString("thelessonname");
        String TheLessonDate= lessonData.getString("thelessondate");
        String TheLessonTime = lessonData.getString("thelessontime");
        String TheLessonLocation = lessonData.getString("thelessonlocation");
        String TheLessonComments = lessonData.getString("thelessoncomments");

        //create variable which references output field
        final TextView nameTextView = (TextView) findViewById(R.id.thelessonname);
        final TextView dateTextView = (TextView) findViewById(R.id.thelessondate);
        final TextView timeTextView = (TextView) findViewById(R.id.thelessontime);
        final TextView locationTextView = (TextView) findViewById(R.id.thelessonlocation);
        final TextView commentsTextView = (TextView) findViewById(R.id.thelessoncomments);

        //use setText to change text
        nameTextView.setText(TheLessonName);
        dateTextView.setText(TheLessonDate);
        timeTextView.setText(TheLessonTime);
        locationTextView.setText(TheLessonLocation);
        commentsTextView.setText(TheLessonComments);

    }

    public void goBackScreen(View view) {
        try {
            Intent lastScreen = new Intent(this, AdminActivity.class);
            startActivity(lastScreen);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }


}
