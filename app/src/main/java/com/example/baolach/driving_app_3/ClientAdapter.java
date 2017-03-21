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

public class ClientAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private ArrayList<Client> clientlist;

    public ClientAdapter(Context context, int layout, ArrayList<Client> bikelist) {
        this.context = context;
        this.layout = layout;
        this.clientlist = bikelist;
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
        TextView client_name,client_phone;
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
            row.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) row.getTag();
        }


        Client client = clientlist.get(position);
        holder.client_name.setText(client.getName());
        holder.client_phone.setText(client.getPhone());
        return row;
    }
}
