package com.endicott.edu.models;

import com.endicott.edu.simulators.CoachManager;

public class CoachModel extends FacultyModel {
    private String sportName;

    public CoachModel(String sportName, String name, String title, String department, String collegeID, int salary){
        super(name, title, department, collegeID, salary);
        this.sportName = sportName;
        CoachManager.addToCollegeCoaches(this);
    }

    public String getSportName() { return sportName; }
    public void setSportName(String teamName) { this.sportName = teamName; }

}
