package com.endicott.edu.ui;// Created by abrocken on 8/25/2017.


import com.endicott.edu.datalayer.PlagueDao;
import com.endicott.edu.models.PlagueModel;
import com.endicott.edu.simulators.*;

import javax.servlet.RequestDispatcher;
import java.io.IOException;
import java.util.List;

public class ViewCollegeServlet extends javax.servlet.http.HttpServlet {

    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        String collegeId = InterfaceUtils.getCollegeIdFromSession(request);
        // Check if session timed out.
        if (collegeId == null || !CollegeManager.doesCollegeExist(collegeId)) {
            RequestDispatcher dispatcher=request.getRequestDispatcher("/welcome");
            dispatcher.forward(request, response);
            return;
        }

        PopupEventManager popupManager = (PopupEventManager) request.getSession().getAttribute("popupMan");

        // Advance Time
        int advanceTimeDays = 0;
        if (request.getParameter("nextDayButton") != null) {
            advanceTimeDays = 1;
            popupManager.clearPopupManager();
        }
        if (request.getParameter("nextWeekButton") != null) {
            advanceTimeDays = 7;
            popupManager.clearPopupManager();
        }

        for (int i=0; i < advanceTimeDays && popupManager.isManagerEmpty(); i++) {
            CollegeManager.advanceTimeByOneDay(collegeId, popupManager);
        }

        if(request.getParameter("updateTuitionButton") != null){
            String tuitionValue = request.getParameter("tuitionValue");
            int tuition = Integer.parseInt(tuitionValue);
            CollegeManager.updateCollegeTuition(collegeId, tuition);
        }

        if(request.getParameter("changeCollegeMode") != null){
            String mode = request.getParameter("collegeMode");
            CollegeManager.updateCollegeMode(collegeId, mode);
        }

        // upgrade button for fire, flood and snow popup sends user to store page
        if (request.getParameter("goToStore") != null){
            InterfaceUtils.openCollegeAndStoreInRequest(collegeId, request);
            RequestDispatcher dispatcher=request.getRequestDispatcher("/viewstore.jsp");
            dispatcher.forward(request, response);
        }

        if(request.getParameter("quarantineStudents") != null){
            PlagueManager.quarantineStudents(collegeId);
        }
        if(request.getParameter("outSourceHelp") != null){
            PlagueManager.govHandlesPlagueMutation(collegeId, request, popupManager);
        }
        if(request.getParameter("inHouseHelp") != null){
            PlagueManager.schoolHandlesPlagueMutation(collegeId, request, popupManager);
        }

        // TODO: Stephen this is where you would check if a left or right button was hit.
        if(request.getParameter("picked_pro") != null) {
            PlayManager.handleProfessionalDirectorPicked(collegeId);
        }
        else if(request.getParameter("picked_student") != null){
            PlayManager.handleStudentDirectorPicked(collegeId);
        }


        // Check if the button pressed was from a popup.  If so clear it.
        popupManager.removePopupIfButtonPressed(request);

        // Bankrupt college PopupEvent Button
        if(request.getParameter("returnToWelcome") != null){
            response.sendRedirect(request.getContextPath() + "/welcome.jsp");
//            RequestDispatcher dispatcher=request.getRequestDispatcher("/welcome.jsp");
//            dispatcher.forward(request, response);


            return;
        }

        if (request.getParameter("nextTip") != null) {
            TutorialManager.advanceTip("viewCollege", collegeId);
        }
        if (request.getParameter("hideTips") != null){
            TutorialManager.hideTips("viewCollege", collegeId);
        }
        if (request.getParameter("showTips") != null){
            TutorialManager.showTips("viewCollege", collegeId);
        }

        // Attempt to fetch the college and load into
        // request attributes to pass to the jsp page.
        InterfaceUtils.openCollegeAndStoreInRequest(collegeId, request);
        InterfaceUtils.setPopupEventManagerInSession(popupManager, request);

        RequestDispatcher dispatcher=request.getRequestDispatcher("/viewcollege.jsp");
        dispatcher.forward(request, response);
    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        String collegeId = InterfaceUtils.getCollegeIdFromSession(request);
        PopupEventManager popupManager = (PopupEventManager) request.getSession().getAttribute("popupMan");

        if (request.getParameter("nextDayButton") != null) {
            CollegeManager.advanceTimeByOneDay(collegeId, popupManager);
        }

        // Attempt to fetch the college and load into
        // request attributes to pass to the jsp page.
        InterfaceUtils.openCollegeAndStoreInRequest(collegeId, request);
        InterfaceUtils.setPopupEventManagerInSession(popupManager, request);

        RequestDispatcher dispatcher=request.getRequestDispatcher("/viewcollege.jsp");
        dispatcher.forward(request, response);
    }
}
