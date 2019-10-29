package com.endicott.edu.rest;

import com.endicott.edu.datalayer.BuildingDao;
import com.endicott.edu.datalayer.CollegeDao;
import com.endicott.edu.datalayer.SportsDao;
import com.endicott.edu.models.BuildingModel;
import com.endicott.edu.models.CollegeModel;
import com.endicott.edu.models.SportModel;
import com.endicott.edu.simulators.SportManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by Ran Ben David on 09/25/2019.
 */

public class SportsServlet extends javax.servlet.http.HttpServlet {
    private static Logger logger = Logger.getLogger("ViewAboutServlet");

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            BufferedReader br =
                    new BufferedReader(new InputStreamReader(request.getInputStream()));

            String json = "";
            String newResponse = "";
            if (br != null) {
                json = br.readLine();
                System.out.println(json);
            }
            Gson g = new Gson();
            Type type = new TypeToken<List<TT>>() {
            }.getType();
            List<TT> theJson = g.fromJson(json, type);


            switch (theJson.get(0).getActionId()) {
                case "ADD":
                    newResponse = SportManager.addNewTeam(theJson.get(0).getSportName(), theJson.get(0).getCollegeId());
                    break;
                case "SELL":
                    SportManager.deleteSelectedSport(theJson.get(0).getCollegeId(), theJson.get(0).getSportName());
                    newResponse = "successfull";
                    break;
            }

            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, OPTIONS");
            response.setHeader("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With");

            if (newResponse == null) {
                newResponse = "successfull";
            }


            Temp t = new Temp("OK!", newResponse);
            sendAsJson(response, t);
        }catch (Exception e){
            //
            sendAsJson(response, "ERROR");
        }

    }

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
        List<SportModel> buildings;
        buildings = SportsDao.getSports(collegeId);

        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With");

        sendAsJson(response, buildings);
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

class Temp{
    public Temp(String ok, String title) {
        this.ok = ok;
        this.title = title;
    }

    private String ok;
    private String title;
}

class TT{
    private String sportName, collegeId, actionId;


    public TT(String sportName, String collegeId, String actionId) {
        this.sportName = sportName;
        this.collegeId = collegeId;
        this.actionId = actionId;
    }

    public String getActionId() {
        return actionId;
    }

    public void setActionId(String actionId) {
        this.actionId = actionId;
    }

    public TT() {
    }

    public String getSportName() {
        return sportName;
    }

    public void setSportName(String sportName) {
        this.sportName = sportName;
    }

    public String getCollegeId() {
        return collegeId;
    }

    public void setCollegeId(String collegeId) {
        this.collegeId = collegeId;
    }
}