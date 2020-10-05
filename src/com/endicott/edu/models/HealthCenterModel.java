package com.endicott.edu.models;
import java.util.ArrayList;

public class HealthCenterModel extends BuildingModel {
    private ArrayList<Upgrades> upgrades;
    private Upgrades sanitation = new Upgrades("hcSanitation", 0, 0);
    private Upgrades hospitality = new Upgrades("hcHospitality", 0, 0);
    private Upgrades hours = new Upgrades("hcHours", 0, 0);
    private Upgrades numRooms = new Upgrades("hcNumRooms", 0, 0);
    private Upgrades roomQuality = new Upgrades("hcRoomQuality", 0, 0);

    //inherits from BuildingModel
    public HealthCenterModel(String name){
        super(name, BuildingType.health().getType());
        this.upgrades.add(sanitation);
        this.upgrades.add(hospitality);
        this.upgrades.add(hours);
        this.upgrades.add(numRooms);
        this.upgrades.add(roomQuality);
    }

    public void increaseHealthCenterLevel(Upgrades upgrade) {
        for(int i = 0; i < upgrade.getMaxLevel(); i++) {
            if(upgrade.getName()==upgrades.get(i).getName())
                upgrades.get(i).increaseLevel();
        }
    }

    public void decreaseHealthCenterLevel(Upgrades upgrade) {
        for(int i = 0; i > 0; i++) {
            if(upgrade.getName()==upgrades.get(i).getName())
                upgrades.get(i).decreaseLevel();
        }
    }
}
