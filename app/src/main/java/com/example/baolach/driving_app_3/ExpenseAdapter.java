package com.example.baolach.driving_app_3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Baolach on 15/04/2017.
 */

public class ExpenseAdapter extends BaseAdapter{
    private Context context;
    private int layout;
    private ArrayList<Expense> expenselist;

    public ExpenseAdapter(Context context, int layout, ArrayList<Expense> expenselist) {
        this.context = context;
        this.layout = layout;
        this.expenselist = expenselist;
    }

    @Override
    public int getCount() {

        return expenselist.size();
    }

    @Override
    public Object getItem(int position) {
        return expenselist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    private class ViewHolder {
        TextView expense_name, expense_amount, expense_date; // no id needed here
    }


    @Override
    public View getView(int position, View view, ViewGroup parent) {

        View row = view;
        ExpenseAdapter.ViewHolder holder = new ExpenseAdapter.ViewHolder();

        if(row == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout, null);
            holder.expense_name = (TextView) row.findViewById(R.id.expenses);
            holder.expense_amount = (TextView) row.findViewById(R.id.amount);
            holder.expense_date = (TextView) row.findViewById(R.id.date);




            row.setTag(holder);
        }
        else
        {
            holder = (ExpenseAdapter.ViewHolder) row.getTag();
        }

        // from the Expense class
        Expense expense = expenselist.get(position);
        holder.expense_name.setText(expense.getName());
        holder.expense_amount.setText(expense.getAmount());
        holder.expense_date.setText(expense.getDate());


        return row;
    }
}
