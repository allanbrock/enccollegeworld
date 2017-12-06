package com.endicott.edu.ui;

import com.endicott.edu.datalayer.SimTalker;
import com.endicott.edu.datalayer.SportsSimTalker;
import com.endicott.edu.datalayer.CollegeSimTalker;


import javax.servlet.RequestDispatcher;
import java.io.IOException;
import java.util.logging.Logger;

public class ViewSportsServlet extends javax.servlet.http.HttpServlet {

    static private Logger logger = Logger.getLogger("ViewSportsServlet");

    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        if (request.getParameter("addSport") != null) {  // addSport is present if addSport button was pressed
            addSport(request, response);
        }else if (request.getParameter("sellSportBtn") !=null && request.getParameter("sellSportBtn").equals("Sell Sport") ){
            doDelete(request,response);
            return;
        }
    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        String runId=request.getParameter("runid"); //college ID
        String server=request.getParameter("server");
        request.setAttribute("server", server);

        if (request.getParameter("nextDayButton") != null) {
            CollegeSimTalker.nextDayAtCollege(server, runId);
        }

        // Attempt to fetch the college and load into
        // request attributes to pass to the jsp page.
        SimTalker.openCollegeAndStoreInRequest(server, runId, request);

        RequestDispatcher dispatcher=request.getRequestDispatcher("/viewsports.jsp");
        dispatcher.forward(request, response);
    }

    private void addSport(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        String runId=request.getParameter("runid"); //college ID
        String server=request.getParameter("server");
        String sportName=request.getParameter("sportName");

        logger.info("Attempting to add sport: " + sportName + " to " + runId + " at server " + server);
        if (runId == null || server == null || sportName == null) {
            UiMessage message = new UiMessage("Can't add a team because missing information");
            request.setAttribute("message", message);
            logger.severe("Parameters bad for adding sport.");
        }
        else {
            SportsSimTalker.addSport(runId, server, sportName);
            logger.info("Added sport: " + sportName + " to " + runId + " at server " + server);
        }

        request.setAttribute("server", server);
        SimTalker.openCollegeAndStoreInRequest(server, runId, request);

        RequestDispatcher dispatcher=request.getRequestDispatcher("/viewsports.jsp");
        dispatcher.forward(request,response);
    }

    protected void doDelete(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        String runId = request.getParameter("runid"); //college ID
        String server = request.getParameter("server");
        String name = request.getParameter("sellSportName");
        request.setAttribute("server", server);

        String buttonValue = request.getParameter("sellSportBtn");

        if(name != null && buttonValue.equals("Sell Sport")){

            SportsSimTalker.deleteSport(server,runId, name);
        }else {
            logger.info("Sell sport button was pressed: " + name + " -----------------------");
        }

        request.setAttribute("server", server);
        SimTalker.openCollegeAndStoreInRequest(server, runId, request);

        RequestDispatcher dispatcher=request.getRequestDispatcher("/viewsports.jsp");
        dispatcher.forward(request,response);


    }

}
