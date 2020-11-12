package com.endicott.edu.rest;

import com.endicott.edu.datalayer.CollegeDao;
import com.endicott.edu.datalayer.EverythingDao;
import com.endicott.edu.models.CollegeModel;
import com.endicott.edu.models.EverythingModel;
import com.endicott.edu.simulators.CollegeManager;
import com.endicott.edu.simulators.FinanceManager;
import com.endicott.edu.simulators.PopupEventManager;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;

public class RestCollegeServlet extends javax.servlet.http.HttpServlet {
    private static Logger logger = Logger.getLogger("RestCollegeServlet");

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("In RestCollegeServlet doPut");
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
            CollegeManager.advanceTime(collegeId, popupMgr);

            EverythingModel everything = EverythingDao.getEverything(collegeId);
            sendAsJson(response, everything);
            return;
        }

        logger.info("In RestCollegeServlet doPut " + collegeId);

        StringBuilder buffer = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            buffer.append(line);
        }

        String payload = buffer.toString();
        Gson gson = new Gson();
        CollegeModel collegeModel = gson.fromJson(payload, CollegeModel.class);
        logger.info("Attempting to save tuition. " + collegeId);
        CollegeDao.saveCollege(collegeModel);

        sendAsJson(response, collegeModel);
    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        logger.info("In RestCollegeServlet doGet");
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
            logger.info("Servlet command: " + command + " " + collegeId);
            PopupEventManager popupMgr = new PopupEventManager();  // This is a stub.  Must be changed.

            if(splits[2].equalsIgnoreCase("calculateContract")) {
                String amount = splits[3];
                int integerAmount = Integer.parseInt(amount);
                FinanceManager.calculateContract(integerAmount, collegeId);
            }
            else if(splits[2].equalsIgnoreCase("createContract")) {
                FinanceManager.createContract(collegeId);
            }
            else if(splits[2].equalsIgnoreCase("makePayment")) {
                String amount = splits[3];
                String loanNum = splits[4];
                int integerAmount = Integer.parseInt(amount);
                int integerLoanNum = Integer.parseInt(loanNum);
                FinanceManager.makePayment(collegeId, integerAmount, integerLoanNum);
            }
            else {
                CollegeManager.advanceTime(collegeId, popupMgr);
            }

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
