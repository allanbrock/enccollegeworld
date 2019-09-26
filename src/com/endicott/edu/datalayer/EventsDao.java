package com.endicott.edu.datalayer;

import com.endicott.edu.models.EventsModel;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class EventsDao {
    private static String getFilePath(String collegeId) {
        return DaoUtils.getFilePathPrefix(collegeId) +  "event.dat";
    }

    private static Logger logger = Logger.getLogger("EventsDao");

    /**
     * This function returns a list of all the events
     * The college is defined by its collegeId
     * @param collegeId sim id
     * @return ArrayList<EventsModel> events
     */
    public static List<EventsModel> getEvents(String collegeId) {
        ArrayList<EventsModel> events = new ArrayList<>();
        EventsModel eventsModel = null; //not sure why this is defined and not used.....
        try {
            File file = new File(getFilePath(collegeId));

            if (!file.exists()) {
                return events;  // No events exist
            }
            else{ //events exist lets return the objects....
                FileInputStream fis = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(fis);
                events = (ArrayList<EventsModel>) ois.readObject();
                ois.close();
            }
        } catch (IOException | ClassNotFoundException e) {
            logger.warning("IO exception in retrieving events.. ");
            e.printStackTrace();
        }

        return events;
    }


    /**
     * This creates a new event
     * After assigning them an ID
     * @param collegeId sim id
     * @param event event object
     */
    public void saveNewEvent(String collegeId, EventsModel event) {
        // logger.info("Saving new event...");
        List<EventsModel> events = getEvents(collegeId);
        event.setRunId(collegeId);
        event.setEventId(IdNumberGenDao.getID(collegeId));

        //logger.info("Creating event with ID: " + event.getEventId());
        events.add(event);
        saveAllEvents(collegeId, events);
    }

    /**
     * This function writes a list of events objects to the disk...
     * @param collegeId sim id
     * @param events list of events
     */
    private void saveAllEvents(String collegeId, List<EventsModel> events) {
        logger.info("Saving all events...");
        try {
            File file = new File(getFilePath(collegeId));
            file.createNewFile();
            FileOutputStream fos;
            fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(events);
            oos.close();
        } catch (FileNotFoundException e) {
            logger.info("Got file not found when attempting to create: " + getFilePath(collegeId));
            e.printStackTrace();
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        } catch (IOException e) {
            logger.info("Got io exceptionfound when attempting to create: " + getFilePath(collegeId));
            e.printStackTrace();
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }

        logger.info("Saved events...");

    }

    /**
     * Deletes the events file
     * @param collegeId sim id
     */
    public static void removeAllEvents(String collegeId){
        File file = new File(getFilePath(collegeId));
        file.delete();
    }

    /**
     * Loads all of the events, and searches through the list until it finds a matching event id to the event passed
     * as argument. Then removes that from the list and saves the new list.
     * @param collegeId sim id
     * @param event instance of an event to be deleted.
     */
    public void removeSingleEvent(String collegeId,EventsModel event){
        logger.info("Removing event..");
        int tmp = event.getEventId();
        String name = event.getEventName();

        List<EventsModel> events = getEvents(collegeId);
        for( int i = 0; i < events.size(); i++){
            if(event.getEventId() == events.get(i).getEventId()){
                logger.info("removing " + events.get(i).getEventName());
                events.remove(i);
                break;
            }

        }
        saveAllEvents(collegeId,events);

        logger.info(name + " removed.. ID: " + tmp );


    }

    /**
     * returns the number of events in the list for the college
     * @param collegeId sim id
     * @return number of events
     */
    public int numberOfEvents(String collegeId){
        return getEvents(collegeId).size();

    }


    public static void main(String[] args) {
        final String collegeId = "eventtesting1";
        EventsDao dao = new EventsDao();
        List<EventsModel> events = new ArrayList<>();
        EventsModel event1 = new EventsModel(collegeId,"test1","desc1",100,200);
        dao.saveNewEvent(collegeId,event1);
        assert (dao.numberOfEvents(collegeId) == 1);
        EventsModel event2 = new EventsModel(collegeId,"test2","desc2",100,200);
        events = dao.getEvents(collegeId);
        assert(events.size() == 1);
        events.add(event2);
        dao.saveAllEvents(collegeId,events);
        assert(dao.numberOfEvents(collegeId) == 2);
        dao.removeSingleEvent(collegeId,event1);
        assert(dao.numberOfEvents(collegeId) == 1);
        EventsDao.removeAllEvents(collegeId);
        assert(dao.numberOfEvents(collegeId) == 0);
    }

}