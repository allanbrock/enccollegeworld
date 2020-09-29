package com.endicott.edu.models;

import java.util.ArrayList;

public class LibraryModel extends BuildingModel {
    //inherits from BuildingModel
    private ArrayList<Upgrades> upgrades;

    public LibraryModel(String name){
        super(name, BuildingType.library().getType());
        this.upgrades = DormUpgrades.getUpgrades();
    }
}
