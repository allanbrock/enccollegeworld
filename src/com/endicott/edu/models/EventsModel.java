package com.endicott.edu.models;

import java.io.Serializable;

public class EventsModel  implements Serializable {
    private String runId;
    private int eventId = -1;
    private String eventName;
    private String description; //a brief overview of what the event is
    private int numAttending = 0;
    private int cost;
    private int income; //how much money will this event generate

    public EventsModel(String runId, String eventName, String description, int cost, int income) {
        this.runId = runId;
        this.eventName = eventName;
        this.description = description;
        this.cost = cost;
        this.income = income;
    }
    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }
    public String getRunId() {
        return runId;
    }

    public void setRunId(String runId) {
        this.runId = runId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getNumAttending() {
        return numAttending;
    }

    public void setNumAttending(int numAttending) {
        this.numAttending = numAttending;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public int getIncome() {
        return income;
    }

    public void setIncome(int income) {
        this.income = income;
    }



}
