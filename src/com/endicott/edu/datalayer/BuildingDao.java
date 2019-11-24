package com.endicott.edu.datalayer;

import com.endicott.edu.models.*;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

// Created by abrocken on 7/17/2017.

public class BuildingDao {
    private  static HashMap<String, List<BuildingModel>> cache = new HashMap<>();

    private static String getFilePath(String collegeId) {
        return DaoUtils.getFilePathPrefix(collegeId) +  "building.dat";
    }
    private static Logger logger = Logger.getLogger("BuildingDao");

    public static List<BuildingModel> getBuildings(String collegeId) {
        if (cache.containsKey(collegeId))
            return cache.get(collegeId);

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
        cache.put(collegeId,buildings);
        return buildings;
    }

    public static BuildingModel[] getBuildingsArray(String collegeId) {
        List<BuildingModel> buildings = getBuildings(collegeId);
        return buildings.toArray(new BuildingModel[buildings.size()]);
    }

    public static void saveAllBuildingsJustToCache(String runId, List<BuildingModel> buildings){
        cache.put(runId, buildings);  // We need to update the cache so that next we get the up to date college.
    }

    public static void saveAllBuildingsUsingCache(String runId) {
        List<BuildingModel> buildings;

        if (!cache.containsKey(runId))
            return;

        buildings = cache.get(runId);
        saveAllBuildings(runId, buildings);
    }

    public static void saveAllBuildings(String collegeId, List<BuildingModel> buildings){
        logger.info("Saving all buildings to disk");
        try {
            File file = new File(getFilePath(collegeId));
            file.createNewFile();
            FileOutputStream fos;
            fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(buildings);
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

        cache.put(collegeId, buildings);
    }

    public static void saveNewBuilding(String collegeId, BuildingModel building) {
        logger.info("Saving new building...");
        List<BuildingModel> buildings = getBuildings(collegeId);
        building.setRunId(collegeId);
        buildings.add(building);
        saveAllBuildings(collegeId, buildings);
    }

    public static void updateSingleBuildingInCache(String collegeId, BuildingModel building) {
        List<BuildingModel> buildings = getBuildings(collegeId);
        for(int b = 0; b < buildings.size(); b++){
            if(buildings.get(b).getName().equals(building.getName())){
                buildings.set(b, building);
                saveAllBuildingsJustToCache(collegeId, buildings);
            }
        }
    }

    public static void deleteBuilding(String collegeId) {
        File file = new File(getFilePath(collegeId));
        file.delete();

        if (cache.containsKey(collegeId)){
            cache.remove(collegeId);
        }
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

        DormModel m1 = new DormModel("Dormitory", 0, "Medium");
        AdministrativeBldgModel m2 = new AdministrativeBldgModel("Admin");
        ArrayList<BuildingModel> buildings = new ArrayList<>();
        buildings.add(m1);
        buildings.add(m2);
        dao.saveAllBuildings(collegeId, buildings);

        List<BuildingModel> outMsgs = dao.getBuildings(collegeId);

        assert(outMsgs.size() == 2);
        assert(outMsgs.get(1).getCapacity() == 100);

        DiningHallModel m3 = new DiningHallModel( "Dining", 200, "Medium" );
        AcademicCenterModel m4 = new AcademicCenterModel("Academic", 200, "Medium");
        dao.saveNewBuilding(collegeId, m3);
        outMsgs = dao.getBuildings(collegeId);
        assert(outMsgs.size() == 3);

        System.out.println("First building: a(n) " + m1.getSize() + " " + m1.getKindOfBuilding() + " with a capacity of "
                            + m1.getCapacity());
        System.out.println("Second building: a(n) " + m2.getKindOfBuilding() + " with a reputation of " + m2.getHiddenQuality());
        System.out.println("Third building: a(n) " + m3.getSize() + " " + m3.getKindOfBuilding());
        System.out.println("Fourth building: a(n) " + m4.getKindOfBuilding() + " with a capacity of " + m4.getCapacity());
    }
}
