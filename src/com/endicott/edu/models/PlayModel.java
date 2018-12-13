package com.endicott.edu.models;

import java.io.Serializable;

public class PlayModel implements Serializable {
    public int getPayout() {
        return payout;
    }

    public void setPayout(int payout) {
        this.payout = payout;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public enum PlayState {
        JUST_STARTED,
        DIRECTOR_PICKED,
        PLAY_IN_PERFORMANCE
    }

    private PlayState state;
    private String director;
    private int daysSincePurchase;
    private int payout;
    private boolean isDone;

    public PlayModel(){
        this.setDirector("");
        this.state = PlayState.JUST_STARTED;
        this.daysSincePurchase = 0;
        this.payout = 2000;
        this.isDone = false;
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
