package com.endicott.edu.ui;// Created by abrocken on 8/25/2017.

import com.endicott.edu.datalayer.SimTalker;
import com.endicott.edu.datalayer.CollegeSimTalker;

import javax.servlet.RequestDispatcher;
import java.io.IOException;

public class ViewStudentServlet extends javax.servlet.http.HttpServlet {

    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        String runId=request.getParameter("runid");
        String server=request.getParameter("server");
        request.setAttribute("server", server);

        if (request.getParameter("nextDayButton") != null) {
            CollegeSimTalker.nextDayAtCollege(server, runId);
        }

        // Attempt to fetch the college and load into
        // request attributes to pass to the jsp page.
        CollegeSimTalker.openCollegeAndStoreInRequest(server, runId, request);

        RequestDispatcher dispatcher=request.getRequestDispatcher("/viewstudent.jsp");
        dispatcher.forward(request, response);
    }
}
