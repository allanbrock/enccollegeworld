package com.endicott.edu.simulators;

import com.endicott.edu.datalayer.BuildingDao;
import com.endicott.edu.datalayer.DormitoryDao;
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
    FireDAO fireDao = new FireDAO();
    BuildingDao buildingDao = new BuildingDao();
    DormitoryDao dormDao = new DormitoryDao();
    StudentDao studentDao = new StudentDao();

    /**
     * See if a fire occurs during the time advance
     *
     * @param runId college name
     * @param hoursAlive number of hours college has been alive
     * @param popupManager popup manager instance
     */
    public void handleTimeChange(String runId, int hoursAlive, PopupEventManager popupManager) {
        possiblyCreateFire(runId, hoursAlive);
        List<FireModel> fires = FireDAO.getFires(runId);
        if (fires.size() != 0) {
            for (FireModel fire: fires) {
                popupManager.newPopupEvent("Fire in " + fire.getBuildingBurned().getName(), fire.getDescription(), "Repair", "ok", "Buy Upgrade", "goToStore", "resources/images/fire.png", "Plague Doctor");
            }
        }
        fires.clear();
        this.fireDao.saveAllFires(runId, fires);
    }

    /**
     * Starts a fire based on bool value
     *
     * @param runId
     * @param hoursAlive
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
        ArrayList <BuildingModel> buildings = (ArrayList) BuildingDao.getBuildings(runId);
        ArrayList <DormitoryModel> dorms = (ArrayList) DormitoryDao.getDorms(runId);
        ArrayList<StudentModel> students = (ArrayList) StudentDao.getStudents(runId);
        List<FireModel> fires = FireDAO.getFires(runId);
        String victims = "";

        // if there are no buildings, there can not be a fire.
        if (buildings.size() <= 0)
            return;

        BuildingModel buildingToBurn = findBuildingToBurn(buildings, dorms);
        int numDeaths = getNumFatalities();
        int cost = getFireCost(numDeaths, buildingToBurn);

        FireModel fire = new FireModel(cost, numDeaths, runId, buildingToBurn);
        if (numDeaths > 0) {
            for (int i = 0; i < numDeaths; ++i) {
                int studentIndex = new Random().nextInt(students.size()-1);
                victims  += students.get(studentIndex).getName().trim() + ", ";
                students.remove(studentIndex);
            }
            fire.setDescription(victims);
        } else {
            victims = "No one";
            fire.setDescription(victims);
        }
        fires.add(fire);
        fireDao.saveNewFire(runId, fire);
        studentDao.saveAllStudents(runId, students);
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
        ArrayList <BuildingModel> buildings = (ArrayList)BuildingDao.getBuildings(runId);
        ArrayList <DormitoryModel> dorms = (ArrayList)DormitoryDao.getDorms(runId);
        ArrayList<StudentModel> students = (ArrayList)StudentDao.getStudents(runId);
        List<FireModel> fires = FireDAO.getFires(runId);
        String victims = "all";

        // if there are no buildings, there can not be a fire.
        if (buildings.size() <= 0)
            return;

        BuildingModel buildingToBurn = findBuildingToBurn(buildings, dorms);
        int numDeaths = buildingToBurn.getNumStudents();

        // If all students are in fire building, leave some alive
        if (numDeaths == students.size()) numDeaths = students.size()/2;

        int fireCost = getFireCost(numDeaths, buildingToBurn);
        FireModel fire = new FireModel(fireCost, numDeaths, runId, buildingToBurn);
        Random rand = new Random();
        for (int i = 0; i < numDeaths && students.size() > 10; ++i) {
            students.remove(rand.nextInt(students.size()));
        }
        fire.setDescription(victims);
        fires.add(fire);
        fireDao.saveNewFire(runId, fire);
        studentDao.saveAllStudents(runId, students);
        Accountant.payBill(runId, "Fire damage cost ", fire.getCostOfFire());
        NewsManager.createNews(runId, hoursAlive, ("Major fire at " + fire.getBuildingBurned().getName() + " was destroyed, " + numDeaths + " died."), NewsType.COLLEGE_NEWS, NewsLevel.BAD_NEWS);
        buildings.remove(buildingToBurn);
        buildingDao.saveAllBuildings(runId, buildings);

    }

    /**
     * Starts a regular fire 10% of the time, and major fire 5%
     *
     * @param runId
     * @param hoursAlive
     */
    public void possiblyCreateFire(String runId, int hoursAlive) {
        Random rand = new Random();
        int odds = rand.nextInt(100);
        if (odds < 10) {
            if (odds < 5){
                boolean isCatastrophic = true;
                startFireRandomly(runId, hoursAlive, isCatastrophic);
                return;
            }
            boolean isCatastrophic = false;
            startFireRandomly(runId, hoursAlive, isCatastrophic);
        }
    }

    public void clearFires(String runId) {
        FireDAO.deleteFires(runId);
    }

    public int getFireCost(int victims, BuildingModel buildingToBurn) {
        Random rand = new Random();
        int randCost = rand.nextInt(1000);
        if (victims > 1 && victims <= 10) {
            int costOfFire = victims * randCost;
            return costOfFire;
        }else if (victims > 10) {
            int costOfFire = buildingToBurn.getTotalBuildCost();
            return costOfFire;
        }
        int costOfFire = randCost;
        return costOfFire;
    }

    public int getNumFatalities() {
        Random rand = new Random();
        int numDeaths = rand.nextInt(10);
        return numDeaths;
    }

    public BuildingModel findBuildingToBurn(ArrayList<BuildingModel> buildings, ArrayList<DormitoryModel> dorms) {
        ArrayList<BuildingModel> allBuildings = buildings;
        Random rand = new Random();
        int randomIndex = rand.nextInt(buildings.size());
        for (DormitoryModel d : dorms) {
            allBuildings.add(d);
        }
        return allBuildings.get(randomIndex);
    }
}