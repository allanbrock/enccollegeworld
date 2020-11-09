package com.endicott.edu.models;

import com.endicott.edu.datalayer.CollegeDao;
import com.endicott.edu.simulators.FacultyManager;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class TipsModel implements Serializable {
    private static ArrayList<String> academicTips = new ArrayList<>();         //Holds all academic tips that the page will display
    private static ArrayList<String> athleticTips = new ArrayList<>();         //Holds all athletic tips that the page will display
    private static ArrayList<String> infrastructureTips = new ArrayList<>();   //Holds all infrastructure tips that the page will display
    private static ArrayList<String> safetyTips = new ArrayList<>();           //Holds all safety tips that the page will display
    private static ArrayList<String> valueTips = new ArrayList<>();            //Holds all value tips that the page will display
    private static ArrayList<String> socialTips = new ArrayList<>();           //Holds all social tips that the page will display

    public static void setAcademicTips(ArrayList<String> at) { academicTips = at; }
    public static ArrayList<String> getAcademicTips(){ return academicTips; }

    public static void setAthleticTips(ArrayList<String> at) { athleticTips = at; }
    public static ArrayList<String> getAthleticTips(){ return athleticTips; }

    public static void setInfrastructureTips(ArrayList<String> it) { infrastructureTips = it; }
    public static ArrayList<String> getInfrastructureTips(){ return infrastructureTips; }

    public static void setSafetyTips(ArrayList<String> st) { safetyTips = st; }
    public static ArrayList<String> getSafetyTips(){ return safetyTips; }

    public static void setValueTips(ArrayList<String> vt) { valueTips = vt; }
    public static ArrayList<String> getValueTips(){ return valueTips; }

    public static void setSocialTips(ArrayList<String> st) { socialTips = st; }
    public static ArrayList<String> getSocialTips(){ return socialTips; }

}
