package com.endicott.edu.models;
import java.util.ArrayList;

public class BaseballDiamondModel extends BuildingModel{
    //inherits from BuildingModel
    public BaseballDiamondModel(String name, String size) {
        super(name, BuildingType.baseballDiamond().getType(), size);
        this.getUpgrades().add(new Upgrades("grassQuality",0, 0));
        this.getUpgrades().add(new Upgrades("dirtQuality", 0, 0));
        this.getUpgrades().add(new Upgrades("baseQuality", 0, 0));
        this.getUpgrades().add(new Upgrades("seatQuality", 0, 0));
        this.getUpgrades().add(new Upgrades("stadiumLights", 0, 0));
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
