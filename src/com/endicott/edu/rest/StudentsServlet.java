package com.endicott.edu.rest;

import com.endicott.edu.datalayer.StudentDao;
import com.endicott.edu.models.StudentModel;
import com.google.gson.Gson;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;

/**
 * Created by Timothy Amello on 10/02/2019.
 */

public class StudentsServlet extends javax.servlet.http.HttpServlet {
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
        StudentModel[] students;
        students = StudentDao.getStudentsArray(collegeId);

        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With");

        RestHelper.sendAsJson(response, students);
    }
}
