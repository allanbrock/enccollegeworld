package com.endicott.edu.datalayer;

import com.endicott.edu.models.NewsFeedItemModel;
import com.endicott.edu.models.NewsLevel;
import com.endicott.edu.models.NewsType;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


// Created by abrocken on 7/17/2017.

public class NewsFeedDao {
    private Logger logger = Logger.getLogger("NewFeedDao");
    private static String getFilePath(String collegeId) {
        return DaoUtils.getFilePathPrefix(collegeId) +  "newsfeed.dat";
    }

    public static List<NewsFeedItemModel> getAllNotes(String collegeId) {
        ArrayList<NewsFeedItemModel> notes = new ArrayList<>();
        NewsFeedItemModel noteModel = null;
        try {
            File file = new File(getFilePath(collegeId));

            if (!file.exists()) {
                return notes;
            }
            else{
                FileInputStream fis = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(fis);
                notes = (ArrayList<NewsFeedItemModel>) ois.readObject();
                ois.close();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return notes;
    }

    public static NewsFeedItemModel[] getNews(String collegeId) {
        List<NewsFeedItemModel> news = getAllNotes(collegeId);
        return news.toArray(new NewsFeedItemModel[news.size()]);
    }

    public List<NewsFeedItemModel> getNotes(String collegeId, int dayNumber, NewsType noteType) {
        List<NewsFeedItemModel> noteModels = new ArrayList<>();
        noteModels = getAllNotes(collegeId);

        ArrayList<NewsFeedItemModel> outNotes = new ArrayList<>();
        for (NewsFeedItemModel entry : noteModels) {
            if (entry.getNoteType() == noteType &&
                    entry.getHour() == dayNumber) {
                outNotes.add(entry);
            }
        }

        return outNotes;
    }

    private void saveAllNotes(String collegeId, List<NewsFeedItemModel> notes){
        try {
            File file = new File(getFilePath(collegeId));
            file.createNewFile();
            FileOutputStream fos;
            fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(notes);
            oos.close();
        } catch (IOException e) {
            logger.info("Cannot create file: " + getFilePath(collegeId));
            e.printStackTrace();
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    public void saveNote(String collegeId, NewsFeedItemModel note) {
        List<NewsFeedItemModel> allNotes = getAllNotes(collegeId);
        note.setRunId(collegeId);
        allNotes.add(note);
        saveAllNotes(collegeId, allNotes);
    }

    public static void deleteNotes(String collegeId) {
        File file = new File(getFilePath(collegeId));
        file.delete();
    }

    public static void main(String[] args) {
        testNotes();
    }

    private static void testNotes() {
        NewsFeedDao dao = new NewsFeedDao();
        NewsFeedItemModel m1 = new NewsFeedItemModel(1, "Day One - msg 1", NewsType.COLLEGE_NEWS, "000", NewsLevel.GOOD_NEWS);
        NewsFeedItemModel m2 = new NewsFeedItemModel(1, "Day One - msg 2", NewsType.COLLEGE_NEWS, "000", NewsLevel.GOOD_NEWS);
        ArrayList<NewsFeedItemModel> notes = new ArrayList<>();
        notes.add(m1);
        notes.add(m2);
        dao.saveAllNotes("000", notes);

        List<NewsFeedItemModel> outMsgs = dao.getAllNotes("000");

        assert(outMsgs.size() == 2);
        assert(outMsgs.get(1).getHour() == 1);

        NewsFeedItemModel m3 = new NewsFeedItemModel(2, "Day Two", NewsType.COLLEGE_NEWS, "000", NewsLevel.GOOD_NEWS);
        dao.saveNote("000", m3);
        outMsgs = dao.getAllNotes("000");
        assert(outMsgs.size() == 3);

        System.out.println("Test case name: testNotes, Result: pass");
    }
}
