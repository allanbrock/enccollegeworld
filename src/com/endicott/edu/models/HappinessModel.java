package com.endicott.edu.models;


import java.io.Serializable;


public class HappinessModel implements Serializable {
    private int healthRating = 0;
    private int academicRating = 0;
    private int professorRating = 0;
    private int moneyRating = 0;
    private int funRating = 0;
    private int advisorRating = 0;
    private int diningRating = 0;
    private int academicCenterRating = 0;
    private int dormRating = 0;
    private int overallBuildingRating = 0;

    public HappinessModel() {

    }


    public int getHealthRating() {
        return healthRating;
    }

    public void setHealthRating(int healthRating) {
        this.healthRating = healthRating;
    }

    public int getAcademicRating() {
        return academicRating;
    }

    public void setAcademicRating(int academicRating) {
        this.academicRating = academicRating;
    }

    public int getProfessorRating() {
        return professorRating;
    }

    public void setProfessorRating(int professorRating) {
        this.professorRating = professorRating;
    }

    public int getMoneyRating() {
        return moneyRating;
    }

    public void setMoneyRating(int moneyRating) {
        this.moneyRating = moneyRating;
    }

    public int getFunRating() {
        return funRating;
    }

    public void setFunRating(int funRating) {
        this.funRating = funRating;
    }

    public int getAdvisorRating() {
        return advisorRating;
    }

    public void setAdvisorRating(int advisorRating) {
        this.advisorRating = advisorRating;
    }

    public int getDiningRating() {
        return diningRating;
    }

    public void setDiningRating(int diningRating) {
        this.diningRating = diningRating;
    }

    public int getAcademicCenterRating() {
        return academicCenterRating;
    }

    public void setAcademicCenterRating(int academicCenterRating) {
        this.academicCenterRating = academicCenterRating;
    }

    public int getDormRating() {
        return dormRating;
    }

    public void setDormRating(int dormRating) {
        this.dormRating = dormRating;
    }

    public int getOverallBuildingRating() {
        return overallBuildingRating;
    }

    public void setOverallBuildingRating(int overallBuildingRating) {
        this.overallBuildingRating = overallBuildingRating;
    }
}
