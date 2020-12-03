package com.endicott.edu.models;

import java.io.Serializable;
import java.util.HashMap;

public class DepartmentModel implements Serializable {
    private String departmentName;
    private int departmentRating;
    private HashMap<String, Integer> employeeCounts;
    private Boolean hasDean, hasAssistantDean, bonusGiven;

    public DepartmentModel(String name){
        this.departmentName = name;
        departmentRating = 0;
        employeeCounts = new HashMap<>();
        employeeCounts.put("Dean", 0);
        employeeCounts.put("Assistant Dean", 0);
        employeeCounts.put("Faculty", 0);
        hasDean = true;
        hasAssistantDean = true;
        bonusGiven = false;
    }

    public String getDepartmentName(){ return this.departmentName; }
    public HashMap<String, Integer> getEmployeeCounts(){
        return employeeCounts;
    }
    public void setEmployeeCount(String key, int value) { employeeCounts.put(key, value); }
    public void putInEmployeeCounts(String key, int count){
        employeeCounts.put(key, count);
    }
    public int getOverallEmployeeCount(){ return employeeCounts.get("Dean") + employeeCounts.get("Assistant Dean") + employeeCounts.get("Faculty"); }
    public int getDepartmentRating(){ return this.departmentRating; }
    public void setDepartmentRating(int departmentRating){ this.departmentRating = departmentRating; }
    public Boolean getBonusGiven() { return bonusGiven; }
    public void setBonusGiven(Boolean bonusGiven) { this.bonusGiven = bonusGiven; }
}
