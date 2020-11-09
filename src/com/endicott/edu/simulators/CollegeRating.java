package com.endicott.edu.simulators;

import com.endicott.edu.datalayer.*;
import com.endicott.edu.models.BuildingModel;
import com.endicott.edu.models.CollegeModel;
import com.endicott.edu.models.SportModel;
import com.endicott.edu.models.StudentModel;

public class CollegeRating {
    private SportsDao sportDao = new SportsDao();
    private BuildingDao buildDao = new BuildingDao();
    private FacultyDao facultyDao = new FacultyDao();

    private static int eventFactor = 5;
    private static int recentDeathCountdown = 0;
    private static Boolean recentRiot = false;
    private CollegeModel college;

    public CollegeRating(){}

    /**
     *
     * @param collegeId
     */
    public void handleTimeChange(String collegeId) {
        college = CollegeDao.getCollege(collegeId);
        updateAcademicRating(collegeId);
        updateAthleticRating(collegeId);
        updateInfrastructureRating(collegeId);
        updateSafetyRating(collegeId);
        updateSocialRating(collegeId);
        updateSchoolValue(collegeId);
    }

    /**
     * Get number 0-100
     * @param value
     * @return value between 0-100
     */
    private static int checkBounds(int value){
        value = Math.min(100, value);
        value = Math.max(0, value);
        return value;
    }

    /**
     * Update School's Academic Rating based on student-faculty ratio
     * rating, academic building quality, faculty performance, and
     * @param collegeId
     */
    private void updateAcademicRating(String collegeId) {
        int academicQuality = 0;
        int numStudents = 0;
        int numBuildings = 0;
        int buildingQuality = 0;

        // Get overall academic building quality
        for(BuildingModel building : buildDao.getBuildings(collegeId)){
            String type = building.getKindOfBuilding();
            if(type == "ACADEMIC" || type == "LIBRARY" ){
                numBuildings++;
                buildingQuality += building.getShownQuality();
            }
        }

        // Get overall academic ability of student body
        for(StudentModel student : StudentDao.getStudents(collegeId)){
            academicQuality += student.getQuality().getAcademicQuality();
            numStudents++;
        }

        // Calculate Rating
        int rating = (int) (0.2 * college.getStudentFacultyRatioRating() +
                        0.2 * college.getFacultyBodyHappiness() +
                        0.3 * FacultyManager.getAverageFacultyPerformance(collegeId) +
                        0.1 * (academicQuality / Math.max(numStudents, 1)) +
                        0.2 * (buildingQuality / Math.max(numBuildings, 1)));
        college.setAcademicRating(checkBounds(rating));
    }

    /**
     * Update School's Athletic Rating based on number of sports team,
     *  athletic facilities, team performance, and student athletic quality
     * @param collegeId
     */
    private void updateAthleticRating(String collegeId){
        int totalWins = 0;
        int totalGames = 0;
        int numTeams = 0;
        int numBuildings = 0;
        int buildingQuality = 0;
        int rating = college.getAthleticRating();
        int athleticQuality = 0;
        int numStudents = 0;

        // Get info about sports teams
        for(SportModel sport : sportDao.getSports(collegeId)){
            totalWins += sport.getGamesWon();
            totalGames += sport.getGamesWon();
            totalGames += sport.getGamesTied();
            totalGames += sport.getGamesLost();
            numTeams++;
        }

        // Get overall athletic building quality
        for(BuildingModel building : buildDao.getBuildings(collegeId)){
            String type = building.getKindOfBuilding();
            if(type == "SPORTS" || type == "BASEBALL DIAMOND" ||
                    type == "FOOTBALL STADIUM" || type == "HOCKEY RINK" ){
                numBuildings++;
                buildingQuality += building.getShownQuality();
            }
        }

        // Get overall athletic ability of student body
        for(StudentModel student : StudentDao.getStudents(collegeId)){
            athleticQuality += student.getQuality().getAthleticQuality();
            numStudents++;
        }

        // Calculate rating:
        // If teams, based on performance, number of teams, facility quality, student athletic quality
        if(numTeams != 0) {
            rating = (int) (0.2 * rating +
                        0.2 * (100*(totalWins / Math.max(totalGames, 1))) +
                        0.1 * (100*numTeams/9) +
                        0.2 * (buildingQuality / Math.max(numBuildings, 1)) +
                        0.2 * (athleticQuality / Math.max(numStudents, 1)) +
                        0.1 * (100*college.getNumChampionshipsWon()/Math.max(1, numTeams)));
        }
        // If no teams based on existing rating, facility quality, and student athletic quality
        else {
            rating = (int) (0.85 * rating +
                            0.05 * (buildingQuality / Math.max(numBuildings, 1)) +
                            0.1 * (athleticQuality / Math.max(numStudents, 1)));
        }
        college.setAthleticRating(checkBounds(rating));
    }

    /**
     * Decrease athletic rating when team is sold/removed
     * @param collegeId
     */
    public static void decreaseAthleticRating(String collegeId) {
        CollegeModel college = CollegeDao.getCollege(collegeId);
        int rating = college.getAthleticRating();
        // Decrease Athletic Rating
        rating -= 2*eventFactor;
        college.setAthleticRating(checkBounds(rating));
    }

    /**
     * Update School's Infrastructure Rating based on building
     * qualities, upgrades and capacity
     * @param collegeId
     */
    private void updateInfrastructureRating(String collegeId){
        int rating = college.getInfrastructureRating();
        int numBuildings = 0;
        int buildingQuality = 0;

        for(BuildingModel building : buildDao.getBuildings(collegeId)){
            numBuildings++;
            buildingQuality += building.getShownQuality();
            // overcrowded buildings  = worse infrastructure rating
            if(building.getNumStudents() > building.getCapacity()){
                rating -= eventFactor;
            }
            // upgraded buildings = better infrastructure rating
            if(building.getUpgrades() != null){
                rating += eventFactor;
            }
        }
        // Calculate rating
        rating =
                (int) (0.6 * rating +
                        0.4 * (buildingQuality/Math.max(1,numBuildings)));
        college.setInfrastructureRating(checkBounds(rating));
    }

    /**
     * Decrease Infrastructure Rating if building destroyed / sold
     * @param collegeId
     * @param isDisaster
     */
    public static void decreaseInfrastructureRating(String collegeId, Boolean isDisaster){
        CollegeModel college = CollegeDao.getCollege(collegeId);
        int rating = college.getInfrastructureRating();

        if(isDisaster){
            rating -= 3*eventFactor;
        }
        else {
            rating -= eventFactor;
        }
        college.setInfrastructureRating(checkBounds(rating));
    }

    /**
     * Update School's Safety rating based off deaths, building quality &
     * capacity and student illnesses
     * @param collegeId
     */
    private void updateSafetyRating(String collegeId){
        int rating = college.getSafetyRating();
        PlagueDao plagueDao = new PlagueDao();
        int buildingQuality = 0;
        int numBuildings = 0;

        // Decrease safety rating by small amount while recovering from "recent deaths"
        if(recentDeathCountdown > 0) {
            if(recentDeathCountdown > eventFactor) {
                rating -= Math.abs(eventFactor - recentDeathCountdown);
            }
            // Slowly increase rating if further from event
            else {
                rating += Math.abs(eventFactor - recentDeathCountdown);
            }
            recentDeathCountdown--;
        }

        // For each overcrowded building, decrease safety rating
        // Also get overall building quality - bad building quality = unsafe
        for(BuildingModel building : buildDao.getBuildings(collegeId)){
            numBuildings++;
            buildingQuality += building.getShownQuality();
            if(building.getNumStudents() > building.getCapacity()){
                rating -= eventFactor;
            }
        }

        // Decrease rating for each sick student
        for(StudentModel student : StudentDao.getStudents(collegeId)) {
            if(student.getNumberHoursLeftBeingSick() > 0){
                rating--;
            }
        }

        // Calculate overall rating
        if(recentRiot) {
            rating = (int) (0.8 * rating +
                            0.1 * college.getStudentHealthRating() +
                            0.1 * (buildingQuality/Math.max(1, numBuildings)));
            recentRiot = false;
        }
        else {
            rating = (int) (0.6 * rating +
                    0.2 * college.getStudentHealthRating() +
                    0.2 * (buildingQuality / Math.max(1, numBuildings)));
        }
        college.setSafetyRating(checkBounds(rating));

    }

    /**
     * Cause an immediate decrease in safety rating in event of deaths
     * @param collegeId
     * @param numDeaths number of student and faculty deaths
     */
    public static void decreaseSafetyRating(String collegeId, int numDeaths){
        CollegeModel college = CollegeDao.getCollege(collegeId);
        int rating = college.getSafetyRating();
        // decrease rating based on number of deaths
        rating -= eventFactor * (numDeaths/2);
        // add to countdown so rating doesn't immediately increase next day
        recentDeathCountdown = 2 * eventFactor;
        college.setSafetyRating(checkBounds(rating));
    }

    /**
     * Cause immediate decrease in safety rating during riots
     * @param collegeId
     * @param riotType
     */
    public static void decreaseSafetyRating(String collegeId, String riotType) {
        CollegeModel college = CollegeDao.getCollege(collegeId);
        int rating = college.getSafetyRating();

        // decrease safety rating depending on size of riot
        if (riotType == "REGULAR") {
            rating -= 4* eventFactor;
        }
        else if (riotType == "SEVERE") {
            rating -= 5* eventFactor;
        }
        // riotSeverity == "ROWDY", faculty riot, etc.{
        else {
            rating -= 3*eventFactor;

        }
        recentRiot = true;
        college.setSafetyRating(checkBounds(rating));
    }

    /**
     * Update the School's Value based on School Traits and Tuition
     * @param collegeId
     */
    private void updateSchoolValue(String collegeId) {
        int schoolValue;
        int tuitionRating = college.getYearlyTuitionRating();

        // average of all school traits
        int avgRating = (int) (0.2 * college.getAcademicRating() + 0.2 * college.getAthleticRating() +
                0.2 * college.getSocialRating() + 0.2 * college.getInfrastructureRating() + 0.2 * college.getSafetyRating());

        // bad average, decrease school value
        if(avgRating <= 50){
            schoolValue = tuitionRating - (Math.abs(51 - avgRating)/2);
        }
        // good average, increase school value
        else {
            schoolValue = tuitionRating + (Math.abs(50 - avgRating)/4);
        }
        college.setSchoolValue(checkBounds(schoolValue));
    }

    /**
     * Update School's Social Rating based on student happiness,
     * faculty happiness, sporting events, student social quality
     * @param collegeId
     */
    private void updateSocialRating(String collegeId) {
        int numStudents = 0;
        int socialQuality = 0;
        int numGames =  0;

        // get number of sports games
        for(SportModel sport : sportDao.getSports(collegeId)) {
            numGames += (sport.getNumGames());
        }
        // get student social quality
        for(StudentModel student : StudentDao.getStudents(collegeId)){
            socialQuality += student.getQuality().getSocialQuality();
            numStudents++;
        }

        // calculate rating
        int rating = (int) (0.4 * (socialQuality / Math.max(numStudents, 1)) +
                            0.1 * (numGames * eventFactor/2) +
                            0.3 * college.getStudentBodyHappiness() +
                            0.1 * college.getFacultyBodyHappiness());
        college.setSocialRating(checkBounds(rating));

    }
}
