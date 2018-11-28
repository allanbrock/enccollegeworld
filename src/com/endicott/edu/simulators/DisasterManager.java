package com.endicott.edu.simulators;

public class DisasterManager {

    public DisasterManager(){
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
