package com.endicott.edu.models;

public class HealthCenterModel extends BuildingModel {

    //inherits from BuildingModel
    public HealthCenterModel(String name){
        super(name, BuildingType.health().getType());
        this.getUpgrades().add(new Upgrade("hcSanitation", 0, 0));
        this.getUpgrades().add(new Upgrade("hcHospitality", 0, 0));
        this.getUpgrades().add(new Upgrade("hcHours", 0, 0));
        this.getUpgrades().add(new Upgrade("hcNumRooms", 0, 0));
        this.getUpgrades().add(new Upgrade("hcRoomQuality", 0, 0));
    }

    public void increaseHealthCenterLevel(Upgrade upgrade) {
        for(int i = 0; i < upgrade.getMaxLevel(); i++) {
            if(upgrade.getName()==getUpgrades().get(i).getName())
                getUpgrades().get(i).increaseLevel();
        }
    }

    public void decreaseHealthCenterLevel(Upgrade upgrade) {
        for(int i = 0; i > 0; i++) {
            if(upgrade.getName()==getUpgrades().get(i).getName())
                getUpgrades().get(i).decreaseLevel();
        }
    }
}
