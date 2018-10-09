package com.endicott.edu.simulators;

import com.endicott.edu.datalayer.BuildingDao;
import com.endicott.edu.datalayer.CollegeDao;
import com.endicott.edu.datalayer.DormitoryDao;
import com.endicott.edu.datalayer.StudentDao;
import com.endicott.edu.models.*;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * BuildingManager is responsible for simulating all building activity and for
 * providing functions for manipulation and retrieving information
 * about buildings.
 */
public class BuildingManager {
    static private BuildingDao dao = new BuildingDao();
    static private DormitoryDao dormDao = new DormitoryDao();
    static private Logger logger = Logger.getLogger("BuildingManager");
    static private StudentDao studentDao = new StudentDao();

    /**
     * Simulate all changes in buildings caused by advancing the hours the college
     * has been alive to the given value.
     *
     * @param runId college name
     * @param hoursAlive number of hours college has been alive
     */
    public void handleTimeChange(String runId, int hoursAlive) {
        // Read in all the buildings
        List<BuildingModel> buildings = dao.getBuildings(runId);

        // Go through the buildings making changes based on elasped time.
        for (BuildingModel building : buildings) {
            billRunningCostOfBuilding(runId, hoursAlive, building);
            workOnBuilding(building, 24, runId);
            building.setHourLastUpdated(hoursAlive);
        }

        // Really important the we save the changes to disk.
        dao.saveAllBuildings(runId, buildings);
    }

    /**
     * Perform any construction work needed on buildings.
     *
     * @param building
     * @param hoursWorkingOnBuilding
     * @param runId
     */
    public void workOnBuilding(BuildingModel building, int hoursWorkingOnBuilding, String runId){
        if (building.getHourLastUpdated() > 0) {  // Not sure what this if is about.
            int left = building.getHoursToComplete() - hoursWorkingOnBuilding;
            left = Math.max(0, left);
            building.setHoursToComplete(left);

            surpriseEventDuringConstruction(runId, hoursWorkingOnBuilding);
        }
    }

    /**
     * Given a building type, set attributes of the building.
     *
     * @param building
     */
    private static void setBuildingAttributesByBuildingType(BuildingModel building) {
        BuildingType buildingType = BuildingType.valueOf(building.getBuildingType());

        switch(buildingType) {
            case SMALL:
                building.setCapacity(200);
                building.setNumRooms(100);
                building.setTotalBuildCost(100);
                break;
            case MEDIUM:
                building.setCapacity(350);
                building.setNumRooms(175);
                building.setTotalBuildCost(175);
                break;
            case LARGE:
                building.setCapacity(500);
                building.setNumRooms(250);
                building.setTotalBuildCost(250);
                break;
            default:
                logger.severe("Could not add building: '" + building.getName() + "'");
        }
    }

    static public BuildingModel addBuilding(String collegeId, String buildingName, String buildingType) {
        if (!CollegeManager.doesCollegeExist(collegeId)) {
            return null;
        }

        int buildingTypeInt;
        if (buildingType.equals("Small")) {
            buildingTypeInt = 1;
        } else if (buildingType.equals("Medium")) {
            buildingTypeInt = 2;
        } else if (buildingType.equals("Large")) {
            buildingTypeInt = 3;
        } else {
            return null;
        }

        // Override some fields
        BuildingDao buildingDao = new BuildingDao();
        CollegeDao dao = new CollegeDao();
        CollegeModel college = dao.getCollege(collegeId);
        return (BuildingManager.createBuilding(collegeId, buildingName, buildingTypeInt,college.getHoursAlive()));
    }

    /**
     * Create a building.
     *
     * @param collegeId college name
     * @param buildingName name of new building
     * @param buildingType type of building (see BuildingType)
     * @param hoursAlive number of hours college has existed
     * @return
     */
    public static BuildingModel createBuilding(String collegeId, String buildingName, int buildingType, int hoursAlive) {

        // Create building
        BuildingModel newBuilding = new BuildingModel();
        newBuilding.setName(buildingName);
        newBuilding.setBuildingType(buildingType);
        setBuildingAttributesByBuildingType(newBuilding);
        newBuilding.setHourLastUpdated(0);
        newBuilding.setReputation(5);
        newBuilding.setCurDisaster("none");
        newBuilding.setMaintenanceCostPerDay(newBuilding.getNumRooms());

        // Pay for building
        if (newBuilding.getTotalBuildCost() >= Accountant.getAvailableCash(collegeId)) {
            newBuilding.setNote("Not enough money to build it.");
            return newBuilding;
        }

        Accountant.payBill(collegeId, "Charge of new building", newBuilding.getTotalBuildCost());
        NewsManager.createNews(collegeId, hoursAlive, "Construction of " + buildingName +" building has started! ", NewsType.RES_LIFE_NEWS, NewsLevel.GOOD_NEWS);
        newBuilding.setNote("A new building has been created.");

        // Save building
        BuildingDao buildingDao = new BuildingDao();
        buildingDao.saveNewBuilding(collegeId, newBuilding);
        return newBuilding;
    }

    /**
     * Charge the college the cost of running the building.
     *
     * @param collegeId college name
     * @param hoursAlive number of hours the college has existed
     * @param building the building that's causing the cost
     */
    private void billRunningCostOfBuilding(String collegeId, int hoursAlive, BuildingModel building) {
        // A building stores the last time it was updated.
        // Figure out how many hours have past since last updated.
        // Multiple by cost per hour.
        int newCharge = (hoursAlive - building.getHourLastUpdated()) * building.getMaintenanceCostPerDay();
        Accountant.payBill(collegeId, "Maintenance of building " + building.getName(), (int) (newCharge));

        // TODO: getMaintenanceCostPerDay() is used like it's an hourly cost.  Seems like it should be divided by 24.
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
        List<BuildingModel> buildings = dao.getBuildings(collegeId);
        String buildingName = "";
        for (BuildingModel b : buildings) {
            int s = b.getNumStudents();
            int c = b.getCapacity();
            buildingName = b.getName();
            if (b.getKindOfBuilding().equals(buildingType) && s < c) {
                b.setNumStudents(s + 1);
                dao.saveAllBuildings(collegeId, buildings);
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

    /**
     * Sell a building.
     *
     * @param collegeId
     * @param buildingName
     */
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

    /**
     * Get a list of the types of the buildings that can be built.
     * Returns a list of buildings where the building type is indicating
     * which buildings can be built.
     *
     * @param collegeId
     * @return
     */
    public List<BuildingModel> getWhatTypesOfBuildingsCanBeBuilt(String collegeId){
        ArrayList<BuildingModel> availableBuildingTypes = new ArrayList<>();
        BuildingModel smallBuilding = new BuildingModel();
        smallBuilding.setBuildingType(1);
        BuildingModel mediumBuilding = new BuildingModel();
        mediumBuilding.setBuildingType(2);
        BuildingModel largeBuilding = new BuildingModel();
        largeBuilding.setBuildingType(3);

        int availableCash = Accountant.getAvailableCash(collegeId);

        if(availableCash >= 250000){
            //can build all building types
            availableBuildingTypes.add(smallBuilding);
            availableBuildingTypes.add(mediumBuilding);
            availableBuildingTypes.add(largeBuilding);
        }
        else if(availableCash >= 175000){
            availableBuildingTypes.add(smallBuilding);
            availableBuildingTypes.add(mediumBuilding);
        }
        else if(availableCash >=100000){
            availableBuildingTypes.add(smallBuilding);
        }

        return availableBuildingTypes;
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

    private static void saveBuildingHelper(BuildingModel building, String collegeId, CollegeModel college){
        building.setHoursToComplete(0);
        building.setMaintenanceCostPerDay(60);
        building.setTotalBuildCost(100);
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
        DormModel startingDorm = new DormModel(college.getRunId()+" Hall",  0, 20, "Medium");
        saveBuildingHelper(startingDorm, collegeId, college);

        DiningHallModel startingDiningHall = new DiningHallModel(college.getRunId()+" Dining Hall",
                 0, 20, "Medium");
        saveBuildingHelper(startingDiningHall, collegeId, college);

        AcademicCenterModel startingAcademicBuilding = new AcademicCenterModel(college.getRunId()+" Academics",
                0, 20,"Medium");
        saveBuildingHelper(startingAcademicBuilding, collegeId, college);

        AdministrativeBldgModel startingAdministrative = new AdministrativeBldgModel(college.getRunId()+" Administrative",
                20);
        saveBuildingHelper(startingAdministrative, collegeId, college);
    }

    /**
     * Get the dorms at the college.
     *
     * @param collegeId
     * @return
     */
    static public List<DormitoryModel> getDorms(String collegeId){
        String dormName = "";
        List<DormitoryModel> dorms = dormDao.getDorms(collegeId);
        for(DormitoryModel d : dorms){
            d.setNumStudents(0);
        }
        List<StudentModel> students = studentDao.getStudents(collegeId);
        for(StudentModel s : students){
            dormName = s.getDorm();
            for (DormitoryModel d : dorms) {
                if (dormName.equals(d.getName())) {
                    d.incrementNumStudents(1);
                } else {
                    logger.info("Dorm was not found.");
                }
            }
        }
        return dorms;
    }
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
}







