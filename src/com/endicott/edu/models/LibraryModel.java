package com.endicott.edu.models;

import java.util.ArrayList;

public class LibraryModel extends BuildingModel {
    //inherits from BuildingModel
    private ArrayList<Upgrades> upgrades;
    public static final int maxLevel = 5;

    public LibraryModel(String name){
        super(name, BuildingType.library().getType());
        this.upgrades = DormUpgrades.getUpgrades();
    }
}
