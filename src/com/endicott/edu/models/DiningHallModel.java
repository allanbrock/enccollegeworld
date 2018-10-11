package com.endicott.edu.models;

public class DiningHallModel extends BuildingModel {
    //inherits from BuildingModel
    public DiningHallModel(String name, int numStudents, String size){
        super(name, numStudents, BuildingModel.getDiningConst(), size);
    }
}
