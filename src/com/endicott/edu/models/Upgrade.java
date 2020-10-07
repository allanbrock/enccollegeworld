package com.endicott.edu.models;

import com.sun.org.apache.xpath.internal.operations.Bool;

import java.io.Serializable;
import java.util.ArrayList;

public class Upgrade implements Serializable {
    protected String name;
    protected int cost;
    protected int happinessModifier;
    protected int curretLevel;
    protected String description;
    protected ArrayList<UpgradeEvents> events;
    protected int maxLevel;
    protected boolean isInteriorChange;

    public Upgrade(String name, int cost, int happinessModifier){
        this.name = name;
        this.cost = cost;
        this.happinessModifier = happinessModifier;
        this.curretLevel = 0;
    }

    public Upgrade(String name, int cost, int happinessModifier, String description, ArrayList<UpgradeEvents> events, int maxLevel, boolean typeOfChange){
        this.name = name;
        this.cost = cost;
        this.happinessModifier = happinessModifier;
        this.curretLevel = 0;
        this.description = description;
        this.events = events;
        this.maxLevel = maxLevel;
        this.isInteriorChange = typeOfChange;
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

    public String getDescription(){
        return this.description;
    }
    //gives level to the upgrades, each building may have a max level of upgrade
    //each level increase should give more stats eg. student happiness, faculty happiness, college rating, etc
    public void increaseLevel(){
        this.curretLevel++;
    }

    public void decreaseLevel(){this.curretLevel--;}

    public int getMaxLevel(){
        return this.maxLevel;
    }

    public Boolean getTypeOfChange(){ return this.isInteriorChange; }
}
