package com.endicott.edu.datalayer;

import com.endicott.edu.models.PlagueModel;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


// Created by dyannone on 10/10/2017.

public class PlagueDao {
    private static String getFilePath(String collegeId) {
        return DaoUtils.getFilePathPrefix(collegeId) +  "plague.dat";
    }
    private Logger logger = Logger.getLogger("PlagueDao");

    public List<PlagueModel> getPlagues(String collegeId) {
        ArrayList<PlagueModel> plagues = new ArrayList<>();
        PlagueModel plagueModel = null;
        try {
            File file = new File(getFilePath(collegeId));

            if (!file.exists()) {
                return plagues;  // There are no plagues yet.
            }
            else{
                FileInputStream fis = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(fis);
                plagues = (ArrayList<PlagueModel>) ois.readObject();
                ois.close();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return plagues;
    }
    public PlagueModel getPlague(String collegeId){
        List<PlagueModel> plagues;
        plagues = getPlagues(collegeId);
        if (plagues == null || plagues.size() <= 0)
            return null;

        return plagues.get(0);
    }

    public void saveAllPlagues(String collegeId, List<PlagueModel> notes){
        logger.info("Saving plague...");
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

        logger.info("Saved plague...");
    }

    public void savePlague(String collegeId, PlagueModel plague){
        List<PlagueModel> plagues = new ArrayList<>();
        plagues.add(plague);
        saveAllPlagues(collegeId, plagues);

    }

    public void saveNewPlague(String collegeId, PlagueModel plague) {
        logger.info("Saving new plague...");
        List<PlagueModel> plagues = getPlagues(collegeId);
        plague.setRunId(collegeId);
        plagues.add(plague);
        saveAllPlagues(collegeId, plagues);
    }

    public static void deletePlagues(String collegeId) {
        File file = new File(getFilePath(collegeId));
        file.delete();
    }

    public static void main(String[] args) {
        testNotes();
    }

    private static void testNotes() {
        final String collegeId = "testplague001";
        PlagueDao dao = new PlagueDao();
        PlagueModel m1 = new PlagueModel(100, 10, "Hampshire Hall", collegeId, 5, 0, 1000, 72, 0 );
        PlagueModel m2 = new PlagueModel(200, 20,"Vermont House", collegeId, 5, 0, 1000, 72, 0);
        ArrayList<PlagueModel> plagues = new ArrayList<>();
        plagues.add(m1);
        plagues.add(m2);
        dao.saveAllPlagues(collegeId, plagues);

        List<PlagueModel> outMsgs = dao.getPlagues(collegeId);

        assert(outMsgs.size() == 2);

        PlagueModel m3 = new PlagueModel(300, 10,"Maine Manor", collegeId, 5, 0, 0, 72, 0);
        dao.saveNewPlague(collegeId, m3);
        outMsgs = dao.getPlagues(collegeId);
        assert(outMsgs.size() == 3);

        System.out.println("Test case name: tests, Result: pass");
    }
}
