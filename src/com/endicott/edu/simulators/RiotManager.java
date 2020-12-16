package com.endicott.edu.simulators;
import java.util.Random;
import com.endicott.edu.datalayer.BuildingDao;
import com.endicott.edu.datalayer.CollegeDao;
import com.endicott.edu.models.*;
import java.util.List;

public class RiotManager {
    RiotModel currentRiot = new RiotModel();
    private Random rand = new Random();
    private CollegeModel college = new CollegeModel();
    private BuildingManager bm = new BuildingManager();

    public void handleTimeChange(String runId, int hoursAlive, PopupEventManager popupManager) {
        college = CollegeDao.getCollege(runId);
        checkRiotSeverity(college, bm, runId, popupManager);
    }

    public void checkRiotSeverity(CollegeModel college, BuildingManager bm, String runId, PopupEventManager popupManager) {
        // Low student happiness and high tuition
        if(college.getStudentBodyHappiness() <= 50 && college.getYearlyTuitionCost() > 80000)
            createSevereRiot(bm, runId, currentRiot, popupManager, "The Student Body is Unhappy and Tuition is too high!");
        // Low student happiness or High tuition
        else if(college.getStudentBodyHappiness() <= 65 || college.getYearlyTuitionCost() > 60000)
            createRowdyStudentBehaviour(runId, bm, currentRiot, popupManager, "The students are becoming visibly upset!");
        // Low faculty happiness
        else if(college.getFacultyBodyHappiness() <= 50)
            createFacultyRiot(bm, runId, currentRiot, popupManager, "You Aren't Treating The Faculty Well");
        // Number of students drops below certain amount
        else if(college.getRetentionRate() <= 60f)
            createStakeholderRiot(bm, runId, currentRiot, popupManager, "The Stakeholders are upset about the retention rate!");
        // Random, low cost riot
        else if(randomRiotChance())
            letPeopleRiot(bm, runId, 1000, 500, 250, 2500, randomRiotDescription());
    }

    // Lowers a random building's shown quality value and charges the player a fee
    public void letPeopleRiot(BuildingManager bm, String runID, int mean, int stdDev, int min, int max, String msg) {
        String affectedBldg = setRandomLocation(bm, runID);
        int amt = SimulatorUtilities.getRandomNumberWithNormalDistribution(mean, stdDev, min, max);
        Accountant.payBill(runID, msg + " " + affectedBldg + " was damaged during the riot!" + " This cost you: ", amt );
    }

    // For Joe and others to make non-sport related riots
    public void createRegularRiot(String collegeId, RiotModel riot, PopupEventManager popupManager, String cause) {
        riot.setName(cause);
        riot.setDescription("A riot has started on campus!");
        popupManager.newPopupEvent(collegeId, riot.getName(), riot.getDescription(), "Close", "ok", "resources/images/rioticon.png", "icon");
        // Decrease College Safety trait in event of riot
        CollegeRating.decreaseSafetyRating(collegeId, "REGULAR");
    }

    // Creates a severe riot if student happiness is very low and tuition is very high
    public void createSevereRiot(BuildingManager bm, String collegeId, RiotModel riot, PopupEventManager popupManager, String cause) {
        riot.setName(cause);
        riot.setRiotCost(250000);
        riot.setDescription("Mayhem has broken loose across the student body! They demand lower tuition and better quality of life!");
        popupManager.newPopupEvent(collegeId, riot.getName(), riot.getDescription(), "Close", "ok", "resources/images/rioticon.png", "icon");
        letPeopleRiot(bm, collegeId, 5000, 2000, 0, riot.getRiotCost(), "You really messed up.");
        // Decrease College Safety trait in event of riot
        CollegeRating.decreaseSafetyRating(collegeId, "SEVERE");
    }

    // Creates a faculty riot if the pay and happiness are too low
    public void createFacultyRiot(BuildingManager bm, String collegeId, RiotModel riot, PopupEventManager popupManager, String cause) {
        riot.setName(cause);
        riot.setRiotCost(150000);
        riot.setDescription("The faculty are fed up with the administration! They have started to boycott teaching!");
        popupManager.newPopupEvent(collegeId, riot.getName(), riot.getDescription(), "Close", "ok", "resources/images/rioticon.png", "icon");
        letPeopleRiot(bm, collegeId, 5000, 2000, 0, riot.getRiotCost(), "Professors aren't professing.");
        // Decrease College Safety trait in event of riot
        CollegeRating.decreaseSafetyRating(collegeId, "FACULTY");
    }

    // Defaces a random school property due to rowdy student behaviour
    public void createRowdyStudentBehaviour(String collegeId, BuildingManager bm, RiotModel riot, PopupEventManager popupManager, String cause) {
        riot.setName(cause);
        riot.setRiotCost(5000);
        riot.setDescription("A group of rowdy students defaced school property!");
        letPeopleRiot(bm, collegeId, 5000, 2000, 0, riot.getRiotCost(), "This gang of hooligans were destructive.");
        popupManager.newPopupEvent(collegeId, riot.getName(), riot.getDescription(), "Close", "ok", "resources/images/rioticon.png", "icon");
        // Decrease College Safety trait in event of riot
        CollegeRating.decreaseSafetyRating(collegeId, "ROWDY");
    }

    // Stakeholders will riot if retention rate is poor
    public void createStakeholderRiot(BuildingManager bm, String collegeId, RiotModel riot, PopupEventManager popupManager, String cause) {
        riot.setName(cause);
        riot.setRiotCost(50000);
        riot.setDescription("Your stakeholders aren't happy with your management");
        popupManager.newPopupEvent(collegeId, riot.getName(), riot.getDescription(), "Close", "ok", "resources/images/rioticon.png", "icon");
        letPeopleRiot(bm, collegeId, riot.getRiotCost(), 25000, 0, riot.getRiotCost(), "They did a lot of damage.");
    }

    // Occurs when a sports team wins a championship
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
        popupManager.newPopupEvent(collegeId, riot.getName(), riot.getDescription(), "Close", "ok", "resources/images/rioticon.png", "icon");
        // FIXME: Add support in SportManager so that createSportsRiot is passed a BuildingManager reference
        // letPeopleRiot(bm, collegeId, riot.getRiotCost(), 25000, 0, riot.getRiotCost(), "They did a lot of damage.");
    }

    // chooses a random building for the riot to have occurred in or nearby, and damages it
    public String setRandomLocation(BuildingManager bm, String collegeId) {
        int numAllBlds = 0;
        BuildingModel victim = new BuildingModel();
        List<BuildingModel> allBuildings = BuildingDao.getBuildings(collegeId);
        for (int i = 0; i < allBuildings.size(); i++)
            numAllBlds += 1;
        victim = allBuildings.get(rand.nextInt(numAllBlds));
        bm.acceleratedDecay(collegeId, String.valueOf(victim), "riot");
        return victim.getName();
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