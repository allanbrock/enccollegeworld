package com.endicott.edu.models;

import java.util.ArrayList;

public class DormModel extends BuildingModel {
    //inherits from BuildingModel
    private ArrayList<Upgrades> upgrades;
    public static final int maxLevel = 3;

    public DormModel(String name, int numStudents, String size){
        super(name, numStudents, BuildingType.dorm().getType(), size);
        this.upgrades = DormUpgrades.getUpgrades();
    }

    public void upgradeDormModel(Upgrades upgrade) {
        for(int i = 0; i < maxLevel; i++) {
            if(upgrade.getName()==upgrades.get(i).getName())
                upgrades.get(i).increaseLevel();
        }
    }

    public void downgradeDormModel(Upgrades upgrade) {
        for(int i = 0; i < maxLevel; i++) {
            if(upgrade.getName()==upgrades.get(i).getName())
                upgrades.get(i).decreaseLevel();
        }
    }
}
