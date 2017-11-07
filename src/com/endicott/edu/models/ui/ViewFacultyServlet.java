package com.endicott.edu.models.ui;

import com.endicott.edu.models.datalayer.CollegeSimTalker;
import com.endicott.edu.models.datalayer.SimTalker;

import javax.servlet.RequestDispatcher;
import java.io.IOException;

public class ViewFacultyServlet extends javax.servlet.http.HttpServlet {

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
        SimTalker.openCollegeAndStoreInRequest(server, runId, request);

        RequestDispatcher dispatcher=request.getRequestDispatcher("/viewfaculty.jsp");
        dispatcher.forward(request, response);
    }
}
