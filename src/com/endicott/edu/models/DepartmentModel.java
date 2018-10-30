package com.endicott.edu.models;

import java.util.HashMap;
import java.util.List;

public class DepartmentModel {
    private String departmentName;
    private int departmentRating;
    private HashMap<String, Integer> employeeCounts;

    public DepartmentModel(String name){
        this.departmentName = name;
        employeeCounts = new HashMap<>();
    }

    public void setDepartmentRating(List<Integer> facultyPerformances){

    }

    public HashMap<String, Integer> getEmployeeCounts(){
        return employeeCounts;
    }

    public void putInEmployeeCounts(String key, int count){
        employeeCounts.put(key, count);
    }
}
