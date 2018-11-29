package com.endicott.edu.simulators;

import com.endicott.edu.datalayer.SportsDao;
import com.endicott.edu.models.CoachModel;
import com.endicott.edu.models.SportModel;

import java.util.ArrayList;
import java.util.List;

public class CoachManager {
    private static ArrayList<CoachModel> collegeCoaches;

    public static void addToCollegeCoaches(CoachModel coach){
        if(collegeCoaches == null)
            collegeCoaches = new ArrayList<>();
        collegeCoaches.add(coach);
    }

    public static ArrayList<CoachModel> getCollegeCoaches(){ return collegeCoaches; }

    public static void removeCoach(String collegeId, CoachModel coach){
        SportsDao dao = new SportsDao();
        List<SportModel> newSports = SportsDao.getSports(collegeId);
        collegeCoaches.remove(coach);
        for(SportModel sport : newSports){
            if(sport.getCoachName().equals(coach.getFacultyName()))
                sport.setCoachName("noCoach");
        }
        dao.saveAllSports(collegeId, newSports);
    }

    public static CoachModel getCoachByName(String name){
        if (collegeCoaches == null)
            return null;

        for(CoachModel c : collegeCoaches){
            if(c.getFacultyName().equals(name))
                return c;
        }
        return null; // line should never be hit
    }
}
