package com.endicott.edu.models;

import java.util.HashMap;
import java.util.Map;

public enum DormType {
    SMALL(1),
    MEDIUM(2),
    LARGE(3);

    private int value;

    private static Map map = new HashMap<>();

    private DormType(int value) {
        this.value = value;
    }

    static {
        for (DormType dormType : DormType.values()) {
            map.put(dormType.value, dormType);
        }
    }

    public static DormType valueOf(int dormType) {
        return (DormType) map.get(dormType);
    }

    public int getValue() {
        return value;
    }
}