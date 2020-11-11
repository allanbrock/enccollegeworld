package com.endicott.edu.simulators;

import com.endicott.edu.datalayer.AcademicsDao;
import com.endicott.edu.models.AcademicModel;

import java.util.logging.Logger;

public class AcademicsManager {
    static private final Logger logger = Logger.getLogger("AcademicsManager");

    /**
     * Simulate the changes in faculty and academic programs
     * due to passage of time at the college.
     *
     * @param collegeId  ID of the college currently in use
     * @param hoursAlive Amount of time the college has been open
     */
    public static void handleTimeChange(String collegeId, int hoursAlive, PopupEventManager popupManager) {
        CollegeManager.logger.info("Academics running handle time change - " + CollegeManager.getDate());

    }

    /**
     * Create the admissions/potential students for the new college.
     *
     * @param collegeId instance of the simulation
     */
    public static void establishCollege(String collegeId){
        AcademicModel am = new AcademicModel();
        AcademicsDao.saveAcademicsData(collegeId,am);
    }

}
