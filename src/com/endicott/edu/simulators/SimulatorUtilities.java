package com.endicott.edu.simulators;

import java.util.Random;

// Created by
public class SimulatorUtilities {
    /**
     * Return a number between 0 and 100, based on a given value.
     * The result is scaled by 0 and 100 by making 0 correspond to
     * the given bottom value and 100 correspond to the given top value.
     * If the bottom is larger than the top, then this method will return
     * still scale so that the bottom corresponds to 0 and 100 to the top.
     *
     * @param bottom
     * @param top
     * @param value
     * @return
     */
    public static int getRatingZeroToOneHundred(int bottom, int top, int value) {
        float range = Math.abs(top - bottom);
        int normalize = value - Math.min(top, bottom);
        int answer =  (int) ((normalize / range)* 100 + 0.5);
        answer = Math.max(answer, 0);
        answer = Math.min(answer, 100);
        if (bottom > top) {
            answer = 100 - answer;
        }
        return answer;
    }

    /**
     * Return a random number that follows a normal distribution matter.
     * For example, if the normal distribution you want is centered on 50,
     * and the standard deviation is 10, then pass 50 for mean and 10 for sd.
     * This method will also enforce a min/max on the result.
     *
     * @param mean
     * @param standardDev
     * @param min
     * @param max
     * @return
     */
    public static int getRandomNumberWithNormalDistribution(int mean, int standardDev, int min, int max) {
        Random random = new Random();
        int normalDistNo = (int) (random.nextGaussian()*standardDev + mean + 0.5);
        normalDistNo = Math.min(max, normalDistNo);
        normalDistNo = Math.max(min, normalDistNo);
        return normalDistNo;
    }

    public static void main(String[] args) {
        Random random = new Random();
        int n;

        // Height using normal distribution
        int sd, mean;

        mean = 70; // ave height
        sd = 4;    // SD: 68% fall within 1 SD, 95% within 2
        
        n = (int) (random.nextGaussian()*sd + mean);
        System.out.println(inToFt(n));
    }

    private static String inToFt(int givenInches) {
        int ft = givenInches/12;
        int in = givenInches - 12 * ft;
        return ("" + ft + "' " + in);
    }
}
