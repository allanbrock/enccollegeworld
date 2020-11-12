package com.endicott.edu.simulators;

import com.endicott.edu.datalayer.AchievementDao;
import com.endicott.edu.datalayer.CollegeDao;
import com.endicott.edu.models.AchievementModel;
import com.endicott.edu.models.CollegeModel;

import java.util.ArrayList;

/**
 * Author: Justen Koo
 * Date: November 1, 2020
 * Class AchievementManager maintains a list of all possible Achievement objects from a text file
 * To add or delete specific Achievements, go to
 */
public class AchievementManager {

    private static ArrayList<AchievementModel> achievementsList = new ArrayList();

    public AchievementManager() {}

    public static void createAchievement(String collegeId, String name, String description, String type, int reward, int level, int money, int happiness) {
        AchievementDao achDao = new AchievementDao();
        AchievementModel temp = new AchievementModel(name, description, type, reward, level, money, happiness);
        achDao.saveNewAchievement(collegeId, temp);
        achievementsList.add(temp);
    }

    public static void checkAchievementStatus(String collegeId, PopupEventManager popupManager) {
        AchievementModel temp = new AchievementModel("", "", "", 0, 0, 0, 0);
        // using collegeId, get the college's current money, level and happiness
        int money = Accountant.getAvailableCash(collegeId);
        int level = GateManager.getObjectives(collegeId).currentLevel;
        CollegeModel college = new CollegeModel();
        int happiness = college.getStudentBodyHappiness();
        // iterate through list of achievements and compare with college stats
        for(int i=0; i<achievementsList.size(); i++) {
            temp = achievementsList.get(i);
            if(temp.getType() == "level") {
                if(temp.getLevelReq("level") == level)
                    unlockAchievement(collegeId, temp, popupManager);
            } else if (temp.getType() == "money") {
                if(temp.getLevelReq("money") >= money)
                    unlockAchievement(collegeId, temp, popupManager);
            } else if (temp.getType() == "happiness") {
                if(temp.getLevelReq("happiness") >= level)
                    unlockAchievement(collegeId, temp, popupManager);
            }
        }
    }

    public static void unlockAchievement(String collegeId, AchievementModel achievement, PopupEventManager popupManager) {
        achievement.setLock(false);
        Accountant.receiveIncome(collegeId, achievement.getDescription(), achievement.getCashReward());
        popupManager.newPopupEvent(collegeId, "Achievement Get!", "Congrats you've unlocked a new achievement:" + achievement.getName() + "!", "Okay", "okGate", "resources/images/star.png", "icon");
    }

    public static void handleTimeChange(String collegeId, int hoursAlive, PopupEventManager popupManager) {
        CollegeModel college = CollegeDao.getCollege(collegeId);
        checkAchievementStatus(college.getRunId(), popupManager);
        CollegeDao.saveCollege(college);
    }
}