package com.endicott.edu.ui;


import com.endicott.edu.simulators.CollegeManager;

import javax.servlet.RequestDispatcher;
import java.io.IOException;

public class ViewAdminServlet extends javax.servlet.http.HttpServlet {

    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        String runId = InterfaceUtils.getCollegeIdFromSession(request);

        if (request.getParameter("nextDayButton") != null) {
            CollegeManager.nextDay(runId);
        }

        // Attempt to fetch the college and load into
        // request attributes to pass to the jsp page.
        InterfaceUtils.openCollegeAndStoreInRequest(runId, request);

        RequestDispatcher dispatcher=request.getRequestDispatcher("/viewadmin.jsp");
        dispatcher.forward(request, response);
    }
}
