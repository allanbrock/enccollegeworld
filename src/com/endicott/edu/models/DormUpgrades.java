package com.endicott.edu.models;

import java.util.ArrayList;

public class DormUpgrades {
    private static ArrayList<Upgrades> dormUpgrades;
    private Upgrades airConditioning= new Upgrades("Air Conditioning", 10000, 50);
    private Upgrades plumbing= new Upgrades("Plumbing", 3200, 50);
    private Upgrades commonRooms = new Upgrades("CommonRooms", 5000, 50);
    public static final int maxLevel = 5;

    private DormUpgrades(){
        dormUpgrades = new ArrayList<Upgrades>();
        dormUpgrades.add(airConditioning);
        dormUpgrades.add(plumbing);
        dormUpgrades.add(commonRooms);
    }

    public static ArrayList<Upgrades> getUpgrades(){
        return dormUpgrades;
    }
}
