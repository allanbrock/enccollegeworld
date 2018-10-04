package com.endicott.edu.ui;


import com.endicott.edu.simulators.CollegeManager;

import javax.servlet.RequestDispatcher;
import java.io.IOException;
import java.util.Base64;
import java.util.Enumeration;

public class ViewAdminServlet extends javax.servlet.http.HttpServlet {

    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        // Might be deleting
        Enumeration<String> params = request.getParameterNames();
        while(params.hasMoreElements()) {
            String paramName = params.nextElement();
            String paramValue = request.getParameter(paramName);
            if (request.getParameter(paramName).equals("Delete")) {
                Base64.Decoder decoder = Base64.getDecoder();
                String collegeName = new String(paramName);
                CollegeManager.sellCollege(collegeName);
            }
        }
        String i = request.getParameter("sellDorm");

        String collegeId = InterfaceUtils.getCollegeIdFromSession(request);
        InterfaceUtils.openCollegeAndStoreInRequest(collegeId, request);
        RequestDispatcher dispatcher=request.getRequestDispatcher("/viewadmin.jsp");
        dispatcher.forward(request, response);
    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        String collegeId = InterfaceUtils.getCollegeIdFromSession(request);

        if (request.getParameter("nextDayButton") != null) {
            CollegeManager.iterateTime(collegeId, 0);
        }

        // Attempt to fetch the college and load into
        // request attributes to pass to the jsp page.
        InterfaceUtils.openCollegeAndStoreInRequest(collegeId, request);

        RequestDispatcher dispatcher=request.getRequestDispatcher("/viewadmin.jsp");
        dispatcher.forward(request, response);
    }
}
