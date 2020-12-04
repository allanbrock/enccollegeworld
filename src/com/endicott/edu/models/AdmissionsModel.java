package com.endicott.edu.models;

import java.io.Serializable;
import java.util.List;

/**
 * A model of the PotentialStudents currently being recruited by the Admissions department
 * Right now organized into three bins.  Players will choose which bin they want to accept
 * as their incoming class
 */
public class AdmissionsModel implements Serializable {

    private List<PotentialStudentModel> groupA;
    private List<PotentialStudentModel> groupB;
    private List<PotentialStudentModel> groupC;
    private int weeksUntilAcceptance;
    private int numberOfRegenerationsLeft;
    private String selectedGroup;
    private int openCapacity;
    private int academicRatingImpact;
    private int athleticRatingImpact;
    private int socialRatingImpact;

    public AdmissionsModel(){
        selectedGroup = "GroupA";           //The currently selected group of students to accept at the end of the year
        weeksUntilAcceptance = 0;           //Weeks until admissions day (30)
        numberOfRegenerationsLeft = 3;      //Number of times the user can reroll their student pools
        openCapacity = 0;                   //Number of students that can be accepted this year (Graduating students + current capacity)
    }

    public List<PotentialStudentModel> getGroupA() { return groupA; }
    public List<PotentialStudentModel> getGroupB() { return groupB; }
    public List<PotentialStudentModel> getGroupC() { return groupC; }

    public void setGroupA(List<PotentialStudentModel> studentList) { this.groupA = studentList; }
    public void setGroupB(List<PotentialStudentModel> studentList) { this.groupB = studentList; }
    public void setGroupC(List<PotentialStudentModel> studentList) { this.groupC = studentList; }

    public int getWeeksLeft() { return weeksUntilAcceptance; }
    public int getNumberOfRegenerationsLeft() { return numberOfRegenerationsLeft; }
    public String getSelectedGroup() { return this.selectedGroup; }
    public int getOpenCapacity() { return this.openCapacity; }

    public void setNumberOfRegenerationsLeft(int num) { this.numberOfRegenerationsLeft = num; }
    public void setWeeksUntilAcceptance(int numWeeksUntilAcceptance){ this.weeksUntilAcceptance = numWeeksUntilAcceptance; }
    public void setSelectedGroup(String group) { this.selectedGroup = group; }
    public void setOpenCapacity(int num) { this.openCapacity = num; }

    public void getAcademicImpact(List<PotentialStudentModel> studentGroup){
        academicRatingImpact = 0;
        for (PotentialStudentModel student : studentGroup){
            academicRatingImpact += student.getQuality().getAcademicQuality();
        }
        academicRatingImpact = (academicRatingImpact/studentGroup.size());
    }

    public void getAthleticImpact(List<PotentialStudentModel> studentGroup){
        athleticRatingImpact = 0;
        for (PotentialStudentModel student : studentGroup){
            athleticRatingImpact += student.getQuality().getAthleticQuality();
        }
        athleticRatingImpact = (athleticRatingImpact/studentGroup.size());
    }

    public void getSocialImpact(List<PotentialStudentModel> studentGroup){
        socialRatingImpact = 0;
        for (PotentialStudentModel student : studentGroup){
            socialRatingImpact += student.getQuality().getSocialQuality();
        }
        socialRatingImpact = (socialRatingImpact/studentGroup.size());
    }
}
