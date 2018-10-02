package com.endicott.edu.datalayer;

import com.endicott.edu.models.TutorialModel;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class TutorialDao {
    private static String getFilePath() {
        return "tutorials.dat";
    }
    private Logger logger = Logger.getLogger("TutorialDao");

    public static List<TutorialModel> getTutorials() {
        ArrayList<TutorialModel> tutorials = new ArrayList<>();
        TutorialModel tutorialModel = null;
        try {
            File file = new File(getFilePath());

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

    public static TutorialModel[] getTutorialsArray(String collegeId) {
        List<TutorialModel> tutorials = getTutorials();
        return tutorials.toArray(new TutorialModel[tutorials.size()]);
    }

    public void saveAllTutorials(List<TutorialModel> notes){
        logger.info("Saving all tutorials...");
        try {
            File file = new File(getFilePath());
            file.createNewFile();
            FileOutputStream fos;
            fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(notes);
            oos.close();
        } catch (FileNotFoundException e) {
            logger.info("Got file not found when attempting to create: " + getFilePath());
            e.printStackTrace();
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        } catch (IOException e) {
            logger.info("Got io exceptionfound when attempting to create: " + getFilePath());
            e.printStackTrace();
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }

        logger.info("Saved tutorials...");
    }

    public void saveNewTutorial(TutorialModel tutorial) {
        logger.info("Saving new tutorial...");
        List<TutorialModel> tutorials = getTutorials();
        tutorial.setRefNum(tutorials.size() + 1);
        tutorials.add(tutorial);
        saveAllTutorials(tutorials);

    }

    public static void main(String[] args) {
        testNotes();
    }

    private static void testNotes() {
        TutorialDao dao = new TutorialDao();

        TutorialModel m1 = new TutorialModel("Testing tutorials", "This is a test", "admin", 1);
        TutorialModel m2 = new TutorialModel("Test Tutorial #2", "This is another test", "admin", 2);
        ArrayList<TutorialModel> tutorials = new ArrayList<>();
        tutorials.add(m1);
        tutorials.add(m2);
        dao.saveAllTutorials(tutorials);

        List<TutorialModel> outMsgs = dao.getTutorials();


        TutorialModel m3 = new TutorialModel("This already happened", "It's like groundhog day.", "Home", 3);
        dao.saveNewTutorial(m3);
        outMsgs = dao.getTutorials();
        assert(outMsgs.size() == 3);

        System.out.println("Test case name: testTutorials, Result: pass");
    }
}

