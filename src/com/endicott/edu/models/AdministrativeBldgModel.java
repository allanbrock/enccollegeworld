package com.endicott.edu.models;

import java.util.ArrayList;

public class AdministrativeBldgModel extends BuildingModel{
    //inherits from BuildingModel

    private Upgrades tables = new Upgrades("Tables",0, 0);
    private Upgrades bursars = new Upgrades("Bursars", 0, 0);
    private Upgrades cubicles = new Upgrades("cubicles", 0, 0);
    private Upgrades meetingHalls = new Upgrades("meetingHalls", 0, 0);

    public AdministrativeBldgModel(String name){
        super(name, BuildingType.admin().getType());
        this.getUpgrades().add(tables);
        this.getUpgrades().add(bursars);
        this.getUpgrades().add(cubicles);
        this.getUpgrades().add(meetingHalls);
    }

    public void upgradeAdminModel(Upgrades upgrade) {
        for(int i = 0; i < upgrade.getMaxLevel(); i++) {
            if(upgrade.getName()==getUpgrades().get(i).getName())
                getUpgrades().get(i).increaseLevel();
        }
    }

    public void downgradeAdminModel(Upgrades upgrade) {
        for(int i = 0; i > 0; i++) {
            if(upgrade.getName()==getUpgrades().get(i).getName())
                getUpgrades().get(i).decreaseLevel();
        }
    }
}
