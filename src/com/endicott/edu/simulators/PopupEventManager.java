package com.endicott.edu.simulators;

import com.endicott.edu.datalayer.BuildingDao;
import com.endicott.edu.datalayer.PopupEventDao;
import com.endicott.edu.models.BuildingModel;
import com.endicott.edu.models.PopupEventModel;
import com.endicott.edu.ui.InterfaceUtils;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by CJ Mustone and Joseph Moss
 */
public class PopupEventManager {
    //Keeps a list of all Popup Events for the current simulation period
    static private PopupEventDao dao = new PopupEventDao();

    /*
        Time does not affect popups currently, and so this function is not needed at this time.
     */
    public void handleTimeChange(String runId, int hoursAlive, PopupEventManager popupManager) {
        // Really important the we save the changes to disk.
        // dao.saveAllPopupEvents(runId, popupEvents);
    }

    //Use this function when you want to create a popup that
    public static PopupEventModel newPopupEvent(String collegeId, String title, String description, String leftButtonText, String leftButtonCallback,
                              String rightButtonText, String rightButtonCallback, String imagePath, String altImageText){

        PopupEventModel newEvent = new PopupEventModel(title, description, leftButtonText, leftButtonCallback, rightButtonText,
                rightButtonCallback, imagePath, altImageText);

        dao.saveNewPopupEvent(collegeId, newEvent);
        return newEvent;

    }
    //    public void newPopupEvent(String title, String description, String acknowledgeButtonText){
//        PopupEventModel newEvent = new PopupEventModel(title, description, acknowledgeButtonText);
//        this.addEvent(newEvent);
//    }
    public static PopupEventModel newPopupEvent(String collegeId, String title, String description, String acknowledgeButtonText, String acknowledgeButtonCallback, String imagePath, String altImageText) {

        PopupEventModel newEvent = new PopupEventModel(title, description, acknowledgeButtonText,
                acknowledgeButtonCallback, imagePath, altImageText);

        dao.saveNewPopupEvent(collegeId, newEvent);
        return newEvent;
    }

    public boolean isQueueInitiated(String collegeId){
        return (dao.getNumberOfPopupEvents(collegeId) > 0);
    }
    public ArrayList<PopupEventModel> getEventsList(String collegeId){
        return new ArrayList<PopupEventModel>(dao.getPopupEvents(collegeId));
    }
    public PopupEventModel getCurrentEvent(String collegeId){
        return dao.getPopupEvents(collegeId).get(0);
    }
    public int getNumberOfEvents(String collegeId){
        return dao.getNumberOfPopupEvents(collegeId);
    }
    public void clearPopupManager(String collegeId){
        dao.clearPopupManager(collegeId);
    }
    public boolean isManagerEmpty(String collegeId){
        return (dao.getNumberOfPopupEvents(collegeId) == 0);
    }



    /**
     * Take the request and see if indicates that a popup acknowledgement button was pressed.
     * If so, call the Manager that is support to handle the request, delete the pop event,
     * and return true.  Return false we didn't find that a popup
     */
    /*
    public void removePopupIfButtonPressed(javax.servlet.http.HttpServletRequest request) {
        if (InterfaceUtils.isThisParamNameInRequest(request, "readAll")) {
            ArrayList<PopupEventModel> tempEvents = new ArrayList<>(currentEvents);

            for (PopupEventModel e : tempEvents) {
                if (!(e.getAcknowledgeButtonCallback() == null)) {
                    currentEvents.remove(e);
                }
            }
            return;
        }
        for (PopupEventModel e : currentEvents) {
            if (InterfaceUtils.isThisParamNameInRequest(request, e.getAcknowledgeButtonCallback())) {
                currentEvents.remove(e);
                return;
            }else if(InterfaceUtils.isThisParamNameInRequest(request, e.getLeftButtonCallback()) || InterfaceUtils.isThisParamNameInRequest(request, e.getRightButtonCallback())){
                currentEvents.remove(e);
                return;

            }
        }
    }
    */

}