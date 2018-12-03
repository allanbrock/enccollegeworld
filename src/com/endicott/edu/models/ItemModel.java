package com.endicott.edu.models;

import com.sun.org.apache.xpath.internal.operations.Bool;

import java.io.Serializable;


public class ItemModel implements Serializable {
    private String name;
    private Boolean isPurchased;
    private String imageName;
    private int cost;
    private Boolean isUnlocked;
    private int gateNum;
    private String description;


    public ItemModel() {
    }

    public ItemModel(String name, Boolean isPurchased, String imageName, int cost, Boolean isUnlocked, int gateNum, String description) {
        this.name = name;
        this.isPurchased = isPurchased;
        this.imageName = imageName;
        this.cost = cost;
        this.isUnlocked = isUnlocked;
        this.gateNum = gateNum;
        this.description = description;
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

    public String getLockedImageName() {
        if (imageName.length() <= 4 || !imageName.contains("."))
            return imageName;

        return imageName.replace(".","_k.");
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public int getGateNum() {
        return gateNum;
    }

    public void setGateNum(int gateNum) {
        this.gateNum = gateNum;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static void main(String[] args) {
        ItemModel i = new ItemModel();
        i.setImageName("abc.png");
        System.out.println(" " + i.getLockedImageName());
    }
}

