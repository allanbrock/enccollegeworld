package com.endicott.edu.models;

import java.io.Serializable;
import java.util.Random;

/**
 * Created by Connor Frazier on 9/12/2017.
 * Edited by Ryan Kelley, Marissa Patti, and Giana Nekitopoulos on 10/5/20
 */
public class StudentModel extends PersonModel implements Serializable {
    private boolean athlete = false;                //Set if they are on a team or not (Based upon athleticAbility)
    private int athleticAbility = 0;                //Rating of 0-10, 10 being best (Randomized when created then set)
    private String team = "unknown";                //Team that the student is apart of (When created, set to empty)
    private String dorm = "unknown";                //Dorm the student is in
    private String diningHall = "unknown";          //Dining hall the student is in
    private String academicBuilding = "unknown";    //Academic Building the student is in
    private String runId = "unknown";               //The ID of the college (just saved into each student)
    private String feedback = "";                   //The student's thoughts about the college (Based off of happiness)
    private String nature = "unknown";              //Trait of the student (a general idea of what kind of person the student is)
    private int numberHoursLeftBeingSick = 0;       //The number of hours the student has left being sick (0 if not sick)
    private int hourLastUpdated = 0;                //Amount of time the student has been alive (in hours)
    private int healthHappinessRating = 0;          //Student's happiness when it comes to health on campus (0-100)
    private int academicHappinessRating = 0;        //Student's happiness when it comes to the academics on campus (0-100)
    private int professorHappinessRating = 0;       //Student's happiness when it comes to the professors (0-100)
    private int moneyHappinessRating = 0;           //Student happiness when it comes to the tuition of the college (0-100)
    private int funHappinessRating = 0;             //Student's happiness when it comes to the entertainment on campus (0-100)
    private int advisorHappinessRating = 0;         //Student's happiness when it comes to their advisor (0-100)
    private int diningHallHappinessRating = 0;      //Student's happiness when it comes to their dining hall building (0-100)
    private int academicCenterHappinessRating = 0;  //Student's happiness when it comes to their academic building (0-100)
    private int dormHappinessRating = 0;            //Student's happiness when it comes to their dorm building (0-100)
    private int overallBuildingHappinessRating = 0; //Student's happiness when it comes to the quality of building on campus (0-100)
    private String advisor ="";                     //The advisor of a student
    private int classYear = 0;                      //The current year the student is in(1=freshman, 2=sophomore etc.)

    private QualityModel quality = null;            // the quality (stats) of the student -- how much the student contributes
    private PersonalityModel personality = null;    // the personality of the student -- how much the student needs to remain happy

    //Constructor to make a new student with preset fields (assumed to be set after creation)
    public StudentModel() {
    }

    //Constructor to create a student with some defined values
    public StudentModel(String firstName, String lastName, GenderModel gender, int idNumber, int happinessLevel, boolean athlete, int athleticAbility, String dorm, String runId, int numberHoursLeftBeingSick, int hourLastUpdated, String nature, int year) {
        super(firstName, lastName, gender, idNumber, happinessLevel);
        this.athlete = athlete;
        this.athleticAbility = athleticAbility;
        this.dorm = dorm;
        this.runId = runId;
        this.numberHoursLeftBeingSick = numberHoursLeftBeingSick;
        this.hourLastUpdated = hourLastUpdated;
        this.nature = nature;
        this.classYear = year;
    }

    public void setPersonality(PersonalityModel pm){ this.personality = pm; }
    public PersonalityModel getPersonality() { return this.personality; }
    public void setQuality(QualityModel qm){ this.quality = qm; }
    public QualityModel getQuality() { return this.quality; }

    public boolean isAthlete() {
        return athlete;
    }
    public void setAthlete(boolean athlete) {
        this.athlete = athlete;
    }

    public int getAthleticAbility() {
        return athleticAbility;
    }
    public void setAthleticAbility(int athleticAbility) {
        this.athleticAbility = athleticAbility;
    }

    public String getTeam() {
        return team;
    }
    public void setTeam(String team) {
        this.team = team;
    }

    public String getDorm() {
        return dorm;
    }
    public void setDorm(String dorm) {
        this.dorm = dorm;
    }

    public String getDiningHall() {
        return diningHall;
    }
    public void setDiningHall(String diningHall) {
        this.diningHall = diningHall;
    }

    public String getAcademicBuilding() {
        return academicBuilding;
    }
    public void setAcademicBuilding(String academicBuilding) {
        this.academicBuilding = academicBuilding;
    }

    public String getFeedback() { return feedback; }
    public void setFeedback(String feedback) { this.feedback = feedback;}

    public String getRunId() {
        return runId;
    }
    public void setRunId(String runId) {
        this.runId = runId;
    }

    public void setNature(String n) {this.nature = n;}
    public String getNature(){return this.nature;}

    public int getNumberHoursLeftBeingSick() {
        return numberHoursLeftBeingSick;
    }
    public void setNumberHoursLeftBeingSick(int numberHoursLeftBeingSick) {this.numberHoursLeftBeingSick = numberHoursLeftBeingSick;}

    public int getHourLastUpdated() {
        return hourLastUpdated;
    }
    public void setHourLastUpdated(int hourLastUpdated) {
        this.hourLastUpdated = hourLastUpdated;
    }

    public int getHealthHappinessRating() {
        return healthHappinessRating;
    }
    public void setHealthHappinessRating(int healthHappinessRating) {this.healthHappinessRating = healthHappinessRating; }

    public int getAcademicHappinessRating() {
        return academicHappinessRating;
    }
    public void setAcademicHappinessRating(int academicHappinessRating) {this.academicHappinessRating = academicHappinessRating;}

    public int getMoneyHappinessRating() {
        return moneyHappinessRating;
    }
    public void setMoneyHappinessRating(int moneyHappinessRating) {
        this.moneyHappinessRating = moneyHappinessRating;
    }

    public int getFunHappinessRating() {
        return funHappinessRating;
    }
    public void setFunHappinessRating(int funHappinessRating) {this.funHappinessRating = funHappinessRating;}

    public int getAdvisorHappinessHappinessRating() {
        return advisorHappinessRating;
    }

    public void setAdvisorHappinessHappinessRating(int advisorHappinessRating) {this.advisorHappinessRating = advisorHappinessRating;}

    public int getDiningHallHappinessRating() {
        return diningHallHappinessRating;
    }
    public void setDiningHallHappinessRating(int diningHallHappinessRating) {this.diningHallHappinessRating = diningHallHappinessRating; }

    public int getAcademicCenterHappinessRating() {
        return academicCenterHappinessRating;
    }
    public void setAcademicCenterHappinessRating(int academicCenterHappinessRating) {this.academicCenterHappinessRating = academicCenterHappinessRating;}

    public int getDormHappinessRating() {
        return dormHappinessRating;
    }
    public void setDormHappinessRating(int dormHappinessRating) {
        this.dormHappinessRating = dormHappinessRating;
    }

    public int getOverallBuildingHappinessRating() {
        return overallBuildingHappinessRating;
    }
    public void setOverallBuildingHappinessRating(int overallBuildingHappinessRating) {this.overallBuildingHappinessRating = overallBuildingHappinessRating;}

    public int getProfessorHappinessRating() {
        return professorHappinessRating;
    }
    public void setProfessorHappinessRating(int professorHappinessRating) {this.professorHappinessRating = professorHappinessRating;}

    public void setAdvisor(FacultyModel advisor) {
        this.advisor = advisor.getFacultyID();
    }
    public String getAdvisor() {
        return this.advisor;
    }

    public static String assignRandomNature() {
        Random rand = new Random();
        if (rand.nextInt(10) > 8)
            return "Impulsive";
        else if (rand.nextInt(10) > 6)
            return "Studious";
        else if (rand.nextInt(10) > 4)
            return "Lazy";
        else if (rand.nextInt(10) > 2)
            return "Rebellious";
        else if (rand.nextInt(10) > 1)
            return "Party Fiend";
        else
            return "Normal";
    }

    public int getClassYear(){
        return classYear;
    }
    public void setClassYear(int classYear){
        this.classYear = classYear;
    }

    public AvatarModel getAvatar(){
        return avatar;
    }
}