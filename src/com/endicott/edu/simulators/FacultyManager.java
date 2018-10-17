package com.endicott.edu.simulators;

import com.endicott.edu.datalayer.FacultyDao;
import com.endicott.edu.datalayer.IdNumberGenDao;
import com.endicott.edu.datalayer.NameGenDao;
import com.endicott.edu.models.FacultyModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
        FacultyDao fao = new FacultyDao();
        payFaculty(collegeId, hoursAlive, fao);
        List<FacultyModel> editableFaculty = FacultyDao.getFaculty(collegeId);
        for(FacultyModel member : editableFaculty){
            computeFacultyHappiness(member, true);
            computeFacultyPerformance(collegeId, member);
        }
        fao.saveAllFaculty(collegeId, editableFaculty);
    }

    /**
     * Pay the faculty based on the number of hours that have passed at the
     * college since the faculty was last paid.
     *
     * @param collegeId
     * @param hoursAlive  number of hours college has existed.
     */
   private static void payFaculty(String collegeId, int hoursAlive, FacultyDao fao){
        List<FacultyModel> facultyList = fao.getFaculty(collegeId);
        int total = 0;
        for(FacultyModel member : facultyList){
            int paycheck = member.getSalary()/365;
            total += paycheck;
        }
        Accountant.payBill(collegeId,"Faculty has been paid", total);
   }

    /**
     * Create the initial faculty at the new college.
     *
     * @param collegeId instance of the simulation
     */
   public static void establishCollege(String collegeId){
       for (int i=0; i<10; i++) {
           FacultyModel member = addFaculty(collegeId, 100000);  // Default salary for now
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

    public static void computeFacultyHappiness(FacultyModel faculty, Boolean daileyComputation){
        if(!daileyComputation) {
            int tempHappiness = computeTemporaryHappiness(faculty.getSalary());
            for (int i = 0; i < FacultyManager.getSalaryOptions().size(); i++) {
                if (faculty.getSalary() == FacultyManager.getSalaryOptions().get(i)) {
                    if (i == 0)
                        faculty.setHappiness(tempHappiness - 10);
                    else if (i == 1)
                        faculty.setHappiness(tempHappiness - 5);
                    else if (i == 2)
                        faculty.setHappiness(tempHappiness + 5);
                    else
                        faculty.setHappiness(tempHappiness + 10);
                    break;
                }
            }
        }
        else{
            Random r = new Random();
            int randGenerator = r.nextInt(4 - 1) + 1;
            Boolean direction = computeDirection(faculty);
            if(direction)
                faculty.setHappiness(faculty.getHappiness() + randGenerator);
            else
                faculty.setHappiness(faculty.getHappiness() - randGenerator);
        }
        if(faculty.getHappiness() > 100)
            faculty.setHappiness(100);
    }

    public static ArrayList<Integer> getSalaryOptions(){
        ArrayList<Integer> salaryOptions = new ArrayList<Integer>();
        int[] salaries = new int[] {100000, 125000, 150000, 175000};
        for(int i = 0; i < salaries.length; i++){
            salaryOptions.add(salaries[i]);
        }
        return salaryOptions;
    }

    // Computes an algorithm to generate a daily performance for an employee member
    // The algorithm is based primarily on member happiness but also randomness
    private static void computeFacultyPerformance(String collegeID, FacultyModel member){
        Random r = new Random();
        int randGenerator = r.nextInt(4-1) + 1;
        Boolean increase = computeDirection(member);
        int curPerformance = member.getPerformance();
        if(member.getHappiness() > 85)
            curPerformance += 4;
        else if(member.getHappiness() > 75)
            curPerformance += 3;
        else if(member.getHappiness() > 65)
            curPerformance += 2;
        else if(member.getHappiness() > 50)
            curPerformance += 1;
        else if(member.getHappiness() > 30)
            curPerformance -= 2;
        else
            curPerformance -= 4;
        if(increase)
            curPerformance += randGenerator;
        else
            curPerformance -= randGenerator;
        if(curPerformance > 100)
            curPerformance = 100;
        member.setPerformance(curPerformance);
    }

    private static Boolean computeDirection(FacultyModel member){
        double direction = Math.random();
        if(member.getHappiness() >= 75) {
            if(direction <= 0.75)
                return true;
            else
                return false;
        }
        else if(member.getHappiness() >= 50) {
            if(direction <= 0.5)
                return true;
            else
                return false;
        }
        else {
            if(direction <= 0.3)
                return true;
            else
                return false;
        }
    }

    // Used specifically for initial happiness computation
    private static int computeTemporaryHappiness(int salary){
        Random r = new Random();
        if(salary == 100000)
            return r.nextInt(100 - 50) + 50;
        else if(salary == 125000)
            return r.nextInt(100 - 65) + 50;
        else if(salary == 150000)
            return r.nextInt(100 - 75) + 50;
        else
            return r.nextInt(100 - 90) + 50;
    }

    public static void removeFaculty(String collegeID, FacultyModel member){
        FacultyDao fao = new FacultyDao();
        fao.removeSingleFaculty(collegeID, member);
    }

    public static String generateFacultyID(FacultyModel member){
        int randID = IdNumberGenDao.randIDGeneration();
        for(int i = 0; i < FacultyDao.getFaculty(member.getCollegeID()).size(); i++){
            if(String.valueOf(randID).equals(FacultyDao.getFaculty(member.getCollegeID()).get(i))){
                randID = IdNumberGenDao.randIDGeneration();
                i = -1;
            }
        }
        return String.valueOf(randID);
    }

    public static Boolean giveFacultyRaise(String collegeID, FacultyModel member){
        FacultyDao fao = new FacultyDao();
        List<FacultyModel> newFaculty = FacultyDao.getFaculty(collegeID);
        Boolean nextValueRaise = false;
        if(member.getSalary() == 200000)
            return false;
        else{
            for(int i : getSalaryOptions()){
                if(nextValueRaise){
                    for(FacultyModel faculty : newFaculty){
                        if(member.getFacultyID().equals(faculty.getFacultyID())){
                            faculty.setSalary(i);
                            fao.saveAllFaculty(collegeID, newFaculty);
                            return true;
                        }
                    }
                }
                else if(member.getSalary() == i)
                    nextValueRaise = true;
            }
        }
        return null; // Statement will should never be hit
    }
}
