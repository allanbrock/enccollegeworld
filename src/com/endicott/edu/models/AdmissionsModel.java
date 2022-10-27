package com.endicott.edu.models;

import java.io.Serializable;
import java.util.List;

/**
 * A model of the PotentialStudents currently being recruited by the Admissions department
 * Right now organized into three bins.  Players will choose which bin they want to accept
 * as their incoming class
 */
public class AdmissionsModel implements Serializable {

    private List<PotentialStudent> groupA;
    private List<PotentialStudent> groupB;
    private List<PotentialStudent> groupC;
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

    public List<PotentialStudent> getGroupA() { return groupA; }
    public List<PotentialStudent> getGroupB() { return groupB; }
    public List<PotentialStudent> getGroupC() { return groupC; }

    public void setGroupA(List<PotentialStudent> studentList) { this.groupA = studentList; }
    public void setGroupB(List<PotentialStudent> studentList) { this.groupB = studentList; }
    public void setGroupC(List<PotentialStudent> studentList) { this.groupC = studentList; }

    public int getWeeksLeft() { return weeksUntilAcceptance; }
    public int getNumberOfRegenerationsLeft() { return numberOfRegenerationsLeft; }
    public String getSelectedGroup() { return this.selectedGroup; }
    public int getOpenCapacity() { return this.openCapacity; }

    public void setNumberOfRegenerationsLeft(int num) { this.numberOfRegenerationsLeft = num; }
    public void setWeeksUntilAcceptance(int numWeeksUntilAcceptance){ this.weeksUntilAcceptance = numWeeksUntilAcceptance; }
    public void setSelectedGroup(String group) { this.selectedGroup = group; }
    public void setOpenCapacity(int num) { this.openCapacity = num; }

    public void getAcademicImpact(List<PotentialStudent> studentGroup){
        academicRatingImpact = 0;
        for (PotentialStudent student : studentGroup){
            academicRatingImpact += student.getQuality().getAcademicQuality();
        }
        academicRatingImpact = (academicRatingImpact/studentGroup.size());
    }

    public void getAthleticImpact(List<PotentialStudent> studentGroup){
        athleticRatingImpact = 0;
        for (PotentialStudent student : studentGroup){
            athleticRatingImpact += student.getQuality().getAthleticQuality();
        }
        athleticRatingImpact = (athleticRatingImpact/studentGroup.size());
    }

    public void getSocialImpact(List<PotentialStudent> studentGroup){
        socialRatingImpact = 0;
        for (PotentialStudent student : studentGroup){
            socialRatingImpact += student.getQuality().getSocialQuality();
        }
        socialRatingImpact = (socialRatingImpact/studentGroup.size());
    }
}
