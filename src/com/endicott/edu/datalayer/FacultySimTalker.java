package com.endicott.edu.datalayer;

import com.endicott.edu.models.FacultyModel;
import com.endicott.edu.models.StudentModel;
import com.endicott.edu.ui.UiMessage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.glassfish.jersey.client.ClientConfig;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.logging.Logger;
import javax.ws.rs.client.*;

public class FacultySimTalker {
    private static Logger logger = Logger.getLogger("FacultySimTalker");

    static public  FacultyModel[] getFaculty(String server, String runId, UiMessage msg){
        FacultyModel[] faculty;
        Client client = ClientBuilder.newClient(new ClientConfig());
        WebTarget webTarget = client.target(server + "faculty/" + runId);
        Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);

        Response response = invocationBuilder.get();
        String responseAsString = response.readEntity(String.class);
        Gson gson = new GsonBuilder().create();

        try {
            faculty = gson.fromJson(responseAsString, FacultyModel[].class);
        } catch (Exception e) {
            msg.setMessage("Faculty Failure" + e.getMessage());
            return null;
        }
        return faculty;
    }

    public static boolean addFaculty(String runId, String server, String facultyName) {
        Client client = ClientBuilder.newClient(new ClientConfig());
        WebTarget webTarget = client.target(server + "faculty/" + runId + "/" + facultyName);
        Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);

        String json = "{  \"runID\" : \"" + runId + "\"," +
                "    \"facultyName\" : \"" + facultyName + "\"" + "}";

        logger.info("Adding faculty: " + json);

        Response response = invocationBuilder.post(Entity.entity(json, MediaType.APPLICATION_JSON_TYPE));
        String responseAsString = response.readEntity(String.class);

        if (response.getStatus() != 200) {
            logger.severe("Add faculty: Got a bad response: " + response.getStatus());
            return false;
        }
        else {
            logger.info("Add faculty: Got a ok response: " + runId);
            return true;
        }
    }
}
