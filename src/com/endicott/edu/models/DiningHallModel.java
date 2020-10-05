package com.endicott.edu.models;

public class DiningHallModel extends BuildingModel {
    //inherits from BuildingModel

    public DiningHallModel(String name, int numStudents, String size){
        super(name, numStudents, BuildingType.dining().getType(), size);
        this.getUpgrades().add(new Upgrade("Food Variety", 10000, 50));
        this.getUpgrades().add(new Upgrade("Cutleries", 3200, 50));
        this.getUpgrades().add(new Upgrade("Staff", 5000, 50));
    }

    public void upgradeDiningHallModel(Upgrade upgrade) {
        for(int i = 0; i < upgrade.getMaxLevel(); i++) {
            if(upgrade.getName()==getUpgrades().get(i).getName())
                getUpgrades().get(i).increaseLevel();
        }
    }

    public void downgradeDiningHallModel(Upgrade upgrade) {
        for(int i = 0; i > 0; i++) {
            if(upgrade.getName()==getUpgrades().get(i).getName())
                getUpgrades().get(i).decreaseLevel();
        }
    }


}
