package com.endicott.edu.simulators;

import com.endicott.edu.datalayer.BuildingDao;
import com.endicott.edu.datalayer.CollegeDao;
import com.endicott.edu.datalayer.StudentDao;
import com.endicott.edu.datalayer.NameGenDao;
import com.endicott.edu.models.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
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
    static private AchievementManager achievementManager = new AchievementManager();
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
        // Read in all the buildings.
        List<BuildingModel> buildings = dao.getBuildings(runId);

        // Go through the buildings making changes based on elapsed time.
        for (BuildingModel building : buildings) {
            building.updateTimeSinceLastRepair(CollegeManager.daysAdvance *24);
            billRunningCostOfBuilding(runId, building); //charges daily maintenance cost
            buildingDecayForTimeAdvance(runId, building); //decays the building quality
            workOnBuilding(building, runId); //if the building is under construction for any reason, this advances construction
            setNewRepairCost(runId, building); //sets the repair cost based on the building quality

            if(building.getHoursToComplete() == 0 && !building.isHasBeenAnnouncedAsComplete()){
                popupManager.newPopupEvent(runId, "Building Complete!", "Your new " + building.getKindOfBuilding() + " building, "
                                + building.getName() + "has finished construction and is now open!", "Close", "ok",
                        "resources/images/" + building.getKindOfBuilding() + ".png", building.getKindOfBuilding());
                building.setHasBeenAnnouncedAsComplete(true);
                BuildingDao.updateSingleBuildingInCache(runId, building);
            }
        }
        calculateOverallBuildingHealth(runId, buildings);
        // Really important the we save the changes to disk.
        dao.saveAllBuildingsUsingCache(runId);

        AchievementManager.checkAchievementStatus(runId);
    }

    /**
     * Charge the college the cost of running the building.
     *
     * @param collegeId college name
     * @param building the building that's causing the cost
     */
    private void billRunningCostOfBuilding(String collegeId, BuildingModel building) {
        // Multiple the cost per day based on how much the building is decayed
        int newCharge = ((int)(100 - building.getShownQuality())) * CollegeManager.daysAdvance *building.getCostPerDay();
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
        BuildingDao.updateSingleBuildingInCache(collegeId, building);
    }

    /**
     * Perform any construction work needed on buildings.
     *
     * @param building
     * @param runId
     */
    private void workOnBuilding(BuildingModel building, String runId){
        if (building.getHoursToComplete() > 0) {
            building.setHoursToComplete(building.getHoursToComplete() - (24 * CollegeManager.daysAdvance));
            surpriseEventDuringConstruction(runId, CollegeManager.daysAdvance * 24);
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

        BuildingDao.updateSingleBuildingInCache(runId, building);
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

    /**
     * Called by the servlet to add the building.
     * Sets all the initial stats, charges the college, and saves the building.
     *
     * @param collegeId
     * @param buildingName
     * @param buildingType
     * @param buildingSize
     * @return
     */
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
    }

    /**
     * Creates the desired type of building.
     *
     * @param buildingType type of building
     * @param buildingName name of building
     * @param buildingSize size of building
     */
    public static BuildingModel createCorrectBuildingType(String buildingType, String buildingName, String buildingSize) {
        if(buildingType.equals("Academic Center"))
            return new AcademicCenterModel(buildingName, 0, buildingSize);
        else if(buildingType.equals("Administrative Building"))
            return new AdministrativeBldgModel(buildingName);
        else if(buildingType.equals("Dining Hall"))
            return new DiningHallModel(buildingName, 0, buildingSize);
        else if(buildingType.equals("Dormitory"))
            return new DormModel(buildingName, 0, buildingSize);
        else if(buildingType.equals("Entertainment Center"))
            return new EntertainmentCenterModel(buildingName);
        else if(buildingType.equals("Health Center"))
            return new HealthCenterModel(buildingName);
        else if(buildingType.equals("Library"))
            return new LibraryModel(buildingName);
        else if(buildingType.equals("Sports Center"))
            return new SportsCenterModel(buildingName);
        else if(buildingType.equals("Baseball Diamond"))
            return new BaseballDiamondModel(buildingName, buildingSize);
        else if(buildingType.equals("Football Stadium"))
            return new FootballStadiumModel(buildingName, buildingSize);
        else if(buildingType.equals("Hockey Rink"))
            return new HockeyRinkModel(buildingName, buildingSize);
        //if for some reason it is none of these it will still make a new building
        else
            return new BuildingModel();
    }

    /**
     * Begins the upgrade process for the building.
     *
     * @param collegeId
     * @param building
     */
    public static void upgradeBuilding(String collegeId, BuildingModel building){
        //Upgrade time should always be Two(2) weeks
        building.setHoursToComplete(336); //always take two weeks to upgrade
        building.setIsUpgradeComplete(false);
        Accountant.payBill(collegeId, "Upgrade to " + building.getName(), building.getUpgradeCost());//pay for upgrade
        BuildingDao.updateSingleBuildingInCache(collegeId, building);
        BuildingDao.saveAllBuildingsUsingCache(collegeId);
    }

    /**
     * Author: Justen Koo
     * FIXME: The popup only appears after the user advances by one day, not instantly
     * Shows the current upgrades of the passed in building
     * @param building
     */
    public static void viewUpgrades(String collegeId, BuildingModel building) {
        String msg = building.getUpgradesString();
        PopupEventManager.newPopupEvent(collegeId, building.getName(), msg, "Ok", "ok", "resources/images/rioticon.png", "icon");
    }

    /**
     * Repairs the quality of the building based on how damaged it is.
     *
     * @param collegeId
     * @param building
     */
    public static void repairBuilding(String collegeId, BuildingModel building){
        float qualityDecayed = 100 - building.getShownQuality(); //100 is starting quality, take away the current building quality
        int numDays;

        Accountant.payBill(collegeId, "Repair to " + building.getName(), building.getRepairCost());//pay for repair

        if(qualityDecayed > 10){
            numDays = (int)qualityDecayed/10; //The repair time will be one day for every 10% below 90%
            building.setIsRepairComplete(false);
            building.setHoursToComplete(numDays * 24); //needs to be in hours, multiply by 24
        }
        else{ // If building quality is greater than 90% it should be immediately repaired
            building.setIsRepairComplete(true);
            building.setHiddenQuality(10); //10 is the max hidden quality
            setNewRepairCost(collegeId, building);
        }
        BuildingDao.updateSingleBuildingInCache(collegeId, building);
        BuildingDao.saveAllBuildingsUsingCache(collegeId);
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

    /**
     * Handles decaying every building everyday.
     *
     * @param collegeId
     * @param b
     */
    private void buildingDecayForTimeAdvance(String collegeId, BuildingModel b){
        // Only decay buildings that aren't under construction
        if((!(b.getHoursToComplete() > 0 || b.isUpgradeComplete() == false || b.isRepairComplete() == false))){
            float currentQuality = b.getHiddenQuality();
            //Generates a random number between 0-1
            //Multiply by .2 since One hidden quality point = Five shown quality points
            //It's impossible for a building to ever lose more than 1% per day
            double randomDecay = CollegeManager.daysAdvance * Math.random() * 0.2;
            b.setHiddenQuality((float) (currentQuality - randomDecay));
        }
        dao.updateSingleBuildingInCache(collegeId, b);
    }

    /**
     * Destroys a building in the case of a catastrophic fire/other disaster.
     *
     * @param buildings
     * @param collegeId
     * @param buildingName
     */
    public void destroyBuildingInCaseOfDisaster(List<BuildingModel> buildings, String collegeId, String buildingName){
        for (BuildingModel b : buildings) {
            if (b.getName().equals(buildingName)) {
                buildings.remove(b);
                // The students need to move to a new building
                studentManager.removeFromBuildingAndReassignAfterDisaster(collegeId, buildingName, b.getKindOfBuilding());
                // Decrease College Infrastructure rating in event of destroyed building
                CollegeRating.decreaseInfrastructureRating(collegeId, true);
                return;
            }
        }
    }

    /**
     * After a bad disaster, the building quality should drop significantly.
     * This will only drop the building quality up to 39.99% for disasters or 24.99% for riots and only ONCE (not every day after the disaster).
     *
     * @param collegeId
     * @param buildingName
     */
    public void acceleratedDecay(String collegeId, String buildingName, String event){
        List<BuildingModel> buildings = dao.getBuildings(collegeId);
        double randomDecay;
        for (BuildingModel b : buildings) {
            if (b.getName() == buildingName) {
                float currentQuality = b.getHiddenQuality();
                if (event == "disaster")
                     randomDecay = Math.random() * 40;
                else
                    randomDecay = Math.random() * 25;
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
                dao.updateSingleBuildingInCache(collegeId, b);
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
        return findBuildingToAssignToStudent(collegeId, BuildingType.dorm().getType());
    }

    /**
     * Return the name of a building that has an open spot for a student.
     *
     * @param collegeId college name
     * @return the name of the building where the student as placed.  If no space available, return commuter.
     */
    public String assignDiningHall(String collegeId) {
        return findBuildingToAssignToStudent(collegeId, BuildingType.dining().getType());
    }

    /**
     * Return the name of a building that has an open spot for a student.
     *
     * @param collegeId college name
     * @return the name of the building where the student as placed.  If no space available, return commuter.
     */
    public String assignAcademicBuilding(String collegeId) {
        return findBuildingToAssignToStudent(collegeId, BuildingType.academic().getType());
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
            if (name.equals(dormName) || name.equals(diningName) || name.equals(academicName))
                b.setNumStudents(s - 1);
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
        return getOpenSpots(collegeID, BuildingType.dorm().getType());
    }

    /**
     * Return the number of open desks in the college using helper method.
     *
     * @param collegeID
     * @return
     */
    public static int getOpenDesks(String collegeID){
        return getOpenSpots(collegeID, BuildingType.academic().getType());
    }

    /**
     * Return the number of open plates in the college using helper method.
     *
     * @param collegeID
     * @return
     */
    public static int getOpenPlates(String collegeID){
        return getOpenSpots(collegeID, BuildingType.dining().getType());
    }

    /**
     * Helper function for establishCollege to making saving the newly constructed buildings easier.
     *
     * @param building
     * @param collegeId
     * @param college
     */
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

        // Pre-loaded buildings
        // Every college should start with:
        // - One Dorm
        // - One Dining Hall
        // - One Academic Center
        // - Once Admin Building
        // - Once Sports Center
        DormModel startingDorm = new DormModel(NameGenDao.generateBuildingName()+" Hall",  0, "Medium");
        saveBuildingHelper(startingDorm, collegeId, college); // See above function

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

        // Necessary gates for buildings
        // Sizes
        gateManager.createGate(collegeId, "Large Size", "With Large Size you can house more students!", "resources/images/DORM.png", 1);
        gateManager.createGate(collegeId, "Extra Large Size", "Need room for even more students?  Extra Large Size dorms can help.", "resources/images/DORM.png",2);
        // Special Buildings
        gateManager.createGate(collegeId, "Library", "The library helps students academics.", "resources/images/LIBRARY.png", 3);
        gateManager.createGate(collegeId, "Health Center", "With the Health Center on campus students don't get sick as often.", "resources/images/HEALTH.png", 4);
        gateManager.createGate(collegeId, "Entertainment Center", "You're students would have more fun at the Entertainment Center.", "resources/images/ENTERTAINMENT.png", 5);
        // Sports Buildings
        gateManager.createGate(collegeId, "Football stadium", "To have a football team you need a football stadium.", "resources/images/FOOTBALL%20STADIUM.png", 2);
        gateManager.createGate(collegeId, "Baseball diamond", "To have a baseball team you need a football stadium.", "resources/images/BASEBALL%20DIAMOND.png", 3);
        gateManager.createGate(collegeId, "Hockey rink", "To have a hockey team you need a rink.", "resources/images/HOCKEY%20RINK.png", 4);

        /**
         * TODO: Achievements shouldn't be made in the BuildingManager, but should exist as soon as the player creates a college. These few lines of code should be moved somewhere else
         */
        // Achievements for the game
        achievementManager.createAchievement(collegeId, "I'm Level 1", "Reach Level 1", "level", 5000,1, 0, 0);
        achievementManager.createAchievement(collegeId,"Big Saver I", "Accumulate $300K or More", "money", 1500,1, 300000, 0);
        achievementManager.createAchievement(collegeId,"Make Happy I", "Reach 85% Student Happiness for the First Time", "happiness", 15000, 1, 0, 85);
        achievementManager.createAchievement(collegeId, "I'm Level 5", "Reach Level 5", "level", 10000,5, 0, 0);
        achievementManager.createAchievement(collegeId,"Big Saver II", "Accumulate $500k or More", "money", 5000,1, 500000, 0);
        achievementManager.createAchievement(collegeId,"Make Happy II", "Reach 90% Student Happiness for the First Time", "happiness", 50000, 1, 0, 90);
        achievementManager.createAchievement(collegeId, "I'm Level 10", "Reach Level 5", "level", 10000,5, 0, 0);
        achievementManager.createAchievement(collegeId,"Big Saver III", "Accumulate $750k or More", "money", 100000,1, 750000, 0);
        achievementManager.createAchievement(collegeId,"Make Happy III", "Reach 95% Student Happiness for the First Time", "happiness", 150000, 1, 0, 90);
    }

    /**
     * Pass in a building type, get a list of all the buildings of that type in the college.
     *
     * @param buildingType
     * @param collegeId
     * @return
     */
    public static List<BuildingModel> getBuildingListByType(String buildingType, String collegeId){
        List<BuildingModel> allBuildings = dao.getBuildings(collegeId);
        List<BuildingModel> buildingsToReturn = new ArrayList<>();
        for(BuildingModel b : allBuildings){
            if(b.getKindOfBuilding().equals(buildingType))
                buildingsToReturn.add(b);
        }
        return buildingsToReturn;
    }

    /**
     * Pass in a building name, get the BuildingModel of the building with that name.
     *
     * @param name
     * @param collegeId
     * @return
     */
    public static BuildingModel getBuildingByName(String name, String collegeId){
        List<BuildingModel> allBuildings = dao.getBuildings(collegeId);
        BuildingModel buildingToReturn = null;
        for(BuildingModel b : allBuildings){
            if(b.getName().equals(name))
                buildingToReturn = b;
        }
        return buildingToReturn;
    }

    /**
     * Make all the tips that show on the page.
     *
     * @param collegeId
     */
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

    private void calculateOverallBuildingHealth(String collegeId, List<BuildingModel> buildings) {
        CollegeManager.logger.info("BuildingManager: recreational happiness statistics");
        CollegeModel college = CollegeDao.getCollege(collegeId);

        int healthSum = 0;
        for (int i=0; i<buildings.size(); i++) {
            healthSum += buildings.get(i).getShownQuality();
        }

        int avgHealth = healthSum/Math.max(1,buildings.size());

        Random rand = new Random();
        college.setTotalBuildingHealth(Math.min(100, avgHealth + rand.nextInt(8)));
    }
}