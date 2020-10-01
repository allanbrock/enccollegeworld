package com.endicott.edu.models;
import java.util.ArrayList;

public class DiningHallUpgrades {
    private static ArrayList<Upgrades> diningUpgrades;
    private Upgrades foodVariety= new Upgrades("Food Variety", 10000, 50);
    private Upgrades cutleries= new Upgrades("Cutleries", 3200, 50);
    private Upgrades staff = new Upgrades("Staff", 5000, 50);
    public static final int maxLevel = 10;

    private DiningHallUpgrades(){
        diningUpgrades = new ArrayList<Upgrades>();
        diningUpgrades.add(foodVariety);
        diningUpgrades.add(cutleries);
        diningUpgrades.add(staff);
    }

    public static ArrayList<Upgrades> getUpgrades(){
        return diningUpgrades;
    }
}
