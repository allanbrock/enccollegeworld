package com.endicott.edu.models;

import java.io.Serializable;

/**
 * Created by CJ Mustone
 */
public class FireModel implements Serializable {
    private int costOfFire = 0;
    private int numOfFatalities = 0;
    private String runId = "unknown";
    private String description;
    private BuildingModel buildingBurned;

    public FireModel(int costOfFire, int numOfFatalities, String runId, BuildingModel buildingBurned){
        this.runId = runId;
        this.numOfFatalities = numOfFatalities;
        this.costOfFire = costOfFire;
        this.buildingBurned = buildingBurned;
        this.description = "";
    }

    public String getDescription(){
        return this.description;
    }


    public int getCostOfFire() {
        return costOfFire;
    }

    public void setCostOfFire(int costOfFire) {
        this.costOfFire = costOfFire;
    }


    public int getNumOfFatalities() {
        return numOfFatalities;
    }

    public void setNumOfFatalities(int numOfFatalities) {
        this.numOfFatalities = numOfFatalities;
    }


    public String getRunId() {
        return runId;
    }

    public void setRunId(String runId) {
        this.runId = runId;
    }
    public BuildingModel getBuildingBurned(){
        return this.buildingBurned;
    }

    public void setDescription(String victims) {
        if (victims.equalsIgnoreCase("No one")) {
            this.description = this.buildingBurned.getName() + " caught fire! Everyone made it out safe.";
        } else if (victims.equalsIgnoreCase("all")){
            this.description = "Catastrophic fire occured in " + this.buildingBurned.getName() + ". If anyone was inside they didn't survive."+
            "Purchase an upgrade to reduce the chance of these major fires from happening.";
        } else {
            this.description = this.buildingBurned.getName() + " caught fire! " + victims + " died in the fire. Upgrading " +
            "the college's fire detection at the store will reduce the chance of losing students to fires.";
        }
    }
}