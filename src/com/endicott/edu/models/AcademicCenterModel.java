package com.endicott.edu.models;

public class AcademicCenterModel extends BuildingModel {
    //inherits from BuildingModel
    public AcademicCenterModel(String name, int numStudents, int reputation, String size){
        super(name, numStudents, reputation, BuildingModel.getAcademicConst(), size);
    }
}
