package com.endicott.edu.models;

public class AcademicCenterModel extends BuildingModel {
    //inherits from BuildingModel
    public AcademicCenterModel(String name, int numStudents, String size){
        super(name, numStudents, BuildingModel.getAcademicConst(), size);
    }
}
