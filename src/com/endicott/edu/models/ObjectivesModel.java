package com.endicott.edu.models;

import java.io.Serializable;
import java.util.List;

public class ObjectivesModel implements Serializable {
    public int currentLevel;
    public int studentsNeededForNextLevel;
    public int nextLevel;
    public int studentCount;
    public int studentsNeededForLevel[];
    public GateModel[] gates;
}
