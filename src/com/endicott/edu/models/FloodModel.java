package com.endicott.edu.models;

import java.io.Serializable;

/**
 * Created by cseidl on 9/29/17.
 */
public class FloodModel implements Serializable {

    private int costOfFlood = 0;
    private int lengthOfFlood = 0;
    private int hoursLeftInFlood = 0;
    private int hourLastUpdated = 0;
    private String note = "no note";
    private String runId = "unknown";
    private String dormName = "unknown";

    public FloodModel() {
    }

    public FloodModel(int costOfFlood, int lengthOfFlood, int hoursLeftInFlood, int hourLastUpdated, String dormName, String runId) {
        this.costOfFlood = costOfFlood;
        this.lengthOfFlood = lengthOfFlood;
        this.hoursLeftInFlood = hoursLeftInFlood;
        this.hourLastUpdated = hourLastUpdated;
        this.dormName = dormName;
        this.runId = runId;
    }

    public int getCostOfFlood() { return costOfFlood; }

    public void setCostOfFlood(int costOfFlood) { this.costOfFlood = costOfFlood; }

    public int getLengthOfFlood() { return lengthOfFlood; }

    public void setLengthOfFlood(int lengthOfFlood) { this.lengthOfFlood = lengthOfFlood; }

    public int getHourLastUpdated() {
        return hourLastUpdated;
    }

    public void setHourLastUpdated(int hourLastUpdated) {
        this.hourLastUpdated = hourLastUpdated;
    }

    public int getHoursLeftInFlood() { return hoursLeftInFlood; }

    public void setHoursLeftInFlood(int hoursLeftInFlood) { this.hoursLeftInFlood = hoursLeftInFlood; }

    public String getDormName() { return dormName; }

    public void setDormName(String dormName) { this.dormName = dormName; }

    public String getNote() { return note; }

    public void setNote(String note) { this.note = note; }

    public String getRunId() { return runId; }

    public void setRunId(String runId) { this.runId = runId; }
}