package com.endicott.edu.models;
import java.util.ArrayList;
public class LibraryUpgrades {

    private static ArrayList<Upgrades> libraryUpgrades;
    private Upgrades catalogue= new Upgrades("Catalogue", 10000, 50);
    private Upgrades computers= new Upgrades("Computers", 3200, 50);
    private Upgrades printers = new Upgrades("Printers", 5000, 50);
    private Upgrades seating = new Upgrades("Seating", 500, 50);

    private LibraryUpgrades(){
        libraryUpgrades = new ArrayList<Upgrades>();
        libraryUpgrades.add(catalogue);
        libraryUpgrades.add(computers);
        libraryUpgrades.add(printers);
        libraryUpgrades.add(seating);
    }

    public static ArrayList<Upgrades> getUpgrades(){
        return libraryUpgrades;
    }
}

