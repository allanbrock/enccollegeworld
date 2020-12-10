package com.endicott.edu.models;

import com.endicott.edu.datalayer.CollegeDao;
import com.endicott.edu.simulators.FacultyManager;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class TipsModel implements Serializable {

    private ArrayList<String> academicTips;         //Holds all academic tips that the page will display
    private ArrayList<String> athleticTips;         //Holds all athletic tips that the page will display
    private ArrayList<String> infrastructureTips;   //Holds all infrastructure tips that the page will display
    private ArrayList<String> safetyTips;           //Holds all safety tips that the page will display
    private ArrayList<String> valueTips;            //Holds all value tips that the page will display
    private ArrayList<String> socialTips;           //Holds all social tips that the page will display
    private ArrayList<String> admissionsTips;       //Holds all social tips that the page will display
    private ArrayList<String> generalTips;          //Holds all general tips that the page will display
    private ArrayList<String> objectivesTips;       //Holds all social tips that the page will display
    private ArrayList<String> schoolTraitsTips;       //Holds all social tips that the page will display

    public TipsModel() {
        academicTips = new ArrayList<>();
        athleticTips = new ArrayList<>();
        infrastructureTips = new ArrayList<>();
        safetyTips = new ArrayList<>();
        valueTips = new ArrayList<>();
        socialTips = new ArrayList<>();
        admissionsTips = new ArrayList<>();
        generalTips = new ArrayList<>();
        objectivesTips = new ArrayList<>();
        schoolTraitsTips = new ArrayList<>();
    }

    public ArrayList<String> getAcademicTips(){ return academicTips; }

    public ArrayList<String> getAthleticTips(){ return athleticTips; }

    public ArrayList<String> getInfrastructureTips(){ return infrastructureTips; }

    public ArrayList<String> getSafetyTips(){ return safetyTips; }

    public ArrayList<String> getValueTips(){ return valueTips; }

    public ArrayList<String> getSocialTips(){ return socialTips; }

    public ArrayList<String> getAdmissionsTips() {return admissionsTips; }

    public ArrayList<String> getGeneralTips() {return generalTips; }

    public ArrayList<String> getObjectivesTips() {return objectivesTips; }

    public ArrayList<String> getSchoolTraitsTips() {return schoolTraitsTips; }
}
