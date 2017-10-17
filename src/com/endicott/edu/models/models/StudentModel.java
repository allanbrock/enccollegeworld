package com.endicott.edu.models.models;

import java.io.Serializable;

/**
 * Created by Connor Frazier on 9/12/2017.
 */
public class StudentModel implements Serializable {

    private int idNumber = 0;
    private int happinessLevel = 0; //0-100
    private boolean athlete = false;
    private int athleticAbility = 0; //0-10
    private static int tuitionCost = 100;
    private int hourLastUpdated = 0;
    private String team = "unknown";
    private String dorm = "unknown";
    private String gender = "unknown";
    private boolean isSick = false;
    private String runId = "unknown";
    private String note = "no note";


    public StudentModel() {
    }

    public StudentModel(int idNumber, int happinessLevel, boolean athlete, int athleticAbility, String dorm, String Gender, boolean isSick, String runId) {
        this.idNumber = idNumber;
        this.happinessLevel = happinessLevel;
        this.athlete = athlete;
        this.athleticAbility = athleticAbility;
        this.dorm = dorm;
        this.gender = gender;
        this.isSick = isSick;
        this.runId = runId;
    }

    public int getIdNumber() {
        return idNumber;
    }

    public int getHappinessLevel() {
        return happinessLevel;
    }

    public boolean isAthlete() {
        return athlete;
    }

    public int getAthleticAbility() {
        return athleticAbility;
    }

    public String getTeam() {
        return team;
    }

    public String getDorm() { return dorm; }

    public String getNote() { return note; }

    public String getGender() {
        return gender;
    }

    public boolean isSick() { return isSick; }

    public String getRunId() {
        return runId;
    }

    public static int getTuitionCost() {return tuitionCost; }

    public void setIdNumber(int idNumber) {
        this.idNumber = idNumber;
    }

    public void setHappinessLevel(int happinessLevel) {
        this.happinessLevel = happinessLevel;
    }

    public void setAthlete(boolean athlete) {
        this.athlete = athlete;
    }

    public void setAthleticAbility(int athleticAbility) {
        this.athleticAbility = athleticAbility;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public void setDorm(String dorm) {
        this.dorm = dorm;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setSick(boolean sick) { isSick = sick; }

    public void setRunId(String runId) { this.runId = runId; }

    public void setNote(String note) { this.note = note; }

    public static void setTuitionCost(int tuitionCost){ tuitionCost = tuitionCost; }

    public int getHourLastUpdated() { return hourLastUpdated; }

    public void setHourLastUpdated(int hourLastUpdated) { this.hourLastUpdated = hourLastUpdated; }
}

