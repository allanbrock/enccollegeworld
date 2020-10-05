package com.endicott.edu.models;

import java.util.ArrayList;

public class DiningHallModel extends BuildingModel {
    //inherits from BuildingModel
    private Upgrades foodVariety= new Upgrades("Food Variety", 10000, 50);
    private Upgrades cutleries= new Upgrades("Cutleries", 3200, 50);
    private Upgrades staff = new Upgrades("Staff", 5000, 50);

    public DiningHallModel(String name, int numStudents, String size){
        super(name, numStudents, BuildingType.dining().getType(), size);
        this.getUpgrades().add(foodVariety);
        this.getUpgrades().add(cutleries);
        this.getUpgrades().add(staff);
    }

    public void upgradeDiningHallModel(Upgrades upgrade) {
        for(int i = 0; i < upgrade.getMaxLevel(); i++) {
            if(upgrade.getName()==getUpgrades().get(i).getName())
                getUpgrades().get(i).increaseLevel();
        }
    }

    public void downgradeDiningHallModel(Upgrades upgrade) {
        for(int i = 0; i > 0; i++) {
            if(upgrade.getName()==getUpgrades().get(i).getName())
                getUpgrades().get(i).decreaseLevel();
        }
    }


}
