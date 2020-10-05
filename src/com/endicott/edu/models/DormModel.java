package com.endicott.edu.models;

import java.util.ArrayList;

public class DormModel extends BuildingModel {
    //inherits from BuildingModel
    public DormModel(String name, int numStudents, String size){
        super(name, numStudents, BuildingType.dorm().getType(), size);
        this.getUpgrades().add(new Upgrades("Air Conditioning", 10000, 50));
        this.getUpgrades().add(new Upgrades("Plumbing", 3200, 50));
        this.getUpgrades().add(new Upgrades("CommonRooms", 5000, 50));
    }

    public void upgradeDormModel(Upgrades upgrade) {
        for(int i = 0; i < upgrade.getMaxLevel(); i++) {
            if(upgrade.getName()==getUpgrades().get(i).getName())
                getUpgrades().get(i).increaseLevel();
        }
    }

    public void downgradeDormModel(Upgrades upgrade) {
        for(int i = 0; i > 0; i++) {
            if(upgrade.getName()==getUpgrades().get(i).getName())
                getUpgrades().get(i).decreaseLevel();
        }
    }
}
