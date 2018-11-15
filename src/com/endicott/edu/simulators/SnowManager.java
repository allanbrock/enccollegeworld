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
 * NOTE: THERE CAN ONLY BE ONE SNOW STORM AT A TIME.
 *
 *  startLowIntensitySnow () - Weather Alert: Low Intensity Snow Storm
 *  startMidIntensitySnow ()
 *  startHighIntensitySnow() - URGENT - WINTER WEATHER MESSAGE
 *
 * */
public class SnowManager {
    private static final float PROBABILTY_OF_LOW_STORM = 40;
    private static final float PROBABILTY_OF_MID_STORM = 70;
    private static final float PROBABILTY_OF_HIGH_STORM = 100;
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
        logger.info("EVARUBIO . SNOW .  handleTimeChange() START OF METHOD ");
        SnowModel snowStorm = SnowDao.getSnowStorm(collegeId);
        //for future use
        List<BuildingModel> buildings = BuildingDao.getBuildings(collegeId);
        List<StudentModel> students = StudentDao.getStudents(collegeId);
        List<FacultyModel> faculty = FacultyDao.getFaculty(collegeId);

        snowSeasonPopup(hoursAlive,collegeId,popupManager);
        //if there is NO snow storm occurring, possibly start one:
        if (snowStorm == null) {
            logger.info("EVARUBIO . handleTimeChange() snow storm is NULL, gonna call possiblyCreateSnowStorm()  ");
            possiblyCreateSnowStorm(collegeId, hoursAlive, popupManager);
            return;
        }
        //if there IS a snow storm happening do the following:
            // Figures out how much times has passed since I updated floods
            // currentTime -lastTime
        int elapsedTime = hoursAlive - snowStorm.getHourLastUpdated();
        int timeLeft = Math.max(0, snowStorm.getHoursLeftInSnowStorm() - elapsedTime);
        if (timeLeft <= 0) {
            logger.info("EVARUBIO - SNOW handleTimeChange() snow storm is OVER, gonna call deleteSnowStorm()  ");
            isHappening = false;
            logger.info("EVARUBIO - SNOW handleTimeChange() just set isHappening to false");
            logger.info("EVARUBIO - SNOW handleTimeChange() value of isHappening = " + isHappening);

            generateCorrectPopup(snowStorm,true,popupManager, collegeId,hoursAlive);
            SnowDao.deleteSnowStorm(collegeId);
            return;
        } else {
            logger.info("EVARUBIO . handleTimeChange() snow storm is currently happening, gonna call setHoursLeftInSnowStorm()  ");

            snowStorm.setHoursLeftInSnowStorm(timeLeft);
        }
        snowDao.saveSnowStorm(collegeId,snowStorm);

    }
    /**
     * Creates a low/mid/high intensity snow storm depending on the odds.
     * TODO call this method between specific days: between days 90 (aprx 3 months) and 160 (aprox 5 months and a half)
     *
     * @param collegeId
     * @param hoursAlive
     *
     * use play mode.
     */
    public void possiblyCreateSnowStorm(String collegeId, int hoursAlive,PopupEventManager popupManager) {
        int currentDay = hoursAlive / 24 + 1;
        logger.info("EVARUBIO . PossiblyCreateSnowStorm() currentDay: "+currentDay);
        System.out.println("EVARUBIO . PossiblyCreateSnowStorm() currentDay: "+currentDay);

        Boolean hasLowUpgrade = hasSpecificUpgradePurchased(lowUpgradeName, collegeId);
        Boolean hasMidUpgrade = hasSpecificUpgradePurchased(midUpgradeName, collegeId);
        Boolean hasHighUpgrade = hasSpecificUpgradePurchased(highUpgradeName, collegeId);

        Random rand = new Random();
        int oddsOfStorm = rand.nextInt(220);
        logger.info("EVARUBIO . possiblyCreateSnowStorm() Random oddsOfStorm: " + oddsOfStorm);

            //  ---- LOW SNOW ----      (0 - 40 : 40)
        if (oddsOfStorm <= PROBABILTY_OF_LOW_STORM && !hasLowUpgrade) {
            startLowIntensitySnow(collegeId,hoursAlive,popupManager);
            // 0 - 30 : 30
        }else if ((oddsOfStorm <= (PROBABILTY_OF_LOW_STORM - 10)) && hasLowUpgrade){
            startLowIntensitySnow(collegeId, hoursAlive, popupManager);
            logger.info("EVARUBIO . possiblyCreateSnowStorm() has LOW Upgrade, probability has been decreased by 10. ");
            //  ---- MID SNOW ----      (40 - 70 : 30)
        }else if((oddsOfStorm > PROBABILTY_OF_LOW_STORM) && (oddsOfStorm <= PROBABILTY_OF_MID_STORM) && !hasMidUpgrade) {
            startMidIntensitySnow(collegeId, hoursAlive, popupManager);

        }else if ((oddsOfStorm > (PROBABILTY_OF_LOW_STORM + 10)) && (oddsOfStorm <= PROBABILTY_OF_MID_STORM) && hasMidUpgrade){
            startMidIntensitySnow(collegeId, hoursAlive, popupManager);
            logger.info("EVARUBIO . possiblyCreateSnowStorm() has MID Upgrade, probability has been decreased by 10. ");
            //  ---- HIGH SNOW ----     (70 - 100 : 30)
        }else if((oddsOfStorm > PROBABILTY_OF_MID_STORM) && (oddsOfStorm <= PROBABILTY_OF_HIGH_STORM) && !hasHighUpgrade) {
            startHighIntensitySnow(collegeId, hoursAlive, popupManager);
        }else if ((oddsOfStorm > (PROBABILTY_OF_MID_STORM + 10)) && (oddsOfStorm <= PROBABILTY_OF_HIGH_STORM) && hasHighUpgrade){
            startHighIntensitySnow(collegeId, hoursAlive, popupManager);
            logger.info("EVARUBIO . possiblyCreateSnowStorm() has HIGH Upgrade, probability has been decreased by 10. ");

        } else if (CollegeManager.isMode(collegeId, CollegeMode.DEMO_SNOW)) {
            startHighIntensitySnow(collegeId,hoursAlive,popupManager);
        }
    }
    /**
     * Starts a Low intensity Snow Storm.
     *
     *      title: Weather Alert: Low Intensity Snow Storm
     *      item: Snow Pusher
     * */
    public void startLowIntensitySnow(String collegeId, int hoursAlive, PopupEventManager popupManager){
        BuildingManager buildingMgr = new BuildingManager();
        SnowDao snowDao = new SnowDao();
        int intensity = 1;
        isHappening = true;
        logger.info("EVARUBIO - SNOW startLowIntensitySnow() just set isHappening to true");
        logger.info("EVARUBIO - SNOW startLowIntensitySnow() value of isHappening = " + isHappening);
        List<BuildingModel> buildings = BuildingDao.getBuildings(collegeId);
        BuildingModel oneBuildingSnowed = getRandCompletedBuilding(buildings);
        int lengthOfStorm = generateLengthOfSnow(intensity);
        int lowRandCost = generateCostOfSnow(intensity,hasSpecificUpgradePurchased(lowUpgradeName,collegeId));

        SnowModel lowSnow = new SnowModel(collegeId,oneBuildingSnowed,intensity,lowRandCost, lengthOfStorm, lengthOfStorm, oneBuildingSnowed.getTimeSinceLastRepair());
        snowDao.saveSnowStorm(collegeId,lowSnow);
        logger.info("EVARUBIO .  startLowIntensitySnow() LOW-SNOW STORM CREATED name of dorm:  " + oneBuildingSnowed.getName() +" Duration: "+ lengthOfStorm );
        System.out.println("EVARUBIO .  startLowIntensitySnow() LOW-SNOW STORM CREATED name of dorm:  " + oneBuildingSnowed.getName() +" Duration: "+ lengthOfStorm );

        generateCorrectPopup(lowSnow,false, popupManager,collegeId,hoursAlive);

        billCostOfSnowStorm(collegeId,lowSnow);
        buildingMgr.disasterStatusChange(lengthOfStorm , oneBuildingSnowed.getName(), collegeId, "Snowed In");
    }
    /**
     * Starts a Mid intensity Snow Storm.
     *
     *      title: Weather Alert: Mid Intensity Blizzard Warning.
     *      item: Frozen Pipes
     *
     *      Blizzard WARNING: Sustained winds or frequent gusts of 35 miles per hour or greater,
     *      plus considerable falling or blowing snow reducing visibility to less than a quarter mile.
     * */
    private void startMidIntensitySnow(String collegeId, int hoursAlive, PopupEventManager popupManager) {
        BuildingManager buildingMgr = new BuildingManager();
        SnowDao snowDao = new SnowDao();
        int intensity = 2;
        isHappening = true;
        logger.info("EVARUBIO - SNOW startMidIntensitySnow() just set isHappening to true");
        logger.info("EVARUBIO - SNOW startMidIntensitySnow() value of isHappening = " + isHappening);
        List<BuildingModel> buildings = BuildingDao.getBuildings(collegeId);
        BuildingModel oneBuildingSnowed = getRandCompletedBuilding(buildings);
        int lengthOfStorm = generateLengthOfSnow(intensity);
        int midRandCost = generateCostOfSnow(intensity,hasSpecificUpgradePurchased(midUpgradeName,collegeId));

        SnowModel midSnow = new SnowModel(collegeId,oneBuildingSnowed,intensity,midRandCost, lengthOfStorm, lengthOfStorm, oneBuildingSnowed.getTimeSinceLastRepair());
        snowDao.saveSnowStorm(collegeId,midSnow);
        logger.info("EVARUBIO .  startMidIntensitySnow() MID-SNOW STORM CREATED name of dorm:  " + oneBuildingSnowed.getName() +" Duration: "+ lengthOfStorm );

        generateCorrectPopup(midSnow,false, popupManager,collegeId,hoursAlive);

        billCostOfSnowStorm(collegeId, midSnow);
        buildingMgr.disasterStatusChange(lengthOfStorm , oneBuildingSnowed.getName(), collegeId, "Snowed In");
    }

    /**
     * Starts a High intensity Snow Storm.      Severe Snow Storm. Winter Storm Warning.
     *
     *       title: URGENT - WINTER WEATHER MESSAGE
     *       item: Snow Plows
     *
     * */
    private void startHighIntensitySnow(String collegeId, int hoursAlive, PopupEventManager popupManager) {
        BuildingManager buildingMgr = new BuildingManager();
        SnowDao snowDao = new SnowDao();
        int intensity = 3;
        isHappening = true;
        logger.info("EVARUBIO - SNOW startHighIntensitySnow() just set isHappening to true");
        logger.info("EVARUBIO - SNOW startHighIntensitySnow() value of isHappening = " + isHappening);
        List<BuildingModel> buildings = BuildingDao.getBuildings(collegeId);
        List<BuildingModel> buildingsSnowedIn = new ArrayList<>();
        int lengthOfStorm = generateLengthOfSnow(intensity);
        int randSevereCost = generateCostOfSnow(intensity,hasSpecificUpgradePurchased(highUpgradeName,collegeId));
        //int randSickSutd = getRandomNumOfSickStudents(collegeId,intensity);

        //To create the List of Affected (snowed in) buildings:
        BuildingModel oneBuild = getRandCompletedBuilding(buildings);
        BuildingModel twoBuild = getRandCompletedBuilding(buildings);
        while (oneBuild == twoBuild){
            twoBuild = getRandCompletedBuilding(buildings);
        }
        buildingsSnowedIn.add(oneBuild);
        buildingsSnowedIn.add(twoBuild);

        SnowModel intenseSnowStorm = new SnowModel(collegeId, buildingsSnowedIn, intensity, randSevereCost, lengthOfStorm, lengthOfStorm, oneBuild.getTimeSinceLastRepair());
        snowDao.saveSnowStorm(collegeId,intenseSnowStorm);
        logger.info("EVARUBIO .  startHighIntensitySnow() HIGH-SNOW STORM CREATED name of dorms:  " + oneBuild.getName() +" "+twoBuild.getName()+ " Duration: "+ lengthOfStorm );

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
     * Depending on the type of building that was affected by the Snow storm,
     * update corresponding features.
     *     >> Class building - classes canceled so: - Student Happiness increases
     *                                           - Faculty Happiness increases
     *     >> Sports building
     *     if there is currently a snow storm it will bump the funHappiness.
     *
     * */


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
        List<StudentModel> allStudents = StudentDao.getStudents(collegeId);
        List<FacultyModel> allFaculty = FacultyDao.getFaculty(collegeId);
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
        logger.info("EVARUBIO . billCostOfSnowStorm() Start-Of-Method. Intensity: "+snowStorm.getSnowIntensity());
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
     *
     *
     * */
    private void snowSeasonPopup(int hoursAlive, String collegeId, PopupEventManager popupManager){
        int currentDay = hoursAlive / 24 + 1;

        if(currentDay>= 30){
            popupManager.newPopupEvent("Snow Season!", "The snow season is here !! ",
                    "Ok","okSnowStormEnded",
                    "resources/images/lowSunny.png","Sun");

        }else if(currentDay >= 40){
            popupManager.newPopupEvent("Snow Season OVER!", "The snow season is OOVER !! ",
                    "Ok","okSnowStormEnded",
                    "resources/images/lowSunny.png","Sun");
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
                popupManager.newPopupEvent("Low Intensity Snow Storm OVER!", "The snow storm is finally over. Maintenance has successfully removed all snow from " +snowStorm.getOneBuildingSnowed().getName()+". Time to enjoy the great weather!",
                        "Ok","okSnowStormEnded",
                        "resources/images/lowSunny.png","Sun");
            }else if(!hasLowUpgrade && !isOver){    //if not over, and NO upgrade
                popupManager.newPopupEvent("Weather Alert: Low Intensity Snow Storm",
                        "Oh no! "+oneBuildingSnowed.getName() +
                                " has been snowed in! Would you like to buy more Snow Pushers from the store to prevent this from happening so often?",
                        "Buy Snow Pushers","goToStore","Do nothing ($0)","doNothing",
                        "resources/images/lowSnowStorm.png","Low Intensity Snow Storm");
            }else if(hasLowUpgrade && !isOver){     //if not over and YES upgrade
                popupManager.newPopupEvent("Weather Alert: Low Intensity Snow Storm",
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
                logger.info("EVARUBIO . handleTimeChange() -> MID-STORM has been DELETED.");
                NewsManager.createNews(collegeId, hoursAlive, "Mid-Intensity Blizzard OVER.", NewsType.COLLEGE_NEWS, NewsLevel.GOOD_NEWS);
                popupManager.newPopupEvent("Mid-Intensity Blizzard OVER!",
                        "The snow blizzard is finally over. Maintenance has successfully removed all snow from " +snowStorm.getOneBuildingSnowed().getName()+" and its pipes are fully functioning now. Temperatures are finally rising! Time to enjoy the great weather!",
                        "Ok","okSnowStormEnded",
                        "resources/images/midTempRising.png","Temperatures rising");
            }else if(!hasMidUpgrade && !isOver){
                popupManager.newPopupEvent("Weather Alert: Mid-Intensity Blizzard",
                        "Oh no! "+oneBuildingSnowed.getName() +
                                " has been snowed in! The low temperatures and the amount of fallen snow have caused the pipes to completely freeze. Would you like to buy better and newer pipes from out store? Newer pipes reduce the probability of this happening again, reducing future costs.",
                        "Buy New Pipes","goToStore","Do nothing ($0)","doNothing",
                        "resources/images/midBlizzardThunder.png","Mid Intensity Blizzard Storm");
            }else if(hasMidUpgrade && !isOver){
                popupManager.newPopupEvent("Weather Alert: Mid-Intensity Blizzard",
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
                popupManager.newPopupEvent("Severe Snow Storm OVER!",
                        "The snow emergency is finally over. Maintenance has successfully removed all snow from affected areas! Time to enjoy this great weather while it lasts!",
                        "Ok", "okSnowStormEnded",
                        "resources/images/highCoolSun.png", "Cool Sun");
            }else if(!hasHighUpgrade && !isOver){
                popupManager.newPopupEvent("URGENT - WINTER WEATHER MESSAGE",
                        "High Intensity Winter Storm Warning in effect starting today. Expecting 4 to 7 inches of snow. "+ buildingsSnowedIn.get(0).getName() +" and " +buildingsSnowedIn.get(1).getName()+
                                " have been completely snowed in! Would you like to buy more Snowplows at our store for future use? Buying more Snowplows reduces both the probability of a high-intensity snow storm from happening again and also reduces future snow removal costs.",
                        "Buy Snowplows","goToStore","Do nothing ($0)","doNothing",
                        "resources/images/highHeavySnow.png","Heavy Snow Sign");
            }else if(hasHighUpgrade && !isOver){
                popupManager.newPopupEvent("URGENT - WINTER WEATHER MESSAGE",
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
     * */
    public boolean isEventActive() {
        return isHappening;
    }
}
