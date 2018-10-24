package com.endicott.edu.ui;// Created by abrocken on 8/25/2017.


import com.endicott.edu.simulators.BuildingManager;
import com.endicott.edu.simulators.CollegeManager;
import com.endicott.edu.simulators.PopupEventManager;
//import com.endicott.edu.simulators.DormManager;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Base64;
import java.util.Enumeration;
import java.util.logging.Logger;

public class ViewBuildingsServlet extends javax.servlet.http.HttpServlet {
    private boolean beginPurchase = false;
    private boolean buildingTypeSelected = false;
    private String buildingType;
    private static Logger logger = Logger.getLogger("ViewBuildingsServlet");

    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        if (request.getParameter("purchaseBuilding") != null) {  // addDorm is present if addDorm button was pressed
            beginPurchase = false;
            buildingTypeSelected = false;
            String beginPurchaseStr = String.valueOf(beginPurchase);
            String buildingTypeSelectedStr = String.valueOf(buildingTypeSelected);
            request.setAttribute("beginBuildingPurchase", beginPurchaseStr);
            request.setAttribute("wasBuildingTypeSelected", buildingTypeSelectedStr);
            addBuilding(request, response);
            //doGet(request, response);
        }
        else if(request.getParameter("beginBuildingPurchase") != null){ //if they want to begin the process of purchasing a building
            beginPurchase = true; //begin attribute becomes true
            String beginStr = String.valueOf(beginPurchase);
            request.setAttribute("beginBuildingPurchase", beginStr);
            doGet(request, response);
        }
        else if(request.getParameter("selectBuildingType") != null){ //if they've selected the building type
            buildingTypeSelected = true;
            String buildingTypeSelectedStr = String.valueOf(buildingTypeSelected);
            request.setAttribute("wasBuildingTypeSelected", buildingTypeSelectedStr);
            buildingType = request.getParameter("buildingType");
            request.setAttribute("buildingType", buildingType);
            doGet(request, response);
        }
        else {
            // Might be selling a dorm.
            Enumeration<String> params = request.getParameterNames();

            while(params.hasMoreElements()) {
                // We're looking for a parameter whose value is "Sell" and whose name is BASE64 encoded dorm name.
                // We're doing this BASE64 encoding to handle issues caused by single quotes that could
                // appear in the name.
                String paramName = params.nextElement();
                String paramValue = request.getParameter(paramName);
                logger.info("Parameter Name - "+paramName+", Value - "+paramValue);
//                if (request.getParameter(paramName).equals("Sell")) {
//                    Base64.Decoder decoder = Base64.getDecoder();
//                    String dormName = new String(decoder.decode(paramName));
//                    logger.info("Selling dorm: " + dormName + " Encoded name: " + paramName);
//
//                    sellDorm(request, response, dormName);
//                }
            }
            String i = request.getParameter("sellDorm");
        }

    }
//    private void sellDorm(HttpServletRequest request, HttpServletResponse response, String dormName) throws ServletException, IOException {
//        String collegeId = InterfaceUtils.getCollegeIdFromSession(request);
//
//        logger.info("In ViewBuildingsServlet.sellDorm()");
//        InterfaceUtils.logRequestParameters(request);
//
//        if(collegeId == null || dormName == null){
//            UiMessage message = new UiMessage("Cannot delete dorm, information is missing");
//            request.setAttribute("message", message);
//            logger.info("Bad parameters for deleting a dorm");
//        }
//        else {
//            // Need to do the work here.
//            DormManager.sellDorm(collegeId, dormName);
//         }
//
//
//        //load the request with attributes for the dorm
//        InterfaceUtils.openCollegeAndStoreInRequest(collegeId, request);
//
//
//        RequestDispatcher dispatcher=request.getRequestDispatcher("/viewbuildings.jsp");
//        dispatcher.forward(request, response);
//        // Attempt to fetch the college and load into
//        // request attributes to pass to the jsp page.
//        InterfaceUtils.openCollegeAndStoreInRequest(collegeId, request);
//    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        String runId = InterfaceUtils.getCollegeIdFromSession(request);
        String beginStr = String.valueOf(beginPurchase);
        request.setAttribute("beginBuildingPurchase", beginStr); //begin attribute is originally false
        String buildingTypeSelectedStr = String.valueOf(buildingTypeSelected);
        request.setAttribute("wasBuildingTypeSelected", buildingTypeSelectedStr); //building type selected is originally false
        // Attempt to fetch the college and load into
        // request attributes to pass to the jsp page.
        InterfaceUtils.openCollegeAndStoreInRequest(runId, request);
        if(beginPurchase){
            RequestDispatcher dispatcher=request.getRequestDispatcher("/viewbuildings.jsp?hash=purchase");
            dispatcher.forward(request, response);
        }
        else{
            RequestDispatcher dispatcher=request.getRequestDispatcher("/viewbuildings.jsp");
            dispatcher.forward(request, response);
        }
    }

    private void addBuilding(HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        String runId = InterfaceUtils.getCollegeIdFromSession(request);
        String buildingName=request.getParameter("buildingName");
        //String buildingType=request.getParameter("buildingType");
        String test = buildingType;
        String buildingSize=request.getParameter("buildingSize");

       if(buildingSize.equals("$50,000 - Small (50)")){
           buildingSize = "Small";
       }
       else if(buildingSize.equals("$150,000 - Medium (200)")){
           buildingSize = "Medium";
       }
       else if(buildingSize.equals("$350,000 - Large (500)")){
           buildingSize = "Large";
       }
       else{
           buildingSize = "Extra Large";
       }
        //set the capacity
        //subtract the building cost amount from the balance

        logger.info("In ViewBuildingsServlet.doPost()");
        InterfaceUtils.logRequestParameters(request);

        if(runId == null || buildingName ==null || buildingType == null ){
            UiMessage message = new UiMessage("Cannot add dorm, information is missing");
            request.setAttribute("message", message);
            logger.severe("Parameters bad for adding a dorm.");
        }
        else{
            //change this so it takes in the building size as well
            if(buildingSize == null){
                buildingSize = "N/A";
            }
            BuildingManager.addBuilding(runId, buildingName, buildingType, buildingSize);
        }

        //load the request with attributes for the dorm
        InterfaceUtils.openCollegeAndStoreInRequest(runId, request);


        RequestDispatcher dispatcher=request.getRequestDispatcher("/viewbuildings.jsp");
        dispatcher.forward(request, response);
        // Attempt to fetch the college and load into
        // request attributes to pass to the jsp page.
        InterfaceUtils.openCollegeAndStoreInRequest(runId, request);
    }




}
