package com.endicott.edu.simulators;

import com.endicott.edu.datalayer.SportsDao;
import com.endicott.edu.models.Coach;
import com.endicott.edu.models.SportModel;

import java.util.ArrayList;
import java.util.List;

public class CoachManager {
    private static ArrayList<Coach> collegeCoaches;

    public static void addToCollegeCoaches(Coach coach){
        if(collegeCoaches == null)
            collegeCoaches = new ArrayList<>();
        collegeCoaches.add(coach);
    }

    public static ArrayList<Coach> getCollegeCoaches(){ return collegeCoaches; }

    public static void removeCoach(String collegeId, Coach coach){
        SportsDao dao = new SportsDao();
        List<SportModel> newSports = SportsDao.getSports(collegeId);
        collegeCoaches.remove(coach);
        for(SportModel sport : newSports){
            if(sport.getCoachName().equals(coach.getFullName()))
                sport.setCoachName("noCoach");
        }
        dao.saveAllSports(collegeId, newSports);
    }
    public static void removeAllCollegeCoaches(String collegeId){
        collegeCoaches.clear();
    }

    public static Coach getCoachByName(String name){
        if (collegeCoaches == null)
            return null;

        for(Coach c : collegeCoaches){
            if(c.getFullName().equals(name))
                return c;
        }
        return null; // line should never be hit
    }
}
