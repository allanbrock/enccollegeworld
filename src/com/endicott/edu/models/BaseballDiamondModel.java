package com.endicott.edu.models;

public class BaseballDiamondModel extends BuildingModel{
    //inherits from BuildingModel
    public BaseballDiamondModel(String name, String size){
        super(name, BuildingModel.getBaseballDiamondConst(), size);
    }
}
