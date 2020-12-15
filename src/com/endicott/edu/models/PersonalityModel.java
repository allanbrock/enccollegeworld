package com.endicott.edu.models;

import com.endicott.edu.simulators.SimulatorUtilities;

import java.io.Serializable;

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
    private int overall; // the sum of the individual personalities (used to compute offsets)
                        // NOTE: IF YOU ADD MORE FIELDS, UPDATE reComputeOverall() and generateRandomModel() below


    private static final int[][] personalityTiers =
            {       // mean   stddev    min     max
                    {   25,     5,       20,       30},  /* tier 0: basic --easily impressed */
                    {   45,     10,      35,       55},  /* tier 1: moderate --less easily impressed */
                    {   70,     10,      60,       80},  /* tier 2: medium -- students require achievements in this area */
                    {   80,     10,      70,       90},  /* tier 3: tough -- hard to recruit */
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
    public static PersonalityModel generateRandomModel(int tier){
        PersonalityModel p = new PersonalityModel();
        p.academics = generateNumberAtTier(tier);
        p.sports = generateNumberAtTier(tier);
        p.infrastructures = generateNumberAtTier(tier);
        p.campusLife = generateNumberAtTier(tier);
        p.cost = generateNumberAtTier(tier);
        p.safety = generateNumberAtTier(tier);
        p.reComputeOverall();
        return p;
    }

    private PersonalityModel(){
    }

    private void reComputeOverall(){
        overall = academics+sports+infrastructures+campusLife+cost+safety;
    }

    public int getAcademics() {
        return academics;
    }
    public int getSports() {
        return sports;
    }
    public int getInfrastructures() {
        return infrastructures;
    }
    public int getCampusLife() {
        return campusLife;
    }
    public int getCost() {
        return cost;
    }
    public int getSafety() {
        return safety;
    }
    public int getOverall() { return overall; }

    public void setAcademics(int academics) {
        this.academics = academics;
        this.reComputeOverall();
    }

    public void setSports(int sports) {
        this.sports = sports;
        this.reComputeOverall();
    }

    public void setInfrastructures(int infrastructures) {
        this.infrastructures = infrastructures;
        this.reComputeOverall();
    }

    public void setCampusLife(int campusLife) {
        this.campusLife = campusLife;
        this.reComputeOverall();
    }

    public void setCost(int cost) {
        this.cost = cost;
        this.reComputeOverall();
    }

    public void setSafety(int safety) {
        this.safety = safety;
        this.reComputeOverall();
    }


}
