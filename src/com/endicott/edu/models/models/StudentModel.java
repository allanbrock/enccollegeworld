package com.endicott.edu.models;

import java.io.Serializable;

/**
 * Created by Connor Frazier on 7/10/2017.
 */
public class StudentModel implements Serializable {

    private String name = "unknown";
    private int idNumber = 0;
    private int happinessLevel = 0;
    private boolean athlete = false;
    private int athleticAbility = 0;
    private String team = "unknown";
    private String dorm = "unknown";
    private String gender = "unknown";
    private String runId = "unknown";


    public StudentModel() {
    }

    public StudentModel(String name, int idNumber, int happinessLevel, boolean athlete, int athleticAbility, String dorm, String Gender, String runId) {
        this.name = name;
        this.idNumber = idNumber;
        this.happinessLevel = happinessLevel;
        this.athlete = athlete;
        this.athleticAbility = athleticAbility;
        this.dorm = dorm;
        this.gender = gender;
        this.runId = runId;
    }

    public String getName() {
        return name;
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

    public String getDorm() {
        return dorm;
    }

    public String getGender() {
        return gender;
    }

    public String getRunId() {
        return runId;
    }

    public void setName(String name) {
        this.name = name;
    }

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

    public void setRunId(String runId) { this.runId = runId; }
}

