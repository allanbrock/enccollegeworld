package com.endicott.edu.rest;

import com.endicott.edu.datalayer.BuildingDao;
import com.endicott.edu.datalayer.CollegeDao;
import com.endicott.edu.datalayer.EverythingDao;
import com.endicott.edu.models.BuildingModel;
import com.endicott.edu.models.CollegeModel;
import com.endicott.edu.models.EverythingModel;
import com.endicott.edu.simulators.BuildingManager;
import com.google.gson.Gson;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;

/**
 * Created by Ran Ben David on 09/25/2019.
 */

public class BuildingServlet extends javax.servlet.http.HttpServlet {
    private static Logger logger = Logger.getLogger("ViewAboutServlet");

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
        BuildingModel[] buildings;
        buildings = BuildingDao.getBuildingsArray(collegeId);

        if(splits[2].equalsIgnoreCase("repair")) {
            BuildingManager.repairBuilding(collegeId, BuildingManager.getBuildingByName(splits[3], collegeId));
        } else if (splits[2].equalsIgnoreCase("upgrade")) {
            BuildingManager.upgradeBuilding(collegeId, BuildingManager.getBuildingByName(splits[3], collegeId));
        } else if (splits[2].equalsIgnoreCase("purchase")) {
            BuildingManager.addBuilding(collegeId, splits[3], splits[4], "Medium");
        }

        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With");

        EverythingModel everything = EverythingDao.getEverything(collegeId);
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
