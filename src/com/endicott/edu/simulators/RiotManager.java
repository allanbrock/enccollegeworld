package com.endicott.edu.simulators;

import java.util.Random;

import com.endicott.edu.models.CollegeMode;
import com.endicott.edu.models.EventType;
import com.endicott.edu.models.SportModel;
import com.endicott.edu.models.RiotModel;


public class RiotManager {
    RiotModel currentRiot = new RiotModel();

    //Just to test riot popups
    SportModel testSport = new SportModel("$50,000 - Men's Basketball");


    public void handleTimeChange(String runId, int hoursAlive, PopupEventManager popupManager) {
        EventManager eventManager = new EventManager(runId);

        if (CollegeManager.isMode(runId, CollegeMode.DEMO_RIOT) || eventManager.doesEventStart(runId, EventType.RIOT)) {
            createSportsRiot(testSport, currentRiot, popupManager);
        }

    }


    public static void createSportsRiot(SportModel sport, RiotModel riot, PopupEventManager popupManager) {

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


        popupManager.newPopupEvent(riot.getName(), riot.getDescription(), "Break up the Riot", "ok", "Let it Be", "ok", "resources/images/rioticon.png", "icon");


    }

    public boolean isEventActive(String collegeId) {
        return false;
    }
}

