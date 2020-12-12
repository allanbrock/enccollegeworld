package com.endicott.edu.models;

import com.endicott.edu.simulators.SimulatorUtilities;

import java.io.Serializable;
import java.util.Random;

/***
 * defines the interests/values of a Person --these are the coefficients for their happiness
 * equation and indicates how likely they are to value/prioritize different stats.
 */
public class PersonalityModel implements Serializable {
    private int academics; // student-faculty ratio or ...?
    private int sports;
    private int infrastructures; // buildings
    private int campusLife;
    private int cost; // i.e., value
    private int safety;


    private static final int[][] personalityTiers =
            {       // mean   stddev    min     max
                    {   10,      5,       5,       15},  /* tier 0: basic --easily impressed */
                    {   30,     10,      20,       40},  /* tier 1: moderate --less easily impressed */
                    {   50,     15,      35,       65},  /* tier 2: medium -- students require achievements in this area */
                    {   75,     20,      55,       95},  /* tier 3: tough -- hard to recruit */
            };

    private static int generateNumberAtTier(int tier){
        tier = Math.max(tier,0);
        tier = Math.min(tier,3);

        return SimulatorUtilities.getRandomNumberWithNormalDistribution(personalityTiers[tier][0],
                                                                        personalityTiers[tier][1],
                                                                        personalityTiers[tier][2],
                                                                        personalityTiers[tier][3]);
    }

    // generates a completely random model for the given tier of student
    public static PersonalityModel generateRandomModel(int[] tier){
        PersonalityModel p = new PersonalityModel();
        p.academics = generateNumberAtTier(tier[0]);
        p.sports = generateNumberAtTier(tier[1]);
        p.infrastructures = generateNumberAtTier(tier[2]);
        p.campusLife = generateNumberAtTier(tier[3]);
        p.cost = generateNumberAtTier(tier[4]);
        p.safety = generateNumberAtTier(tier[5]);
        return p;
    }

    public static String assignRandomTier() {
        double rand = Math.random();
        if (rand >= .66)
            return "Athletic";
        else if (rand >= .33)
            return "Studious";
        else
            return "Social";
    }

    private PersonalityModel(){
    }

    public int getAcademics() {
        return academics;
    }

    public void setAcademics(int academics) {
        this.academics = academics;
    }

    public int getSports() {
        return sports;
    }

    public void setSports(int sports) {
        this.sports = sports;
    }

    public int getInfrastructures() {
        return infrastructures;
    }

    public void setInfrastructures(int infrastructures) {
        this.infrastructures = infrastructures;
    }

    public int getCampusLife() {
        return campusLife;
    }

    public void setCampusLife(int campusLife) {
        this.campusLife = campusLife;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public int getSafety() {
        return safety;
    }

    public void setSafety(int safety) {
        this.safety = safety;
    }


}
