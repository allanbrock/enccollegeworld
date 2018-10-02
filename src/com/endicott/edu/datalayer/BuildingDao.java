package com.endicott.edu.datalayer;

import com.endicott.edu.models.BuildingModel;
import com.endicott.edu.models.DormModel;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


// Created by abrocken on 7/17/2017.

public class BuildingDao {
    private static String getFilePath(String collegeId) {
        return DaoUtils.getFilePathPrefix(collegeId) +  "building.dat";
    }
    private Logger logger = Logger.getLogger("BuildingDao");

    public static List<BuildingModel> getBuildings(String collegeId) {
        ArrayList<BuildingModel> buildings = new ArrayList<>();
        BuildingModel buildingModel = null;
        try {
            File file = new File(getFilePath(collegeId));

            if (!file.exists()) {
                return buildings;  // There are no buildings yet.
            }
            else{
                FileInputStream fis = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(fis);
                buildings = (ArrayList<BuildingModel>) ois.readObject();
                ois.close();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return buildings;
    }

    public static BuildingModel[] getBuildingsArray(String collegeId) {
        List<BuildingModel> buildings = getBuildings(collegeId);
        return buildings.toArray(new BuildingModel[buildings.size()]);
    }

    public void saveAllBuildings(String collegeId, List<BuildingModel> notes){
        logger.info("Saving all buildings...");
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

        logger.info("Saved buildings...");
    }

    public void saveNewBuilding(String collegeId, BuildingModel building) {
        logger.info("Saving new building...");
        List<BuildingModel> buildings = getBuildings(collegeId);
        building.setRunId(collegeId);
        buildings.add(building);
        saveAllBuildings(collegeId, buildings);

    }

    public static void deleteBuilding(String collegeId) {

        File file = new File(getFilePath(collegeId));
        file.delete();
    }

    public static void main(String[] args) {
        testNotes();
    }


    private static void testNotes() {
      /*  final String collegeId = "testbuilding001";
        BuildingDao dao = new BuildingDao();

        BuildingModel m1 = new BuildingModel(100, 10, "Hampshire Hall", 50,
                "none", 3, "none", 50);
        BuildingModel m2 = new BuildingModel(200, 10, "Vermont House", 70,
                "none", 7, "none", 100);
        ArrayList<BuildingModel> buildings = new ArrayList<>();
        buildings.add(m1);
        buildings.add(m2);
        dao.saveAllBuildings(collegeId, buildings);

        List<BuildingModel> outMsgs = dao.getBuildings(collegeId);

        assert(outMsgs.size() == 2);
        assert(outMsgs.get(1).getCapacity() == 100);

        BuildingModel m3 = new BuildingModel(300, 10, "Maine Manor", 250,
                "flood", 8, "none",100);
        dao.saveNewBuilding(collegeId, m3);
        outMsgs = dao.getBuildings(collegeId);
        assert(outMsgs.size() == 3);

        System.out.println("Test case name: testDorms, Result: pass");*/

      //my test
        final String collegeId = "testbuilding001";
        BuildingDao dao = new BuildingDao();

        DormModel m1 = new DormModel("Small", 200, 95, 100, "Dorm" );
        BuildingModel m2 = new BuildingModel(100, "Administrative Building");
        ArrayList<BuildingModel> buildings = new ArrayList<>();
        buildings.add(m1);
        buildings.add(m2);
        dao.saveAllBuildings(collegeId, buildings);

        List<BuildingModel> outMsgs = dao.getBuildings(collegeId);

        assert(outMsgs.size() == 2);
        assert(outMsgs.get(1).getCapacity() == 100);

        BuildingModel m3 = new BuildingModel("Health Center");
        dao.saveNewBuilding(collegeId, m3);
        outMsgs = dao.getBuildings(collegeId);
        assert(outMsgs.size() == 3);

        System.out.println("First building: a(n) " + m1.getSize() + " " + m1.getKindOfBuilding() + " with a capacity of "
                            + m1.getCapacity());
        System.out.println("Second building: a(n) " + m2.getKindOfBuilding() + " with a reputation of " + m2.getReputation());
        System.out.println("Third building: a(n) " + m3.getKindOfBuilding());
    }
}
