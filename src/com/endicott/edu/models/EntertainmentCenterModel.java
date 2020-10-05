package com.endicott.edu.models;

import java.util.ArrayList;

public class EntertainmentCenterModel extends BuildingModel {
    //inherits from BuildingModel
    public EntertainmentCenterModel(String name){
        super(name, BuildingType.entertainment().getType());
        this.getUpgrades().add(new Upgrades("Games",0, 0));
        this.getUpgrades().add(new Upgrades("Sofas", 0, 0));
        this.getUpgrades().add(new Upgrades("movieInventory", 0, 0));
    }

    public void upgradeEntertainmentCenter(Upgrades upgrade) {
        for(int i = 0; i < upgrade.getMaxLevel(); i++) {
            if(upgrade.getName()==getUpgrades().get(i).getName())
                getUpgrades().get(i).increaseLevel();
        }
    }

    public void downgradeEntertainmentCenter(Upgrades upgrade) {
        for(int i = 0; i > 0; i++) {
            if(upgrade.getName()==getUpgrades().get(i).getName())
                getUpgrades().get(i).decreaseLevel();
        }
    }

}
