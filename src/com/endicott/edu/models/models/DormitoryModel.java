package com.endicott.edu.models.models;

import java.io.Serializable;

/**
 * Created by abrocken on 7/10/2017.
 */
public class DormitoryModel implements Serializable {
    private int capacity = 0;
    private float costPerHour = 0;
    private int hourLastUpdated = 0;
    private float hoursToComplete = 0;
    public int numStudents = 0;
    public String curDisaster = "none";
    private String name = "unknown";
    private String runId = "unknown";
    private String note = "no note";
    //dorms start at a middle reputation (5/10) upon creation. (0/10 is the worst reputation, 10/10 is the best).
    public int reputation = 0;
    private int dormType = 0;
    private float buildCost = 0;
    //per year maintenance cost
    private float maintenanceCost = 0;
    private int numRooms = 0;
    private float squareFeet;

    private DormitoryModel() {
    }

    public DormitoryModel(int capacity, int hourLastUpdated, String name, int numStudents,
                          String curDisaster, int reputation, String runId, int numRooms,
                          float maintenanceCost) {
        this.capacity = capacity;
        this.hourLastUpdated = hourLastUpdated;
        this.name = name;
        this.numStudents=numStudents;
        this.curDisaster = curDisaster;
        this.reputation = reputation;
        this.runId = runId;
        this.numRooms = numRooms;
        this.squareFeet = 250 * numRooms;
        this.maintenanceCost = maintenanceCost;
        this.costPerHour = (squareFeet * 2) / 24;
        this.hoursToComplete = squareFeet * 2; 
    }

    public float getHoursToComplete() {
        return hoursToComplete;
    }
    public void setHoursToComplete(float hoursToComplete) {
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

//    public String getDormClass() {
//        return dormClass;
//    }
//
//    public void setDormClass(String dormClass) {
//        this.dormClass = dormClass;
//    }

    public int getReputation() {
        return reputation;
    }

    public void setReputation(int reputation) {
        this.reputation = reputation;
    }

    public float getBuildCost() {
        return buildCost;
    }

    public void setBuildCost(float buildCost) {
        this.buildCost = buildCost;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public float getCostPerHour() {
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

    public int getDormType() {
        return dormType;
    }

    public void setDormType(int dormType) {
        this.dormType = dormType;
    }
}