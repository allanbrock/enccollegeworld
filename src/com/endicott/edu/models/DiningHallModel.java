package com.endicott.edu.models;

public class DiningHallModel extends BuildingModel {
    //inherits from BuildingModel
    public DiningHallModel(String name, int numStudents, int reputation, String size){
        super(name, numStudents, reputation, BuildingModel.getDiningConst(), size);
    }
}
