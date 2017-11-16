package com.endicott.edu.datalayer;


import com.endicott.edu.models.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.glassfish.jersey.client.ClientConfig;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.logging.Logger;


/**
 * created by Mazlin Higbee
 * 11-6-2017
 * mhigb411@mail.endicott.edu
 * Handle college operations
 */
public class CollegeSimTalker {
    private  static Logger logger = Logger.getLogger("CollegeSimTalker");


    public static boolean updateTuition(String server, String runId, String amount) {

        Client client = ClientBuilder.newClient(new ClientConfig());
        String uri = server + "college/" + runId + "/tuition" + "/" + amount;
        WebTarget webTarget = client.target(uri);
        Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);

        logger.info("Updating Tuition to : " + amount );
        logger.info("URI: " + uri);
        Entity<?> empty = Entity.text("");//the put method needs an entity but we have no payload so lets create a blank

        Response response = invocationBuilder.put(empty);
        String responseAsString = response.readEntity(String.class);

        if (response.getStatus() != 200) {
            logger.severe("Bad response: " + response.getStatus());
            return false;
        } else {
            logger.info("Good response: " + runId);
            return true;
        }
    }
    static public CollegeModel getCollege(String server, String runId){
        CollegeModel college;
        Client client = ClientBuilder.newClient(new ClientConfig());
        WebTarget webTarget = client.target(server + "college/" + runId);
        Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);

        Response response = invocationBuilder.get();
        String responseAsString = response.readEntity(String.class);
        Gson gson = new GsonBuilder().create();

        try {
            college = gson.fromJson(responseAsString, CollegeModel.class);
        } catch (Exception e) {
            logger.severe("Exception getting college: " + server + "college/" + runId + " " + e.getMessage() + " College: " + responseAsString);
            return null;
        }

        return college;
    }
    static public void deleteCollege(String server, String runId){
        Client client = ClientBuilder.newClient(new ClientConfig());
        WebTarget webTarget = client.target(server + "college/" + runId);
        Invocation.Builder invocationBuilder =  webTarget.request(MediaType.TEXT_PLAIN);

        Response response = invocationBuilder.delete();
        String responseAsString = response.readEntity(String.class);
        Gson gson = new GsonBuilder().create();

        try {
            String message = gson.fromJson(responseAsString, String.class);
        } catch (Exception e) {
            logger.severe("Exception getting college: " + server + "college/" + runId + " " + e.getMessage() + " College: " + responseAsString);
            return;
        }

        return;
    }

    static public CollegeModel nextDayAtCollege(String server, String runId){
        CollegeModel college;
        Client client = ClientBuilder.newClient(new ClientConfig());
        WebTarget webTarget = client.target(server + "college/" + runId + "/nextDay");
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
}
