package com.endicott.edu.models;

import java.io.Serializable;

/**
 * Created by CJ Mustone and Joseph Moss 10/01/18
 */
public class PopupEventModel implements Serializable {
    private String type;
    private boolean response;
    private String title;
    private String description;
    private String goodButtonText;
    private String badButtonText;

    /**
     *
     * @param type
     * @param title
     * @param description
     * @param goodButtonText
     * @param badButtonText
     */
    public PopupEventModel(String type, String title, String description, String goodButtonText, String badButtonText){
        this.type = type;
        this.title = title;
        this.description = description;
        this.goodButtonText = goodButtonText;
        this.badButtonText = badButtonText;
        this.response = false;
    }


    public String getType() {
        return type;
    }

    public boolean isResponse() {
        return response;
    }
    public boolean getResponse() {
        return response;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getGoodButtonText() {
        return goodButtonText;
    }

    public String getBadButtonText() {
        return badButtonText;
    }
}
