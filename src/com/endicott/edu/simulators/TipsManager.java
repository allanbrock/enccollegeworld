package com.endicott.edu.simulators;

import com.endicott.edu.models.CollegeModel;
import com.endicott.edu.models.TipsModel;


public class TipsManager {

    /**
     * Function will add/delete tips on how to improve the academic score of the college
     *
     * @param college The college model that the college has
     * @param academicQuality The average academic quality of a student
     * @param buildingQuality The average quality of all the academic buildings in the college
     */
    static public void changeAcademicTips(CollegeModel college, int academicQuality, int buildingQuality) {
        TipsModel.getAcademicTips().clear();

        //Look to add any tips based upon each stat that makes up the rating
        if(college.getStudentFacultyRatioRating() < 75) {
            TipsModel.getAcademicTips().add("Try hiring more staff to balance the student faculty ratio!");
        }
        if(college.getFacultyBodyHappiness() < 75 || FacultyManager.getAverageFacultyPerformance(college.getRunId()) < 75) {
            //Change this once faculty happiness and performance is better calculated
            TipsModel.getAcademicTips().add("Try boosting the happiness of your faculty to improve their performance!");
        }
        if(academicQuality < 70) {
            TipsModel.getAcademicTips().add("Try recruiting students who have a better academic rating!");
        }
        if(buildingQuality < 70) {
            TipsModel.getAcademicTips().add("Try repairing up some of your academic buildings!");
        }

        //Probably want a way to add at least 1 tip if the user is doing pretty well in all areas.

    }

    /**
     * Function will add/delete tips on how to improve the athletic score of the college
     * @param winPercentage The percentage of games won by all the teams the college has
     * @param amountOfTeams  The amount of teams the college has compared to the max number of sports that can be created
     * @param buildingQuality The quality of the athletic buildings on campus
     * @param championships The number of championships per team (1 team with no championships is 0/1 = 0%)
     */
    static public void changeAthleticTips(int winPercentage, int amountOfTeams, int buildingQuality, int athleticQuality, int championships) {
        TipsModel.getAthleticTips().clear();

        //Look to add any tips based upon each stat that makes up the rating
        if(winPercentage < 70 || athleticQuality < 70) {
            TipsModel.getAthleticTips().add("Try recruiting students with a better athletic rating!");
        }
        if(amountOfTeams < 60) {
            TipsModel.getAthleticTips().add("Try developing some more sports teams!");
        }
        if(buildingQuality < 75) {
            TipsModel.getAthleticTips().add("Try repairing your athletic buildings!");
        }
        if(championships > 50) {
            TipsModel.getAthleticTips().add("Try helping your team win a championship!");
        }

        //Probably want a way to add at least 1 tip if the user is doing pretty well in all areas.
    }

    /**
     * Function will add/delete tips on how to improve the infrastructure score of the college
     *
     * @param buildingQuality The quality of all buildings on campus
     * @param overcrowded A boolean to tell if any dorm is overcrowded
     * @param upgraded A boolean to tell if there are upgraded buildings
     */
    static public void changeInfrastructureTips(int buildingQuality, boolean overcrowded, boolean upgraded) {
        TipsModel.getInfrastructureTips().clear();

        if(buildingQuality < .75) {
            TipsModel.getInfrastructureTips().add("Try repairing some of your buildings on campus!");
        }
        if(overcrowded) {
            TipsModel.getInfrastructureTips().add("Some of your buildings are overcrowded, build more dorms!");
        }
        if(upgraded) {
            TipsModel.getInfrastructureTips().add("Try purchasing some upgrades for your buildings!");
        }

        //Probably want a way to add at least 1 tip if the user is doing pretty well in all areas.

    }

    /**
     * Function will add/delete tips on how to improve the safety score of the college
     *
     * @param buildingQuality The quality of all the buildings on campus
     * @param healthRating The average health rating of all the students on campus
     * @param recentRiot A boolean to tell if there has been a riot on campus recently
     * @param wasDeath A boolean to tell if there was a death on campus recently
     * @param wasSick A boolean to tell if there are still sick students on campus
     * @param overcrowded A boolean to tell if there are any dorms that are over capacity
     */
    static public void changeSafetyTips(int buildingQuality, int healthRating, boolean recentRiot, boolean wasDeath, boolean wasSick,
                                        boolean overcrowded) {
        TipsModel.getSafetyTips().clear();

        if(buildingQuality < .75) {
            TipsModel.getSafetyTips().add("Try repairing some of your buildings!");
        }

        if(healthRating < .7 || wasSick) {
            TipsModel.getSafetyTips().add("Try improving your health rating by buying store items and spending money to help quarantine students!");
        }
        if(wasDeath) {
            TipsModel.getSafetyTips().add("Prepare your campus better for disasters to minimize potential deaths!");
        }
        if(recentRiot) {
            TipsModel.getSafetyTips().add("Raise the happiness of your students and faculty to prevent rioting or recruit less destructive students!");
        }

        //Probably want a way to add at least 1 tip if the user is doing pretty well in all areas.
    }

    /**
     * Function will add/delete tips on how to improve the value score of the college
     *
     * @param averageRating The average rating of all college traits put together
     */
    static public void changeValueTips(int averageRating) {
        TipsModel.getValueTips().clear();

        if(averageRating <= 50) {
            TipsModel.getValueTips().add("Try improving other aspects of the school or lower the tuition rating!");
        }
    }

    /**
     * Function will add/delete tips on how to improve the social score of the college
     *
     * @param socialQuality The average social quality of all the students on campus
     * @param gameNum The rating calculated with the number of games that have been played on campus
     * @param studentHappiness The average happiness of all the students on campus
     * @param facultyHappiness The average happiness of all the faculty on campus
     */
    static public void changeSocialTips(int socialQuality, int gameNum, int studentHappiness, int facultyHappiness) {
        TipsModel.getSocialTips().clear();

        if(socialQuality < 70) {
            TipsModel.getSocialTips().add("Try recruiting students who are more social!");
        }
        if(gameNum < 75) {
            TipsModel.getSocialTips().add("Add new sports to your college so students can attend more games!");
        }
        if(studentHappiness < 70 || facultyHappiness < 70) {
            TipsModel.getSocialTips().add("Increase your student's and faculty's happiness!");
        }

        //Probably want a way to add at least 1 tip if the user is doing pretty well in all areas.
    }
}
