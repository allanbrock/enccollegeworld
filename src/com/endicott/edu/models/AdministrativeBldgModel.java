package com.endicott.edu.models;

public class AdministrativeBldgModel extends BuildingModel{
    //inherits from BuildingModel
    public AdministrativeBldgModel(String name){
        super(name, BuildingType.admin().getType());
    }
}
