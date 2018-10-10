package com.endicott.edu.models;

public class EntertainmentCenterModel extends BuildingModel {
    //inherits from BuildingModel
    public EntertainmentCenterModel(String name){
        super(name, BuildingModel.getEntertainmentConst());
    }
}
