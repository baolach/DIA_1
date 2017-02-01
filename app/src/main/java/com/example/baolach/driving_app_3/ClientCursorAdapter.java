package com.example.baolach.driving_app_3;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;



public class ClientCursorAdapter extends CursorAdapter{

    public ClientCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    // The newView method is used to inflate a new view and return it,
    // you don't bind any data to the view at this point.
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.clientnames, parent, false);
    }


    // The bindView method is used to bind all data to a given view
    // such as setting the text on a TextView.
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find fields to populate in inflated template
        TextView clientName = (TextView) view.findViewById(R.id.TextView_client_name); // what is read in here should be displayed
        TextView logNumber = (TextView) view.findViewById(R.id.TextView_log_no);

        // Extract properties from cursor
        String names = cursor.getString(cursor.getColumnIndexOrThrow(DBManager.KEY_TASK_CLIENT_NAME));
        String log_nos = cursor.getString(cursor.getColumnIndexOrThrow(DBManager.KEY_TASK_LOG_NO));

        // Populate fields with extracted properties
        clientName.setText(names);
        logNumber.setText(log_nos);


    }

}

