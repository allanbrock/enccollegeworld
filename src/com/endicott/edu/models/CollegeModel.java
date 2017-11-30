package com.endicott.edu.models;

import java.io.Serializable;

/**
 * Created by abrocken on 7/10/2017.
 */
//@XmlRootElement
public class CollegeModel implements Serializable {
    private int hoursAlive = 0;
    private int availableCash = 0;
    private int yearlyTuitionCost = 40000; //the amount it costs to attend the school for a single year
    private int reputation = 50; //reputation of college based on 1-100
    private String runId = "unknown";
    private String note = "empty";
    private int studentBodyHappiness;
    private int studentFacultyRatio = 1;

    public int getReputation() {
        return reputation;
    }

    public void setReputation(int reputation) {
        this.reputation = reputation;
    }

    public int getYearlyTuitionCost() {
        return yearlyTuitionCost;
    }

    public void setYearlyTuitionCost(int yearlyTuitionCost) {
        this.yearlyTuitionCost = yearlyTuitionCost;
    }

    public int getAvailableCash() {
        return availableCash;
    }

    public void setAvailableCash(int availableCash) {
        this.availableCash = availableCash;
    }

    public String getRunId() { return runId; }

    public void setRunId(String runId) {
        this.runId = runId;
    }

    public int getHoursAlive() {
        return hoursAlive;
    }

    public void setHoursAlive(int hoursAlive) {
        this.hoursAlive = hoursAlive;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void advanceClock(int hours) {
        setHoursAlive(getHoursAlive() + hours);
    }

    public int getCurrentDay() {
        return hoursAlive / 24 + 1;
    }

    public int getStudentBodyHappiness() { return studentBodyHappiness; }

    public void setStudentBodyHappiness(int studentBodyHappiness) { this.studentBodyHappiness = studentBodyHappiness; }

    public int getStudentFacultyRatio() { return studentFacultyRatio; }

    public void setStudentFacultyRatio(int studentFacultyRatio) { this.studentFacultyRatio = studentFacultyRatio; }
}
