package com.endicott.edu.datalayer;

import com.endicott.edu.models.DormitoryModel;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


// Created by abrocken on 7/17/2017.

public class DormitoryDao {
    private static String getFilePath(String collegeId) {
        return DaoUtils.getFilePathPrefix(collegeId) +  "dormitory.dat";
    }
    private static Logger logger = Logger.getLogger("DormitoryDao");

    public static List<DormitoryModel> getDorms(String collegeId) {
        ArrayList<DormitoryModel> dorms = new ArrayList<>();
        DormitoryModel dormModel = null;
        try {
            File file = new File(getFilePath(collegeId));

            if (!file.exists()) {
                return dorms;  // There are no dorms yet.
            }
            else{
                FileInputStream fis = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(fis);
                dorms = (ArrayList<DormitoryModel>) ois.readObject();
                ois.close();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return dorms;
    }

    public static DormitoryModel[] getDormsArray(String collegeId) {
        List<DormitoryModel> dorms = getDorms(collegeId);
        return dorms.toArray(new DormitoryModel[dorms.size()]);
    }

    public static void saveAllDorms(String collegeId, List<DormitoryModel> notes){
        logger.info("Saving all dorm...");
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

        logger.info("Saved dorms...");
    }

    public static void saveNewDorm(String collegeId, DormitoryModel dorm) {
        logger.info("Saving new dorm...");
        List<DormitoryModel> dorms = getDorms(collegeId);
        dorm.setRunId(collegeId);
        dorms.add(dorm);
        saveAllDorms(collegeId, dorms);

    }

    public static void deleteDorm(String collegeId) {

        File file = new File(getFilePath(collegeId));
        file.delete();
    }

    public static void main(String[] args) {
        testNotes();
    }


    private static void testNotes() {
        final String collegeId = "testdorm001";
        DormitoryDao dao = new DormitoryDao();

        DormitoryModel m1 = new DormitoryModel(100, 10, "Hampshire Hall", 50,
                "none", 3, "none", 50);
        DormitoryModel m2 = new DormitoryModel(200, 10, "Vermont House", 70,
                "none", 7, "none", 100);
        ArrayList<DormitoryModel> dorms = new ArrayList<>();
        dorms.add(m1);
        dorms.add(m2);
        dao.saveAllDorms(collegeId, dorms);

        List<DormitoryModel> outMsgs = dao.getDorms(collegeId);

        assert(outMsgs.size() == 2);
        assert(outMsgs.get(1).getCapacity() == 100);

        DormitoryModel m3 = new DormitoryModel(300, 10, "Maine Manor", 250,
                "flood", 8, "none",100);
        dao.saveNewDorm(collegeId, m3);
        outMsgs = dao.getDorms(collegeId);
        assert(outMsgs.size() == 3);

        System.out.println("Test case name: testDorms, Result: pass");
    }
}
