package com.endicott.edu.simulators;

import com.endicott.edu.datalayer.*;
import com.endicott.edu.models.CollegeModel;
import com.endicott.edu.models.NewsLevel;
import com.endicott.edu.models.NewsType;

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

        PlagueManager.establishCollege(collegeId);
        FloodManager.establishCollege(collegeId);

        EventManager.establishCollege(collegeId);

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
    }

    /**
     * Advance the clock x days.  Simulate all changes occurring during those days.
     * Time at the college is counted in hours.
     * All calculations are done in terms of hours.
     *
     * @param collegeId college name
     * @param dayCount  number of days
     */
    static public CollegeModel iterateTime(String collegeId, int dayCount) {
        CollegeDao collegeDao = new CollegeDao();

        // Advance time college has been alive.
        CollegeModel college = collegeDao.getCollege(collegeId);
        college.setHoursAlive(college.getHoursAlive() + (24*dayCount));  // We are advancing x days.
        collegeDao.saveCollege(college);  // Notice that after setting fields in college we need to save.

        // How many hours has the college been alive (counting from hour 0).
        int hoursAlive = college.getHoursAlive();

        // Tell all the simulators about the time change.
        // Each one takes care of what happened since they were
        // last called.  They are given the current time.

        PlagueManager plagueManager = new PlagueManager();
        plagueManager.handleTimeChange(collegeId, hoursAlive);

        BuildingManager buildingManager = new BuildingManager();
        buildingManager.handleTimeChange(collegeId, hoursAlive);

        SportManager sportManager = new SportManager();
        sportManager.handleTimeChange(collegeId, hoursAlive);

        StudentManager studentManager = new StudentManager();
        studentManager.handleTimeChange(collegeId, hoursAlive);

        FloodManager floodManager = new FloodManager();
        floodManager.handleTimeChange(collegeId, hoursAlive);

        FacultyManager.handleTimeChange(collegeId,hoursAlive);


        // After all the simulators are run, there is a final
        // calculation of the college statistics.
        calculateStatisticsAndRatings(collegeId);

        return college;
    }

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
        studentManager.calculateStatistics(collegeId);

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