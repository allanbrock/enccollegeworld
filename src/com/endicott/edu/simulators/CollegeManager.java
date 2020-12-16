package com.endicott.edu.simulators;

import com.endicott.edu.datalayer.*;
import com.endicott.edu.models.*;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

/**
 * The CollegeManager is responsible for simulating all overall college functions,
 * such as creating a college, deleting the college, and is responsible for
 * providing college information (retention rate, current tuition, etc.)
 */

public class CollegeManager {
    static public final int STARTUP_FUNDING = 200000;  // Amount of money initially in college bank account.
    public static Logger logger = Logger.getLogger("CollegeManager");

    /**
     * Creates a new college.
     *
     * @param collegeId college name
     * @return the college
     */
    static public CollegeModel establishCollege(String collegeId) {
        loadTips(collegeId);
        Logger logger = Logger.getLogger("CollegeManager");
        logger.info("Establishing the college");
        CollegeModel college = null;

        // See if there already is a college for this run.
        // We don't expect this, but if so, just return it.
        try {
            college = CollegeDao.getCollege(collegeId);
        } catch (Exception ignored) {
            return null;
        }
        if (college != null) {
            return college;
        }

        // Create the college.
        college = new CollegeModel();
        college.setRunId(collegeId);
        college.setHoursAlive(1);
        college.setNewSemester();
        college.setAvailableCash(STARTUP_FUNDING);
        CollegeDao.saveCollege(college);

        // Each functional area/simulator in the college gets called to
        // take care of its start-the-college needs.
        // The order of this matters (example: a dorm is established, before students enter).
        NewsManager.createNews(collegeId, college.getCurrentDay(),"The college was established today.", NewsType.COLLEGE_NEWS, NewsLevel.GOOD_NEWS);

        logger.info("Establish buildings.");BuildingManager.establishCollege(collegeId, college);
        logger.info("Establish academics.");AcademicsManager.establishCollege(collegeId);
        logger.info("Establish faculty.");FacultyManager.establishCollege(collegeId, college);
        logger.info("Establish admissions.");AdmissionsManager.establishCollege(collegeId, 140);

        logger.info("Establish student.");StudentManager studentManager = new StudentManager();
        logger.info("Establish student.");studentManager.establishCollege(collegeId);
        logger.info("Establish sports.");SportManager.establishCollege(collegeId);
        logger.info("Establish gates.");GateManager.establishCollege(collegeId);
        logger.info("Establish floods.");FloodManager.establishCollege(collegeId);
        logger.info("Establish snow.");SnowManager.establishCollege(collegeId);
        logger.info("Establish plague.");PlagueManager.establishCollege(collegeId);
        logger.info("Establish fire.");FireManager.establishCollege(collegeId);
        logger.info("Establish play.");PlayManager.establishCollege(collegeId);
        logger.info("Establish inventory.");InventoryManager.establishCollege(collegeId);
        //CoachManager.removeAllCollegeCoaches(collegeId);
        calculateTuitionRating(collegeId);
        CollegeRating collegeTraits = new CollegeRating();
        collegeTraits.handleTimeChange(collegeId);
        college.getFinancialGraph().getTuitionCosts().add(college.getYearlyTuitionCost());
        studentManager.calculateStatistics(collegeId, true);
        return college;
    }

    /**
     * Deletes a college.  Removes all storage associated with the college.
     *
     * @param collegeId college name
     */
    static public void sellCollege(String collegeId) {
        //CoachManager.removeAllCollegeCoaches(collegeId);
        CollegeDao.deleteCollege(collegeId);
        BuildingDao.deleteBuilding(collegeId);
        FacultyDao.removeAllFaculty(collegeId);
        FloodDao.deleteFlood(collegeId);
        NewsFeedDao.deleteNotes(collegeId);
        PlagueDao.deletePlagues(collegeId);
        SportsDao.deleteSports(collegeId);
        StudentDao.deleteStudents(collegeId);
        IdNumberGenDao.deleteIDs(collegeId);
        InventoryDao.deleteItem(collegeId);
        SnowDao.deleteSnowStorm(collegeId);
        AdmissionsDao.removeAdmissionsData(collegeId);
        AcademicsDao.removeAcademicData(collegeId);

    }

    /**
     * Advance the clock x days.  Simulate all changes occurring during those days.
     * Time at the college is counted in hours.
     * All calculations are done in terms of hours.
     *
     * @param collegeId college name
     */
    static public CollegeModel advanceTime(String collegeId, PopupEventManager popupManager) {
        CollegeModel college = CollegeDao.getCollege(collegeId);

        //Reset both the daily expenses and income (Since setter is += we will subtract current amt)
        college.setTotalExpenditure(-1*college.getTotalExpenditure());
        college.setTotalIncome(-1*college.getTotalIncome());

        // If there is a popup, we are not going to advance the day.
        // The pop must be cleared through the user interface first.
        List<PopupEventModel> popupEvents = PopupEventDao.getPopupEvents(collegeId);
        //I COMMENTED THIS OUT BECAUSE POPUPS ARE NOT GOING AWAY, WHEN FIXED UNCOMMENT THIS
//        if (popupEvents == null || popupEvents.size() > 0) {
//            logger.info("Not advancing day because there are pop ups.");
//            return college;
//        }

        college.setHoursAlive(college.getHoursAlive() + (24*CollegeModel.daysAdvance));  //Advance time by 1 week
        college.advanceTime(collegeId);  // decrease number of weeks left in semester
        CollegeDao.saveCollege(college);  // Notice that after setting fields in college we need to save.

        // How many hours has the college been alive (counting from hour 0).
        int hoursAlive = college.getHoursAlive();

        // Tell all the simulators about the time change.
        // Each one takes care of what happened since they were
        // last called.  They are given the current time.

        InventoryManager.handleTimeChange(collegeId, hoursAlive, popupManager);

        logger.info("AdvanceTime Calculate Tuition Rating");
        //Before anything else, the tuition rating it changed for the college
        calculateTuitionRating(collegeId);

        logger.info("AdvanceTime Disasters");
        EventManager disasterManager = new EventManager(collegeId);
        disasterManager.handleTimeChange(collegeId, hoursAlive, popupManager);

        logger.info("AdvanceTime Plagues");
        PlagueManager plagueManager = new PlagueManager();
        plagueManager.handleTimeChange(collegeId, hoursAlive, popupManager);

        logger.info("AdvanceTime Floods");
        FloodManager floodManager = new FloodManager();
        floodManager.handleTimeChange(collegeId, hoursAlive, popupManager);

        logger.info("AdvanceTime Fires");
        FireManager fireManager = new FireManager();
        fireManager.handleTimeChange(collegeId, hoursAlive, popupManager);

        logger.info("AdvanceTime Snow");
        SnowManager snowManager = new SnowManager();
        snowManager.handleTimeChange(collegeId, hoursAlive, popupManager);

        logger.info("AdvanceTime Riots");
        RiotManager riotManager = new RiotManager();
        riotManager.handleTimeChange(collegeId, hoursAlive, popupManager);

        logger.info("AdvanceTime Buildings");
        BuildingManager buildingManager = new BuildingManager();
        buildingManager.handleTimeChange(collegeId, hoursAlive, popupManager);

        logger.info("AdvanceTime Sports");
        SportManager sportManager = new SportManager();
        sportManager.handleTimeChange(collegeId, hoursAlive, popupManager);

        logger.info("AdvanceTime Students");
        StudentManager studentManager = new StudentManager();
        studentManager.handleTimeChange(collegeId, hoursAlive, popupManager);

        logger.info("AdvanceTime Admissions");
        AdmissionsManager.handleTimeChange(collegeId, hoursAlive, popupManager);

        logger.info("AdvanceTime Academics");
        AcademicsManager.handleTimeChange(collegeId, hoursAlive, popupManager);


        logger.info("AdvanceTime Faculty");
        FacultyManager.handleTimeChange(collegeId, hoursAlive, popupManager);
        DepartmentManager.handleTimeChange(collegeId, popupManager);
        PlayManager.handleTimeChange(collegeId, hoursAlive, popupManager);
        GateManager.handleTimeChange(collegeId, hoursAlive, popupManager);

        logger.info("AdvanceTime College Traits and update tips");
        CollegeRating collegeTraits = new CollegeRating();
        collegeTraits.handleTimeChange(collegeId);

        logger.info("AdvanceTime Loans");
        FinanceManager fm = new FinanceManager();
        fm.handleTimeChange(collegeId);

        logger.info("AdvanceTime Achievements");
        AchievementManager.handleTimeChange(collegeId, popupManager);

        college.getFinancialGraph().getTuitionCosts().add(college.getYearlyTuitionCost());

        TutorialManager.advanceTip("viewBuildings",collegeId);
        TutorialManager.advanceTip("viewCollege",collegeId);
        TutorialManager.advanceTip("viewFaculty",collegeId);
        TutorialManager.advanceTip("viewSports",collegeId);
        TutorialManager.advanceTip("viewStudent",collegeId);

//        if (college.getAvailableCash() <= 0) {
//            popupManager.newPopupEvent("Bankrupt!", "You ran out of money! Better luck next time!",
//                    "Return to Main Menu", "returnToWelcome", "resources/images/bankrupt.jpg",
//                    "Insert empty Wallet Picture Here");
//        }

        logger.info("AdvanceTime Done");
        return college;
    }

    public static String getDate(){
        return String.valueOf(System.currentTimeMillis());
    }


    /**
     * Returns current Date of college.
     *
     * @param collegeId college name
     * @return the Date of the college
     */
    static public Date getCollegeDate(String collegeId) {
        CollegeModel college = new CollegeDao().getCollege(collegeId);
        int hoursAlive = 0;
        if (college != null) {
            hoursAlive = college.getHoursAlive();
        }
        return(hoursToDate(hoursAlive));
    }

    static public Date hoursToDate(int hoursAlive) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.MONTH, 8);
        cal.set(Calendar.YEAR, 2018);
        cal.add(Calendar.DAY_OF_MONTH, hoursAlive/24);
        return cal.getTime();
    }

    static public int getDaysOpen(String collegeId) {
        CollegeModel college = new CollegeDao().getCollege(collegeId);
        int hoursAlive = 0;
        if (college != null) {
            hoursAlive =college.getHoursAlive();
        }
        return hoursAlive/24;
    }

    /**
     * Returns the current month (1 based) of the date in the college.
     */
    static public int getCollegeCurrentMonth(String collegeId) {
        Date date = getCollegeDate(collegeId);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int m = cal.get(Calendar.MONTH);
        int d = cal.get(Calendar.DAY_OF_MONTH);
        return cal.get(Calendar.MONTH) + 1;
    }

    /**
     * Returns the day of the date in the college.
     */
    static public int getCollegeCurrentDay(String collegeId) {
        Date date = getCollegeDate(collegeId);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * Sets college yearly tuition.
     *
     * @param collegeId college name
     * @return the college
     */
    public static CollegeModel updateCollegeTuition(String collegeId, int amount, PopupEventManager popupManager){
        CollegeModel college = CollegeDao.getCollege(collegeId);
        logger.info("Updating Tuition to: " + amount);
        if ((amount < 0) || (amount > 100000)) {
            popupManager.newPopupEvent(collegeId,"Invalid tuition", "Please enter a tuition between 0 and 100000 dollars.", "Ok", "done", "resources/images/money.jpg", "Money");
            return college;
        }

        college.setPreviousTuitionCost(college.getYearlyTuitionCost());
        college.setYearlyTuitionCost(amount);
        CollegeDao.saveCollege(college);

        calculateTuitionRating(collegeId);

        NewsManager.createNews(collegeId, college.getHoursAlive(), "Tuition Updated to: $" + amount, NewsType.FINANCIAL_NEWS, NewsLevel.GOOD_NEWS);

        return college;
    }

    private static void calculateTuitionRating(String collegeId) {
        CollegeModel college = CollegeDao.getCollege(collegeId);
        int rating = SimulatorUtilities.getRatingZeroToOneHundred(75000, 25000, college.getYearlyTuitionCost());
        logger.info("Rating was: " + rating);
        college.setYearlyTuitionRating(rating);
        CollegeDao.saveCollege(college);
    }

    static private void loadTips(String collegeId) {
        // Only the first tip should be set to true.
        TutorialManager.saveNewTip(collegeId, 0,"viewCollege", "Unlock features by getting more students to come to your college.", true, "LIBRARY.png");
        TutorialManager.saveNewTip(collegeId, 1,"viewCollege", "Grow your college by attracting new students to come.", false, "LIBRARY.png");
        TutorialManager.saveNewTip(collegeId, 2,"viewCollege", "The college earns money primarily from tuition.", false, "money.jpg");
        TutorialManager.saveNewTip(collegeId, 3,"viewCollege", "Keep your students happy if you want your college to grow.", false, "smile.png");
    }

    /**
     * Return true if the given college exists.
     *
     * @param collegeId college name
     * @return true if exists.
     */
    static public boolean doesCollegeExist(String collegeId) {
        try {
            CollegeDao.getCollege(collegeId);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static void updateCollegeMode(String collegeId, String mode) {
        CollegeModel college = CollegeDao.getCollege(collegeId);
        college.setMode(mode);
        CollegeDao.saveCollege(college);
    }

    static public boolean isMode(String collegeId, CollegeMode mode) {
        CollegeModel college = new CollegeDao().getCollege(collegeId);
        return (college.getMode() == mode);
    }

    static public int getGate(String collegeId) {
        CollegeModel college = new CollegeDao().getCollege(collegeId);
        return (college.getGate());
    }

    public static int getDaysUntilNextEvent(String collegeId) {
        CollegeModel college = CollegeDao.getCollege(collegeId);
        return college.getDaysUntilNextEvent();
    }

    public static void setDaysUntilNextEvent(String collegeId, int daysUntilNextEvent) {
        CollegeModel college = CollegeDao.getCollege(collegeId);
        college.setDaysUntilNextEvent(Math.max(0,daysUntilNextEvent));
        CollegeDao.saveCollege(college);
    }

    public static void recieveDepartmentPerformanceBonus(String collegeId, String departmentName, PopupEventManager popupManager){
        int award = (int)(1 + Math.random() * 10);
        CollegeModel college = CollegeDao.getCollege(collegeId);
        college.setAvailableCash(college.getAvailableCash() + (award) * 1000);
        popupManager.newPopupEvent(collegeId, "Academic Grant awarded", departmentName + " has been awarded an Academic grant of $" + award + " for their great work!", "ok", "done", "resources/images/money.jpg", "Department Award");
        // OK to not save to DAO because a save is coming after this call.
    }
}