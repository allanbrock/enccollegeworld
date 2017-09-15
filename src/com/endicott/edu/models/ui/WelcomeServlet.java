package com.endicott.edu.models.ui;// Created by abrocken on 8/25/2017.

import com.endicott.edu.models.datalayer.SimTalker;
import com.endicott.edu.models.models.CollegeModel;
import com.endicott.edu.models.models.DormitoryModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.glassfish.jersey.client.ClientConfig;

import javax.servlet.RequestDispatcher;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.logging.Logger;

public class WelcomeServlet extends javax.servlet.http.HttpServlet {
    Logger logger = Logger.getLogger(getClass().getName());

    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        String runId=request.getParameter("runid");
        String server=request.getParameter("server");
        request.setAttribute("server", server);

        // Maybe the user wants to create the college...
        String buttonValue = request.getParameter("button");
        if (buttonValue != null && buttonValue.equals("Create College")) {
            if (!SimTalker.createCollege(server, runId)) {
                UiMessage msg = new UiMessage();
                msg.setMessage("Unable to create the college.  See log for details.");
                request.setAttribute("message", msg);
                RequestDispatcher dispatcher=request.getRequestDispatcher("/welcome.jsp");
                dispatcher.forward(request, response);
                return;
            }
        } else {
            logger.info("Open College button pressed: " + buttonValue);
        }

        // Attempt to fetch the college and load into
        // request attributes to pass to the jsp page.
        SimTalker.openCollegeAndStoreInRequest(server, runId, request);

        logger.info("Attribute college: " + request.getAttribute("college"));
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

    }

}
