package com.endicott.edu.models;

import java.util.ArrayList;

public class DormModel extends BuildingModel {
    //inherits from BuildingModel
    private ArrayList<Upgrades> upgrades;

    public DormModel(String name, int numStudents, String size){
        super(name, numStudents, BuildingType.dorm().getType(), size);
        this.upgrades = DormUpgrades.getUpgrades();
    }

}
