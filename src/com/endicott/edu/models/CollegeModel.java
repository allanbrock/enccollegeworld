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
    private float collegeScore = 0f;
    private int studentBodyHappiness;
    private int studentFacultyRatio = 1;
    private int numberStudentsAdmitted = 0;
    private int numberStudentsWithdrew = 0;
    private int numberStudentsGraduated = 0;
    private float retentionRate = 0f;

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

    public float getCollegeScore() { return collegeScore; }

    public void setCollegeScore(float collegeScore) { this.collegeScore = collegeScore; }
}
