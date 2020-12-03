package com.endicott.edu.simulators;

import com.endicott.edu.datalayer.FacultyDao;
import com.endicott.edu.models.AcademicModel;
import com.endicott.edu.models.DepartmentModel;
import com.endicott.edu.models.FacultyModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

public class DepartmentManager {
    private static PopupEventManager popupManager;


    public static void handleTimeChange(String collegeId, PopupEventManager popupManager){
        DepartmentManager.popupManager = popupManager;

        // load from the DAO.
        AcademicModel am = AcademicModel.getFromDAO(collegeId);

        // compute dept ratings

        for(DepartmentModel department : am.getUnlockedDepts()){
            int rating = computeDepartmentRating(collegeId, department);

            // check for bonus eligibility
            //checkDepartmentRatingsForBonuses(collegeId, departmentRatings);
            if ( rating > 92) {
                if (!department.getBonusGiven()) {
                    CollegeManager.recieveDepartmentPerformanceBonus(collegeId, department.getDepartmentName(), popupManager);
                    department.setBonusGiven(true);
                }
            }
        }

        // if all departments are maxed out, get ready to add a new department.
        boolean allDeptsAwardedBonuses = true;
        for(DepartmentModel d : am.getUnlockedDepts()){
            if(!d.getBonusGiven()){
                allDeptsAwardedBonuses = false;
                break;
            }
        }

        if(allDeptsAwardedBonuses && !am.getNewDepartmentAvailable()){
            am.setNewDepartmentAvailable(true);
            popupManager.newPopupEvent(collegeId,"Eligible to add a new Academic Department!", "Due to the College's academic success, a new department can be added! Go to the faculty page to add a department", "ok", "done", "resources/images/books.png", "Eligible for new department");
        }

        am.saveToDAO(collegeId);
    }

    private static void establishNewDepartment(String collegeId, DepartmentModel department){
        String title;
        int numNewDepartmentEmployees = 6;
        for(int i = 0; i < numNewDepartmentEmployees; i++){
            title = FacultyManager.generateFacultyTitle(department);
            FacultyModel faculty = FacultyManager.addFaculty(collegeId, 100000, title, department.getDepartmentName());
            if(faculty.getTitle().equals("Dean") || faculty.getTitle().equals("Assistant Dean"))
                department.putInEmployeeCounts(faculty.getTitle(), 1);
            else
                department.putInEmployeeCounts(faculty.getTitle(), department.getOverallEmployeeCount() - 1);
        }
    }

    @Deprecated
    public static Boolean getNewDepartmentReady(String collegeId){
        AcademicModel am = AcademicModel.getFromDAO(collegeId);
        return am.getNewDepartmentAvailable();
    }


    public static Boolean addToDepartmentOptions(String collegeId, String departmentName){
        // DAO load
        AcademicModel am = AcademicModel.getFromDAO(collegeId);
        DepartmentModel dept = am.unlockDepartment(departmentName);
        if(dept != null){
            establishNewDepartment(collegeId, dept);
            return true;
        }
        Logger.getLogger("DepartmentManager").warning("Critical error: attempt to unlock unknown, or previously unlocked department '" + departmentName + '"');
        return false;
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
        department.setDepartmentRating(ratingSum);
        return ratingSum / (facultyPerformances.length + 3);
    }

    @Deprecated
    public static HashMap<String, Integer> getRatingsForDepartments(String collegeId){
        HashMap<String, Integer> departmentRatings = new HashMap<>();
        departmentRatings.put("Overall Academic Happiness", 0);
        return departmentRatings;
    }


    public static void removeEmployeeFromDepartment(FacultyModel member, DepartmentModel department){
        department.putInEmployeeCounts(member.getTitle(), department.getEmployeeCounts().get(member.getTitle()) - 1);
    }
}
