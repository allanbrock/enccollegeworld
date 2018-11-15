package com.endicott.edu.simulators;

import com.endicott.edu.datalayer.BuildingDao;
import com.endicott.edu.datalayer.FacultyDao;
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

    private int upgradedRegFireProb;
    private int regFireProb;
    private int upgradedCatFireProb;
    private int catFireProb;


    public FireManager() {
        upgradeName = "Smoke Detectors";
        //Old fire values, seemed to high but still want to discuss with CJ  before removing completely
//        upgradedRegFireProb = 8;
//        regFireProb = 10;
//        upgradedCatFireProb = 3;
//        catFireProb = 5;
        upgradedRegFireProb = 2;
        regFireProb = 4;
        upgradedCatFireProb = 1;
        catFireProb = 2;
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

        createFireByOdds(runId, hoursAlive,hasUpgradeBeenPurchased());
        List<FireModel> fires = FireDAO.getFires(runId);
        if (fires.size() != 0) {
            generateCorrectPopUp(hasUpgradeBeenPurchased(),fires,popupManager);
        }
        fires.clear();
        FireDAO.saveAllFires(runId, fires);
    }

    private void generateCorrectPopUp(boolean isUpgraded, List<FireModel> fires, PopupEventManager popupManager){
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
    private void startFireRandomly(String runId, int hoursAlive, boolean isCatastrophic) {
        if (isCatastrophic) {
            startCatastrophicFire(runId, hoursAlive);
            return;
        }
        startNormalFire(runId, hoursAlive);
    }

    /**
     * Starts a fire with 0-10 deaths
     *  @param runId
     * @param hoursAlive
     */
    private void startNormalFire(String runId, int hoursAlive) {
        ArrayList<BuildingModel> buildings = (ArrayList<BuildingModel>) BuildingDao.getBuildings(runId);
        ArrayList<StudentModel> students = (ArrayList<StudentModel>) StudentDao.getStudents(runId);
        ArrayList<FacultyModel> faculty = (ArrayList<FacultyModel>) FacultyDao.getFaculty(runId);
        List<FireModel> fires = FireDAO.getFires(runId);
        BuildingManager buildingManager = new BuildingManager();
        String victims = "";
        final boolean isCatastrophic = false;

        // if there are no buildings, there can not be a fire.
        if (buildings.size() <= 0)
            return;

        BuildingModel buildingToBurn = findBuildingToBurn(buildings);
        int numStudentDeaths = getNumStudentFatalities();
        int numFacultyDeaths = getNumFacultyFatalities(numStudentDeaths);
        int numDeaths = numFacultyDeaths + numStudentDeaths;
        int cost = getFireCost(numDeaths, buildingToBurn,runId,hasUpgradeBeenPurchased(),isCatastrophic);

        FireModel fire = new FireModel(cost, numStudentDeaths,numFacultyDeaths, runId, buildingToBurn);
        removeFireVictims(numStudentDeaths,numFacultyDeaths,students,faculty,victims,fire);
        buildingManager.acceleratedDecayAfterDisaster(runId,buildingToBurn.getName());
        fires.add(fire);
        FireDAO.saveNewFire(runId, fire);
        StudentDao.saveAllStudents(runId, students);
        FacultyDao.saveAllFaculty(runId,faculty);
        Accountant.payBill(runId, "Fire damaged cost ", fire.getCostOfFire());
        NewsManager.createNews(runId, hoursAlive, ("Fire in " + fire.getBuildingBurned().getName() + ", " + numDeaths + " died."), NewsType.COLLEGE_NEWS, NewsLevel.BAD_NEWS);
    }

    /**
     * Starts a major fire that destroys building and kills number of students inside, if there are any
     *
     * @param runId
     * @param hoursAlive
     */
    private void startCatastrophicFire(String runId, int hoursAlive) {
        ArrayList<BuildingModel> buildings = (ArrayList<BuildingModel>) BuildingDao.getBuildings(runId);
        ArrayList<StudentModel> students = (ArrayList<StudentModel>) StudentDao.getStudents(runId);
        ArrayList<FacultyModel> faculty = (ArrayList<FacultyModel>) FacultyDao.getFaculty(runId);
        List<FireModel> fires = FireDAO.getFires(runId);
        BuildingManager buildingManager = new BuildingManager();
        String victims = "all";
        final boolean isCatastrophic = true;

        /*/ if there are no buildings, there can not be a fire.
        if (buildings.size() <= 1)
            return;
        */

        BuildingModel buildingToBurn = findBuildingToBurn(buildings);

        // if findbuildingToBurn only found the admin building the method returned this to be null
        if (buildingToBurn == null) return;

        int numStudentDeaths = buildingToBurn.getNumStudents();

        // If all students are in fire building, leave some alive
        if (numStudentDeaths >= students.size()) numStudentDeaths = numStudentDeaths / 2;

        int numFacultyDeaths = getNumFacultyFatalities(numStudentDeaths);
        int numDeaths = numFacultyDeaths + numStudentDeaths;

        int fireCost = getFireCost(numDeaths, buildingToBurn, runId,hasUpgradeBeenPurchased(),isCatastrophic);
        FireModel fire = new FireModel(fireCost, numStudentDeaths, numFacultyDeaths, runId, buildingToBurn);
        removeFireVictims(numStudentDeaths,numFacultyDeaths,students,faculty,victims,fire);
        fire.setDescription(victims, hasUpgradeBeenPurchased());
        fires.add(fire);
        FireDAO.saveNewFire(runId, fire);
        StudentDao.saveAllStudents(runId, students);
        FacultyDao.saveAllFaculty(runId,faculty);
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
    private void createFireByOdds(String runId, int hoursAlive, boolean isUpgraded) {
        Random rand = new Random();
        int odds = rand.nextInt(100);
        if (isUpgraded) {
            possiblyCreateFire(odds, upgradedRegFireProb, upgradedCatFireProb, runId, hoursAlive);
        } else {
            possiblyCreateFire(odds, regFireProb, catFireProb, runId, hoursAlive);
        }
    }

    /**
     * Starts either type of fire based on the type of probability it receives
     *
     * @param odds random number generated
     * @param regProb probability of a regular fire starting
     * @param catProb probability of a catastrophic fire happening
     * @param runId
     * @param hoursAlive
     */
    private void possiblyCreateFire(int odds, int regProb, int catProb, String runId, int hoursAlive){
        if (odds < regProb || CollegeManager.isMode(runId, CollegeMode.DEMO_FIRE)) {
            if (odds < catProb || CollegeManager.isMode(runId, CollegeMode.DEMO_FIRE)) {
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

    private int getFireCost(int victims, BuildingModel buildingToBurn, String runId, boolean isUpgraded, boolean isCatastrophic) {
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


    private int getNumStudentFatalities() {
        Random rand = new Random();
        int numDeaths = rand.nextInt(10);
        if (hasUpgradeBeenPurchased()){
            return numDeaths/2;
        }
        return numDeaths;
    }

    private int getNumFacultyFatalities(int studentVictims){
        if (studentVictims == 0) return studentVictims;
        return studentVictims/4;
    }

    private void removeFireVictims(int numStudentDeaths, int numFacultyDeaths, ArrayList<StudentModel> students,
                                   ArrayList<FacultyModel> faculty, String victims, FireModel fire){
        if (numFacultyDeaths == faculty.size()){
            numFacultyDeaths = numStudentDeaths/2;
        } else if (numStudentDeaths == students.size()){
            numStudentDeaths = numStudentDeaths/2;
        }

        if (fire.isCatastrophic()){
            for (int i = 0; i < numStudentDeaths; ++i) {
                int studentToRemove = new Random().nextInt(students.size());
                students.remove(studentToRemove);
            }
            for (int j = 0; j <numFacultyDeaths; j++){
                int facultyToRemove = new Random().nextInt(faculty.size());
                faculty.remove(facultyToRemove);
            }
            return;
        }

        if (numStudentDeaths > 0 && !fire.isCatastrophic()) {
            for (int i = 0; i < numStudentDeaths && (students.size() > 0); ++i) {
                int studentToRemove = new Random().nextInt(students.size());
                victims += students.get(studentToRemove).getName().trim() + ", ";
                students.remove(studentToRemove);
            }
            for (int j = 0; j <numFacultyDeaths && (faculty.size() > 0); j++){
                int facultyToRemove = new Random().nextInt(faculty.size());
                victims += faculty.get(facultyToRemove).getFacultyName().trim() + ", ";
                faculty.remove(facultyToRemove);
            }
            fire.setDescription(victims, hasUpgradeBeenPurchased());
        } else{
            victims = "No one";
            fire.setDescription(victims,hasUpgradeBeenPurchased());
        }
    }



    private BuildingModel findBuildingToBurn(ArrayList<BuildingModel> buildings) {
        Random rand = new Random();
        ArrayList<BuildingModel> validBuildings = new ArrayList<>();
        if (buildings.size() <= 1) return null;

        for (int i = 0; i < buildings.size(); i++){
            if (!buildings.get(i).getKindOfBuilding().equalsIgnoreCase("ADMIN")){
                validBuildings.add(buildings.get(i));
            }else i++;
        }

        int randomIndex = rand.nextInt(validBuildings.size());
        return validBuildings.get(randomIndex);
    }

    private void checkForUpgrade(String runId){
        if (inventoryManager.isPurchased(upgradeName,runId)){
            this.hasBeenUpgraded = true;
        }
    }

    private boolean hasUpgradeBeenPurchased(){
        return this.hasBeenUpgraded;
    }
}

