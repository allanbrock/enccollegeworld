package com.endicott.edu.datalayer;

import com.endicott.edu.models.RiotModel;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.io.*;
import java.util.logging.Logger;



public class RiotDao {
    private Logger logger = Logger.getLogger("RiotDao");

    private static String getFilePath(String collegeId) {
        return DaoUtils.getFilePathPrefix(collegeId) + "Riot.dat";
    }

    public static RiotModel getRiot(String collegeId) {

        RiotModel Riot = null;
        try {
            File file = new File(getFilePath(collegeId));

            if (!file.exists()) {
                return Riot;
            } else {
                FileInputStream fis = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(fis);
                Riot = (RiotModel) ois.readObject();
                ois.close();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return Riot;
    }


    public void saveTheRiot(String collegeId, RiotModel theRiot) {
        logger.info("Saving one Riot...");
        try {
            File file = new File(getFilePath(collegeId));
            file.createNewFile();
            FileOutputStream fos;
            fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(theRiot);
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

        logger.info("Saved one Riot...");
    }

    /**
     * Deletes the file of the Riot.
     *  - used in: CollegeManager.java (sellCollege(..))
     *             RiotManager.java (handleTimeChange(..))
     *
     * @param collegeId a String representing the ID of the current college
     */
    public static void deleteRiot(String collegeId) {
        File file = new File(getFilePath(collegeId));
        file.delete();
    }

    public static void main(String[] args) {

    }


}

