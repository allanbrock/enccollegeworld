package com.endicott.edu.models;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Eva Rubio 11/05/2018
 *
*/
public class SnowModel  {


    private String collegeId = "unknown";
    private String description;
    private List <BuildingModel> buildingsAffected;
    private BuildingModel oneBuildingSnowed;
    private int snowIntensity;      //Range from 1-3
    private int costOfPlowing = 0;
    private int numOfSickStudents = 0;
    //private int numOfSickFaculty = 0;
    private int lengthOfSnowStorm = 0;
    private int hoursLeftInSnowStorm = 0;
    private int hourLastUpdated = 0;

    //Intensity 1 and 2 storms.
    public SnowModel (String collegeId, BuildingModel oneBuildingSnowed, int snowIntensity, int costOfPlowing, int lengthOfSnowStorm, int hoursLeftInSnowStorm, int hourLastUpdated){
        this.collegeId = collegeId;
        this.oneBuildingSnowed = oneBuildingSnowed;
        this.snowIntensity = snowIntensity;
        this.costOfPlowing = costOfPlowing;
        this.lengthOfSnowStorm = lengthOfSnowStorm;
        this.hoursLeftInSnowStorm =hoursLeftInSnowStorm;
        this.hourLastUpdated = hourLastUpdated;

    }

    //Intensity 3 storm.
    public SnowModel (String collegeId, List<BuildingModel> buildingsAffected, int snowIntensity, int costOfPlowing, int lengthOfSnowStorm, int hoursLeftInSnowStorm, int hourLastUpdated){
        this.collegeId = collegeId;
        this.buildingsAffected = buildingsAffected;
        this.snowIntensity = snowIntensity;
        this.costOfPlowing = costOfPlowing;
        this.lengthOfSnowStorm = lengthOfSnowStorm;
        this.hoursLeftInSnowStorm =hoursLeftInSnowStorm;
        this.hourLastUpdated = hourLastUpdated;

    }

    public SnowModel( String collegeId, List<BuildingModel> buildingsAffected, int snowIntensity, int costOfPlowing, int numOfSickStudents, int lengthOfSnowStorm, int hoursLeftInSnowStorm, int hourLastUpdated){

        this.collegeId = collegeId;
        this.description ="";
        this.buildingsAffected = buildingsAffected;
        this.snowIntensity = snowIntensity;
        this.costOfPlowing = costOfPlowing;
        this.numOfSickStudents = numOfSickStudents;
        //this.numOfSickFaculty = numOfSickFaculty;
        this.lengthOfSnowStorm =lengthOfSnowStorm;
        this.hoursLeftInSnowStorm = hoursLeftInSnowStorm;
        this.hourLastUpdated = hourLastUpdated;

    }
    public int getSnowIntensity() {
        return this.snowIntensity;
    }
    public void setSnowIntensity(int snowIntensity) {
        this.snowIntensity = snowIntensity;
    }

    public void setOneBuildingSnowed(BuildingModel oneBuildingSnowed) {
        this.oneBuildingSnowed = oneBuildingSnowed;
    }

    public BuildingModel getOneBuildingSnowed() {
        return oneBuildingSnowed;
    }

    public String getCollegeId() {
        return this.collegeId;
    }
    public void setCollegeId(String collegeId) {
        this.collegeId = collegeId;
    }

    public List<BuildingModel> getBuildingsAffectedList() {
        return this.buildingsAffected;
    }

    public void setBuildingsAffectedList(List<BuildingModel> buildingsAffected) {
        this.buildingsAffected = buildingsAffected;
    }
    public int getCostOfPlowing() {
        return this.costOfPlowing;
    }
    public void setCostOfPlowing(int plowingCost) {
        this.costOfPlowing = plowingCost;
    }
    public String getDescription(){
        return this.description;
    }
    public void setDescription(String descrip){
        this.description = descrip;
    }

    public int getNumOfSickStudents() {
        return this.numOfSickStudents;
    }

    public void setNumOfSickStudents(int numOfSickStudents) {
        this.numOfSickStudents = numOfSickStudents;
    }

    public int getHourLastUpdated() {
        return this.hourLastUpdated;
    }

    public void setHourLastUpdated(int hourLastUpdated) {
        this.hourLastUpdated = hourLastUpdated;
    }

    public int getHoursLeftInSnowStorm() {
        return hoursLeftInSnowStorm;
    }

    public void setHoursLeftInSnowStorm(int hoursLeftInSnowStorm) {
        this.hoursLeftInSnowStorm = hoursLeftInSnowStorm;
    }

    public int getLengthOfSnowStorm() {
        return lengthOfSnowStorm;
    }

    public void setLengthOfSnowStorm(int lengthOfSnowStorm) {
        this.lengthOfSnowStorm = lengthOfSnowStorm;
    }
/*
    public int getNumOfSickFaculty() {
        return this.numOfSickFaculty;
    }

    public void setNumOfSickFaculty(int numOfSickFaculty) {
        this.numOfSickFaculty = numOfSickFaculty;
    }
    */
}
