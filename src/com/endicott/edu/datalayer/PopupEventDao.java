package com.endicott.edu.datalayer;

import com.endicott.edu.models.PopupEventModel;
import com.endicott.edu.models.StudentModel;
import com.endicott.edu.ui.InterfaceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

public class PopupEventDao {
    private static String getFilePath(String runId) {
        return DaoUtils.getFilePathPrefix(runId) +  "popupevent.dat";
    }
    private Logger logger = Logger.getLogger("PopupEventDao");
    private  static HashMap<String, List<PopupEventModel> cache = new HashMap<>();

    public PopupEventDao(){
        popupEventModels = new ArrayList<>();
    }

    public static List<PopupEventModel> getPopupEvents(String runId){
        if (cache.containsKey(runId))
            return cache.get(runId);

        ArrayList<PopupEventModel> popupEvents = new ArrayList<>();

        try {
            File file = new File(getFilePath(runId));

            if (!file.exists()) {
                return popupEvents;  // There are no students yet.
            }
            else{
                FileInputStream fis = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(fis);
                popupEvents = (ArrayList<PopupEventModel>) ois.readObject();
                ois.close();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        cache.put(runId,popupEvents);

        return popupEvents;
    }

    public static PopupEventModel[] getStudentsArray(String collegeId) {
        List<PopupEventModel> popupEvents = getPopupEvents(collegeId);
        return popupEvents.toArray(new PopupEventModel[popupEvents.size()]);
    }

    public void newPopupEvent(String title, String description, String leftButtonText, String leftButtonCallback,
                              String rightButtonText, String rightButtonCallback, String imagePath, String altImageText){
        PopupEventModel newEvent = new PopupEventModel(title, description, leftButtonText, leftButtonCallback, rightButtonText,
                rightButtonCallback, imagePath, altImageText);

        this.addEvent(newEvent);
    }

    public void newPopupEvent(String title, String description, String acknowledgeButtonText, String acknowledgeButtonCallback, String imagePath, String altImageText) {
        PopupEventModel newEvent = new PopupEventModel(title, description, acknowledgeButtonText,
                acknowledgeButtonCallback, imagePath, altImageText);

        this.addEvent(newEvent);
    }

    public boolean isQueueInitiated(){
        return (getNumberOfEvents() > 0);
    }
    public ArrayList<PopupEventModel> getEventsList(){
        return this.popupEventModels;
    }
    public PopupEventModel getCurrentEvent(){
        return popupEventModels.get(0);
    }
    public int getNumberOfEvents(){
        return popupEventModels.size();
    }
    public void clearPopupManager(){
        popupEventModels.clear();
    }
    public boolean isManagerEmpty(){
        return (popupEventModels.size() == 0);
    }

    /**
     * Take the request and see if indicates that a popup acknowledgement button was pressed.
     * If so, call the Manager that is support to handle the request, delete the pop event,
     * and return true.  Return false we didn't find that a popup
     */
    public void removePopupIfButtonPressed(javax.servlet.http.HttpServletRequest request) {
        if (InterfaceUtils.isThisParamNameInRequest(request, "readAll")) {
            ArrayList<PopupEventModel> tempEvents = new ArrayList<>(popupEventModels);

            for (PopupEventModel e : tempEvents) {
                if (!(e.getAcknowledgeButtonCallback() == null)) {
                    popupEventModels.remove(e);
                }
            }
            return;
        }
        for (PopupEventModel e : popupEventModels) {
            if (InterfaceUtils.isThisParamNameInRequest(request, e.getAcknowledgeButtonCallback())) {
                popupEventModels.remove(e);
                return;
            }else if(InterfaceUtils.isThisParamNameInRequest(request, e.getLeftButtonCallback()) || InterfaceUtils.isThisParamNameInRequest(request, e.getRightButtonCallback())){
                popupEventModels.remove(e);
                return;

            }
        }
    }

}