package com.endicott.edu.models;

public class FootballStadiumModel extends BuildingModel{
    //inherits from BuildingModel
    public FootballStadiumModel(String name, String size){
        super(name, BuildingType.footballStadium().getType(), size);
    }
}
