package com.endicott.edu.simulators;

import com.endicott.edu.models.EventType;

import java.util.HashMap;

public class EventManager {
    HashMap<Integer, EventType> eventCalendar = new HashMap<Integer, EventType>();

    public EventManager(){
        eventCalendar.put(5, EventType.PLAGUE);
        eventCalendar.put(7, EventType.FIRE);
        eventCalendar.put(9, EventType.SNOW);
        eventCalendar.put(11, EventType.FLOOD);
        eventCalendar.put(13, EventType.RIOT);
    }

    public void handleTimeChange(String collegeId, int hoursAlive, PopupEventManager popupEventManager){
        CollegeManager.setDaysUntilNextEvent(collegeId, CollegeManager.getDaysUntilNextEvent(collegeId) - 1);
    }

    public static void newEventStart(String collegeId) {
        CollegeManager.setDaysUntilNextEvent(collegeId, SimulatorUtilities.getRandomNumberWithNormalDistribution(7, 2, 3, 10));
    }

    public static boolean isEventPermitted(String collegeId) {
        return (CollegeManager.getDaysUntilNextEvent(collegeId) <= 0);
    }
}
