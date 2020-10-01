package com.endicott.edu.models;

import java.util.ArrayList;

public class Upgrades {
    private String name;
    private int cost;
    private int happinessModifier;
    private int level;

    public Upgrades(String name, int cost, int happinessModifier){
        this.name = name;
        this.cost = cost;
        this.happinessModifier = happinessModifier;
        this.level = 0;
    }

    public String getName(){
        return this.name;
    }

    public int getCost(){
        return this.cost;
    }

    public int getHappinessModifier(){
        return this.happinessModifier;
    }

    //gives level to the upgrades, each building may have a max level of upgrade
    //each level increase should give more stats eg. student happiness, faculty happiness, college rating, etc
    public void increaseLevel(){
        this.level++;
    }
}
