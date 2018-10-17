package com.endicott.edu.ui;// Created by abrocken on 8/25/2017.


import com.endicott.edu.simulators.CollegeManager;
import com.endicott.edu.simulators.PopupEventManager;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class ViewCollegeServlet extends javax.servlet.http.HttpServlet {

    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        String collegeId = InterfaceUtils.getCollegeIdFromSession(request);
        PopupEventManager popupManager = (PopupEventManager) request.getSession().getAttribute("popupMan");

        if (request.getParameter("nextDayButton") != null) {
            CollegeManager.iterateTime(collegeId, popupManager);
        }
        if (request.getParameter("nextWeekButton") != null) {
            for(int i = 0; i < 7; i++) {
                if (popupManager.isManagerEmpty())
                    CollegeManager.iterateTime(collegeId, popupManager);
            }
        }
        if (request.getParameter("nextMonthButton") != null) {
            for(int i = 0; i < 30; i++) {
                if(popupManager.isManagerEmpty())
                    CollegeManager.iterateTime(collegeId, popupManager);
            }
        }
        if(request.getParameter("updateTuitionButton") != null){
            //call update tuition
            String tuitionValue = request.getParameter("tuitionValue");
            int tuition = Integer.parseInt(tuitionValue);
            CollegeManager.updateCollegeTuition(collegeId, tuition);
        }
        if(request.getParameter("returnToWelcome") != null){
            RequestDispatcher dispatcher=request.getRequestDispatcher("/welcome.jsp");
            dispatcher.forward(request, response);
            return;
        }
        if(request.getParameter("bankruptCollege") != null){
            CollegeManager.bankruptCollege(collegeId);
            CollegeManager.iterateTime(collegeId, popupManager);

        }
//        if(request.getParameter("addEventButton") != null){
//            PopupEventManager popupMan = (PopupEventManager) request.getAttribute("popupMan");
//            popupMan.newPopupEvent("Test Event", "Test Event", "This event is a test of the popup system", "Ok!");
//            request.setAttribute("popupMan", popupMan);
//        }
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
            CollegeManager.iterateTime(collegeId, popupManager);
        }

        // Attempt to fetch the college and load into
        // request attributes to pass to the jsp page.
        InterfaceUtils.openCollegeAndStoreInRequest(collegeId, request);
        InterfaceUtils.setPopupEventManagerInSession(popupManager, request);

        RequestDispatcher dispatcher=request.getRequestDispatcher("/viewcollege.jsp");
        dispatcher.forward(request, response);
    }
}
