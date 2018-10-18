package com.endicott.edu.simulators;

import com.endicott.edu.datalayer.BuildingDao;
import com.endicott.edu.datalayer.DormitoryDao;
import com.endicott.edu.datalayer.FireDAO;
import com.endicott.edu.models.BuildingModel;
import com.endicott.edu.models.DormModel;
import com.endicott.edu.models.DormitoryModel;
import com.endicott.edu.models.FireModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FireManager {
    FireDAO fireDao = new FireDAO();
    BuildingDao buildingDao = new BuildingDao();
    //DormitoryDao dormDao = new DormitoryDao();



    public void handleTimeChange(String runId, int hoursAlive, PopupEventManager popupManager){
        List<FireModel> fires = FireDAO.getFires(runId);
        popupManager.newPopupEvent("Fire","test of fire popup", "ok","done");

        // start fire
        startFireRandomly(runId);

        // alert popupManager about each fire
        for (FireModel fire: fires){
            //.newPopupEvent("Fire in " + fire.getBuildingBurned().getName(), fire.getDescription(), "sucks");
        }

        clearFires(runId);
        fireDao.saveAllFires(runId,fires);

    }

    public void startFireRandomly(String runId){
        ArrayList<BuildingModel> buildings = (ArrayList<BuildingModel>) buildingDao.getBuildings(runId);
        Random rand = new Random();
        int randomIndex = rand.nextInt(buildings.size());
        BuildingModel buildingToBurn = buildings.get(randomIndex);



        FireModel fire = new FireModel(getFireCost(), getNumFatalities(), runId, buildingToBurn);
        FireDAO dao = new FireDAO();
        ArrayList<FireModel> fires = (ArrayList) FireDAO.getFires(runId);
        fires.add(fire);
        dao.saveNewFire(runId,fire);
    }

    public void clearFires(String runId){
        FireDAO.deleteFires(runId);
    }

    public int getFireCost(){
        Random rand = new Random();
        int costOfFire = rand.nextInt() *  1000;
        return costOfFire;
    }

    public int getNumFatalities(){
        Random rand = new Random();
        int numDeaths = rand.nextInt() *  10;
        return numDeaths;
    }
}
