package com.endicott.edu.ui;// Created by abrocken on 8/25/2017.

import com.endicott.edu.simulators.CollegeManager;
import com.endicott.edu.simulators.PopupEventManager;
import com.endicott.edu.simulators.TutorialManager;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class ViewStudentServlet extends javax.servlet.http.HttpServlet {

    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        String collegeId = InterfaceUtils.getCollegeIdFromSession(request);
        if (request.getParameter("nextTip") != null) {
            TutorialManager.advanceTip("viewStudent", collegeId);
        }
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
