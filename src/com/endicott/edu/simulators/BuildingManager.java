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
            building.updateTimeSinceLastRepair(24);
            billRunningCostOfBuilding(runId, building); //charges daily maintenance cost
            buildingDecayForOneDay(runId, building); //decays the building quality
            workOnBuilding(building, runId); //if the building is under construction for any reason, this advances construction
            setNewRepairCost(runId, building); //sets the repair cost based on the building quality

            if(building.getHoursToComplete() == 0 && !building.isHasBeenAnnouncedAsComplete()){
                popupManager.newPopupEvent("Building Complete!", "Your new " + building.getKindOfBuilding() + " building, "
                                + building.getName() + "has finished construction and is now open!", "Close", "ok",
                        "resources/images/" + building.getKindOfBuilding() + ".png", building.getKindOfBuilding());
                building.setHasBeenAnnouncedAsComplete(true);
                BuildingDao.updateSingleBuilding(runId, building);
            }
        }

        // Really important the we save the changes to disk.
        dao.saveAllBuildings(runId, buildings);
    }

    /**
     * Charge the college the cost of running the building.
     *
     * @param collegeId college name
     * @param building the building that's causing the cost
     */
    private void billRunningCostOfBuilding(String collegeId, BuildingModel building) {
        // Multiple the cost per day based on how much the building is decayed
        int newCharge = ((int)(100 - building.getShownQuality())) * building.getCostPerDay();
        Accountant.payBill(collegeId, "Maintenance of building " + building.getName(), newCharge);
    }

    /**
     * Update the repair cost based on the building quality
     *
     * @param building the building that's causing the cost
     */
    private static void setNewRepairCost(String collegeId, BuildingModel building){
        int newRepairCost = ((100 - (int)building.getShownQuality()) * 300);
        building.setRepairCost(newRepairCost);
        BuildingDao.updateSingleBuilding(collegeId, building);
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

        //This is to check if the REPAIR is done and fix the building quality
        if(building.getHoursToComplete() == 0 && building.isRepairComplete() == false){
            building.setIsRepairComplete(true);
            building.setHiddenQuality(10); //10 is the max hidden quality
            setNewRepairCost(runId, building);
        }

        //This it to check if the UPGRADE is done and fix the building stats based on that
        if(building.getHoursToComplete() == 0 && building.isUpgradeComplete() == false) {
            building.setIsBuilt(true);
            building.setIsUpgradeComplete(true);
            if (building.isUpgradeComplete()) {
                //This is how the building is upgraded
                //It's inside this for loop to make sure the extra capacity isn't added until AFTER the upgrade time
                String oldSize = building.getSize();
                String newSize;
                if (oldSize.equals("Small")) {newSize = "Medium";}
                else if (oldSize.equals("Medium")) {newSize = "Large";}
                else if (oldSize.equals("Large")) {newSize = "Extra Large";}
                else {newSize = "";}
                building.setSize(newSize);
                building.setStatsBasedOnSize(newSize);
                building.setCapacity(building.setCapacityBasedOnSize(newSize));
            }
        }


        BuildingDao.updateSingleBuilding(runId, building);
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
        newBuilding.setIsBuilt(false);

        // Pay for building
        if (newBuilding.getTotalBuildCost() >= Accountant.getAvailableCash(collegeId)) {
            newBuilding.setNote("Not enough money to build it.");
            return newBuilding;
        }

        Accountant.payBill(collegeId, "Charge of new building", newBuilding.getTotalBuildCost());
        CollegeModel college = CollegeDao.getCollege(collegeId);
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

    /**
     * Begins the upgrade process for the building
     *
     * @param collegeId
     * @param building
     */
    public static void upgradeBuilding(String collegeId, BuildingModel building){
        //Upgrade time should always be Two(2) weeks
        building.setHoursToComplete(336); //always take two weeks to upgrade
        building.setIsUpgradeComplete(false);
        Accountant.payBill(collegeId, "Upgrade to " + building.getName(), building.getUpgradeCost());//pay for upgrade
        BuildingDao.updateSingleBuilding(collegeId, building);
    }

    /**
     * Repairs the quality of the building based on how damaged it is
     *
     * @param collegeId
     * @param building
     */
    public static void repairBuilding(String collegeId, BuildingModel building){
        float qualityDecayed = 100 - building.getShownQuality(); //100 is starting quality, take away the current building qaulity
        int numDays;

        Accountant.payBill(collegeId, "Repair to " + building.getName(), building.getRepairCost());//pay for repair

        if(qualityDecayed > 10){
            numDays = (int)qualityDecayed/10; //The repair time will be one day for every 10% below 90%
            building.setIsRepairComplete(false);
            building.setHoursToComplete(numDays * 24); //needs to be in hours, multiply by 24
        }
        else{
            building.setIsRepairComplete(true);
            building.setHiddenQuality(10); //10 is the max hidden quality
            setNewRepairCost(collegeId, building);
        }

        BuildingDao.updateSingleBuilding(collegeId, building);
    }

    /**
     * Set the building as either having a disaster or None
     * and set the number of hours left in it.
     *
     * @param hoursLeftInDisaster the number of hours left in the disaster
     * @param buildingName   name of the building affected
     * @param collegeId
     * @param status  string representing the change of status
     */
    /*Takes in the length of the flood, the building buildingName affected by the flood, and the collegeId of the college. */
    public void disasterStatusChange(int hoursLeftInDisaster, String buildingName, String collegeId, String status) {
        List<BuildingModel> buildings = dao.getBuildings(collegeId);
        for (BuildingModel b : buildings) {
            if (b.getName().equals(buildingName)) {
                b.setCurDisaster(status);
                b.setLengthOfDisaster(hoursLeftInDisaster);
            }
        }
        dao.saveAllBuildings(collegeId, buildings);
    }


    public void buildingDecayForOneDay(String collegeId, BuildingModel b){
        if((!(b.getHoursToComplete() > 0|| b.getKindOfBuilding().equals(buildingModel.getLibraryConst()) ||
        b.getKindOfBuilding().equals(buildingModel.getHealthConst()) || b.getKindOfBuilding().equals(buildingModel.getEntertainmentConst()))
        || b.isUpgradeComplete() == false || b.isRepairComplete() == false)) {
            float currentQuality = b.getHiddenQuality();
            //Generates a random number between 0-1
            //Multiply by .2 since One hidden quality point = Five shown quality points
            //It's impossible for a building to ever lose more than 1% per day
            double randomDecay = Math.random() * 0.2;
            b.setHiddenQuality((float) (currentQuality - randomDecay));
        }
        dao.updateSingleBuilding(collegeId, b);
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
        gateManager.createGate(collegeId, "Library", "Gate until library is unlocked.", "resources/images/LIBRARY.png", 2000);
        gateManager.createGate(collegeId, "Health Center", "Gate until health center is unlocked.", "resources/images/HEALTH.png", 3500);
        gateManager.createGate(collegeId, "Entertainment Center", "Gate until entertainment center is unlocked.", "resources/images/ENTERTAINMENT.png", 5000);
    }

    public static List<BuildingModel> getBuildingListByType(String buildingType, String collegeId){
        List<BuildingModel> allBuildings = dao.getBuildings(collegeId);
        List<BuildingModel> buildingsToReturn = new ArrayList<>();
        for(BuildingModel b : allBuildings){
            if(b.getKindOfBuilding().equals(buildingType)){
                buildingsToReturn.add(b);
            }
        }

        return buildingsToReturn;
    }

    public static BuildingModel getBuildingByName(String name, String collegeId){
        List<BuildingModel> allBuildings = dao.getBuildings(collegeId);
        BuildingModel buildingToReturn = null;
        for(BuildingModel b : allBuildings){
            if(b.getName().equals(name)){
                buildingToReturn = b;
            }
        }
        return buildingToReturn;
    }

    private static void loadTips(String collegeId) {
        // Only the first tip should be set to true.
        TutorialManager.saveNewTip(collegeId, 0,"viewBuildings", "Construct more buildings to allow a greater maximum capacity at your college.", true, "ENTERTAINMENT.png");
        TutorialManager.saveNewTip(collegeId, 1,"viewBuildings","Upgrade your buildings to hold more students.", false, "underconstruction.png");
        TutorialManager.saveNewTip(collegeId, 2,"viewBuildings", "Construct sports buildings to unlock certain sports.", false, "BASEBALL DIAMOND.png");
        TutorialManager.saveNewTip(collegeId, 3,"viewBuildings","Building quality will decay automatically everyday. Be sure to repair your buildings often to keep your students happy!", false);
        TutorialManager.saveNewTip(collegeId, 4,"viewBuildings", "There must be enough Desks, Plates, and Beds to accommodate students coming into your college.", false, "desk.png");
        TutorialManager.saveNewTip(collegeId, 5,"viewBuildings", "Certain buildings have specific benefits. Try to see if you can notice them!", false);
        TutorialManager.saveNewTip(collegeId, 6,"viewBuildings", "Remember when purchasing a building that construction will take time. It won't just be built immediately!", false);
        TutorialManager.saveNewTip(collegeId, 7,"viewBuildings", "Watch out for disasters in buildings! The results could be catastrophic.", false, "fire.png");
    }
}







