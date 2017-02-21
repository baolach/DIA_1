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
 */
public class InsertLesson extends Activity {

    DBManager db = new DBManager(this);

    EditText lessonName;
    EditText lessonDate;
    EditText lessonTime;
    EditText lessonLocation;
    EditText lessonComments;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.insert_lesson_details);

        Button setButton = (Button) findViewById(R.id.button_submit);

        setButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    db.open();

                    lessonName = (EditText) findViewById(R.id.editText_lessonName);
                    lessonDate = (EditText) findViewById(R.id.editText_lessonDate);
                    lessonTime = (EditText) findViewById(R.id.editText_lessonTime);
                    lessonLocation = (EditText) findViewById(R.id.editText_lessonLocation);
                    lessonComments = (EditText) findViewById(R.id.editText_lessonComments);

                    db.insertLesson(lessonName.getText().toString(),
                            lessonDate.getText().toString(),
                            lessonTime.getText().toString(),
                            lessonLocation.getText().toString(),
                            lessonComments.getText().toString()
                    );

                    db.close();

                    // return to the home screen.
                    Intent homeScreen = new Intent(InsertLesson.this, ListLessons.class);
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
}


