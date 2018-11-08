package com.endicott.edu.simulators;

import com.endicott.edu.datalayer.FacultyDao;
import com.endicott.edu.models.DepartmentModel;
import com.endicott.edu.models.FacultyModel;

import java.util.ArrayList;
import java.util.HashMap;

public class DepartmentManager {

    private static ArrayList<DepartmentModel> departmentOptions ;

    public static ArrayList<DepartmentModel> establishDepartments(int departmentCount){
        // departmentCount will determine how many departments need to be loaded back in
        departmentOptions = new ArrayList<>();
        departmentOptions.add(new DepartmentModel("Arts and Sciences"));
        departmentOptions.add(new DepartmentModel("Sports Science and Fitness"));
        departmentOptions.add(new DepartmentModel("Business"));
        departmentOptions.add(new DepartmentModel("Nursing"));
        return departmentOptions;
    }

    public static ArrayList<DepartmentModel> getDepartmentOptions(){
        if (departmentOptions == null)
            establishDepartments(0);

        return departmentOptions;
    }

    private static void addToDepartmentOptions(int departmentLevel){
        if(departmentLevel == 2)
            departmentOptions.add(new DepartmentModel("Communications"));
        else if(departmentLevel == 3)
            departmentOptions.add(new DepartmentModel("Performing Arts"));
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

        if (departmentOptions == null)
            establishDepartments(0);

        for(DepartmentModel d : departmentOptions){
            departmentRatings.put(d.getDepartmentName(), computeDepartmentRating(collegeId, d));
        }
        return departmentRatings;
    }
}
