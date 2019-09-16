package com.endicott.edu.ui;// Created by abrocken on 8/25/2017.


import com.endicott.edu.datalayer.CollegeDao;
import com.endicott.edu.simulators.CollegeManager;
import com.endicott.edu.simulators.PopupEventManager;

import javax.servlet.RequestDispatcher;
import java.io.IOException;
import java.util.logging.Logger;

public class WelcomeServlet extends javax.servlet.http.HttpServlet {
    private Logger logger = Logger.getLogger(getClass().getName());

    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        String collegeId=request.getParameter("runId");


        //struggled with two forms problem, this is a temporary solution
        //post recieves and sends to delete if the delete button was hit
        String buttonValue = request.getParameter("button");
        if (buttonValue != null && buttonValue.equals("Delete College")){
            doDelete(request, response);
            return;
        }

        // Maybe the user wants to create the college...
        if (buttonValue != null && buttonValue.equals("Create College")) {
            if (CollegeManager.establishCollege(collegeId) == null) {
                UiMessage msg = new UiMessage();
                msg.setMessage("Unable to create the college.  See glassfish server log for details.");
                request.setAttribute("message", msg);
                RequestDispatcher dispatcher=request.getRequestDispatcher("/welcome.jsp");
                dispatcher.forward(request, response);
                return;
            }
        } else {
            logger.info("Open College button was pressed: " + buttonValue);
        }

        // Maybe the college doesn't exist.
        if (!CollegeDao.doesCollegeExist(collegeId)) {
            UiMessage msg = new UiMessage("Can't open college " + collegeId);
            request.setAttribute("message", msg);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/welcome.jsp");
            dispatcher.forward(request, response);
            return;
        }
        // Attempt to fetch the college and load into
        // request attributes to pass to jsp page.
        InterfaceUtils.openCollegeAndStoreInRequest(collegeId, request);
        InterfaceUtils.setCollegeIdInSession(collegeId, request);
//        PopupEventManager popupMan = new PopupEventManager();
//        InterfaceUtils.setPopupManagerInSession(popupMan, request);

        RequestDispatcher dispatcher=request.getRequestDispatcher("/viewcollege.jsp");
        dispatcher.forward(request, response);

    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        request.setAttribute("host", "http://" + request.getServerName() + ":" + request.getServerPort() + "/enccollegesim/" );
        RequestDispatcher dispatcher = request.getRequestDispatcher("/welcome.jsp");
        dispatcher.forward(request, response);
    }


    protected void doDelete(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        String collegeId=request.getParameter("runId");

        String buttonValue = request.getParameter("button");

        if (buttonValue != null && buttonValue.equals("Delete College")) {
            CollegeManager.sellCollege(collegeId);
            UiMessage msg = new UiMessage("College deleted");
            request.setAttribute("message", msg);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/welcome.jsp");
            dispatcher.forward(request, response);
            return;
        }

    }


}
