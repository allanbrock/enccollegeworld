package com.endicott.edu.models.models;

import java.io.Serializable;

/**
 * Created by abrocken on 7/10/2017.
 */
public class DormitoryModel implements Serializable {
    private int capacity = 0;
    private int costPerHour = 0;
    private String runId = "unknown";
    private String note = "no note";
    private int hourLastTimeBillPaid = 0;
    private String name = "unknown";

    public DormitoryModel() {
    }

    public DormitoryModel(int capacity, int costPerHour, int elapsedHoursSinceCharge, String name, String runId) {
        this.capacity = capacity;
        this.costPerHour = costPerHour;
        this.hourLastTimeBillPaid = elapsedHoursSinceCharge;
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

    public int getHourLastTimeBillPaid() {
        return hourLastTimeBillPaid;
    }

    public void setHourLastTimeBillPaid(int hourLastTimeBillPaid) {
        this.hourLastTimeBillPaid = hourLastTimeBillPaid;
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
