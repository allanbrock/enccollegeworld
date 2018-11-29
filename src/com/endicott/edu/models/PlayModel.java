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

    public PlayModel(){
        this.state = PlayState.JUST_STARTED;
        this.daysSincePurchase = 0;
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
