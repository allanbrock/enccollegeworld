package com.endicott.edu.models;

public class EventCalendarModel {
    int daysCollegeOpen;
    EventType eventType;

    public  EventCalendarModel(){

    }

    public EventCalendarModel(int daysCollegeOpen, EventType eventType){
        this.daysCollegeOpen = daysCollegeOpen;
        this.eventType = eventType;
    }

    public int getDaysCollegeOpen() {
        return daysCollegeOpen;
    }

    public void setDaysCollegeOpen(int daysCollegeOpen) {
        this.daysCollegeOpen = daysCollegeOpen;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }
}
