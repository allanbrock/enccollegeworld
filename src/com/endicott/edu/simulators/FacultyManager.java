package com.endicott.edu.simulators;

import com.endicott.edu.datalayer.FacultyDao;
import com.endicott.edu.datalayer.IdNumberGenDao;
import com.endicott.edu.datalayer.NameGenDao;
import com.endicott.edu.models.DepartmentModel;
import com.endicott.edu.models.FacultyModel;
import com.endicott.edu.models.StudentModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Responsible for simulating faculty at the college.
 */
public class FacultyManager {

    private static ArrayList<DepartmentModel> departmentOptions;
    /**
     * Simulate changes in faculty based on the passage of time at the college.
     *
     * @param collegeId
     * @param hoursAlive number of hours college has existed.
     */
    public static void handleTimeChange(String collegeId, int hoursAlive, PopupEventManager popupManager){
        FacultyDao fao = new FacultyDao();
        payFaculty(collegeId, hoursAlive, fao);
        List<FacultyModel> editableFaculty = FacultyDao.getFaculty(collegeId);
        for(FacultyModel member : editableFaculty){
            computeFacultyHappiness(member, true);
            computeFacultyPerformance(collegeId, member);
            if(member.getRaiseRecentlyGiven()){
                popupManager.newPopupEvent("Employee Raise", "A raise was given to " + member.getFacultyName(), "ok", "done", "resources/images/money.jpg", "Employee Raise");
                member.setRaiseRecentlyGiven(false);
                fao.saveAllFaculty(collegeId, editableFaculty);
            }
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
        Accountant.payBill(collegeId, "Faculty has been paid", total);
   }

    /**
     * Create the initial faculty at the new college.
     *
     * @param collegeId instance of the simulation
     */
    public static void establishCollege(String collegeId){
        departmentOptions = new ArrayList<>();
        departmentOptions.add(new DepartmentModel("Arts and Sciences"));
        departmentOptions.add(new DepartmentModel("Sports Science and Fitness"));
        departmentOptions.add(new DepartmentModel("Business"));
        departmentOptions.add(new DepartmentModel("Nursing"));
        for (int i=0; i<12; i++) {
            String department = generateFacultyDepartment();
           FacultyModel member = addFaculty(collegeId, 100000, generateFacultyTile(department), department);  // Default salary for now
       }
   }

    /**
     * Add new faculty to the college.
     */
    public static FacultyModel addFaculty(String collegeID, int salary, String facultyTitle, String facultyDepartment) {
        Boolean isFemale;
        FacultyModel member;
        FacultyDao fao = new FacultyDao();
        double r = Math.random();
        if(r < 0.5)
            isFemale = true;
        else
            isFemale = false;
        member = new FacultyModel("Dr. " + NameGenDao.generateName(isFemale), facultyTitle, facultyDepartment, "LSB", collegeID, salary);
        fao.saveNewFaculty(collegeID, member);
        return member;
    }

    public static String generateFacultyTile(String departmentName){
        String title;
        DepartmentModel curDept = new DepartmentModel("tempName");
        for(DepartmentModel d : departmentOptions){
            if(d.getDepartmentName().equals(departmentName)){
                curDept = d;
                break;
            }
        }
        if(curDept.getDepartmentEmployeeCount() == 0)
            title = "Dean";
        else if(curDept.getDepartmentEmployeeCount() == 1)
            title = "Assistant Dean";
        else
            title = "Faculty";
        curDept.putInEmployeeCounts(title , curDept.getDepartmentEmployeeCount() - 2 + 1);
        return title;
    }

    public static String generateFacultyDepartment(){
        Random r = new Random();
        ArrayList<String> emptyDepartments = new ArrayList<>();
        ArrayList<String> onlyDeans = new ArrayList<>();
        for(DepartmentModel d : departmentOptions){
            if(d.getDepartmentEmployeeCount() == 0)
                emptyDepartments.add(d.getDepartmentName());
            else if(d.getDepartmentEmployeeCount() == 1)
                onlyDeans.add(d.getDepartmentName());
        }
        if(emptyDepartments.size() > 0 ){
            int rand = r.nextInt((emptyDepartments.size() - 0) + 1);
            for(int i = 0; i < emptyDepartments.size(); i++){
                if(i == rand)
                    return emptyDepartments.get(i);
            }
        }
        else if(onlyDeans.size() > 0){
            int rand = r.nextInt((onlyDeans.size() - 0) + 1);
            for(int i = 0; i < onlyDeans.size(); i++){
                if(i == rand)
                    return onlyDeans.get(i);
            }
        }
        else{
            int rand = r.nextInt((departmentOptions.size() - 0) + 1);
            for(int i = 0; i < departmentOptions.size(); i++){
                if(i == rand)
                    return departmentOptions.get(i).getDepartmentName();
            }
        }
        return "";  // Statement should never be hit
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
        ArrayList<Integer> salaryOptions = new ArrayList();
        int[] salaries = new int[] {100000, 125000, 150000, 175000};
        for(int i = 0; i < salaries.length; i++){
            salaryOptions.add(salaries[i]);
        }
        return salaryOptions;
    }

    private static void addToDepartmentOptions(int departmentLevel){
        if(departmentLevel == 2)
            departmentOptions.add(new DepartmentModel("Communications"));
        else if(departmentLevel == 3)
            departmentOptions.add(new DepartmentModel("Performing Arts"));
    }

    public static String[] getTitleOptions(){
        String[] titleOptions = {"Dean", "Assistant Dean", "Professor"};
        return titleOptions;
    }

    public static String[] getDepartmentOptionStrings (){
        String[] names = new String[departmentOptions.size()];
        for(int i = 0; i < departmentOptions.size(); i++){
            names[i] = departmentOptions.get(i).getDepartmentName();
        }
        return names;
    }

    public static ArrayList<DepartmentModel> getDepartmentOptions() { return departmentOptions; }

    // Computes an algorithm to generate a daily performance for an employee member
    // The algorithm is based primarily on member happiness but also randomness
    private static void computeFacultyPerformance(String collegeID, FacultyModel member){
        Random r = new Random();
        int randGenerator = r.nextInt((4-1) + 1) + 1;
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
        if(member.getSalary() == 200000) {
            // Max amount message
            return false;
        }
        else{
            for(int i : getSalaryOptions()){
                if(nextValueRaise){
                    for(FacultyModel faculty : newFaculty){
                        if(member.getFacultyID().equals(faculty.getFacultyID())){
                            faculty.setSalary(i);
                            faculty.setRaiseRecentlyGiven(true);
                            fao.saveAllFaculty(collegeID, newFaculty);
                            return true;
                        }
                    }
                }
                else if(member.getSalary() == i)
                    nextValueRaise = true;
            }
        }
        return true; // Statement should never be hit
    }

    public static FacultyModel assignAdvisorToStudent(String collegeId, StudentModel student){
        Random r = new Random();
        int positionInFaculty = 0;
        FacultyModel newAdvisor = new FacultyModel();
        List<FacultyModel> updatedFaculty = FacultyDao.getFaculty(collegeId);
        int newAdvisorPosition = r.nextInt(FacultyDao.getFaculty(collegeId).size()) + 1;
        for(FacultyModel faculty : updatedFaculty){
            if(positionInFaculty == newAdvisorPosition) {
                newAdvisor = faculty;
                break;
            }
            positionInFaculty++;
        }
        return newAdvisor;
    }
}
