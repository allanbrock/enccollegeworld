package com.endicott.edu.models;
import java.util.ArrayList;

public class HockeyRinkModel extends BuildingModel{
    private ArrayList<Upgrades> upgrades;
    private Upgrades seatQuality = new Upgrades("fbSeat", 0, 0);
    private Upgrades stadiumLights = new Upgrades("fbLighting", 0, 0);
    private Upgrades iceQuality = new Upgrades("hrIce", 0, 0);
    private Upgrades barrierQuality = new Upgrades("hrBarrier", 0, 0);

    //inherits from BuildingModel
    public HockeyRinkModel(String name, String size) {
        super(name, BuildingType.hockeyRink().getType(), size);
        this.upgrades.add(seatQuality);
        this.upgrades.add(stadiumLights);
        this.upgrades.add(iceQuality);
        this.upgrades.add(barrierQuality);
    }

    public void increaseHockeyRinkLevel(Upgrades upgrade) {
        for(int i = 0; i < upgrades.size(); i++) {
            if(upgrade.getName()==upgrades.get(i).getName())
                upgrades.get(i).increaseLevel();
        }
    }

    public void decreaseHockeyRinkLevel(Upgrades upgrade) {
        for(int i = 0; i < upgrades.size(); i++) {
            if(upgrade.getName()==upgrades.get(i).getName())
                upgrades.get(i).decreaseLevel();
        }
    }
}