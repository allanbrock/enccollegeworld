package com.endicott.edu.simulators;

import com.endicott.edu.datalayer.AdmissionsDao;
import com.endicott.edu.datalayer.IdNumberGenDao;
import com.endicott.edu.datalayer.NameGenDao;
import com.endicott.edu.datalayer.StudentDao;
import com.endicott.edu.models.*;
import com.endicott.edu.datalayer.HobbyGenDao;
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
    public static void handleTimeChange(String collegeId, int hoursAlive, PopupEventManager popupManager) {
        CollegeManager.logger.info("Admissions running handle time change - " + CollegeManager.getDate());
        AdmissionsModel adm = AdmissionsDao.getAdmissions(collegeId);
        int dayInCycle = (hoursAlive / 24);
        int weekInCycle = (dayInCycle / 7) % 15;
        if(weekInCycle == 0){
            // this is the admissions week!!
        }
        else {
            adm.setWeeksUntilAcceptance(15 - weekInCycle);
        }
    }

    /**
     * Create the admissions/potential students for the new college.
     *
     * @param collegeId instance of the simulation
     */
    public static void establishCollege(String collegeId){
        AdmissionsModel adm = new AdmissionsModel();
        adm.setGroupA(generateNewCandidates(25, collegeId));
        adm.setGroupB(generateNewCandidates(25, collegeId));
        adm.setGroupC(generateNewCandidates(25, collegeId));
        adm.setWeeksUntilAcceptance(15);
        AdmissionsDao.saveAdmissionsData(collegeId,adm);
    }

    /**
     * Creates students for the college by making a model filled with nothing, then calling functions to fill the fields
     *
     * @param numNewStudents The number of new students to be made
     * @param collegeId The id of the college in use
     */
    private static ArrayList<PotentialStudentModel> generateNewCandidates(int numNewStudents, String collegeId){
        Random rand = new Random();
        ArrayList<PotentialStudentModel> potentialStudents = new ArrayList<PotentialStudentModel>();
        for (int i = 0; i < numNewStudents; i++) {
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

            String firstName = "";
            String lastName = "";
            if(rand.nextInt(10) + 1 > 5) {
                firstName = NameGenDao.generateFirstName(false);
                lastName = NameGenDao.generateLastName();
            }
            else {
                firstName = NameGenDao.generateFirstName(true);
                lastName = NameGenDao.generateLastName();
            }
            int id = IdNumberGenDao.getID(collegeId);
            PotentialStudentModel potentialStudent = new PotentialStudentModel(firstName,lastName,GenderModel.FEMALE,id,80,pm,qm);
            if(rand.nextInt(10) + 1 > 5) {
                potentialStudent.setGender(GenderModel.MALE);
                potentialStudent.getAvatar().generateStudentAvatar(false);
            }
            else {
                potentialStudent.setGender(GenderModel.FEMALE);
                potentialStudent.getAvatar().generateStudentAvatar(true);
            }
            potentialStudent.setHobbies(HobbyGenDao.generateHobbies());
            potentialStudent.getAvatar().generateHappyAvatar();
            potentialStudent.setNature(StudentModel.assignRandomNature());
            potentialStudents.add(potentialStudent);
        }
        return potentialStudents;
    }

    public static void acceptGroup(String collegeID, String group) {
    //TODO: This code assumes that there is enough room in the college, (freshman that graduate + capacity should be the max allowed)
        AdmissionsModel am = AdmissionsDao.getAdmissions(collegeID);
        CollegeModel college = new CollegeModel();

        //Check for the right group of students to pull from
        if(group.equalsIgnoreCase("GroupA")) {
            for(int i = 0; i < am.getGroupA().size(); i++) {
                convertToStudent(collegeID, am.getGroupA().get(i));
                college.setNumberStudentsAdmitted(college.getNumberStudentsAdmitted() + am.getGroupA().size());
            }
        }
        else if(group.equalsIgnoreCase("GroupB")) {
            for(int i = 0; i < am.getGroupB().size(); i++) {
                convertToStudent(collegeID, am.getGroupB().get(i));
                college.setNumberStudentsAdmitted(college.getNumberStudentsAdmitted() + am.getGroupB().size());
            }
        }
        else {
            for(int i = 0; i < am.getGroupC().size(); i++) {
                convertToStudent(collegeID, am.getGroupC().get(i));
                college.setNumberStudentsAdmitted(college.getNumberStudentsAdmitted() + am.getGroupC().size());
            }
        }
        college.setNumberStudentsAccepted(0);   //Reset the
    }

    public static void convertToStudent(String collegeID, PotentialStudentModel psm) {
        //Create the new model and setup all the extra managers and classes necessary to generate a student
        StudentModel sm = new StudentModel();
        BuildingManager bm = new BuildingManager();
        StudentManager stuMgr = new StudentManager();
        Random rand = new Random();

        //Sets the PersonModel fields for the newly accepted student
        sm.setName(psm.getName());
        sm.setFirstName(psm.getFirstName());
        sm.setLastName(psm.getLastName());
        sm.setId(psm.getId());
        if(psm.getGender().equalsIgnoreCase("Male")) {sm.setGender(GenderModel.MALE);}
        else {sm.setGender(GenderModel.FEMALE);}
        sm.setAvatar(psm.getAvatar());

        //Assigns the newly accepted student to the specific buildings and advisor
        sm.setAcademicBuilding(bm.assignAcademicBuilding(collegeID));
        sm.setDiningHall(bm.assignDiningHall(collegeID));
        sm.setDorm(bm.assignDorm(collegeID));
        sm.setAdvisor(FacultyManager.assignAdvisorToStudent(collegeID));

        //Sets up the student's athletic fields
        sm.setAthleticAbility(rand.nextInt(10));
        if (sm.getAthleticAbility() > 6) { sm.setAthlete(true); }
        else { sm.setAthlete(false); }
        sm.setTeam("");

        //Sets up the student year and passes in the rest of their qualities and characteristics
        sm.setClassYear(1);
        sm.setNature(psm.getNature());
        sm.setPersonality(psm.getPersonality());
        sm.setQuality(psm.getQuality());
        sm.setRunId(collegeID);

        StudentDao dao = new StudentDao();
        dao.saveNewStudent(collegeID, sm);
    }
}
