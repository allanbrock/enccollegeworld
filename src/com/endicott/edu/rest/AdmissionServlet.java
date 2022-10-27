package com.endicott.edu.rest;

import com.endicott.edu.datalayer.AdmissionsDao;
import com.endicott.edu.datalayer.EverythingDao;
import com.endicott.edu.models.AdmissionsModel;
import com.endicott.edu.models.EverythingModel;
import com.endicott.edu.models.PotentialStudent;
import com.google.gson.Gson;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.logging.Logger;

public class AdmissionServlet extends javax.servlet.http.HttpServlet {
    private static Logger logger = Logger.getLogger("ViewAboutServlet");
    private static List<PotentialStudent> temp;

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

        if(splits[2].equalsIgnoreCase("changeGroup")) {
            String group = splits[3];
            AdmissionsModel am = AdmissionsDao.getAdmissions(collegeId);
            am.setSelectedGroup(group);

            if(group.equals("GroupA")){
                temp = am.getGroupA();
            }
            else if(group.equals("GroupB")){
                temp = am.getGroupB();
            }
            else if(group.equals("GroupC")){
                temp = am.getGroupC();
            }
            else{
                logger.warning("No Groups Selected for Impacts!");
            }
            am.getAcademicImpact(temp);
            am.getAthleticImpact(temp);
            am.getSocialImpact(temp);

            AdmissionsDao.saveAdmissionsData(collegeId, am);
        }

        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With");

        EverythingModel allOfEverything = EverythingDao.getEverything(collegeId);
        sendAsJson(response, allOfEverything);
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

