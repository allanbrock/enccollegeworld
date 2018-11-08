package com.endicott.edu.models;

public class CoachModel extends FacultyModel {
    private String teamName;

    public CoachModel(String teamName, String name, String title, String department, String collegeID, int salary){
        super(name, title, department, collegeID, salary);
        this.teamName = teamName;
    }

    public String getTeamName() { return teamName; }
    public void setTeamName(String teamName) { this.teamName = teamName; }
}
