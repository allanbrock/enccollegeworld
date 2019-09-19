package com.endicott.edu.ui;// Created by abrocken on 8/25/2017.


import com.endicott.edu.datalayer.BuildingDao;
import com.endicott.edu.models.BuildingModel;
import com.endicott.edu.simulators.BuildingManager;
import com.endicott.edu.simulators.CollegeManager;
import com.endicott.edu.simulators.PopupEventManager;
import com.endicott.edu.simulators.TutorialManager;
import com.endicott.edu.datalayer.NameGenDao;
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
    private String buildingToUpgrade;
    private String sortByType;
    private String matchedConstantType;
    private String randomName;
    private static Logger logger = Logger.getLogger("ViewBuildingsServlet");

    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        InterfaceUtils.logRequestParameters(request);
        String collegeId = InterfaceUtils.getCollegeIdFromSession(request);

        // For loop to check if the repair or upgrade buttons were hit on one of the buildings.
        for(int b = 0; b < BuildingDao.getBuildings(collegeId).size(); b++) {
            if (request.getParameter("upgradeBuilding" + b) != null) {
                upgradeBuilding(request, response, BuildingDao.getBuildings(collegeId).get(b));
            }
            if (request.getParameter("repairBuilding" + b) != null){
                repairBuilding(request, response, BuildingDao.getBuildings(collegeId).get(b));
            }
        }
        /*if(request.getParameter("randomBuildingName") != null){
            // still need to add code handling the parameter
            String randomName = NameGenDao.generateBuildingName();
            request.setAttribute("randomName", randomName);
            doGet(request, response);
        }*/
        //handles the sort by button parameter
        if(request.getParameter("startSortByBuildingType") != null){
            sortByType = request.getParameter("sortByBuildingType");
            //needs to make the building type from the dropdown match one of the constants for the type of building
            matchedConstantType = convertToConsts(sortByType);
            request.setAttribute("sortByType", matchedConstantType);
            doGet(request, response);
        }
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
        else if(request.getParameter("randomBuildingName") != null){
            randomName = NameGenDao.generateBuildingName();
            request.setAttribute("randomName", randomName);
        }
        if(request.getParameter("selectBuildingType") != null || request.getParameter("randomBuildingName")!= null){ //(was else if) if they've selected the building type
            buildingTypeSelected = true;
            String buildingTypeSelectedStr = String.valueOf(buildingTypeSelected);
            request.setAttribute("wasBuildingTypeSelected", buildingTypeSelectedStr);
            if(request.getParameter("buildingType") != null){
                buildingType = request.getParameter("buildingType");
            }
            request.setAttribute("buildingType", buildingType);
            doGet(request, response);
        }

        // Handling the tips
        if (request.getParameter("nextTip") != null) {
            TutorialManager.advanceTip("viewBuildings", collegeId);
            doGet(request, response);
        }
        if (request.getParameter("hideTips") != null){
            TutorialManager.hideTips("viewBuildings", collegeId);
            doGet(request, response);
        }
        if (request.getParameter("showTips") != null){
            TutorialManager.showTips("viewBuildings", collegeId);
            doGet(request, response);
        }

        InterfaceUtils.openCollegeAndStoreInRequest(collegeId, request);

        RequestDispatcher dispatcher=request.getRequestDispatcher("/viewbuildings.jsp");
        dispatcher.forward(request, response);

    }

    /**
     * Converts the sort-by types in the dropdown to building constants
     *
     * @param sortByType
     * @return The correct const string
     */
    private String convertToConsts(String sortByType){
        if(sortByType.equals("Academic Center")){
            matchedConstantType = "ACADEMIC";
        }
        else if(sortByType.equals("Administrative Building")){
            matchedConstantType = "ADMIN";
        }
        else if(sortByType.equals("Baseball Diamond")){
            matchedConstantType = "BASEBALL DIAMOND";
        }
        else if(sortByType.equals("Dining Hall")){
            matchedConstantType = "DINING";
        }
        else if(sortByType.equals("Dormitory")){
            matchedConstantType = "DORM";
        }
        else if(sortByType.equals("Football Stadium")){
            matchedConstantType = "FOOTBALL STADIUM";
        }
        else if(sortByType.equals("Hockey Rink")){
            matchedConstantType = "HOCKEY RINK";
        }
        else if(sortByType.equals("Entertainment Center")){
            matchedConstantType = "ENTERTAINMENT";
        }
        else if(sortByType.equals("Health Center")){
            matchedConstantType = "HEALTH";
        }
        else if(sortByType.equals("Library")){
            matchedConstantType = "LIBRARY";
        }
        else{
            matchedConstantType = "All Buildings";
        }
        return matchedConstantType;
    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        logger.info("In doGet.");
        InterfaceUtils.logRequestParameters(request);
        String runId = InterfaceUtils.getCollegeIdFromSession(request);
        logger.info("College id is " + runId);
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

    /**
     * Handles when a building is purchased.
     *
     * @param request
     * @param response
     * @throws javax.servlet.ServletException
     * @throws IOException
     */
    private void addBuilding(HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        String runId = InterfaceUtils.getCollegeIdFromSession(request);
        String buildingName=request.getParameter("buildingName");
        if(buildingName == null){
            buildingName = randomName;
        }
        //String buildingType=request.getParameter("buildingType");
        String buildingSize=request.getParameter("buildingSize");

        // Some buildings (Sports Center) are missing the size.
        if(buildingSize == null){
            buildingSize = "N/A";
        }
        else if(buildingSize.equals("$50,000 - Small (50)")){
           buildingSize = "Small";
        }
        else if(buildingSize.equals("$150,000 - Medium (200)")){
           buildingSize = "Medium";
        }
        else if(buildingSize.equals("$350,000 - Large (500)")){
           buildingSize = "Large";
        }
        else if(buildingSize.equals("$650,000 - Extra Large (1000)")){
           buildingSize = "Extra Large";
        }


        logger.info("In ViewBuildingsServlet.doPost()");
        InterfaceUtils.logRequestParameters(request);

        if(runId == null || buildingName ==null || buildingType == null ){
            UiMessage message = new UiMessage("Cannot add dorm, information is missing");
            request.setAttribute("message", message);
            logger.severe("Parameters bad for adding a dorm.");
        }
        else{
            BuildingManager.addBuilding(runId, buildingName, buildingType, buildingSize);
        }

        //load the request with attributes for the building
        InterfaceUtils.openCollegeAndStoreInRequest(runId, request);


        RequestDispatcher dispatcher=request.getRequestDispatcher("/viewbuildings.jsp");
        dispatcher.forward(request, response);
        // Attempt to fetch the college and load into
        // request attributes to pass to the jsp page.
        InterfaceUtils.openCollegeAndStoreInRequest(runId, request);
    }

    /**
     * Handles when a building is upgraded.
     *
     * @param request
     * @param response
     * @param building
     * @throws javax.servlet.ServletException
     * @throws IOException
     */
    private void upgradeBuilding(HttpServletRequest request, HttpServletResponse response, BuildingModel building) throws javax.servlet.ServletException, IOException {
        String runId = InterfaceUtils.getCollegeIdFromSession(request);
        BuildingManager.upgradeBuilding(runId, building);

        doGet(request, response);

        //load the request with attributes for the building
        InterfaceUtils.openCollegeAndStoreInRequest(runId, request);

        // Attempt to fetch the college and load into
        // request attributes to pass to the jsp page.
        RequestDispatcher dispatcher=request.getRequestDispatcher("/viewbuildings.jsp");
        dispatcher.forward(request, response);
    }

    /**
     * Handles when a building is repaired.
     *
     * @param request
     * @param response
     * @param building
     * @throws javax.servlet.ServletException
     * @throws IOException
     */
    private void repairBuilding(HttpServletRequest request, HttpServletResponse response, BuildingModel building) throws javax.servlet.ServletException, IOException {
        String runId = InterfaceUtils.getCollegeIdFromSession(request);
        BuildingManager.repairBuilding(runId, building);

        doGet(request, response);

        //load the request with attributes for the building
        InterfaceUtils.openCollegeAndStoreInRequest(runId, request);

        // Attempt to fetch the college and load into
        // request attributes to pass to the jsp page.
        RequestDispatcher dispatcher=request.getRequestDispatcher("/viewbuildings.jsp");
        dispatcher.forward(request, response);
    }




}
