package com.endicott.edu.simulators;

import com.endicott.edu.datalayer.*;
import com.endicott.edu.models.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

/**
 * Responsible for simulating everything sports related.
 */
public class SportManager {
    private static String season = "Fall";
    SportsDao dao = new SportsDao();
    static private Logger logger = Logger.getLogger("SportManager");

    public static void establishCollege(String collegeId) {
        season = "Fall";
        loadTips(collegeId);
        InventoryManager.createItem("Volley Ball Net",false,"volleyballnet.png",500, 0, "For a volleyball team, you need a net.", collegeId);
    }

    /**
     * Simulate the elapse of time at the college.
     *
     * @param collegeId
     * @param hoursAlive number of hours since the college started.
     */
    public void handleTimeChange(String collegeId, int hoursAlive, PopupEventManager popupManager) {
        List<SportModel> sports = dao.getSports(collegeId);

        //has the sport season changed?
        int thisMonth = CollegeManager.getCollegeCurrentMonth(collegeId);

        //if thisMonth is in fall and season is still summer
        if (thisMonth >= 9 && thisMonth <= 11){
           if (season.equalsIgnoreCase("Summer")){
               handleSeasonChange(collegeId, hoursAlive, popupManager);
           }
        }
        //if thisMonth is in winter and season is still fall
        else if (thisMonth == 12 || thisMonth == 1 || thisMonth == 2){
            if (season.equalsIgnoreCase("Fall")){
                handleSeasonChange(collegeId, hoursAlive, popupManager);
            }
        }
        //if thisMonth is in spring season is still winter
        else if (thisMonth >= 3 && thisMonth <= 5){
            if (season.equalsIgnoreCase("Winter")){
                handleSeasonChange(collegeId, hoursAlive, popupManager);
            }
        }
        //if thisMonth is in summer and season is still spring
        else if (thisMonth >= 6 && thisMonth <= 8){
            if (season.equalsIgnoreCase("Spring")){
                handleSeasonChange(collegeId, hoursAlive, popupManager);
            }
        }

        // If the net has been bought, create women's volleyball
        if (InventoryManager.isPurchased("Volley Ball Net", collegeId)) {
            createWomenVolleyball(collegeId, sports, popupManager);
        }

        for (SportModel sport : sports) {
            calculateNumberOfPlayersOnTeam(collegeId, sport);
            if(!sport.getCoachName().equals("noCoach"))
                updateCoachPerformance(collegeId, sport.getCoachName());
            else {
                assignCoach(collegeId, sport);
                popupManager.newPopupEvent(collegeId,"New Coach", "The " + sport.getSportName() + " team coach has been replaced", "ok", "done", "resources/images/money.jpg", "Coach Replacement");
            }
            fillUpTeamAndSetActiveStatus(collegeId, sport);
            billRunningCostofSport(collegeId, hoursAlive, sport);
            sport.setHourLastUpdated(hoursAlive);
            playGame(sport, hoursAlive, collegeId, popupManager);
        }

        dao.saveAllSports(collegeId, sports);
    }

    public void handleSeasonChange(String collegeId, int hoursAlive, PopupEventManager popupManager){
        List<SportModel> sports = dao.getSports(collegeId);

        //ONLY DO THE FOLLOWING IF sports IS NOT EMPTY:
        if (!(sports == null)){

            List<SportModel> recentSeasonSports = null;
            //get all sports from recent season, if they were active
            for (SportModel s: sports){
                if ((s.getSportSeason().equalsIgnoreCase(season)) && s.getIsActive() == 1){
                    recentSeasonSports.add(s);
                }
            }

            //ONLY DO THE FOLLOWING IF recentSeasonSports IS NOT EMPTY:
            if (!(recentSeasonSports == null)){
                //Check for championship
                for (SportModel recentSport: recentSeasonSports){
                    //If they win 80% or more of their games, they're a champion
                    if ((recentSport.getGamesWon()/recentSport.getNumGames()) >= 0.8){
                        //this sport is a champ
                        recentSport.addChampionship();
                        //make a popup
                        popupManager.newPopupEvent(collegeId, "Champions!", recentSport.getName() + " have won a championship!", "OK","ok", "resources/images/trophy.png", "Sports");
                        NewsManager.createNews(collegeId, hoursAlive, recentSport.getName() + " has won a championship!", NewsType.SPORTS_NEWS, NewsLevel.GOOD_NEWS);
                        //cause riot
                        RiotModel riot = new RiotModel();
                        RiotManager.createSportsRiot(collegeId, recentSport, riot, popupManager);
                    }
                    //set recent season sports to inactive even if they didn't win
                    recentSport.setIsActive(0);
                }
            }

            //next season sports will be set later in handleTimeChange
        }
        //change season variable
        if (season.equalsIgnoreCase("Summer")){
            season = "Fall";
        }
        else if (season.equalsIgnoreCase("Fall")){
            season = "Winter";
        }
        else if (season.equalsIgnoreCase("Winter")){
            season = "Spring";
        }
        else {
            season = "Summer";
        }
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

    private static void createWomenVolleyball(String collegeId, List<SportModel> sports, PopupEventManager popupManager) {
        if (!isSportAlreadyThere("Women's Volleyball", sports)) {
            SportModel team = new SportModel(11, 0, 25, 100, 0, 0, 0, 20, 0, 50, 0, "Women's Volleyball", collegeId, 0, 48, "Female", 3, "Fall", 96);
            assignCoach(collegeId, team);
            addPlayers(collegeId, team);
            calculateNumberOfPlayersOnTeam(collegeId, team);
            fillUpTeamAndSetActiveStatus(collegeId, team);
            sports.add(team);   // We've added a new sport to our list.
            popupManager.newPopupEvent(collegeId, team.getName(), "The " + team.getName() + " just formed. " +
                    " " + team.getCoachName() + " is the coach. Go team!",
                    "OK", "ok",
                    "resources/images/volleyballnet.png", "Sports");

        }
    }

    private static boolean isSportAlreadyThere(String sportName, List<SportModel> sports) {
        for (SportModel sport : sports) {
            if (sportName.equalsIgnoreCase(sport.getName()))
                return true;
        }
        return false;
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
                if (b.getKindOfBuilding().equals("FOOTBALL STADIUM")) {
                    stadiumBuilt = true;
                } else if (b.getKindOfBuilding().equals("HOCKEY RINK")) {
                    rinkBuilt = true;
                } else if (b.getKindOfBuilding().equals("SPORTS")) {
                    sportCenterBuilt = true;
                } else if (b.getKindOfBuilding().equals("BASEBALL DIAMOND")) {
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
        int oldActiveStatus = sport.getIsActive();
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
        if (sport.getIsActive() == 1 && oldActiveStatus == 0){
            //TODO: A new sport is now active, make pop up for main page

        }
    }

    /**
     *
     * @param collegeId
     * @param sport
     */
    public static void setSportSeason(String collegeId, SportModel sport){
        //set iterationDate to the current date, because that is what we will iterate from first
        Date iterationDate = CollegeManager.getCollegeDate(collegeId);
        for (int i = 0; i < sport.getNumGames(); i++){
            //set the random time interval for when the next game will be played
            //set the iterationDate to itself plus the time interval
            //add the new iterationDate to this sport's seasonSchedule
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
        ArrayList<SportModel> availableSports = new ArrayList<>();
        SportsDao dao = new SportsDao();
        CollegeModel college = CollegeDao.getCollege(collegeId);

        if (college == null) {
            return availableSports;
        }

        int collegeFunds = college.getAvailableCash();

        // Creates a list called availableSportNames of all sports names a college can make
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
        for(int yz = 0; yz < availableSportsNames.size(); yz++){

            // TODO: we should check if the college has enough money to startup the sport.
//            System.out.println(availableSportsNames.get(yz) + "This is a check");
//            logger.info("list of the names after the check " + availableSportsNames.get(yz));
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
    public static void playGame(SportModel sport, int hoursAlive, String collegeId, PopupEventManager popupManager){
        if (sport.getHoursUntilNextGame() <= 0) {
            simulateGame(sport, hoursAlive, collegeId, popupManager);
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
    public static void simulateGame(SportModel sport, int hoursAlive, String collegeId, PopupEventManager popupManager){
        //has the team won a game yet? (this is so we can send a popup when they win their first game)
        boolean winless = false;
        if (sport.getGamesWon() == 0){
            winless = true;
        }

        StudentDao stuDao = new StudentDao();
        List<StudentModel> students = stuDao.getStudentsOnSport(collegeId,sport.getName());
        int numOfPlayers = sport.getCurrentPlayers();
        int totalAthleticAbility = 0;

        for (StudentModel student : students) {
            totalAthleticAbility = totalAthleticAbility + student.getAthleticAbility();
        }

        int aveAbilityOnTeam = 0;
        if (numOfPlayers > 0)
            aveAbilityOnTeam = totalAthleticAbility / numOfPlayers;

        Random rand = new Random();
        int numberBetween5and9 = rand.nextInt(5) + 5;

        if (numberBetween5and9 > aveAbilityOnTeam) {
            sport.setGamesLost(sport.getGamesLost() + 1);
            popupManager.newPopupEvent(collegeId,"Sports", sport.getName() + " lost a game.", "OK", "ok", "resources/images/dangerSign.png", "Sports");
            NewsManager.createNews(collegeId, hoursAlive, sport.getName() + " just lost a game.", NewsType.SPORTS_NEWS, NewsLevel.BAD_NEWS);
        } else {
            sport.setGamesWon(sport.getGamesWon() + 1);
            //if winless is still set to true, this team just won their first game. send a popup to main page
            if (winless){
                popupManager.newPopupEvent(collegeId,"Sports", sport.getName() + " has won their first game of the season!", "OK", "ok", "resources/images/award.png", "Sports");
                NewsManager.createNews(collegeId, hoursAlive, sport.getName() + " just won a game!", NewsType.SPORTS_NEWS, NewsLevel.GOOD_NEWS);
            }
            else {
                popupManager.newPopupEvent(collegeId,"Sports", sport.getName() + " won a game!", "OK", "ok", "resources/images/award.png", "Sports");
                NewsManager.createNews(collegeId, hoursAlive, sport.getName() + " just won a game!", NewsType.SPORTS_NEWS, NewsLevel.GOOD_NEWS);
            }
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
//        CoachModel coach = new CoachModel();
        AvatarModel avatar = new AvatarModel();
        CoachModel coach = new CoachModel(team.getSportName(), coachName, "Coach", "Athletics", collegeId, 100000);
//        coach.setSportName(team.getSportName());
//        coach.setFacultyName(coachName);
//        coach.setTitle("Coach");
//        coach.setDepartmentName("Athletics");
//        coach.setCollegeID(collegeId);
//        coach.setSalary(100000);
        //Generates a random url for the student's avatar
        coach.setAvatarIcon(avatar);
        team.setCoachName(coach.getFacultyName());

    }

    public static String generateCoachUnderPerformingScenario(String badCoachName){
        Random r = new Random();
        String[] scenarios = {badCoachName + " is not well liked by the team",
        badCoachName + " Has been gambling on the games"};
        int rand = r.nextInt(scenarios.length - 0);
        return scenarios[rand];
    }

    private static void updateCoachPerformance(String collegeId, String coachName){
        CoachModel coach = CoachManager.getCoachByName(coachName);
        if (coach == null)
            return;
        FacultyManager.computeFacultyHappiness(coach, true);
        FacultyManager.computeFacultyPerformance(collegeId, coach);
    }

    private static void loadTips(String collegeId) {
        TutorialManager.saveNewTip(collegeId, 0,"viewSports", "Sports makes students happy.", true, "trophy.png");
        TutorialManager.saveNewTip(collegeId, 1,"viewSports", "Add more sports to make more money!", false, "money.jpg");
        TutorialManager.saveNewTip(collegeId, 2,"viewSports", "The better a team does, the happier students will be!", false, "smile.png");
    }
}

