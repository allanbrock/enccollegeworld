package com.endicott.edu.models;

import com.endicott.edu.simulators.CoachManager;
public class CoachModel extends FacultyModel {
    private String sportName;


    public CoachModel(String sportName, String title, String department, String collegeID, int salary, Boolean isFemale){
        super(title, department, collegeID, salary, isFemale);
        this.sportName = sportName;
        CoachManager.addToCollegeCoaches(this);
    }

    public String getSportName() { return sportName; }
    public void setSportName(String teamName) { this.sportName = teamName; }

}
