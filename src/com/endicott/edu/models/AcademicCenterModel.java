package com.endicott.edu.models;

import java.util.ArrayList;

public class AcademicCenterModel extends BuildingModel {
    private Upgrades tables = new Upgrades("Tables",0, 0);
    private Upgrades airconditioning = new Upgrades("Air Conditioning", 0, 0);
    private Upgrades vendingMachines = new Upgrades("Vending Machines", 0, 0);
    private Upgrades projectors = new Upgrades("Projectors", 0, 0);
    private Upgrades seats = new Upgrades("Seats", 0, 0);
    //inherits from BuildingModel
    public AcademicCenterModel(String name, int numStudents, String size){
        super(name, numStudents, BuildingType.academic().getType(), size);
        this.getUpgrades().add(tables);
        this.getUpgrades().add(airconditioning);
        this.getUpgrades().add(vendingMachines);
        this.getUpgrades().add(projectors);
        this.getUpgrades().add(seats);
    }

    public void upgradeAcademicCenter(Upgrades upgrade) {
        for(int i = 0; i < upgrade.getMaxLevel(); i++) {
            if(upgrade.getName()==getUpgrades().get(i).getName())
                getUpgrades().get(i).increaseLevel();
        }
    }

    public void downgradeAcademicCenter(Upgrades upgrade) {
        for(int i = 0; i > 0; i++) {
            if(upgrade.getName()==getUpgrades().get(i).getName())
                getUpgrades().get(i).decreaseLevel();
        }
    }
}
