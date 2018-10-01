package com.endicott.edu.simulators;

import java.util.ArrayList;
import com.endicott.edu.models.PopupEventModel;

/**
 * Created by CJ Mustone and Joseph moss
 */
public class PopupEventManager {
    ArrayList<PopupEventModel> currentEvents = new ArrayList<PopupEventModel>();
    ArrayList<PopupEventModel> eventQueue = new ArrayList<PopupEventModel>();

    public void addEvent(PopupEventModel event){
        currentEvents.add(event);
    }
    public PopupEventModel getNextEvent(){
        PopupEventModel event = eventQueue.get(0);
        eventQueue.remove(0);
        return event;
    }
    public void newEvent(String type, String title, String description, String goodButtonText, String badButtonText){
        PopupEventModel newEvent = new PopupEventModel(type, title, description, goodButtonText, badButtonText);
        currentEvents.add(newEvent);
        eventQueue.add(newEvent);
    }


}
