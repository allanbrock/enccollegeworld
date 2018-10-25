package com.endicott.edu.simulators;

import com.endicott.edu.datalayer.DormitoryDao;
import com.endicott.edu.datalayer.FloodDao;
import com.endicott.edu.models.DormitoryModel;
import com.endicott.edu.models.FloodModel;
import com.endicott.edu.models.NewsLevel;
import com.endicott.edu.models.NewsType;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Random;

/**
 * Responsible for simulating floods at the college.
 * NOTE: THERE CAN ONLY BE ONE FLOOD AT A TIME.
 */
public class FloodManager {
    private static final float PROBABILTY_OF_FLOOD_PER_HOUR = 0.05f;
    FloodDao floodDao = new FloodDao();
    DormitoryDao dormDao = new DormitoryDao();

    /**
     * Simulate changes in floods due to passage of time at college.
     * Dao - Data Access Object
     * @param collegeId
     * @param hoursAlive number of hours college has been active.
     */
    public void handleTimeChange(String collegeId, int hoursAlive, PopupEventManager popupManager) {
        List<FloodModel> floods = floodDao.getFloods(collegeId);
        List<DormitoryModel> dorms = dormDao.getDorms(collegeId);

//        popupManager.newPopupEvent("Flood!", "Oh no, there was a flood!", "Ok!");

        // If there are no floods, possibly start one.
        if (floods.size() <= 0) {
            possiblyStartFlood(collegeId, hoursAlive);
            return;
        }

        // Advance state of flood.
        for (FloodModel flood : floods) {
            String floodedDorm = flood.getDormName();
            for (DormitoryModel dorm : dorms) {
                if (dorm.getName().compareTo(floodedDorm) == 0)
                    billCostOfFlood(collegeId, hoursAlive, dorm);
            }
            // figures out how much times has passed since i updated floods
            // currentTime -lastTime
            int elapsedTime = hoursAlive - flood.getHourLastUpdated();
            int timeLeft = Math.max(0, flood.getHoursLeftInFlood() - elapsedTime);
            if (timeLeft <= 0) {
                floodDao.deleteFloods(collegeId);
                return;
            } else {
                flood.setHoursLeftInFlood(timeLeft);
            }

        }
        floodDao.saveAllFloods(collegeId, floods);
    }

    /**
     * Possibly start a flood at one of the dorms at the college.
     * The dorm must be fully built.
     *
     * @param collegeId
     * @param hoursAlive
     */
    private void possiblyStartFlood(String collegeId, int hoursAlive) {
        List<DormitoryModel> dorms = dormDao.getDorms(collegeId);

        for (DormitoryModel dorm : dorms) {
            if (dorm.getHoursToComplete() <= 0) {
                if (didFloodStartAtThisDorm(collegeId, hoursAlive, dorm)) {
                    return;
                }
            }
        }
    }

    /**
     * Charge the college for flood cleanup costs.
     *
     * @param collegeId
     * @param hoursAlive
     * @param dorm
     */
    private void billCostOfFlood(String collegeId, int hoursAlive, DormitoryModel dorm) {
        Random rand = new Random();
        Accountant.payBill(collegeId,"Flood cleanup cost for dorm " + dorm.getName(), rand.nextInt(500) + 500);
    }

    /**
     * Possibly start a flood at the given dorm.
     *
     * @param collegeId
     * @param hoursAlive
     * @param dorm
     * @return true if fllod started.
     */
    private boolean didFloodStartAtThisDorm(String collegeId, int hoursAlive, DormitoryModel dorm) {
        float oddsOfFlood = (hoursAlive - dorm.getHourLastUpdated()) * PROBABILTY_OF_FLOOD_PER_HOUR;
        if (Math.random() <= oddsOfFlood) {
            BuildingManager buildingMgr = new BuildingManager();
            int randomCost = (int)(Math.random()*1500) + 1000 ;
            int randomLength = (int) (Math.random() * 72) + 24;

            FloodModel flood = new FloodModel(randomCost, randomLength, randomLength, dorm.getHourLastUpdated(), dorm.getName(), collegeId);
            FloodDao floodDao = new FloodDao();
            floodDao.saveNewFlood(collegeId, flood);

            NewsManager.createNews(collegeId, hoursAlive, "Flooding detected at " + flood.getDormName(), NewsType.COLLEGE_NEWS, NewsLevel.BAD_NEWS);
            Accountant.payBill(collegeId, "Flood cost for dorm " + dorm.getName(), flood.getCostOfFlood());

            buildingMgr.floodAlert(hoursAlive , dorm.getName(), collegeId);
            return true;
        }

        return false;
    }

    /**
     * Take care of any initial flood set up when colege is first created.
     * @param collegeId
     */
    public static void establishCollege(String collegeId){
    }
}
