package com.endicott.edu.models.models;

import java.io.Serializable;

/**
 * Created by abrocken on 7/10/2017.
 */
public class DormitoryModel implements Serializable {
    private int capacity = 0;
    private int costPerHour = 0;
    private int hourLastUpdated = 0;
    private String name = "unknown";
    private String runId = "unknown";
    private String note = "no note";
    private int hoursToComplete = 0;
    public int numStudents = 0;
    public String curDisaster = "none";
    //There are small, medium, or large classes of dorms.
    private String dormClass = "unknown";
    //dorms start at a middle reputation (5/10) upon creation. (0/10 is the worst reputation, 10/10 is the best).
    public int reputation = 5;

    public int getHoursToComplete() {
        return hoursToComplete;
    }

    public void setHoursToComplete(int hoursToComplete) {
        this.hoursToComplete = hoursToComplete;
    }

    public int getNumStudents() {
        return numStudents;
    }

    public void setNumStudents(int numStudents) {
        this.numStudents = numStudents;
    }

    public String getCurDisaster() {
        return curDisaster;
    }

    public void setCurDisaster(String curDisaster) {
        this.curDisaster = curDisaster;
    }

    public String getDormClass() {
        return dormClass;
    }

    public void setDormClass(String dormClass) {
        this.dormClass = dormClass;
    }

    public int getReputation() {
        return reputation;
    }

    public void setReputation(int reputation) {
        this.reputation = reputation;
    }

//    private int hoursToComplete = 0;
//    public int numStudents = 0;
//    public String curDisaster = "none";
//    //There are small, medium, or large classes of dorms.
//    private String dormClass = "unknown";
//    //dorms start at a middle reputation (5/10) upon creation. (0/10 is the worst reputation, 10/10 is the best).
//    public int reputation = 5;

    public DormitoryModel() {
    }

    public DormitoryModel(int capacity, int costPerHour, int hourLastUpdated, String name, int hoursToComplete,
                          int numStudents, String curDisaster, String dormClass, int reputation, String runId) {
        this.capacity = capacity;
        this.costPerHour = costPerHour;
        this.hourLastUpdated = hourLastUpdated;
        this.name = name;
        this.runId = runId;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getCostPerHour() {
        return costPerHour;
    }

    public void setCostPerHour(int costPerHour) {
        this.costPerHour = costPerHour;
    }

    public int getHourLastUpdated() {
        return hourLastUpdated;
    }

    public void setHourLastUpdated(int hourLastUpdated) {
        this.hourLastUpdated = hourLastUpdated;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRunId() {
        return runId;
    }

    public void setRunId(String runId) {
        this.runId = runId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
