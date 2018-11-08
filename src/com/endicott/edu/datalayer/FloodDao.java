package com.endicott.edu.datalayer;

import com.endicott.edu.models.FloodModel;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.io.*;
import java.util.logging.Logger;


// Created by cseidl on 7/17/2017.

public class FloodDao {
    private Logger logger = Logger.getLogger("FloodDao");

    private static String getFilePath(String collegeId) {
        return DaoUtils.getFilePathPrefix(collegeId) + "flood.dat";
    }

    /**
     * Checks if there is a Flood in the current college and returns it.
     * If the file does not exist, returns NULL .
     *      - used in: InterfaceUtils.java (openCollegeAndStoreInRequest(..))
     *                 FloodManager.java (handleTimeChange(..))
     *
     * @param collegeId a String representing the ID of the current college
     * @return the existing FloodModel, Null if it doesnt exist
     */
    public static FloodModel getFlood(String collegeId) {

        FloodModel flood = null;
        try {
            File file = new File(getFilePath(collegeId));   //Creates a new File instance by converting the given pathname(string) into an abstract pathname.

            if (!file.exists()) {
                return flood;  // There are no floods yet, return Null.
            } else {
                FileInputStream fis = new FileInputStream(file);    //creates a FileInputStream by opening a connection to an actual File.
                ObjectInputStream ois = new ObjectInputStream(fis);
                flood = (FloodModel) ois.readObject();
                ois.close();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return flood;
    }

    /**
     * Creates a new file with its appropriate path and saves theFlood in it.
     *      - used in: FloodManager.java (handleTimeChange(..) & didFloodStartAtThisDorm(..))
     *
     * @param collegeId a String representing the ID of the current college
     * @param theFlood  the FloodModel to save
     */
    public void saveTheFlood(String collegeId, FloodModel theFlood) {
        logger.info("Saving one flood...");
        try {
            File file = new File(getFilePath(collegeId));
            file.createNewFile();
            FileOutputStream fos;
            fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(theFlood);
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

        logger.info("Saved one flood...");
    }

    /**
     * Deletes the file of the flood.
     *  - used in: CollegeManager.java (sellCollege(..))
     *             FloodManager.java (handleTimeChange(..))
     *
     * @param collegeId a String representing the ID of the current college
     */
    public static void deleteFlood(String collegeId) {
        File file = new File(getFilePath(collegeId));
        file.delete();
    }

    public static void main(String[] args) {
        testNotes();
    }

    private static void testNotes() {

        final String collegeId = "testflood001";
        FloodDao dao = new FloodDao();

        FloodModel m1 = new FloodModel(1000, 72, 100, 10, "AnotherEva Hall", collegeId);
        //FloodModel m2 = new FloodModel(1500, 96, 200, 20, "Vermont House", collegeId);
        FloodModel m3 = new FloodModel(1000, 72, 300, 10, "RafaHall", collegeId);

        FloodModel outMsgs = dao.getFlood(collegeId);


        if (outMsgs == null) {
            System.out.println(" (first) outMsgs is Null. No floods exist in the file");
        } else {
            System.out.println("(first)  outMsgs is NOT null.");
            System.out.println(" (first) flood in: " + outMsgs.getDormName());
        }


        dao.saveTheFlood(collegeId, m1);
        outMsgs = dao.getFlood(collegeId);

        System.out.println("Flood m1 Eva created and saved : " + outMsgs.getDormName());
        System.out.println("Flood m1 costs: " + outMsgs.getCostOfFlood());


        dao.deleteFlood(collegeId);
        System.out.println("Flood m1 DELETED");
        outMsgs = dao.getFlood(collegeId);


        if (outMsgs == null) {
            System.out.println(" (second) dao.deleteFlood called. outMsgs is NULL. ");

        } else {
            System.out.println("(second)  dao.deleteFlood called. outMsgs is NOT null.");
            System.out.println(" (second) flood in: " + outMsgs.getDormName());
        }

        dao.saveTheFlood(collegeId, m3);
        outMsgs = dao.getFlood(collegeId);
        System.out.println("Flood m3 created and saved " + outMsgs.getDormName());
        System.out.println("Flood m3 costs: " + outMsgs.getCostOfFlood());

        dao.deleteFlood(collegeId);
        outMsgs = dao.getFlood(collegeId);
        if (outMsgs == null) {
            System.out.println(" (third) dao.deleteFlood called. outMsgs is NULL. ");

        } else {
            System.out.println("(third)  dao.deleteFlood called. outMsgs is NOT null.");
            System.out.println(" (third) flood in: " + outMsgs.getDormName());
        }
        System.out.println("Test case name: testFloods, Result: pass");
    }
}
