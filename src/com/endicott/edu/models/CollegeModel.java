package com.endicott.edu.models;

//import com.endicott.edu.datalayer.PopupEventDao;

import java.io.Serializable;

public class CollegeModel implements Serializable {
    private int hoursAlive = 0;              // hours the college has been in existence
    private int availableCash = 20000;           // amount of money in college bank account
    private int yearlyTuitionCost = 40000;   // the amount it costs to attend the school for a single year
    private int reputation = 50;             // reputation of college based on 1-100
    private String runId = "unknown";        // name of the college
    private String note = "empty";           // note for debugging
    private int studentBodyHappiness;        // out of 100, 0 is unhappy
    private int studentFacultyRatio = 100;     // number of students per faculty member
    private int numberStudentsAdmitted = 0;  // number of students admitted since college created.
    private int numberStudentsAccepted = 0;  // number of students accepted to the school.
    private int numberStudentsWithdrew = 0;  // number of students withdrawn since college created.
    private int numberStudentsGraduated = 0; // number of students graduate since college created
    private float retentionRate = 100f;        // percentage of students retained (or graduated) since college created
    private int yearlyTuitionRating = 0;     // 0 to 100 rating of happiness corresponding to tuition.
    private int studentFacultyRatioRating = 0; // 0 to 100 rating of student faculty ratio
    private int studentHealthRating = 0;       // 0 to 100 rating of student health
    private int studentRecreationalHappiness = 0; // 0 to 100 rating of student recreational happiness
    private int studentFinancialHappiness = 0; // 0 to 100 rating of student recreational happiness
    private int totalBuildingHealth = 0; // 0 to 100 average of all buildings health
    private int departmentCount = 4;
    private int academicRating = 0;
    private int gate = 0;                    // This is the current gate or level that is open.
    private CollegeMode mode = CollegeMode.PLAY;
    private int daysUntilNextEvent = 3;
    private boolean isTimePaused = false;
    //public PopupEventModel;

    //Counts amount of total championships won to be tracked in the trophy case
    private int footballChampionships = 0;
    private int mHockeyChampionships = 0;
    private int wHockeyChampionships = 0;
    private int mSoccerChampionships = 0;
    private int wSoccerChampionships = 0;
    private int baseballChampionships = 0;
    private int softballChampionships = 0;
    private int mBasketballChampionships = 0;
    private int wBasketballChampionships = 0;

    public int getDepartmentCount(){ return departmentCount; }
    public void setDepartmentCount(int departmentCount){ this.departmentCount = departmentCount; }

    public int getStudentHealthRating() {
        return studentHealthRating;
    }

    public void setStudentHealthRating(int studentHealthRating) {
        this.studentHealthRating = studentHealthRating;
    }

    public void setStudentRecreationalHappiness(int studentRecreationalHappiness) {this.studentRecreationalHappiness = studentRecreationalHappiness;}

    public int getStudentRecreationalHappiness() {return studentRecreationalHappiness;}

    public void setStudentFinancialHappiness(int studentFinancialHappiness) { this.studentFinancialHappiness = studentFinancialHappiness;}

    public int getStudentFinancialHappiness() {return studentFinancialHappiness;}

    public int getStudentFacultyRatioRating() {
        return studentFacultyRatioRating;
    }

    public void setTotalBuildingHealth(int totalBuildingHealth) {this.totalBuildingHealth= totalBuildingHealth;}

    public int getTotalBuildingHealth() {return totalBuildingHealth;}

    public void setStudentFacultyRatioRating(int studentFacultyRatioRating) {
        this.studentFacultyRatioRating = studentFacultyRatioRating;
    }

    public int getYearlyTuitionRating() {
        return yearlyTuitionRating;
    }

    public void setYearlyTuitionRating(int yearlyTuitionRating) {
        this.yearlyTuitionRating = yearlyTuitionRating;
    }

    public int getReputation() { return reputation; }

    public void setReputation(int reputation) { this.reputation = reputation; }

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

    public int getStudentFacultyRatio() { return studentFacultyRatio; }

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

    public void setStudentFacultyRatio(int studentFacultyRatio) { this.studentFacultyRatio = studentFacultyRatio; }

    public int getNumberStudentsAdmitted() { return numberStudentsAdmitted; }

    public void setNumberStudentsAdmitted(int numberStudentsAdmitted) { this.numberStudentsAdmitted = numberStudentsAdmitted; }

    public int getNumberStudentsWithdrew() { return numberStudentsWithdrew; }

    public void setNumberStudentsWithdrew(int numberStudentsWithdrew) { this.numberStudentsWithdrew = numberStudentsWithdrew; }

    public int getNumberStudentsGraduated() { return numberStudentsGraduated; }

    public void setNumberStudentsGraduated(int numberStudentsGraduated) { this.numberStudentsGraduated = numberStudentsGraduated; }

    public float getRetentionRate() { return retentionRate; }

    public void setRetentionRate(float retentionRate) { this.retentionRate = retentionRate; }

    public int getNumberStudentsAccepted() { return numberStudentsAccepted; }

    public void setNumberStudentsAccepted(int numberStudentsAccepted) { this.numberStudentsAccepted = numberStudentsAccepted; }

    public boolean getIsTimePaused() { return isTimePaused; }

    public void setIsTimePaused(boolean condition) { this.isTimePaused = condition; }

    public CollegeMode getMode() {
        return mode;
    }

    public void setMode(CollegeMode mode) {
        this.mode = mode;
    }

    public void setMode(String mode) {
        //Testing the "PopupEventDao"
//        if (mode.equals("Demo")){
//            new PopupEventDao().newPopupEvent("DEMO", "DEMO","TEXT", "CallBack", "image", "TEXT" );
//        }

        this.mode = CollegeMode.PLAY;

        if (mode.equals("Play")) this.mode = CollegeMode.PLAY;
        else if (mode.equals("Demo")) this.mode = CollegeMode.DEMO;
        else if (mode.equals("Demo Fire")) this.mode = CollegeMode.DEMO_FIRE;
        else if (mode.equals("Demo Plague")) this.mode = CollegeMode.DEMO_PLAGUE;
        else if (mode.equals("Demo Riot")) this.mode = CollegeMode.DEMO_RIOT;
        else if (mode.equals("Demo Snow")) this.mode = CollegeMode.DEMO_SNOW;
        else if (mode.equals("Demo Flood")) this.mode = CollegeMode.DEMO_FLOOD;
        else if (mode.equals("Demo Championship")) this.mode = CollegeMode.DEMO_CHAMPIONSHIP;
        else if (mode.equals("Demo Plague Mutation")) this.mode = CollegeMode.DEMO_ZOMBIE_MUTATION;
    }

    //Getters for Championships Won
    public int getFootballChampionships() { return footballChampionships;}

    public int getMSoccerChampionships() { return mSoccerChampionships;}

    public int getWSoccerChampionships() { return wSoccerChampionships;}

    public int getMHockeyChampionships() { return mHockeyChampionships;}

    public int getWHockeyChampionships() { return wHockeyChampionships;}

    public int getMBasketballChampionships() { return mBasketballChampionships;}

    public int getWBasketballChampionships() { return wBasketballChampionships;}

    public int getBaseballChampionships() { return baseballChampionships;}

    public int getSoftballChampionships() { return softballChampionships;}

    public int getAcademicRating() { return academicRating; }

    public void setAcademicRating(int academicRating) { this.academicRating = academicRating; }

    public int getGate() {
        return gate;
    }

    public void setGate(int gate) {
        this.gate = gate;
    }

    public int getDaysUntilNextEvent() {
        return daysUntilNextEvent;
    }

    public void setDaysUntilNextEvent(int daysUntilNextEvent) {
        this.daysUntilNextEvent = daysUntilNextEvent;
    }
}