package com.endicott.edu.datalayer;

import com.endicott.edu.models.FloodModel;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


// Created by cseidl on 7/17/2017.

public class FloodDao {
    private static String getFilePath(String collegeId) {
        return DaoUtils.getFilePathPrefix(collegeId) +  "flood.dat";
    }
    private Logger logger = Logger.getLogger("FloodDao");

    public static List<FloodModel> getFloods(String collegeId) {
        ArrayList<FloodModel> floods = new ArrayList<>();
        FloodModel floodModel = null;
        try {
            File file = new File(getFilePath(collegeId));

            if (!file.exists()) {
                return floods;  // There are no floods yet.
            }
            else{
                FileInputStream fis = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(fis);
                floods = (ArrayList<FloodModel>) ois.readObject();
                ois.close();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return floods;
    }

    public static FloodModel[] getFloodsArray(String collegeId) {
        List<FloodModel> floods = getFloods(collegeId);
        return floods.toArray(new FloodModel[floods.size()]);
    }

    public void saveAllFloods(String collegeId, List<FloodModel> notes){
        logger.info("Saving flood...");
        try {
            File file = new File(getFilePath(collegeId));
            file.createNewFile();
            FileOutputStream fos;
            fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(notes);
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

        logger.info("Saved flood...");
    }

    public void saveNewFlood(String collegeId, FloodModel flood) {
        logger.info("Saving new flood...");
        List<FloodModel> floods = getFloods(collegeId);
        flood.setRunId(collegeId);
        floods.add(flood);
        saveAllFloods(collegeId, floods);
    }

    public static void deleteFloods(String collegeId) {
        File file = new File(getFilePath(collegeId));
        file.delete();
    }

    public static void main(String[] args) {
        testNotes();
    }

    private static void testNotes() {
        final String collegeId = "testflood001";
        FloodDao dao = new FloodDao();
        FloodModel m1 = new FloodModel(1000, 72, 100, 10, "Hampshire Hall", collegeId );
        FloodModel m2 = new FloodModel(1500, 96, 200, 20,"Vermont House", collegeId);
        ArrayList<FloodModel> floods = new ArrayList<>();
        floods.add(m1);
        floods.add(m2);
        dao.saveAllFloods(collegeId, floods);

        List<FloodModel> outMsgs = dao.getFloods(collegeId);

        assert(outMsgs.size() == 2);

        FloodModel m3 = new FloodModel(1000, 72, 300, 10,"Maine Manor", collegeId);
        dao.saveNewFlood(collegeId, m3);
        outMsgs = dao.getFloods(collegeId);
        assert(outMsgs.size() == 3);

        System.out.println("Test case name: testFloods, Result: pass");
    }
}
