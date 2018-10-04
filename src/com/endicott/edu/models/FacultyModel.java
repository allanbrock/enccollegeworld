package com.endicott.edu.models;

import com.endicott.edu.datalayer.FacultyDao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Implemented 9-28-17 by Mazlin Higbee
 * mhigb411@mail.endicott.edu
 */
public class FacultyModel implements Serializable {

    private static ArrayList<Integer> salaryOptions;
    private String facultyID; // a unique id for that member of the faculty.
    private String collegeID;
    private String facultyName; //simply the name
    private String title; //EX: Assoicate prof, Dean, VP...
    private String department; //department of the faculty member EX: Math, Computer Science, Biology
    private int salary;   //private int salary = 115000; //yearly salary
    private String officeLocation; //office building and number
    private int happiness;

    public FacultyModel(String facultyName, String title, String department, int salary, String officeLocation, String facultyID) {
        System.out.print("Other constructor hit");
        this.facultyName = facultyName;
        this.title = title;
        this.department = department;
        this.officeLocation = officeLocation;
        this.facultyID = facultyID;
    }

    public FacultyModel(String facultyName, String title, String department, String officeLocation, String collegeID, int salary) {
        this.facultyName = facultyName;
        this.title = title;
        this.department = department;
        this.officeLocation = officeLocation;
        this.collegeID = collegeID;
        this.salary = salary;
        this.facultyID = this.generateFacultyID();
        this.computeFacultyHappiness(salary);
    }

    public String getFacultyName() {
        return facultyName;
    }

    public void setFacultyName(String facultyName) {
        this.facultyName = facultyName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public String getOfficeLocation() {
        return officeLocation;
    }

    public void setOfficeLocation(String officeLocation) {
        this.officeLocation = officeLocation;
    }
    public String getFacultyID() { return facultyID; }
    public void setFacultyID(String facultyID) { this.facultyID = facultyID;}

    public void setHappiness(int happiness){ this.happiness = happiness; }
    public int getHappiness() { return this.happiness; }

    private void computeFacultyHappiness(int pay){
        Random r = new Random();
        int tempHappiness = r.nextInt(100 - 50) + 50;
        for(int i = 0; i < salaryOptions.size(); i++){
            if(pay == salaryOptions.get(i)){
                if(i == 0)
                    happiness = tempHappiness - 15;
                else if(i == 1)
                    happiness = tempHappiness - 5;
                else if(i == 2)
                    happiness = tempHappiness + 5;
                else
                    happiness = tempHappiness + 15;
                break;
            }
        }
        if(happiness > 100)
            happiness = 100;
    }

    private String generateFacultyID(){
        List<FacultyModel> curFaculty = FacultyDao.getFaculty(this.collegeID);
        int randID = this.randIDGeneration();
        for(int i = 0; i < FacultyDao.getFaculty(this.collegeID).size(); i++){
            if(String.valueOf(randID).equals(FacultyDao.getFaculty(this.collegeID).get(i))){
                randID = this.randIDGeneration();
                i = -1;
            }
        }
        return String.valueOf(randID);
    }

    private int randIDGeneration(){
        return (int) (100000 + Math.random() * 900000); // Generates random 6 digit ID
    }

    private static void establishSalaryOptions(){
        salaryOptions = new ArrayList();
        int[] salaries = new int[] {100000, 125000, 150000, 175000};
        for(int i = 0; i < salaries.length; i++){
            salaryOptions.add(salaries[i]);
        }
    }

    public static ArrayList<Integer> getSalaryOptions(){
        establishSalaryOptions();
        return salaryOptions;
    }
}
