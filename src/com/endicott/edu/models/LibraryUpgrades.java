package com.endicott.edu.models;
import java.util.ArrayList;
public class LibraryUpgrades {

    private static ArrayList<Upgrade> libraryUpgrades;

    public static final int maxLevel = 3;

    private LibraryUpgrades(){
        libraryUpgrades = new ArrayList<Upgrade>();

    }

    public static ArrayList<Upgrade> getUpgrades(){
        return libraryUpgrades;
    }
}

