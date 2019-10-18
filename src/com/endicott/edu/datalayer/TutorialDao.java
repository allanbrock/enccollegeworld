package com.endicott.edu.datalayer;

import com.endicott.edu.models.TutorialModel;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

// TODO: we need another Dao which is TutorialBookmarkDao.  Bookmark has a page, tip number, isHidden.
// the current tip you are on for that page.  This will be loaded in as part of InterfaceUtils.

public class TutorialDao {
    private static String getFilePath(String collegeId) {
        return DaoUtils.getFilePathPrefix(collegeId) + "tutorials.dat";
    }  // TODO: follow other DAO's so using college path.
    private Logger logger = Logger.getLogger("TutorialDao");

    //TODO: add in college id like other DAOs.
    public static List<TutorialModel> getTutorials(String collegeId) {
        ArrayList<TutorialModel> tutorials = new ArrayList<>();
        TutorialModel tutorialModel = null;
        try {
            File file = new File(getFilePath(collegeId));

            if (!file.exists()) {
                return tutorials;  // There are no tutorials yet.
            }
            else{
                FileInputStream fis = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(fis);
                tutorials = (ArrayList<TutorialModel>) ois.readObject();
                ois.close();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return tutorials;
    }

    // TODO:  add something like this for adding a tip
//    public static void saveNewTip(String collegeId, TutorialModel tip) {
//        List<TutorialModel> tipList = getTutorials(collegeId);
//        tipList.add(tip);
//        saveAllTutorials(collegeId, tipList);
//    }

    public void saveNewTutorial(String collegeId, TutorialModel tutorial) {
        logger.info("Saving new tutorial...");
        List<TutorialModel> tutorials = getTutorials(collegeId);
        tutorial.setRefNum(tutorials.size() + 1);
        tutorials.add(tutorial);
        saveAllTutorials(collegeId, tutorials);
    }

    public static TutorialModel[] getTutorialsArray(String collegeId) {
        List<TutorialModel> tutorials = getTutorials(collegeId);
        return tutorials.toArray(new TutorialModel[tutorials.size()]);
    }

    // We actually don't need to save this stuff the file.
    // But we do need save:  TutorialBookmarks.  That has: # of last tip shown and whether tips are hidden.
    // One bookmark per page.
    public void saveAllTutorials(String collegeId, List<TutorialModel> notes){
        logger.info("Saving all tutorials...");
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

        logger.info("Saved tutorials...");
    }

    public static void deleteTutorials(String collegeId) {
        File file = new File(getFilePath(collegeId));
        file.delete();
    }

    public static void main(String[] args) {
        testNotes();
    }

    private static void testNotes() {
        final String collegeId = "testtips001";
        TutorialDao dao = new TutorialDao();

        TutorialModel m1 = new TutorialModel("This is a test", "admin", 1, true);
        TutorialModel m2 = new TutorialModel("This is another test", "admin", 2, false);
        ArrayList<TutorialModel> tutorials = new ArrayList<>();
        tutorials.add(m1);
        tutorials.add(m2);
        dao.saveAllTutorials(collegeId, tutorials);

        List<TutorialModel> outMsgs = dao.getTutorials(collegeId);


        TutorialModel m3 = new TutorialModel("It's like groundhog day.", "Home", 3, true);
        dao.saveNewTutorial(collegeId, m3);
        outMsgs = dao.getTutorials(collegeId);
        assert(outMsgs.size() == 3);

        System.out.println("Test case name: testTutorials, Result: pass");
    }
}

