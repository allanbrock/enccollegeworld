package com.endicott.edu.models;

import java.util.ArrayList;

public class DiningHallModel extends BuildingModel {
    //inherits from BuildingModel
    public static final int maxLevel = 10;
    private ArrayList<Upgrades> upgrades;

    public DiningHallModel(String name, int numStudents, String size){
        super(name, numStudents, BuildingType.dining().getType(), size);
        this.upgrades = DiningHallUpgrades.getUpgrades();
    }

    public void upgradeDiningHallModel(Upgrades upgrade) {
        for(int i = 0; i < maxLevel; i++) {
            if(upgrade.getName()==upgrades.get(i).getName())
                upgrades.get(i).increaseLevel();
        }
    }

    public void downgradeDiningHallModel(Upgrades upgrade) {
        for(int i = 0; i < maxLevel; i++) {
            if(upgrade.getName()==upgrades.get(i).getName())
                upgrades.get(i).decreaseLevel();
        }
    }
}
