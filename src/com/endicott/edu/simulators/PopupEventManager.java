package com.endicott.edu.simulators;

import java.util.ArrayList;
import com.endicott.edu.models.PopupEventModel;

import javax.swing.*;

/**
 * Created by CJ Mustone and Joseph Moss
 */
public class PopupEventManager {
    //Keeps a list of all Popup Events for the current simulation period
    private ArrayList<PopupEventModel> currentEvents;

    public PopupEventManager(){
       currentEvents = new ArrayList<>();

    }

    private void addEvent(PopupEventModel event){
        currentEvents.add(event);
    }


    public void newPopupEvent(String title, String description, String goodButtonText, String goodButtonCallback, String badButtonText, String badButtonCallback){
        PopupEventModel newEvent = new PopupEventModel(title, description, goodButtonText, goodButtonCallback, badButtonText, badButtonCallback);
        currentEvents.add(newEvent);

    }
    public void newPopupEvent(String title, String description, String acknowledgeButtonText){
        PopupEventModel newEvent = new PopupEventModel(title, description, acknowledgeButtonText);
        this.addEvent(newEvent);

    }
    public boolean isQueueInitiated(){
        if(getNumberOfEvents() > 0){
            return true;
        }else{
            return false;
        }
    }
    public ArrayList<PopupEventModel> getEventsList(){
        return this.currentEvents;
    }
    public PopupEventModel getCurrentEvent(){
        return currentEvents.get(0);
    }
    public int getNumberOfEvents(){
        return currentEvents.size();
    }
    public void clearPopupManager(){
        currentEvents.clear();
    }


}
