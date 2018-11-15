package com.endicott.edu.simulators;

import com.endicott.edu.models.EventType;
import java.util.Random;


//TODO: implement isEventActive() for each manager
//TODO: refactor to EventManager instead of DisasterManager
//TODO: make way to save events to DAO/conflict if this class was made static?

/*
all commented out code is for a different way to handle the enums
 */

public class DisasterManager {
    private FireManager fireManager;
    private PlagueManager plagueManager;
    private FloodManager floodManager;
    private SnowManager snowManager;
    private RiotManager riotManager;



    public DisasterManager(){
        fireManager = new FireManager();
        plagueManager = new PlagueManager();
        floodManager = new FloodManager();
        snowManager = new SnowManager();
        riotManager = new RiotManager();
    }

    public static void establishCollege(String collegeId){
        FloodManager.establishCollege(collegeId);
        PlagueManager.establishCollege(collegeId);
    }


    public void handleTimeChange(String runId, int hoursAlive, PopupEventManager popupEventManager){
        if (isCurrentDisaster()) return;
        pickEventAndRunSim(runId,hoursAlive,popupEventManager);
    }

    private boolean isCurrentDisaster(){
        boolean isActive = false;

        while (!isActive){
            if(plagueManager.isEventActive()){
                isActive = true;
            } else if (floodManager.isEventActive()){
                isActive = true;
            } else if (snowManager.isEventActive()){
                isActive = true;
            } else if (riotManager.isEventActive()){
                isActive = true;
            }
        }
        return isActive;
    }

    private void pickEventAndRunSim(String runId, int hoursAlive, PopupEventManager popupEventManager){
        EventType managers[] = EventType.values();
        //private List<EventType> managers = Arrays.asList(EventType.values());

        Random rand = new Random();

        int numEventTypes = 4;
        int randEventIndex = rand.nextInt(numEventTypes);
        //int randEventIndex = rand.nextInt(managers.size());

        if (managers[randEventIndex].equals(EventType.FLOOD)){
            floodManager.handleTimeChange(runId, hoursAlive, popupEventManager);
        } else if (managers[randEventIndex].equals(EventType.PLAGUE)) {
            plagueManager.handleTimeChange(runId,hoursAlive,popupEventManager);
        } else if(managers[randEventIndex].equals(EventType.SNOW)){
            snowManager.handleTimeChange(runId,hoursAlive,popupEventManager);
        } else if(managers[randEventIndex].equals(EventType.RIOT)){
            //missing handleTimeChange()
            //riotManager.handleTimeChange(runId,hoursAlive,popupEventManager); ???
            return;
        } else {
            fireManager.handleTimeChange(runId,hoursAlive,popupEventManager);
        }
        /*
        if (managers.get(randEventIndex).equals(EventType.FLOOD_MANAGER)){
            floodManager.handleTimeChange(runId, hoursAlive, popupEventManager);
        } else if(managers.get(randEventIndex).equals(EventType.PLAGUE_MANAGER){
            plagueManager.handleTimeChange(runId, hoursAlive, popupEventManager);
        } else if
        ...
        */
    }
}
