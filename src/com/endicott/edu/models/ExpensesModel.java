package com.endicott.edu.models;

import com.endicott.edu.simulators.CollegeManager;

import java.io.Serializable;

public class ExpensesModel implements Serializable{
    private float sports;
    private float buildings;
    private float faculty;
    private float store;
    private float loans;
    private float income;
    private float sportsPercent;
    private float buildingsPercent;
    private float facultyPercent;
    private float storePercent;
    private float loansPercent;

    public ExpensesModel(){
        sports = 0;
        buildings = 0;
        faculty = 0;
        store = 0;
        loans = 0;
        income = 0;
        sportsPercent = 0;
        buildingsPercent = 0;
        facultyPercent = 0;
        storePercent = 0;
        loansPercent = 0;
    }

    public void calculateExpenses(){
        float total = sports + buildings + faculty + store + loans;
        sportsPercent = (sports / total) * 100;
        buildingsPercent = (buildings / total) * 100;
        facultyPercent = (faculty / total) * 100;
        storePercent = (store / total) * 100;
        loansPercent = (loans / total) * 100;

    }

    public float getSports() {
        return sports;
    }

    public void setSports(int sports) {
        this.sports += sports;
    }

    public float getBuildings() {
        return buildings;
    }

    public void setBuildings(int buildings) {
        this.buildings += buildings;
    }

    public float getFaculty() {
        return faculty;
    }

    public void setFaculty(int faculty) {
        this.faculty += faculty;
    }

    public float getStore() {
        return store;
    }

    public void setStore(int store) {
        this.store += store;
    }

    public float getLoans() {
        return loans;
    }

    public void setLoans(int loans) {
        this.loans += loans;
    }

    public float getIncome(){
        return income;
    }
    public void setIncome(int income){
        this.income += income;
    }
}
