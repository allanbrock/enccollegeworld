package com.endicott.edu.simulators;

import com.endicott.edu.datalayer.BuildingDao;
import com.endicott.edu.datalayer.FloodDao;
import com.endicott.edu.models.*;

import java.util.logging.Logger;
import java.util.List;
import java.util.Random;

/**
 * Responsible for simulating floods at the college.
 * NOTES:   - THERE CAN ONLY BE ONE FLOOD AT A TIME.
 *          - FLOODS CAN ONLY HAPPEN IN FULLY BUILT DORMS.
 */
public class FloodManager {
    private static final float PROBABILTY_OF_FLOOD_PER_HOUR = 0.004f;
    FloodDao floodDao = new FloodDao();
    BuildingDao buildingDao = new BuildingDao();
    BuildingManager buildingManager = new BuildingManager();
    InventoryManager inventoryManager = new InventoryManager();
    private Logger logger = Logger.getLogger("FloodManager");
    private boolean isHappening = false;

    /**
     * Simulate changes in floods due to passage of time at college. Called when One day goes by.
     *          -used in: CollegeManager (advanceTimeByOneDay)
     * Dao - Data Access Object
     * @param collegeId
     * @param hoursAlive number of hours college has been active.
     * @param popupManager popup manager instance
     */
    public void handleTimeChange(String collegeId, int hoursAlive, PopupEventManager popupManager) {
        Boolean hasUpgrade = hasUpgradeBeenPurchased(collegeId);

        FloodModel flood = FloodDao.getFlood(collegeId);
        List<BuildingModel> dorms = BuildingManager.getBuildingListByType(BuildingModel.getDormConst(), collegeId);

        //If there is NO flood, possibly start one:
        if (flood == null) {
            possiblyStartFlood(collegeId, hoursAlive, popupManager, hasUpgrade);
            return;
        }
        //If there IS a flood do the following:
        String floodedDorm = flood.getDormName();

        for (BuildingModel dorm : dorms) {
            if (dorm.getName().compareTo(floodedDorm) == 0){
                billCostOfFlood(collegeId, dorm);
            }
        }
        // Figures out how much times has passed since I updated floods
        // currentTime -lastTime
        int elapsedTime = hoursAlive - flood.getHourLastUpdated();
        int timeLeft = Math.max(0, flood.getHoursLeftInFlood() - elapsedTime);
        if (timeLeft <= 0) {
            isHappening = false;
            logger.info("EVARUBIO - FLOOD handleTimeChange() just set isHappening to false");
            logger.info("EVARUBIO - FLOOD handleTimeChange() value of isHappening = " + isHappening);

            buildingManager.disasterStatusChange(flood.getHoursLeftInFlood(),floodedDorm, collegeId, "None");
            logger.info("EVARUBIO . handleTimeChange() -> flood has been DELETED.");
            NewsManager.createNews(collegeId, hoursAlive, "Flooding of " + floodedDorm+" has ended! ", NewsType.COLLEGE_NEWS, NewsLevel.GOOD_NEWS);
            popupManager.newPopupEvent("Flood Ended!", "The flood in "+floodedDorm+" is finally over!","Ok","okFloodEnded",
                    "resources/images/DORM.png","Unflooded Dorm");
            FloodDao.deleteFlood(collegeId);
            return;
        } else {
            flood.setHoursLeftInFlood(timeLeft);
        }
        floodDao.saveTheFlood(collegeId, flood);
    }


    /**
     * Take care of any initial flood set up when college is first created.
     * @param collegeId
     */
    public static void establishCollege(String collegeId){
        InventoryManager.createItem("Drains",false,"drain.png",15000, 0, "Adding drains to your campus might get help when it rains.", collegeId);
        InventoryManager.createItem("Pipes", false, "pipes.png", 47000, 2, "Buying new underground pipes might help your college in the case of heavy rains.", collegeId);
    }

    /**
     * Possibly start a flood at one of the dorms at the college.
     * The dorm must be fully built.
     *
     * @param collegeId
     * @param hoursAlive
     */
    private void possiblyStartFlood(String collegeId, int hoursAlive, PopupEventManager popupManager, Boolean hasUpgrade) {
        List<BuildingModel> dorms = BuildingManager.getBuildingListByType(BuildingModel.getDormConst(), collegeId);

        logger.info(" EVARUBIO . possiblyStartFlood() START-OF-METHOD ");

        for (BuildingModel dorm : dorms) {
            if (dorm.getHoursToComplete() <= 0) {   //only if a dorm is FULLY built call didFloodStartAtThisDorm()
                logger.info(" EVARUBIO . possiblyStartFlood() there are dorms completed so call didFloodStartAtThisDorm() to check odds of creating a flood." );
                if (didFloodStartAtThisDorm(collegeId, hoursAlive, dorm, popupManager, hasUpgrade)) {     //if the odds say yes start a flood, if not nothing.
                    //logger.info(" EVARUBIO . possiblyStartFlood() didFloodStartAtThisDorm() good odds- is TRUE, FLOOD CREATED(3/6) in dorm:   " + dorm.getName() );
                    return;
                }
            }
        }
        logger.info(" EVARUBIO . possiblyStartFlood() END-OF-METHOD ");
    }

    /**
     * Possibly start a flood at the given dorm.
     *
     * @param collegeId
     * @param hoursAlive
     * @param dorm
     * @return true if flood started.
     */

    private boolean didFloodStartAtThisDorm(String collegeId, int hoursAlive, BuildingModel dorm, PopupEventManager popupManager, Boolean hasUpgrade) {
        if (!EventManager.isEventPermitted(collegeId)) {
            return false;
        }

        float oddsOfFlood = (hoursAlive - dorm.getTimeSinceLastRepair()) * PROBABILTY_OF_FLOOD_PER_HOUR;
        //If a flood upgrade was bought from the store, decrease the probability of floods.
        if(hasUpgrade){
            oddsOfFlood = oddsOfFlood - 0.02f;
        }
        if (Math.random() <= oddsOfFlood || CollegeManager.isMode(collegeId, CollegeMode.DEMO_FLOOD)) {
            BuildingManager buildingMgr = new BuildingManager();
            int randomCost = (int)(Math.random()*1500) + 1000 ;
            int randomLength = (int) (Math.random() * 72) + 24;

            FloodModel randomFlood = new FloodModel(randomCost, randomLength, randomLength, dorm.getTimeSinceLastRepair(), dorm.getName(), collegeId);

            FloodDao floodDao = new FloodDao();
            floodDao.saveTheFlood(collegeId, randomFlood);
            EventManager.isEventPermitted(collegeId);
            isHappening = true;
            logger.info("EVARUBIO - FLOOD . didFloodStartAtThisDorm()  just set isHappening to true ");
            logger.info("EVARUBIO FLOOD.  didFloodStartAtThisDorm() value of isHappening : " + isHappening);
            logger.info("EVARUBIO FLOOD.  didFloodStartAtThisDorm() FLOOD CREATED name of dorm:  " + dorm.getName() + "Duration: "+ randomLength );

            generateCorrectPopup(hasUpgrade,randomFlood,popupManager);

            NewsManager.createNews(collegeId, hoursAlive, "Flooding detected at " + randomFlood.getDormName(), NewsType.COLLEGE_NEWS, NewsLevel.BAD_NEWS);
            //Accountant.payBill(collegeId, "Flood cost for dorm " + dorm.getName(), randomFlood.getCostOfFlood());

            billCostOfFlood(collegeId, dorm);
            buildingMgr.disasterStatusChange(randomLength , dorm.getName(), collegeId, "Flooded");
            return true;
        }
        return false;
    }

    /**
     * Creates correct PopupEventManager for the flood
     * @param hasUpgrade Bool determining whether the user has bought the upgrade or not
     * @param theFlood the FloodModel for which the popup will be created
     * @param popupManager the PopupEventManager
     * */
    private void generateCorrectPopup(Boolean hasUpgrade, FloodModel theFlood, PopupEventManager popupManager){
        if(hasUpgrade){
            popupManager.newPopupEvent("Flood in  "+ theFlood.getDormName()+"!", "Oh no! "+theFlood.getDormName() +" has been flooded!","Ok","okFloodWithUpgrade",
                    "resources/images/DORM.png","Unflooded Dorm");
        }else{
            popupManager.newPopupEvent("Flood in "+ theFlood.getDormName()+"!", "Oh no! "+theFlood.getDormName() +" has been flooded! Would you like to visit the store to invest in more drains to reduce the probability of future floods? ",
                    "Go to Store","goToStore","No Thanks","doNothing", "resources/images/flood.png","flooded Dorm");
        }

    }

    /**
     * Checks if a flood upgrade has been purchased or not
     * future use: param upgradeName the name of the upgrade to check
     * @param collegeId the college ID
     * @return the Bool indicating if upgrade was bought or not
     * */
    private Boolean hasUpgradeBeenPurchased(String collegeId){
        return inventoryManager.isPurchased("Drains investment", collegeId);
    }

    /**
     * Charge the college for flood cleanup costs.
     *         - used in: FloodManager (handleTimeChange)
     * @param collegeId
     * @param dorm
     */
    private void billCostOfFlood(String collegeId, BuildingModel dorm){
        Random rand = new Random();
        Accountant.payBill(collegeId,"Flood cleanup cost for " + dorm.getName(), rand.nextInt(500) + 500);
    }

    /**
     * Determines whether there is a Flood currently happening or not.
     *
     * @param collegeId*/
    public boolean isEventActive(String collegeId) {
        return FloodDao.getFlood(collegeId) != null;
    }
}
