package com.endicott.edu.simulators;

import com.endicott.edu.datalayer.*;
import com.endicott.edu.models.*;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
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
    static private float class_tier_distributions[][] = {   {0.45f, 0.35f, 0.15f, 0.05f },  // initial school
                                                            {0.35f, 0.35f, 0.20f, 0.10f}, // level 1
                                                            {0.25f, 0.40f, 0.25f, 0.10f}, // level 2
                                                            {0.15f, 0.35f, 0.35f, 0.15f}, // level 3
                                                            {0.05f, 0.30f, 0.45f, 0.20f}, // level 4
                                                            {0.00f, 0.20f, 0.50f, 0.30f}};// level 5

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
        int tempCapacity = adm.getOpenCapacity();
        adm.setOpenCapacity(AdmissionsManager.findOpenCapacity(collegeId));

        //90 old student cap
        //5 withdrew
        //95 new student cap
        //Make 5 new if old is less than new

        //This means we should add in new students to each of the groups
        if(tempCapacity < adm.getOpenCapacity()) {
            CollegeModel cm = CollegeDao.getCollege(collegeId);
            int level = Math.min(cm.getGate(),5);

            ArrayList<PotentialStudentModel> listA = generateNewCandidates(adm.getOpenCapacity()-tempCapacity, collegeId, level);
            ArrayList<PotentialStudentModel> listB = generateNewCandidates(adm.getOpenCapacity()-tempCapacity, collegeId, level);
            ArrayList<PotentialStudentModel> listC = generateNewCandidates(adm.getOpenCapacity()-tempCapacity, collegeId, level);
            adm.getGroupA().addAll(listA);
            adm.getGroupB().addAll(listB);
            adm.getGroupC().addAll(listC);
        }

        if(weekInCycle == 0){
            // this is the admissions week!!
        }
        else {
            adm.setWeeksUntilAcceptance(15 - weekInCycle);
        }
        AdmissionsDao.saveAdmissionsData(collegeId,adm);
    }

    /**
     * Create the admissions/potential students for the new college.
     *
     * @param collegeId instance of the simulation
     */
    public static void establishCollege(String collegeId, int numStudents){
        AdmissionsModel adm = new AdmissionsModel();
        CollegeModel college = CollegeDao.getCollege(collegeId);


        // BUILD THE EXISTING STUDENT BODY!
        int numStudentsAdded = 0;
        List<StudentModel> students = StudentDao.getStudents(collegeId); //Returns an empty List for us
        // create potential students and convert them
        for(int yr = 1; yr <= 4; yr++) {
            ArrayList<PotentialStudentModel> studentClass = generateNewCandidates(numStudents / 4, collegeId, 0);
            for(PotentialStudentModel potentialStudent : studentClass){
                students = convertToStudent(collegeId, potentialStudent, yr, students);
                numStudentsAdded++;
            }
        }
        college.setNumberStudentsAdmitted(numStudentsAdded);
        college.setStudentsGraduating(numStudents/4);
        StudentDao.saveAllStudents(collegeId, students);
        StudentDao.saveAllStudentsUsingCache(collegeId);
        // Existing student body done

        adm.setOpenCapacity(AdmissionsManager.findOpenCapacity(collegeId));
        adm.setGroupA(generateNewCandidates(adm.getOpenCapacity(), collegeId, 1));
        adm.setGroupB(generateNewCandidates(adm.getOpenCapacity(), collegeId, 1));
        adm.setGroupC(generateNewCandidates(adm.getOpenCapacity(), collegeId, 1));
        adm.setWeeksUntilAcceptance(15);
        AdmissionsDao.saveAdmissionsData(collegeId,adm);
    }

    /**
     * Creates students for the college by making a model filled with nothing, then calling functions to fill the fields
     *
     * @param numNewStudents The number of new students to be made
     * @param collegeLevelForTierDistributions used to determine the quality of students generated
     * @param collegeId The id of the college in use
     */
    public static ArrayList<PotentialStudentModel> generateNewCandidates(int numNewStudents, String collegeId, int collegeLevelForTierDistributions){
        Random rand = new Random();
        ArrayList<PotentialStudentModel> potentialStudents = new ArrayList<PotentialStudentModel>();
        float tierDistributions[] = new float[4];
        tierDistributions[0] = class_tier_distributions[collegeLevelForTierDistributions][0];
        tierDistributions[1] = class_tier_distributions[collegeLevelForTierDistributions][1] + tierDistributions[0];
        tierDistributions[2] = class_tier_distributions[collegeLevelForTierDistributions][2] + tierDistributions[1];
        tierDistributions[3] = 1 - tierDistributions[2];

        for (int i = 0; i < numNewStudents; i++) {
            PotentialStudentModel potentialStudent = new PotentialStudentModel();
            // assign a personality and a quality

            int tier;
            float percentage = i / (float) numNewStudents;
            if (percentage <=  tierDistributions[0]) {
                tier = 0;
            } else if (percentage <= tierDistributions[1]) {
                tier = 1;
            }
            else if (percentage <= tierDistributions[2]){
                tier = 2;
            }
            else{
                tier = 3;
            }
            //PersonalityModel pm = PersonalityModel.generateRandomModel(tier);
            PersonalityModel pm = PersonalityModel.generateRandomModel(tier);
            QualityModel qm = QualityModel.generateRandomModel(tier);

            if(rand.nextInt(10) + 1 > 5) {
                potentialStudent.setGender(GenderModel.MALE);
                potentialStudent.getAvatar().generateStudentAvatar(false);
                potentialStudent.setFirstName(NameGenDao.generateFirstName(false));
                potentialStudent.setLastName(NameGenDao.generateLastName());
            }
            else {
                potentialStudent.setFirstName(NameGenDao.generateFirstName(true));
                potentialStudent.setLastName(NameGenDao.generateLastName());
                potentialStudent.setGender(GenderModel.FEMALE);
                potentialStudent.getAvatar().generateStudentAvatar(true);
            }
            potentialStudent.setName(potentialStudent.getFirstName(), potentialStudent.getLastName());
            int id = IdNumberGenDao.getID(collegeId);

            potentialStudent.setId(id);
            potentialStudent.setHobbies(HobbyGenDao.generateHobbies());
            potentialStudent.getAvatar().generateHappyAvatar();
            potentialStudent.setNature(StudentModel.assignRandomNature());
            potentialStudent.setHappiness(80);
            potentialStudent.setPersonality(pm);
            potentialStudent.setQuality(qm);
            potentialStudents.add(potentialStudent);
        }
        return potentialStudents;
    }

    public static void acceptGroup(String collegeID, String group) {
        AdmissionsModel am = AdmissionsDao.getAdmissions(collegeID);
        CollegeModel college = CollegeDao.getCollege(collegeID);
        List<StudentModel> students = StudentDao.getStudents(collegeID);

        //Check for the right group of students to pull from
        if(group.equalsIgnoreCase("GroupA")) {
            for(int i = 0; i < am.getGroupA().size(); i++) {
                students = convertToStudent(collegeID, am.getGroupA().get(i),1, students);
                college.setNumberStudentsAdmitted(college.getNumberStudentsAdmitted() + am.getGroupA().size());
            }
        }
        else if(group.equalsIgnoreCase("GroupB")) {
            for(int i = 0; i < am.getGroupB().size(); i++) {
                students = convertToStudent(collegeID, am.getGroupB().get(i),1, students);
                college.setNumberStudentsAdmitted(college.getNumberStudentsAdmitted() + am.getGroupB().size());
            }
        }
        else {
            for(int i = 0; i < am.getGroupC().size(); i++) {
                students = convertToStudent(collegeID, am.getGroupC().get(i), 1, students);
                college.setNumberStudentsAdmitted(college.getNumberStudentsAdmitted() + am.getGroupC().size());
            }
        }
        StudentDao.saveAllStudents(collegeID, students);
        StudentDao.saveAllStudentsUsingCache(collegeID);
        college.setNumberStudentsAccepted(0);
    }

    /**
     * Clear out all 3 groups to make room for newly generated students
     *
     * @param collegeId The id of the college currently in use
     */
    public static void resetGroups(String collegeId) {
        AdmissionsModel am = AdmissionsDao.getAdmissions(collegeId);
        List<PotentialStudentModel> tempA =  am.getGroupA();
        List<PotentialStudentModel> tempB =  am.getGroupB();
        List<PotentialStudentModel> tempC =  am.getGroupC();
        tempA.clear();
        tempB.clear();
        tempC.clear();
        am.setGroupA(tempA);
        am.setGroupB(tempB);
        am.setGroupC(tempC);
        AdmissionsDao.saveAdmissionsData(collegeId, am);
    }

    /**
     * Function takes a potential student and converts them to the college as a normal student. Requires the current list
     * of students so it can be added and then saved all at once at your own leisure instead of saving each individual student
     *
     * @param collegeID ID of the college that the player is on
     * @param psm The potential student
     * @param year The year of that student (1-4)
     * @param students The arraylist of current students (when college is established, is an empty array)
     *
     * @return Returns the arraylist for the programmer to save on their own so we are not saving so many times
     */
    public static List<StudentModel> convertToStudent(String collegeID, PotentialStudentModel psm, int year, List<StudentModel> students) {
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
        sm.setGender(psm.getGenderType());
        sm.setAvatar(psm.getAvatar());

        //Assigns the newly accepted student to the specific buildings and advisor
        sm.setAcademicBuilding(bm.assignAcademicBuilding(collegeID));
        sm.setDiningHall(bm.assignDiningHall(collegeID));
        sm.setDorm(bm.assignDorm(collegeID));
        sm.setAdvisor(FacultyManager.assignAdvisorToStudent(collegeID));

        //Sets up the student's athletic fields
        sm.setAthleticAbility(rand.nextInt(10));
        //TODO: Instead of randomizing it, base their ability off of their quality
        if (sm.getAthleticAbility() > 6) { sm.setAthlete(true); }
        else { sm.setAthlete(false); }
        sm.setTeam("");

        //Sets up the student year and passes in the rest of their qualities and characteristics
        sm.setClassYear(year);
        sm.setNature(psm.getNature());
        sm.setPersonality(psm.getPersonality());
        sm.setQuality(psm.getQuality());
        sm.setRunId(collegeID);

        students.add(sm);

        return students;
    }

    /**
     * Function finds the amount of spots open at the college. This factors in only on campus
     * students, and this value will be used to generate student pools
     *
     * @param collegeId The college id currently opened
     */
    public static int findOpenCapacity(String collegeId) {
        int capacity = 0;
        int availableBeds = BuildingManager.getOpenBeds(collegeId);
        int availableDesks = BuildingManager.getOpenDesks(collegeId);
        int availablePlates = BuildingManager.getOpenPlates(collegeId);
        //Find min between all 3
        int min = Math.min(availableBeds, availableDesks);
        capacity = Math.min(min, availablePlates);

        CollegeModel college = CollegeDao.getCollege(collegeId);
        capacity += college.getStudentsGraduating();    //THIS CODE ASSUMES NO COMMUTER STUDENTS AT THE MOMENT!
        return capacity;
    }
}
