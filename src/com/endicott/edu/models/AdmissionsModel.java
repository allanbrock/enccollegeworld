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

    public List<PotentialStudentModel> getGroupA() { return groupA; }
    public List<PotentialStudentModel> getGroupB() { return groupB; }
    public List<PotentialStudentModel> getGroupC() { return groupC; }

    public void setGroupA(List<PotentialStudentModel> studentList) { this.groupA = studentList; }
    public void setGroupB(List<PotentialStudentModel> studentList) { this.groupB = studentList; }
    public void setGroupC(List<PotentialStudentModel> studentList) { this.groupC = studentList; }

    public int getWeeksLeft() { return weeksUntilAcceptance; }
    public int getNumberOfRegenerationsLeft() { return numberOfRegenerationsLeft; }

    public AdmissionsModel(){
        selectedGroup = "GroupA";
        weeksUntilAcceptance = 0;
        numberOfRegenerationsLeft = 3;
    }

    public void setWeeksUntilAcceptance(int numWeeksUntilAcceptance){
        this.weeksUntilAcceptance = numWeeksUntilAcceptance;
    }
    public void setSelectedGroup(String group) { this.selectedGroup = group; }
    public String getSelectedGroup() { return this.selectedGroup; }

}
