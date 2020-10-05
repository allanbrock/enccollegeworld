package com.endicott.edu.models;

import java.io.Serializable;
import java.util.ArrayList;

public class AdminModel implements Serializable{
    private ArrayList<Upgrades> upgrades;
    public static final int maxLevel = 3;
    private Upgrades tables = new Upgrades("Tables",0, 0);
    private Upgrades bursars = new Upgrades("Bursars", 0, 0);
    private Upgrades cubicles = new Upgrades("cubicles", 0, 0);
    private Upgrades meetingHalls = new Upgrades("meetingHalls", 0, 0);

    public AdminModel() {
        this.upgrades.add(tables);
        this.upgrades.add(bursars);
        this.upgrades.add(cubicles);
        this.upgrades.add(meetingHalls);
    }

    public void upgradeAdminModel(Upgrades upgrade) {
        for(int i = 0; i < maxLevel; i++) {
            if(upgrade.getName()==upgrades.get(i).getName())
                upgrades.get(i).increaseLevel();
        }
    }

    public void downgradeAdminModel(Upgrades upgrade) {
        for(int i = 0; i < maxLevel; i++) {
            if(upgrade.getName()==upgrades.get(i).getName())
                upgrades.get(i).decreaseLevel();
        }
    }
}
