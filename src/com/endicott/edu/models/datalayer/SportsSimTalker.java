package com.endicott.edu.models.datalayer;

import org.glassfish.jersey.client.ClientConfig;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.logging.Logger;

public class SportsSimTalker {
    private static Logger logger = Logger.getLogger("SportsSimTalker");

    public static boolean addSport(String runId, String server, String sportName) {
        Client client = ClientBuilder.newClient(new ClientConfig());
        WebTarget webTarget = client.target(server + "sports/" + runId + "/" + sportName);
        Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);

        String json = "{  \"runID\" : \"" + runId + "\"," +
                "    \"sportName\" : \"" + sportName + "\"" + "}";

        logger.info("Creating sport: " + json);

        Response response = invocationBuilder.post(Entity.entity(json, MediaType.APPLICATION_JSON_TYPE));
        String responseAsString = response.readEntity(String.class);

        if (response.getStatus() != 200) {
            logger.severe("Add sport: Got a bad response: " + response.getStatus());
            return false;
        }
        else {
            logger.info("Add sport: Got a ok response: " + runId);
            return true;
        }
    }
}
