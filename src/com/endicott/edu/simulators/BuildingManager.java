package com.endicott.edu.simulators;

import com.endicott.edu.datalayer.BuildingDao;
import com.endicott.edu.datalayer.CollegeDao;
import com.endicott.edu.datalayer.StudentDao;
import com.endicott.edu.datalayer.NameGenDao;
import com.endicott.edu.models.*;

import javax.naming.Name;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * BuildingManager is responsible for simulating all building activity and for
 * providing functions for manipulation and retrieving information
 * about buildings.
 */;
public class BuildingManager {
    static private BuildingDao dao = new BuildingDao();
    static private Logger logger = Logger.getLogger("BuildingManager");
    static private StudentDao studentDao = new StudentDao();
    static private GateManager gateManager = new GateManager();
    static private StudentManager studentManager = new StudentManager();
    static private BuildingModel buildingModel = new BuildingModel();


    /**
     * Simulate all changes in buildings caused by advancing the hours the college
     * has been alive to the given value.
     *
     * @param runId college name
     * @param hoursAlive number of hours college has been alive
     */
    public void handleTimeChange(String runId, int hoursAlive, PopupEventManager popupManager) {
        // Read in all the buildings
        List<BuildingModel> buildings = dao.getBuildings(runId);

        // Go through the buildings making changes based on elapsed time.
        for (BuildingModel building : buildings) {
            building.updateTimeSinceLastRepair(24); //when a building is upgraded, this should go back to zero
            billRunningCostOfBuilding(runId, hoursAlive, building);
            workOnBuilding(building, runId);
            buildingDecayForOneDay(runId, building);
        }

        // Really important the we save the changes to disk.
        dao.saveAllBuildings(runId, buildings);
    }

    /**
     * Charge the college the cost of running the building.
     *
     * @param collegeId college name
     * @param hoursAlive number of hours the college has existed
     * @param building the building that's causing the cost
     */
    private void billRunningCostOfBuilding(String collegeId, int hoursAlive, BuildingModel building) {
        // Multiple the cost per day based on how much the building is decayed
        int newCharge = ((int)(100 - building.getShownQuality())) * building.getCostPerDay();
        Accountant.payBill(collegeId, "Maintenance of building " + building.getName(), newCharge);
    }

    /**
     * Perform any construction work needed on buildings.
     *
     * @param building
     * @param runId
     */
    public void workOnBuilding(BuildingModel building, String runId){
        if (building.getHoursToComplete() > 0) {
            building.setHoursToComplete(building.getHoursToComplete() - 24);

            surpriseEventDuringConstruction(runId, 24);
        }
    }

    /**
     * See if a surprise event has occurred during construction.
     * If so, report it.
     *
     * @param collegeId
     * @param hoursWorkingOnBuilding
     */
    public void surpriseEventDuringConstruction(String collegeId, int hoursWorkingOnBuilding) {
        String buildingName = "";
        double chance = Math.random();

        // There can be surprise donations to help pay for building costs.
        // TODO: this change calculation is questionable. Seems like should be hours/24.
        if (chance < 0.25 * 24f/hoursWorkingOnBuilding) {
            //25% chance of gaining $1000 dollars every 24 hours working on buildi.
            Accountant.receiveIncome(collegeId, "Donation received for building " + buildingName, 1000);
        } else if (chance < 0.35 * 24/hoursWorkingOnBuilding) {
            Accountant.receiveIncome(collegeId, "Ran into unexpected construction costs building " + buildingName, 500);
        }
    }

    static public BuildingModel addBuilding(String collegeId, String buildingName, String buildingType, String buildingSize) {
        if (!CollegeManager.doesCollegeExist(collegeId)) {
            return null;
        }

        // Create building
        BuildingModel newBuilding = createCorrectBuildingType(buildingType, buildingName, buildingSize);
        newBuilding.setTimeSinceLastRepair(0);
        newBuilding.setHoursToBuildBasedOnSize(buildingSize);
        newBuilding.setHasBeenAnnouncedAsComplete(false);

        // Pay for building
        if (newBuilding.getTotalBuildCost() >= Accountant.getAvailableCash(collegeId)) {
            newBuilding.setNote("Not enough money to build it.");
            return newBuilding;
        }

        Accountant.payBill(collegeId, "Charge of new building", newBuilding.getTotalBuildCost());
        CollegeDao dao = new CollegeDao();
        CollegeModel college = dao.getCollege(collegeId);
        NewsManager.createNews(collegeId, college.getHoursAlive(), "Construction of " + buildingName +" has started! ", NewsType.RES_LIFE_NEWS, NewsLevel.GOOD_NEWS);
        newBuilding.setNote("A new building has been created.");

        // Save building
        BuildingDao buildingDao = new BuildingDao();
        buildingDao.saveNewBuilding(collegeId, newBuilding);
        return newBuilding;
        // Override some fields
    }

    /**
     * Creates the desired type of building
     *
     * @param buildingType type of building
     * @param buildingName name of building
     * @param buildingSize size of building
     */
    public static BuildingModel createCorrectBuildingType(String buildingType, String buildingName, String buildingSize) {
        if(buildingType.equals("Academic Center")){
            return new AcademicCenterModel(buildingName, 0, buildingSize);
        }
        else if(buildingType.equals("Administrative Building")){
            return new AdministrativeBldgModel(buildingName);
        }
        else if(buildingType.equals("Dining Hall")){
            return new DiningHallModel(buildingName, 0, buildingSize);
        }
        else if(buildingType.equals("Dormitory")){
            return new DormModel(buildingName, 0, buildingSize);
        }
        else if(buildingType.equals("Entertainment Center")){
            return new EntertainmentCenterModel(buildingName);
        }
        else if(buildingType.equals("Health Center")){
            return new HealthCenterModel(buildingName);
        }
        else if(buildingType.equals("Library")){
            return new LibraryModel(buildingName);
        }
        else if(buildingType.equals("Sports Center")){
            return new SportsCenterModel(buildingName);
        }
        else if(buildingType.equals("Baseball Diamond")){
            return new BaseballDiamondModel(buildingName, buildingSize);
        }
        else if(buildingType.equals("Football Stadium")){
            return new FootballStadiumModel(buildingName, buildingSize);
        }
        else if(buildingType.equals("Hockey Rink")){
            return new HockeyRinkModel(buildingName, buildingSize);
        }
        else{ //if for some reason it is none of these it will still make a new building
            return new BuildingModel();
        }
    }

    public void upgradeBuilding(BuildingModel building){
        //Upgrade time should always be One(1) week
        buildingModel.upgradeBuilding(building.getSize());
        building.setHoursToComplete(168); //always take a week to upgrade
    }

    /**
     * Set the building as having a flood disaster and set the number of hours left in the flooding.
     *
     * @param lengthOfFlood
     * @param buildingName
     * @param collegeId
     */
    /*Takes in the length of the flood, the building buildingName affected by the flood, and the collegeId of the college. */
    public void floodAlert(int lengthOfFlood, String buildingName, String collegeId) {
        List<BuildingModel> buildings = dao.getBuildings(collegeId);
        for (BuildingModel b : buildings) {
            if (b.getName() == buildingName) {
                b.setCurDisaster("flood");
                b.setLengthOfDisaster(lengthOfFlood);
            }
        }
        dao.saveAllBuildings(collegeId, buildings);
    }

    public void buildingDecayForOneDay(String collegeId, BuildingModel b){
        if(!(b.getHoursToComplete() > 0)) {
            float currentQuality = b.getHiddenQuality();
            //Generates a random number between 0-1
            //Multiply by .2 since One hidden quality point = Five shown quality points
            //It's impossible for a building to ever lose more than 1% per day
            double randomDecay = Math.random() * 0.2;
            b.setHiddenQuality((float) (currentQuality - randomDecay));
        }
    }

    public void destroyBuildingInCaseOfDisaster(List<BuildingModel> buildings, String collegeId, String buildingName){
        for (BuildingModel b : buildings) {
            if (b.getName().equals(buildingName)) {
                buildings.remove(b);
                studentManager.removeFromBuildingAndReassignAfterDisaster(collegeId, buildingName, b.getKindOfBuilding());
                return;
            }
        }
    }

    public void acceleratedDecayAfterDisaster(String collegeId, String buildingName){
        List<BuildingModel> buildings = dao.getBuildings(collegeId);
        for (BuildingModel b : buildings) {
            if (b.getName() == buildingName) {
                float currentQuality = b.getHiddenQuality();
                double randomDecay = Math.random() * 4; //Max decay: 20%
                b.setHiddenQuality((float) (currentQuality - randomDecay));
            }
        }
        dao.saveAllBuildings(collegeId, buildings);
    }

    /**
     * Return the name of a building that has an open spot for a student
     * and reserve this spot by increasing the building occupancy by 1.
     * It's the responsibility of the caller to store the building name
     * under the student.
     *
     * @param collegeId college name
     * @param buildingType type of building
     * @return the name of the building where the student as placed.  If no space available, return commuter.
     */
    private String findBuildingToAssignToStudent(String collegeId, String buildingType){
        List<BuildingModel> buildingsByType = getBuildingListByType(buildingType, collegeId);
        String buildingName = "";
        for (BuildingModel b : buildingsByType) {
            int s = b.getNumStudents();
            int c = b.getCapacity();
            buildingName = b.getName();
            if (s < c) {
                b.setNumStudents(s + 1);
                dao.updateSingleBuilding(collegeId, b);
                return buildingName;
            }
        }

        return "Commuter";
    }

    /**
     * Return the name of a building that has an open spot for a student.
     *
     * @param collegeId college name
     * @return the name of the building where the student as placed.  If no space available, return commuter.
     */
    public String assignDorm(String collegeId) {
        return findBuildingToAssignToStudent(collegeId, BuildingModel.getDormConst());
    }

    /**
     * Return the name of a building that has an open spot for a student.
     *
     * @param collegeId college name
     * @return the name of the building where the student as placed.  If no space available, return commuter.
     */
    public String assignDiningHall(String collegeId) {
        return findBuildingToAssignToStudent(collegeId, BuildingModel.getDiningConst());
    }

    /**
     * Return the name of a building that has an open spot for a student.
     *
     * @param collegeId college name
     * @return the name of the building where the student as placed.  If no space available, return commuter.
     */
    public String assignAcademicBuilding(String collegeId) {
        return findBuildingToAssignToStudent(collegeId, BuildingModel.getAcademicConst());
    }

    /**
     * Increase the occupancy of the given building.
     * It's the responsibility of the caller to clear the building name
     * stored under the student.
     *
     * @param collegeId
     * @param dormName
     * @param diningName
     * @param academicName
     */
    public void removeStudent(String collegeId, String dormName, String diningName, String academicName) {
        List<BuildingModel> buildings = dao.getBuildings(collegeId);
        Boolean removed = false;
        for (BuildingModel b : buildings) {
            String name = b.getName();
            int s = b.getNumStudents();
            if (name.equals(dormName) || name.equals(diningName) || name.equals(academicName)) {
                b.setNumStudents(s - 1);
            }
        }
        dao.saveAllBuildings(collegeId, buildings);
    }

    /**
     * Helper function to return the number of open spots in certain buildings within the college.
     *
     * @param collegeId
     * @param buildingType
     * @return
     */
    private static int getOpenSpots(String collegeId, String buildingType) {
        List<BuildingModel> buildings = dao.getBuildings(collegeId);
        int openSpots = 0;
        for (BuildingModel b : buildings) {
            if(b.getKindOfBuilding().equals(buildingType) && b.getHoursToComplete() <= 0) {
                int numStudents = b.getNumStudents();
                int capacity = b.getCapacity();
                openSpots += capacity - numStudents;
            }
        }
        return openSpots;
    }

    /**
     * Return the number of open beds in the college using helper method.
     *
     * @param collegeID
     * @return
     */
    public static int getOpenBeds(String collegeID){
        return getOpenSpots(collegeID, BuildingModel.getDormConst());
    }

    /**
     * Return the number of open desks in the college using helper method.
     *
     * @param collegeID
     * @return
     */
    public static int getOpenDesks(String collegeID){
        return getOpenSpots(collegeID, BuildingModel.getAcademicConst());
    }

    /**
     * Return the number of open plates in the college using helper method.
     *
     * @param collegeID
     * @return
     */
    public static int getOpenPlates(String collegeID){
        return getOpenSpots(collegeID, BuildingModel.getDiningConst());
    }

    /**
     * Return the number of beds under construction.
     *
     * @param collegeId
     * @return
     */
    public static int getBedsUnderConstruction(String collegeId) {
        List<BuildingModel> buildings = dao.getBuildings(collegeId);
        int openBeds = 0;
        for (BuildingModel b : buildings) {
            if(b.getHoursToComplete() > 0) {
                int numStudents = b.getNumStudents();
                int capacity = b.getCapacity();
                openBeds += capacity - numStudents;
            }
        }
        return openBeds;
    }

   /* *//**
     * Sell a building.
     *
     * @param collegeId
     * @param buildingName
     *//*
    public static void sellBuilding(String collegeId, String buildingName) {

        BuildingDao buildingDao = new BuildingDao();
        List<BuildingModel> buildings = buildingDao.getBuildings(collegeId);
        List<StudentModel> students = studentDao.getStudents(collegeId);
        String name = "";
        int totalBuildCost = 0;
        int refund = 0;
        for(BuildingModel b : buildings){
            name = b.getName();
            totalBuildCost = b.getTotalBuildCost();

            //takes 20% of the build cost to refund back to the college.
            refund = (int)(totalBuildCost/20);
            if(name.equals(buildingName)){
                if(students.size() < (getOpenBeds(collegeId) - b.getCapacity())){
                    buildings.remove(b);
                    buildingDao.saveAllBuildings(collegeId, buildings);
                    Accountant.receiveIncome(collegeId, buildingName + "has been sold.", refund);
                    return;
                }
                else{
                    logger.info("Not enough open beds...");
                }

            }
        }

    }
*/
    /**
     * Get a list of the types of the buildings that can be built.
     * Returns a list of buildings where the building type is indicating
     * which buildings can be built.
     *
     * @param collegeId
     * @return
     */
//    public List<BuildingModel> getWhatTypesOfBuildingsCanBeBuilt(String collegeId){
//        ArrayList<BuildingModel> availableBuildingTypes = new ArrayList<>();
//        BuildingModel smallBuilding = new BuildingModel();
//        smallBuilding.setBuildingType(1);
//        BuildingModel mediumBuilding = new BuildingModel();
//        mediumBuilding.setBuildingType(2);
//        BuildingModel largeBuilding = new BuildingModel();
//        largeBuilding.setBuildingType(3);
//
//        int availableCash = Accountant.getAvailableCash(collegeId);
//
//        if(availableCash >= 250000){
//            //can build all building types
//            availableBuildingTypes.add(smallBuilding);
//            availableBuildingTypes.add(mediumBuilding);
//            availableBuildingTypes.add(largeBuilding);
//        }
//        else if(availableCash >= 175000){
//            availableBuildingTypes.add(smallBuilding);
//            availableBuildingTypes.add(mediumBuilding);
//        }
//        else if(availableCash >=100000){
//            availableBuildingTypes.add(smallBuilding);
//        }
//
//        return availableBuildingTypes;
//    }

    private static void saveBuildingHelper(BuildingModel building, String collegeId, CollegeModel college){
        building.setHoursToComplete(0);
        building.setCurDisaster("None");
        BuildingDao buildingDao = new BuildingDao();
        buildingDao.saveNewBuilding(collegeId, building);
        NewsManager.createNews(collegeId, college.getCurrentDay(), "Building " + building.getName() + " has opened.", NewsType.RES_LIFE_NEWS, NewsLevel.GOOD_NEWS);
    }
    /**
     * A new college has just been built.
     * Take care of any initial building construction.
     *
     * @param collegeId
     * @param college
     */
    static public void establishCollege(String collegeId, CollegeModel college) {
        loadTips(collegeId);

        //pre-loaded buildings
        DormModel startingDorm = new DormModel(NameGenDao.generateBuildingName()+" Hall",  0, "Medium");
        saveBuildingHelper(startingDorm, collegeId, college);

        DiningHallModel startingDiningHall = new DiningHallModel(NameGenDao.generateBuildingName()+" Dining Hall",
                 0, "Medium");
        saveBuildingHelper(startingDiningHall, collegeId, college);

        AcademicCenterModel startingAcademicBuilding = new AcademicCenterModel(NameGenDao.generateBuildingName()+" Academic Building",
                0,"Medium");
        saveBuildingHelper(startingAcademicBuilding, collegeId, college);

        AdministrativeBldgModel startingAdministrative = new AdministrativeBldgModel(NameGenDao.generateBuildingName()+" Building");
        saveBuildingHelper(startingAdministrative, collegeId, college);

        SportsCenterModel startingSportsCenter = new SportsCenterModel(NameGenDao.generateBuildingName()+" Sports Center");
        saveBuildingHelper(startingSportsCenter, collegeId, college);

        gateManager.createGate(collegeId, "Large Size", "Gate until large buildings are unlocked.", "resources/images/DORM.png", 700);
        gateManager.createGate(collegeId, "Extra Large Size", "Gate until extra large buildings are unlocked.", "resources/images/DORM.png",1500);
    }

    /**
     * Get the dorms at the college.
     *
     * @param collegeId
     * @return
     */
//    static public List<DormitoryModel> getDorms(String collegeId){
//        String dormName = "";
//        List<DormitoryModel> dorms = dormDao.getDorms(collegeId);
//        for(DormitoryModel d : dorms){
//            d.setNumStudents(0);
//        }
//        List<StudentModel> students = studentDao.getStudents(collegeId);
//        for(StudentModel s : students){
//            dormName = s.getDorm();
//            for (DormitoryModel d : dorms) {
//                if (dormName.equals(d.getName())) {
//                    d.incrementNumStudents(1);
//                } else {
//                    logger.info("Dorm was not found.");
//                }
//            }
//        }
//        return dorms;
//    }
    //made copy of above method because not sure which is right here
    static public List<BuildingModel> getBuildings(String collegeId){
        String buildingName = "";
        List<BuildingModel> buildings = dao.getBuildings(collegeId);
        for(BuildingModel b : buildings){
            b.setNumStudents(0);
        }
        List<StudentModel> students = studentDao.getStudents(collegeId);
        for(StudentModel s : students){
            buildingName = s.getDorm();
            for (BuildingModel b : buildings) {
                if (buildingName.equals(b.getName())) {
                    b.incrementNumStudents(1);
                } else {
                    logger.info("Building was not found.");
                }
            }
        }
        return buildings;
    }

    public List<BuildingModel> getBuildingListByType(String buildingType, String collegeId){
        List<BuildingModel> allBuildings = dao.getBuildings(collegeId);
        List<BuildingModel> buildingsToReturn = new ArrayList<>();
        for(BuildingModel b : allBuildings){
            if(b.getKindOfBuilding().equals(buildingType)){
                buildingsToReturn.add(b);
            }
        }

        return buildingsToReturn;
    }

    private static void loadTips(String collegeId) {
        TutorialManager.saveNewTip(collegeId, 0,"viewBuildings", "Construct more buildings to allow a greater maximum capacity at your college.", true);
        TutorialManager.saveNewTip(collegeId, 1,"viewBuildings","Upgrade your buildings to hold more students.", true);
        TutorialManager.saveNewTip(collegeId, 2,"viewBuildings", "Construct sports buildings to unlock certain sports.", false);
        TutorialManager.saveNewTip(collegeId, 3,"viewBuildings","Building quality will decay automatically everyday. Be sure to repair your buildings often to keep your students happy!", true);
        TutorialManager.saveNewTip(collegeId, 4,"viewBuildings", "There must be enough Desks, Plates, and Beds to accommodate students coming into your college.", true);
        TutorialManager.saveNewTip(collegeId, 5,"viewBuildings", "Certain buildings have specific benefits. Try to see if you can notice them!", true);
        TutorialManager.saveNewTip(collegeId, 6,"viewBuildings", "Remember when purchasing a building that construction will take time. It won't just be built immediately!", true);
        TutorialManager.saveNewTip(collegeId, 7,"viewBuildings", "Watch out for disasters in buildings! The results could be catastrophic.", true);


    }
}







