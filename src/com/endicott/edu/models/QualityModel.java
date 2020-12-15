package com.endicott.edu.models;

import com.endicott.edu.simulators.SimulatorUtilities;

import java.io.Serializable;

/***
 * A model of the quality of a student (or the college overall)
 *  a student has values between 0..100 for each of these attributes which determines
 *  how they impact the college's quality as a whole.
 */
public class QualityModel implements Serializable {
    private int academics;
    private int sports;
    private int social;

    private static final int[][] qualityTiers =
            {       // mean   stddev    min     max
                    {   35,     10,      25,       45}, /* tier 0: not great */
                    {   50,     15,      35,       65}, /* tier 1: ok */
                    {   65,     15,      50,       80}, /* tier 2: good */
                    {   75,     25,      50,      100}, /* tier 3: very good */
            };

    private static int generateNumberAtTier(int tier){
        tier = Math.max(tier,0);
        tier = Math.min(tier,3);

        return SimulatorUtilities.getRandomNumberWithNormalDistribution(qualityTiers[tier][0],
                qualityTiers[tier][1],
                qualityTiers[tier][2],
                qualityTiers[tier][3]);
    }

    // generates a completely random model for the given tier of student
    public static QualityModel generateRandomModel(int tier){
        QualityModel q = new QualityModel();
        q.academics = generateNumberAtTier(tier);
        q.sports = generateNumberAtTier(tier);
        q.social = generateNumberAtTier(tier);
        return q;
    }

    public int getSocialQuality() { return this.social; }
    public int getAthleticQuality() { return this.sports; }
    public int getAcademicQuality() { return this.academics; }

    private QualityModel(){
    }

    public int getAcademics() {
        return academics;
    }

    public int getSports() {
        return sports;
    }

    public int getSocial() {
        return social;
    }
}
