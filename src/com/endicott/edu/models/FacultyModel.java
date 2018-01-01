package com.endicott.edu.models;

import java.io.Serializable;
/**
 * Implemented 9-28-17 by Mazlin Higbee
 * mhigb411@mail.endicott.edu
 */
public class FacultyModel implements Serializable {

    private long facultyID = -2; // a unique id for that member of the faculty.
    private String facultyName; //simply the name
    private String title; //EX: Assoicate prof, Dean, VP...
    private String department; //department of the faculty member EX: Math, Computer Science, Biology
    private int salary = 115000; //yearly salary
    private String officeLocation; //office building and number
    private String runId;

    public FacultyModel() {
    }

    public FacultyModel(String facultyName, String title, String department, int salary, String officeLocation, String runId) {
        this.facultyName = facultyName;
        this.title = title;
        this.department = department;
        this.salary = salary;
        this.officeLocation = officeLocation;
        this.runId = runId;
        this.facultyID = facultyID;
    }

    public FacultyModel(String facultyName, String title, String department, String officeLocation, String runId) {
        this.facultyName = facultyName;
        this.title = title;
        this.department = department;
        this.officeLocation = officeLocation;
        this.runId = runId;
        this.facultyID = facultyID;
        this.salary = salary;
    }

    public String getFacultyName() {
        return facultyName;
    }

    public void setFacultyName(String facultyName) {
        this.facultyName = facultyName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public String getOfficeLocation() {
        return officeLocation;
    }

    public void setOfficeLocation(String officeLocation) {
        this.officeLocation = officeLocation;
    }

    public String getRunId() {
        return runId;
    }

    public void setRunId(String runId) {
        this.runId = runId;
    }
    public long getFacultyID() { return facultyID; }
    public void setFacultyID(long facultyID) { this.facultyID = facultyID;}

}
