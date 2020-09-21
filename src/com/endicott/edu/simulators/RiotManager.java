package com.endicott.edu.simulators;

import java.util.Random;

import com.endicott.edu.datalayer.BuildingDao;
import com.endicott.edu.datalayer.CollegeDao;
import com.endicott.edu.datalayer.FacultyDao;
import com.endicott.edu.models.*;
import java.util.List;


public class RiotManager {
    RiotModel currentRiot = new RiotModel();
    private Random rand = new Random();

    public void handleTimeChange(String runId, int hoursAlive, PopupEventManager popupManager) {
        // Makes a new CollegeModel college and sets it equal to the current college
        CollegeModel college = new CollegeModel();
        college = CollegeDao.getCollege(runId);
        StudentManager sm = new StudentManager();
        BuildingManager bm = new BuildingManager();

        checkRiotSeverity(college, sm, bm, runId, hoursAlive, popupManager);
    }

    /**
     * Author: Justen Koo
     * FIXME: Hardcoded the threshold for student happiness, tuition and faculty happiness and pay for testing purposes
     * Checks student happiness and tuition:
     * If student happiness is low, but tuition is acceptable, rowdy behaviour occurs
     * If student happiness is acceptable, but tuition is too high, rowdy behaviour occurs
     * If faculty happiness and pay are low, faculty riot occurs
     * If both happiness and tuition are unsatisfactory, a severe riot occurs
     * If both student happiness and tuition are acceptable, a regular riot occurs
     * @return void
     */
    public void checkRiotSeverity(CollegeModel college, StudentManager sm, BuildingManager bm, String runId, int hoursAlive, PopupEventManager popupManager) {
        // Low student happiness and high tuition
        if(college.getStudentBodyHappiness() < 33 && college.getYearlyTuitionCost() > 80000)
            createSevereRiot(runId, currentRiot, popupManager, "Student QoL Sucks and Tuition is too high!");
        // Low student happiness or High tuition or high percentage of Rebellious students
        else if(college.getStudentBodyHappiness() < 50 || college.getYearlyTuitionCost() > 60000 || sm.getNumRebelliousStudentsRatio(runId) > 30)
            createRowdyStudentBehaviour(runId, currentRiot, popupManager, "The students are becoming visibly upset!");
        // Low faculty pay or happiness
        else if(college.getStudentFacultyRatioRating() < 3 || college.getFacultyBodyHappiness() < 50)
            createFacultyRiot(runId, currentRiot, popupManager, "You Aren't Treating The Faculty Well");
        // Number of students drops below certain amount
        else if(college.getRetentionRate() < 60f)
            createStakeholderRiot(runId, currentRiot, popupManager, "The Stakeholders are pissed you messed up the school");
        // Acceptable happiness and tuition
        else {
            createRegularRiot(runId, currentRiot, popupManager, randomRiotDescription());
            letPeopleRiot(runId, 5000, 2000, 0, 5000, "Boring");
        }
    }

    public void letPeopleRiot(String runID, int mean, int stdDev, int min, int max, String msg) {
        int amt = SimulatorUtilities.getRandomNumberWithNormalDistribution(mean, stdDev, min, max);
        Accountant.payBill(runID, msg + " This cost you: ", amt );
    }

    // For Joe and others to make non-sport related riots
    public void createRegularRiot(String collegeId, RiotModel riot, PopupEventManager popupManager, String cause) {
        riot.setName(cause);
        riot.setDescription("A riot has started on campus!");
        popupManager.newPopupEvent(collegeId, riot.getName(), riot.getDescription(), "Ok", "ok", "resources/images/rioticon.png", "icon");
    }

    /**
     * Author: Justen Koo
     * Creates a severe riot if student happiness is very low and tuition is very high
     * @param
     * @return
     */
    public void createSevereRiot(String collegeId, RiotModel riot, PopupEventManager popupManager, String cause) {
        riot.setName(cause);
        riot.setRiotCost(250000);
        //chooseBuildingVictim();
        riot.setDescription("Mayhem has broken loose across the student body! They demand lower tuition and better quality of life!");
        popupManager.newPopupEvent(collegeId, riot.getName(), riot.getDescription(), "Ok", "ok", "resources/images/rioticon.png", "icon");
        letPeopleRiot(collegeId, 5000, 2000, 0, riot.getRiotCost(), "You really messed up.");
    }

    /**
     * Author: Justen Koo
     * Creates a faculty riot if the pay and happiness are too low
     * @param
     * @return
     */
    public void createFacultyRiot(String collegeId, RiotModel riot, PopupEventManager popupManager, String cause) {
        riot.setName(cause);
        riot.setRiotCost(150000);
        //chooseBuildingVictim();
        riot.setDescription("The faculty are fed up with the administration! They have started to boycott teaching!");
        popupManager.newPopupEvent(collegeId, riot.getName(), riot.getDescription(), "Ok", "ok", "resources/images/rioticon.png", "icon");
        letPeopleRiot(collegeId, 5000, 2000, 0, riot.getRiotCost(), "Professors aren't professing.");
    }

    /**
     * Author: Justen Koo
     * Defaces a random school property due to rowdy student behaviour
     */
    public void createRowdyStudentBehaviour(String collegeId, RiotModel riot, PopupEventManager popupManager, String cause) {
        riot.setName(cause);
        riot.setRiotCost(5000);
        // chooseBuildingVictim(collegeId);
        riot.setDescription("A group of rowdy students defaced school property!");
        popupManager.newPopupEvent(collegeId, riot.getName(), riot.getDescription(), "Ok", "ok", "resources/images/rioticon.png", "icon");
        letPeopleRiot(collegeId, 5000, 2000, 0, riot.getRiotCost(), "This gang of hooligans were destructive.");
    }

    public void createStakeholderRiot(String collegeId, RiotModel riot, PopupEventManager popupManager, String cause) {
        riot.setName(cause);
        riot.setRiotCost(50000);
        // chooseBuildingVictim(collegeId)
        riot.setDescription("Your stakeholders aren't happy with your management");
        popupManager.newPopupEvent(collegeId, riot.getName(), riot.getDescription(), "Ok", "ok", "resources/images/rioticon.png", "icon");
        letPeopleRiot(collegeId, riot.getRiotCost(), 2000, 0, riot.getRiotCost(), "They did a lot of damage.");
    }

    public static void createSportsRiot(String collegeId, SportModel sport, RiotModel riot, PopupEventManager popupManager) {
        if (sport.getSportName().equals("$50,000 - Men's Basketball")) {
            riot.setName("Men's Basketball Riot");
            riot.setDescription("Your Men's Basketball team has won a conference championship! A riot has broke out on campus, it will cost you $" + riot.getRiotCost());
        } else if (sport.getSportName().equals("$50,000 - Women's Basketball")) {
            riot.setName("Women's Basketball Riot");
            riot.setDescription("Your Women's Basketball team has won a conference championship! A riot has broke out on campus, it will cost you $" + riot.getRiotCost());
        } else if (sport.getSportName().equals("$50,000 - Men's Soccer")) {
            riot.setName("Men's Soccer Riot");
            riot.setDescription("Your Men's Soccer team has won a conference championship! A riot has broke out on campus, it will cost you $" + riot.getRiotCost());
        } else if (sport.getSportName().equals("$50,000 - Women's Soccer")) {
            riot.setName("Women's Soccer Riot");
            riot.setDescription("Your Women's Soccer team has won a conference championship! A riot has broke out on campus, it will cost you $" + riot.getRiotCost());
        } else if (sport.getSportName().equals("$50,000 - Men's Football")) {
            riot.setName("Football Riot");
            riot.setDescription("Your Football team has won a conference championship! A riot has broke out on campus, it will cost you $" + riot.getRiotCost());
        } else if (sport.getSportName().equals("$50,000 - Baseball")) {
            riot.setName("Baseball Riot");
            riot.setDescription("Your Baseball team has won a conference championship! A riot has broke out on campus, it will cost you $" + riot.getRiotCost());
        } else if (sport.getSportName().equals("$50,000 - Softball")) {
            riot.setName("Softball Riot");
            riot.setDescription("Your Softball team has won a conference championship! A riot has broke out on campus");
        }
        popupManager.newPopupEvent(collegeId, riot.getName(), riot.getDescription(), "Ok", "ok", "resources/images/rioticon.png", "icon");
    }

    /**
     * Author: Justen Koo
     * Compares the satisfaction levels of all of the buildings and causes the riot to occur at the one with the lowest
     * @param
     * @return
     */
    /*public String chooseBuildingVictim(String collegeId) {
        BuildingManager.getBuildingListByType("Academic", collegeId);
        return building;
    }*/

    /**
     * Author: Justen Koo
     * chooses a random building for the riot to have occurred in or nearby, and damages it
     * @param collegeId
     * @return
     */
    public BuildingModel setRandomLocation(BuildingManager bm, String collegeId) {
        int numAllBlds = 0;
        BuildingModel victim = new BuildingModel();
        List<BuildingModel> allBuildings = BuildingDao.getBuildings(collegeId);
        for (int i = 0; i < allBuildings.size(); i++)
            numAllBlds += 1;
        victim = allBuildings.get(rand.nextInt(numAllBlds));
        bm.acceleratedDecayAfterDisaster(collegeId, String.valueOf(victim));
        return victim;
    }

    public boolean isEventActive(String collegeId) {
        return false;
    }

    public Boolean randomRiotChance() {
        Random randRiot = new Random();
        int amt;
        amt = randRiot.nextInt(10);

        if (amt <= 2)
            return true;
        else
            return false;
    }

    public String randomRiotDescription() {
        Random randDesc = new Random();
        int amt;
        amt = randDesc.nextInt(10);

        if(amt <= 2)
            return "Your Local City's sport's team won a world championship! A Riot has erupted on campus!";
        else if(amt > 2 && amt <= 4)
            return "A huge election just ended and the results have hit the public, your students riot over the results!";
        else if(amt > 4 && amt <= 6)
            return "Your students complain they are getting too much homework and erupt into a riot!";
        else if(amt > 6 && amt <= 8)
            return " Your students complain they are getting too much homework and erupt into a riot!";
        else if(amt > 8 && amt <= 10)
            return "There is a nationwide story of college students protesting and your students decided to join in!";
        else
            return "Nope";
    }
}