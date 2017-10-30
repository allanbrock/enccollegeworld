package com.endicott.edu.models.datalayer;

import org.glassfish.jersey.client.ClientConfig;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.logging.Logger;

public class DormSimTalker {
    private static Logger logger = Logger.getLogger("SimTalker");

    public static boolean addDorm(String server, String runId, String dormName, String dormType) {

        Client client = ClientBuilder.newClient(new ClientConfig());
        WebTarget webTarget = client.target(server + "dorms");
        Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
        String json = "{    \"runId\" : \"" + runId + "\"," +
                "   \"dormName\" : \"" + dormName + "\"," +
                "   \"dormType\" : \"" + dormType + "\"" +
                "}";
        logger.info("Creating a dorm: " + json);
        logger.info("URI" + server + "dorms");

        Response response = invocationBuilder.post(Entity.entity(json, MediaType.APPLICATION_JSON_TYPE));
        String responseAsString = response.readEntity(String.class);

        if(response.getStatus() != 200){
            logger.severe("Bad response: " + response.getStatus());
            return false;
        }
        else{
            logger.info("Good response: " + runId);
            return true;
        }


    }
}
