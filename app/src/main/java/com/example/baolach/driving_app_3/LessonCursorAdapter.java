package com.example.baolach.driving_app_3;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class LessonCursorAdapter extends CursorAdapter{

    public LessonCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    // The newView method is used to inflate a new view and return it,
    // you don't bind any data to the view at this point.
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.lessoninfo, parent, false); // the listview to show the lessoninfo
    }

    // The bindView method is used to bind all data to a given view
    // such as setting the text on a TextView.
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find fields to populate in inflated template
        TextView lessonName = (TextView) view.findViewById(R.id.TextView_lesson_name); // what is read in here should be displayed
        TextView lessonDate = (TextView) view.findViewById(R.id.TextView_lesson_date);
        TextView lessonTime = (TextView) view.findViewById(R.id.TextView_lesson_time);

        // Extract properties from cursor
        String lesson_names = cursor.getString(cursor.getColumnIndexOrThrow(DBManager.KEY_TASK_LESSON_NAME));
        String lesson_dates = cursor.getString(cursor.getColumnIndexOrThrow(DBManager.KEY_TASK_LESSON_DATE));
        String lesson_times = cursor.getString(cursor.getColumnIndexOrThrow(DBManager.KEY_TASK_LESSON_TIME));

        // Populate fields with extracted properties
        lessonName.setText(lesson_names);
        lessonDate.setText(lesson_dates);
        lessonTime.setText(lesson_times);

    }

}

