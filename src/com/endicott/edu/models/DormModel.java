package com.endicott.edu.models;

import java.util.ArrayList;

public class DormModel extends BuildingModel {
    //inherits from BuildingModel
    private ArrayList<Upgrades> upgrades;
    private Upgrades airConditioning= new Upgrades("Air Conditioning", 10000, 50);
    private Upgrades plumbing= new Upgrades("Plumbing", 3200, 50);
    private Upgrades commonRooms = new Upgrades("CommonRooms", 5000, 50);
    public DormModel(String name, int numStudents, String size){
        super(name, numStudents, BuildingType.dorm().getType(), size);
        this.upgrades.add(airConditioning);
        this.upgrades.add(plumbing);
        this.upgrades.add(commonRooms);
    }

    public void upgradeDormModel(Upgrades upgrade) {
        for(int i = 0; i < upgrade.getMaxLevel(); i++) {
            if(upgrade.getName()==upgrades.get(i).getName())
                upgrades.get(i).increaseLevel();
        }
    }

    public void downgradeDormModel(Upgrades upgrade) {
        for(int i = 0; i > 0; i++) {
            if(upgrade.getName()==upgrades.get(i).getName())
                upgrades.get(i).decreaseLevel();
        }
    }
}
