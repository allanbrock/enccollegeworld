package com.endicott.edu.simulators;

import com.endicott.edu.datalayer.BuildingDao;
import com.endicott.edu.datalayer.PopupEventDao;
import com.endicott.edu.models.BuildingModel;
import com.endicott.edu.models.PopupEventModel;
import com.endicott.edu.ui.InterfaceUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by CJ Mustone and Joseph Moss
 */
public class PopupEventManager {
    //Keeps a list of all Popup Events for the current simulation period
    static private PopupEventDao dao = new PopupEventDao();

    private static void assignUniqueId(String collegeId, PopupEventModel event) {
        int id = new Random().nextInt(900000) + 100000;
        while (!isUniqueId(getEventsList(collegeId), id)) {
            id = new Random().nextInt(900000) + 100000;
        }
        event.setEventId(id);
    }

    private static boolean isUniqueId (ArrayList <PopupEventModel> events, int id){
        for (PopupEventModel p : events) {
            if (p.getEventId() == id) {
                return false;
            }
        }
        return true;
    }

    /*
        Time does not affect popups currently, and so this function is not needed at this time.
     */
    public void handleTimeChange(String runId, PopupEventManager popupManager) {
        // Really important the we save the changes to disk.
        // dao.saveAllPopupEvents(runId, popupEvents);
    }

    //Use this function when you want to create a popup that
    public static PopupEventModel newPopupEvent(String collegeId, String title, String description, String leftButtonText, String leftButtonCallback,
                              String rightButtonText, String rightButtonCallback, String imagePath, String altImageText){

        PopupEventModel newEvent = new PopupEventModel(title, description, leftButtonText, leftButtonCallback, rightButtonText,
                rightButtonCallback, imagePath, altImageText);

        assignUniqueId(collegeId, newEvent);
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

        assignUniqueId(collegeId, newEvent);
        dao.saveNewPopupEvent(collegeId, newEvent);
        return newEvent;
    }

    public boolean isQueueInitiated(String collegeId){
        return (dao.getNumberOfPopupEvents(collegeId) > 0);
    }
    public static ArrayList<PopupEventModel> getEventsList(String collegeId){
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
    public void removePopupIfButtonPressed(String collegeId, HttpServletRequest request) {
        ArrayList<PopupEventModel> tempEvents = new ArrayList<>(getEventsList(collegeId));
        if (InterfaceUtils.isThisParamNameInRequest(request, "readAll")) {
            for (PopupEventModel e : tempEvents) {
                if (!(e.getAcknowledgeButtonCallback() == null)) {
                    dao.deletePopupEvent(collegeId, e);
                }
            }
            return;
        }

        StringBuilder requestURL = new StringBuilder(request.getRequestURL().toString());
        String queryString = request.getQueryString();
        String requestAsString = "";

        if (queryString == null) {
            requestAsString = requestURL.toString();
        } else {
            requestAsString = requestURL.append('?').append(queryString).toString();
        }

        for (PopupEventModel e : tempEvents) {
            // NOT ACTUAL SOLUTION - allows user to continue without being followed by same popup
            // check if url contains eventId of the event e in tempEvent (which im pretty sure is empty)
            // can't figure out anythhing about tempevents or e, so I don't know how to address issue - logger wont show them
            //if (requestAsString.contains(Integer.toString(e.getEventId()))) {
                if (e.getRightButtonCallback() != null && requestAsString.contains(e.getRightButtonCallback())) {
                    //CollegeManager.logger.info("GIANA NEKITOPOULOS right");
                    handleCallback(e.getRightButtonCallback(), collegeId);
                }
                else if (e.getLeftButtonCallback() != null && requestAsString.contains(e.getLeftButtonCallback())) {
                    //CollegeManager.logger.info("GIANA NEKITOPOULOS left");
                    handleCallback(e.getLeftButtonCallback(), collegeId);
                }
                // CollegeManager.logger.info("GIANA NEKITOPOULOS delete");
                dao.deletePopupEvent(collegeId, e);
                return;
            }
       // }
    }

    private void handleCallback(String callbackName, String collegeId) {
        // CollegeManager.logger.info("GIANA NEKITOPOULOS handler");
        // never enters this method, so popups with options are never properly handled, just removed.
        if (callbackName.equals("goToStore")){
            // Well, we'd like to automatically goto the store.  See ViewCollegeServet.
        }

        if(callbackName.equals("quarantineStudents")){
            PlagueManager.quarantineStudents(collegeId);
        }
        if(callbackName.equals("outSourceHelp")){
            PopupEventManager popupManager = new PopupEventManager();
            PlagueManager.govHandlesPlagueMutation(collegeId, null, popupManager);
        }
        if(callbackName.equals("inHouseHelp")){
            PopupEventManager popupManager = new PopupEventManager();
            PlagueManager.schoolHandlesPlagueMutation(collegeId, null, popupManager);
        }
        if(callbackName.equals("picked_pro")) {
            PlayManager.handleProfessionalDirectorPicked(collegeId);
        }
        else if(callbackName.equals("picked_student")){
            PlayManager.handleStudentDirectorPicked(collegeId);
        }
    }

}