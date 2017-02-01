package com.example.baolach.driving_app_3;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

// This class displays the client names in a list to the user
public class ListLessons extends Activity
{
    DBManager db = new DBManager(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_lessons);


        final ListView listView = (ListView) findViewById(R.id.listView_lessons); // listView from the list_lessons.xml
        try {
            db.open();
            Cursor result = db.getAllLessons();
            LessonCursorAdapter cursorAdapter = new LessonCursorAdapter(ListLessons.this, result);
            listView.setAdapter(cursorAdapter);
            db.close();
        } catch (Exception e) {
            e.printStackTrace();

        }

        // When a client is clicked it goes to the ClientInfo activity and displays all info on that client
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long arg) {
                try {
                    Cursor myCursor = (Cursor) parent.getItemAtPosition(position); // where the info is stored on what you clicked
                    String lessonname = myCursor.getString(1); // 4th position in the clients table (LOG NUMBER)
                    String lessondate = myCursor.getString(2);
                    String lessontime = myCursor.getString(3);
                    String lessonlocation = myCursor.getString(4);
                    String lessoncomments = myCursor.getString(5);

                    Intent i = new Intent(ListLessons.this, LessonInfo.class);

                    i.putExtra("thelessonname", lessonname);
                    i.putExtra("thelessondate", lessondate);
                    i.putExtra("thelessontime", lessontime);
                    i.putExtra("thelessonlocation", lessonlocation);
                    i.putExtra("thelessoncomments", lessoncomments);

                    startActivity(i);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public void listLessonName(View view) {
        try {
            Intent lesson_name_intent = new Intent(this, InsertLesson.class);
            startActivity(lesson_name_intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
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

