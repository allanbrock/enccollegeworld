package com.endicott.edu.models;

public class BuildingType {
    private String type;

    private BuildingType(string type){
        BuildingType = new BuildingType();
    }

    public BuildingType academic(){
        return BuildingType("ACADEMIC");
    }

    public BuildingType admin(){
        return BuildingType("ADMIN");
    }

    public BuildingType dining(){
        return BuildingType("DINING");
    }

    public BuildingType dorm(){
        return BuildingType("DORM");
    }

    public BuildingType entertaiment(){
        return BuildingType("ENTERTAINMENT");
    }

    public BuildingType health(){
        return BuildingType("HEALTH");
    }

    public BuildingType library(){
        return BuildingType("LIBRARY");
    }

    public BuildingType sports(){
        return BuildingType("SPORTS");
    }

    public BuildingType baseballDiamond(){
        return BuildingType("BASEBALL DIAMOND");
    }

    public BuildingType footballStadium(){
        return BuildingType("FOOTBALL STADIUM");
    }

    public BuildingType hokeyRink(){
        return BuildingType("HOCKEY RINK");
    }
}
