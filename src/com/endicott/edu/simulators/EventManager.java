package com.endicott.edu.simulators;

import com.endicott.edu.models.EventType;

import java.util.HashMap;

public class EventManager {
    HashMap<Integer, EventType> eventCalendar = new HashMap<Integer, EventType>();

    public EventManager(String collegeId) {
        // Calendar of number of days open / event type
        eventCalendar.put(5, EventType.PLAGUE);
        eventCalendar.put(7, EventType.FIRE);
        eventCalendar.put(13, EventType.FLOOD);
        eventCalendar.put(16, EventType.RIOT);
        eventCalendar.put(18, EventType.SNOW);
    }

    public void handleTimeChange(String collegeId, int hoursAlive, PopupEventManager popupEventManager){
        CollegeManager.setDaysUntilNextEvent(collegeId, CollegeManager.getDaysUntilNextEvent(collegeId) - 1);
    }

    public boolean doesEventStart(String collegeId, EventType eventType) {
        EventType todaysEvent = eventCalendar.get(CollegeManager.getDaysOpen(collegeId) + 1);
        return todaysEvent != null && todaysEvent == eventType;
    }

    public static void newEventStart(String collegeId) {
        CollegeManager.setDaysUntilNextEvent(collegeId, SimulatorUtilities.getRandomNumberWithNormalDistribution(7, 2, 3, 10));
    }

    public static boolean isEventPermitted(String collegeId) {
        return (CollegeManager.getDaysUntilNextEvent(collegeId) <= 0);
    }
}
