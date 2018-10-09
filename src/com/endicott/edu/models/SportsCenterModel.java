package com.endicott.edu.models;

public class SportsCenterModel extends BuildingModel {
    //inherits from BuildingModel
    public SportsCenterModel(String name, int reputation){
        super(name, reputation, BuildingModel.getSportsConst());
    }
}
