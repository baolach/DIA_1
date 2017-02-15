package com.example.baolach.driving_app_3;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created by Baolach on 04/02/2017.
 */

public class FinancesCursorAdapter extends CursorAdapter {

    public FinancesCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    // The newView method is used to inflate a new view and return it,
    // you don't bind any data to the view at this point.
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.clientnames, parent, false); // will show client payments
    }


    // The bindView method is used to bind all data to a given view
    // such as setting the text on a TextView.
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find fields to populate in inflated template
        TextView clientName = (TextView) view.findViewById(R.id.TextView_client_name); // what is read in here should be displayed
        TextView clientPayment = (TextView) view.findViewById(R.id.TextView_log_no);

        // Extract properties from cursor
        String names = cursor.getString(cursor.getColumnIndexOrThrow(DBManager.KEY_TASK_CLIENT_NAME));
        String log_nos = cursor.getString(cursor.getColumnIndexOrThrow(DBManager.KEY_TASK_LOG_NO)); // will be KEY_TASK_BALANCE_DUE //  I want to do the calculation and return the balance due which should be stored in the db under the clients name

        // Populate fields with extracted properties
        clientName.setText(names);
        clientPayment.setText(log_nos); // will be payments - log_nos


    }
}
