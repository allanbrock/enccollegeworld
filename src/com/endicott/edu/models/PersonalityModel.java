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

    private static final int[][] personalityTiers =
            {       // mean   stddev    min     max
                    {   10,      5,       0,       40},  /* tier 0: basic --easily impressed */
                    {   30,     10,      10,       60},  /* tier 1: moderate --less easily impressed */
                    {   50,     15,      30,       80},  /* tier 2: medium -- students require achievements in this area */
                    {   75,     20,      45,       90},  /* tier 3: tough -- hard to recruit */
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
        return p;
    }

    private PersonalityModel(){
    }

}
