package com.endicott.edu.datalayer; // Created by abrocken on 8/25/2017.


import com.endicott.edu.models.*;
import com.endicott.edu.ui.UiMessage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.glassfish.jersey.client.ClientConfig;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.logging.Logger;

public class SimTalker {
    private static Logger logger = Logger.getLogger("SimTalker");
    static public  DormitoryModel[] getDormitories(String server, String runId, UiMessage msg){
        DormitoryModel[] dorms;
        Client client = ClientBuilder.newClient(new ClientConfig());
        WebTarget webTarget = client.target(server + "dorms/" + runId);
        Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);

        Response response = invocationBuilder.get();
        String responseAsString = response.readEntity(String.class);
        Gson gson = new GsonBuilder().create();
        logger.info("Dorms as string: " +responseAsString);

        try {
            dorms = gson.fromJson(responseAsString, DormitoryModel[].class);
        } catch (Exception e) {
            msg.setMessage("Dorm Failure: " + e.getMessage());
            return null;
        }
        return dorms;
    }

//    static public  StudentModel[] getStudents(String server, String runId, UiMessage msg){
//        StudentModel[] students;
//        Client client = ClientBuilder.newClient(new ClientConfig());
//        WebTarget webTarget = client.target(server + "students/" + runId);
//        Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
//
//        Response response = invocationBuilder.get();
//        String responseAsString = response.readEntity(String.class);
//        Gson gson = new GsonBuilder().create();
//
//        try {
//            students = gson.fromJson(responseAsString, StudentModel[].class);
//        } catch (Exception e) {
//            msg.setMessage("Student Failure" + e.getMessage());
//            return null;
//        }
//        return students;
//    }




}
