package com.endicott.edu.simulators;

import java.util.ArrayList;
import com.endicott.edu.models.PopupEventModel;

/**
 * Created by CJ Mustone and Joseph Moss
 */
public class PopupEventManager {
    //Keeps a list of all Popup Events for the current simulation period
    ArrayList<PopupEventModel> currentEvents = new ArrayList<PopupEventModel>();

    public void addEvent(PopupEventModel event){
        currentEvents.add(event);
    }


    public void newPopupEvent(String type, String title, String description, String goodButtonText, String badButtonText){
        PopupEventModel newEvent = new PopupEventModel(type, title, description, goodButtonText, badButtonText);
        currentEvents.add(newEvent);

    }
    public void newPopupEvent(String type, String title, String description, String acknowledgeButtonText){
        PopupEventModel newEvent = new PopupEventModel(type, title, description, acknowledgeButtonText);
        currentEvents.add(newEvent);

    }
    public PopupEventModel getNextEvent(){
        return currentEvents.get(0);
    }
    public int getNumberOfEvents(){
        return currentEvents.size();
    }
    public void clearPopupManager(){
        currentEvents.clear();
    }


}
