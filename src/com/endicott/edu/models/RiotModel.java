package com.endicott.edu.models;

import java.io.Serializable;

//Ryan Gallagher
public class RiotModel implements Serializable {
    private int riotCost = 0;
    private String runId = "unknown";
    private String description;
    private String name;
    private String riotLocation = "";

    public RiotModel() {




    }

    public void setDescription(String description) {this.description = description;}
    public String getDescription() {return description; }

    public void setRiotCost(int cost) {this.riotCost = cost;}
    public int getRiotCost() {return this.riotCost;}

    public void setName(String n) {this.name = n;}
    public String getName() {return this.name;}

    public void setRiotLocation(String loc) {this.riotLocation = loc;}
    public String getRiotLocation() {return this.riotLocation;}
}
