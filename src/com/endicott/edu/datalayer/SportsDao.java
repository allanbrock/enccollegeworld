package com.endicott.edu.datalayer;

import com.endicott.edu.models.SportModel;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


// Created by Nick DosSantos on 10/2/17.
// Testing

public class SportsDao {
    private static String getFilePath(String collegeId) {
        return DaoUtils.getFilePathPrefix(collegeId) +  "sports.dat";
    }
    private Logger logger = Logger.getLogger("SportsDao");

    public static List<SportModel> getSports(String collegeId) {
        ArrayList<SportModel> sports = new ArrayList<>();
        SportModel sportModel = null;
        try {
            File file = new File(getFilePath(collegeId));

            if (!file.exists()) {
                return sports;  // There are no sports yet.
            }
            else{
                FileInputStream fis = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(fis);
                sports = (ArrayList<SportModel>) ois.readObject();
                ois.close();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return sports;
    }

    public static SportModel[] getSportsArray(String collegeId) {
        List<SportModel> students = getSports(collegeId);
        return students.toArray(new SportModel[students.size()]);
    }

    public void saveAllSports(String collegeId, List<SportModel> notes){
        logger.info("Saving all sport...");

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

        logger.info("Saved sports...");
    }

    public void saveNewSport(String collegeId, SportModel sport) {
        logger.info("Saving new sport...");
        List<SportModel> sports = getSports(collegeId);
        sport.setRunId(collegeId);
        sports.add(sport);
        saveAllSports(collegeId, sports);
    }

    public static void deleteSports(String collegeId) {
        File file = new File(getFilePath(collegeId));
        file.delete();
    }

    public void deleteSelectedSport(String collegeId, String sportName){
        List<SportModel> sports = getSports(collegeId);
        for(int i =0; i < sports.size(); i++){
            if(sportName.equals(sports.get(i).getName())){
                sports.remove(i);
            }
        }
        saveAllSports(collegeId,sports);
    }

    public static void main(String[] args) {
        testNotes();
    }

    private static void testNotes() {
        final String collegeId = "testsport001";
        SportsDao dao = new SportsDao();
        SportModel m1 = new SportModel(18, 0, 20, 100, 0, 0, 10, 20, 200, 2, 0, "Soccer", collegeId,0 , 48, "unknown", 3, "Fall", 72);
        SportModel m2 = new SportModel(20, 0, 30, 500, 0, 0, 10, 30, 1500, 3, 0, "Hockey", collegeId, 0, 48, "unknown", 3, "Fall",72);
        ArrayList<SportModel> sports = new ArrayList<>();
        sports.add(m1);
        sports.add(m2);
        dao.saveAllSports(collegeId, sports);
        SportModel m4 = new SportModel(20, 0, 30, 500, 0, 0, 10, 30, 1500, 3, 0, "Hockey", collegeId, 0, 48, "unknown", 3, "Fall",72);
        dao.saveNewSport(collegeId, m4);
        List<SportModel> outMsgs = dao.getSports(collegeId);

        assert(outMsgs.size() == 3);
        assert(outMsgs.get(1).getCapacity() == 100);

        SportModel m3 = new SportModel(10,0, 20, 100, 0, 0, 10, 20, 200, 2, 0, "Test Team", collegeId,0, 48, "unknown", 3, "Fall", 24);
        dao.saveNewSport(collegeId, m3);
        outMsgs = dao.getSports(collegeId);
        assert(outMsgs.size() == 3);

        System.out.println("Test case name: testSports, Result: pass");

    }
    public ArrayList<String> seeAllSportNames(){
        ArrayList<String> sportNames = new ArrayList<>();

        sportNames.add("Men's Basketball");
        sportNames.add("Women's Basketball");
        sportNames.add("Baseball");
        sportNames.add("Softball");
        sportNames.add("Women's Soccer");
        sportNames.add("Men's Soccer");
        sportNames.add("Men's Football");
        sportNames.add("Women's Volleyball");
        return sportNames;
    }
}
