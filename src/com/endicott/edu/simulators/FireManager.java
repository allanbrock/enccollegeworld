package com.endicott.edu.simulators;

import com.endicott.edu.datalayer.BuildingDao;
import com.endicott.edu.datalayer.DormitoryDao;
import com.endicott.edu.datalayer.FireDAO;
import com.endicott.edu.models.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FireManager {
    FireDAO fireDao = new FireDAO();
    BuildingDao buildingDao = new BuildingDao();
    //DormitoryDao dormDao = new DormitoryDao();



    public void handleTimeChange(String runId, int hoursAlive, PopupEventManager popupManager){

        // start fire
        startFireRandomly(runId,hoursAlive);
        List<FireModel> fires = FireDAO.getFires(runId);


        // alert popupManager about each fire
        for (FireModel fire: fires){
            popupManager.newPopupEvent("Fire in " + fire.getBuildingBurned().getName(), fire.getDescription(), "ok","done");
        }

        fires.clear();
        fireDao.saveAllFires(runId,fires);

    }

    public void startFireRandomly(String runId,int hoursAlive){
        ArrayList<BuildingModel> buildings = (ArrayList<BuildingModel>) buildingDao.getBuildings(runId);
        Random rand = new Random();
        int randomIndex = rand.nextInt(buildings.size());
        BuildingModel buildingToBurn = buildings.get(randomIndex);

        FireModel fire = new FireModel(getFireCost(), getNumFatalities(), runId, buildingToBurn);
        FireDAO dao = new FireDAO();
        List<FireModel> fires = FireDAO.getFires(runId);
        fires.add(fire);
        dao.saveNewFire(runId,fire);

        NewsManager.createNews(runId, hoursAlive, "Fire detected at " + fire.getBuildingBurned().getName(), NewsType.COLLEGE_NEWS, NewsLevel.BAD_NEWS);
    }

    public void clearFires(String runId){
        FireDAO.deleteFires(runId);
    }

    public int getFireCost(){
        Random rand = new Random();
        int costOfFire = rand.nextInt(1000);
        return costOfFire;
    }

    public int getNumFatalities(){
        Random rand = new Random();
        int numDeaths = rand.nextInt(10);
        return numDeaths;
    }
}
