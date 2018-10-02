package com.endicott.edu.models;

import java.util.HashMap;
import java.util.Map;

public enum BuildingType {
    SMALL(1),
    MEDIUM(2),
    LARGE(3);

    private int value;

    private static Map map = new HashMap<>();

    private BuildingType(int value) {
        this.value = value;
    }

    static {
        for (BuildingType buildingType : BuildingType.values()) {
            map.put(buildingType.value, buildingType);
        }
    }

    public static BuildingType valueOf(int buildingType) {
        return (BuildingType) map.get(buildingType);
    }

    public int getValue() {
        return value;
    }
}