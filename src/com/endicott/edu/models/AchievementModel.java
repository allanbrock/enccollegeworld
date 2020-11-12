package com.endicott.edu.models;

import java.io.Serializable;

public class AchievementModel implements Serializable {
    private String name;
    private String description;
    private String type;
    private int cashReward;
    private String runId;
    private String iconPath;
    // private Experience expReward;

    private boolean locked;

    private int levelReq;
    private int moneyReq;
    private int happinessReq;

    public AchievementModel(String name, String description, String type, int cashReward, int level, int money, int happiness) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.cashReward = cashReward;
        this.locked = true;
        this.runId = "unknown";

        if(type == "level")
            this.levelReq = level;
        else if(type == "money")
            this.moneyReq = money;
        else if(type == "happiness")
            this.happinessReq = happiness;
    }

    public String getName(){return this.name;}
    public void setName(String name){this.name = name;}

    public String getDescription(){return this.description;}
    public void setDescription(String description){this.description = description;}

    public int getCashReward(){return this.cashReward;}
    public void setCashReward(int cash){this.cashReward = cash;}

    public boolean getLock(){return this.locked;}
    public void setLock(boolean status){this.locked = status;}

    public String getType(){return this.type;}
    public void setType(String type){this.type = type;}

    public int getLevelReq(String type){
        if(type == "level")
            return this.levelReq;
        else if(type == "money")
            return this.moneyReq;
        else
            return this.happinessReq;
    }

    public void setRunId(String _runId){this.runId = _runId;}

    public String getIconPath(){return this.iconPath;}
}
