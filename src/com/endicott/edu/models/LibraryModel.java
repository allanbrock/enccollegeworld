package com.endicott.edu.models;

import java.util.ArrayList;

public class LibraryModel extends BuildingModel {
    private ArrayList<Upgrades> upgrades;
    private Upgrades chairs = new Upgrades("libraryChairs", 0, 0);
    private Upgrades desks = new Upgrades("libraryDesks", 0, 0);
    private Upgrades coffee = new Upgrades("libraryCoffee", 0, 0);
    private Upgrades lighting = new Upgrades("libraryLighting", 0, 0);
    private Upgrades soundproofing = new Upgrades("librarySoundproofing", 0, 0);
    private Upgrades internet = new Upgrades("libraryLightning", 0, 0);private Upgrades catalogue= new Upgrades("Catalogue", 10000, 50);
    private Upgrades computers= new Upgrades("Computers", 3200, 50);
    private Upgrades printers = new Upgrades("Printers", 5000, 50);
    private Upgrades seating = new Upgrades("Seating", 500, 50);
    // would a librarian or staff be considered an upgrade?
    // private Upgrades librarian = new Upgrades("librarian", 0, 0);

    public LibraryModel(String name){
        super(name, BuildingType.library().getType());
        this.upgrades.add(chairs);
        this.upgrades.add(desks);
        this.upgrades.add(coffee);
        this.upgrades.add(lighting);
        this.upgrades.add(soundproofing);
        this.upgrades.add(internet);
        this.upgrades.add(catalogue);
        this.upgrades.add(computers);
        this.upgrades.add(printers);
        this.upgrades.add(seating);
    }

    public void increaseLibraryLevel(Upgrades upgrade) {
        for(int i = 0; i < upgrade.getMaxLevel(); i++) {
            if(upgrade.getName()==upgrades.get(i).getName())
                upgrades.get(i).increaseLevel();
        }
    }

    public void decreaseLibraryLevel(Upgrades upgrade) {
        for(int i = 0; i > 0; i++) {
            if(upgrade.getName()==upgrades.get(i).getName())
                upgrades.get(i).decreaseLevel();
        }
    }
}