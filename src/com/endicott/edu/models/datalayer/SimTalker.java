package com.endicott.edu.models.datalayer; // Created by abrocken on 8/25/2017.

import com.endicott.edu.models.models.*;
import com.endicott.edu.models.ui.ServiceUtils;
import com.endicott.edu.models.ui.UiMessage;
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

    public static void openCollegeAndStoreInRequest(String server, String runId, HttpServletRequest request) {
        CollegeModel college;
        UiMessage msg = new UiMessage();

        college = CollegeSimTalker.getCollege(server, runId);
        if (college == null) {
            msg.setMessage("Failed to find college.");
            logger.info(msg.getMessage());
        } else {
            msg.setMessage("Found college: " + college.getRunId());
            logger.info("Found college: " + runId);
        }

        DormitoryModel[] dorms = DormSimTalker.getDormitories(server, runId, msg);
        NewsFeedItemModel[] news = NewsSimTalker.getNews(server, runId, msg);
        SportModel[] sport = SportsSimTalker.getSports(server, runId, msg);
        StudentModel[] students = StudentSimTalker.getStudents(server, runId, msg);

        request.setAttribute("message",msg);
        request.setAttribute("college",college);
        request.setAttribute("dorms",dorms);
        request.setAttribute("news",news);
        request.setAttribute("sports", sport);
        request.setAttribute("students",students);
    }






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



    public static boolean createCollege(String server, String runId) {
        CollegeModel college = new CollegeModel();
        college.setRunId(runId);

        logger.info("Creating the college " + runId);

        Client client = ClientBuilder.newClient(new ClientConfig());
        WebTarget webTarget = client.target(server + "college/" + runId);
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
