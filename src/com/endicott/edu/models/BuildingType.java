package com.endicott.edu.models;

import java.util.ArrayList;

public class BuildingType {
    private String type;

    private BuildingType(String type){
        this.type = type;
    }

    public String getType(){
        return this.type;
    }

    public static BuildingType academic(){
        return new BuildingType("ACADEMIC");
    }

    public static BuildingType admin(){
        return new BuildingType("ADMIN");
    }

    public static BuildingType dining(){
        return new BuildingType("DINING");
    }

    public static BuildingType dorm(){
        return new BuildingType("DORM");
    }

    public static BuildingType entertainment(){
        return new BuildingType("ENTERTAINMENT");
    }

    public static BuildingType health(){
        return new BuildingType("HEALTH");
    }

    public static BuildingType library(){
        return new BuildingType("LIBRARY");
    }

    public static BuildingType sports(){
        return new BuildingType("SPORTS");
    }

    public static BuildingType baseballDiamond(){
        return new BuildingType("BASEBALL DIAMOND");
    }

    public static BuildingType footballStadium(){
        return new BuildingType("FOOTBALL STADIUM");
    }

    public static BuildingType hockeyRink(){
        return new BuildingType("HOCKEY RINK");
    }
}
