package com.endicott.edu.models;
import java.util.ArrayList;

public class FootballStadiumModel extends BuildingModel{

    //inherits from BuildingModel
    public FootballStadiumModel(String name, String size) {
        super(name, BuildingType.footballStadium().getType(), size);
        this.getUpgrades().add(new Upgrades("fbGrass",0, 0));
        this.getUpgrades().add(new Upgrades("fbSeat", 0, 0));
        this.getUpgrades().add(new Upgrades("fbLighting", 0, 0));
        this.getUpgrades().add(new Upgrades("fbLine", 0, 0));
    }

    public void increaseFootballStadiumLevel(Upgrades upgrade) {
        for(int i = 0; i < upgrade.getMaxLevel(); i++) {
            if(upgrade.getName()==getUpgrades().get(i).getName())
                getUpgrades().get(i).increaseLevel();
        }
    }

    public void decreaseFootballStadiumLevel(Upgrades upgrade) {
        for(int i = 0; i > 0; i++) {
            if(upgrade.getName()==getUpgrades().get(i).getName())
                getUpgrades().get(i).decreaseLevel();
        }
    }
}
