package com.endicott.edu.simulators;

import com.endicott.edu.models.CoachModel;

import java.util.ArrayList;

public class CoachManager {
    private static ArrayList<CoachModel> collegeCoaches;

    public static void addToCollegeCoaches(CoachModel coach){
        if(collegeCoaches == null)
            collegeCoaches = new ArrayList<>();
        collegeCoaches.add(coach);
    }

    public static ArrayList<CoachModel> getCollegeCoaches(){ return collegeCoaches; }
}
