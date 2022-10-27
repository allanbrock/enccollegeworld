package com.endicott.edu.simulators;

import com.endicott.edu.datalayer.*;
import com.endicott.edu.models.BuildingModel;
import com.endicott.edu.models.SnowModel;
import com.endicott.edu.models.NewsLevel;
import com.endicott.edu.models.NewsType;
import java.util.logging.Logger;

import com.endicott.edu.models.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Eva Rubio 11/05/2018
 *
 * Responsible for simulating Snow Storms at the college.
 *
 * NOTES:   - THERE CAN ONLY BE ONE SNOW STORM AT A TIME.
 *          - SNOW STORMS CAN ONLY HAPPEN DURING WINTER SEASON
 *          - ONLY HAPPEN ON FULLY BUILT BUILDINGS.
 *
 * */
public class SnowManager {
    private static final float PROBABILTY_OF_LOW_STORM = 40;
    private static final float PROBABILTY_OF_MID_STORM = 70;
    private static final float PROBABILTY_OF_HIGH_STORM = 100;
    private static final double PROBABILTY_OF_SNOWBALL_FIGTH_FOR_HIGH = 0.75;
    private static final double PROBABILTY_OF_SNOWBALL_FIGTH_FOR_MID = 0.50;
    private static final double PROBABILTY_OF_SNOWBALL_FIGTH_FOR_LOW = 0.25;
    private static final int START_OF_WINTER = 30;      //must be an int (dealing with whole days)
    private static final int END_OF_WINTER = 40;        //must be an int (dealing with whole days)
    private static final String lowUpgradeName = "Snow Pushers";
    private static final String midUpgradeName = "Pipes";
    private static final String highUpgradeName = "Snowplows";
    private static boolean isHappening = false;

    SnowDao snowDao = new SnowDao();
    BuildingDao buildingDao = new BuildingDao();
    BuildingManager buildingManager = new BuildingManager();
    StudentDao studentDao = new StudentDao();
    InventoryManager inventoryManager = new InventoryManager();
    private Logger logger = Logger.getLogger("SnowManager");

    /**
     * Simulates snow storms and changes in them due to passage of time at college. Called when One day goes by.
     *          -used in: CollegeManager (advanceTimeByOneDay)
     *
     * @param collegeId college name
     * @param hoursAlive number of hours college has been alive
     * @param popupManager popup manager instance
     */
    public void handleTimeChange(String collegeId, int hoursAlive, PopupEventManager popupManager) {
        SnowModel snowStorm = SnowDao.getSnowStorm(collegeId);
        //for future use
        List<BuildingModel> buildings = BuildingDao.getBuildings(collegeId);
        List<Student> students = StudentDao.getStudents(collegeId);
        List<Faculty> faculty = FacultyDao.getFaculty(collegeId);

        //snowSeasonPopup(hoursAlive,collegeId,popupManager);
        //if there is NO snow storm occurring, possibly start one:
        if (snowStorm == null ) {
            if(isItWinter(collegeId,hoursAlive,popupManager)){
                //logger.info("EVARUBIO - SNOW handleTimeChange() snow storm is NULL, gonna call possiblyCreateSnowStorm()  ");
                possiblyCreateSnowStorm(collegeId, hoursAlive, popupManager);
            }

            return;
        }
        //if there IS a snow storm happening do the following:
            // Figures out how much times has passed since I updated floods
            // currentTime -lastTime
        int elapsedTime = hoursAlive - snowStorm.getHourLastUpdated();
        int timeLeft = Math.max(0, snowStorm.getHoursLeftInSnowStorm() - elapsedTime);
        if (timeLeft <= 0) {
            generateCorrectPopup(snowStorm,true,popupManager, collegeId,hoursAlive);
            SnowDao.deleteSnowStorm(collegeId);
            return;
        } else {
            //logger.info("EVARUBIO . handleTimeChange() snow storm is currently happening, gonna call setHoursLeftInSnowStorm()  ");
            possiblyStartSnowballFight(snowStorm.getSnowIntensity(),collegeId,popupManager);
            snowStorm.setHoursLeftInSnowStorm(timeLeft);
        }
        snowDao.saveSnowStorm(collegeId,snowStorm);

    }
    /**
     * Determines whether or not it is winter season to create or not a Snow Storm.
     * Creates popup to notify user.
     * FOR NOW: winter season between days 30 and 40
     * @param collegeId
     * @param hoursAlive
     * @param popupManager
     * */
    public Boolean isItWinter(String collegeId, int hoursAlive,PopupEventManager popupManager){
        int currentDay = hoursAlive / 24 + 1;
        Boolean isCold = false;

        if(currentDay == START_OF_WINTER){
            NewsManager.createNews(collegeId, hoursAlive, "Winter is here.", NewsType.COLLEGE_NEWS, NewsLevel.BAD_NEWS);
            popupManager.newPopupEvent(collegeId, "Winter is here!", "Winter is here, and with it.. Snow Storms! Stay warm and pay attention to possible weather changes. ",
                    "Ok","okWinterStarted",
                    "resources/images/winterIcon.png","winter season icon");
        }else if (currentDay == END_OF_WINTER){
            NewsManager.createNews(collegeId, hoursAlive, "Spring has arrived.", NewsType.COLLEGE_NEWS, NewsLevel.GOOD_NEWS);
            popupManager.newPopupEvent(collegeId, "Goodbye snow..Spring is here!", "After waiting all winter long, we finally get to feel the sun and warmth of Spring. Don't forget to stop and smell the flowers! ",
                    "Ok","okSpringStarted",
                    "resources/images/springIcon.png","spring season icon");
            //logger.info("EVARUBIO SNOW isItWinter() ES EL END_OF_WINTER !!!! popupshould be here. currentDay = " +currentDay);
        }

        if(currentDay>= START_OF_WINTER && currentDay <= END_OF_WINTER){
            isCold = true;

        }
        //logger.info("EVARUBIO - SNOW isItWinter() value: "+isCold);
        return isCold;
    }
    /**
     * Creates a low/mid/high intensity snow storm depending on the odds.
     * Method called between specific days: 30 and 40.
     *
     * @param collegeId
     * @param hoursAlive
     *
     * use play mode.
     */
    public void possiblyCreateSnowStorm(String collegeId, int hoursAlive,PopupEventManager popupManager) {
        if (!EventManager.isEventPermitted(collegeId)) {
            //logger.info("EVARUBIO . SNOW . possiblyCreateSnowStorm() - the Event was NOT permitted. ");
            return;
        }

        Boolean hasLowUpgrade = hasSpecificUpgradePurchased(lowUpgradeName, collegeId);
        Boolean hasMidUpgrade = hasSpecificUpgradePurchased(midUpgradeName, collegeId);
        Boolean hasHighUpgrade = hasSpecificUpgradePurchased(highUpgradeName, collegeId);

        Random rand = new Random();
        int oddsOfStorm = rand.nextInt(220);
        //logger.info("EVARUBIO . SNOW. possiblyCreateSnowStorm() Random oddsOfStorm: " + oddsOfStorm);

        EventManager eventManager = new EventManager(collegeId);
        if (CollegeManager.isMode(collegeId, CollegeMode.DEMO_SNOW) || eventManager.doesEventStart(collegeId, EventType.SNOW)) {
            // We are going to have a snow storm.  It's just a question of which one.
            // This logic probably needs to be adjusted.  It creates a high intensity snow
            // storm as a last resort.

            //  ---- LOW SNOW ----      (0 - 40 : 40)
            if (oddsOfStorm <= PROBABILTY_OF_LOW_STORM && !hasLowUpgrade) {
                startLowIntensitySnow(collegeId,hoursAlive,popupManager);
                // 0 - 30 : 30
            }else if ((oddsOfStorm <= (PROBABILTY_OF_LOW_STORM - 10)) && hasLowUpgrade){
                startLowIntensitySnow(collegeId, hoursAlive, popupManager);
                //logger.info("EVARUBIO . possiblyCreateSnowStorm() has LOW Upgrade, probability has been decreased by 10. ");
                //  ---- MID SNOW ----      (40 - 70 : 30)
            }else if((oddsOfStorm > PROBABILTY_OF_LOW_STORM) && (oddsOfStorm <= PROBABILTY_OF_MID_STORM) && !hasMidUpgrade) {
                startMidIntensitySnow(collegeId, hoursAlive, popupManager);

            }else if ((oddsOfStorm > (PROBABILTY_OF_LOW_STORM + 10)) && (oddsOfStorm <= PROBABILTY_OF_MID_STORM) && hasMidUpgrade){
                startMidIntensitySnow(collegeId, hoursAlive, popupManager);
                //logger.info("EVARUBIO . possiblyCreateSnowStorm() has MID Upgrade, probability has been decreased by 10. ");
                //  ---- HIGH SNOW ----     (70 - 100 : 30)
            }else if((oddsOfStorm > PROBABILTY_OF_MID_STORM) && (oddsOfStorm <= PROBABILTY_OF_HIGH_STORM) && !hasHighUpgrade) {
                startHighIntensitySnow(collegeId, hoursAlive, popupManager);
            }else if ((oddsOfStorm > (PROBABILTY_OF_MID_STORM + 10)) && (oddsOfStorm <= PROBABILTY_OF_HIGH_STORM) && hasHighUpgrade){
                startHighIntensitySnow(collegeId, hoursAlive, popupManager);
                //logger.info("EVARUBIO . possiblyCreateSnowStorm() has HIGH Upgrade, probability has been decreased by 10. ");

            } else {
                startHighIntensitySnow(collegeId,hoursAlive,popupManager);
            }
        }
    }
    /**
     * If a snow storm is currently happening, and the upgrade was NOT bought,
     * create a snowball fight:
     *      - intensity 1: 25% chance of snowball fight happening
     *      - intensity 2: 50% chance of snowball fight happening
     *      - intensity 3: 75% chance of snowball fight happening
     * */
    public void possiblyStartSnowballFight(int stormIntensity, String collegeId, PopupEventManager popupManager){
        Random rand = new Random();
        double fightChance = rand.nextDouble();

        if((stormIntensity == 1) && (fightChance < PROBABILTY_OF_SNOWBALL_FIGTH_FOR_LOW)){
            generateFightPopup(collegeId, popupManager);
            Accountant.payBill(collegeId,"Cost of snowball fight damages ascends to: " , 1500 );
        } else if((stormIntensity == 2) && (fightChance < PROBABILTY_OF_SNOWBALL_FIGTH_FOR_MID)){
            generateFightPopup(collegeId, popupManager);
            Accountant.payBill(collegeId,"Cost of snowball fight damages ascends to: " , 2500 );
        } else if((stormIntensity == 3) && (fightChance < PROBABILTY_OF_SNOWBALL_FIGTH_FOR_HIGH)){
            generateFightPopup(collegeId, popupManager);
            Accountant.payBill(collegeId,"Cost of snowball fight damages ascends to: " , 3700 );
        }


    }

    /**
     * Generates the correct popup in the event that a snowball fight is happening
     * @param popupManager The popupEventManager
     * */
    public void generateFightPopup(String collegeId, PopupEventManager popupManager){
        // for future use (alternating them)
        String grinchImgPath = "resources/images/grinch-snowball-fight.png";
        String handsImgPath ="resources/images/hands-snowball-fight.png";
        popupManager.newPopupEvent(collegeId,"Snowball Fight Outburst!",
                "Oh no! The un-plowed amount of snow was too tempting for students.. A campus-wide Snowball Fight is in progress, Maintenance won't be happy.",
                "Ok","okSnowballFight",
                "resources/images/grinch-snowball-fight.png","grinch snowball fight");
        //logger.info("EVARUBIO SNOW . possiblyStartSnowballFight() - snowball fight happening! ");
    }
    /**
     * Starts a Low intensity Snow Storm.
     *
     *      title: Weather Alert: Low Intensity Snow Storm
     *      item: Snow Pusher
     * */
    public void startLowIntensitySnow(String collegeId, int hoursAlive, PopupEventManager popupManager){
        EventManager.newEventStart(collegeId);
        BuildingManager buildingMgr = new BuildingManager();
        SnowDao snowDao = new SnowDao();
        int intensity = 1;
        isHappening = true;
        List<BuildingModel> buildings = BuildingDao.getBuildings(collegeId);
        BuildingModel oneBuildingSnowed = getRandCompletedBuilding(buildings);
        int lengthOfStorm = generateLengthOfSnow(intensity);
        int lowRandCost = generateCostOfSnow(intensity,hasSpecificUpgradePurchased(lowUpgradeName,collegeId));

        SnowModel lowSnow = new SnowModel(collegeId,oneBuildingSnowed,intensity,lowRandCost, lengthOfStorm, lengthOfStorm, oneBuildingSnowed.getTimeSinceLastRepair());
        snowDao.saveSnowStorm(collegeId,lowSnow);

        generateCorrectPopup(lowSnow,false, popupManager,collegeId,hoursAlive);

        billCostOfSnowStorm(collegeId,lowSnow);
        buildingMgr.disasterStatusChange(lengthOfStorm , oneBuildingSnowed.getName(), collegeId, "Snowed In");
    }
    /**
     * Starts a Mid intensity Snow Storm.
     * Accelerates the decay of the snowed in building.
     *
     *      title: Weather Alert: Mid Intensity Blizzard Warning.
     *      item: Frozen Pipes
     *
     *      Blizzard WARNING: Sustained winds or frequent gusts of 35 miles per hour or greater,
     *      plus considerable falling or blowing snow reducing visibility to less than a quarter mile.
     * */
    private void startMidIntensitySnow(String collegeId, int hoursAlive, PopupEventManager popupManager) {
        EventManager.newEventStart(collegeId);
        BuildingManager buildingMgr = new BuildingManager();
        SnowDao snowDao = new SnowDao();
        int intensity = 2;
        isHappening = true;
        List<BuildingModel> buildings = BuildingDao.getBuildings(collegeId);
        BuildingModel oneBuildingSnowed = getRandCompletedBuilding(buildings);
        buildingMgr.acceleratedDecay(collegeId, oneBuildingSnowed.getName(), "disaster");
        int lengthOfStorm = generateLengthOfSnow(intensity);
        int midRandCost = generateCostOfSnow(intensity,hasSpecificUpgradePurchased(midUpgradeName,collegeId));

        SnowModel midSnow = new SnowModel(collegeId,oneBuildingSnowed,intensity,midRandCost, lengthOfStorm, lengthOfStorm, oneBuildingSnowed.getTimeSinceLastRepair());
        snowDao.saveSnowStorm(collegeId,midSnow);

        generateCorrectPopup(midSnow,false, popupManager,collegeId,hoursAlive);

        billCostOfSnowStorm(collegeId, midSnow);
        buildingMgr.disasterStatusChange(lengthOfStorm , oneBuildingSnowed.getName(), collegeId, "Snowed In");
    }

    /**
     * Starts a High intensity Snow Storm.      Severe Snow Storm. Winter Storm Warning.
     * Accelerates the decay of the snowed in buildings.
     *
     *       title: URGENT - WINTER WEATHER MESSAGE
     *       item: Snow Plows
     *
     * */
    private void startHighIntensitySnow(String collegeId, int hoursAlive, PopupEventManager popupManager) {
        EventManager.newEventStart(collegeId);
        BuildingManager buildingMgr = new BuildingManager();
        SnowDao snowDao = new SnowDao();
        int intensity = 3;
        isHappening = true;
        List<BuildingModel> buildings = BuildingDao.getBuildings(collegeId);
        List<BuildingModel> buildingsSnowedIn = new ArrayList<>();
        int lengthOfStorm = generateLengthOfSnow(intensity);
        int randSevereCost = generateCostOfSnow(intensity,hasSpecificUpgradePurchased(highUpgradeName,collegeId));
        //int randSickSutd = getRandomNumOfSickStudents(collegeId,intensity);

        if (buildings.size() <= 0) {
            return;
        }
        //To create the List of Affected (snowed in) buildings:
        BuildingModel oneBuild = getRandCompletedBuilding(buildings);
        BuildingModel twoBuild = getRandCompletedBuilding(buildings);
        while (oneBuild == twoBuild){
            twoBuild = getRandCompletedBuilding(buildings);
        }
        buildingsSnowedIn.add(oneBuild);
        buildingMgr.acceleratedDecay(collegeId, oneBuild.getName(), "decay");
        buildingMgr.acceleratedDecay(collegeId, twoBuild.getName(), "decay");
        buildingsSnowedIn.add(twoBuild);

        SnowModel intenseSnowStorm = new SnowModel(collegeId, buildingsSnowedIn, intensity, randSevereCost, lengthOfStorm, lengthOfStorm, oneBuild.getTimeSinceLastRepair());
        snowDao.saveSnowStorm(collegeId,intenseSnowStorm);

        generateCorrectPopup(intenseSnowStorm,false, popupManager,collegeId,hoursAlive);

        billCostOfSnowStorm(collegeId,intenseSnowStorm);
        buildingMgr.disasterStatusChange(lengthOfStorm , oneBuild.getName(), collegeId, "Snowed In");
        buildingMgr.disasterStatusChange(lengthOfStorm , twoBuild.getName(), collegeId, "Snowed In");
    }

    /**
     * Checks if a snow storm upgrade has been purchased or not
     *      future use: param upgradeName the name of the upgrade to check
     * @param upgradeName the name of the upgrade to check
     * @param collegeId the college ID
     * @return the Bool indicating if upgrade was bought or not
     * */
    private Boolean hasSpecificUpgradePurchased(String upgradeName, String collegeId){
        return inventoryManager.isPurchased(upgradeName, collegeId);
    }

    /**
     * Generates a random cost of the Snow Storm depending on its intensity.
     *
     * @param intensity the level of severity of the Snow Storm
     * @param hasUpgrade whether or not an upgrade has been purchased
     * @return  a random cost of the Snow
     * */
    public int generateCostOfSnow(int intensity, Boolean hasUpgrade){
        int randCost=0;
        if (intensity == 1 && !hasUpgrade) {
            randCost = (int) (Math.random() * 2000) + 700;
        }else if(intensity == 1 && hasUpgrade){
            randCost = (int) (Math.random() * 1500) + 700;
        }else if(intensity == 2 && !hasUpgrade) {
            randCost = (int) (Math.random() * 3500) + 1000;
        }else if(intensity ==2 && hasUpgrade){
            randCost = (int) (Math.random() * 2000) + 1000;
        }else if(intensity == 3 && !hasUpgrade){
            randCost = (int)(Math.random() * 5000) + 2500;
        }else if(intensity == 3 && hasUpgrade){
            randCost = (int)(Math.random() * 4000) + 2500;
        }
        return randCost;
    }
    /**
     * Generates a random length of the Snow Storm depending on its intensity.
     * (Math.random() * range) + min;
     * @param intensity the level of severity of the Snow Storm
     * @return  the random length of the Snow
     * */
    public int generateLengthOfSnow(int intensity){
        int randLength= 1;
        if (intensity == 1){
            randLength = (int)(Math.random() * 48) + 24;
        }else if(intensity == 2){
            randLength = (int)(Math.random() * 96) + 48;    //up to 4 days
        }else if(intensity == 3){
            randLength = (int)(Math.random() * 120) + 96;   //up to 5 days
        }
        return randLength;
    }


    /**
     * Produces a random number of sick people depending on
     * the intensity of the Snow Storm that is currently happening.
     * Ranges:
     * intensity 1: 7-30
     * intensity 2: 50-100
     * intensity 3: 100-150
     *
     * @param intensity     an int determining the intensity of the snow Storm
     * @return  the number of sick people
     * */
    public int getRandomNumOfSickStudents(String collegeId, int intensity){
        List<Student> allStudents = StudentDao.getStudents(collegeId);
        List<Faculty> allFaculty = FacultyDao.getFaculty(collegeId);
        int sick;
        if(intensity == 3){
            sick = (int)(Math.random() * 90) + 70;
        }else if (intensity == 2){
            sick = (int)(Math.random() * 60) + 30;
        }else{
            sick = (int)(Math.random() * 20) + 7;
        }
        return sick;
    }

    /**
     * Randomly selects a building for it to be plowed in.
     * The building is NOT under construction. Note: Building type does NOT matter.
     *
     * @param buildings     a List of BuildingModel that currently exist in the college
     * @return  the building (as a BuildingModel) that will be covered in snow.
     * */
    public BuildingModel getRandCompletedBuilding(List<BuildingModel> buildings){
        List<BuildingModel> buildingsCompleted = new ArrayList<>();
        for(BuildingModel edif : buildings){
            if(edif.getHoursToComplete() <= 0){
                buildingsCompleted.add(edif);
            }
        }

        Random rand = new Random();
        int index = rand.nextInt(buildingsCompleted.size());
        BuildingModel randBuilding = buildingsCompleted.get(index);

        return randBuilding;
    }
    /**
     * Charges the college for plowing costs depending on the intensity of the storm..
     *         - used in: SnowManager (handleTimeChange)
     * @param collegeId
     * @param snowStorm
     */
    private void billCostOfSnowStorm(String collegeId, SnowModel snowStorm){
        if(snowStorm.getSnowIntensity() == 1){
            Accountant.payBill(collegeId,"Plowing cost for " + snowStorm.getOneBuildingSnowed().getName(), snowStorm.getCostOfPlowing());
        }else if(snowStorm.getSnowIntensity() == 2){
            //do something
        }else if(snowStorm.getSnowIntensity() == 3){
            List<BuildingModel> buildings = snowStorm.getBuildingsAffectedList();
            for (BuildingModel b : buildings) {
                Accountant.payBill(collegeId,"Plowing cost for " + b.getName(), snowStorm.getCostOfPlowing());
            }
        }
    }

    /**
     * Generates the correct PopupEventManager depending on type of storm and if an upgrade has been purchased.
     * If storm has finished, correctly update the Building's "Current Disaster"
     *
     * @param snowStorm the snow storm currently taking place
     * @param isOver whether the snow storm has finished or not
     * @param popupManager the popupEventManager
     * @param collegeId the college running ID
     * @param hoursAlive the hours alive
     *
     * */
    private void generateCorrectPopup(SnowModel snowStorm, Boolean isOver, PopupEventManager popupManager, String collegeId, int hoursAlive ){
        Boolean hasLowUpgrade = hasSpecificUpgradePurchased("Snow Pushers", collegeId);
        Boolean hasMidUpgrade = hasSpecificUpgradePurchased("Pipes", collegeId);
        Boolean hasHighUpgrade = hasSpecificUpgradePurchased("Snowplows", collegeId);
        int intensity = snowStorm.getSnowIntensity();
        if(intensity == 1){
            BuildingModel oneBuildingSnowed = snowStorm.getOneBuildingSnowed();
            NewsManager.createNews(collegeId, hoursAlive, "Low-Intensity Snow Storm: "+oneBuildingSnowed.getName()+" currently snowed in.", NewsType.COLLEGE_NEWS, NewsLevel.BAD_NEWS);
            if(isOver){     //if snow storm is over:
                NewsManager.createNews(collegeId, hoursAlive, "All snow removed from " + oneBuildingSnowed.getName() +"! ", NewsType.RES_LIFE_NEWS, NewsLevel.GOOD_NEWS);
                buildingManager.disasterStatusChange(snowStorm.getHoursLeftInSnowStorm(),snowStorm.getOneBuildingSnowed().getName(), collegeId, "None");
                NewsManager.createNews(collegeId, hoursAlive, "Low Intensity Snow Storm OVER.", NewsType.COLLEGE_NEWS, NewsLevel.GOOD_NEWS);
                popupManager.newPopupEvent(collegeId,"Low Intensity Snow Storm OVER!", "The snow storm is finally over. Maintenance has successfully removed all snow from " +snowStorm.getOneBuildingSnowed().getName()+". Time to enjoy the great weather!",
                        "Ok","okSnowStormEnded",
                        "resources/images/lowSunny.png","Sun");
            }else if(!hasLowUpgrade && !isOver){    //if not over, and NO upgrade
                popupManager.newPopupEvent(collegeId,"Weather Alert: Low Intensity Snow Storm",
                        "Oh no! "+oneBuildingSnowed.getName() +
                                " has been snowed in! Visit the store to buy snowplows.",
                        "Ok","NoCallback",
                        "resources/images/lowSnowStorm.png","Low Intensity Snow Storm");
            }else if(hasLowUpgrade && !isOver){     //if not over and YES upgrade
                popupManager.newPopupEvent(collegeId,"Weather Alert: Low Intensity Snow Storm",
                        "Oh no! "+ oneBuildingSnowed.getName() +
                                " has been snowed in! Maintenance will be able to reduce snow removal costs thanks to previously buying Snow Pushers! What a great call!",
                        "Ok","okSnowStormEnded",
                        "resources/images/lowSnowStorm.png","Low Intensity Snow Storm");
            }
        }else if(intensity == 2){
            BuildingModel oneBuildingSnowed = snowStorm.getOneBuildingSnowed();
            NewsManager.createNews(collegeId, hoursAlive, "Mid-Intensity Blizzard: "+oneBuildingSnowed.getName()+" currently snowed in with frozen pipes.", NewsType.COLLEGE_NEWS, NewsLevel.BAD_NEWS);
            if(isOver){
                NewsManager.createNews(collegeId, hoursAlive, "All snow removed from " + oneBuildingSnowed.getName() +"! ", NewsType.RES_LIFE_NEWS, NewsLevel.GOOD_NEWS);
                buildingManager.disasterStatusChange(snowStorm.getHoursLeftInSnowStorm(),snowStorm.getOneBuildingSnowed().getName(), collegeId, "None");
                //logger.info("EVARUBIO . handleTimeChange() -> MID-STORM has been DELETED.");
                NewsManager.createNews(collegeId, hoursAlive, "Mid-Intensity Blizzard OVER.", NewsType.COLLEGE_NEWS, NewsLevel.GOOD_NEWS);
                popupManager.newPopupEvent(collegeId,"Mid-Intensity Blizzard OVER!",
                        "The snow blizzard is finally over. Maintenance has successfully removed all snow from " +snowStorm.getOneBuildingSnowed().getName()+" and its pipes are fully functioning now. Temperatures are finally rising! Time to enjoy the great weather!",
                        "Ok","okSnowStormEnded",
                        "resources/images/midTempRising.png","Temperatures rising");
            }else if(!hasMidUpgrade && !isOver){
                popupManager.newPopupEvent(collegeId,"Weather Alert: Mid-Intensity Blizzard",
                        "Oh no! "+oneBuildingSnowed.getName() +
                                " has been snowed in! The low temperatures and the amount of fallen snow have caused the pipes to completely freeze." +
                        " Visit the store to buy more freeze-proof pipes.",
                        "Ok","NoCallback",
                        "resources/images/midBlizzardThunder.png","Mid Intensity Blizzard Storm");
            }else if(hasMidUpgrade && !isOver){
                popupManager.newPopupEvent(collegeId,"Weather Alert: Mid-Intensity Blizzard",
                        "Oh no! The low temperatures and the amount of fallen snow have caused "+ oneBuildingSnowed.getName() +
                                " to be snowed in! Thanks to previously investing in newer pipes the plumbing system is intact! Great call! ",
                        "Ok","okSnowStormEnded",
                        "resources/images/midBlizzardThunder.png","Mid Intensity Blizzard Storm");
            }
        }else if (intensity == 3){
            List<BuildingModel> buildingsSnowedIn = snowStorm.getBuildingsAffectedList();
            NewsManager.createNews(collegeId, hoursAlive, "Severe High-Intensity Snow Storm. " + buildingsSnowedIn.get(0).getName() +" and "+ buildingsSnowedIn.get(1).getName()+" affected.", NewsType.COLLEGE_NEWS, NewsLevel.BAD_NEWS);
            if(isOver){
                NewsManager.createNews(collegeId, hoursAlive, "Severe Snow Storm OVER.", NewsType.COLLEGE_NEWS, NewsLevel.GOOD_NEWS);
                for (BuildingModel b : buildingsSnowedIn) {
                    buildingManager.disasterStatusChange(snowStorm.getHoursLeftInSnowStorm(), b.getName(), collegeId, "None");
                    NewsManager.createNews(collegeId, hoursAlive, "Successfully removed snow from " + b.getName() +"! ", NewsType.RES_LIFE_NEWS, NewsLevel.GOOD_NEWS);

                }
                popupManager.newPopupEvent(collegeId, "Severe Snow Storm OVER!",
                        "The snow emergency is finally over. Maintenance has successfully removed all snow from affected areas! Time to enjoy this great weather while it lasts!",
                        "Ok", "okSnowStormEnded",
                        "resources/images/highCoolSun.png", "Cool Sun");
            }else if(!hasHighUpgrade && !isOver){
                popupManager.newPopupEvent(collegeId,"URGENT - WINTER WEATHER MESSAGE",
                        "High Intensity Winter Storm Warning in effect starting today. Expecting 4 to 7 inches of snow. "+ buildingsSnowedIn.get(0).getName() +" and " +buildingsSnowedIn.get(1).getName()+
                                " have been completely snowed in! " +
                                "Visit the store to buy more snowplows.",
                        "Ok","NoCallback",
                        "resources/images/highHeavySnow.png","Heavy Snow Sign");
            }else if(hasHighUpgrade && !isOver){
                popupManager.newPopupEvent(collegeId,"URGENT - WINTER WEATHER MESSAGE",
                        "High Intensity Winter Storm Warning in effect starting today. Expecting 4 to 7 inches of snow. "+ buildingsSnowedIn.get(0).getName() +" and " +buildingsSnowedIn.get(1).getName() +
                                " have been completely snowed in! Thanks to previously investing in more Snowplows Maintenance will be able to reduce snow removal costs! Great call! ",
                        "Ok","okSnowStormEnded",
                        "resources/images/highHeavySnow.png","Heavy Snow Sign");
            }
        }
    }

    /**
     * Determines whether there is a Snow Storm currently happening or not.
     * Regardless of the intensity of the storm.
     *
     * @param collegeId*/
    public boolean isEventActive(String collegeId) {
        return SnowDao.getSnowStorm(collegeId) != null;
    }

    public static void establishCollege(String collegeId) {
        InventoryManager.createItem("Snowplows",false, "snowplow.png", 4200, 2, "Buying Snowplows reduces both future maintenance costs and the probability of a 'Severe High-Intensity Snow Storm' from happening again.", collegeId);
        InventoryManager.createItem("Snow Pushers", false, "snowPusher.png", 2200, 0, "Purchasing Snow Pushers prevents 'Low-Intensity Snow Storms' from happening so often, as well as reducing future costs of snow removal.", collegeId);
    }

}
