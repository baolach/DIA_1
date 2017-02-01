package com.example.baolach.driving_app_3;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

/**
 */
public class Calender extends Activity {

    DBManager db = new DBManager(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calender);

        final ListView listView = (ListView) findViewById(R.id.listView_lessons); // lsitView from the list_lessons.xml
        try {
            db.open();
            Cursor result = db.getAllLessons();
            LessonCursorAdapter cursorAdapter = new LessonCursorAdapter(Calender.this, result);
            listView.setAdapter(cursorAdapter);
            db.close();
        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    public void goBackScreen(View view) {
        try {
            Intent lastScreen = new Intent(this, MainActivity.class);
            startActivity(lastScreen);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
