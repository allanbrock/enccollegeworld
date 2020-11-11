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

    public TipsModel() {
        academicTips = new ArrayList<>();
        athleticTips = new ArrayList<>();
        infrastructureTips = new ArrayList<>();
        safetyTips = new ArrayList<>();
        valueTips = new ArrayList<>();
        socialTips = new ArrayList<>();
    }

    public void setAcademicTips(ArrayList<String> at) { academicTips = at; }
    public ArrayList<String> getAcademicTips(){ return academicTips; }

    public void setAthleticTips(ArrayList<String> at) { athleticTips = at; }
    public ArrayList<String> getAthleticTips(){ return athleticTips; }

    public void setInfrastructureTips(ArrayList<String> it) { infrastructureTips = it; }
    public ArrayList<String> getInfrastructureTips(){ return infrastructureTips; }

    public void setSafetyTips(ArrayList<String> st) { safetyTips = st; }
    public ArrayList<String> getSafetyTips(){ return safetyTips; }

    public void setValueTips(ArrayList<String> vt) { valueTips = vt; }
    public ArrayList<String> getValueTips(){ return valueTips; }

    public void setSocialTips(ArrayList<String> st) { socialTips = st; }
    public ArrayList<String> getSocialTips(){ return socialTips; }

}
