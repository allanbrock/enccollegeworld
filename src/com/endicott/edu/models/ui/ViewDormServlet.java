package com.endicott.edu.models.ui;// Created by abrocken on 8/25/2017.

import com.endicott.edu.models.datalayer.SimTalker;

import javax.servlet.RequestDispatcher;
import java.io.IOException;
import java.util.logging.Logger;

public class ViewDormServlet extends javax.servlet.http.HttpServlet {
    private static Logger logger = Logger.getLogger("ViewDormServlet");

    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {

        String runId=request.getParameter("runid");
        String server=request.getParameter("server");
        request.setAttribute("server", server);
        String dormName=request.getParameter("dormToAdd");
        String dormType=request.getParameter("dormType");
        logger.info("In ViewDormServlet.doPost()");

//        if(runId == null || server ==null ||dormName ==null || dormType == null){
//            UiMessage message = new UiMessage("Cannot add dorm, information is missing");
//            request.setAttribute("message", message);
//        }
//        else{
//            SimTalker.addDorm(server, runId, dormName, dormType);
//        }

        //load the request with attributes for the dorm
        //request.setAttribute("server", server);
        //SimTalker.openCollegeAndStoreInRequest(server, runId, request);


        RequestDispatcher dispatcher=request.getRequestDispatcher("/viewdorm.jsp");
        dispatcher.forward(request, response);
        // Attempt to fetch the college and load into
        // request attributes to pass to the jsp page.
        //SimTalker.openCollegeAndStoreInRequest(server, runId, request);



    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        String runId=request.getParameter("runid");
        String server=request.getParameter("server");
        request.setAttribute("server", server);

        if (request.getParameter("nextDayButton") != null) {
            SimTalker.nextDayAtCollege(server, runId);
        }

        // Attempt to fetch the college and load into
        // request attributes to pass to the jsp page.
        SimTalker.openCollegeAndStoreInRequest(server, runId, request);

        RequestDispatcher dispatcher=request.getRequestDispatcher("/viewdorm.jsp");
        dispatcher.forward(request, response);
    }
}
