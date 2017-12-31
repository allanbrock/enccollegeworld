package com.endicott.edu.models;

import java.io.Serializable;

public class CollegeModel implements Serializable {
    private int hoursAlive = 0;              // hours the college has been in existence
    private int availableCash = 0;           // amount of money in college bank account
    private int yearlyTuitionCost = 40000;   // the amount it costs to attend the school for a single year
    private int reputation = 50;             // reputation of college based on 1-100
    private String runId = "unknown";        // name of the college
    private String note = "empty";           // note for debugging
    private int studentBodyHappiness;        // out of 100, 0 is unhappy
    private int studentFacultyRatio = 100;     // number of students per faculty member
    private int numberStudentsAdmitted = 0;  // number of students admitted since college created.
    private int numberStudentsWithdrew = 0;  // number of students withdrawn since college created.
    private int numberStudentsGraduated = 0; // number of students graduate since college created
    private float retentionRate = 0f;        // percentage of students retained (or graduated) since college created
    private int yearlyTuitionRating = 0;     // 0 to 100 rating of happiness corresponding to tuition.
    private int studentFacultyRatioRating = 0; // 0 to 100 rating of student faculty ratio
    private int studentHealthRating = 0;       // 0 to 100 rating of student health

    public int getStudentHealthRating() {
        return studentHealthRating;
    }

    public void setStudentHealthRating(int studentHealthRating) {
        this.studentHealthRating = studentHealthRating;
    }

    public int getStudentFacultyRatioRating() {
        return studentFacultyRatioRating;
    }

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

}