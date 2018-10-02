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
     * @param type: The type of event or update, i.e., flood, plague, building completion, etc.
     * @param title: The title of the event, i.e., "Flood destroys dorm", "Football team won the championship!"
     * @param description: The description for the event.
     * @param goodButtonText: Text for the "Handle Correctly" button.
     * @param badButtonText: Text for the "Handle Poorly" button.
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
