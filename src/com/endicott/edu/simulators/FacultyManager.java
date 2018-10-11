package com.endicott.edu.simulators;

import com.endicott.edu.datalayer.FacultyDao;
import com.endicott.edu.datalayer.NameGenDao;
import com.endicott.edu.models.FacultyModel;

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

        payFaculty(collegeId, hoursAlive);
        for(FacultyModel member : FacultyDao.getFaculty(collegeId)){
            computeFacultyPerformance(member);
        }
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
            computeFacultyHappiness(member, true);
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

    public static void computeFacultyHappiness(FacultyModel faculty, Boolean daileyComputation){
        if(!daileyComputation) {
            if (FacultyModel.getSalaryOptions() == null) {
                faculty.setHappiness(0);
                return;
            }

            Random r = new Random();
            int tempHappiness = r.nextInt(100 - 50) + 50;
            for (int i = 0; i < FacultyModel.getSalaryOptions().size(); i++) {
                if (faculty.getSalary() == FacultyModel.getSalaryOptions().get(i)) {
                    if (i == 0)
                        faculty.setHappiness(tempHappiness - 15);
                    else if (i == 1)
                        faculty.setHappiness(tempHappiness - 5);
                    else if (i == 2)
                        faculty.setHappiness(tempHappiness + 5);
                    else
                        faculty.setHappiness(tempHappiness + 15);
                    break;
                }
            }

        }
        else{
            Boolean increase;
            Random r = new Random();
            int randGenerator = r.nextInt(10 - 1) + 1;
            double rand = Math.random();
            if(rand <= 0.5)
                faculty.setHappiness(faculty.getHappiness() + randGenerator);
            else
                faculty.setHappiness(faculty.getHappiness() - randGenerator);
        }
    }

    public static void establishSalaryOptions(){
        FacultyModel.instantiateSalaryOptions();
        int[] salaries = new int[] {100000, 125000, 150000, 175000};
        for(int i = 0; i < salaries.length; i++){
            FacultyModel.getSalaryOptions().add(salaries[i]);
        }
    }

    // Computes an algorithm to generate a daily performance for an employee member
    // The algorithm is based primarily on member happiness but also randomness
    private static void computeFacultyPerformance(FacultyModel member){
        Boolean increase;
        Random r = new Random();
        double direction = Math.random();
        int randGenerator = r.nextInt(5-1) + 1;
        if(direction <= 0.5)
            increase = false;
        else
            increase = true;
        int curPerformance = member.getPerformance();
        if(member.getHappiness() > 85)
            curPerformance += 5;
        else if(member.getHappiness() > 75)
            curPerformance += 3;
        else if(member.getHappiness() > 65)
            curPerformance -= 3;
        else
            curPerformance -= 5;
        if(increase)
            curPerformance += randGenerator;
        else
            curPerformance -= randGenerator;
        if(curPerformance > 100)
            curPerformance = 100;
        member.setPerformance(curPerformance);
    }

    public static void removeFaculty(String collegeID, FacultyModel member){
        FacultyDao fao = new FacultyDao();
        fao.removeSingleFaculty(collegeID, member);
    }
}
