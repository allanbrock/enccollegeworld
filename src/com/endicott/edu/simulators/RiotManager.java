package com.endicott.edu.simulators;

import java.util.Random;

import com.endicott.edu.models.CollegeMode;
import com.endicott.edu.models.EventType;
import com.endicott.edu.models.SportModel;
import com.endicott.edu.models.RiotModel;


public class RiotManager {
    RiotModel currentRiot = new RiotModel();

    //Just to test riot popups



    public void handleTimeChange(String runId, int hoursAlive, PopupEventManager popupManager) {
        EventManager eventManager = new EventManager(runId);

        if (CollegeManager.isMode(runId, CollegeMode.DEMO_RIOT) || eventManager.doesEventStart(runId, EventType.RIOT)) {
            createRegularRiot(currentRiot, popupManager, randomRiotDescription());
            letStudentsRiot(runId);
        }

    }

    public Boolean randomRiotChance() {
        Random randRiot = new Random();
        int amt;
        amt = randRiot.nextInt(10);

        if (amt <= 2) {
            return true;
        } else {
            return false;
        }
    }

    public String randomRiotDescription() {
        Random randDesc = new Random();
        int amt;
        amt = randDesc.nextInt(10);

        if(amt <= 2) {
            return "Your Local City's sport's team won a world championship! A Riot has erupted on campus!";
        }
        else if(amt > 2 && amt <= 4) {
            return "A huge election just ended and the results have hit the public, your students riot over the results!";
        }
        else if(amt > 4 && amt <= 6) {
            return "Your students complain they are getting too much homework and erupt into a riot!";
        }
        else if(amt > 6 && amt <= 8) {
            return " Your students complain they are getting too much homework and erupt into a riot!";
        }
        else if(amt > 8 && amt <= 10) {
            return "There is a nationwide story of college students protesting and your students decided to join in!";
        }
        else
        {
            return "Nope";
        }

    }



    public void letStudentsRiot(String runID) {
        int amt = SimulatorUtilities.getRandomNumberWithNormalDistribution(5000, 2000, 0, 10000);

            Accountant.payBill(runID, "Unfortunately, The Massive Riot cost you " + amt + " in damages.", amt );



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
            riot.setDescription("Your Softball team has won a conference championship! A riot has broke out on campus");
        }


        popupManager.newPopupEvent(riot.getName(), riot.getDescription(), "Ok", "ok", "resources/images/rioticon.png", "icon");


    }

    //For Joe and others to make non-sport related riots
    public void createRegularRiot(RiotModel riot, PopupEventManager popupManager, String cause)
    {
        riot.setName(cause);
        riot.setDescription("A riot has started on campus!");

        popupManager.newPopupEvent(riot.getName(), riot.getDescription(), "Ok", "ok", "resources/images/rioticon.png", "icon");
    }

    public boolean isEventActive(String collegeId) {
        return false;
    }
}

