package com.endicott.edu.models.models;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Connor Frazier on 10/16/2017.
 */

public class StudentsModel implements Serializable {
    static private List<StudentModel> studentList;
    static private int retentionRate;
    static private int happinessLevel;
    static private int graduationRate;

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


    public static void calculateStudentsStats(){
        int happinessSum = 0;
        for(int i = 0; i < studentList.size(); i++){
            happinessSum += studentList.get(i).getHappinessLevel();
        }
        happinessLevel = happinessSum/studentList.size();

        //need calculation for grad rate
        //  based off days alive and happiness + reputation


        //need calculation for retention rate
        //  based off happiness level


    }
}
