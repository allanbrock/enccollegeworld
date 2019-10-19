package com.endicott.edu.rest;

import com.endicott.edu.datalayer.BuildingDao;
import com.endicott.edu.datalayer.EverythingDao;
import com.endicott.edu.models.BuildingModel;
import com.endicott.edu.models.EverythingModel;
import com.google.gson.Gson;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;

/**
 * Created by Ran Ben David on 09/25/2019.
 */

public class EverythingServlet extends javax.servlet.http.HttpServlet {

    protected void doGet(javax.servlet.http.HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        String[] splits = pathInfo.split("/");
        if(splits.length < 2) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        String collegeId = splits[1];
        EverythingModel everything = EverythingDao.getEverything(collegeId);


        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With");

        // Does college exist? A poor way to check is number of students.
        if (everything.students.length <= 0) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        sendAsJson(response, everything);
    }

    //a utility method to send object
    //as JSON response
    private void sendAsJson(
            HttpServletResponse response,
            Object obj) throws IOException {

        response.setContentType("application/json");
        Gson gson = new Gson();
        String res = gson.toJson(obj);

        PrintWriter out = response.getWriter();

        out.print(res);
        out.flush();
    }
}
