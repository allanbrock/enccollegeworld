package com.endicott.edu.models;

import com.sun.org.apache.xpath.internal.operations.Bool;

import java.io.Serializable;


public class ItemModel implements Serializable {
    private String name;
    private Boolean isPurchased;

    public ItemModel() {
    }

    public ItemModel(String name, Boolean isPurchased) {
        this.name = name;
        this.isPurchased = isPurchased;
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
}

