package com.endicott.edu.models;

import java.util.ArrayList;

public class EntertainmentCenterModel extends BuildingModel {
    private Upgrades games = new Upgrades("Games",0, 0);
    private Upgrades sofas = new Upgrades("Sofas", 0, 0);
    private Upgrades movieInventory = new Upgrades("movieInventory", 0, 0);
    //inherits from BuildingModel
    public EntertainmentCenterModel(String name){
        super(name, BuildingType.entertainment().getType());
        this.getUpgrades().add(games);
        this.getUpgrades().add(sofas);
        this.getUpgrades().add(movieInventory);
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
