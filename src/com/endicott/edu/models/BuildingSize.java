package com.endicott.edu.models;

import java.util.HashMap;
import java.util.Map;

public enum BuildingSize {
    SMALL(1),
    MEDIUM(2),
    LARGE(3);

    private int value;

    private static Map map = new HashMap<>();

    private BuildingSize(int value) {
        this.value = value;
    }

    static {
        for (BuildingSize buildingSize : BuildingSize.values()) {
            map.put(buildingSize.value, buildingSize);
        }
    }

    public static BuildingSize valueOf(int buildingSize) {
        return (BuildingSize) map.get(buildingSize);
    }

    public int getValue() {
        return value;
    }
}