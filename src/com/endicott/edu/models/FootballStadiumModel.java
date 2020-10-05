package com.endicott.edu.models;

public class FootballStadiumModel extends BuildingModel{

    //inherits from BuildingModel
    public FootballStadiumModel(String name, String size) {
        super(name, BuildingType.footballStadium().getType(), size);
        this.getUpgrades().add(new Upgrade("fbGrass",0, 0));
        this.getUpgrades().add(new Upgrade("fbSeat", 0, 0));
        this.getUpgrades().add(new Upgrade("fbLighting", 0, 0));
        this.getUpgrades().add(new Upgrade("fbLine", 0, 0));
    }

    public void increaseFootballStadiumLevel(Upgrade upgrade) {
        for(int i = 0; i < upgrade.getMaxLevel(); i++) {
            if(upgrade.getName()==getUpgrades().get(i).getName())
                getUpgrades().get(i).increaseLevel();
        }
    }

    public void decreaseFootballStadiumLevel(Upgrade upgrade) {
        for(int i = 0; i > 0; i++) {
            if(upgrade.getName()==getUpgrades().get(i).getName())
                getUpgrades().get(i).decreaseLevel();
        }
    }
}
