package com.endicott.edu.ui;// Created by abrocken on 8/25/2017.


import com.endicott.edu.simulators.CollegeManager;

import javax.servlet.RequestDispatcher;
import java.io.IOException;
import java.util.logging.Logger;

public class WelcomeServlet extends javax.servlet.http.HttpServlet {
    private Logger logger = Logger.getLogger(getClass().getName());

    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        String runId=request.getParameter("runid");

        //struggled with two forms problem, this is a temporary solution
        //post recieves and sends to delete if the delete button was hit
        String buttonValue = request.getParameter("button");
        if (buttonValue != null && buttonValue.equals("Delete College")){
            doDelete(request, response);
            return;
        }

        // Maybe the user wants to create the college...
        if (buttonValue != null && buttonValue.equals("Create College")) {
            if (CollegeManager.establishCollege(runId) == null) {
                UiMessage msg = new UiMessage();
                msg.setMessage("Unable to create the college.  See glassfish server log for details.");
                request.setAttribute("message", msg);
                RequestDispatcher dispatcher=request.getRequestDispatcher("/welcome.jsp");
                dispatcher.forward(request, response);
                return;
            }
        } else {
            logger.info("Open College button was pressed: " + buttonValue);
        }

        // Attempt to fetch the college and load into
        // request attributes to pass to JSP page.
        InterfaceUtils.openCollegeAndStoreInRequest(runId, request);
        InterfaceUtils.setCollegeIdInSession(runId, request);

        if (request.getAttribute("college") == null) {
            UiMessage msg = new UiMessage("Unable to open the college.  See log for details.");
            request.setAttribute("message", msg);
            RequestDispatcher dispatcher=request.getRequestDispatcher("/welcome.jsp");
            dispatcher.forward(request, response);
            return;
        }

        RequestDispatcher dispatcher=request.getRequestDispatcher("/viewcollege.jsp");
        dispatcher.forward(request, response);

    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        request.setAttribute("host", "http://" + request.getServerName() + ":" + request.getServerPort() + "/enccollegesim/" );
        RequestDispatcher dispatcher = request.getRequestDispatcher("/welcome.jsp");
        dispatcher.forward(request, response);
    }


    protected void doDelete(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        String runId=request.getParameter("runid");

        String buttonValue = request.getParameter("button");

        if (buttonValue != null && buttonValue.equals("Delete College")) {
            CollegeManager.sellCollege(runId);
            UiMessage msg = new UiMessage("College deleted");
            request.setAttribute("message", msg);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/welcome.jsp");
            dispatcher.forward(request, response);
            return;

        }

    }


}
