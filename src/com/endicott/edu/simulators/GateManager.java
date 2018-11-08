package com.endicott.edu.simulators;

import com.endicott.edu.datalayer.GateDao;
import com.endicott.edu.datalayer.StudentDao;
import com.endicott.edu.models.GateModel;

import java.util.List;

/**
 * Responsible for handling the games Gate mechanic.
 */
public class GateManager {

    /**
     * @param collegeId     unique college string
     * @param key           unique gate string
     * @param description   description for gate
     * @param gate          number of students till goal is met
     */
    public static void createGate(String collegeId, String key, String description, String iconPath, int gate) {
        GateDao gateDao = new GateDao();

        gateDao.saveNewGate(collegeId, new GateModel(key, description, iconPath, gate));
    }

    /**
     * @param collegeId     unique college string
     * @param key           unique gate string
     * @return              true / false if gate has met goal
     */
    public static boolean testGate(String collegeId, String key) {
        int studentCount = StudentDao.getStudents(collegeId).size();
        List<GateModel> gates = GateDao.getGates(collegeId);
        for(GateModel gate : gates) {
            if (gate.getKey().equals(key)) return (gate.getGoal() <= studentCount);
        }
        return false;
    }

    /**
     * @param collegeId     unique college string
     * @param key           unique gate string
     * @return              the gate object
     */
    public static GateModel getGate(String collegeId, String key) {
        List<GateModel> gates = GateDao.getGates(collegeId);
        for(GateModel gate : gates) {
            if (gate.getKey().equals(key)) return gate;
        }
        return null;
    }

    /**
     * @param collegeId     unique college string
     * @param key           unique gate string
     * @return              range from 0 to 100 depending on gate completion
     */
    public static int getGateProgress(String collegeId, String key) {
        double studentCount = StudentDao.getStudents(collegeId).size();
        int progress = 0;
        List<GateModel> gates = GateDao.getGates(collegeId);
        for(GateModel gate : gates) {
            if (key.equals(gate.getKey())){
                double goal = gate.getGoal();
                progress = Math.min(100,(int)Math.floor(100*(studentCount / goal)));
                break;
            }
        }
        return progress;
    }

    /**
     * Take care of initial gate set up when college is first created.
     * @param collegeId
     */
    public static void establishCollege(String collegeId){
//        createGate(collegeId, "Test gate 1", "test description 1", "resources/images/star.png", 50);
//        createGate(collegeId, "Test gate 2", "test description 2", "resources/images/star.png", 300);
//        createGate(collegeId, "Test gate 3", "test description 3", "resources/images/star.png", 400);
    }
}
