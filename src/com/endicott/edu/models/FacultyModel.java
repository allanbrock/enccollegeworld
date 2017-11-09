package com.endicott.edu.models;

import java.io.Serializable;

public class FacultyModel implements Serializable{

    private int idNumber;

    public FacultyModel() {

    }

    public FacultyModel(int idNumber) {
        this.idNumber = idNumber;
    }

    public int getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(int idNumber) {
        this.idNumber = idNumber;
    }
}
