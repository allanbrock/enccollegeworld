package com.endicott.edu.models;

import com.endicott.edu.datalayer.AcademicsDao;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * New home for information about faculty and academic departments
 * Salary information, etc.
 */
public class AcademicModel implements Serializable {
    private int level = 0;
    private int[] facultySalaries = {100000, 125000, 150000, 175000, 200000}; // non-static so it is in the REST data

    private ArrayList<DepartmentModel> unlockedDepts;
    private ArrayList<DepartmentModel> lockedDepts;
    private boolean newDepartmentAvailable;

    public AcademicModel(){
        level = 1;
        newDepartmentAvailable = false;
        unlockedDepts = new ArrayList<DepartmentModel>();
        unlockedDepts.add(new DepartmentModel("Arts and Sciences"));
        unlockedDepts.add(new DepartmentModel("Sports Science and Fitness"));
        unlockedDepts.add(new DepartmentModel("Business"));
        unlockedDepts.add(new DepartmentModel("Nursing"));

        lockedDepts = new ArrayList<DepartmentModel>();
        lockedDepts.add(new DepartmentModel("Communications"));
        lockedDepts.add(new DepartmentModel("Performing Arts"));
    }

    public static AcademicModel getFromDAO(String collegeId){
        return AcademicsDao.getAcademics(collegeId);
    }
    public void saveToDAO(String collegeId){
        AcademicsDao.saveAcademicsData(collegeId, this);
    }

    /**
     * unlock a department with the given name
     * @param departmentName
     * @return the DepartmentModel of the unlocked dept or null in the event of a problem
     */
    public DepartmentModel unlockDepartment(String departmentName) {
        for (DepartmentModel d : lockedDepts) {
            if (departmentName.equals(d.getDepartmentName())) {
                lockedDepts.remove(d);
                unlockedDepts.add(d);
                newDepartmentAvailable = false;
                return d;
            }
        }
        return null;
    }
    public boolean getNewDepartmentAvailable(){
        return newDepartmentAvailable;
    }
    public void setNewDepartmentAvailable(boolean newState) {
        newDepartmentAvailable = newState;
    }
    public ArrayList<DepartmentModel> getUnlockedDepts(){
        return unlockedDepts;
    }
    public ArrayList<DepartmentModel> getLockedDepts(){
        return lockedDepts;
    }



}
