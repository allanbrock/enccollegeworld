package com.endicott.edu.datalayer;


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


/**
 * created by Mazlin Higbee
 * 11-6-2017
 * mhigb411@mail.endicott.edu
 * Handle college operations
 */
public class CollegeSimTalker {
    private  static Logger logger = Logger.getLogger("CollegeSimTalker");

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
        SportModel[] availableSports = SportsSimTalker.getAvailableSports(server,runId,msg);
        StudentModel[] students = StudentSimTalker.getStudents(server, runId, msg);
        CollegeModel[] colleges = CollegeSimTalker.getAllColleges(server,runId,msg);
        FacultyModel[] faculty = FacultySimTalker.getFaculty(server, runId, msg);
        //FloodModel[] flood = FloodSimTalker.getFloods(server, runId, msg);
        FloodModel[] flood = new FloodModel[0];

        logger.info("Setting attribute college: " + college);
        request.setAttribute("message",msg);
        request.setAttribute("college",college);
        request.setAttribute("colleges",colleges);
        request.setAttribute("dorms",dorms);
        request.setAttribute("news",news);
        request.setAttribute("sports", sport);
        request.setAttribute("availableSports",availableSports);
        request.setAttribute("students",students);
        request.setAttribute("faculty",faculty);
        request.setAttribute("floods",flood);
    }
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


    /**
     * This function calls the service to update the given college's tuition.
     * @param server what endpoint does our request need to go to
     * @param runId simulation ID
     * @param amount how much does tuition cost now
     * @return true if 200 ok false if 400 or something bad.
     */
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

    static public CollegeModel[] getAllColleges(String server, String runId,UiMessage msg){
        CollegeModel[] colleges;
        Client client = ClientBuilder.newClient(new ClientConfig());
        WebTarget webTarget = client.target(server + "admin/getColleges" );
        Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);

        Response response = invocationBuilder.get();
        String responseAsString = response.readEntity(String.class);
        Gson gson = new GsonBuilder().create();

        try{
//            Type collectionType = new TypeToken<Collection<CollegeModel[]>>(){}.getType();
            colleges = gson.fromJson(responseAsString,CollegeModel[].class);
        } catch (Exception e){
            msg.setMessage("College failure" + e.getMessage());
            return null;
        }

        return colleges;
    }
}
