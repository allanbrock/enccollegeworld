package com.endicott.edu.ui;

import com.endicott.edu.datalayer.*;
import com.endicott.edu.models.*;
import com.endicott.edu.simulators.SportManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.logging.Logger;

public class InterfaceUtils {
    private  static Logger logger = Logger.getLogger("CollegeSimTalker");

    public static void openCollegeAndStoreInRequest(String collegeId, HttpServletRequest request) {
        CollegeModel college;
        UiMessage msg = new UiMessage();
        CollegeDao collegeDao = new CollegeDao();
        college = collegeDao.getCollege(collegeId);
        if (college == null) {
            msg.setMessage("Failed to find college.");
            logger.info(msg.getMessage());
        } else {
            msg.setMessage("Found college: " + college.getRunId());
            logger.info("Found college: " + collegeId);
        }

        DormitoryModel[] dorms = DormitoryDao.getDormsArray(collegeId);
        NewsFeedItemModel[] news = NewsFeedDao.getNews(collegeId);
        SportModel[] sport = SportsDao.getSportsArray(collegeId);
        SportModel[] availableSports = SportManager.getAvailableSports(collegeId);
        StudentModel[] students = StudentDao.getStudentsArray(collegeId); //   StudentSimTalker.getStudents(server, collegeId, msg);
        CollegeModel[] colleges = CollegeDao.getColleges();
        FacultyModel[] faculty = FacultyDao.getFacultyArray(collegeId);
        FloodModel[] flood = FloodDao.getFloodsArray(collegeId);
        //FloodModel[] flood = new FloodModel[0];

        logger.info("Setting attribute college: " + college);
        request.setAttribute("message",msg);
        request.setAttribute("college",college);
        request.setAttribute("colleges",colleges);
        request.setAttribute("dorms",dorms);
        request.setAttribute("news",news);
        request.setAttribute("sports", sport);
        request.setAttribute("availableSports",availableSports);
        request.setAttribute("students",students);
        request.setAttribute("faculty",faculty);
        request.setAttribute("floods",flood);
    }

    public static void setCollegeIdInSession(String collegeId, HttpServletRequest request)
    {
        HttpSession session = request.getSession();
        session.setAttribute("runid",collegeId);
    }

    public static String getCollegeIdFromSession(HttpServletRequest request)
    {
        HttpSession session = request.getSession();
        return (String) session.getAttribute("runid");
    }
}
