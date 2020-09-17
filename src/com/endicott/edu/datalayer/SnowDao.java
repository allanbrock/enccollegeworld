package com.endicott.edu.datalayer;

import com.endicott.edu.models.BuildingModel;
import com.endicott.edu.models.BuildingType;
import com.endicott.edu.models.SnowModel;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/*
* Created by Eva Rubio 11/05/2018
*/
public class SnowDao {
    private Logger logger = Logger.getLogger("SnowDao");

    private static String getFilePath(String collegeId) {
        return DaoUtils.getFilePathPrefix(collegeId) + "snow.dat";
    }
    /**
     * Checks if there is a Snow storm in the current college and returns it.
     * If the file does not exist, returns NULL .
     *      - used in: InterfaceUtils.java (openCollegeAndStoreInRequest(..))
     *                 SnowManager.java (handleTimeChange(..))
     *
     * @param collegeId a String representing the ID of the current college
     * @return the existing SnowModel, Null if it doesnt exist
     */
    public static SnowModel getSnowStorm(String collegeId) {

        SnowModel snowStorm = null;
        try {
            File file = new File(getFilePath(collegeId));   //Creates a new File instance by converting the given pathname(string) into an abstract pathname.

            if (!file.exists()) {
                return snowStorm;  // There are no Snow Storms yet, return Null.
            } else {
                FileInputStream fis = new FileInputStream(file);    //creates a FileInputStream by opening a connection to an actual File.
                ObjectInputStream ois = new ObjectInputStream(fis);
                snowStorm = (SnowModel) ois.readObject();
                ois.close();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return snowStorm;
    }
    /**
     * Creates a new file with its appropriate path and saves theSnowStorm in it.
     *      - used in: SnowManager.java (handleTimeChange(..) & didSnowStormStartAtThisDorm(..))
     *
     * @param collegeId a String representing the ID of the current college
     * @param theSnowStorm  the SnowModel to save
     */
    public void saveSnowStorm(String collegeId, SnowModel theSnowStorm) {
        logger.info("Saving a snow storm...");
        try {
            File file = new File(getFilePath(collegeId));
            file.createNewFile();
            FileOutputStream fos;
            fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(theSnowStorm);
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

        logger.info("Saved a snow storm...");
    }
    /**
     * Deletes the file of the snow storm.
     *  - used in: CollegeManager.java (sellCollege(..))
     *             SnowManager.java (handleTimeChange(..))
     *
     * @param collegeId a String representing the ID of the current college
     */
    public static void deleteSnowStorm(String collegeId) {
        File file = new File(getFilePath(collegeId));
        file.delete();
    }
    public static void main(String[] args) {
        testNotes();
    }

    /**
     * For DormModel, DiningHallModel, and AcademicCenterModel:
     *         new BuildingModel(String name, int numStudents, String kindOfBuilding, String size)
     * */
    private static void testNotes() {
        final String collegeId = "testsnowstorm001";
        SnowDao dao = new SnowDao();
        List<BuildingModel> edifList = new ArrayList<>();

        BuildingModel b1 = new BuildingModel("Eva Hall", 50, BuildingType.dorm().getType(),"Medium");
        BuildingModel b2 = new BuildingModel("Rafa Dorm", 40, BuildingType.dorm().getType(),"Small");
        edifList.add(b1);
        edifList.add(b2);

        SnowModel lowSnow = new SnowModel(collegeId,b1,1,100, 10, 10, b1.getTimeSinceLastRepair());

        SnowModel intenseSnowStorm = new SnowModel(collegeId, edifList, 3, 500, 15, 15, b1.getTimeSinceLastRepair());


        SnowModel outMsgs = dao.getSnowStorm(collegeId);


        if (outMsgs == null) {
            System.out.println(" (first) outMsgs is Null. No snow storm exist in the file");
        } else {
            System.out.println(" (first)  outMsgs is NOT null.");
            System.out.println(" (first) Storm intensity of: " + outMsgs.getSnowIntensity());
            System.out.println(" (first) Storm intensity of: " + outMsgs.getOneBuildingSnowed().getName());
        }
        dao.saveSnowStorm(collegeId,lowSnow);
        outMsgs = dao.getSnowStorm(collegeId);

        System.out.println(" LOW Storm created and saved. One dorm : " + outMsgs.getOneBuildingSnowed().getName());
        System.out.println("LOW Storm costs: " + outMsgs.getCostOfPlowing());


        dao.deleteSnowStorm(collegeId);
        System.out.println("LOW Storm DELETED");
        outMsgs = dao.getSnowStorm(collegeId);


        if (outMsgs == null) {
            System.out.println(" (second) dao.deleteSnowStorm called. outMsgs is NULL. ");

        } else {
            System.out.println("(second)  dao.deleteSnowStorm called. outMsgs is NOT null.");
            System.out.println(" (second) storm of intensity: " + outMsgs.getSnowIntensity());
        }

        dao.saveSnowStorm(collegeId, intenseSnowStorm);
        outMsgs = dao.getSnowStorm(collegeId);
        System.out.println("INTENSE Storm created and saved. Intensity: " + outMsgs.getSnowIntensity());
        System.out.println("INTENSE Storm costs: " + outMsgs.getCostOfPlowing());
        System.out.println("INTENSE Storm. B1: " + outMsgs.getBuildingsAffectedList().get(0).getName());
        System.out.println("INTENSE Storm. -B2-: " + outMsgs.getBuildingsAffectedList().get(1).getName());

        dao.deleteSnowStorm(collegeId);
        outMsgs = dao.getSnowStorm(collegeId);
        if (outMsgs == null) {
            System.out.println(" (third) dao.deleteSnowStorm called. outMsgs is NULL. ");

        } else {
            System.out.println(" (third)  dao.deleteSnowStorm called. outMsgs is NOT null.");
            System.out.println(" (second) storm of intensity: " + outMsgs.getSnowIntensity());
        }
        System.out.println("Test case name: testSNOWSTORMS, Result: pass");
    }
}
