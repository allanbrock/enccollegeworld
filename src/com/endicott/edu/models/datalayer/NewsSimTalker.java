package com.endicott.edu.models.datalayer;
import com.endicott.edu.models.NewsFeedItemModel;
import com.endicott.edu.models.models.*;
import com.endicott.edu.models.ui.UiMessage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.glassfish.jersey.client.ClientConfig;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.logging.Logger;

public class NewsSimTalker {
    private static Logger logger = Logger.getLogger("NewsSimTalker");


    static public NewsFeedItemModel[] getNews(String server, String runId, UiMessage msg) {
        NewsFeedItemModel[] news;
        Client client = ClientBuilder.newClient(new ClientConfig());
        WebTarget webTarget = client.target(server + "newsfeed/" + runId);
        Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);

        Response response = invocationBuilder.get();
        String responseAsString = response.readEntity(String.class);
        Gson gson = new GsonBuilder().create();
        logger.info("Retreived news.");

        try {
            news = gson.fromJson(responseAsString, NewsFeedItemModel[].class);
        } catch (Exception e) {
            msg.setMessage(e.getMessage());
            logger.severe("Exception getting news: " + e.getMessage());
            return null;
        }
        return news;
    }
}

