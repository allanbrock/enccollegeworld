package com.endicott.edu.models;
import com.endicott.edu.datalayer.NameGenDao;
import com.endicott.edu.simulators.FacultyManager;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Implemented 9-28-17 by Mazlin Higbee
 * mhigb411@mail.endicott.edu
 */
public class FacultyModel extends PersonModel implements Serializable{

    private String facultyID; // a unique id for that member of the faculty.
    private String collegeID;
//    private String facultyName; //simply the name
    private String title; //EX: Assoicate prof, Dean, VP...
    private String departmentName; //department of the faculty member EX: Math, Computer Science, Biology
    private int salary;   //private int salary = 115000; //yearly salary
//    private int happiness;
    private int performance;
    private Boolean raiseRecentlyGiven;
    private Boolean underPerforming;

    public FacultyModel(){}

    public FacultyModel( String title, String department, int salary, String officeLocation, String facultyID, Boolean isFemale) {
        super.firstName = NameGenDao.generateFirstName(isFemale);
        super.lastName = NameGenDao.generateLastName();
        super.name = "Dr. " + super.firstName + " " + super.lastName;
        this.title = title;
        this.departmentName = department;
        this.facultyID = facultyID;
    }

    public FacultyModel(String title, String department, String collegeID, int salary, Boolean isFemale) {
        super.firstName = NameGenDao.generateFirstName(isFemale);
        super.lastName = NameGenDao.generateLastName();
        super.name = "Dr. " + super.firstName + " " + super.lastName;
        this.title = title;
        this.departmentName = department;
        this.collegeID = collegeID;
        this.salary = salary;
        this.facultyID = FacultyManager.generateFacultyID(this);
        this.performance = 75;
        this.raiseRecentlyGiven = false;
        this.underPerforming = false;
        FacultyManager.computeFacultyHappiness(this, false);
    }

    public Boolean getUnderPerforming(){ return underPerforming; }
    public void setUnderPerforming(Boolean underPerforming){ this.underPerforming = underPerforming; }

//    public String getFacultyName() {
//        return facultyName;
//    }

//    public void setFacultyName(String facultyName) {
//        this.facultyName = facultyName;
//    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String department) {
        this.departmentName = department;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public String getFacultyID() { return facultyID; }
    public void setFacultyID(String facultyID) { this.facultyID = facultyID;}

//    public void setHappiness(int happiness){ this.happiness = happiness; }
//    public int getHappiness() { return this.happiness; }

    public void setCollegeID(String collegeID){ this.collegeID = collegeID; }
    public String getCollegeID() { return this.collegeID; }

    public int getPerformance(){ return this.performance; }
    public void setPerformance(int performance) { this.performance = performance; }

    public void setRaiseRecentlyGiven(Boolean raiseRecentlyGiven){ this.raiseRecentlyGiven = raiseRecentlyGiven; }
    public Boolean getRaiseRecentlyGiven(){ return this.raiseRecentlyGiven; }

}
