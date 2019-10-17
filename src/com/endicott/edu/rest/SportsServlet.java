package com.endicott.edu.rest;

import com.endicott.edu.datalayer.BuildingDao;
import com.endicott.edu.datalayer.CollegeDao;
import com.endicott.edu.datalayer.SportsDao;
import com.endicott.edu.models.BuildingModel;
import com.endicott.edu.models.CollegeModel;
import com.endicott.edu.models.SportModel;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by Ran Ben David on 09/25/2019.
 */

public class SportsServlet extends javax.servlet.http.HttpServlet {
    private static Logger logger = Logger.getLogger("ViewAboutServlet");

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

//        if (username != null) {
//            request.getSession().setAttribute("user", user);
//            response.sendRedirect("home");
//        }
//        else {
//            request.setAttribute("error", "Unknown user, please try again");
//            request.getRequestDispatcher("/login.jsp").forward(request, response);
//        }

        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With");

        Temp t = new Temp("ok!", "It's worked!");
        sendAsJson(response, t);

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
