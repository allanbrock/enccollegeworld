package com.endicott.edu.models;

import com.endicott.edu.simulators.SimulatorUtilities;

import java.io.Serializable;
import java.util.ArrayList;

public class AdmissionsModel implements Serializable{
    private ArrayList<PotentialStudentModel> studentPoolA = new ArrayList<>();
    private ArrayList<PotentialStudentModel> studentPoolB = new ArrayList<>();
    private ArrayList<PotentialStudentModel> studentPoolC = new ArrayList<>();
    private int weeksUntilAcceptance;


    public AdmissionsModel(){
    }

    public void setGroupA(ArrayList<PotentialStudentModel> poolA){
        this.studentPoolA = poolA;
    }

    public void setGroupB(ArrayList<PotentialStudentModel> poolB){
        this.studentPoolB = poolB;
    }

    public void setGroupC(ArrayList<PotentialStudentModel> poolC){
        this.studentPoolC = poolC;
    }

    public void setWeeksUntilAcceptance(int weeksUntilAcceptance) {
        this.weeksUntilAcceptance = weeksUntilAcceptance;
    }
}
