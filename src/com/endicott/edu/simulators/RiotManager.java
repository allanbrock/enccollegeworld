package com.endicott.edu.simulators;
import java.util.Random;
import com.endicott.edu.models.SportModel;
import com.endicott.edu.simulators.SportManager;
import com.endicott.edu.models.RiotModel;
import com.endicott.edu.models.PopupEventModel;
import com.endicott.edu.simulators.PopupEventManager;

public class RiotManager {
    RiotModel currentRiot;

    //Just to test riot popups
    SportModel testSport = new SportModel("$50,000 - Men's Basketball");


//    public void handleTimeChange(String runId, int hoursAlive, PopupEventManager popupManager) {
//        Random rand = new Random();
//
//    }


    public void createSportsRiot(SportModel sport, RiotModel riot, PopupEventManager popupManager) {

        if (sport.getSportName().equals("$50,000 - Men's Basketball")) {
            riot.setName("Men's Basketball Riot");
            riot.setDescription("Your Men's Basketball team has won a conference championship! A riot has broke out on campus, it will cost you $" + riot.getRiotCost());
        } else if (sport.getSportName().equals("$50,000 - Women's Basketball")) {
            riot.setName("Women's Basketball Riot");
            riot.setDescription("Your Women's Basketball team has won a conference championship! A riot has broke out on campus, it will cost you $" + riot.getRiotCost());
        } else if (sport.getSportName().equals("$50,000 - Men's Soccer")) {
            riot.setName("Men's Soccer Riot");
            riot.setDescription("Your Men's Soccer team has won a conference championship! A riot has broke out on campus, it will cost you $" + riot.getRiotCost());
        } else if (sport.getSportName().equals("$50,000 - Women's Soccer")) {
            riot.setName("Women's Soccer Riot");
            riot.setDescription("Your Women's Soccer team has won a conference championship! A riot has broke out on campus, it will cost you $" + riot.getRiotCost());
        } else if (sport.getSportName().equals("$50,000 - Men's Football")) {
            riot.setName("Football Riot");
            riot.setDescription("Your Football team has won a conference championship! A riot has broke out on campus, it will cost you $" + riot.getRiotCost());
        } else if (sport.getSportName().equals("$50,000 - Baseball")) {
            riot.setName("Baseball Riot");
            riot.setDescription("Your Baseball team has won a conference championship! A riot has broke out on campus, it will cost you $" + riot.getRiotCost());
        } else if (sport.getSportName().equals("$50,000 - Softball")) {
            riot.setName("Softball Riot");
            riot.setDescription("Your Softball team has won a conference championship! A riot has broke out on campus, it will cost you $" + riot.getRiotCost());
        }


        popupManager.newPopupEvent(riot.getName(), riot.getDescription(), "Break up the Riot", "ok", "Let the Students Be", "ok", "web/resources/images/rioticon.png", "icon");



    }

    public boolean isEventActive() {
        return false;
    }
}

