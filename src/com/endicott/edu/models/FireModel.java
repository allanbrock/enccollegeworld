package com.endicott.edu.models;

import java.io.Serializable;

/**
 * Created by CJ Mustone
 */
public class FireModel implements Serializable {
    private int costOfFire = 0;
    private int numOfStudentFatalities = 0;
    private int numOfFacultyFatalities = 0;
    private String runId = "unknown";
    private String description;
    private BuildingModel buildingBurned;
    private boolean isCatastrophic;

    public FireModel(int numOfStudentFatalities, int numOfFacultyFatalities, String runId, BuildingModel buildingBurned){
        this.runId = runId;
        this.numOfStudentFatalities = numOfStudentFatalities;
        this.numOfFacultyFatalities = numOfFacultyFatalities;
        this.buildingBurned = buildingBurned;
        this.description = "";
        this.isCatastrophic = false;
    }

    public String getDescription(){
        return this.description;
    }

    public int getNumOfStudentFatalities(){
        return numOfStudentFatalities;
    }

    public int getNumOfFacultyFatalities(){
        return numOfFacultyFatalities;
    }

    public int getNumTotalFatalities(){
        return numOfFacultyFatalities + numOfStudentFatalities;
    }


    public void setCostOfFire(int cost){
        costOfFire = cost;
    }
    public int getCostOfFire() {
        return costOfFire;
    }

    public String getRunId() {
        return runId;
    }

    public void setCatastrophicStatus(Boolean isThisCatastrophic){
        isCatastrophic = isThisCatastrophic;
    }

    public void setRunId(String runId) {
        this.runId = runId;
    }

    public BuildingModel getBuildingBurned(){
        return this.buildingBurned;
    }

    public void setDescription(String victims, boolean isUpgraded) {
        if (isUpgraded){
            if (victims.equalsIgnoreCase("")) {
                this.description = this.buildingBurned.getName() + " caught fire! Everyone made it out safe.";
            } else if (isCatastrophic){
                this.description = "Catastrophic fire occurred in " + this.buildingBurned.getName() + ". If anyone was inside they didn't survive."+
                        "Visit the Buildings page to rebuild.";
            } else {
                this.description = this.buildingBurned.getName() + " caught fire! " + victims + " died in the fire.";
            }
        } else {
            if (victims.equalsIgnoreCase("")) {
                this.description = this.buildingBurned.getName() + " caught fire! Everyone made it out safe.";
            } else if (isCatastrophic) {
                this.description = "Catastrophic fire occurred in " + this.buildingBurned.getName() + ". If anyone was inside they didn't survive." +
                        "Visit the Buildings page to rebuild. Don't forget you can purchase smoke detectors to reduce the chance of these major fires from happening.";
            } else {
                this.description = this.buildingBurned.getName() + " caught fire! " + victims + " died in the fire. Upgrading " +
                        "the college's Smoke Detectors at the store will reduce the chance of losing students to fires.";
            }
        }
    }

    public boolean isCatastrophic() {
        return this.isCatastrophic;
    }
}
