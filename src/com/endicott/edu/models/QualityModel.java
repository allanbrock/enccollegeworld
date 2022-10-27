package com.endicott.edu.models;

import com.endicott.edu.datalayer.CollegeDao;
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

    private int schoolAcademics;
    private int schoolSports;
    private int schoolSocial;

//    private static final int[][] qualityTiers =
//            {       // mean   stddev    min     max
//                    {   35,     10,      25,       45}, /* tier 0: not great */
//                    {   50,     15,      35,       65}, /* tier 1: ok */
//                    {   65,     15,      50,       80}, /* tier 2: good */
//                    {   75,     25,      50,      100}, /* tier 3: very good */
//            };
//
//    private static int generateNumberAtTier(int tier){
//        tier = Math.max(tier,0);
//        tier = Math.min(tier,3);
//
//        return SimulatorUtilities.getRandomNumberWithNormalDistribution(qualityTiers[tier][0],
//                qualityTiers[tier][1],
//                qualityTiers[tier][2],
//                qualityTiers[tier][3]);
//    }

    // generates a completely random model for the given tier of student
    public static QualityModel generateRandomModel(String collegeId, int academicTier, int sportsTier, int socialTier){
        QualityModel q = new QualityModel(collegeId);
        q.academics = q.schoolAcademics + generateDifference(academicTier);
        q.sports = q.schoolSports + generateDifference(sportsTier);
        q.social = q.schoolSocial + generateDifference(socialTier);
        return q;
    }

    private static int generateDifference(int tier){
        int difference = 0;

        // Formula for generating random number: (int)Math.random() * (max - min + 1) + min (min is inclusive while the max is exclusive)

        if (tier == 1){ // Tier 1 implies a decrease of 0 - 8
            difference = -1 * ((int)Math.random() * (10)); // Simplified from  -->  -1 * ((int)Math.random() * (9 - 0 + 1) + 0);
        } else if (tier == 2) { // Tier 2 implies an increase of 0 - 7
            difference = (int)Math.random() * (9); // Simplified from  -->  (int)Math.random() * (8 - 0 + 1) + 0;
        } else { // Tier 3 implies an increase of 8 - 15
            difference = (int)Math.random() * (9) + 8; // Simplified from  -->  (int)Math.random() * (16 - 8 + 1) + 8;
        }

        return difference;
    }

    public int getSocialQuality() { return this.social; }
    public int getAthleticQuality() { return this.sports; }
    public int getAcademicQuality() { return this.academics; }

    private QualityModel(String collegeId){
        CollegeModel college = CollegeDao.getCollege(collegeId);

        schoolAcademics = college.getAcademicRating();
        schoolSports = college.getAthleticRating();
        schoolSocial = college.getSocialRating();
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
