package com.endicott.edu.models;

import java.io.Serializable;

/**
 * Created by abrocken on 7/23/2017.
 */

public class NewsFeedItemModel implements Serializable {
    int hour = 0;
    String message = "Nothing is new.";
    NewsType noteType = NewsType.UNKNOWN_NOTE;
    String runId = "unknown";
    int amount;
    NewsLevel noteLevel = NewsLevel.UNKNOWN_NEWS;

    public NewsFeedItemModel() {
    }

    public NewsFeedItemModel(int dayNumber, String message, NewsType message_type, String runId, NewsLevel message_level) {
        this.hour = dayNumber;
        this.message = message;
        this.noteType = message_type;
        this.runId = runId;
        this.noteLevel = message_level;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public NewsType getNoteType() {
        return noteType;
    }

    public NewsLevel getNoteLevel() {
        return noteLevel;
    }

    public void setNoteType(NewsType noteType) {
        this.noteType = noteType;
    }

    public void setNoteLevel(NewsLevel noteLevel) {this.noteLevel = noteLevel;}

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRunId() {
        return runId;
    }

    public void setRunId(String runId) {
        this.runId = runId;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

}
