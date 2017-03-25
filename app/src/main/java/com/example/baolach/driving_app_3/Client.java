package com.example.baolach.driving_app_3;

/**
 * Created by Baolach on 21/03/2017.
 */

public class Client {

    private int id;
    private String name, phone, address, logno, driverno, dob, nooflessons, balancedue, comments;

    public String getDriverno() {
        return driverno;
    }

    public void setDriverno(String driverno) {
        this.driverno = driverno;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getNooflessons() {
        return nooflessons;
    }

    public void setNooflessons(String nooflessons) {
        this.nooflessons = nooflessons;
    }

    public String getBalancedue() {
        return balancedue;
    }

    public void setBalancedue(String balancedue) {
        this.balancedue = balancedue;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Client(int id, String name, String phone, String address, String logno, String driverno, String dob, String nooflessons, String balancedue, String comments) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.logno = logno;
        this.driverno = driverno;
        this.dob = dob;
        this.nooflessons = nooflessons;
        this.balancedue = balancedue;
        this.comments = comments;



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
