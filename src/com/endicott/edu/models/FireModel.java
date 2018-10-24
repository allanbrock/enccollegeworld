package com.endicott.edu.models;

import com.endicott.edu.datalayer.BuildingDao;

import java.io.Serializable;
import java.util.ArrayList;

public class FireModel implements Serializable {
    private int costOfFire = 0;
    //private int durationOfFire = 0;
    private int numOfFatalities = 0;
    //private int numBuildingsOnFire = 0;
    private String runId = "unknown";
    private String description;
    private BuildingModel buildingBurned;
    //private ArrayList<String> listofBurningBuildings;
    //private ArrayList<BuildingModel> listofCollegeBuildings;

    public FireModel(){

    }

    public FireModel(int costOfFire, int numOfFatalities, String runId, BuildingModel buildingBurned){
        this.runId = runId;
        this.numOfFatalities = numOfFatalities;
        this.costOfFire = costOfFire;
        this.buildingBurned = buildingBurned;
        this.description = "";//buildingBurned.getName() + " caught fire! There were " + numOfFatalities + " deaths.";

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
        String names = "";

        if (victims.equalsIgnoreCase("No one")) {
            this.description = this.buildingBurned.getName() + " caught fire! Everyone made it out safe.";
        }

        this.description = this.buildingBurned.getName() + " caught fire! " + victims + " died in the fire.";
    }
}
