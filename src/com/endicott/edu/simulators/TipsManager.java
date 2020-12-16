package com.endicott.edu.simulators;

import com.endicott.edu.models.CollegeMode;
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
    public static void changeAcademicTips(CollegeModel college, int academicQuality, int buildingQuality) {
        TipsModel tm = college.getTips();
        tm.getAcademicTips().clear();

        //Look to add any tips based upon each stat that makes up the rating
        if(college.getStudentFacultyRatioRating() < 75) {
            tm.getAcademicTips().add("Try hiring more staff to balance the student faculty ratio!");
        }
        if(college.getFacultyBodyHappiness() < 75 || FacultyManager.getAverageFacultyPerformance(college.getRunId()) < 75) {
            //Change this once faculty happiness and performance is better calculated
            tm.getAcademicTips().add("Try boosting the happiness of your faculty to improve their performance!");
        }
        if(academicQuality < 70) {
            tm.getAcademicTips().add("Try recruiting students who have a better academic rating!");
        }
        if(buildingQuality < 70) {
            tm.getAcademicTips().add("Try repairing up some of your academic buildings!");
        }

        //If the player has done very well in this department, remind them of reason why it might not be a perfect rating
        if(tm.getAcademicTips().size() == 0) {
            tm.getAcademicTips().add("Good work, continue keeping your students and faculty happy!");
        }

        college.setTips(tm);
    }

    /**
     * Function will add/delete tips on how to improve the athletic score of the college
     * @param winPercentage The percentage of games won by all the teams the college has
     * @param amountOfTeams  The amount of teams the college has compared to the max number of sports that can be created
     * @param buildingQuality The quality of the athletic buildings on campus
     * @param championships The number of championships per team (1 team with no championships is 0/1 = 0%)
     */
    public static void changeAthleticTips(CollegeModel college, int winPercentage, int amountOfTeams, int buildingQuality, int athleticQuality, int championships) {
        TipsModel tm = college.getTips();
        tm.getAthleticTips().clear();

        //Look to add any tips based upon each stat that makes up the rating
        if(winPercentage < 70 || athleticQuality < 70) {
            tm.getAthleticTips().add("Try recruiting students with a better athletic rating!");
        }
        if(amountOfTeams < 60) {
            tm.getAthleticTips().add("Try developing some more sports teams!");
        }
        if(buildingQuality < 75) {
            tm.getAthleticTips().add("Try repairing your athletic buildings!");
        }
        if(championships > 50) {
            tm.getAthleticTips().add("Try helping your team win a championship!");
        }

        //If the player has done very well in this department, remind them of reason why it might not be a perfect rating
        if(tm.getAthleticTips().size() == 0) {
            tm.getAthleticTips().add("Good work, ensure that your teams win more games and championships!");
        }
        college.setTips(tm);
    }

    /**
     * Function will add/delete tips on how to improve the infrastructure score of the college
     *
     * @param buildingQuality The quality of all buildings on campus
     * @param overcrowded A boolean to tell if any dorm is overcrowded
     * @param upgraded A boolean to tell if there are upgraded buildings
     */
    public static void changeInfrastructureTips(CollegeModel college, int buildingQuality, boolean overcrowded, boolean upgraded) {
        TipsModel tm = college.getTips();
        tm.getInfrastructureTips().clear();

        if(buildingQuality < .75) {
            tm.getInfrastructureTips().add("Try repairing some of your buildings on campus!");
        }
        if(overcrowded) {
            tm.getInfrastructureTips().add("Some of your buildings are overcrowded, build more dorms!");
        }
        if(upgraded) {
            tm.getInfrastructureTips().add("Try purchasing some upgrades for your buildings!");
        }

        //If the player has done very well in this department, remind them of reason why it might not be a perfect rating
        if(tm.getInfrastructureTips().size() == 0) {
            tm.getInfrastructureTips().add("Good work, continue to purchase upgrades and keep your building quality up!");
        }
        college.setTips(tm);
    }

    /**
     * Function will add/delete tips on how to improve the admissions score of the college
     */
    public static void changeAdmissionsTips(CollegeModel college) {
        TipsModel tm = college.getTips();
        //In the future if you decide to make these tips dynamic add line below
        tm.getAdmissionsTips().clear();
        tm.getAdmissionsTips().add("Pick the group with the best impact on your school");
        tm.getAdmissionsTips().add("Level up to improve the quality of students in your future admission pools.");
        college.setTips(tm);
    }

    /**
     * Function will add/delete tips on how to improve the admissions score of the college
     */
    public static void changeGeneralTips(CollegeModel college) {
        TipsModel tm = college.getTips();
        //In the future if you decide to make these tips dynamic add line below
        tm.getGeneralTips().clear();
        tm.getGeneralTips().add("Focus on Budgeting your Money.");
        tm.getGeneralTips().add("Checkout Potential Student's Applications in Admissions!");
        college.setTips(tm);
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
    public static void changeSafetyTips(CollegeModel college, int buildingQuality, int healthRating, boolean recentRiot, boolean wasDeath,
                                 boolean wasSick, boolean overcrowded) {
        TipsModel tm = college.getTips();
        tm.getSafetyTips().clear();

        if(buildingQuality < .8) {
            tm.getSafetyTips().add("Try repairing some of your buildings!");
        }
        if(healthRating < .6 || wasSick) {
            tm.getSafetyTips().add("Try improving your health rating by buying store items and spending money to help quarantine students!");
        }
        if(wasDeath) {
            tm.getSafetyTips().add("Prepare your campus better for disasters to minimize potential deaths!");
        }
        if(recentRiot) {
            tm.getSafetyTips().add("Raise the happiness of your students and faculty to prevent rioting or recruit less destructive students!");
        }
        if(overcrowded) {
            tm.getSafetyTips().add("Your dorms are overcrowded, try building new ones or upgrading old ones!");
        }

        //If the player has done very well in this department, remind them of reason why it might not be a perfect rating
        if(tm.getSafetyTips().size() == 0) {
            tm.getSafetyTips().add("Good work, continue keeping your buildings and health up!");
        }
        college.setTips(tm);
    }

    /**
     * Function will add/delete tips on how to improve the value score of the college
     *
     * @param averageRating The average rating of all college traits put together
     */
    public static void changeValueTips(CollegeModel college, int averageRating) {
        TipsModel tm = college.getTips();
        tm.getValueTips().clear();

        if(averageRating <= 65) {
            tm.getValueTips().add("Try improving all aspects of the school or lower the tuition rating!");
        }
        college.setTips(tm);
    }

    /**
     * Function will add/delete tips on how to improve the social score of the college
     *
     * @param socialQuality The average social quality of all the students on campus
     * @param gameNum The rating calculated with the number of games that have been played on campus
     * @param studentHappiness The average happiness of all the students on campus
     * @param facultyHappiness The average happiness of all the faculty on campus
     */
    public static void changeSocialTips(CollegeModel college, int socialQuality, int gameNum, int studentHappiness, int facultyHappiness) {
        TipsModel tm = college.getTips();
        tm.getSocialTips().clear();

        if(socialQuality < 70) {
            tm.getSocialTips().add("Try recruiting students who are more social!");
        }
        if(gameNum < 75) {
            tm.getSocialTips().add("Add new sports to your college so students can attend some games!");
        }
        if(studentHappiness < 70 || facultyHappiness < 70) {
            tm.getSocialTips().add("Increase your student's and faculty's happiness!");
        }

        //If the player has done very well in this department, remind them of reason why it might not be a perfect rating
        if(tm.getSocialTips().size() == 0) {
            tm.getSocialTips().add("Good work, continue keeping your students and faculty happy!");
        }
        college.setTips(tm);
    }
}
