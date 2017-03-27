package com.example.baolach.driving_app_3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;



/**
 * this activity will list the lessoninfo

 */
public class Diary extends Activity {


    //public static Button diary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_lessons);

    }

    // just goes back to the main screen
    public void goBackScreen(View view) {
        try {
            Intent lastScreen = new Intent(this, MainActivity.class);
            startActivity(lastScreen);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}





