package com.endicott.edu.rest;

import com.endicott.edu.datalayer.CollegeDao;
import com.endicott.edu.datalayer.EverythingDao;
import com.endicott.edu.models.CollegeModel;
import com.endicott.edu.models.EverythingModel;
import com.endicott.edu.simulators.CollegeManager;
import com.endicott.edu.simulators.PopupEventManager;
import com.google.gson.Gson;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;

public class RestCollegeServlet extends javax.servlet.http.HttpServlet {
    private static Logger logger = Logger.getLogger("RestCollegeServlet");

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
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
        CollegeModel college;

        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With");

        // There may be a command after the college name.
        if (splits.length >= 3) {
            String command = splits[2];  // only command is next day at the moment.
            logger.info("Servlet command: " + command);
            PopupEventManager popupMgr = new PopupEventManager();  // This is a stub.  Must be changed.
            CollegeManager.advanceTimeByOneDay(collegeId, popupMgr);

            EverythingModel everything = EverythingDao.getEverything(collegeId);
            sendAsJson(response, everything);
            return;
        }

        college = CollegeDao.getCollege(collegeId);
        sendAsJson(response, college);
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
