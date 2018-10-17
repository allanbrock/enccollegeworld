package com.endicott.edu.models;

public class HockeyRinkModel extends BuildingModel{
    //inherits from BuildingModel
    public HockeyRinkModel(String name, String size){
        super(name, BuildingModel.getHockeyRinkConst(), size);
    }
}
