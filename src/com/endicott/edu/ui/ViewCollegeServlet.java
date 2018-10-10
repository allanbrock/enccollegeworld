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
        HttpSession session = request.getSession();
        session.setAttribute("popupMan", popupManager);
        if (request.getParameter("nextDayButton") != null) {
            CollegeManager.iterateTime(collegeId, 1, popupManager, session);
        }
        if (request.getParameter("nextWeekButton") != null) {
            CollegeManager.iterateTime(collegeId, 7, popupManager, session);
        }
        if (request.getParameter("nextMonthButton") != null) {
            CollegeManager.iterateTime(collegeId, 30, popupManager, session);
        }
        if(request.getParameter("updateTuitionButton") != null){
            //call update tuition
            String tuitionValue = request.getParameter("tuitionValue");
            int tuition = Integer.parseInt(tuitionValue);
            CollegeManager.updateCollegeTuition(collegeId, tuition);
        }
//        if(request.getParameter("addEventButton") != null){
//            PopupEventManager popupMan = (PopupEventManager) request.getAttribute("popupMan");
//            popupMan.newPopupEvent("Test Event", "Test Event", "This event is a test of the popup system", "Ok!");
//            request.setAttribute("popupMan", popupMan);
//        }
        // Attempt to fetch the college and load into
        // request attributes to pass to the jsp page.
        InterfaceUtils.openCollegeAndStoreInRequest(collegeId, request);


        RequestDispatcher dispatcher=request.getRequestDispatcher("/viewcollege.jsp");
        dispatcher.forward(request, response);
    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        String collegeId = InterfaceUtils.getCollegeIdFromSession(request);
        PopupEventManager popupManager = (PopupEventManager) request.getSession().getAttribute("popupMan");
        HttpSession session = request.getSession();
        if (request.getParameter("nextDayButton") != null) {
            CollegeManager.iterateTime(collegeId, 0, popupManager, session);
        }

        // Attempt to fetch the college and load into
        // request attributes to pass to the jsp page.
        InterfaceUtils.openCollegeAndStoreInRequest(collegeId, request);

        RequestDispatcher dispatcher=request.getRequestDispatcher("/viewcollege.jsp");
        dispatcher.forward(request, response);
    }
}
