package com.endicott.edu.models;
import java.util.ArrayList;
public class LibraryUpgrades {

    private static ArrayList<Upgrades> libraryUpgrades;

    public static final int maxLevel = 3;

    private LibraryUpgrades(){
        libraryUpgrades = new ArrayList<Upgrades>();

    }

    public static ArrayList<Upgrades> getUpgrades(){
        return libraryUpgrades;
    }
}

