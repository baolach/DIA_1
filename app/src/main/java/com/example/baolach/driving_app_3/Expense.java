package com.example.baolach.driving_app_3;

/**
 * Created by Baolach on 15/04/2017.
 */

public class Expense {

    private int id;
    private String name, amount, date;

    public Expense(int id, String name, String amount, String date, String dbid) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.date = date;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}


