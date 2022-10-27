package com.endicott.edu.rest;

import com.endicott.edu.datalayer.SportsDao;
import com.endicott.edu.models.Coach;
import com.endicott.edu.models.SportModel;
import com.endicott.edu.simulators.CoachManager;
import com.endicott.edu.simulators.FacultyManager;
import com.endicott.edu.ui.InterfaceUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.List;
import java.util.logging.Logger;


public class SportsCoachesOptionServlet extends javax.servlet.http.HttpServlet {
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
            Type type = new TypeToken<List<SportsCoachesOptionServletModel>>() {
            }.getType();
            List<SportsCoachesOptionServletModel> theJson = g.fromJson(json, type);


            switch (theJson.get(0).getActionId()) {
                case "FIRE_COACH":
                    //FacultyManager.removeFaculty(theJson.get(0).getCollegeId(), theJson.get(0).getCoachModel(), true);
                    fireOrGiveRaise(theJson.get(0).getCoachModel().getFacultyID(), theJson.get(0).getCollegeId(), theJson.get(0).getActionId());
                    newResponse = "successfull";
                    break;
                case "GIVE_RAISE":
                    fireOrGiveRaise(theJson.get(0).getCoachModel().getFacultyID(), theJson.get(0).getCollegeId(), theJson.get(0).getActionId());
                    newResponse = "successfull";
                    break;
            }

            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, OPTIONS");
            response.setHeader("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With");

            if (newResponse == null) {
                newResponse = "successfull";
            }

            InterfaceUtils.openCollegeAndStoreInRequest(theJson.get(0).getCollegeId(), request);

            Temp t = new Temp("OK!", newResponse);
            sendAsJson(response, t);

        }catch (Exception e){
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, OPTIONS");
            response.setHeader("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With");
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

    private void fireOrGiveRaise(String facultyID, String collegeId, String actionId){
        for(int i = 0; i < CoachManager.getCollegeCoaches().size(); i++){
            if (facultyID.equals(CoachManager.getCollegeCoaches().get(i).getFacultyID()) && actionId.equals("GIVE_RAISE")){
                FacultyManager.giveFacultyRaise(collegeId, CoachManager.getCollegeCoaches().get(i), true);
            }

            if (facultyID.equals(CoachManager.getCollegeCoaches().get(i).getFacultyID()) && actionId.equals("FIRE_COACH")){
                FacultyManager.removeFaculty(collegeId, CoachManager.getCollegeCoaches().get(i), true);
            }
        }
    }
}

class SportsCoachesOptionServletModel{
    private Coach coachModel;
    private String actionId,collegeId;

    public SportsCoachesOptionServletModel(Coach coachModel, String actionId, String collegeId) {
        this.coachModel = coachModel;
        this.actionId = actionId;
        this.collegeId = collegeId;
    }

    public SportsCoachesOptionServletModel() {
    }

    public Coach getCoachModel() {
        return coachModel;
    }

    public void setCoachModel(Coach coachModel) {
        this.coachModel = coachModel;
    }

    public String getActionId() {
        return actionId;
    }

    public void setActionId(String actionId) {
        this.actionId = actionId;
    }

    public String getCollegeId() {
        return collegeId;
    }

    public void setCollegeId(String collegeId) {
        this.collegeId = collegeId;
    }
}