package com.endicott.edu.models;

import java.util.ArrayList;

public class AcademicCenterModel extends BuildingModel {
    //inherits from BuildingModel
    public AcademicCenterModel(String name, int numStudents, String size){
        super(name, numStudents, BuildingType.academic().getType(), size);
        this.getUpgrades().add(new Upgrades("Tables",0, 0));
        this.getUpgrades().add(new Upgrades("Air Conditioning", 0, 0));
        this.getUpgrades().add(new Upgrades("Vending Machines", 0, 0));
        this.getUpgrades().add(new Upgrades("Projectors", 0, 0));
        this.getUpgrades().add(new Upgrades("Seats", 0, 0));
    }

    public void upgradeAcademicCenter(Upgrades upgrade) {
        for(int i = 0; i < upgrade.getMaxLevel(); i++) {
            if(upgrade.getName()==getUpgrades().get(i).getName())
                getUpgrades().get(i).increaseLevel();
        }
    }

    public void downgradeAcademicCenter(Upgrades upgrade) {
        for(int i = 0; i > 0; i++) {
            if(upgrade.getName()==getUpgrades().get(i).getName())
                getUpgrades().get(i).decreaseLevel();
        }
    }
}
