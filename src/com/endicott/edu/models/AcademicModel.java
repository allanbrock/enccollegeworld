package com.endicott.edu.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * New home for information about faculty and academic departments
 * Salary information, etc.
 */
public class AcademicModel implements Serializable {
    private static String[] SCHOOLS = {"Arts and Sciences", "Business", "Nursing", "Sports Science"};
    private int level = 0;
    private List<String> schools;
    private int[] facultySalaries = {60000, 70000, 80000, 90000, 100000}; // non-static so it is in the REST data

    public AcademicModel(){
        schools = new ArrayList<String>();
        schools.add(SCHOOLS[0]);
        level = 1;
    }

    public void advanceLevel(){
        if(level < SCHOOLS.length){
            schools.add(SCHOOLS[level]);
            level++;
        }
        else{
            // max level reached
        }
    }


}
