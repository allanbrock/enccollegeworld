package com.endicott.edu.models;

public class AdministrativeBldgModel extends BuildingModel{
    //inherits from BuildingModel
    public AdministrativeBldgModel(String name){
        super(name, BuildingType.admin().getType());
//        this.getUpgrades().add(new Upgrade("Tables",2500, 0));
//        this.getUpgrades().add(new Upgrade("Bursars", 205, 0));
//        this.getUpgrades().add(new Upgrade("Cubicles", 320, 0));
//        this.getUpgrades().add(new Upgrade("MeetingHalls", 32, 0));
    }

    public void upgradeAdminModel(Upgrade upgrade) {
        for(int i = 0; i < upgrade.getMaxLevel(); i++) {
            if(upgrade.getName()==getUpgrades().get(i).getName())
                getUpgrades().get(i).increaseLevel();
        }
    }

    public void downgradeAdminModel(Upgrade upgrade) {
        for(int i = 0; i > 0; i++) {
            if(upgrade.getName()==getUpgrades().get(i).getName())
                getUpgrades().get(i).decreaseLevel();
        }
    }
}
