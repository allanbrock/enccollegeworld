package com.endicott.edu.models;

import java.util.ArrayList;

public class AcademicCenterModel extends BuildingModel {
    private ArrayList<Upgrades> upgrades;
    private Upgrades tables = new Upgrades("Tables",0, 0);
    private Upgrades airconditioning = new Upgrades("Air Conditioning", 0, 0);
    private Upgrades vendingMachines = new Upgrades("Vending Machines", 0, 0);
    private Upgrades projectors = new Upgrades("Projectors", 0, 0);
    private Upgrades seats = new Upgrades("Seats", 0, 0);
    //inherits from BuildingModel
    public AcademicCenterModel(String name, int numStudents, String size){
        super(name, numStudents, BuildingType.academic().getType(), size);
        this.upgrades.add(tables);
        this.upgrades.add(airconditioning);
        this.upgrades.add(vendingMachines);
        this.upgrades.add(projectors);
        this.upgrades.add(seats);
    }

    public void upgradeAcademicCenter(Upgrades upgrade) {
        for(int i = 0; i < upgrade.getMaxLevel(); i++) {
            if(upgrade.getName()==upgrades.get(i).getName())
                upgrades.get(i).increaseLevel();
        }
    }

    public void downgradeAcademicCenter(Upgrades upgrade) {
        for(int i = 0; i > 0; i++) {
            if(upgrade.getName()==upgrades.get(i).getName())
                upgrades.get(i).decreaseLevel();
        }
    }
}
