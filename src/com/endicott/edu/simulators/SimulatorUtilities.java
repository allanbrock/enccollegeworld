package com.endicott.edu.simulators;

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
}
