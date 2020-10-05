package com.endicott.edu.models;
import java.util.ArrayList;

public class HealthCenterModel extends BuildingModel {

    //inherits from BuildingModel
    public HealthCenterModel(String name){
        super(name, BuildingType.health().getType());
        this.getUpgrades().add(new Upgrades("hcSanitation", 0, 0));
        this.getUpgrades().add(new Upgrades("hcHospitality", 0, 0));
        this.getUpgrades().add(new Upgrades("hcHours", 0, 0));
        this.getUpgrades().add(new Upgrades("hcNumRooms", 0, 0));
        this.getUpgrades().add(new Upgrades("hcRoomQuality", 0, 0));
    }

    public void increaseHealthCenterLevel(Upgrades upgrade) {
        for(int i = 0; i < upgrade.getMaxLevel(); i++) {
            if(upgrade.getName()==getUpgrades().get(i).getName())
                getUpgrades().get(i).increaseLevel();
        }
    }

    public void decreaseHealthCenterLevel(Upgrades upgrade) {
        for(int i = 0; i > 0; i++) {
            if(upgrade.getName()==getUpgrades().get(i).getName())
                getUpgrades().get(i).decreaseLevel();
        }
    }
}
