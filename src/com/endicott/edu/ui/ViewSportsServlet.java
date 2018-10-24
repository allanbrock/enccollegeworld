package com.endicott.edu.ui;

import com.endicott.edu.simulators.CollegeManager;
import com.endicott.edu.simulators.PopupEventManager;
import com.endicott.edu.simulators.SportManager;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpSession;
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
        String collegeId = InterfaceUtils.getCollegeIdFromSession(request);

        // Attempt to fetch the college and load into
        // request attributes to pass to the jsp page.
        InterfaceUtils.openCollegeAndStoreInRequest(collegeId, request);

        RequestDispatcher dispatcher=request.getRequestDispatcher("/viewsports.jsp");
        dispatcher.forward(request, response);
    }

    private void addSport(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        String collegeId = InterfaceUtils.getCollegeIdFromSession(request);
        String sportName=request.getParameter("sportName");
        String addSportResultMsg = null;

        if (collegeId == null || sportName == null) {
            UiMessage message = new UiMessage("Can't add a team because missing information");
            request.setAttribute("message", message);
            logger.severe("Parameters bad for adding sport.");
        }
        else {
            addSportResultMsg = SportManager.addNewTeam(sportName,collegeId);
        }

        InterfaceUtils.openCollegeAndStoreInRequest(collegeId, request);
        request.setAttribute("addSportResultMsg", addSportResultMsg);

        RequestDispatcher dispatcher=request.getRequestDispatcher("/viewsports.jsp");
        dispatcher.forward(request,response);
    }

    protected void doDelete(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        String collegeId = InterfaceUtils.getCollegeIdFromSession(request);
        String name = request.getParameter("sellSportName");

        String buttonValue = request.getParameter("sellSportBtn");

        if(name != null && buttonValue.equals("Sell Sport")){
            SportManager.deleteSelectedSport(collegeId,name);
        }else {
            logger.info("Sell sport button was pressed: " + name + " -----------------------");
        }

        InterfaceUtils.openCollegeAndStoreInRequest(collegeId, request);

        RequestDispatcher dispatcher=request.getRequestDispatcher("/viewsports.jsp");
        dispatcher.forward(request,response);


    }

}
