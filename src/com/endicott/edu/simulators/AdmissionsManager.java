package com.endicott.edu.simulators;

//import com.endicott.edu.datalayer.AdmissionsDao;
import com.endicott.edu.datalayer.AdmissionsDao;
import com.endicott.edu.datalayer.IdNumberGenDao;
import com.endicott.edu.datalayer.NameGenDao;
import com.endicott.edu.datalayer.StudentDao;
import com.endicott.edu.models.*;

import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Logger;

/***
 * Admissions simulates everything having to do with potential students considering
 * attending the virtual college.  The Admissions model generates potential students
 * and tracks their interest as we approach the admissions date
 *
 * NOTE: All simulator/Managers need to implement: handleTimeChange, establishCollege
 *  --there should be an interface...
 */
public class AdmissionsManager {
    static private final Logger logger = Logger.getLogger("Admissions");

    /**
     * Simulate the changes in potential students interested in the college
     * due to passage of time at the college.
     *
     * @param collegeId  ID of the college currently in use
     * @param hoursAlive Amount of time the college has been open
     */
    public void handleTimeChange(String collegeId, int hoursAlive, PopupEventManager popupManager) {
        CollegeManager.logger.info("Admissions running handle time change - " + CollegeManager.getDate());

    }

    /**
     * Create the admissions/potential students for the new college.
     *
     * @param collegeId instance of the simulation
     */
    public static void establishCollege(String collegeId){
        AdmissionsModel adm = new AdmissionsModel();
        adm.setGroupA(generateNewCandidates(50, collegeId));
        adm.setGroupB(generateNewCandidates(50, collegeId));
        adm.setGroupC(generateNewCandidates(50, collegeId));
        adm.setWeeksUntilAcceptance(15);
        AdmissionsDao.saveAdmissionsData(collegeId,adm);
    }

    /**
     * Creates students for the college by making a model filled with nothing, then calling functions to fill the fields
     *
     * @param numNewStudents The number of new students to be made
     * @param collegeId The id of the college in use
     */
    public static ArrayList<PotentialStudentModel> generateNewCandidates(int numNewStudents, String collegeId){
        Random rand = new Random();
        ArrayList<PotentialStudentModel> students = new ArrayList<PotentialStudentModel>();
        for (int i = 0; i < numNewStudents; i++) {
            PotentialStudentModel student = new PotentialStudentModel();
            // assign a personality and a quality
            // TODO: determine 'tier' and 'quality' from the current level of the college
            // for now, 5/8 of students will be basic,
            //          2/8 will be one tier above
            //          1/8 will be two tiers above
            int tier = 0;
            float percentage = i / (float) numNewStudents;
            if (percentage > 7 / 8.0 * numNewStudents) {
                tier = 2;
            } else if (percentage > 5 / 8.0 * numNewStudents) {
                tier = 1;
            }
            PersonalityModel pm = PersonalityModel.generateRandomModel(tier);
            QualityModel qm = QualityModel.generateRandomModel(tier);

            String name;
            GenderModel gender;

            if (rand.nextInt(10) + 1 > 5) {
                name = NameGenDao.generateName(false);
                student.setGender(GenderModel.MALE);
                student.getAvatar().generateStudentAvatar(false);
            } else {
                name = NameGenDao.generateName(true);
                student.setGender(GenderModel.FEMALE);
                student.getAvatar().generateStudentAvatar(true);
            }
            student.setFirstName(name.substring(0, name.indexOf(" ") - 1));
            student.setLastName(name.substring(name.indexOf(" ") + 1));

            student.setId(IdNumberGenDao.getID(collegeId));
            student.setNature(StudentModel.assignRandomNature());
            student.getAvatar().generateHappyAvatar();
            students.add(student);
        }
        return students;
    }

}
