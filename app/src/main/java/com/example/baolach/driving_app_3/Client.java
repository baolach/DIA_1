package com.example.baolach.driving_app_3;

/**
 * Created by Baolach on 21/03/2017.
 */

public class Client {

    private int id;
    private String name, phone;

    public Client(int id, String name, String phone) {
        this.id = id;
        this.name = name;
        this.phone = phone;

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

}
