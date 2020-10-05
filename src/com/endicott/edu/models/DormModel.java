package com.endicott.edu.models;

public class DormModel extends BuildingModel {
    //inherits from BuildingModel
    public DormModel(String name, int numStudents, String size){
        super(name, numStudents, BuildingType.dorm().getType(), size);
        this.getUpgrades().add(new Upgrade("Air Conditioning", 10000, 50));
        this.getUpgrades().add(new Upgrade("Plumbing", 3200, 50));
        this.getUpgrades().add(new Upgrade("CommonRooms", 5000, 50));
    }

    public void upgradeDormModel(Upgrade upgrade) {
        for(int i = 0; i < upgrade.getMaxLevel(); i++) {
            if(upgrade.getName()==getUpgrades().get(i).getName())
                getUpgrades().get(i).increaseLevel();
        }
    }

    public void downgradeDormModel(Upgrade upgrade) {
        for(int i = 0; i > 0; i++) {
            if(upgrade.getName()==getUpgrades().get(i).getName())
                getUpgrades().get(i).decreaseLevel();
        }
    }
}
