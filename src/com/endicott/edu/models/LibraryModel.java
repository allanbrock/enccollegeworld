package com.endicott.edu.models;

import java.util.ArrayList;

public class LibraryModel extends BuildingModel {
    private ArrayList<Upgrades> upgrades;
    private Upgrades chairs = new Upgrades("libraryChairs", 0, 0);
    private Upgrades desks = new Upgrades("libraryDesks", 0, 0);
    private Upgrades coffee = new Upgrades("libraryCoffee", 0, 0);
    private Upgrades lighting = new Upgrades("libraryLighting", 0, 0);
    private Upgrades soundproofing = new Upgrades("librarySoundproofing", 0, 0);
    private Upgrades internet = new Upgrades("libraryLightning", 0, 0);
    // would a librarian or staff be considered an upgrade?
    // private Upgrades librarian = new Upgrades("librarian", 0, 0);

    public static final int maxLevel = 5;

    public LibraryModel(String name){
        super(name, BuildingType.library().getType());
        this.upgrades.add(chairs);
        this.upgrades.add(desks);
        this.upgrades.add(coffee);
        this.upgrades.add(lighting);
        this.upgrades.add(soundproofing);
        this.upgrades.add(internet);
    }

    public void increaseLibraryLevel(Upgrades upgrade) {
        for(int i = 0; i < upgrades.size(); i++) {
            if(upgrade.getName()==upgrades.get(i).getName())
                upgrades.get(i).increaseLevel();
        }
    }

    public void decreaseLibraryLevel(Upgrades upgrade) {
        for(int i = 0; i < upgrades.size(); i++) {
            if(upgrade.getName()==upgrades.get(i).getName())
                upgrades.get(i).decreaseLevel();
        }
    }
}