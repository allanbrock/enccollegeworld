package com.endicott.edu.models;
import java.util.ArrayList;

public class FootballStadiumModel extends BuildingModel{
    private ArrayList<Upgrades> upgrades;
    private Upgrades grassQuality = new Upgrades("fbGrass",0, 0);
    private Upgrades seatQuality = new Upgrades("fbSeat", 0, 0);
    private Upgrades stadiumLights = new Upgrades("fbLighting", 0, 0);
    private Upgrades lineQuality = new Upgrades("fbLine", 0, 0);

    //inherits from BuildingModel
    public FootballStadiumModel(String name, String size) {
        super(name, BuildingType.footballStadium().getType(), size);
        this.upgrades.add(grassQuality);
        this.upgrades.add(seatQuality);
        this.upgrades.add(stadiumLights);
        this.upgrades.add(lineQuality);
    }

    public void increaseFootballStadiumLevel(Upgrades upgrade) {
        for(int i = 0; i < upgrade.getMaxLevel(); i++) {
            if(upgrade.getName()==upgrades.get(i).getName())
                upgrades.get(i).increaseLevel();
        }
    }

    public void decreaseFootballStadiumLevel(Upgrades upgrade) {
        for(int i = 0; i > 0; i++) {
            if(upgrade.getName()==upgrades.get(i).getName())
                upgrades.get(i).decreaseLevel();
        }
    }
}
