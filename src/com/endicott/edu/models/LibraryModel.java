package com.endicott.edu.models;

import java.util.ArrayList;

public class LibraryModel extends BuildingModel {
    // would a librarian or staff be considered an upgrade?
    // private Upgrades librarian = new Upgrades("librarian", 0, 0);

    public LibraryModel(String name){
        super(name, BuildingType.library().getType());
        this.getUpgrades().add(new Upgrades("libraryChairs", 0, 0));
        this.getUpgrades().add(new Upgrades("libraryDesks", 0, 0));
        this.getUpgrades().add(new Upgrades("libraryCoffee", 0, 0));
        this.getUpgrades().add(new Upgrades("libraryLighting", 0, 0));
        this.getUpgrades().add(new Upgrades("librarySoundproofing", 0, 0));
        this.getUpgrades().add(new Upgrades("libraryLightning", 0, 0));
        this.getUpgrades().add(new Upgrades("Catalogue", 10000, 50));
        this.getUpgrades().add(new Upgrades("Computers", 3200, 50));
        this.getUpgrades().add(new Upgrades("Printers", 5000, 50));
        this.getUpgrades().add(new Upgrades("Seating", 500, 50));
    }

    public void increaseLibraryLevel(Upgrades upgrade) {
        for(int i = 0; i < upgrade.getMaxLevel(); i++) {
            if(upgrade.getName()==getUpgrades().get(i).getName())
                getUpgrades().get(i).increaseLevel();
        }
    }

    public void decreaseLibraryLevel(Upgrades upgrade) {
        for(int i = 0; i > 0; i++) {
            if(upgrade.getName()==getUpgrades().get(i).getName())
                getUpgrades().get(i).decreaseLevel();
        }
    }
}