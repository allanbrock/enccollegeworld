package com.endicott.edu.simulators;

import com.endicott.edu.datalayer.NewsFeedDao;
import com.endicott.edu.models.NewsFeedItemModel;
import com.endicott.edu.models.NewsLevel;
import com.endicott.edu.models.NewsType;

/**
 * Responsible for recording news at the college.
 */
public class NewsManager {

    /**
     * Create a news item.
     *
     * @param collegeId
     * @param newsHour hours college had been alive when news occurred.
     * @param message
     * @param newsType
     * @param newsLevel
     */
    public static void createNews(String collegeId, int newsHour, String message, NewsType newsType, NewsLevel newsLevel) {
        NewsFeedItemModel note = new NewsFeedItemModel();
        note.setHour(newsHour);
        note.setMessage(message);
        note.setNoteType(newsType);
        note.setNoteLevel(newsLevel);
        NewsFeedDao noteDao = new NewsFeedDao();
        noteDao.saveNote(collegeId, note);
    }

    public static void createFinancialNews(String collegeId, int newsHour, String message, int amount) {
        NewsFeedItemModel note = new NewsFeedItemModel();
        note.setHour(newsHour);
        note.setMessage(message);
        note.setAmount(amount);
        note.setNoteType(NewsType.FINANCIAL_NEWS);
        NewsFeedDao noteDao = new NewsFeedDao();
        noteDao.saveNote(collegeId, note);
    }
}
