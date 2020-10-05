package com.endicott.edu.models;
import java.util.ArrayList;

public class HockeyRinkModel extends BuildingModel{
    //inherits from BuildingModel
    public HockeyRinkModel(String name, String size) {
        super(name, BuildingType.hockeyRink().getType(), size);
        this.getUpgrades().add(new Upgrades("hrSeat", 0, 0));
        this.getUpgrades().add(new Upgrades("hrLighting", 0, 0));
        this.getUpgrades().add(new Upgrades("hrIce", 0, 0));
        this.getUpgrades().add(new Upgrades("hrBarrier", 0, 0));
    }

    public void increaseHockeyRinkLevel(Upgrades upgrade) {
        for(int i = 0; i < upgrade.getMaxLevel(); i++) {
            if(upgrade.getName()==getUpgrades().get(i).getName())
                getUpgrades().get(i).increaseLevel();
        }
    }

    public void decreaseHockeyRinkLevel(Upgrades upgrade) {
        for(int i = 0; i > 0; i++) {
            if(upgrade.getName()==getUpgrades().get(i).getName())
                getUpgrades().get(i).decreaseLevel();
        }
    }
}