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

    public String getDepartmentName(){ return this.departmentName; }

    public void computeDepartmentRating(List<Integer> facultyPerformances){

    }

    public HashMap<String, Integer> getEmployeeCounts(){
        return employeeCounts;
    }
    public void putInEmployeeCounts(String key, int count){
        employeeCounts.put(key, count);
    }
    public int getDepartmentEmployeeCount() {
        int employeeCount = 0;
        for(String s : employeeCounts.keySet()){
            employeeCount = employeeCount + employeeCounts.get(s);
        }
        return employeeCount;
    }

}
