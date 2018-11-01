package com.endicott.edu.simulators;

import com.endicott.edu.datalayer.*;
import com.endicott.edu.models.CollegeModel;
import com.endicott.edu.models.NewsLevel;
import com.endicott.edu.models.NewsType;

import javax.servlet.http.HttpSession;
import java.util.Calendar;
import java.util.logging.Logger;
import java.util.Date;

/**
 * The CollegeManager is responsible for simulating all overall college functions,
 * such as creating a college, deleting the college, and is responsible for
 * providing college information (retention rate, current tuition, etc.)
 */

public class CollegeManager {
    static public final int STARTUP_FUNDING = 200000;  // Amount of money initially in college bank account.

    /**
     * Creates a new college.
     *
     * @param collegeId college name
     * @return the college
     */
    static public CollegeModel establishCollege(String collegeId) {
        loadTips(collegeId);
        CollegeDao collegeDao = new CollegeDao();
        Logger logger = Logger.getLogger("CollegeManager");
        logger.info("Establishing the college");
        CollegeModel college = null;

        // See if there already is a college for this run.
        // We don't expect this, but if so, just return it.
        try {
            college = collegeDao.getCollege(collegeId);
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
        college.setAvailableCash(STARTUP_FUNDING);
        collegeDao.saveCollege(college);

        // Each functional area/simulator in the college gets called to
        // take care of its start-the-college needs.
        // The order of this matters (example: a dorm is established, before students enter).
        NewsManager.createNews(collegeId, college.getCurrentDay(),"The college was established today.", NewsType.COLLEGE_NEWS, NewsLevel.GOOD_NEWS);

        BuildingManager.establishCollege(collegeId, college);
        FacultyManager.establishCollege(collegeId);

        StudentManager studentManager = new StudentManager();
        studentManager.establishCollege(collegeId);

        SportManager.establishDefaultSportsTeams(collegeId);

        PlagueManager.establishCollege(collegeId);
        FloodManager.establishCollege(collegeId);

        EventManager.establishCollege(collegeId);

        GateManager.establishCollege(collegeId);

        InventoryManager inventoryManager = new InventoryManager();
        inventoryManager.establishCollege(collegeId);

        return college;
    }

    /**
     * Deletes a college.  Removes all storage associated with the college.
     *
     * @param collegeId college name
     */
    static public void sellCollege(String collegeId) {
        CollegeDao.deleteCollege(collegeId);
        BuildingDao.deleteBuilding(collegeId);
        FacultyDao.removeAllFaculty(collegeId);
        FloodDao.deleteFloods(collegeId);
        NewsFeedDao.deleteNotes(collegeId);
        PlagueDao.deletePlagues(collegeId);
        SportsDao.deleteSports(collegeId);
        StudentDao.deleteStudents(collegeId);
        IdNumberGenDao.deleteIDs(collegeId);
        InventoryDao.deleteItem(collegeId);
    }

    /**
     * Advance the clock x days.  Simulate all changes occurring during those days.
     * Time at the college is counted in hours.
     * All calculations are done in terms of hours.
     *
     * @param collegeId college name
     */
    static public CollegeModel advanceTimeByOneDay(String collegeId, PopupEventManager popupManager) {
        CollegeDao collegeDao = new CollegeDao();


        // Advance time college has been alive.
        CollegeModel college = collegeDao.getCollege(collegeId);
        college.setHoursAlive(college.getHoursAlive() + 24);  // We are advancing x days.
        collegeDao.saveCollege(college);  // Notice that after setting fields in college we need to save.

        // How many hours has the college been alive (counting from hour 0).
        int hoursAlive = college.getHoursAlive();

        // Tell all the simulators about the time change.
        // Each one takes care of what happened since they were
        // last called.  They are given the current time.
        PlagueManager plagueManager = new PlagueManager();
        plagueManager.handleTimeChange(collegeId, hoursAlive, popupManager);

        BuildingManager buildingManager = new BuildingManager();
        buildingManager.handleTimeChange(collegeId, hoursAlive, popupManager);

        SportManager sportManager = new SportManager();
        sportManager.handleTimeChange(collegeId, hoursAlive, popupManager);

        StudentManager studentManager = new StudentManager();
        studentManager.handleTimeChange(collegeId, hoursAlive, popupManager);

        FloodManager floodManager = new FloodManager();
        floodManager.handleTimeChange(collegeId, hoursAlive, popupManager);

        FacultyManager.handleTimeChange(collegeId, hoursAlive, popupManager);

        FireManager fireManager = new FireManager();
        fireManager.handleTimeChange(collegeId, hoursAlive, popupManager);

        // After all the simulators are run, there is a final
        // calculation of the college statistics.
        calculateStatisticsAndRatings(collegeId);

        if (college.getAvailableCash() <= 0) {
            popupManager.newPopupEvent("Bankrupt!", "You ran out of money! Better luck next time!",
                    "Return to Main Menu", "returnToWelcome", "resources/images/bankrupt.jpg",
                    "Insert empty Wallet Picture Here");
        }

        return college;
    }

    /**
     * Returns current Date of college.
     *
     * @param collegeId college name
     * @return the Date of the college
     */
    static public Date getCollegeDate(String collegeId) {
        CollegeModel college = new CollegeDao().getCollege(collegeId);
        int hoursAlive = college.getHoursAlive();

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.MONTH, 8);
        cal.set(Calendar.YEAR, 2018);
        cal.add(Calendar.DAY_OF_MONTH, hoursAlive/24);

        return cal.getTime();
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
    public static CollegeModel updateCollegeTuition(String collegeId, int amount){
        CollegeDao cao = new CollegeDao();

        CollegeModel college = cao.getCollege(collegeId);
        college.setYearlyTuitionCost(amount);
        cao.saveCollege(college);

        calculateStatisticsAndRatings(collegeId);

        NewsManager.createNews(collegeId, college.getHoursAlive(),"Tuition Updated to: $" + amount, NewsType.FINANCIAL_NEWS, NewsLevel.GOOD_NEWS);

        StudentManager studentManager = new StudentManager();
        studentManager.calculateStatistics(collegeId, false);

        return college;
    }

    // TODO: TEMPORARY FUNCTION, WILL BE REMOVED AFTER TESTING/SPRINT
    public static CollegeModel bankruptCollege(String collegeId){
        CollegeDao cao = new CollegeDao();

        CollegeModel college = cao.getCollege(collegeId);
        college.setAvailableCash(0);
        cao.saveCollege(college);

        calculateStatisticsAndRatings(collegeId);

        StudentManager studentManager = new StudentManager();
        studentManager.calculateStatistics(collegeId, false);

        return college;
    }

    private static void calculateStatisticsAndRatings(String collegeId) {
        calculateTuitionRating(collegeId);
    }

    private static void calculateTuitionRating(String collegeId) {
        CollegeDao collegeDao = new CollegeDao();
        CollegeModel college = collegeDao.getCollege(collegeId);

        int rating = SimulatorUtilities.getRatingZeroToOneHundred(60000, 24000, college.getYearlyTuitionCost());
        college.setYearlyTuitionRating(rating);
        collegeDao.saveCollege(college);
    }

    static private void loadTips(String collegeId) {
        TutorialManager.saveNewTip(collegeId, 0,"viewCollege", "Welcome to College Simulator!", true);
        TutorialManager.saveNewTip(collegeId, 1,"viewCollege", "Don't spend all your money at once.", false);
    }

    /**
     * Return true if the given college exists.
     *
     * @param collegeId college name
     * @return true if exists.
     */
    static public boolean doesCollegeExist(String collegeId) {
        CollegeDao collegeDao = new CollegeDao();

        try {
            collegeDao.getCollege(collegeId);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}