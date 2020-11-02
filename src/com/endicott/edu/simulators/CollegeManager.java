package com.endicott.edu.simulators;

import com.endicott.edu.datalayer.*;
import com.endicott.edu.models.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Logger;
import java.util.Date;

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
        LoanModel testModel = new LoanModel(1000, 10, 100);
        college.getLoans().add(testModel);
        LoanModel testModel2 = new LoanModel(2000, 25, 250);
        college.getLoans().add(testModel2);
        System.out.println("ADDED MODELS");
        college.setAvailableCash(STARTUP_FUNDING);
        CollegeDao.saveCollege(college);

        // Each functional area/simulator in the college gets called to
        // take care of its start-the-college needs.
        // The order of this matters (example: a dorm is established, before students enter).
        NewsManager.createNews(collegeId, college.getCurrentDay(),"The college was established today.", NewsType.COLLEGE_NEWS, NewsLevel.GOOD_NEWS);

        logger.info("Establish buildings.");BuildingManager.establishCollege(collegeId, college);
        logger.info("Establish faculty.");FacultyManager.establishCollege(collegeId, college);
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
        FloodDao.deleteFlood(collegeId);
        NewsFeedDao.deleteNotes(collegeId);
        PlagueDao.deletePlagues(collegeId);
        SportsDao.deleteSports(collegeId);
        StudentDao.deleteStudents(collegeId);
        IdNumberGenDao.deleteIDs(collegeId);
        InventoryDao.deleteItem(collegeId);
        SnowDao.deleteSnowStorm(collegeId);
    }

    /**
     * Advance the clock x days.  Simulate all changes occurring during those days.
     * Time at the college is counted in hours.
     * All calculations are done in terms of hours.
     *
     * @param collegeId college name
     */
    static public CollegeModel advanceTimeByOneDay(String collegeId, PopupEventManager popupManager) {
        CollegeModel college = CollegeDao.getCollege(collegeId);

        // If there is a popup, we are not going to advance the day.
        // The pop must be cleared through the user inteface first.
        List<PopupEventModel> popupEvents = PopupEventDao.getPopupEvents(collegeId);
        //I COMMENTED THIS OUT BECAUSE POPUPS ARE NOT GOING AWAY, WHEN FIXED UNCOMMENT THIS
//        if (popupEvents == null || popupEvents.size() > 0) {
//            logger.info("Not advancing day because there are pop ups.");
//            return college;
//        }

        college.setHoursAlive(college.getHoursAlive() + 24);  // We are advancing x days.
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

        logger.info("AdvanceTime Faculty");
        FacultyManager.handleTimeChange(collegeId, hoursAlive, popupManager);
        DepartmentManager.handleTimeChange(collegeId, popupManager);
        PlayManager.handleTimeChange(collegeId, hoursAlive, popupManager);
        GateManager.handleTimeChange(collegeId, hoursAlive, popupManager);

        //Run the loans every 7 days/week
        if(college.getHoursAlive() % 169 == 0) {
            logger.info("AdvanceTime Loans");
            //Find the amount of money the user has to pay on their loans and add it up
            int total = 0;
            for(int i = 0; i < college.getLoans().size(); i++) {
                total += makeWeeklyPayment(college.getLoans().get(i));
                //Update the debt using the interest rate now!!!
            }

            //Pay off the loans, updating the college debt and their balance
            college.setDebt(college.getDebt()-total);
            college.setAvailableCash(college.getAvailableCash()-total);

            //Update the college credit for paying these loans
            int newCredit = updateCredit(total);
            college.setCredit(college.getCredit() + newCredit);
        }

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
            popupManager.newPopupEvent(collegeId,"Invalid tuition", "Please enter a tutuion between 0 and 100000 dollars.", "Ok", "done", "resources/images/money.jpg", "Money");
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

    /**
     * Creates the contract and places it in the college's loan list
     */
    static public void createContract(String collegeId) {
        System.out.println("Creating a contract");
        CollegeModel college = CollegeDao.getCollege(collegeId);
        LoanModel lm = college.getProposedLoan();   //Grab the saved loan
        System.out.println("This is the model of it:" + lm.getValue() + lm.getInterest() + lm.getWeeklyPayment());
        college.getLoans().add(lm);                 //Add it to the arraylist
        System.out.println("Here are all the loans now: " + college.getLoans());
        lm = new LoanModel(0, 0, 0);    //Make a default proposed loan
        college.setProposedLoan(lm);                //Set the proposed loan to the default again
        System.out.println("Just b4 we save, proposed loan is now empty see:" + college.getProposedLoan());
        CollegeDao.saveCollege(college);
    }

    /**
     * Function will start be generating an interest rate for the loan. This is based on how much a user borrows, their credit,
     * plus their current debt. Lowest range is 2% while the cap is 20% even if it should be more based upon the algorithm.
     * Finally, the function will also generate the average weekly payment the college must make to pay this off, which is also
     * influenced by credit score and the amount of debt the college already has.
     */
    //Make this just take in the college model and the amount instead?
    static public void calculateContract(int amount, String collegeId) {

        //Algorithm should likely be rebalanced but these are rough parameters of how each should impact the interest rate
        //Base percentage says there is 1% rate for every 15K taken out
        CollegeModel college = CollegeDao.getCollege(collegeId);
        LoanModel lm = college.getProposedLoan();
        lm.setValue(amount);    //First set the value of the contract
        lm.setInterest(amount/13000);

        //Factor in the amount of debt your college is already in
        //We will add an extra percent for every 50K the college is in debt right now
        double debtAddon = college.getDebt()/25000;
        lm.setInterest(lm.getInterest() + debtAddon);

        //Factor in credit score now
        //We will take off half a percent for every 50 points in the credit score
        int temp = college.getCredit()/75;
        double creditTakeoff = temp/2;
        lm.setInterest(lm.getInterest() + creditTakeoff);

        //Make sure it's between 2-20%
        Math.min(20, lm.getInterest());
        Math.max(2, lm.getInterest());

        //Calculate weekly payment below
        //We will first find a percentage they should pay based upon their credit, then calculate from there
        //Based on standard credit score ranges of bad-excellent
        int creditScore = college.getCredit();
        double percentPayment = 0;
        if(creditScore >= 300 && creditScore <= 579) {
            percentPayment = 4.0;
        }
        else if(creditScore >= 580 && creditScore <= 669) {
            percentPayment = 3.0;
        }
        else if(creditScore >= 670 && creditScore <= 739) {
            percentPayment =  2.0;
        }
        else if(creditScore >= 740 && creditScore <= 799) {
            percentPayment = 1.5;
        }
        else if(creditScore >= 800 && creditScore <= 850) {
            percentPayment = 1.0;
        }

        //Every 50K we will add another .25 percent to the percentage
        temp = amount/50000;
        for(int i = 0; i < temp; i++) {
            percentPayment += 0.25;
        }

        lm.setWeeklyPayment(Math.round(amount*(percentPayment/100)));

        college.setProposedLoan(lm);
        CollegeDao.saveCollege(college);
    }

    /**
     * Function will determine how much needs to deducted from the loan, and remove it, also returning it for the college
     * so it can remove it from the college balance
     *
     * @return Returns whether or not this loan has been fully paid off or not
     */
    static public double makeWeeklyPayment(LoanModel lm) {
        double num = lm.getValue()*(100.0/lm.getWeeklyPayment());  //First grab the double value of what the user must pay
        int valueToRemove = (int)num;                              //Cast it to an int since the college doesn't have cents counted
        lm.setValue(lm.getValue() - valueToRemove);                //Subtract it from this loan's value

        return valueToRemove;
    }

    /**
     * Function allows user to pay the debt on this loan
     *
     * @param amount The amount the user wants to pay on the loan
     *
     * @return Returns the increase in credit score to update the credit with
     */
    static public int makePayment(int amount, LoanModel lm, int credit) {
        lm.setValue(lm.getValue() - amount);
        credit += updateCredit(amount);
        return credit;
    }

    /**
     * Function takes an amount of money paid on loans and increases the college's credit
     *
     * @param amount The amount the user just paid on one/all their loans
     * @return The increase to credit
     */
    static public int updateCredit(int amount) {
        int increase = (amount/1000);   //For every thousand dollars the user paid on their loans, they get 1 point
        return increase;
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

    public static void recieveDepartmentPerformanceBonus(String collegeId, CollegeModel college, String departmentName, PopupEventManager popupManager){
        college.setAvailableCash(college.getAvailableCash() + 10000);
        //popupManager.newPopupEvent(collegeId, "Department Award", departmentName + " has won an award for it's academic success!", "ok", "done", "resources/images/money.jpg", "Department Award");
    }
}