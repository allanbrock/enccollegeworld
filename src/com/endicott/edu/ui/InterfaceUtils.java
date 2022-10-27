package com.endicott.edu.ui;

import com.endicott.edu.datalayer.*;
import com.endicott.edu.models.*;
import com.endicott.edu.simulators.PopupEventManager;
import com.endicott.edu.simulators.SportManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;
import java.util.logging.Logger;

public class InterfaceUtils {
    private  static Logger logger = Logger.getLogger("InterfaceUtils");

    public static void openCollegeAndStoreInRequest(String collegeId, HttpServletRequest request) {
        CollegeModel college;
        UiMessage msg = new UiMessage();
        college = CollegeDao.getCollege(collegeId);
        if (college == null) {
            msg.setMessage("Failed to find college: " + collegeId);
            logger.info(msg.getMessage());
        } else {
            msg.setMessage("Found college: " + college.getRunId());
//          logger.info("Found college: " + collegeId);
        }

        BuildingModel[] buildings = BuildingDao.getBuildingsArray(collegeId);
        NewsFeedItemModel[] news = NewsFeedDao.getNews(collegeId);
        GateModel[] gates = GateDao.getGatesArray(collegeId);
        AchievementModel[] achievements = AchievementDao.getAchievementArray(collegeId);
        SportModel[] sport = SportsDao.getSportsArray(collegeId);
        SportModel[] availableSports = SportManager.getAvailableSports(collegeId);
        Student[] students = StudentDao.getStudentsArray(collegeId); //   StudentSimTalker.getStudents(server, collegeId, msg);
        Faculty[] faculty = FacultyDao.getFacultyArray(collegeId);
        FloodModel flood = FloodDao.getFlood(collegeId);
        PopupEventManager popupManager = new PopupEventManager();
        TutorialModel[] tutorials = TutorialDao.getTutorialsArray(collegeId);
        FireModel[] fires = FireDAO.getFiresArray(collegeId);
        ItemModel[] items = InventoryDao.getInventoryArray(collegeId);
        SnowModel snowStorm = SnowDao.getSnowStorm(collegeId);


        // Load the name of all colleges.
        // This is commented out because it is so slow. Maybe used by Admin tab?
        //CollegeModel[] colleges = CollegeDao.getColleges();
        CollegeModel[] colleges = null;

        //logger.info("Setting attribute college.");
        request.setAttribute("message",msg);
        request.setAttribute("college",college);
        request.setAttribute("colleges",colleges);
        request.setAttribute("buildings",buildings);
        request.setAttribute("news",news);
        request.setAttribute("gates", gates);
        request.setAttribute("achievements", achievements);
        request.setAttribute("sports", sport);
        request.setAttribute("availableSports",availableSports);
        request.setAttribute("students",students);
        request.setAttribute("faculty",faculty);
        request.setAttribute("flood",flood);
        request.setAttribute("fires", fires);
        request.setAttribute("items", items);
        request.getSession().setAttribute("popupMan", popupManager);
        request.setAttribute("tutorials", tutorials);
        request.setAttribute("snowStorm", snowStorm);
        request.setAttribute("runId", collegeId);
    }


    public static void setPopupEventManagerInSession(PopupEventManager manager, HttpServletRequest request)
    {
        HttpSession session = request.getSession();
        session.setAttribute("popupMan",manager);
    }

    public static void setCollegeIdInSession(String collegeId, HttpServletRequest request)
    {
        HttpSession session = request.getSession();
        session.setAttribute("runId",collegeId);
    }

    public static String getCollegeIdFromSession(HttpServletRequest request)
    {
        HttpSession session = request.getSession();
        String collegeId = (String) session.getAttribute("runId");
        if (collegeId == null) {
            logger.info("Failed to get collegeId from session");
            collegeId = request.getParameter("runId");
        }
        //logger.info("College id" + collegeId);
        return collegeId;
    }


    public static void logRequestParameters(javax.servlet.http.HttpServletRequest request) {
        Enumeration<String> params = request.getParameterNames();
        logger.info("Logging parameters from request.  They should follow unless empty.");
        while(params.hasMoreElements()){
            String paramName = params.nextElement();
            logger.info("Parameter Name - "+paramName+", Value - "+request.getParameter(paramName));
        }
    }

    public static boolean isThisParamNameInRequest(javax.servlet.http.HttpServletRequest request, String givenParamName) {
        Enumeration<String> params = request.getParameterNames();
        while (params.hasMoreElements()) {
            String paramName = params.nextElement();
            if (paramName.equals(givenParamName)) {
                return true;
            }
        }

        return false;
    }
}
