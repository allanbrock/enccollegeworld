package com.endicott.edu.models;

import java.util.ArrayList;

public class DiningHallModel extends BuildingModel {
    //inherits from BuildingModel
    private ArrayList<Upgrades> upgrades;

    public DiningHallModel(String name, int numStudents, String size){
        super(name, numStudents, BuildingType.dining().getType(), size);
        this.upgrades = DiningHallUpgrades.getUpgrades();
    }
}
