package com.endicott.edu.ui;// Created by abrocken on 8/25/2017.


import com.endicott.edu.datalayer.PlagueDao;
import com.endicott.edu.models.PlagueModel;
import com.endicott.edu.simulators.Accountant;
import com.endicott.edu.simulators.CollegeManager;
import com.endicott.edu.simulators.PopupEventManager;
import com.endicott.edu.simulators.TutorialManager;

import javax.servlet.RequestDispatcher;
import java.io.IOException;
import java.util.List;

public class ViewCollegeServlet extends javax.servlet.http.HttpServlet {

    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        String collegeId = InterfaceUtils.getCollegeIdFromSession(request);
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
        if (request.getParameter("nextMonthButton") != null) {
            advanceTimeDays = 30;
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

        // TODO: this is for testing purposes, remove.
        if(request.getParameter("bankruptCollege") != null){
            CollegeManager.bankruptCollege(collegeId);
            CollegeManager.advanceTimeByOneDay(collegeId, popupManager);
        }
        // upgrade button for fire popup sends user to store page
        if (request.getParameter("goToStore") != null){
            response.sendRedirect(request.getContextPath() + "/viewstore.jsp");
            return;
        }
        if(request.getParameter("quarantineStudents") != null){
            Accountant accountant = new Accountant();
            PlagueDao dao = new PlagueDao();
            List<PlagueModel> plagues = dao.getPlagues(collegeId);
            for (PlagueModel plague : plagues) {
                plague.setQuarantine(true);
            }
            dao.saveAllPlagues(collegeId, plagues);
            accountant.payBill(collegeId, "Students are now Quarantined", 5000);

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
