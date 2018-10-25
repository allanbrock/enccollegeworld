package com.endicott.edu.simulators;

import com.endicott.edu.datalayer.BuildingDao;
import com.endicott.edu.datalayer.DormitoryDao;
import com.endicott.edu.datalayer.FireDAO;
import com.endicott.edu.datalayer.StudentDao;
import com.endicott.edu.models.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FireManager {
    FireDAO fireDao = new FireDAO();
    BuildingDao buildingDao = new BuildingDao();



    public void handleTimeChange(String runId, int hoursAlive, PopupEventManager popupManager){

        // possibly start fire
        possiblyCreateFire(runId,hoursAlive);
        List<FireModel> fires = FireDAO.getFires(runId);

        // alert popupManager about each fire, currently only one exists fire at a time
        if (fires.size() != 0) {
            for (FireModel fire : fires) {
                popupManager.newPopupEvent("Fire in " + fire.getBuildingBurned().getName(), fire.getDescription(), "Buy Upgrade", "goToStore", "resources/images/fire.png", "Plague Doctor");
            }
        }

        fires.clear();
        fireDao.saveAllFires(runId,fires);

    }

    /**
     *
     * @param runId
     * @param hoursAlive
     *
     * Creates a fire in a random building
     * TODO: add dorms to the mix
     * TODO: add catastrophic chance
     */
    public void startFireRandomly(String runId,int hoursAlive){
        ArrayList<BuildingModel> buildings = (ArrayList<BuildingModel>) buildingDao.getBuildings(runId);
        ArrayList<StudentModel> students = (ArrayList<StudentModel>) StudentDao.getStudents(runId);
        List<FireModel> fires = FireDAO.getFires(runId);
        FireDAO dao = new FireDAO();
        StudentDao studentDao = new StudentDao();
        String victims = "";

        // if there are no buildings, there can not be a fire.
        if (buildings.size() <= 0)
            return;

        Random rand = new Random();
        int randomIndex = rand.nextInt(buildings.size());
        BuildingModel buildingToBurn = buildings.get(randomIndex);

        int numDeaths = getNumFatalities();
        FireModel fire = new FireModel(getFireCost(numDeaths), numDeaths, runId, buildingToBurn);

        //Lines 61-72 get the names of students who die in fire if there are and builds the description for the popup
        if (numDeaths > 0){
            for (int i =0; i < numDeaths; i++){
                int studentIndex = rand.nextInt(students.size());
                victims += students.get(studentIndex).getName().trim() + ", ";
                students.remove(studentIndex);
            }
            fire.setDescription(victims);
        } else{
            victims = "No one";
            fire.setDescription(victims);
        }

        fires.add(fire);
        dao.saveNewFire(runId,fire);
        studentDao.saveAllStudents(runId,students);
        Accountant.payBill(runId,"Fire damaged cost ", fire.getCostOfFire());
        NewsManager.createNews(runId, hoursAlive, "Fire in " + fire.getBuildingBurned().getName() + ", " + numDeaths + " died.", NewsType.COLLEGE_NEWS, NewsLevel.BAD_NEWS);
    }

    public void possiblyCreateFire(String runId, int hoursAlive){
        Random rand = new Random();
        if (rand.nextInt(100) <= 4){
            startFireRandomly(runId,hoursAlive);
        }
    }

    public void clearFires(String runId){
        FireDAO.deleteFires(runId);
    }

    public int getFireCost(int victims){
        Random rand = new Random();
        int costOfFire;
        if (getNumFatalities()>1) {
            costOfFire = rand.nextInt(1000);
            return costOfFire * victims;
        } else{
            costOfFire = rand.nextInt(1000);
            return costOfFire;
        }
    }

    public int getNumFatalities(){
        Random rand = new Random();
        int numDeaths = rand.nextInt(10);
        return numDeaths;
    }
}
