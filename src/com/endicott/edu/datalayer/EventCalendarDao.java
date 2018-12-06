package com.endicott.edu.datalayer;

import com.endicott.edu.models.EventCalendarModel;
import com.endicott.edu.models.ItemModel;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

public class EventCalendarDao {
    private static String getFilePath(String runId) {
        return DaoUtils.getFilePathPrefix(runId) +  "eventcalendar.dat";
    }
    private static Logger logger = Logger.getLogger("EventCalendarDao");

    public static List<EventCalendarModel> getEventCalendars(String runId) {
        ArrayList<EventCalendarModel> eventCalendars = new ArrayList<>();
        EventCalendarModel eventCalendar = null;
        try {
            File file = new File(getFilePath(runId));

            if (!file.exists()) {
                return eventCalendars;  // There are no items yet.
            }
            else{
                FileInputStream fis = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(fis);
                eventCalendars = (ArrayList<EventCalendarModel>) ois.readObject();
                ois.close();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        Collections.sort(eventCalendars, new Comparator<EventCalendarModel>() {
            @Override
            public int compare(EventCalendarModel lhs, EventCalendarModel rhs) {
                // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
                return lhs.getEventType().compareTo(rhs.getEventType());
            }
        });

        return eventCalendars;
    }


    public static EventCalendarModel[] getEventCalendarArray(String collegeId) {
        List<EventCalendarModel> events = getEventCalendars(collegeId);
        return events.toArray(new EventCalendarModel[events.size()]);
    }

    public static void saveAllEventCalendars(String eventCalendar, List<EventCalendarModel> eventCalendars){
        logger.info("Saving all students...");
        try {
            File file = new File(getFilePath(eventCalendar));
            file.createNewFile();
            FileOutputStream fos;
            fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(eventCalendars);
            oos.close();
        } catch (FileNotFoundException e) {
            logger.info("Got file not found when attempting to create: " + getFilePath(eventCalendar));
            e.printStackTrace();
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        } catch (IOException e) {
            logger.info("Got io exceptionfound when attempting to create: " + getFilePath(eventCalendar));
            e.printStackTrace();
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }

        logger.info("Saved items...");
    }

    public static void saveNewEventCalendar(String runId, EventCalendarModel eventCalendar) {
        logger.info("Saving new item...");
        List<EventCalendarModel> eventCalendars = getEventCalendars(runId);
        eventCalendars.add(eventCalendar);
        saveAllEventCalendars(runId, eventCalendars);
    }

    public static void deleteEventCalendar(String runId) {
        File file = new File(getFilePath(runId));
        file.delete();
    }

    public static void main(String[] args) {

    }

}
