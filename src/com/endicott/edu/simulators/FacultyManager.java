package com.endicott.edu.simulators;

import com.endicott.edu.datalayer.FacultyDao;
import com.endicott.edu.datalayer.IdNumberGenDao;
import com.endicott.edu.datalayer.NameGenDao;
import com.endicott.edu.models.FacultyModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Responsible for simulating faculty at the college.
 */
public class FacultyManager {

    /**
     * Simulate changes in faculty based on the passage of time at the college.
     *
     * @param collegeId
     * @param hoursAlive number of hours college has existed.
     */
    public static void handleTimeChange(String collegeId, int hoursAlive){
            payFaculty(collegeId,hoursAlive);
    }

    /**
     * Pay the faculty based on the number of hours that have passed at the
     * college since the faculty was last paid.
     *
     * @param collegeId
     * @param hoursAlive  number of hours college has existed.
     */
   private static void payFaculty(String collegeId, int hoursAlive){
        FacultyDao fao = new FacultyDao();
        List<FacultyModel> facultyList = fao.getFaculty(collegeId);
        int total = 0;
        for(FacultyModel member : facultyList){
            int yearlySalary = member.getSalary();
            int paycheck = (int) ((hoursAlive/24f)*(yearlySalary/365f)) ;
            total += paycheck;
        }

        Accountant.payBill(collegeId,"Faculty has been paid",total);
   }

    /**
     * Create the initial faculty at the new college.
     *
     * @param collegeId instance of the simulation
     */
   public static void establishCollege(String collegeId){
       for (int i=0; i<10; i++) {
           addFaculty(collegeId, 100000);  // Default salary value for now
       }
   }

    /**
     * Add new faculty to the college.
     */
    public static FacultyModel addFaculty(String collegeID, int salary) {
        Boolean isFemale;
        FacultyModel member;
        FacultyDao fao = new FacultyDao();
        double r = Math.random();
        if(r < 0.5)
            isFemale = true;
        else
            isFemale = false;
        member = new FacultyModel("Dr. " + NameGenDao.generateName(isFemale), "Dean", "Science", "LSB", collegeID, salary);
        fao.saveNewFaculty(collegeID, member);
        return member;
    }
}
