package com.endicott.edu.datalayer; // Created by Connor Frazier on 11/6/2017.

import com.endicott.edu.models.FloodModel;
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

public class FloodSimTalker {
    private static Logger logger = Logger.getLogger("FloodSimTalker");

    static public  FloodModel[] getFloods(String server, String runId, UiMessage msg){
        FloodModel[] floods;
        Client client = ClientBuilder.newClient(new ClientConfig());
        WebTarget webTarget = client.target(server + "floods/" + runId);
        Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);

        Response response = invocationBuilder.get();
        String responseAsString = response.readEntity(String.class);
        Gson gson = new GsonBuilder().create();

        try {
            floods = gson.fromJson(responseAsString, FloodModel[].class);
        } catch (Exception e) {
            msg.setMessage("Flood Failure" + e.getMessage());
            return null;
        }
        return floods;
    }
}
