package com.endicott.edu.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DepartmentModel {
    private String departmentName;
    private int departmentRating;
    private HashMap<String, Integer> employeeCounts;
    private Boolean hasDean, hasAssistantDean;

    public DepartmentModel(String name){
        this.departmentName = name;
        employeeCounts = new HashMap<>();
        employeeCounts.put("Dean", 0);
        employeeCounts.put("Assistant Dean", 0);
        employeeCounts.put("Faculty", 0);
        hasDean = true;
        hasAssistantDean = true;
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
    public void setDepartmentRationg(int departmentRating){ this.departmentRating = departmentRating; }

}
