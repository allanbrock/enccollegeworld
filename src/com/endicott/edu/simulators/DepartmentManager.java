package com.endicott.edu.simulators;

import com.endicott.edu.datalayer.CollegeDao;
import com.endicott.edu.datalayer.FacultyDao;
import com.endicott.edu.models.CollegeModel;
import com.endicott.edu.models.DepartmentModel;
import com.endicott.edu.models.FacultyModel;

import java.util.ArrayList;
import java.util.HashMap;

public class DepartmentManager {
    private static ArrayList<DepartmentModel> unlockedDepts;
    private static ArrayList<DepartmentModel> lockedDepts;
    private static PopupEventManager popupManager;
    private static Boolean newDepartmentReady;

    public static ArrayList<DepartmentModel> createDeptNameChoices(int departmentCount){
        // departmentCount will determine how many departments need to be loaded back in
        newDepartmentReady = false;

        unlockedDepts = new ArrayList<>();
        unlockedDepts.add(new DepartmentModel("Arts and Sciences"));
        unlockedDepts.add(new DepartmentModel("Sports Science and Fitness"));
        unlockedDepts.add(new DepartmentModel("Business"));
        unlockedDepts.add(new DepartmentModel("Nursing"));

        lockedDepts = new ArrayList<>();
        lockedDepts.add(new DepartmentModel("Communications"));
        lockedDepts.add(new DepartmentModel("Performing Arts"));
        return unlockedDepts;
    }

    public static void handleTimeChange(String collegeId, PopupEventManager popupManager){
        DepartmentManager.popupManager = popupManager;
        HashMap<String, Integer> departmentRatings = computeEachDepartmentRating(collegeId);
        checkDepartmentRatingsForBonuses(collegeId, departmentRatings);
        if(eligibleToAddDepartment() && !newDepartmentReady){
            newDepartmentReady = true;
            //popupManager.newPopupEvent(collegeId,"Eligible to add a new Academic Department!", "Due to the College's academic success, a new department can be added! Go to the faculty page to add a department", "ok", "done", "resources/images/books.png", "Eligible for new department");
        }
    }

    private static void establishNewDepartment(String collegeId, DepartmentModel department){
        String title;
        int numNewDepartmentEmployees = 6;
        for(int i = 0; i < numNewDepartmentEmployees; i++){
            title = FacultyManager.generateFacultyTile(department.getDepartmentName());
            FacultyModel faculty = FacultyManager.addFaculty(collegeId, 100000, title, department.getDepartmentName());
            if(faculty.getTitle().equals("Dean") || faculty.getTitle().equals("Assistant Dean"))
                department.putInEmployeeCounts(faculty.getTitle(), 1);
            else
                department.putInEmployeeCounts(faculty.getTitle(), department.getOverallEmployeeCount() - 1);
        }
    }

    public static Boolean getNewDepartmentReady(){ return newDepartmentReady; }

    public static String[] getLockedDepartmentNames(){
        ArrayList<String> tempNames = new ArrayList<>();
        if (lockedDepts == null)
            createDeptNameChoices(0);
        for(DepartmentModel d : lockedDepts){
            tempNames.add(d.getDepartmentName());
        }
        String[] names = new String[tempNames.size()];
        for(int i = 0; i < tempNames.size(); i++){
            names[i] = tempNames.get(i);
        }
        return names;
    }

    public static ArrayList<DepartmentModel> getUnlockedDepts(){
        if (unlockedDepts == null)
            createDeptNameChoices(0);

        return unlockedDepts;
    }

    public static Boolean addToDepartmentOptions(String collegeId, String departmentName){
        for(DepartmentModel d : lockedDepts){
            if(departmentName.equals(d.getDepartmentName())){
                lockedDepts.remove(d);
                unlockedDepts.add(d);
                establishNewDepartment(collegeId, d);
                newDepartmentReady = false;
                return true;
            }
        }
        return false;
    }

    private static HashMap<String, Integer> computeEachDepartmentRating(String collegeId){
        HashMap<String, Integer> departmentRatings = new HashMap<>();
        for(DepartmentModel d : unlockedDepts){
            departmentRatings.put((d.getDepartmentName()), computeDepartmentRating(collegeId, d));
        }
        return departmentRatings;
    }

    private static int computeDepartmentRating(String collegeId, DepartmentModel department){
        int ratingSum = 0;
        ArrayList<FacultyModel> departmentFaculty = new ArrayList<>();
        for(FacultyModel f : FacultyDao.getFaculty(collegeId)){
            if(f.getDepartmentName().equals(department.getDepartmentName())){
                departmentFaculty.add(f);
            }
        }
        int[] facultyPerformances = new int[departmentFaculty.size()];
        for(int i = 0; i < facultyPerformances.length; i++){
            facultyPerformances[i] = departmentFaculty.get(i).getPerformance();
            if(departmentFaculty.get(i).getTitle().equals("Dean"))
                ratingSum += facultyPerformances[i] * 3;
            else if(departmentFaculty.get(i).getTitle().equals("Assistant Dean"))
                ratingSum += facultyPerformances[i] * 2;
            else
                ratingSum += facultyPerformances[i];
        }
        return ratingSum / (facultyPerformances.length + 3);
    }

    public static HashMap<String, Integer> getRatingsForDepartments(String collegeId){
        HashMap<String, Integer> departmentRatings = new HashMap<>();
        int sum = 0;
        if (unlockedDepts == null)
            createDeptNameChoices(0);

        for(DepartmentModel d : unlockedDepts){
            int rating = computeDepartmentRating(collegeId, d);
            departmentRatings.put(d.getDepartmentName(), rating);
            sum += rating;
        }
        departmentRatings.put("Overall Academic Happiness", sum / unlockedDepts.size());
        return departmentRatings;
    }

    private static void checkDepartmentRatingsForBonuses(String collegeID, HashMap<String, Integer> departmentRatings){
        CollegeModel college = CollegeDao.getCollege(collegeID);
        for(String department : departmentRatings.keySet()){
            if(!department.equals("Overall Academic Happiness")) {
                if (departmentRatings.get(department) > 92) {
                    for (DepartmentModel d : unlockedDepts) {
                        if (department.equals(d.getDepartmentName())) {
                            if (!d.getBonusGiven()) {
                                CollegeManager.recieveDepartmentPerformanceBonus(collegeID, college, d.getDepartmentName(), popupManager);
                                d.setBonusGiven(true);
                            }
                        }
                    }
                }
            }
        }
    }

    private static Boolean eligibleToAddDepartment(){
        Boolean allBonuses = true;
        for(DepartmentModel d : unlockedDepts){
            if(!d.getBonusGiven()){
                allBonuses = false;
                break;
            }
        }
        return allBonuses;
    }

    public static void removeEmployeeFromDepartment(FacultyModel member, DepartmentModel department){
        department.putInEmployeeCounts(member.getTitle(), department.getEmployeeCounts().get(member.getTitle()) - 1);
    }
}
