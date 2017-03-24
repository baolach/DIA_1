package com.example.baolach.driving_app_3;

/**
 * Created by Baolach on 21/03/2017.
 */

public class Client {

    private int id;
    private String name, phone, address, logno;

    public Client(int id, String name, String phone, String address, String logno) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.logno = logno;

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

    public String getAddress() { return address; }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLogno() {
        return logno;
    }

    public void setLogno(String logno) {
        this.logno = logno;
    }

}
