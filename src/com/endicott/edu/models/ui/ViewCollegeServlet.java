package com.endicott.edu.models.ui;// Created by abrocken on 8/25/2017.

import com.endicott.edu.models.datalayer.SimTalker;
import com.endicott.edu.models.models.CollegeModel;
import com.endicott.edu.models.models.DormitoriesModel;
import com.endicott.edu.models.models.DormitoryModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.glassfish.jersey.client.ClientConfig;

import javax.servlet.RequestDispatcher;
import java.io.IOException;
import java.util.logging.Logger;
import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class ViewCollegeServlet extends javax.servlet.http.HttpServlet {

    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        String runId=request.getParameter("runid");

        if (request.getParameter("nextDayButton") != null) {
            SimTalker.nextDayAtCollege(runId);
        }

        // Attempt to fetch the college and load into
        // request attributes to pass to the jsp page.
        SimTalker.openCollegeAndStoreInRequest(runId, request);

        RequestDispatcher dispatcher=request.getRequestDispatcher("/viewcollege.jsp");
        dispatcher.forward(request, response);
    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {

    }
}
