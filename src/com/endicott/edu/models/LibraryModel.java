package com.endicott.edu.models;

public class LibraryModel extends BuildingModel {
    // would a librarian or staff be considered an upgrade?
    // private Upgrade librarian = new Upgrade("librarian", 0, 0);

    public LibraryModel(String name){
        super(name, BuildingType.library().getType());
        this.getUpgrades().add(new Upgrade("libraryChairs", 0, 0));
        this.getUpgrades().add(new Upgrade("libraryDesks", 0, 0));
        this.getUpgrades().add(new Upgrade("libraryCoffee", 0, 0));
        this.getUpgrades().add(new Upgrade("libraryLighting", 0, 0));
        this.getUpgrades().add(new Upgrade("librarySoundproofing", 0, 0));
        this.getUpgrades().add(new Upgrade("libraryLightning", 0, 0));
        this.getUpgrades().add(new Upgrade("Catalogue", 10000, 50));
        this.getUpgrades().add(new Upgrade("Computers", 3200, 50));
        this.getUpgrades().add(new Upgrade("Printers", 5000, 50));
        this.getUpgrades().add(new Upgrade("Seating", 500, 50));
    }

    public void increaseLibraryLevel(Upgrade upgrade) {
        for(int i = 0; i < upgrade.getMaxLevel(); i++) {
            if(upgrade.getName()==getUpgrades().get(i).getName())
                getUpgrades().get(i).increaseLevel();
        }
    }

    public void decreaseLibraryLevel(Upgrade upgrade) {
        for(int i = 0; i > 0; i++) {
            if(upgrade.getName()==getUpgrades().get(i).getName())
                getUpgrades().get(i).decreaseLevel();
        }
    }
}