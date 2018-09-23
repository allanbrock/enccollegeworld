package com.endicott.edu.ui;// Created by abrocken on 8/25/2017.


import com.endicott.edu.simulators.CollegeManager;

import javax.servlet.RequestDispatcher;
import java.io.IOException;

public class ViewCollegeServlet extends javax.servlet.http.HttpServlet {

    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        String runId = InterfaceUtils.getCollegeIdFromSession(request);

        if (request.getParameter("nextDayButton") != null) {
            CollegeManager.nextDay(runId);
        }
        if(request.getParameter("updateTuitionButton") != null){
            //call update tuition
            String tuitionValue = request.getParameter("tuitionValue");
            int tuition = Integer.parseInt(tuitionValue);
            CollegeManager.updateCollegeTuition(runId, tuition);
        }

        // Attempt to fetch the college and load into
        // request attributes to pass to the jsp page.
        InterfaceUtils.openCollegeAndStoreInRequest(runId, request);

        RequestDispatcher dispatcher=request.getRequestDispatcher("/viewcollege.jsp");
        dispatcher.forward(request, response);
    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        String runId=request.getParameter("runid");

        if (request.getParameter("nextDayButton") != null) {
            CollegeManager.nextDay(runId);
        }

        // Attempt to fetch the college and load into
        // request attributes to pass to the jsp page.
        InterfaceUtils.openCollegeAndStoreInRequest(runId, request);

        RequestDispatcher dispatcher=request.getRequestDispatcher("/viewcollege.jsp");
        dispatcher.forward(request, response);
    }
}
