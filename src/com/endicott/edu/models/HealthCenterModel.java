package com.endicott.edu.models;
import java.util.ArrayList;

public class HealthCenterModel extends BuildingModel {
    private Upgrades sanitation = new Upgrades("hcSanitation", 0, 0);
    private Upgrades hospitality = new Upgrades("hcHospitality", 0, 0);
    private Upgrades hours = new Upgrades("hcHours", 0, 0);
    private Upgrades numRooms = new Upgrades("hcNumRooms", 0, 0);
    private Upgrades roomQuality = new Upgrades("hcRoomQuality", 0, 0);

    //inherits from BuildingModel
    public HealthCenterModel(String name){
        super(name, BuildingType.health().getType());
        this.getUpgrades().add(sanitation);
        this.getUpgrades().add(hospitality);
        this.getUpgrades().add(hours);
        this.getUpgrades().add(numRooms);
        this.getUpgrades().add(roomQuality);
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
