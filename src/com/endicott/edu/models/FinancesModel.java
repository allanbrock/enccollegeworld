package com.endicott.edu.models;

import java.io.Serializable;
import java.util.ArrayList;

public class FinancesModel implements Serializable {
    private int tuition;
    private int value;
    private ArrayList<Integer> tuitionCosts = new ArrayList<Integer>();
    private ArrayList<Integer> valueRatings = new ArrayList<Integer>();

    public FinancesModel(){
        tuition = 0;
        value = 0;
    }

    public int getTuition() {
        return tuition;
    }

    public void setTuition(int tuition) {
        this.tuition = tuition;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public ArrayList getTuitionCosts(){
        return tuitionCosts;
    }

    public ArrayList<Integer> getValueRatings() {
        return valueRatings;
    }

}
