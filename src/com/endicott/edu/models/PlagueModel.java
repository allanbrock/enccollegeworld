package com.endicott.edu.models;

import java.io.Serializable;

/**
 * Created by dyannone on 10/10/17.
 */
public class PlagueModel implements Serializable {


    private int daysLasted = 0;
    private int studentsSick = 0;//?
    private int dayLastUpdated = 0;//?
    private int studentsGone = 0;//?
    private int costToCure = 1000;
    private String name = "Sickness";//?
    private String note = "no note";
    private String runId = "unknown";
    private String dormName = "unknown";//?
    private int hourLastUpdated;  // the time we last updated this record
    private int numberOfHoursLeftInPlague;  // if 0, then plague is over.  No one new can get sick.

    public PlagueModel() {
    }

    public PlagueModel(int daysLasted, int dayLastUpdated, String dormName, String runId, int studentsSick, int studentsGone, int costToCure, int numberOfHoursLeftInPlague, int hourLastUpdated) {
        this.daysLasted = daysLasted;
        this.dayLastUpdated = dayLastUpdated;
        this.dormName = dormName;
        this.runId = runId;
        this.studentsSick = studentsSick;
        this.studentsGone = studentsGone;
        this.name = "Sickness";
        this.costToCure = costToCure;
        this.numberOfHoursLeftInPlague = numberOfHoursLeftInPlague;
        this.hourLastUpdated = hourLastUpdated;
    }




    public int getDayLastUpdated() {
        return dayLastUpdated;
    }

    public void setDayLastUpdated(int dayLastUpdated) {
        this.dayLastUpdated = dayLastUpdated;
    }

    public int getDaysLasted() { return daysLasted; }

    public void setDaysLasted(int daysLasted) { this.daysLasted = daysLasted; }

    public String getDormName() { return dormName; }

    public void setDormName(String dormName) { this.dormName = dormName; }

    public String getNote() { return note; }

    public void setNote(String note) { this.note = note; }

    public String getRunId() { return runId; }

    public void setRunId(String runId) { this.runId = runId; }

    public int getStudentSick() { return studentsSick; }

    public void setStudentsSick(int studentsSick) { this.studentsSick = studentsSick; }

    public int getStudentGone() { return studentsGone; }

    public void setStudentsGone(int studentsGone) { this.studentsSick = studentsGone; }

    public String getName() { return name; }

    public int getCostToCure() { return costToCure; }

    public void setCostToCure(int costToCure) { this.costToCure = costToCure; }

    public int getHourLastUpdated() {
        return hourLastUpdated;
    }

    public void setHourLastUpdated(int hourLastUpdated) {
        this.hourLastUpdated = hourLastUpdated;
    }

    public int getNumberOfHoursLeftInPlague() { return numberOfHoursLeftInPlague; }

    public void setNumberOfHoursLeftInPlague(int numberOfHoursLeftInPlague) { this.numberOfHoursLeftInPlague = numberOfHoursLeftInPlague; }

}