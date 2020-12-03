package com.endicott.edu.datalayer;

import com.endicott.edu.models.PopupEventModel;
import com.endicott.edu.models.StudentModel;
import com.endicott.edu.simulators.CollegeManager;
import com.endicott.edu.ui.InterfaceUtils;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

public class PopupEventDao {
    private static String getFilePath(String runId) {
        return DaoUtils.getFilePathPrefix(runId) +  "popupevent.dat";
    }
    private static Logger logger = Logger.getLogger("PopupEventDao");
    private static HashMap<String, List<PopupEventModel>> cache = new HashMap<>();

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

    public static PopupEventModel[] getPopupEventsArray(String collegeId) {
        List<PopupEventModel> popupEvents = getPopupEvents(collegeId);
        return popupEvents.toArray(new PopupEventModel[popupEvents.size()]);
    }

    public static int getNumberOfPopupEvents(String collegeId) {
        List<PopupEventModel> popupEvents = getPopupEvents(collegeId);
        if (popupEvents == null)
            return 0;
        return popupEvents.size();
    }

    public void saveNewPopupEvent(String runId, PopupEventModel popupEvent) {
        logger.info("Saving new popupEvent...");
        List<PopupEventModel> popupEvents = getPopupEvents(runId);
        popupEvent.setRunId(runId);
        popupEvents.add(popupEvent);
        saveAllPopupEvents(runId, popupEvents);
    }

    public static void saveAllPopupEvents(String runId, List<PopupEventModel> popupEvents){
        logger.info("Saving all popupEvents...");
        try {
            File file = new File(getFilePath(runId));
            file.createNewFile();
            FileOutputStream fos;
            fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(popupEvents);
            oos.close();
        } catch (FileNotFoundException e) {
            logger.info("Got file not found when attempting to create: " + getFilePath(runId));
            e.printStackTrace();
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        } catch (IOException e) {
            logger.info("Got io exceptionfound when attempting to create: " + getFilePath(runId));
            e.printStackTrace();
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }
        cache.put(runId,popupEvents);  // We need to update the cache so that next we get the up to date college.
        logger.info("Saved popupEvents...");
    }

    public static void deletePopupEvent(String runId, PopupEventModel pem) {
        List<PopupEventModel> popupEvents = getPopupEvents(runId);
        popupEvents.remove(pem);
        deletePopupEvents(runId);
        saveAllPopupEvents(runId, popupEvents);
    }

    public static void deletePopupEvents(String runId) {
        File file = new File(getFilePath(runId));
        file.delete();
    }

    public boolean isQueueInitiated(String collegeId){
        return (getNumberOfPopupEvents(collegeId) > 0);
    }
    public ArrayList<PopupEventModel> getEventsList(String collegeId){
        return new ArrayList<PopupEventModel>(getPopupEvents(collegeId));
    }
    public PopupEventModel getCurrentEvent(String collegeId){ return getPopupEventsArray(collegeId)[0]; }
    public void clearPopupManager(String collegeId){
        deletePopupEvents(collegeId);
    }
    public boolean isManagerEmpty(String collegeId){ return (getNumberOfPopupEvents(collegeId) == 0); }
}