package com.endicott.edu.models;

import java.io.Serializable;

public class FacultyModel implements Serializable{

    private String name;
    private int payrate;
    private int idNumber = 0;

    public FacultyModel() {

    }

    public FacultyModel(String name, int payrate, int idNumber) {
        this.name = name;
        this.payrate = payrate;
        this.idNumber = idNumber;
    }

    public FacultyModel(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getIdNumber() {
        return idNumber;
    }

    public int getPayrate() {
        return payrate;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPayrate(int payrate) {
        this.payrate = payrate;
    }

    public void setRandomPayrate() {
        //TO-DO
        //Select random pay rate
    }

    public void setIdNumber(int idNumber) {
        this.idNumber = idNumber;
    }

}
