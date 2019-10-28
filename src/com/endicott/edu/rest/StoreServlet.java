package com.endicott.edu.rest;

import com.endicott.edu.datalayer.InventoryDao;
import com.endicott.edu.models.ItemModel;
import com.endicott.edu.simulators.InventoryManager;
import com.google.gson.Gson;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.logging.Logger;

public class StoreServlet extends javax.servlet.http.HttpServlet {
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
        // see if theres a second param, if theres a second, thats gotta be the item so get it and call buyitem
        logger.info("store " +pathInfo);
        if (splits.length >=2 && splits[2]!=null){
            String name = splits[2];
            InventoryManager.buyItem(name, collegeId);

            return;
        }
        List<ItemModel> items;
        items = InventoryDao.getItems(collegeId);
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With");
        sendAsJson(response, items);
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
