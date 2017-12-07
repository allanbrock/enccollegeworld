package com.endicott.edu.ui;

import com.endicott.edu.datalayer.CollegeSimTalker;
import com.endicott.edu.datalayer.FacultySimTalker;

import javax.servlet.RequestDispatcher;
import java.io.IOException;
import java.util.Enumeration;
import java.util.logging.Logger;

public class ViewFacultyServlet extends javax.servlet.http.HttpServlet {

    static private Logger logger = Logger.getLogger("ViewFacultyServlet");

    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        if (request.getParameter("addFaculty") != null) {  // addFaculty is present if addFaculty button was pressed
            addFaculty(request, response);
        }
    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        String runId=request.getParameter("runid");
        String server=request.getParameter("server");
        request.setAttribute("server", server);

        if (request.getParameter("nextDayButton") != null) {
            CollegeSimTalker.nextDayAtCollege(server, runId);
        }

        // Attempt to fetch the college and load into
        // request attributes to pass to the jsp page.
        CollegeSimTalker.openCollegeAndStoreInRequest(server, runId, request);

        RequestDispatcher dispatcher=request.getRequestDispatcher("/viewfaculty.jsp");
        dispatcher.forward(request, response);
    }

    private void addFaculty(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        String runId=request.getParameter("runid"); //college ID
        String server=request.getParameter("server");
        String facultyName=request.getParameter("facultyName");

        logger.info("Attempting to add faculty: " + facultyName + " to " + runId + " at server " + server);
        if (runId == null || server == null || facultyName == null) {
            UiMessage message = new UiMessage("Can't add a faculty member because missing information");
            request.setAttribute("message", message);
            logger.severe("Parameters bad for adding faculty.");
        }
        else {
            FacultySimTalker.addFaculty(runId, server, facultyName);
            logger.info("Added faculty: " + facultyName + " to " + runId + " at server " + server);
        }

        request.setAttribute("server", server);
        CollegeSimTalker.openCollegeAndStoreInRequest(server, runId, request);

        RequestDispatcher dispatcher=request.getRequestDispatcher("/viewfaculty.jsp");
        dispatcher.forward(request,response);
    }

    private void logRequestParameters(javax.servlet.http.HttpServletRequest request) {
        Enumeration<String> params = request.getParameterNames();
        while(params.hasMoreElements()){
            String paramName = params.nextElement();
            logger.info("Parameter Name - "+paramName+", Value - "+request.getParameter(paramName));
        }
    }

}
