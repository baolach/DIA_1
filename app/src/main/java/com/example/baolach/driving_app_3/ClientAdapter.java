package com.example.baolach.driving_app_3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Baolach on 21/03/2017.
 */

// I think client.xml belongs to the clientAdapter.java activity
    // then Client.java is just the getetrs and setters

public class ClientAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private ArrayList<Client> clientlist;

    public ClientAdapter(Context context, int layout, ArrayList<Client> clientlist) {
        this.context = context;
        this.layout = layout;
        this.clientlist = clientlist;
    }

    @Override
    public int getCount() {

        return clientlist.size();
    }

    @Override
    public Object getItem(int position) {
        return clientlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    private class ViewHolder{
        TextView client_name,client_phone, client_address, log_no, driver_no, dob, no_of_lessons, balance_due, comments ;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        View row = view;
        ViewHolder holder = new ViewHolder();

        if(row == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout, null);
            holder.client_name = (TextView) row.findViewById(R.id.client_name);
            holder.client_phone = (TextView) row.findViewById(R.id.client_phone);
            holder.client_address = (TextView) row.findViewById(R.id.client_address);
            holder.log_no = (TextView) row.findViewById(R.id.log_no);
            holder.driver_no = (TextView) row.findViewById(R.id.driver_no);
            holder.dob = (TextView) row.findViewById(R.id.dob);
            holder.no_of_lessons = (TextView) row.findViewById(R.id.no_of_lessons);
            holder.balance_due = (TextView) row.findViewById(R.id.balance_paid);
            holder.comments = (TextView) row.findViewById(R.id.comments);



            row.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) row.getTag();
        }


        Client client = clientlist.get(position);
        holder.client_name.setText(client.getName());
        holder.client_phone.setText(client.getPhone());
        //holder.client_address.setText(client.getAddress());


        return row;
    }
}
