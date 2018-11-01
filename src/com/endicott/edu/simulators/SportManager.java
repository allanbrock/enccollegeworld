package com.endicott.edu.simulators;

import com.endicott.edu.datalayer.*;
import com.endicott.edu.models.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

/**
 * Responsible for simulating everything sports related.
 */
public class SportManager {
    SportsDao dao = new SportsDao();
    static private Logger logger = Logger.getLogger("SportManager");

    public void establishCollege(String collegeId) {
        loadTips(collegeId);
    }

    /**
     * Simulate the elapse of time at the college.
     *
     * @param collegeId
     * @param hoursAlive number of hours since the college started.
     */
    public void handleTimeChange(String collegeId, int hoursAlive, PopupEventManager popupManager) {
        List<SportModel> sports = dao.getSports(collegeId);

        for (SportModel sport : sports) {
            calculateNumberOfPlayersOnTeam(collegeId, sport);
            fillUpTeamAndSetActiveStatus(collegeId, sport);
            billRunningCostofSport(collegeId, hoursAlive, sport);
            sport.setHourLastUpdated(hoursAlive);
            playGame(sport, hoursAlive, collegeId);
        }

        dao.saveAllSports(collegeId, sports);
    }


    /**
     * Returns 0-100 int to be calculated into overall happiness.
     * int is based on how many spors teams are created and how well sports teams are doing.
     *
     * @return
     */
    public int getSportsHappinessVariable(){
        return 50;
    }

    /**
     * Charge the cost of running the sports since the last time
     * the bill was paid (the sport was updated).
     *
     * @param collegeId
     * @param hoursAlive
     * @param sport
     */
    private void billRunningCostofSport(String collegeId, int hoursAlive, SportModel sport) {
        int hoursSinceBillPaid = hoursAlive - sport.getHourLastUpdated();
        int newCharge = (int) (hoursSinceBillPaid * ((float) sport.getCostPerDay() / 24f));
        if(newCharge > 0)
        {
            Accountant.payBill(collegeId,"Charge for " + sport.getName(), newCharge);
        }
    }

    /**
     * Called by CollegeManager when the college is initially created.
     * Creates Men's and Women's Basketball as the initial sports teams.
     *
     * @param collegeId
     */
    public static void establishDefaultSportsTeams(String collegeId){
        SportsDao newSportDao = new SportsDao();

        //Create Men's and Women's Basketball as default sports
        SportModel default1 = new SportModel(12, 0, 15, 100, 0, 0, 0, 20, 0, 50, 0, "Men's Basketball", collegeId, 0, 72, "Male", 3, "Winter", 72);
        addPlayers(collegeId, default1);
        calculateNumberOfPlayersOnTeam(collegeId, default1);
        fillUpTeamAndSetActiveStatus(collegeId, default1);
        newSportDao.saveNewSport(collegeId, default1);

        SportModel default2 = new SportModel(12, 0, 15, 100, 0, 0, 0, 20, 0, 50, 0, "Women's Basketball", collegeId, 0, 72, "Female", 3, "Winter", 72);
        addPlayers(collegeId, default2);
        calculateNumberOfPlayersOnTeam(collegeId, default2);
        fillUpTeamAndSetActiveStatus(collegeId, default2);
        newSportDao.saveNewSport(collegeId, default2);
    }

    /**
     * Add a new sports team to the college.
     *
     * @param sportName
     * @param collegeId
     * @return
     */
    public static String addNewTeam(String sportName, String collegeId){
        SportsDao newSportDao = new SportsDao();
        logger.info("Attempt to add sport: '" + sportName + "' to '" + collegeId + "'");
        //SportModel sport = new SportModel();
        SportModel result = null;

        //variables for bulidings check
        boolean stadiumBuilt = false;
        boolean rinkBuilt = false;
        boolean diamondBuilt = false;
        boolean sportCenterBuilt = false;

        //Check what has been built
        List<BuildingModel> buildings = BuildingDao.getBuildings(collegeId);
        if (buildings != null) {
            for (BuildingModel b : buildings) {
                if (b.getName().equalsIgnoreCase("FOOTBALL STADIUM")) {
                    stadiumBuilt = true;
                } else if (b.getName().equalsIgnoreCase("HOCKEY RINK")) {
                    rinkBuilt = true;
                } else if (b.getName().equalsIgnoreCase("SPORTS CENTER")) {
                    sportCenterBuilt = true;
                } else if (b.getName().equalsIgnoreCase("BASEBALL DIAMOND")) {
                    diamondBuilt = true;
                }
            }
        }

        //This String will return null if everything is successfull
        String addTeamResult = null;

        if (sportName.equals("$50,000 - Men's Basketball")){
            //requires sports center to be created
            if (sportCenterBuilt == false){
                //error: cannot create building, must create sports center first
                addTeamResult = "Cannot create Men's Basketball until Sports Center is built!";
                return addTeamResult;
            }
            else {
                result = new SportModel(12, 0, 15, 100, 0, 0, 0, 20, 0, 50, 0, "Men's Basketball", collegeId, 0, 72, "Male", 3, "Winter", 72);
                Accountant.payBill(collegeId, "Men's Basketball start up fee", result.getStartupCost());
            }
        }
        else if(sportName.equals("$50,000 - Women's Basketball")){
            //requires sports center to be created
            if (sportCenterBuilt == false){
                //error: cannot create building, must create sports center first
                addTeamResult = "Cannot create Women's Basketball until Sports Center is built!";
                return addTeamResult;
            }
            else {
                result = new SportModel(12, 0, 15, 100, 0, 0, 0, 20, 0, 50, 0, "Women's Basketball", collegeId, 0, 72, "Female", 3, "Winter", 72);
                Accountant.payBill(collegeId, "Women's Basketball start up fee", result.getStartupCost());
            }
        }
        else if(sportName.equals("$50,000 - Baseball")){
            //requires baseball diamond
            if (diamondBuilt == false){
                //error: cannot create building, must create baseball diamond first
                addTeamResult = "Cannot create Baseball until Baseball Diamond is built!";
                return addTeamResult;
            }
            else {
                result = new SportModel(16, 0, 20, 100, 0, 0, 0, 20, 50000, 50, 0, "Baseball", collegeId, 0, 48, "Male", 3, "Spring", 48);
                Accountant.payBill(collegeId, "Baseball start up fee", result.getStartupCost());
            }
        }
        else if(sportName.equals("$50,000 - Softball")){
            //requires baseball diamond
            if (diamondBuilt == false){
                //error: cannot create building, must create baseball diamond first
                addTeamResult = "Cannot create Softball until Baseball Diamond is built!";
                return addTeamResult;
            }
            else {
                result = new SportModel(16, 0, 20, 100, 0, 0, 0, 20, 50000, 50, 0, "Softball", collegeId, 0, 48, "Female", 3, "Spring", 48);
                Accountant.payBill(collegeId, "Softball start up fee", result.getStartupCost());
            }
        }
        else if(sportName.equals("$50,000 - Women's Soccer")){
            //requires stadium
            if (stadiumBuilt == false){
                //error: cannot create building, must create football stadium first
                addTeamResult = "Cannot create Women's Soccer until Football Stadium is built!";
                return addTeamResult;
            }
            else {
                result = new SportModel(15, 0, 20, 100, 0, 0, 0, 20, 50000, 50, 0, "Women's Soccer", collegeId, 0, 72, "Female", 3, "Fall", 72);
                Accountant.payBill(collegeId, "Women's Soccer start up fee", result.getStartupCost());
            }
        }
        else if(sportName.equals("$50,000 - Men's Soccer")){
            //requires stadium
            if (stadiumBuilt == false){
                //error: cannot create building, must create football stadium first
                addTeamResult = "Cannot create Men's Soccer until Football Stadium is built!";
                return addTeamResult;
            }
            else {
                result = new SportModel(15, 0, 20, 100, 0, 0, 0, 20, 50000, 50, 0, "Men's Soccer", collegeId, 0, 72, "Male", 3, "Fall", 72);
                Accountant.payBill(collegeId, "Men's Soccer start up fee", result.getStartupCost());
            }
        }
        else if(sportName.equals("$50,000 - Men's Football")){
            //requires stadium
            if (stadiumBuilt == false){
                //error: cannot create building, must create football stadium first
                addTeamResult = "Cannot create Men's Football until Football Stadium is built!";
                return addTeamResult;
            }
            else {
                result = new SportModel(33, 0, 75, 100, 0, 0, 0, 20, 50000, 50, 0, "Men's Football", collegeId, 0, 144, "Male", 3, "Fall", 144);
                Accountant.payBill(collegeId, "Men's Football start up fee", result.getStartupCost());
            }
        } else {
            logger.severe("Could not add sport: '" + sportName + "'");
        }
        
        addPlayers(collegeId, result);
        calculateNumberOfPlayersOnTeam(collegeId, result);
        fillUpTeamAndSetActiveStatus(collegeId, result);
        newSportDao.saveNewSport(collegeId, result);

        return addTeamResult;
    }

    /**
     * Set the number of players that are on the sport by looking through
     * all students to see who's on the team.
     *
     * @param collegeId
     * @param sport
     */
    public static void calculateNumberOfPlayersOnTeam(String collegeId, SportModel sport){
        StudentDao dao = new StudentDao();
        List<StudentModel> students = dao.getStudents(collegeId);
        for(int i = 0; i < students.size(); i++) {
            if(students.get(i).getTeam().equals(sport)){
                sport.setCurrentPlayers(sport.getCurrentPlayers() + 1);
            }
        }
    }

    /**
     * Fill the roster for the sport and mark the sport as active
     * if you have enough players.
     *
     * @param collegeId
     * @param sport
     */
    public static void fillUpTeamAndSetActiveStatus(String collegeId, SportModel sport){
        if (sport.getCurrentPlayers() < sport.getMinPlayers()){
            addPlayers(collegeId, sport);
            if(sport.getCurrentPlayers() < sport.getMinPlayers()){
                sport.setActive(0);
            }
            else{
                //check if the sport is in season
                sport.setActive(isSportInSeason(sport));
            }
        }
        else {
            sport.setActive(isSportInSeason(sport));
        }
    }

    public static int isSportInSeason(SportModel sport){
        return 0;
    }

    /**
     * Add players to a team until reaching the maximum number of
     * players allowed on the team.
     *
     * @param collegeId
     * @param sport
     * @return
     */
    public static SportModel addPlayers(String collegeId, SportModel sport){
        StudentDao dao = new StudentDao();
        List<StudentModel> students = dao.getStudents(collegeId);
        for(int i = 0; i < students.size(); i++) {
            if (students.get(i).isAthlete() && ((students.get(i).getTeam().equals("")) || students.get(i).getTeam().equals("unknown"))) {
                if (students.get(i).getGender().equals(sport.getGender())) {
                    if (sport.getCurrentPlayers() < sport.getMaxPlayers()) {
                        students.get(i).setTeam(sport.getSportName());
                        sport.setCurrentPlayers((sport.getCurrentPlayers() + 1));
                    }
                }
            }
        }

        dao.saveAllStudents(collegeId, students);
        return sport;

    }

    /**
     * Sell the sports team.
     *
     * @param collegeId
     */
    static public void sellSport(String collegeId) {
        SportsDao sportsDao = new SportsDao();
        NewsFeedDao noteDao = new NewsFeedDao();

        sportsDao.deleteSports(collegeId);
        noteDao.deleteNotes(collegeId);
    }

    /**
     * Return a list of sports representing the sports that can
     * be added to the college.  You can't add a sport that already
     * exists.
     *
     * @param collegeId
     * @return
     */
    public static ArrayList<SportModel> checkAvailableSports(String collegeId) {
        SportsDao dao = new SportsDao();
        CollegeDao cao = new CollegeDao();
        CollegeModel college = cao.getCollege(collegeId);
        int collegeFunds = college.getAvailableCash();

        // Creates a list called availbleSportNames of all sports names a college can make
        ArrayList<String> availableSportsNames = new ArrayList<>();
        for (int i = 0; i < dao.seeAllSportNames().size(); i++ ){
            availableSportsNames.add(dao.seeAllSportNames().get(i));
        }

        // Compares the currents sports names w the names in the availbleSportsNames array and takes out any sports that are already created
        for(int x = 0; x < dao.getSports(collegeId).size(); x++){
            for(int y = 0; y < availableSportsNames.size(); y++){
                if( availableSportsNames.get(y).equals(dao.getSports(collegeId).get(x).getName())){
                    availableSportsNames.remove(y);
                }
            }
        }

        // Takes the modified availbleSportsNames array and converts/creates objects of sport model with the left...
        // over names in availblesportsnames and stores them in abvaibleSports
        ArrayList<SportModel> availableSports = new ArrayList<>();
        for(int yz = 0; yz < availableSportsNames.size(); yz++){

            // TODO: we should check if the college has enough money to startup the sport.

            System.out.println(availableSportsNames.get(yz) + "This is a check");
            logger.info("list of the names after the check " + availableSportsNames.get(yz));
            SportModel tempSport = new SportModel();
            tempSport.setName(availableSportsNames.get(yz));
            availableSports.add(tempSport);
        }
        return availableSports;
    }

    public static SportModel[] getAvailableSports(String collegeId) {
        List<SportModel> students = checkAvailableSports(collegeId);
        return students.toArray(new SportModel[students.size()]);
    }

    /**
     * If enough time has elapsed since the last game, play a game.
     *
     * @param sport
     * @param hoursAlive
     * @param collegeId
     */
    public static void playGame(SportModel sport, int hoursAlive, String collegeId ){
        if (sport.getHoursUntilNextGame() <= 0) {
            simulateGame(sport, hoursAlive, collegeId);
            sport.setHoursUntilNextGame(48);
        } else {
            sport.setHoursUntilNextGame(Math.max(0, hoursAlive - sport.getHourLastUpdated()));
        }
    }

    /**
     * Delete the given sport.
     *
     * @param collegeId
     * @param sportName
     */
    public static void deleteSelectedSport(String collegeId, String sportName){
        SportsDao dao = new SportsDao();
        dao.deleteSelectedSport(collegeId, sportName);
    }

    /**
     * Simulate the playing of a game.
     *
     * @param sport
     * @param hoursAlive
     * @param collegeId
     */
    public static void simulateGame(SportModel sport, int hoursAlive, String collegeId){
        StudentDao stuDao = new StudentDao();
        List<StudentModel> students = stuDao.getStudentsOnSport(collegeId,sport.getName());
        int numOfPlayers = sport.getCurrentPlayers();
        int totalAthleticAbility = 0;

        for (StudentModel student : students) {
            totalAthleticAbility = totalAthleticAbility + student.getAthleticAbility();
        }

        int aveAbilityOnTeam = totalAthleticAbility / numOfPlayers;
        Random rand = new Random();
        int numberBetween5and9 = rand.nextInt(5) + 5;

        if (numberBetween5and9 > aveAbilityOnTeam) {
            sport.setGamesLost(sport.getGamesLost() + 1);
            NewsManager.createNews(collegeId, hoursAlive, sport.getName() + " just lost a game.", NewsType.SPORTS_NEWS, NewsLevel.BAD_NEWS);
        } else {
            sport.setGamesWon(sport.getGamesWon() + 1);
            NewsManager.createNews(collegeId, hoursAlive, sport.getName() + " just won a game!", NewsType.SPORTS_NEWS, NewsLevel.GOOD_NEWS);
        }
        SportManager rep = new SportManager();
        rep.sportRep(sport, collegeId);
    }

    /**
     * Set the reputation of a sports team based on wins and loses.
     *
     * @param sport
     * @param collegeId
     */
    public void sportRep(SportModel sport, String collegeId) {
        if (sport.getGamesWon() > sport.getGamesLost()) {
            sport.setReputation(sport.getReputation() + 5);
        } else if (sport.getGamesWon() < sport.getGamesLost()) {
            sport.setReputation(sport.getReputation() - 5);
        }

        int rep = sport.getReputation();
        rep = Math.max(rep, 0);
        rep = Math.min(rep, 100);
        sport.setReputation(rep);
    }

    private static void loadTips(String collegeId) {
        TutorialManager.saveNewTip(collegeId, 0,"viewSports", "GOOOOOOOAAAAAL!!", true);
        TutorialManager.saveNewTip(collegeId, 1,"viewSports", "Sports makes students happy.", false);
    }
}

