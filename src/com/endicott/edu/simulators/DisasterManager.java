package com.endicott.edu.simulators;

import java.util.ArrayList;
import java.util.List;

public class DisasterManager {
    private FireManager fireManager;
    private PlagueManager plagueManager;
    private FloodManager floodManager;
    private SnowManager snowManager;
    private RiotManager riotManager;


    public DisasterManager(){

    }

    public void handleTimeChange(String runId, int hoursAlive){

    }

    public boolean isCurrentDisaster(){
        // loop through each manager and check if any are active
        // if any one is active return true
        return true;
    }

    public int getTimeLeftInCurDisaster(){
        return 0;

    }


}
