package com.endicott.edu.simulators;

import com.endicott.edu.datalayer.CollegeDao;
import com.endicott.edu.models.CollegeModel;

/**
 * Responsible for handling finances at the college.
 */
public class Accountant {
    static final int MINIMUM_BALANCE = 10000;

    /**
     * Pay the given bill deducting the money from available
     * cash.
     *
     * @param collegeId  college name
     * @param message description of bill
     * @param amount bill amount
     */
    static public void payBill(String collegeId, String message, int amount) {
        CollegeDao collegeDao = new CollegeDao();

        CollegeModel college = collegeDao.getCollege(collegeId);
        int newBalance = Math.max(college.getAvailableCash() - amount, MINIMUM_BALANCE);
        college.setAvailableCash(newBalance);
        collegeDao.saveCollege(college);
        NewsManager.createFinancialNews(collegeId,college.getHoursAlive(), message, - amount);
    }

    /**
     * Increase the available cash at the college.
     *
     * @param collegeId
     * @param message description of why money was received.
     * @param amount
     */
    static public void receiveIncome(String collegeId, String message, int amount) {
        CollegeDao collegeDao = new CollegeDao();

        CollegeModel college = collegeDao.getCollege(collegeId);
        college.setAvailableCash(college.getAvailableCash() + amount);
        collegeDao.saveCollege(college);
        NewsManager.createFinancialNews(collegeId, college.getHoursAlive(),message, + amount);
    }

    /**
     * Get the balance at the college.
     *
     * @param collegeId
     * @return
     */
    public static int getAvailableCash(String collegeId) {
        CollegeDao collegeDao = new CollegeDao();
        CollegeModel college = collegeDao.getCollege(collegeId);
        return college.getAvailableCash();
    }
}

