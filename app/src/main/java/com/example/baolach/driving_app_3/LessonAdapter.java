package com.example.baolach.driving_app_3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Baolach on 25/03/2017.
 */

public class LessonAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private ArrayList<Lesson> lessonlist;

    public LessonAdapter(Context context, int layout, ArrayList<Lesson> lessonlist) {
        this.context = context;
        this.layout = layout;
        this.lessonlist = lessonlist;
    }

    @Override
    public int getCount() {

        return lessonlist.size();
    }

    @Override
    public Object getItem(int position) {
        return lessonlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    private class ViewHolder{
        TextView lesson_name,lesson_date, lesson_time, lesson_location, lesson_comments, id ;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        View row = view;
        ViewHolder holder = new ViewHolder();

        if(row == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout, null);
            holder.lesson_name = (TextView) row.findViewById(R.id.TextView_lesson_name);
            holder.lesson_date = (TextView) row.findViewById(R.id.TextView_lesson_date);
            holder.lesson_time = (TextView) row.findViewById(R.id.TextView_lesson_time);
            holder.lesson_location = (TextView) row.findViewById(R.id.log_no);
            holder.lesson_comments = (TextView) row.findViewById(R.id.driver_no);

            // doesnt need to show the id


            row.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) row.getTag();
        }


        Lesson lesson = lessonlist.get(position);
        holder.lesson_name.setText(lesson.getName());
        holder.lesson_date.setText(lesson.getDate());
        holder.lesson_time.setText(lesson.getTime());
        //holder.lesson_location.setText(lesson.getLocation());
       // holder.lesson_comments.setText(lesson.getComments());


        //holder.client_address.setText(client.getAddress());


        return row;
    }
}
