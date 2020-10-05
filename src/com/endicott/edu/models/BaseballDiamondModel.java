package com.endicott.edu.models;
import java.util.ArrayList;

public class BaseballDiamondModel extends BuildingModel{
    private ArrayList<Upgrades> upgrades;
    private Upgrades grassQuality = new Upgrades("bbGrass",0, 0);
    private Upgrades dirtQuality = new Upgrades("bbDirt", 0, 0);
    private Upgrades baseQuality = new Upgrades("bbBase", 0, 0);
    private Upgrades seatQuality = new Upgrades("bbSeat", 0, 0);
    private Upgrades stadiumLights = new Upgrades("bbLighting", 0, 0);

    //inherits from BuildingModel
    public BaseballDiamondModel(String name, String size) {
        super(name, BuildingType.baseballDiamond().getType(), size);
        this.upgrades.add(grassQuality);
        this.upgrades.add(dirtQuality);
        this.upgrades.add(baseQuality);
        this.upgrades.add(seatQuality);
        this.upgrades.add(stadiumLights);
    }

    public void increaseBaseballDiamondLevel(Upgrades upgrade) {
        for(int i = 0; i < upgrade.getMaxLevel(); i++) {
            if(upgrade.getName()==upgrades.get(i).getName())
                upgrades.get(i).increaseLevel();
        }
    }

    public void decreaseBaseballDiamondLevel(Upgrades upgrade) {
        for(int i = 0; i > 0; i++) {
            if(upgrade.getName()==upgrades.get(i).getName())
                upgrades.get(i).decreaseLevel();
        }
    }
}
