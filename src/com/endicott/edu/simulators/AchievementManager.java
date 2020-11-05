package com.endicott.edu.simulators;

import com.endicott.edu.datalayer.AchievementDao;
import com.endicott.edu.models.AchievementModel;

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
        achDao.saveNewAchievement(collegeId, new AchievementModel(name, description, type, reward, level, money, happiness));
    }

    public static void checkAchievementStatus(String collegeId) {
        AchievementDao achDao = new AchievementDao();
        ArrayList<AchievementModel> achievements = new ArrayList<>();
    }

    public static void unlockAchievement(String collegeId, AchievementModel achievement) {
        achievement.setLock(false);
        Accountant.receiveIncome(collegeId, achievement.getDescription(), achievement.getCashReward());
//        popupManager.newPopupEvent(collegeId, achievement.getName(), achievement.getDescription(), "Ok", "ok", "resources/images/rioticon.png", "icon");
    }
}