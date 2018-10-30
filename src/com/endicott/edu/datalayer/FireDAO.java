package com.endicott.edu.datalayer;

import com.endicott.edu.models.FireModel;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class FireDAO {
    private static String getFilePath(String collegeId) {
        return DaoUtils.getFilePathPrefix(collegeId) +  "fire.dat";
    }
    private static Logger logger = Logger.getLogger("FireDao");

    public static List<FireModel> getFires(String collegeId) {
        ArrayList<FireModel> fires = new ArrayList<>();
        FireModel fireModel = null;
        try {
            File file = new File(getFilePath(collegeId));

            if (!file.exists()) {
                return fires;  // There are no fires yet.
            }
            else{
                FileInputStream fis = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(fis);
                fires = (ArrayList<FireModel>) ois.readObject();
                ois.close();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return fires;
    }

    public static FireModel[] getFiresArray(String collegeId) {
        List<FireModel> fires = getFires(collegeId);
        return fires.toArray(new FireModel[fires.size()]);
    }

    public static void saveAllFires(String collegeId, List<FireModel> notes){
        logger.info("Saving fire...");
        try {
            File file = new File(getFilePath(collegeId));
            file.createNewFile();
            FileOutputStream fos;
            fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(notes);
            oos.close();
        } catch (FileNotFoundException e) {
            logger.info("Got file not found when attempting to create: " + getFilePath(collegeId));
            e.printStackTrace();
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        } catch (IOException e) {
            logger.info("Got io exceptionfound when attempting to create: " + getFilePath(collegeId));
            e.printStackTrace();
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }

        logger.info("Saved fire...");
    }

    public static void saveNewFire(String collegeId, FireModel fire) {
        logger.info("Saving new fire...");
        List<FireModel> fires = getFires(collegeId);
        fire.setRunId(collegeId);
        fires.add(fire);
        saveAllFires(collegeId, fires);
    }

    public static void deleteFires(String collegeId) {
        File file = new File(getFilePath(collegeId));
        file.delete();
    }
}
