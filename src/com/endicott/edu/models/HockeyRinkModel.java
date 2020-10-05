package com.endicott.edu.models;
import java.util.ArrayList;

public class HockeyRinkModel extends BuildingModel{
    private Upgrades seatQuality = new Upgrades("fbSeat", 0, 0);
    private Upgrades stadiumLights = new Upgrades("fbLighting", 0, 0);
    private Upgrades iceQuality = new Upgrades("hrIce", 0, 0);
    private Upgrades barrierQuality = new Upgrades("hrBarrier", 0, 0);

    //inherits from BuildingModel
    public HockeyRinkModel(String name, String size) {
        super(name, BuildingType.hockeyRink().getType(), size);
        this.getUpgrades().add(seatQuality);
        this.getUpgrades().add(stadiumLights);
        this.getUpgrades().add(iceQuality);
        this.getUpgrades().add(barrierQuality);
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