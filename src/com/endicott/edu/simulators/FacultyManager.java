package com.endicott.edu.simulators;

import com.endicott.edu.datalayer.FacultyDao;
import com.endicott.edu.datalayer.IdNumberGenDao;
import com.endicott.edu.datalayer.NameGenDao;
import com.endicott.edu.models.*;

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
    public static void handleTimeChange(String collegeId, int hoursAlive, PopupEventManager popupManager){
        FacultyDao fao = new FacultyDao();
        payFaculty(collegeId, hoursAlive, fao);
        inspectFacultyPerformances(collegeId);
        // Haven't tested this line yet
        //DepartmentManager.checkDepartmentRatingsForBonuses(collegeId, DepartmentManager.getRatingsForDepartments(collegeId));
        ArrayList<DepartmentModel> deanCheck = checkDepartmentsForDeans("Dean");
        ArrayList<DepartmentModel> assistantDeanCheck = checkDepartmentsForDeans("Assistant Dean");

//        if(deanCheck.size() > 0){
//            for(DepartmentModel d : deanCheck){
//                addFaculty(collegeId, 100000, "Dean", d.getDepartmentName());
//                d.setEmployeeCount("Dean", 1);
//            }
//            popupManager.newPopupEvent("New Deans", deanCheck.size() + " departments Deans have been replaced", "ok", "done", "resources/images/student.png", "Dean Replacement");
//        }
//        if(assistantDeanCheck.size() > 0){
//            for(DepartmentModel d : assistantDeanCheck){
//                addFaculty(collegeId, 100000, "Assistant Dean", d.getDepartmentName());
//                d.setEmployeeCount("Assistant Dean", 1);
//            }
//            popupManager.newPopupEvent("New Assistant Deans", assistantDeanCheck.size() + " departments Assistant Deans have been replaced", "ok", "done", "resources/images/student.png", "Assistant Dean Replacement");
//        }

        List<FacultyModel> editableFaculty = FacultyDao.getFaculty(collegeId);
        for(FacultyModel member : editableFaculty){
            computeFacultyHappiness(member, true);
            computeFacultyPerformance(collegeId, member);
            if(member.getRaiseRecentlyGiven()){
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
            int paycheck = CollegeModel.daysAdvance * member.getSalary()/365;
            total += paycheck;
        }
        Accountant.payBill(collegeId, "Faculty has been paid", total);
   }

    /**
     * Create the initial faculty at the new college.
     *
     * @param collegeId instance of the simulation
     */
    public static void establishCollege(String collegeId, CollegeModel college){
        DepartmentManager.createDeptNameChoices(college.getDepartmentCount());
        for (int i=0; i<22; i++) {
            String deptName = getQuasiRandomUnlockedDeptName();
            FacultyModel member = addFaculty(collegeId, 100000, generateFacultyTile(deptName), deptName);

            for(DepartmentModel d : DepartmentManager.getUnlockedDepts()){
                if(member.getDepartmentName().equals(d.getDepartmentName())){
                    if(d.getOverallEmployeeCount() > 2) {
                        d.putInEmployeeCounts(member.getTitle(), d.getOverallEmployeeCount() - 1);
                        break;
                    }
                    else {
                        d.putInEmployeeCounts(member.getTitle(), 1);
                        break;
                    }
                }
            }
       }
       inspectFacultyPerformances(collegeId);
       loadTips(collegeId);
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
        member = new FacultyModel("Dr. " + NameGenDao.generateName(isFemale), facultyTitle, facultyDepartment, collegeID, salary);
        fao.saveNewFaculty(collegeID, member);
        return member;
    }

    public static String generateFacultyTile(String departmentName){
        String title;
        DepartmentModel curDept = new DepartmentModel("tempName");
        for(DepartmentModel d : DepartmentManager.getUnlockedDepts()){
            if(d.getDepartmentName().equals(departmentName)){
                curDept = d;
                break;
            }
        }
        if(curDept.getEmployeeCounts().get("Dean") == 0)
            title = "Dean";
        else if(curDept.getEmployeeCounts().get("Assistant Dean") == 0)
            title = "Assistant Dean";
        else
            title = "Faculty";
        return title;
    }

    private static String getQuasiRandomUnlockedDeptName(){
        Random r = new Random();
        ArrayList<String> deptsWithoutDeans = new ArrayList<>();
        ArrayList<String> deptsWithoutAssistantDeans = new ArrayList<>();

        for(DepartmentModel d : DepartmentManager.getUnlockedDepts()){
            if(d.getEmployeeCounts().get("Dean") == 0)
                deptsWithoutDeans.add(d.getDepartmentName());
            else if(d.getEmployeeCounts().get("Assistant Dean") == 0)
                deptsWithoutAssistantDeans.add(d.getDepartmentName());
        }

        if (deptsWithoutDeans.size() > 0 ){
            int rand = r.nextInt(deptsWithoutDeans.size());
            return deptsWithoutDeans.get(rand);
        }

        if(deptsWithoutAssistantDeans.size() > 0){
            int rand = r.nextInt(deptsWithoutAssistantDeans.size());
            return deptsWithoutAssistantDeans.get(rand);
        }

        int rand = r.nextInt(DepartmentManager.getUnlockedDepts().size());
        return DepartmentManager.getUnlockedDepts().get(rand).getDepartmentName();
    }

    public static void computeFacultyHappiness(FacultyModel faculty, Boolean daileyComputation){
        if (faculty == null)  // This is defensive programming.  Very important in public methods.
            return;

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

    public static String[] getTitleOptions(){
        String[] titleOptions = {"Dean", "Assistant Dean", "Faculty"};
        return titleOptions;
    }

    public static String[] getDepartmentOptionStrings (){
        String[] names = new String[DepartmentManager.getUnlockedDepts().size()];
        for(int i = 0; i < DepartmentManager.getUnlockedDepts().size(); i++){
            names[i] = DepartmentManager.getUnlockedDepts().get(i).getDepartmentName();
        }
        return names;
    }

    public static ArrayList<DepartmentModel> getDepartmentOptions() { return DepartmentManager.getUnlockedDepts(); }

    // Computes an algorithm to generate a daily performance for an employee member
    // The algorithm is based primarily on member happiness but also randomness
    public static void computeFacultyPerformance(String collegeID, FacultyModel member){
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
        if (member == null)
            return false;

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

    public static void removeFaculty(String collegeID, FacultyModel member, Boolean coach){
        FacultyDao fao = new FacultyDao();
        if(!coach) {
            for (DepartmentModel d : DepartmentManager.getUnlockedDepts()) {
                if (member.getDepartmentName().equals(d.getDepartmentName())) {
                    DepartmentManager.removeEmployeeFromDepartment(member, d);
                    break;
                }
            }
            fao.removeSingleFaculty(collegeID, member);
        }
        else
            CoachManager.removeCoach(collegeID, (CoachModel) member);
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

    public static Boolean giveFacultyRaise(String collegeID, FacultyModel member, Boolean coach){
        FacultyDao fao = new FacultyDao();
        ArrayList<FacultyModel> newEmployees;
        if(!coach)
            newEmployees = (ArrayList<FacultyModel>) FacultyDao.getFaculty(collegeID);
        else {
            newEmployees = new ArrayList<>();
            for(CoachModel c : CoachManager.getCollegeCoaches()){
                newEmployees.add((c));
            }
        }
        Boolean nextValueRaise = false;
        if(member.getSalary() == 200000) {
            // Max amount message
            return false;
        }
        else{
            for(int i : getSalaryOptions()){
                if(nextValueRaise){
                    for(FacultyModel faculty : newEmployees){
                        if(member.getFacultyID().equals(faculty.getFacultyID())){
                            faculty.setSalary(i);
                            faculty.setRaiseRecentlyGiven(true);
                            if(!coach)
                                fao.saveAllFaculty(collegeID, newEmployees);
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

    private static void inspectFacultyPerformances(String collegeId){
        List<FacultyModel> faculty = new ArrayList<>();
        for(FacultyModel f : FacultyDao.getFaculty(collegeId)){
            if(f.getPerformance() < 45){
                f.setUnderPerforming(true);
            }
            faculty.add(f);
        }
        FacultyDao fao = new FacultyDao();
        fao.saveAllFaculty(collegeId, faculty);
    }

    public static String generateUnderperformingScenario(String badFacultyName){
        Random r = new Random();
        String[] scenarios = {badFacultyName + " showed up drunk to class!",
        badFacultyName + " has been yelling at all their students!",
        badFacultyName + " posted something bad online!",
        badFacultyName + " has been failing all of their students!"};
        int rand = r.nextInt(scenarios.length - 0);
        return scenarios[rand];
    }

    private static ArrayList<DepartmentModel> checkDepartmentsForDeans(String deanPosition){
        ArrayList<DepartmentModel> deanlessDepartments = new ArrayList<>();
        for(DepartmentModel d : DepartmentManager.getUnlockedDepts()){
            if(d.getEmployeeCounts().get(deanPosition) < 1){
                deanlessDepartments.add(d);
            }
        }
        return deanlessDepartments;
    }

    public static int getAverageFacultyPerformance(String collegeId){
        int sum = 0;
        for(FacultyModel f : FacultyDao.getFaculty(collegeId)){
            sum += f.getPerformance();
        }
        return sum/FacultyDao.getFaculty(collegeId).size();
    }

    private static void loadTips(String collegeId) {
        TutorialManager.saveNewTip(collegeId, 0,"viewFaculty", "Pay Faculty more to increase their happiness", true);
        TutorialManager.saveNewTip(collegeId, 1,"viewFaculty", "Faculty that have been reported are underperforming", false);
        TutorialManager.saveNewTip(collegeId, 2,"viewFaculty", "Each department has one Dean and one Assistant Dean. They are most important", false);
        TutorialManager.saveNewTip(collegeId, 3,"viewFaculty", "Keep overall academic rating high to unlock departments", false);
    }
}
