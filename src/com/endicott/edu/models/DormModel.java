package com.endicott.edu.models;

public class DormModel extends BuildingModel {
    //inherits from BuildingModel
    public DormModel(String name, int numStudents, String size){
        super(name, numStudents, BuildingModel.getDormConst(), size);
    }

}
