package com.endicott.edu.models;

import java.util.ArrayList;

public class EntertainmentCenterModel extends BuildingModel {
    private ArrayList<Upgrades> upgrades;
    public static final int maxLevel = 3;
    private Upgrades games = new Upgrades("Games",0, 0);
    private Upgrades sofas = new Upgrades("Sofas", 0, 0);
    private Upgrades movieInventory = new Upgrades("movieInventory", 0, 0);
    //inherits from BuildingModel
    public EntertainmentCenterModel(String name){
        super(name, BuildingType.entertainment().getType());
        this.upgrades.add(games);
        this.upgrades.add(sofas);
        this.upgrades.add(movieInventory);
    }

    public void upgradeEntertainmentCenter(Upgrades upgrade) {
        for(int i = 0; i < maxLevel; i++) {
            if(upgrade.getName()==upgrades.get(i).getName())
                upgrades.get(i).increaseLevel();
        }
    }

    public void downgradeEntertainmentCenter(Upgrades upgrade) {
        for(int i = 0; i < maxLevel; i++) {
            if(upgrade.getName()==upgrades.get(i).getName())
                upgrades.get(i).decreaseLevel();
        }
    }

}
