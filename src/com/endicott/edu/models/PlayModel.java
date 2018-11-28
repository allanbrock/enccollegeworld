package com.endicott.edu.models;

import java.util.ArrayList;

public class PlayModel {
    private int castSize;
    private int daysToPerformance = 60;
    private ArrayList<StudentModel> cast;

    public PlayModel(){

    }

    public PlayModel(int castSize){
        this.castSize = castSize;
    }

    public int getCastSize() {
        return castSize;
    }

    public void setCastSize(int castSize) {
        this.castSize = castSize;
    }

    public int getDaysToPerformance() {
        return daysToPerformance;
    }

    public void setDaysToPerformance(int daysToPerformance) {
        this.daysToPerformance = daysToPerformance;
    }

    public ArrayList<StudentModel> getCast() {
        return cast;
    }

    public void setCast(ArrayList<StudentModel> cast) {
        this.cast = cast;
    }
}
