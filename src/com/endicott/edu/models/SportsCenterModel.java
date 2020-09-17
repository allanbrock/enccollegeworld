package com.endicott.edu.models;

public class SportsCenterModel extends BuildingModel {
    //inherits from BuildingModel
    public SportsCenterModel(String name){
        super(name, BuildingType.sports().getType());
    }
}
