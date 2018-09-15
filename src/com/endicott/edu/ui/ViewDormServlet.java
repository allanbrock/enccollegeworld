package com.endicott.edu.ui;// Created by abrocken on 8/25/2017.


import com.endicott.edu.simulators.CollegeManager;
import com.endicott.edu.simulators.DormManager;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;
import java.util.Enumeration;
import java.util.logging.Logger;

public class ViewDormServlet extends javax.servlet.http.HttpServlet {
    private static Logger logger = Logger.getLogger("ViewDormServlet");

    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        if (request.getParameter("addDorm") != null) {  // addDorm is present if addDorm button was pressed
            addDorm(request, response);
        }
        else {
            // Might be selling a dorm.
            Enumeration<String> params = request.getParameterNames();

            while(params.hasMoreElements()) {
                // We're looking for a parameter whose value is "Sell" and whose name is BASE64 encoded dorm name.
                // We're doing this BASE64 encoding to handle issues caused by single quotes that could
                // appear in the name.
                String paramName = params.nextElement();
                String paramValue = request.getParameter(paramName);
                logger.info("Parameter Name - "+paramName+", Value - "+paramValue);
                if (request.getParameter(paramName).equals("Sell")) {
                    Base64.Decoder decoder = Base64.getDecoder();
                    String dormName = new String(decoder.decode(paramName));
                    logger.info("Selling dorm: " + dormName + " Encoded name: " + paramName);

                    sellDorm(request, response, dormName);
                }
            }
            String i = request.getParameter("sellDorm");
        }
    }

    private void sellDorm(HttpServletRequest request, HttpServletResponse response, String dormName) throws ServletException, IOException {
        String runId=request.getParameter("runid");
        String server=request.getParameter("server");
        request.setAttribute("server", server);

        logger.info("In ViewDormServlet.sellDorm()");
        logRequestParameters(request);

        logger.info("Attempting to delete dorm: " + dormName+ " from " + runId + " at server " + server);
        if(runId == null || server == null || dormName == null){
            UiMessage message = new UiMessage("Cannot delete dorm, information is missing");
            request.setAttribute("message", message);
            logger.info("Bad parameters for deleting a dorm");
        }
        else {
            // Need to do the work here.
            DormManager.sellDorm(runId, dormName);
            logger.info("Returned from attempt to delete dorm: " + dormName + " to " + runId + " at server " + server);
        }


        //load the request with attributes for the dorm
        request.setAttribute("server", server);
        InterfaceUtils.openCollegeAndStoreInRequest(server, runId, request);


        RequestDispatcher dispatcher=request.getRequestDispatcher("/viewdorm.jsp");
        dispatcher.forward(request, response);
        // Attempt to fetch the college and load into
        // request attributes to pass to the jsp page.
        InterfaceUtils.openCollegeAndStoreInRequest(server, runId, request);
    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        String runId=request.getParameter("runid");
        String server=request.getParameter("server");
        request.setAttribute("server", server);

        if (request.getParameter("nextDayButton") != null) {
            CollegeManager.nextDay(runId);
        }

        // Attempt to fetch the college and load into
        // request attributes to pass to the jsp page.
        InterfaceUtils.openCollegeAndStoreInRequest(server, runId, request);

        RequestDispatcher dispatcher=request.getRequestDispatcher("/viewdorm.jsp");
        dispatcher.forward(request, response);
    }

    private void addDorm(HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        String runId=request.getParameter("runid");
        String server=request.getParameter("server");
        request.setAttribute("server", server);
        String dormName=request.getParameter("dormName");
        String dormType=request.getParameter("dormType");

        logger.info("In ViewDormServlet.doPost()");
        logRequestParameters(request);

        logger.info("Attempting to add dorm: " + dormName + " to " + runId + " at server " + server);
        if(runId == null || server ==null ||dormName ==null || dormType == null){
            UiMessage message = new UiMessage("Cannot add dorm, information is missing");
            request.setAttribute("message", message);
            logger.severe("Parameters bad for adding a dorm.");
        }
        else{
            DormManager.addDorm(runId, dormName, dormType);
            logger.info("Returned from attempt to add dorm: " + dormName + " to " + runId + " at server " + server);
        }

        //load the request with attributes for the dorm
        request.setAttribute("server", server);
        InterfaceUtils.openCollegeAndStoreInRequest(server, runId, request);


        RequestDispatcher dispatcher=request.getRequestDispatcher("/viewdorm.jsp");
        dispatcher.forward(request, response);
        // Attempt to fetch the college and load into
        // request attributes to pass to the jsp page.
        InterfaceUtils.openCollegeAndStoreInRequest(server, runId, request);
    }

    private void logRequestParameters(javax.servlet.http.HttpServletRequest request) {
        Enumeration<String> params = request.getParameterNames();
        while(params.hasMoreElements()){
            String paramName = params.nextElement();
            logger.info("Parameter Name - "+paramName+", Value - "+request.getParameter(paramName));
        }
    }


}
