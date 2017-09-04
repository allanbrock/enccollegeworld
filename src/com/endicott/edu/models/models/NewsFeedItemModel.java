package com.endicott.edu.models.models;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by abrocken on 7/23/2017.
 */
@XmlRootElement
public class NewsFeedItemModel implements Serializable {
    int hour = 0;
    String message = "Nothing is new.";
    NewsType noteType = NewsType.UNKNOWN_NOTE;
    String runId = "unknown";

    public NewsFeedItemModel() {
    }

    public NewsFeedItemModel(int dayNumber, String message, NewsType message_type, String runId) {
        this.hour = dayNumber;
        this.message = message;
        this.noteType = message_type;
        this.runId = runId;
    }

    public NewsType getNoteType() {
        return noteType;
    }

    public void setNoteType(NewsType noteType) {
        this.noteType = noteType;
    }

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
