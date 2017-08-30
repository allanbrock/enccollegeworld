package com.endicott.edu.models.datalayer;// Created by abrocken on 8/25/2017.

import com.endicott.edu.models.models.CollegeModel;
import com.endicott.edu.models.models.DormitoriesModel;
import com.endicott.edu.models.models.DormitoryModel;
import com.endicott.edu.models.ui.ServiceUtils;
import com.endicott.edu.models.ui.UiMessage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.glassfish.jersey.client.ClientConfig;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.logging.Logger;

public class SimTalker {
    private static Logger logger = Logger.getLogger("SimTalker");

    public static void openCollegeAndStoreInRequest(String runId, HttpServletRequest request) {
        CollegeModel college;
        UiMessage msg = new UiMessage();

        college = SimTalker.getCollege(runId);
        if (college == null) {
            msg.setMessage("Failed to find college.");
            logger.info(msg.getMessage());
        } else {
            msg.setMessage("Found college: " + college.getRunId());
            logger.info("Found college: " + runId);
        }

        // Attempt to fetch dorms
        DormitoryModel[] dorms = SimTalker.getDormitories(runId, msg);

        logger.info("Setting attribute college: " + college);
        request.setAttribute("message",msg);
        request.setAttribute("college",college);
        request.setAttribute("dorms",dorms);
    }

    static public CollegeModel getCollege(String runId){
        CollegeModel college;
        Client client = ClientBuilder.newClient(new ClientConfig());
        WebTarget webTarget = client.target(ServiceUtils.LOCAL_SERVICE_URL + "college/" + runId);
        Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);

        Response response = invocationBuilder.get();
        String responseAsString = response.readEntity(String.class);
        Gson gson = new GsonBuilder().create();

        try {
            college = gson.fromJson(responseAsString, CollegeModel.class);
        } catch (Exception e) {
            logger.severe("Exception getting college: " + ServiceUtils.LOCAL_SERVICE_URL + "college/" + runId + " " + e.getMessage() + " College: " + responseAsString);
            return null;
        }

        return college;
    }

    static public  DormitoryModel[] getDormitories(String runId, UiMessage msg){
        DormitoryModel[] dorms;
        Client client = ClientBuilder.newClient(new ClientConfig());
        WebTarget webTarget = client.target(ServiceUtils.LOCAL_SERVICE_URL + "dorms/" + runId);
        Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);

        Response response = invocationBuilder.get();
        String responseAsString = response.readEntity(String.class);
        Gson gson = new GsonBuilder().create();

        try {
            dorms = gson.fromJson(responseAsString, DormitoryModel[].class);
        } catch (Exception e) {
            msg.setMessage(e.getMessage());
            return null;
        }
        return dorms;
    }

    static public CollegeModel nextDayAtCollege(String runId){
        CollegeModel college;
        Client client = ClientBuilder.newClient(new ClientConfig());
        WebTarget webTarget = client.target(ServiceUtils.LOCAL_SERVICE_URL + "college/" + runId + "/nextDay");
        Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);

        Response response = invocationBuilder.put(Entity.json(""));
        String responseAsString = response.readEntity(String.class);
        Gson gson = new GsonBuilder().create();

        try {
            college = gson.fromJson(responseAsString, CollegeModel.class);
        } catch (Exception e) {
            return null;
        }
        return college;
    }

    public static boolean createCollege(String runId) {
        CollegeModel college = new CollegeModel();
        college.setRunId(runId);

        logger.info("Creating college " + runId);

        Client client = ClientBuilder.newClient(new ClientConfig());
        WebTarget webTarget = client.target(ServiceUtils.LOCAL_SERVICE_URL + "college/" + runId);
        Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);

        // In the post we have a minimal JSON for the college just having the runID.
        // The other college attributes are set by the server.
        Response response = invocationBuilder.post(Entity.entity("{" +
                "   \"runId\" : \"" + runId + "\"" +
                "}", MediaType.APPLICATION_JSON_TYPE));
        String responseAsString = response.readEntity(String.class);

        if(response.getStatus() != 200) {
            logger.severe("Got bad college response:" + response.getStatus());
            return false;
        } else {
            logger.info("Got an ok response creating college: "  + runId);
            return true;
        }
    }
}
