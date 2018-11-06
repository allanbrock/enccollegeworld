package com.endicott.edu.ui;// Created by abrocken on 8/25/2017.

import com.endicott.edu.datalayer.InventoryDao;
import com.endicott.edu.models.ItemModel;
import com.endicott.edu.simulators.InventoryManager;
import com.endicott.edu.simulators.TutorialManager;

import javax.servlet.RequestDispatcher;
import java.io.IOException;
import java.util.List;

public class ViewStoreServlet extends javax.servlet.http.HttpServlet {

    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        String collegeId = InterfaceUtils.getCollegeIdFromSession(request);
        List<ItemModel> items = InventoryDao.getItems(collegeId);

        for(int i = 0; i < items.size(); i++) {
            if (request.getParameter("buyItem" + i) != null) {
                String item = items.get(i).getName();
                InventoryManager.buyItem(item, collegeId);
            }
        }
        InterfaceUtils.openCollegeAndStoreInRequest(collegeId, request);

        RequestDispatcher dispatcher=request.getRequestDispatcher("/viewstore.jsp");
        dispatcher.forward(request, response);
    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        String collegeId = InterfaceUtils.getCollegeIdFromSession(request);

        // Attempt to fetch the college and load into
        // request attributes to pass to the jsp page.
        InterfaceUtils.openCollegeAndStoreInRequest(collegeId, request);

        RequestDispatcher dispatcher=request.getRequestDispatcher("/viewstore.jsp");
        dispatcher.forward(request, response);
    }

}
