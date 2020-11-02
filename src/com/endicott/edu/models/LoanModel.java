package com.endicott.edu.models;

import java.util.ArrayList;

//Outline of the UI for this: https://builderx.io/app/j4wlwx6u7u0oooswc4w400o4ckc84k
//Am I missing anything important for logic or in the UI?
public class LoanModel {
    private int value = 0;             //The amount the loan was
    private double interest = 0;       //The percentage of tax on the loan
    private double weeklyPayment = 0;  //The amount of money the college must pay every week

    //Empty constructor assumes that these values will be assigned after creation on their own
    public LoanModel() {

    }

    //Constructor
    public LoanModel(int cValue, int cInterest, int cPayment) {
        this.value = cValue;
        this.interest = cInterest;
        this.weeklyPayment = cPayment;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void setInterest(double interest) {
        this.interest = interest;
    }

    public void setWeeklyPayment(double amount) {
        this.weeklyPayment = amount;
    }

    public int getValue() {
        return this.value;
    }

    public double getInterest() {
        return this.interest;
    }

    public double getWeeklyPayment() {
        return this.weeklyPayment;
    }

}
