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
    private int upgradedCatFireProb, catFireProb, demoProb;

    public FireManager() {
        upgradeName = "Smoke Detectors";
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

        createFireByOdds(runId, hoursAlive);
        List<FireModel> fires = FireDAO.getFires(runId);
        if (fires.size() != 0) {
            for (FireModel fire:fires) {
                generateCorrectPopUp(runId, fire,popupManager);
            }
        }
        fires.clear();
        FireDAO.saveAllFires(runId, fires);
    }

    /**
     * Starts a regular fire 10% of the time, and major fire 5%
     *
     * @param runId
     * @param hoursAlive
     */
    public void createFireByOdds(String runId, int hoursAlive) {
        EventManager eventManager = new EventManager(runId);

        if (CollegeManager.isMode(runId, CollegeMode.DEMO_FIRE) || eventManager.doesEventStart(runId, EventType.FIRE)) {
            // If you don't have smoke detectors, the fire is catastrophic.
            createFire(runId,hoursAlive);
            EventManager.newEventStart(runId);
        }
    }


    public static void establishCollege(String collegeId) {
        InventoryManager.createItem("Smoke Detectors", false, "smokedetector.png", 50000, 3, "Some smoke detectors might decrease the odds of a raging fire from breaking out.", collegeId);
    }

    private void generateCorrectPopUp(String collegeId, FireModel fire, PopupEventManager popupManager){
        popupManager.newPopupEvent(collegeId, fire.getBuildingBurned().getName() + " is on fire!",
                fire.getDescription(),
                "Ok", "NoCallBack",
                "resources/images/fire.png", "Fire");
    }


    public void createFire(String runId,int hoursAlive) {
        ArrayList<BuildingModel> buildings = (ArrayList<BuildingModel>) BuildingDao.getBuildings(runId);
        ArrayList<StudentModel> students = (ArrayList<StudentModel>) StudentDao.getStudents(runId);
        ArrayList<FacultyModel> faculty = (ArrayList<FacultyModel>) FacultyDao.getFaculty(runId);
        List<FireModel> fires = FireDAO.getFires(runId);
        BuildingManager buildingManager = new BuildingManager();
        StudentManager studentManager = new StudentManager();
        Random rand = new Random();
        Boolean isCatastrophic;
        BuildingModel buildingToBurn = findBuildingToBurn(buildings);
        String victims = "";

        // Decide if fire is catastrophic or normal
        if (!hasUpgradeBeenPurchased()){
            isCatastrophic = isFireCat(runId, rand, catFireProb);
        } else {
            isCatastrophic = isFireCat(runId, rand, upgradedCatFireProb);
        }


        // if there are no buildings, there can not be a fire.
        if (buildings.size() <= 0)
            return;

        int numStudentDeaths = getNumStudentFatalities(isCatastrophic,buildingToBurn,runId);
        int numFacultyDeaths = getNumFacultyFatalities(numStudentDeaths);
        FireModel fire = new FireModel(numStudentDeaths,numFacultyDeaths,runId,buildingToBurn);
        int cost = getFireCost(fire.getNumTotalFatalities(),fire.getBuildingBurned(),runId,fire);
        fire.setCostOfFire(cost);
        fire.setCatastrophicStatus(isCatastrophic);

        victims = generateVictimString(numStudentDeaths,numFacultyDeaths,victims,students,faculty);
        removeFireVictims(fire.getNumOfStudentFatalities(),fire.getNumOfFacultyFatalities(),students,faculty,victims,runId,fire);
        //studentManager.removeFromBuildingAndReassignAfterDisaster(runId,buildingToBurn.getName(),buildingToBurn.getKindOfBuilding());

        if (!isCatastrophic){
            fire.setDescription(victims, hasUpgradeBeenPurchased());
            buildingManager.acceleratedDecayAfterDisaster(runId,fire.getBuildingBurned().getName());
            fires.add(fire);
            FireDAO.saveNewFire(runId, fire);
            StudentDao.saveAllStudents(runId, students);
            FacultyDao.saveAllFaculty(runId,faculty);
            Accountant.payBill(runId, "Fire damage cost ", cost);
            NewsManager.createNews(runId, hoursAlive, ("Fire in " + fire.getBuildingBurned().getName() + ", " +
                    fire.getNumTotalFatalities() + " died."), NewsType.COLLEGE_NEWS, NewsLevel.BAD_NEWS);
        } else {
            if (buildingToBurn.getTotalBuildCost() >= Accountant.getAvailableCash(runId)){
                fire.setCostOfFire(buildingToBurn.getTotalBuildCost()/2);
            } else {
                fire.setCostOfFire(buildingToBurn.getTotalBuildCost());
            }
            fire.setDescription(victims, hasUpgradeBeenPurchased());
            fires.add(fire);
            FireDAO.saveNewFire(runId, fire);
            StudentDao.saveAllStudents(runId, students);
            FacultyDao.saveAllFaculty(runId,faculty);
            Accountant.payBill(runId, "Fire damage cost ", fire.getCostOfFire());
            NewsManager.createNews(runId, hoursAlive, ("Major fire at " + fire.getBuildingBurned().getName() +
                    " was destroyed, " + fire.getNumTotalFatalities() + " died."), NewsType.COLLEGE_NEWS, NewsLevel.BAD_NEWS);
            buildingManager.destroyBuildingInCaseOfDisaster(buildings,runId,buildingToBurn.getName());
            BuildingDao.saveAllBuildings(runId, buildings);
        }
    }

    private Boolean isFireCat(String runId, Random rand,int probability) {
        return rand.nextInt(100) <= probability || CollegeManager.isMode(runId, CollegeMode.DEMO_FIRE);
    }

    private String generateVictimString(int studentDeaths, int facultyDeaths,String victims, ArrayList<StudentModel> students,
                                        ArrayList<FacultyModel> faculty){
        for (int i = 0; i < studentDeaths && (students.size() > 0); ++i) {
            int studentToRemove = new Random().nextInt(students.size());
            victims += students.get(studentToRemove).getName().trim() + ", ";
        }
        for (int j = 0; j <facultyDeaths && (faculty.size() > 0); j++) {
            int facultyToRemove = new Random().nextInt(faculty.size());
            victims += faculty.get(facultyToRemove).getFacultyName().trim() + ", ";
        }
        return victims;
    }

    public void clearFires(String runId) {
        FireDAO.deleteFires(runId);
    }

    private int calculateFireCost(int victims, BuildingModel buildingToBurn, String runId, FireModel fire){
        Random rand = new Random();
        int randCost = rand.nextInt(2000);

        if (!fire.isCatastrophic() && victims >= 1){
            return victims * 10000;
        } else if (fire.isCatastrophic()){
            if (buildingToBurn.getTotalBuildCost() >= Accountant.getAvailableCash(runId)) {
                return buildingToBurn.getTotalBuildCost() / 2; // so you dont go bankrupt off one fire
            }
            return buildingToBurn.getTotalBuildCost();
        } else {
            return randCost;
        }
    }

    public int getFireCost(int victims, BuildingModel buildingToBurn, String runId,FireModel fire) {
        int fireUpgradeEffect = 2; // figure to divide costs by when calculating fire effects with upgrade purchased
        if (hasUpgradeBeenPurchased()){
            return calculateFireCost(victims,buildingToBurn,runId,fire)/fireUpgradeEffect;
        } else {
            return calculateFireCost(victims, buildingToBurn, runId, fire);
        }
    }


    public int getNumStudentFatalities(Boolean isCatastrophic, BuildingModel buildingToBurn, String runId) {
        ArrayList<StudentModel> students = (ArrayList<StudentModel>) StudentDao.getStudents(runId);
        Random rand = new Random();
        int numDeaths = rand.nextInt(10);
        if(!isCatastrophic){
            if (!hasUpgradeBeenPurchased()) {
                return numDeaths;
            } else {
                return numDeaths/2;
            }
        } else {
            // If all students are in fire building, leave some alive
            if (buildingToBurn.getNumStudents() >= students.size()){
                return  buildingToBurn.getNumStudents()/2;
            } else {
                return buildingToBurn.getNumStudents();
            }
        }
    }

    public int getNumFacultyFatalities(int studentVictims){
        if (studentVictims == 0) return studentVictims;
        return studentVictims/4;
    }

    public void removeFireVictims(int numStudentDeaths, int numFacultyDeaths, ArrayList<StudentModel> students,
                                     ArrayList<FacultyModel> faculty, String victims, String runId,FireModel fire){
        if (numFacultyDeaths == faculty.size()){
            numFacultyDeaths = numStudentDeaths/2;
        } else if (numStudentDeaths == students.size()){
            numStudentDeaths = numStudentDeaths/2;
        }

        if (fire.isCatastrophic()){
            for (int i = 0; i < numStudentDeaths; ++i) {
                int nStudents = students.size();
                if (nStudents > 0) {
                    int studentToRemove = new Random().nextInt(students.size());
                    students.remove(studentToRemove);

                }
            }
            StudentDao.saveAllStudents(runId,students);
            for (int j = 0; j <numFacultyDeaths; j++){
                int nFaculty = faculty.size();
                if (nFaculty > 0) {
                    int facultyToRemove = new Random().nextInt(faculty.size());
                    if (faculty.get(facultyToRemove).getTitle().equals("Dean") || faculty.get(facultyToRemove).getTitle().equals("Assistant Dean")) {
                        for (DepartmentModel d : DepartmentManager.getUnlockedDepts()) {
                            if (faculty.get(facultyToRemove).getDepartmentName().equals(d.getDepartmentName())) {
                                d.setEmployeeCount(faculty.get(facultyToRemove).getTitle(), 0);
                            }
                        }
                    }
                    faculty.remove(facultyToRemove);
                }
                FacultyDao.saveAllFaculty(runId,faculty);
            }
            return;
        }

        if (numStudentDeaths > 0 && !fire.isCatastrophic()) {
            for (int i = 0; i < numStudentDeaths && (students.size() > 0); ++i) {
                int studentToRemove = new Random().nextInt(students.size());
                victims += students.get(studentToRemove).getName().trim() + ", ";
                students.remove(studentToRemove);
            }
            StudentDao.saveAllStudents(runId,students);
            for (int j = 0; j <numFacultyDeaths && (faculty.size() > 0); j++){
                int facultyToRemove = new Random().nextInt(faculty.size());
                if(faculty.get(facultyToRemove).getTitle().equals("Dean") || faculty.get(facultyToRemove).getTitle().equals("Assistant Dean")){
                    for(DepartmentModel d : DepartmentManager.getUnlockedDepts()){
                        if(faculty.get(facultyToRemove).getDepartmentName().equals(d.getDepartmentName())){
                            d.setEmployeeCount(faculty.get(facultyToRemove).getTitle(), 0);
                        }
                    }
                }
                victims += faculty.get(facultyToRemove).getFacultyName().trim() + ", ";
                faculty.remove(facultyToRemove);
            }
            FacultyDao.saveAllFaculty(runId,faculty);
            //fire.setDescription(victims, hasUpgradeBeenPurchased());
        }
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

