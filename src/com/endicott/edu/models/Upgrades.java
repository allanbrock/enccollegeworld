package com.endicott.edu.models;

import java.util.ArrayList;

public class Upgrades {
    private String name;
    private int cost;
    private int happinessModifier;

    public Upgrades(String name, int cost, int happinessModifier){
        this.name = name;
        this.cost = cost;
        this.happinessModifier = happinessModifier;
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
}
