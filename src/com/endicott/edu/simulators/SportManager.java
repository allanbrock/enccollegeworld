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

    public static void establishCollege(String collegeId) {
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
    public int getSportsHappinessVariable(String collegeId){
        //get all of the sports teams
        List<SportModel> sports = dao.getSports(collegeId);
        //average all of the sports teams records (only if they've played a game)
        int avgRecord = 0;
        //this counter is to keep track of how many sports there are so that we can average the records later
        int counter = 0;
        for (SportModel sport : sports) {
            if (sport.getNumGames() > 0) {
                int thisSportRecord = sport.getGamesWon() / sport.getNumGames();
                avgRecord += thisSportRecord;
                counter++;
            }
        }
        avgRecord = (avgRecord / counter) * 100;

        //Bonus Points:
        for (SportModel sport : sports) {
            if (sport.getNumGames() > 0){
                //if a team is undefeated (after more than 3 games)
                if (sport.getGamesLost() == 0 && sport.getNumGames() > 3){
                    avgRecord += 5;
                }
                //if a team has a winning record
                if (sport.getGamesWon() > sport.getGamesLost()){
                    avgRecord += 1;
                }
            }
        }

        //if avgRecord is greater than 100, just reset it to 100
        if (avgRecord > 100){
            avgRecord = 100;
        }
        return avgRecord;
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

        //Create Women's Volleyball as default sport
        SportModel default1 = new SportModel(11, 0, 25, 100, 0, 0, 0, 20, 0, 50, 0, "Women's Volleyball", collegeId, 0, 48, "Female", 3, "Fall", 96);
        assignCoach(collegeId, default1);
        addPlayers(collegeId, default1);
        calculateNumberOfPlayersOnTeam(collegeId, default1);
        fillUpTeamAndSetActiveStatus(collegeId, default1);
        newSportDao.saveNewSport(collegeId, default1);
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
                result = new SportModel(12, 0, 15, 100, 0, 0, 0, 20, 50000, 50, 0, "Men's Basketball", collegeId, 0, 72, "Male", 3, "Winter", 72);
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
                result = new SportModel(12, 0, 15, 100, 0, 0, 0, 20, 50000, 50, 0, "Women's Basketball", collegeId, 0, 72, "Female", 3, "Winter", 72);
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
        }
        else if(sportName.equals("$50,000 - Women's Volleyball")){
            //requires stadium
            if (sportCenterBuilt == false){
                //error: cannot create building, must create football stadium first
                addTeamResult = "Cannot create Women's Volleyball until Sports Center is built!";
                return addTeamResult;
            }
            else {
                result = new SportModel(11, 0, 25, 100, 0, 0, 0, 20, 0, 50, 0, "Women's Volleyball", collegeId, 0, 72, "Female", 3, "Fall", 48);
                Accountant.payBill(collegeId, "Women's Volleyball start up fee", result.getStartupCost());
            }
        } else {
            logger.severe("Could not add sport: '" + sportName + "'");
        }

        assignCoach(collegeId, result);
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
                sport.setActive(isSportInSeason(sport, collegeId));
            }
        }
        else {
            sport.setActive(isSportInSeason(sport, collegeId));
        }
    }

    /**
     * This will be called to determine whether or not a sport is in season based on the current month.
     * This DOES NOT determine if a sport has enough players on the team to play games.
     *
     * @param sport the sport we wish to find out whether or not it is in season
     * @param collegeId
     * @return 0 if it is not in season, 1 if it is
     */
    public static int isSportInSeason(SportModel sport, String collegeId){
        //What is the current month?
        int currentMonth = CollegeManager.getCollegeCurrentMonth(collegeId);

        //What is the sport's season?
        String thisSportSeason = sport.getSportSeason();

        //What is the current season, based on the current month?
        String currentSeason = null;
        //if the current month is between september and november
        if (currentMonth >= 9 && currentMonth <= 11){
            //set currentSeason to fall
            currentSeason = "Fall";
        }
        //if the current month is between december and february
        else if ((currentMonth == 12) || (currentMonth >= 1 && currentMonth <= 2)){
            //set currentSeason to winter
            currentSeason = "Winter";
        }
        //if the current month is between march and may
        else if (currentMonth >= 3 && currentMonth <= 5){
            //set currentSeason to spring
            currentSeason = "Spring";
        }
        //if the current month is not any of the above, it is the summer, leave current season set to null

        //If the current season is still null, it is summer
        if (currentSeason.equalsIgnoreCase(null)){
            return 0;
        }
        //If the current season is the same as this sport's season
        else if (currentSeason.equalsIgnoreCase(thisSportSeason)){
            return 1;
        }

        //return 0 just in case something went wrong.
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

    public void winChampionship(SportModel team, String collegeId) {
        team.addChampionship();
        if (team.getDivision() == 3) {
            Accountant.receiveIncome(collegeId, "Your " + team.getSportName() + "Won a Championship! You have been awarded $100,000", 100000);
            team.setDivision(2);
        } else if (team.getDivision() == 2)
        {
            Accountant.receiveIncome(collegeId, "Your " + team.getSportName() + "Won a Championship! You have been awarded $500,000", 500000);
        team.setDivision(1);
        }
        else if(team.getDivision() == 1)
            Accountant.receiveIncome(collegeId, "Your " + team.getSportName() + "Won a Championship! You have been awarded $1,000,000", 1000000);

    }

    private static void assignCoach(String collegeId, SportModel team){
        String coachName = NameGenDao.generateName(false);
        CoachModel coach = new CoachModel(team.getSportName(), coachName, "Coach", "Athletics", collegeId, 100000);
        team.setCoachName(coach.getFacultyName());
    }

    private static void loadTips(String collegeId) {
        // Only the first tip should be set to true.
        TutorialManager.saveNewTip(collegeId, 0,"viewSports", "GOOOOOOOAAAAAL!!", true);
        TutorialManager.saveNewTip(collegeId, 1,"viewSports", "Sports makes students happy.", false);
    }
}

