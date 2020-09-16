package com.endicott.edu.simulators;

import java.util.Random;

import com.endicott.edu.datalayer.CollegeDao;
import com.endicott.edu.datalayer.FacultyDao;
import com.endicott.edu.models.*;


public class RiotManager {
    RiotModel currentRiot = new RiotModel();

    public void handleTimeChange(String runId, int hoursAlive, PopupEventManager popupManager) {
        EventManager eventManager = new EventManager(runId);

        // Makes a new CollegeModel college and sets it equal to the current college
        CollegeModel college = new CollegeModel();
        college = CollegeDao.getCollege(runId);

        if (CollegeManager.isMode(runId, CollegeMode.DEMO_RIOT) || eventManager.doesEventStart(runId, EventType.RIOT))
            checkRiotSeverity(college, runId, hoursAlive, popupManager);
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
    public void checkRiotSeverity(CollegeModel college, String runId, int hoursAlive, PopupEventManager popupManager) {
        // Low student happiness and high tuition
        if(college.getStudentBodyHappiness() < 25 && college.getYearlyTuitionCost() > 80000)
            createSevereRiot(runId, currentRiot, popupManager, "Student QoL Sucks and Tuition is too high");
        // Low student happiness or High tuition
        else if(college.getStudentBodyHappiness() < 50 || college.getYearlyTuitionCost() > 60000)
            createRowdyStudentBehaviour(runId, currentRiot, popupManager, "Student QoL Sucks or Tuition is too high");
        // Low faculty pay or happiness
        else if(college.getStudentFacultyRatioRating() < 1 || college.getFacultyBodyHappiness() < 25)
            createFacultyRiot(runId, currentRiot, popupManager, "Faculty QoL Sucks");
        // Acceptable happiness and tuition
        else {
            createRegularRiot(runId, currentRiot, popupManager, randomRiotDescription());
            letStudentsRiot(runId, 5000, 2000, 0, 5000, "Boring");
        }
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

    public void letStudentsRiot(String runID, int mean, int stdDev, int min, int max, String msg) {
        int amt = SimulatorUtilities.getRandomNumberWithNormalDistribution(mean, stdDev, min, max);
        Accountant.payBill(runID, msg + " This cost you: ", amt );
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
        letStudentsRiot(collegeId, 5000, 2000, 0, riot.getRiotCost(), "You really messed up");
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
        letStudentsRiot(collegeId, 5000, 2000, 0, riot.getRiotCost(), "Professors aren't professing.");
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
        letStudentsRiot(collegeId, 5000, 2000, 0, riot.getRiotCost(), "This gang of hooligans were destructive");
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

    public boolean isEventActive(String collegeId) {
        return false;
    }
}