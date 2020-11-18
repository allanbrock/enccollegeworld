package com.endicott.edu.rest;
import com.endicott.edu.datalayer.CollegeDao;
import com.endicott.edu.simulators.CollegeManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by Ran Ben David on 11/17/2019.
 */

public class CreateDeleteCollegeServlet extends javax.servlet.http.HttpServlet {
    private static Logger logger = Logger.getLogger("ViewAboutServlet");

    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
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
            Type type = new TypeToken<List<CreateDeleteCollegeModel>>() {
            }.getType();
            List<CreateDeleteCollegeModel> theJson = g.fromJson(json, type);


            switch (theJson.get(0).getActionId()) {
                case "CREATE_COLLEGE":
                    newResponse = createNewCollege(theJson.get(0).getCollegeName());
                    break;
                case "DELETE_COLLEGE":
                    newResponse = deleteCollege(theJson.get(0).getCollegeName());
                    break;
            }

            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, OPTIONS");
            response.setHeader("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With");

            if (newResponse == null) {
                newResponse = "successfull";
            }

            //InterfaceUtils.openCollegeAndStoreInRequest(theJson.get(0).getCollegeId(), request);

            Temp t = new Temp("OK!", newResponse);
            sendAsJson(response, t);

        }catch (Exception e){
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, OPTIONS");
            response.setHeader("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With");
            sendAsJson(response, "ERROR");
        }
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

    private String createNewCollege(String collegeName){
        if (CollegeDao.doesCollegeExist(collegeName)){
            return "COLLEGE_EXIST";
        }
        // for some reason College creation for this servlet didn't
        // match what the WelcomeServlet was doing; now react creation works.
        if (CollegeManager.establishCollege(collegeName) == null) {
            return "COLLEGE_CREATE_FAILED";
        }
        return "successfull";
    }

    private String deleteCollege(String collegeName){
        if (!CollegeDao.doesCollegeExist(collegeName)){
            return "COLLEGE_DOES_NOT_EXIST";
        }

        CollegeDao.deleteCollege(collegeName);

        return "successfull";
    }
}


class CreateDeleteCollegeModel{
    private String actionId,collegeName;

    public CreateDeleteCollegeModel(String actionId, String collegeName) {
        this.actionId = actionId;
        this.collegeName = collegeName;
    }

    public String getActionId() {
        return actionId;
    }

    public void setActionId(String actionId) {
        this.actionId = actionId;
    }

    public String getCollegeName() {
        return collegeName;
    }

    public void setCollegeName(String collegeName) {
        this.collegeName = collegeName;
    }
}