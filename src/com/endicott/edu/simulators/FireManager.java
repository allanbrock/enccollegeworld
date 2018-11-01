package com.endicott.edu.simulators;

import com.endicott.edu.datalayer.BuildingDao;
import com.endicott.edu.datalayer.FireDAO;
import com.endicott.edu.datalayer.StudentDao;
import com.endicott.edu.models.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by CJ Mustone
 */
public class FireManager {
    private InventoryManager inventoryManager = new InventoryManager();
    private boolean hasBeenUpgraded = false;
    private final String upgradeName;

    private final int upgradedRegFireProb;
    private final int regFireProb;
    private final int upgradedCatFireProb;
    private final int catFireProb;


    public FireManager() {
        upgradeName = "smoke detectors";
        upgradedRegFireProb = 8;
        regFireProb = 10;
        upgradedCatFireProb = 3;
        catFireProb = 5;
    }

    /**
     * See if a fire occurs during the time advance
     *
     * @param runId        college name
     * @param hoursAlive   number of hours college has been alive
     * @param popupManager popup manager instance
     */
    public void handleTimeChange(String runId, int hoursAlive, PopupEventManager popupManager) {
        checkForUpgrade(runId);

        possiblyCreateFire(runId, hoursAlive,hasUpgradeBeenPurchased());
        List<FireModel> fires = FireDAO.getFires(runId);
        if (fires.size() != 0) {
            generateCorrectPopUp(hasUpgradeBeenPurchased(),fires,popupManager);
        }
        fires.clear();
        FireDAO.saveAllFires(runId, fires);
    }

    public void generateCorrectPopUp(boolean isUpgraded, List <FireModel> fires, PopupEventManager popupManager){
        for (FireModel fire : fires) {
            if (isUpgraded) {
                if (fire.isCatastrophic()) {
                    popupManager.newPopupEvent("Fire in " + fire.getBuildingBurned().getName(), fire.getDescription(), "OK", "ok",  "resources/images/fire.png", "Plague Doctor");
                } else {
                    popupManager.newPopupEvent("Fire in " + fire.getBuildingBurned().getName(), fire.getDescription(), "Repair", "ok" , "resources/images/fire.png", "Plague Doctor");
                }
                return;
            }
            if (fire.isCatastrophic()) {
                popupManager.newPopupEvent("Fire in " + fire.getBuildingBurned().getName(), fire.getDescription(), "OK", "ok", "Buy Upgrade", "goToStore", "resources/images/fire.png", "Plague Doctor");
            } else {
                popupManager.newPopupEvent("Fire in " + fire.getBuildingBurned().getName(), fire.getDescription(), "Repair", "ok", "Buy Upgrade", "goToStore", "resources/images/fire.png", "Plague Doctor");
            }
        }
    }

    /**
     * Starts a fire based on bool value
     *
     * @param runId college name
     * @param hoursAlive number of hours college has been alive
     * @param isCatastrophic boolean dictating which type of fire to start
     */
    public void startFireRandomly(String runId, int hoursAlive, boolean isCatastrophic) {
        if (isCatastrophic) {
            startCatastrophicFire(runId, hoursAlive);
            return;
        }
        startNormalFire(runId, hoursAlive);
    }

    /**
     * Starts a fire with 0-10 deaths
     *
     * @param runId
     * @param hoursAlive
     */
    public void startNormalFire(String runId, int hoursAlive) {
        ArrayList<BuildingModel> buildings = (ArrayList) BuildingDao.getBuildings(runId);
        ArrayList<StudentModel> students = (ArrayList) StudentDao.getStudents(runId);
        List<FireModel> fires = FireDAO.getFires(runId);
        String victims = "";
        final boolean isCatastrophic = false;

        // if there are no buildings, there can not be a fire.
        if (buildings.size() <= 0)
            return;

        BuildingModel buildingToBurn = findBuildingToBurn(buildings);
        int numDeaths = getNumFatalities();
        int cost = getFireCost(numDeaths, buildingToBurn,runId,hasUpgradeBeenPurchased(),isCatastrophic);

        FireModel fire = new FireModel(cost, numDeaths, runId, buildingToBurn);
        if (numDeaths > 0) {
            for (int i = 0; i < numDeaths; ++i) {
                int studentToRemove = new Random().nextInt(students.size());
                victims += students.get(studentToRemove).getName().trim() + ", ";
                students.remove(studentToRemove);
            }
            fire.setDescription(victims, hasUpgradeBeenPurchased());
        } else {
            victims = "No one";
            fire.setDescription(victims,hasUpgradeBeenPurchased());
        }
        fires.add(fire);
        FireDAO.saveNewFire(runId, fire);
        StudentDao.saveAllStudents(runId, students);
        Accountant.payBill(runId, "Fire damaged cost ", fire.getCostOfFire());
        NewsManager.createNews(runId, hoursAlive, ("Fire in " + fire.getBuildingBurned().getName() + ", " + numDeaths + " died."), NewsType.COLLEGE_NEWS, NewsLevel.BAD_NEWS);
    }

    /**
     * Starts a major fire that destroys building and kills number of students inside, if there are any
     *
     * @param runId
     * @param hoursAlive
     */
    public void startCatastrophicFire(String runId, int hoursAlive) {
        ArrayList<BuildingModel> buildings = (ArrayList) BuildingDao.getBuildings(runId);
        ArrayList<StudentModel> students = (ArrayList) StudentDao.getStudents(runId);
        List<FireModel> fires = FireDAO.getFires(runId);
        BuildingManager buildingManager = new BuildingManager();
        String victims = "all";
        final boolean isCatastrophic = true;

        // if there are no buildings, there can not be a fire.
        if (buildings.size() <= 0)
            return;

        BuildingModel buildingToBurn = findBuildingToBurn(buildings);
        int numDeaths = buildingToBurn.getNumStudents();

        // If all students are in fire building, leave some alive
        if (numDeaths >= students.size()) numDeaths = students.size() / 2;

        int fireCost = getFireCost(numDeaths, buildingToBurn, runId,hasUpgradeBeenPurchased(),isCatastrophic);
        FireModel fire = new FireModel(fireCost, numDeaths, runId, buildingToBurn);
        Random rand = new Random();
        for (int i = 0; i < numDeaths && students.size() > 10; ++i) {
            students.remove(rand.nextInt(students.size()));
        }
        fire.setDescription(victims,hasUpgradeBeenPurchased());
        fires.add(fire);
        FireDAO.saveNewFire(runId, fire);
        StudentDao.saveAllStudents(runId, students);
        Accountant.payBill(runId, "Fire damage cost ", fire.getCostOfFire());
        NewsManager.createNews(runId, hoursAlive, ("Major fire at " + fire.getBuildingBurned().getName() + " was destroyed, " + numDeaths + " died."), NewsType.COLLEGE_NEWS, NewsLevel.BAD_NEWS);
        buildingManager.destroyBuildingInCaseOfDisaster(buildings,runId,buildingToBurn.getName());
        BuildingDao.saveAllBuildings(runId, buildings);
    }

    /**
     * Starts a regular fire 10% of the time, and major fire 5%
     *
     * @param runId
     * @param hoursAlive
     */
    public void possiblyCreateFire(String runId, int hoursAlive, boolean isUpgraded) {
        Random rand = new Random();
        int odds = rand.nextInt(100);
        if (isUpgraded) {
            if (odds < upgradedRegFireProb) {
                if (odds < upgradedCatFireProb) {
                    boolean isCatastrophic = true;
                    startFireRandomly(runId, hoursAlive, isCatastrophic);
                    return;
                }
                boolean isCatastrophic = false;
                startFireRandomly(runId, hoursAlive, isCatastrophic);
            }
        } else {
            if (odds < regFireProb) {
                if (odds < catFireProb) {
                    boolean isCatastrophic = true;
                    startFireRandomly(runId, hoursAlive, isCatastrophic);
                    return;
                }
                boolean isCatastrophic = false;
                startFireRandomly(runId, hoursAlive, isCatastrophic);
            }
        }
    }

    public void clearFires(String runId) {
        FireDAO.deleteFires(runId);
    }

    public int getFireCost(int victims, BuildingModel buildingToBurn,String runId, boolean isUpgraded, boolean isCatastrophic) {
        Random rand = new Random();
        int randCost = rand.nextInt(2000);
        int costOfFire;
        if (isUpgraded){
            if (victims > 1 && victims <= 10) {
                costOfFire = victims * 10000;
                return costOfFire/2;
            } else if (victims > 10 || isCatastrophic) {
                costOfFire = buildingToBurn.getTotalBuildCost();
                if (costOfFire >= Accountant.getAvailableCash(runId)){
                    return costOfFire/4;
                }
                return costOfFire;
            }
            return randCost/2;
        } else {
            if (victims > 1 && victims <= 10) {
                costOfFire = victims * 10000;
                return costOfFire;
            } else if (victims > 10 || isCatastrophic) {
                costOfFire = buildingToBurn.getTotalBuildCost();
                if (costOfFire >= Accountant.getAvailableCash(runId)) {
                    return costOfFire / 2;
                }
                return costOfFire;
            }
            return randCost;
        }
    }

    public int getNumFatalities() {
        Random rand = new Random();
        int numDeaths = rand.nextInt(10);
        if (hasUpgradeBeenPurchased()){
            return numDeaths/2;
        }
        return numDeaths;
    }

    public BuildingModel findBuildingToBurn(ArrayList<BuildingModel> buildings) {
        Random rand = new Random();
        int randomIndex = rand.nextInt(buildings.size());
        return buildings.get(randomIndex);
    }

    public void checkForUpgrade(String runId){
        if (inventoryManager.isPurchased(upgradeName,runId)){
            this.hasBeenUpgraded = true;
        }
    }

    public boolean hasUpgradeBeenPurchased(){
        return this.hasBeenUpgraded;
    }
}

