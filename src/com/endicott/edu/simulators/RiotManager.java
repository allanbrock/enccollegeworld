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

    public void breakupRiot(String runID) {
        //Student Happiness?
        Random rand = new Random();
        int amt;
        amt = rand.nextInt(10);

        if(amt <= 1) {
        }
        else if(amt >= 1 && amt <= 2 )
        {

        }
        else if(amt >= 2 && amt <= 3 )
        {

        }
        else if(amt >= 3 && amt <= 4 )
        {

        }
        else if(amt >= 4 && amt <= 5 )
        {

        }
        else if(amt >= 5 && amt <= 6 )
        {

        }
        else if(amt >= 6 && amt <= 7 )
        {

        }
        else if(amt >= 7 && amt <= 8 )
        {

        }
        else if(amt >= 8 && amt <= 9 )
        {

        }
        else if(amt >= 9 && amt <= 10 )
        {

        }

    }

    public void letStudentsRiot(String runID) {
        Random rand = new Random();
        int amt;
        amt = rand.nextInt(10);

        if(amt <= 1)
        {
            Accountant.payBill(runID, "Miraculously, the Riot cost you $1000 in damages", 100 );
        }
        else if(amt >= 1 && amt <= 2 )
        {
            Accountant.payBill(runID, "The Riot cost you $2000 in damages", 2000 );
        }
        else if(amt >= 2 && amt <= 3 )
        {
            Accountant.payBill(runID, "The Riot cost you $3000 in damages", 3000 );
        }
        else if(amt >= 3 && amt <= 4 )
        {
            Accountant.payBill(runID, "The Riot cost you $4000 in damages", 4000 );
        }
        else if(amt >= 4 && amt <= 5 )
        {
            Accountant.payBill(runID, "The Riot cost you $5000 in damages", 5000 );
        }
        else if(amt >= 5 && amt <= 6 )
        {
            Accountant.payBill(runID, "The Riot cost you $6000 in damages", 6000 );
        }
        else if(amt >= 6 && amt <= 7 )
        {
            Accountant.payBill(runID, "The Riot cost you $7000 in damages", 7000 );
        }
        else if(amt >= 7 && amt <= 8 )
        {
            Accountant.payBill(runID, "The Riot cost you $8000 in damages", 8000 );
        }
        else if(amt >= 8 && amt <= 9 )
        {
            Accountant.payBill(runID, "The Riot cost you $9000 in damages", 9000 );
        }
        else if(amt >= 9 && amt <= 10 )
        {
            Accountant.payBill(runID, "Unfortunately, The Massive Riot cost you $12,500 in damages", 10000 );
        }



  }


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
            riot.setDescription("Your Softball team has won a conference championship! A riot has broke out on campus");
        }


        popupManager.newPopupEvent(riot.getName(), riot.getDescription(), "Break up the Riot", "ok", "Let it Be", "ok", "resources/images/rioticon.png", "icon");


    }

    public void createRegularRiot(RiotModel riot, PopupEventManager popupManager, String cause)
    {
        riot.setName(cause);
        riot.setDescription("Due to " + cause + ", your students have erupted a riot on campus");
        popupManager.newPopupEvent(riot.getName(), riot.getDescription(), "Break up the Riot", "ok", "Let it Be", "ok", "resources/images/rioticon.png", "icon");
    }

    public boolean isEventActive(String collegeId) {
        return false;
    }
}

