package com.example.baolach.driving_app_3;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


/**
 * Created by Baolach on 28/01/2017.
 */
//        Admin Page
//        List lessons
//        New Lesson
//        List all Client
        // new client
//        Reminders - set reminders or have them automatic for each lesson

    // admin page showing the different admin options
public class AdminActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_activity);

        Button list_lessons_btn = (Button)findViewById(R.id.list_lessons_btn);
        Button new_lesson_btn = (Button)findViewById(R.id.new_lesson_btn);
        Button list_clients_btn = (Button)findViewById(R.id.list_clients_btn);
        Button new_client_btn = (Button)findViewById(R.id.new_client_btn);
        Button reminders_btn = (Button)findViewById(R.id.reminders_btn);
        Button back_screen_btn = (Button)findViewById(R.id.back_screen_btn);



        back_screen_btn.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {

                Intent back_screen_intent = new Intent(AdminActivity.this, MainActivity.class); // lists all lessons
                startActivity(back_screen_intent);
            }
        });


        list_lessons_btn.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {

                Intent list_lessons_intent = new Intent(AdminActivity.this, ListLessons.class); // lists all lessons
                startActivity(list_lessons_intent);
            }
        });
        new_lesson_btn.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {

                Intent new_lesson_intent = new Intent(AdminActivity.this, InsertLesson.class); // lists all lessons
                startActivity(new_lesson_intent);
            }
        });
        list_clients_btn.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {

                Intent list_clients_intent = new Intent(AdminActivity.this, ListClients.class); // lists all lessons
                startActivity(list_clients_intent);
            }
        });
        new_client_btn.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {

                Intent new_client_intent = new Intent(AdminActivity.this, InsertClient.class); // lists all lessons
                startActivity(new_client_intent);
            }
        });
        reminders_btn.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {

                Intent reminders_intent = new Intent(AdminActivity.this, Reminders.class); // lists all lessons
                startActivity(reminders_intent);
            }
        });






    }
}
