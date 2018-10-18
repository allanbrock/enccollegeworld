package com.endicott.edu.datalayer;

import com.endicott.edu.models.GateModel;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.io.*;
import java.util.*;
import java.util.logging.Logger;

public class GateDao {
    private static String getFilePath(String collegeId) {
        return DaoUtils.getFilePathPrefix(collegeId) +  "gates.dat";
    }
    private Logger logger = Logger.getLogger("GateDao");

    public static List<GateModel> getGates(String collegeId) {
        ArrayList<GateModel> gates = new ArrayList<>();
        try {
            File file = new File(getFilePath(collegeId));

            if (!file.exists()) {
                return gates;  // There are no sports yet.
            }
            else{
                FileInputStream fis = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(fis);
                gates = (ArrayList<GateModel>) ois.readObject();
                ois.close();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return gates;
    }

    public static GateModel[] getGatesArray(String collegeId) {
        List<GateModel> students = getGates(collegeId);
        return students.toArray(new GateModel[students.size()]);
    }

    public void saveAllGates(String collegeId, List<GateModel> notes){
        logger.info("Saving all gates...");

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

        logger.info("Saved gates...");
    }

    public void saveNewGate(String collegeId, GateModel gate) {
        logger.info("Saving new gate...");
        List<GateModel> gates = getGates(collegeId);
        gate.setRunId(collegeId);
        gates.add(gate);
        Collections.sort(gates, (o1, o2) -> o1.getGoal() - o2.getGoal());
//        gates.sort(Comparator.comparingInt(GateModel::getGoal));
        saveAllGates(collegeId, gates);
    }

    public static void main(String[] args) {
        testNotes();
    }

    private static void testNotes() {}
}
