package com.endicott.edu.models;

import java.io.Serializable;

//Ryan Gallagher
public class RiotModel implements Serializable {
    private int riotCost = 0;
    private String runId = "unknown";
    private String description;
    private String name;
    private BuildingModel location;

    public RiotModel() {




    }

    public void setLocation(BuildingModel building) {this.location = building;}
    public BuildingModel getLocation() {return this.location;}

    public void setDescription(String description) {this.description = description;}
    public String getDescription() {return description; }

    public void setRiotCost(int cost) {this.riotCost = cost;}
    public int getRiotCost() {return this.riotCost;}

    public void setName(String n) {this.name = n;}
    public String getName() {return this.name;}
}
