package com.endicott.edu.models;

public class HealthCenterModel extends BuildingModel {
    //inherits from BuildingModel
    public HealthCenterModel(String name){
        super(name, BuildingType.health().getType());
    }
}
