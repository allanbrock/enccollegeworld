package com.endicott.edu.models;
import java.util.ArrayList;

public class BaseballDiamondModel extends BuildingModel{
    private Upgrades grassQuality = new Upgrades("bbGrass",0, 0);
    private Upgrades dirtQuality = new Upgrades("bbDirt", 0, 0);
    private Upgrades baseQuality = new Upgrades("bbBase", 0, 0);
    private Upgrades seatQuality = new Upgrades("bbSeat", 0, 0);
    private Upgrades stadiumLights = new Upgrades("bbLighting", 0, 0);

    //inherits from BuildingModel
    public BaseballDiamondModel(String name, String size) {
        super(name, BuildingType.baseballDiamond().getType(), size);
        this.getUpgrades().add(grassQuality);
        this.getUpgrades().add(dirtQuality);
        this.getUpgrades().add(baseQuality);
        this.getUpgrades().add(seatQuality);
        this.getUpgrades().add(stadiumLights);
    }

    public void increaseBaseballDiamondLevel(Upgrades upgrade) {
        for(int i = 0; i < upgrade.getMaxLevel(); i++) {
            if(upgrade.getName()==getUpgrades().get(i).getName())
                getUpgrades().get(i).increaseLevel();
        }
    }

    public void decreaseBaseballDiamondLevel(Upgrades upgrade) {
        for(int i = 0; i > 0; i++) {
            if(upgrade.getName()==getUpgrades().get(i).getName())
                getUpgrades().get(i).decreaseLevel();
        }
    }
}
