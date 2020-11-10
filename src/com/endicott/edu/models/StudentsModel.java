package com.endicott.edu.models;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Connor Frazier on 10/16/2017.
 * NOTE: As of 11/2020 this code seems to be UNUSED!!
 */

public class StudentsModel implements Serializable {
    private List<StudentModel> studentList;
    private int retentionRate;
    private int happinessLevel;
    private int graduationRate;

    public List<StudentModel> getStudentList() {return studentList; }

    public void setStudentList(List<StudentModel> studentList) { this.studentList = studentList; }

    public int getRetentionRate() {
        return retentionRate;
    }

    public void setRetentionRate(int retentionRate) {
        this.retentionRate = retentionRate;
    }

    public int getHappinessLevel() {
        return happinessLevel;
    }

    public void setHappinessLevel(int happinessLevel) {
        this.happinessLevel = happinessLevel;
    }

    public int getGraduationRate() {
        return graduationRate;
    }

    public void setGraduationRate(int graduationRate) {
        this.graduationRate = graduationRate;
    }

}
