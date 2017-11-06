package com.endicott.edu.models.datalayer;

import com.endicott.edu.models.models.CollegeModel;
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

}
