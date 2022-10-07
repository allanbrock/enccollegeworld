package com.endicott.edu.rest;

import com.endicott.edu.datalayer.EverythingDao;
import com.endicott.edu.models.EverythingModel;
import com.google.gson.Gson;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class RestHelper {

    //a utility method to send object
    //as JSON response
    public static void sendAsJson(
            HttpServletResponse response,
            Object obj) throws IOException {

        response.setContentType("application/json");
        Gson gson = new Gson();
        String res = gson.toJson(obj);

        PrintWriter out = response.getWriter();

        out.print(res);
        out.flush();
    }

    public static void sendEverything(
            HttpServletResponse response,
            String collegeId) throws IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With");

        EverythingModel allOfEverything = EverythingDao.getEverything(collegeId);
        RestHelper.sendAsJson(response, allOfEverything);
    }
}
