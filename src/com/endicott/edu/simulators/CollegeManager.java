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
        college.setAvailableCash(STARTUP_FUNDING);
        CollegeDao.saveCollege(college);

        // Each functional area/simulator in the college gets called to
        // take care of its start-the-college needs.
        // The order of this matters (example: a dorm is established, before students enter).
        NewsManager.createNews(collegeId, college.getCurrentDay(),"The college was established today.", NewsType.COLLEGE_NEWS, NewsLevel.GOOD_NEWS);

        logger.info("Establish buildings.");BuildingManager.establishCollege(collegeId, college);
        logger.info("Establish academics.");AcademicsManager.establishCollege(collegeId);
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
        logger.info("Establish admissions.");AdmissionsManager.establishCollege(collegeId);


        CollegeRating collegeTraits = new CollegeRating();
        collegeTraits.handleTimeChange(collegeId);
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
    static public CollegeModel advanceTimeByOneDay(String collegeId, PopupEventManager popupManager) {
        CollegeModel college = CollegeDao.getCollege(collegeId);

        // If there is a popup, we are not going to advance the day.
        // The pop must be cleared through the user interface first.
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

        logger.info("AdvanceTime Admissions");
        AdmissionsManager.handleTimeChange(collegeId, hoursAlive, popupManager);

        logger.info("AdvanceTime Academics");
        AcademicsManager.handleTimeChange(collegeId, hoursAlive, popupManager);


        logger.info("AdvanceTime Faculty");
        FacultyManager.handleTimeChange(collegeId, hoursAlive, popupManager);
        DepartmentManager.handleTimeChange(collegeId, popupManager);
        PlayManager.handleTimeChange(collegeId, hoursAlive, popupManager);
        GateManager.handleTimeChange(collegeId, hoursAlive, popupManager);

        logger.info("AdvanceTime College Traits");
        CollegeRating collegeTraits = new CollegeRating();
        collegeTraits.handleTimeChange(collegeId);

        //Run the loans every week instead of day
        if(college.getHoursAlive() % 169 == 0) {
            logger.info("AdvanceTime Loans");
            //Find the amount of money the user has to pay on their loans and add it up
            int total = 0;
            for(int i = 0; i < college.getLoans().size(); i++) {
                total += makeWeeklyPayment(college.getLoans().get(i));
                addInterest(college.getLoans().get(i), collegeId);
                NewsManager.createNews(collegeId, college.getHoursAlive(), "Weekly debt paid: $" + total, NewsType.FINANCIAL_NEWS, NewsLevel.BAD_NEWS);
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

    /**
     * Function takes the user's proposed loan and "accepts" it by putting it into the array. The proposed loan now
     * goes back to the default value after the additions of the debt/money
     *
     * @param collegeId The ID of the college in use
     */
    static public void createContract(String collegeId) {
        CollegeModel college = CollegeDao.getCollege(collegeId);
        LoanModel lm = college.getProposedLoan();   //Grab the saved loan
        college.getLoans().add(lm);                 //Add it to the arraylist
        college.setDebt(college.getDebt() + lm.getValue());    //Add the debt to the total college debt
        college.setAvailableCash(college.getAvailableCash() + lm.getValue()); //Add the money to the college balance
        lm = new LoanModel(0, 0, 0);    //Make a default proposed loan
        college.setProposedLoan(lm);                //Set the proposed loan to the default again
        CollegeDao.saveCollege(college);
    }

    /**
     * Function will generate both the interest rate and predicted weekly payment on the loan the user is
     * considering to take out. NOTE: This is not adding the contract, instead calculating what the contact would look
     * like for the user, letting them determine if they want to take out this loan or not
     *
     * @param amount The amount the user is considering to take out
     * @param collegeId The id of the college currently in use
     */
    static public void calculateContract(int amount, String collegeId) {
        //Feel free to balance numbers and change calculation algorithms, this is just a start for how they could be calculated
        CollegeModel college = CollegeDao.getCollege(collegeId);
        LoanModel lm = college.getProposedLoan();
        lm.setValue(amount);    //First set the value of the contract

        //Base percentage: 13K equals a percent on the rate
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

        //Make sure it's between 2-20% (Change these if too high or low as caps)
        Math.min(20, lm.getInterest());
        Math.max(2, lm.getInterest());

        //Calculate weekly payment below
        //We will first find a percentage they should pay based upon their credit, then calculate from there
        //Based on standard credit score ranges of bad-excellent online
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

        //Now take the amount they want a loan of and calculate how much based upon that percentage
        lm.setWeeklyPayment((int)(amount*(percentPayment/100)));

        college.setProposedLoan(lm);
        CollegeDao.saveCollege(college);
    }

    /**
     * Function will determine how much needs to deducted from the loan, and remove it, also returning it for the college
     * so it perform other operations
     *
     * @param lm The Loan that will be deducted from
     *
     * @return Returns the value that will be taken out of the college's debt total and balance
     */
    static public double makeWeeklyPayment(LoanModel lm) {
        int valueToRemove = lm.getWeeklyPayment();             //Find out how much many is due
        lm.setValue(lm.getValue() - valueToRemove);           //Subtract it from this loan's value
        return valueToRemove;                                 //Return the amount
    }

    /**
     * Function allows user to pay the debt on this loan, it'll also run a check to see if the loan is fully paid off
     *
     * @param amount The amount the user wants to pay on the loan
     *
     */
    static public void makePayment(String collegeId, int amount, int loanNumber) {
        CollegeModel college = CollegeDao.getCollege(collegeId);
        LoanModel lm = college.getLoans().get(loanNumber);
        lm.setValue(lm.getValue() - amount);    //Remove the amount of cash from the specific loan
        college.setAvailableCash(college.getAvailableCash()-amount);    //Remove the cash from the college balance
        college.setDebt(college.getDebt()-amount);        //Remove the cash from the total college debt
        NewsManager.createNews(collegeId, college.getHoursAlive(), "Payment to loans: $" + amount, NewsType.FINANCIAL_NEWS, NewsLevel.BAD_NEWS);
        checkLoans(collegeId);            //Check to see if any loans are paid off
        CollegeDao.saveCollege(college);
    }

    /**
     * Function will add the amount of money that the interest rate determines. Called after a weekly payment is made
     *
     * @param lm The Loan that will have the interest added
     * @param collegeId The ID of the college in use
     */
    static public void addInterest(LoanModel lm, String collegeId) {
        int amount = 0;     //Original amount of interest
        amount = (int)(lm.getValue()*(lm.getInterest()/100));   //Find the amount of interest to add on
        lm.setValue(lm.getValue() + amount);                //Put that amount into the loan
        CollegeModel college = CollegeDao.getCollege(collegeId);
        college.setDebt(college.getDebt() + amount);        //Also put that amount into the total debt of the college
        CollegeDao.saveCollege(college);
    }

    /**
     * Function takes an amount of money paid on loans and increases the college's credit
     *
     * @param amount The amount the user just paid on one/all their loans
     *
     * @return The increase to credit for the collegeManager to use
     */
    static public int updateCredit(int amount) {
        int increase = (amount/1000);   //For every thousand dollars the user paid on their loans, they get 1 point
        return increase;
    }

    /**
     * Checks all the loans the college has to see if the player has paid one or more off
     *
     * @param collegeId The ID of the college in use
     */
    static public void checkLoans(String collegeId) {
        CollegeModel college = CollegeDao.getCollege(collegeId);
        for(int i = 0; i < college.getLoans().size(); i++) {
            if(college.getLoans().get(i).getValue() <= 0) {
                college.getLoans().remove(i);
                NewsManager.createNews(collegeId, college.getHoursAlive(), "Your college has paid off a loan!", NewsType.FINANCIAL_NEWS, NewsLevel.GOOD_NEWS);
            }
        }
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