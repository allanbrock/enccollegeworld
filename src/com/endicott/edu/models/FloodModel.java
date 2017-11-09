package com.endicott.edu.models;

import java.io.Serializable;

/**
 * Created by cseidl on 9/29/17.
 */
public class FloodModel implements Serializable {


    private int hoursLasted = 0;
    private int hourLastUpdated = 0;
    private String note = "no note";
    private String runId = "unknown";
    private String dormName = "unknown";

    public FloodModel() {
    }

    public FloodModel(int hoursLasted, int hourLastUpdated, String dormName, String runId) {
        this.hoursLasted = hoursLasted;
        this.hourLastUpdated = hourLastUpdated;
        this.dormName = dormName;
        this.runId = runId;
    }




    public int getHourLastUpdated() {
        return hourLastUpdated;
    }

    public void setHourLastUpdated(int hourLastUpdated) {
        this.hourLastUpdated = hourLastUpdated;
    }

    public int getHoursLasted() { return hoursLasted; }

    public void setHoursLasted(int hoursLasted) { this.hoursLasted = hoursLasted; }

    public String getDormName() { return dormName; }

    public void setDormName(String dormName) { this.dormName = dormName; }

    public String getNote() { return note; }

    public void setNote(String note) { this.note = note; }

    public String getRunId() { return runId; }

    public void setRunId(String runId) { this.runId = runId; }
}