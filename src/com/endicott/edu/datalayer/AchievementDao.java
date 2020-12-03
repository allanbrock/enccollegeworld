package com.endicott.edu.datalayer;

import com.endicott.edu.models.AchievementModel;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class AchievementDao {
    private static String getFilePath(String collegeId) {
        return DaoUtils.getFilePathPrefix(collegeId) +  "achievement.dat";
    }
    private Logger logger = Logger.getLogger("AchievementDao");

    public static List<AchievementModel> getAchievements(String collegeId) {
        ArrayList<AchievementModel> achievements = new ArrayList<>();
        try {
            File file = new File(getFilePath(collegeId));

            if (!file.exists()) {
                return achievements;
            }
            else{
                FileInputStream fis = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(fis);
                achievements = (ArrayList<AchievementModel>) ois.readObject();
                ois.close();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return achievements;
    }

    public static AchievementModel[] getAchievementArray(String collegeId) {
        List<AchievementModel> achievements = getAchievements(collegeId);
        return achievements.toArray(new AchievementModel[achievements.size()]);
    }

    public void saveNewAchievement(String collegeId, AchievementModel achievement) {
        logger.info("Saving new achievement...");
        List<AchievementModel> achievements = getAchievements(collegeId);
        achievement.setRunId(collegeId);
        achievements.add(achievement);
        saveAllAchievements(collegeId, achievements);
    }

    public static void saveAllAchievements(String collegeId, List<AchievementModel> notes){

        try {
            File file = new File(getFilePath(collegeId));
            file.createNewFile();
            FileOutputStream fos;
            fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(notes);
            oos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        } catch (IOException e) {
            e.printStackTrace();
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }

    }

    public static void delete(String collegeId) {
        File file = new File(getFilePath(collegeId));
        file.delete();
    }

    public static void main(String[] args) {
        testNotes();
    }

    private static void testNotes() {}
}
