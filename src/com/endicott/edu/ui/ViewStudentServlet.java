package com.endicott.edu.ui;// Created by abrocken on 8/25/2017.

import com.endicott.edu.simulators.CollegeManager;
import com.endicott.edu.simulators.PopupEventManager;
import com.endicott.edu.simulators.TutorialManager;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class ViewStudentServlet extends javax.servlet.http.HttpServlet {

    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        String collegeId = InterfaceUtils.getCollegeIdFromSession(request);                                                       //WHY DOESN'T THIS THING FUCKING WORK!?!?!?
        if (request.getParameter("nextTip") != null) {                                                                         //IT LOOKS JUST ALL THE OTHERS, WHY IS IT GREY?!
            TutorialManager.advanceTip("viewStudent", collegeId);
        }
        if (request.getParameter("hideTips") != null){
            //insert some magical code that I don't understand to hide the "well well-lg" div class
        }

        // Before exiting doPost we need to load information into the page and dispatch to the JSP page.
        InterfaceUtils.openCollegeAndStoreInRequest(collegeId, request);

        RequestDispatcher dispatcher=request.getRequestDispatcher("/viewstudent.jsp");
        dispatcher.forward(request, response);
    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        String collegeId = InterfaceUtils.getCollegeIdFromSession(request);

        // Attempt to fetch the college and load into
        // request attributes to pass to the jsp page.
        InterfaceUtils.openCollegeAndStoreInRequest(collegeId, request);

        RequestDispatcher dispatcher=request.getRequestDispatcher("/viewstudent.jsp");
        dispatcher.forward(request, response);
    }
}
