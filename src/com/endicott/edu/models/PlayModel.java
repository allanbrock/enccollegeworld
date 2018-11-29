package com.endicott.edu.models;

import java.util.ArrayList;

public class PlayModel {
    enum PlayState {
        JUST_STARTED,
        DIRECTOR_PICKED,
        PLAY_IN_PERFORMANCE
    }

    private PlayState state;
    private int daysSincePurchase;
    private int castSize;
    private int daysToPerformance = 60;
    private ArrayList<StudentModel> cast;

    public PlayModel(){
        state = PlayState.JUST_STARTED;
        this.daysSincePurchase = 0;
    }

    public PlayModel(int castSize, ArrayList<StudentModel> cast){
        this.castSize = castSize;
        this.cast = cast;
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

    public PlayState getState() {
        return state;
    }

    public void setState(PlayState state) {
        this.state = state;
    }

    public int getDaysSincePurchase() {
        return daysSincePurchase;
    }

    public void setDaysSincePurchase(int daysSincePurchase) {
        this.daysSincePurchase = daysSincePurchase;
    }
}
