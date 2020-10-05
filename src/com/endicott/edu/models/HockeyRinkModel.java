package com.endicott.edu.models;

public class HockeyRinkModel extends BuildingModel{
    //inherits from BuildingModel
    public HockeyRinkModel(String name, String size) {
        super(name, BuildingType.hockeyRink().getType(), size);
        this.getUpgrades().add(new Upgrade("hrSeat", 0, 0));
        this.getUpgrades().add(new Upgrade("hrLighting", 0, 0));
        this.getUpgrades().add(new Upgrade("hrIce", 0, 0));
        this.getUpgrades().add(new Upgrade("hrBarrier", 0, 0));
    }

    public void increaseHockeyRinkLevel(Upgrade upgrade) {
        for(int i = 0; i < upgrade.getMaxLevel(); i++) {
            if(upgrade.getName()==getUpgrades().get(i).getName())
                getUpgrades().get(i).increaseLevel();
        }
    }

    public void decreaseHockeyRinkLevel(Upgrade upgrade) {
        for(int i = 0; i > 0; i++) {
            if(upgrade.getName()==getUpgrades().get(i).getName())
                getUpgrades().get(i).decreaseLevel();
        }
    }
}