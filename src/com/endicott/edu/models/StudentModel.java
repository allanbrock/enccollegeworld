package com.endicott.edu.models;

import java.io.Serializable;

/**
 * Created by Connor Frazier on 9/12/2017.
 */
public class StudentModel implements Serializable {
    private String name = "unknown";
    private int idNumber = 0;
    private int happinessLevel = 0; //0-100
    private boolean athlete = false;
    private int athleticAbility = 0; //0-10
    private String team = "unknown";
    private String dorm = "unknown";
    private String diningHall = "unknown";
    private String academicBuilding = "unknown";
    private String gender = "unknown";
    private String runId = "unknown";
    private String note = "no note";
    private int numberHoursBeenSick = 0; // number of hours of current illness -- 0 if well
    private int numberHoursLeftBeingSick = 0;
    private int hourLastUpdated = 0;
    private int healthHappinessRating = 0; // 0 if ill, 100 if health
    private int academicHappinessRating = 0; // 0 to 100
    private int moneyHappinessRating = 0; // 0 to 100, determined from college tuition.
    private int funHappinessRating = 0;   // 0 to 100
    private int advisorHappinessRating = 0;
    private FacultyModel advisor;


    public StudentModel() {

    }

    public StudentModel(String name, int idNumber, int happinessLevel, boolean athlete, int athleticAbility, String dorm, String Gender, String runId, int numberHoursBeenSick, int numberHoursLeftBeingSick, int hourLastUpdated) {
        this.name = name;
        this.idNumber = idNumber;
        this.happinessLevel = happinessLevel;
        this.athlete = athlete;
        this.athleticAbility = athleticAbility;
        this.dorm = dorm;
        this.gender = gender;
        this.runId = runId;
        this.numberHoursBeenSick = numberHoursBeenSick;
        this.numberHoursLeftBeingSick = numberHoursLeftBeingSick;
        this.hourLastUpdated = hourLastUpdated;
    }

    public String getName() { return name; }

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

    public String getDiningHall() { return  diningHall; }

    public String getAcademicBuilding() { return  academicBuilding; }

    public String getNote() { return note; }

    public String getGender() {
        return gender;
    }

    public String getRunId() {
        return runId;
    }

    public void setName(String name) { this.name = name; }

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

    public void setDiningHall(String diningHall) {
        this.diningHall = diningHall;
    }

    public void setAcademicBuilding(String academicBuilding) {
        this.academicBuilding = academicBuilding;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setRunId(String runId) { this.runId = runId; }

    public void setNote(String note) { this.note = note; }

    public int getNumberHoursBeenSick() { return numberHoursBeenSick; }

    public void setNumberHoursBeenSick(int numberHoursBeenSick) { this.numberHoursBeenSick = numberHoursBeenSick; }

    public int getNumberHoursLeftBeingSick() {  return numberHoursLeftBeingSick; }

    public void setNumberHoursLeftBeingSick(int numberHoursLeftBeingSick) { this.numberHoursLeftBeingSick = numberHoursLeftBeingSick; }

    public int getHourLastUpdated() { return hourLastUpdated; }

    public void setHourLastUpdated(int hourLastUpdated) { this.hourLastUpdated = hourLastUpdated; }

    public int getHealthHappinessRating() {
        return healthHappinessRating;
    }

    public void setHealthHappinessRating(int healthHappinessRating) {
        this.healthHappinessRating = healthHappinessRating;
    }

    public int getAcademicHappinessRating() {
        return academicHappinessRating;
    }

    public void setAcademicHappinessRating(int academicHappinessRating) {
        this.academicHappinessRating = academicHappinessRating;
    }

    public int getMoneyHappinessRating() {
        return moneyHappinessRating;
    }

    public void setMoneyHappinessRating(int moneyHappinessRating) {
        this.moneyHappinessRating = moneyHappinessRating;
    }

    public int getFunHappinessRating() {
        return funHappinessRating;
    }

    public void setFunHappinessRating(int funHappinessRating) {
        this.funHappinessRating = funHappinessRating;
    }

    public int getAdvisorHappinessHappinessRating() {
        return advisorHappinessRating;
    }

    public void setAdvisorHappinessHappinessRating(int advisorHappinessRating) {
        this.advisorHappinessRating = advisorHappinessRating;
    }

    public void setAdvisor(FacultyModel advisor) { this.advisor = advisor; }
    public FacultyModel getAdvisor() { return this.advisor; }
}

