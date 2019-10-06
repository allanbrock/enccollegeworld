package com.endicott.edu.simulators;

import com.endicott.edu.datalayer.GateDao;
import com.endicott.edu.datalayer.StudentDao;
import com.endicott.edu.models.GateModel;
import com.endicott.edu.models.ObjectivesModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Responsible for handling the games Gate mechanic.
 */
public class GateManager {

    public static final Map GATE_LEVELS = new HashMap() {
        {
        put(0,0);
        put(1, 150);
        put(2, 200);
        put(3, 300);
        put(4, 400);
        put(5, 500);
        }
    };
    private static int gateLevel;

    public GateManager() {
    }

    /**
     * @param collegeId     unique college string
     * @param key           unique gate string
     * @param description   description for gate
     * @param gateLevel     1 - 5
     */
    public static void createGate(String collegeId, String key, String description, String iconPath, int gateLevel) {
        GateDao gateDao = new GateDao();
        gateDao.saveNewGate(collegeId, new GateModel(key, description, iconPath, gateLevel));
    }

    /**
     * @param collegeId     unique college string
     * @param key           unique gate string
     * @return              true / false if gate has met goal
     */
    public static boolean testGate(String collegeId, String key) {
        int studentCount = StudentDao.getStudents(collegeId).size();
        List<GateModel> gates = GateDao.getGates(collegeId);
        for(GateModel gate : gates)
            if (gate.getKey().equals(key) && studentCount > (int)GATE_LEVELS.get(gate.getLevel()))
                return true;
        return false;
    }


    public static int getGateGoal(int gateLevel) {
        return (int)GATE_LEVELS.get(gateLevel);
    }

    public static int getGateLevel(String collegeId) {
        int studentCount = StudentDao.getStudents(collegeId).size();
        for(int i = GATE_LEVELS.size()-1; i > 0; i--)
            if(studentCount > (int)GATE_LEVELS.get(i))
                return i;
        return 0;
    }

    public static int getOverallGateProgress(String collegeId) {
        return 100 * StudentDao.getStudents(collegeId).size() / (int)GATE_LEVELS.get(getGateLevel(collegeId)+1);
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

    public static ObjectivesModel getObjectives(String collegeId) {
        int studentCount = StudentDao.getStudents(collegeId).size();;
        ObjectivesModel objectives = new ObjectivesModel();
        objectives.currentLevel = 0;
        objectives.gates = GateDao.getGates(collegeId).toArray(new GateModel[0]);
        objectives.studentsNeededForLevel = new int[GATE_LEVELS.size()];

        for(int i=0; i<GATE_LEVELS.size(); i++) {
            objectives.studentsNeededForLevel[i] = (int) GATE_LEVELS.get(i);
            if (studentCount > (int)GATE_LEVELS.get(i)) {
                objectives.currentLevel = i;
            }
        }

        objectives.nextLevel = Math.min(objectives.currentLevel+1, GATE_LEVELS.size()-1);
        objectives.studentCount = studentCount;
        objectives.studentsNeededForNextLevel = Math.max((int) GATE_LEVELS.get(objectives.nextLevel) - studentCount, 0);

        return objectives;
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
                double goal = getGateGoal(gate.getLevel());
                progress = Math.min(100,(int)Math.floor(100*(studentCount / goal)));
                break;
            }
        }
        return progress;
    }

    public static void establishCollege(String collegeId){
        gateLevel = 0;
    }

    public static void handleTimeChange(String collegeId, int hoursAlive, PopupEventManager popupManager) {
        int oldGateLevel = gateLevel;
        gateLevel = getGateLevel(collegeId);
        if(oldGateLevel != gateLevel)
            popupManager.newPopupEvent("Level Up!", "Congrats you've reached enough students for level " + gateLevel + "!", "Okay", "okGate", "resources/images/star.png", "icon");
    }
}
