package com.endicott.edu.models;

public class LibraryModel extends BuildingModel {
    //inherits from BuildingModel
    public LibraryModel(String name){
        super(name, BuildingType.library().getType());
    }
}
