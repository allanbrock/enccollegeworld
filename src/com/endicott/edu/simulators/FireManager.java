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
                popupManager.newPopupEvent("Fire in " + fire.getBuildingBurned().getName(), fire.getDescription(), "ok", "done", "resources/images/fire.png", "Plague Doctor");
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
        ArrayList<StudentModel> fireVictims = new ArrayList<>();

        Random rand = new Random();
        int randomIndex = rand.nextInt(buildings.size());
        BuildingModel buildingToBurn = buildings.get(randomIndex);

        FireModel fire = new FireModel(getFireCost(), getNumFatalities(), runId, buildingToBurn);

        if (buildingToBurn.getNumStudents() > 0 && getNumFatalities() > 0){
            for (int i =0; i == getNumFatalities(); i++){
                int studentIndex = rand.nextInt(students.size());
                StudentModel victim = students.get(studentIndex);
                fireVictims.add(victim);
                students.remove(studentIndex);

            }
            fire.setDescription(fireVictims);
        } else{
            fire.setNumOfFatalities(0);
        }

        FireDAO dao = new FireDAO();
        StudentDao studentDao = new StudentDao();
        List<FireModel> fires = FireDAO.getFires(runId);
        fires.add(fire);
        dao.saveNewFire(runId,fire);
        studentDao.saveAllStudents(runId,students);
        fireVictims.clear();



        NewsManager.createNews(runId, hoursAlive, "Fire detected at " + fire.getBuildingBurned().getName(), NewsType.COLLEGE_NEWS, NewsLevel.BAD_NEWS);
    }

    public void possiblyCreateFire(String runId, int hoursAlive){
        Random rand = new Random();
        if (rand.nextInt(100) <= 2){
            startFireRandomly(runId,hoursAlive);
        }
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
