package com.endicott.edu.simulators;

import com.endicott.edu.datalayer.CollegeDao;
import com.endicott.edu.models.CollegeModel;
import com.endicott.edu.models.LoanModel;
import com.endicott.edu.models.NewsLevel;
import com.endicott.edu.models.NewsType;

public class FinanceManager {

    public void handleTimeChange(String collegeId) {
        CollegeModel college = CollegeDao.getCollege(collegeId);
        //Find the amount of money the user has to pay on their loans and add it up
        int total = 0;
        for(int i = 0; i < college.getLoans().size(); i++) {
            total += makeWeeklyPayment(college.getLoans().get(i));
            addInterest(college.getLoans().get(i), collegeId);
            NewsManager.createFinancialNews(collegeId, college.getHoursAlive(), "Weekly debt paid: $", -1*total);
        }

        //Pay off the loans, updating the college debt and their balance
        college.setDebt(college.getDebt()-total);
        college.setAvailableCash(college.getAvailableCash()-total);

        //Check to see if any of the loans had been paid off this week
        checkLoans(collegeId);

        //Update the college credit for paying these loans
        int newCredit = updateCredit(total);
        college.setCredit(college.getCredit() + newCredit);
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
        lm.setInterest(Math.min(20, lm.getInterest()));
        lm.setInterest(Math.max(2, lm.getInterest()));

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

        //Check to see if the user has the cash to make this payment in the first place
        //They can't be allowed to go in debt to pay off debt (only when forced to pay weekly dues)
        if(college.getAvailableCash() < amount) {
            //DON'T MAKE THE PAYMENT THEY DON'T HAVE THE FUNDS
            return;
        }

        //Check to see if the user inputted to high a value to pay off the loan
        if(lm.getValue() < amount) {
            amount = lm.getValue();
        }

        lm.setValue(lm.getValue() - amount);    //Remove the amount of cash from the specific loan
        college.setAvailableCash(college.getAvailableCash()-amount);    //Remove the cash from the college balance
        college.setDebt(college.getDebt()-amount);        //Remove the cash from the total college debt
        NewsManager.createFinancialNews(collegeId, college.getHoursAlive(), "Payment to loans: $", -1*amount);
        checkLoans(collegeId);            //Check to see if any loans are paid off
        college.getExpensesGraph().setLoans(amount);
        college.getExpensesGraph().calculateExpenses();
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
}
