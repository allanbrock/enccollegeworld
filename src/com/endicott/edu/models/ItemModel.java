package com.endicott.edu.models;

import com.sun.org.apache.xpath.internal.operations.Bool;

import java.io.Serializable;


public class ItemModel implements Serializable {
    private String name;
    private Boolean isPurchased;
    private String imageName;
    private int cost;
    private Boolean isUnlocked;


    public ItemModel() {
    }

    public ItemModel(String name, Boolean isPurchased, String imageName, int cost, Boolean isUnlocked) {
        this.name = name;
        this.isPurchased = isPurchased;
        this.imageName = imageName;
        this.cost = cost;
        this.isUnlocked = isUnlocked;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getPurchased() {
        return isPurchased;
    }

    public void setPurchased(Boolean purchased) {
        isPurchased = purchased;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public Boolean getUnlocked() {
        return isUnlocked;
    }

    public void setUnlocked(Boolean unlocked) {
        isUnlocked = unlocked;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
}

