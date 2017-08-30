package com.endicott.edu.models.ui;
// Created by abrocken on 8/25/2017.

import java.io.Serializable;

public class UiMessage implements Serializable {
    private String message;

    public UiMessage(String s) {
        message = s;
    }

    public UiMessage() {
        message = "";
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
