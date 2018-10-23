package com.endicott.edu.simulators;

import java.util.ArrayList;
import com.endicott.edu.models.PopupEventModel;
import com.endicott.edu.ui.InterfaceUtils;

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

    //Use this function when you want to create a popup that
    public void newPopupEvent(String title, String description, String leftButtonText, String leftButtonCallback, String rightButtonText, String rightButtonCallback, String imagePath, String altImageText){
        PopupEventModel newEvent = new PopupEventModel(title, description, leftButtonText, leftButtonCallback, rightButtonText, rightButtonCallback, imagePath, altImageText);
        currentEvents.add(newEvent);

    }
//    public void newPopupEvent(String title, String description, String acknowledgeButtonText){
//        PopupEventModel newEvent = new PopupEventModel(title, description, acknowledgeButtonText);
//        this.addEvent(newEvent);
//    }
    public void newPopupEvent(String title, String description, String acknowledgeButtonText, String acknowledgeButtonCallback, String imagePath, String altImageText) {
        PopupEventModel newEvent = new PopupEventModel(title, description, acknowledgeButtonText, acknowledgeButtonCallback, imagePath, altImageText);
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
    public boolean isManagerEmpty(){
        if(currentEvents.size() == 0){
            return true;
        }
        else{
            return false;
        }
    }



    /**
     * Take the request and see if indicates that a popup acknowledgement button was pressed.
     * If so, call the Manager that is support to handle the request, delete the pop event,
     * and return true.  Return false we didn't find that a popup
     */
    public void removePopupIfButtonPressed(javax.servlet.http.HttpServletRequest request) {
        for (PopupEventModel e : currentEvents) {
            if (InterfaceUtils.isThisParamNameInRequest(request, e.getAcknowledgeButtonCallback())) {
                currentEvents.remove(e);
                return;
            }
        }
    }

}
