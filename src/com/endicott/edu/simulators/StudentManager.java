package com.endicott.edu.simulators;
import com.endicott.edu.datalayer.*;
import com.endicott.edu.models.*;

import java.util.*;
import java.util.logging.Logger;

/**
 * Responsible for simulating students at the college.
 */
public class StudentManager {
    private StudentDao dao = new StudentDao();
    //private CollegeDao collegeDao = new CollegeDao();
    private FacultyDao facultyDao = new FacultyDao();
    private BuildingManager buildingMgr = new BuildingManager();
    private BuildingModel buildingModel = new BuildingModel();
    private CollegeModel college = new CollegeModel();
    private static int studentIndex = 0;
    private Random rand = new Random();

    /**
     * The college has just been created. Add initial students and calculate
     * student statistics.
     *
     * @param collegeId ID of the new college
     */
    public void establishCollege(String collegeId) {
        loadTips(collegeId);

        // Don't generate students; let the AdmissionsManager do it.
        calculateStatistics(collegeId, true);
    }

    /**
     * Simulate the changes in students and student finances
     * due to passage of time at the college.
     *
     * @param collegeId ID of the college currently in use
     * @param hoursAlive Amount of time the college has been open
     */
    public void handleTimeChange(String collegeId, int hoursAlive, PopupEventManager popupManager) {
        CollegeManager.logger.info("Advance Time Students: statistics - " + CollegeManager.getDate());
        calculateStatistics(collegeId, false);

        CollegeManager.logger.info("Advance Time Students: withdraw - " + CollegeManager.getDate());
        withdrawStudents(collegeId, hoursAlive);

        CollegeManager.logger.info("Advance Time Students: tuition - " + CollegeManager.getDate());
        receiveStudentTuition(collegeId);

        CollegeManager.logger.info("Advance Time Students: updateTime - " + CollegeManager.getDate());
        updateStudentsTime(collegeId, hoursAlive);

        CollegeManager.logger.info("Advance Time Students: done " + CollegeManager.getDate());

        CollegeDao.saveCollege(CollegeDao.getCollege(collegeId));
    }

    /**
     * Receive student tuition.
     *
     * @param collegeId
     */
    private void receiveStudentTuition(String collegeId) {
        List<StudentModel> students = StudentDao.getStudents(collegeId);
        college = CollegeDao.getCollege(collegeId);
        int dailyTuitionSum = (college.getYearlyTuitionCost() / 365) * students.size();
        Accountant.receiveIncome(collegeId,"Student tuition received.", CollegeModel.daysAdvance * dailyTuitionSum);
    }


    public static void advanceStudentYears(String collegeId){
        CollegeModel college = CollegeDao.getCollege(collegeId);
        StudentDao dao = new StudentDao();
        BuildingManager bManage = new BuildingManager();

        List<StudentModel> students = dao.getStudents(collegeId);
        college.setStudentsGraduating(0);

        for(int i = students.size() - 1; i >= 0; i--){
            students.get(i).setClassYear(students.get(i).getClassYear()+1);
            if(students.get(i).getClassYear() == 4) {
                college.setStudentsGraduating(college.getStudentsGraduating()+1);
            }
            // If student is past senior year, 'graduate'
            if(students.get(i).getClassYear() > 4) {
                bManage.removeStudent(collegeId, students.get(i).getDorm(), students.get(i).getDiningHall(), students.get(i).getAcademicBuilding());
                students.remove(i);
                college.setNumberStudentsGraduated(college.getNumberStudentsGraduated() + 1);
            }
        }
        dao.saveAllStudents(collegeId, students);
        dao.saveAllStudentsJustToCache(collegeId, students);
        CollegeDao.saveCollege(CollegeDao.getCollege(collegeId));
    }

    static public void setStudentIndex(int i) {
        studentIndex = i;
    }

    static public int getStudentIndex() {
        return studentIndex;
    }

    //OLD CODE: WE NOW ONLY ACCEPT NEW STUDENTS IN ADMISSIONS ON ADMISSIONS DAY
//    /**
//     * Determines how many students to accept in one day.
//     *
//     * @param collegeId Id of the college currently in use
//     * @param hoursAlive The amount of hours the college has been open (days)
//     */
//    private void acceptStudents(String collegeId, int hoursAlive){
//        int numNewStudents;
//
//        // The number of accepted students depends on the happiness of the student body.
//        college = CollegeDao.getCollege(collegeId);
//        int happiness = college.getStudentBodyHappiness();
//
//        if (happiness <= 50) {
//            numNewStudents = 0;
//        } else {
//            int meanAdmittedStudents = happiness/10; // Example: happiness = 50, then 5 students
//            numNewStudents =  SimulatorUtilities.getRandomNumberWithNormalDistribution(meanAdmittedStudents,1, 0, 10);
//        }
//
//        college.setNumberStudentsAccepted(college.getNumberStudentsAccepted() + numNewStudents);
//        //CollegeDao.saveCollege(college);
//
//        if (numNewStudents > 0) {
//            NewsManager.createNews(collegeId, hoursAlive, Integer.toString(numNewStudents) + " students have been accepted to the college.", NewsType.COLLEGE_NEWS, NewsLevel.GOOD_NEWS);
//        }
//    }

    /**
     * Creates students for the college by making a model filled with nothing, then calling functions to fill the fields
     *
     * @param numNewStudents The number of new students to be made
     * @param collegeId The id of the college in use
     * @param students The list of students currently at the college
     * @param initial Check if this is the first time setup for the college
     */
    private void createStudents(int numNewStudents, String collegeId, List<StudentModel>students, boolean initial){
        Random rand = new Random();
        CollegeModel college = CollegeDao.getCollege(collegeId);

        for (int i = 0; i < numNewStudents; i++) {
            StudentModel student = new StudentModel();

            // Assign starting class year evenly
            if(i < numNewStudents/4) {
                student.setClassYear(1);
            }
            else if(i < numNewStudents/2){
                student.setClassYear(2);
            }
            else if(i < 3*numNewStudents/4){
                student.setClassYear(3);
            }
            else{
                student.setClassYear(4);
                college.setStudentsGraduating(college.getStudentsGraduating()+1);
            }

            // generate number 0-9 to decide on tier  for personality/quality
            int tier = rand.nextInt(10);
            // Assign tier based on generated number - weighted towards lower tiers
            if(tier < 5){
                tier = 0;
            }
            else if(tier < 8){
                tier = 1;
            }
            else {
                tier = 2;
            }

            student.setQuality(QualityModel.generateRandomModel(tier));

            if (rand.nextInt(10) + 1 > 5) {
                student.setName(NameGenDao.generateName(false));
                student.setGender(GenderModel.MALE);
                student.getAvatar().generateStudentAvatar(false);
            } else {
                student.setName(NameGenDao.generateName(true));
                student.setGender(GenderModel.FEMALE);
                student.getAvatar().generateStudentAvatar(true);
            }

            student.setId(IdNumberGenDao.getID(collegeId));

            if(i <= 50 && initial) {
                student.setAthleticAbility(rand.nextInt(5) + 5);
            }
            else if (i > 50 && initial){
                student.setAthleticAbility(rand.nextInt(5));
            }
            else{
                student.setAthleticAbility(rand.nextInt(10));
            }

            if (student.getAthleticAbility() > 6) {
                student.setAthlete(true);
            } else {
                student.setAthlete(false);
            }
            student.setTeam("");
            student.setAcademicBuilding(buildingMgr.assignAcademicBuilding(collegeId));
            student.setDiningHall(buildingMgr.assignDiningHall(collegeId));
            student.setDorm(buildingMgr.assignDorm(collegeId));
            student.setRunId(collegeId);
            student.setAdvisor(FacultyManager.assignAdvisorToStudent(collegeId));
            student.setNature(assignRandomNature());
            students.add(student);
        }
        CollegeDao.saveCollege(college);
        dao.saveAllStudentsJustToCache(collegeId, students);
    }

    /**
     * Is this a day on which figure out which students are leaving the college?
     * (Not in use as students are set to leave whenever if they are unhappy enough)
     *
     * @param collegeId The ID of the college currently in use
     *
     * @return True if the current day is divisible by 7, false if not
     */
    private boolean isWithdrawlDay(String collegeId) {
        int day = CollegeManager.getCollegeCurrentDay(collegeId);
        return (day % 7 == 0);
    }

    /**
     * Withdraw students from the college, determine by overall happiness.
     *
     * @param collegeId The ID of the college
     * @param hoursAlive The number of hours the college has been open
     */
    private void withdrawStudents(String collegeId, int hoursAlive) {
        college = CollegeDao.getCollege(collegeId);
        //College now goes by weeks so every next week click is withdraw day
        // if (!isWithdrawlDay(collegeId))
        // return;

        float scalingFactor = .01f;
        List<StudentModel> students = dao.getStudents(collegeId);
        int nStudents = students.size();

        // scroll through students list and remove student based upon a probability determined by their happiness level
        // the lower the happiness the greater the chance they have of withdrawing
        int studentsWithdrawn = 0;

        for (int i = 0; i < students.size(); i++){
            int h = students.get(i).getHappiness();
            double odds = Math.max(0, h/100.0); //Odds are just a percentage of happiness (.64 = 64%)
            if (students.get(i).getHappiness() < 60 && didItHappen(odds)) {
                System.out.println("We are actually removing somebody: " + students.get(i).getName() + " " + students.get(i).getClassYear());
                if(students.get(i).getClassYear() == 4) {
                    college.setStudentsGraduating(college.getStudentsGraduating()-1);
                }
                buildingMgr.removeStudent(collegeId, students.get(i).getDorm(), students.get(i).getDiningHall(), students.get(i).getAcademicBuilding());
                students.remove(i);
                studentsWithdrawn++;
            }
        }

        dao.saveAllStudents(collegeId, students);

        college.setNumberStudentsAdmitted(Math.max(college.getNumberStudentsAdmitted(), nStudents));
        college.setNumberStudentsWithdrew(college.getNumberStudentsWithdrew() + studentsWithdrawn);
        calculateRetentionRate(collegeId); //Calculate the retention rate since students have possibly left the college

        //CollegeDao.saveCollege(college);
        // Don't create a news story if no students leave
        System.out.println("Comparisson: " + nStudents + " vs " + students.size());
        if ((nStudents - students.size()) > 0) {
            System.out.println("We apparently removed some students somehow.");
            NewsManager.createNews(collegeId, hoursAlive, Integer.toString(nStudents - students.size()) + " students withdrew from college.", NewsType.COLLEGE_NEWS, NewsLevel.BAD_NEWS);
        }
    }

    /**
     * Advance the last updated time on the students.
     *
     * @param collegeId
     * @param hoursAlive
     */
    private void updateStudentsTime(String collegeId, int hoursAlive){
        List<StudentModel> students = dao.getStudents(collegeId);

        for(int i = 0; i < students.size(); i++){
            students.get(i).setHourLastUpdated(hoursAlive);
        }

        dao.saveAllStudents(collegeId, students);
    }

    /**
     * Recalculate all the statistics that are being maintained involving students.
     * @param collegeId
     */
    public void calculateStatistics(String collegeId, boolean initial) {
        CollegeManager.logger.info("Calculating new student statistics");
        System.out.println("Calculating new student statistics");
        List<StudentModel> students = dao.getStudents(collegeId);
        calculateStudentFacultyRatio(collegeId, students);
        calculateStudentFacultyRatioRating(collegeId);
        setHappinessForEachStudent(collegeId, initial, students);

        for(int i = 0; i < students.size(); i++){
            calculateHappinessRatings(collegeId, students);
            calculateHappinessRatings(collegeId, students);
            calculateHappinessRatings(collegeId, students);
            calculateHappinessRatings(collegeId, students);
            calculateHappinessRatings(collegeId, students);
            calculateHappinessRatings(collegeId, students);
        }



        calculaterOverallStudentHealth(collegeId, students);
        calculateAllAverageHappiness(collegeId, students);

    }

    private void calculateAllAverageHappiness(String collegeId, List<StudentModel> students) {
        CollegeManager.logger.info("StudentManager: Calculating average happinesses");
        CollegeModel college = CollegeDao.getCollege(collegeId);

        //Variables to find sums of total student happinesses
        int healthHappinessSum = 0;
        int happinessSum = 0;
        int recHappinessSum = 0;
        int finHappinessSum = 0;
        int acaHappinessSum = 0;
        int buildHappinessSum = 0;
        int profHappinessSum = 0;

        //Loop through and find individual student's happiness in every category
        for (int i = 0; i < students.size(); i++) {
            healthHappinessSum += students.get(i).getHealthHappinessRating();
            happinessSum += students.get(i).getHappiness();
            recHappinessSum += students.get(i).getFunHappinessRating();
            finHappinessSum += students.get(i).getMoneyHappinessRating();
            acaHappinessSum += students.get(i).getAcademicHappinessRating();
            buildHappinessSum += students.get(i).getOverallBuildingHappinessRating();
            profHappinessSum += students.get(i).getProfessorHappinessRating();
        }

        //Calculate the average happiness of student population for all
        int avgHealthHappiness = healthHappinessSum/Math.max(1, students.size());
        int avgHappiness = happinessSum/Math.max(1,students.size());
        int avgRecHappiness = recHappinessSum/Math.max(1,students.size());
        int avgFinHappiness = finHappinessSum/Math.max(1,students.size());
        int avgAcaHappiness = acaHappinessSum/Math.max(1,students.size());
        int avgBuildHappiness = buildHappinessSum/Math.max(1,students.size());
        int avgProfHappiness = profHappinessSum/Math.max(1,students.size());

        //Set the happinesses averages accordingly, making sure they stay at 100 or below
        college.setStudentHealthHappiness(Math.min(100, avgHealthHappiness));
        college.setStudentBodyHappiness(Math.min(100, avgHappiness));
        college.setStudentRecreationalHappiness(Math.min(100, avgRecHappiness));
        college.setStudentFinancialHappiness(Math.min(100, avgFinHappiness));
        college.setStudentAcademicHappiness(Math.min(100, avgAcaHappiness));
        college.setStudentBuildingHappiness(avgBuildHappiness);
        college.setStudentProfessorHappiness(avgProfHappiness);
    }

    private void calculaterOverallStudentHealth(String collegeId, List<StudentModel> students) {
        CollegeManager.logger.info("StudentManager: health statistics");
        CollegeModel college = CollegeDao.getCollege(collegeId);

        int nSick = 0;
        for(int i = 0; i < students.size(); i++){
            if(students.get(i).getNumberHoursLeftBeingSick() > 0){
                nSick++;
            }
        }

        int percentageWell = ((Math.max(students.size(), 1) - nSick) * 100) / Math.max(students.size(), 1);
        int rating = Math.max(0, percentageWell);
        rating = Math.min(rating, 100);
        college.setStudentHealthRating(rating);

        //CollegeDao.saveCollege(college);
    }

    /**
     * Calculates the student happiness based on their tier minus the school trait rating(the difference).
     * This function compares the difference and if it is higher than -1, the student is considered happy in that category.
     * @param collegeId the current college
     * @param students the list of students
     */
    private void  calculateHappinessRatings(String collegeId, List<StudentModel> students){
        //i know, this is coded badly
        System.out.println("num students " + students.size());
        CollegeModel college = CollegeDao.getCollege(collegeId);
        int total = 0;
        int counter = 0;
        int bCounter = 0;
        int sportsCounter = 0;
        int socialCounter = 0;
        int costCounter = 0;
        int academicCounter = 0;
        int safetyCounter = 0;

        for(int i = 0; i < students.size(); i++){
            StudentModel student = students.get(i);
            if(student.getAcademicRating() > -1){
                academicCounter++;
                total += student.getAcademicRating();
                counter++;

            }
            System.out.println("acadmeics " + student.getAcademicRating());
            if(student.getInfrastructuresRating() > -1){
                bCounter++;
                total += student.getInfrastructuresRating();
                counter++;
            }
            if(student.getSocialRating() > -1){
                socialCounter++;
                total += student.getSocialRating();
                counter++;
            }
            if(student.getSportsRating() > -1){
                sportsCounter++;
                total += student.getSportsRating();
                counter++;
            }
            if(student.getCostRating() > -1){
                costCounter++;
                total += student.getCostRating();
                counter++;
            }
            if(student.getSafetyRating() > -1){
                safetyCounter++;
                total += student.getSafetyRating();
                counter++;
            }
        }
        college.setStudentAcademicsHappiness((academicCounter / students.size()) * 100);
        college.setStudentInfrastructuresHappiness((bCounter / students.size()) * 100);
        college.setStudentSocialHappiness((socialCounter / students.size()) * 100);
        college.setStudentSportsHappiness((sportsCounter / students.size()) * 100);
        college.setStudentCostHappiness((costCounter / students.size()) * 100);
        college.setStudentSafetyHappiness((safetyCounter / students.size()) * 100);
        college.setStudentBodyHappiness((total / counter) / 100);
        //System.out.println("overall " + college.getStudentBodyHappiness());
        System.out.println("academics " + college.getStudentAcademicsHappiness());
        System.out.println("buildings " + college.getStudentInfrastructuresHappiness());
        System.out.println("safety " + college.getStudentSafetyHappiness());
        System.out.println("sports " + college.getStudentSportsHappiness());
        System.out.println("cost " + college.getStudentCostHappiness());
        System.out.println("social " + college.getStudentSocialHappiness());
    }

    private void setHappinessForEachStudent(String collegeId, boolean initial, List<StudentModel> students){
        CollegeManager.logger.info("StudentManager: set happiness");
        CollegeModel college = CollegeDao.getCollege(collegeId);

        int aveFacultyRating = FacultyManager.getAverageFacultyPerformance(collegeId);

        for(int i = 0; i < students.size(); i++){
           StudentModel student = students.get(i);
//           setStudentHealthHappiness(student, initial);
//           setStudentAdvisorHappiness(student, college, initial);
//           setStudentProfessorHappiness(collegeId, student, aveFacultyRating);
//           setStudentAcademicHappiness(student, college);
//           setStudentMoneyHappiness(student, college, initial);
//           setStudentFunHappiness(student, college);
//           setDiningHallHappinessRating(student, college);
//           setAcademicCenterHappinessRating(student, college);
//           setDormHappinessRating(student, college);
//           setStudentOverallBuildingHappinessRating(student, college);

           student.setAcademicRating(student.getPersonality().getAcademics() - college.getAcademicRating());
           student.setCostRating(student.getPersonality().getCost() - college.getSchoolValue());
           student.setInfrastructuresRating(student.getPersonality().getInfrastructures() - college.getInfrastructureRating());
           student.setSafetyRating(student.getPersonality().getSafety() - college.getSafetyRating());
           student.setSocialRating(student.getPersonality().getCampusLife() - college.getSocialRating());
           student.setSportsRating(student.getPersonality().getSports() - college.getAthleticRating());

           System.out.println("academics difference " + student.getAcademicRating());
            System.out.println("cost difference " + student.getCostRating());
            System.out.println("infra difference " + student.getInfrastructuresRating());
            System.out.println("safety difference " + student.getSafetyRating());
            System.out.println("Social difference " + student.getSocialRating());
            System.out.println("Sports difference " + student.getSportsRating());

            setStudentFeedback(student, collegeId);

            if(student.getHappiness() >= 95){
                student.getAvatar().generateHappyAvatar();
                student.getAvatar().setEyes("Hearts");
            }
            else if(student.getHappiness() >= 70){
                student.getAvatar().generateHappyAvatar();
            }
            else if(student.getHappiness() <= 69 && student.getHappiness() >= 50){
                student.getAvatar().setMouth("Serious");
                student.getAvatar().setEyebrows("Default");
                student.getAvatar().setEyes("Default");

            }
            else if(student.getHappiness() < 50){
                student.getAvatar().generateUnhappyAvatar();
            }
        }


        dao.saveAllStudents(collegeId, students);
    }

    private void setDiningHallHappinessRating(StudentModel student, CollegeModel college) {
        if (student == null || college == null)
            return;

        String studentDiningHall = student.getDiningHall();
        BuildingModel building = BuildingManager.getBuildingByName(studentDiningHall, college.getRunId());

        if (building == null)
            return;

        int diningHallQuality = (int) building.getShownQuality();
        int diningHallHappinessLevel = SimulatorUtilities.getRandomNumberWithNormalDistribution(diningHallQuality, 15, 0, 100);

        student.setDiningHallHappinessRating(diningHallHappinessLevel);
    }

    private void setAcademicCenterHappinessRating(StudentModel student, CollegeModel college) {
        String studentAcademicCenter = student.getAcademicBuilding();
        int academicBuildingQuality = (int)BuildingManager.getBuildingByName(studentAcademicCenter, college.getRunId()).getShownQuality();
        int acadmicBuildingHappinessLevel = SimulatorUtilities.getRandomNumberWithNormalDistribution(academicBuildingQuality, 15, 0, 100);

        student.setAcademicCenterHappinessRating(acadmicBuildingHappinessLevel);
    }

    private void setDormHappinessRating(StudentModel student,CollegeModel college){
        String studentDorm = student.getDorm();
        int dormQuality = (int)BuildingManager.getBuildingByName(studentDorm, college.getRunId()).getShownQuality();
        int dormHappinessLevel = SimulatorUtilities.getRandomNumberWithNormalDistribution(dormQuality, 15, 0, 100);

        student.setDormHappinessRating(dormHappinessLevel);
    }

    private void setStudentFunHappiness(StudentModel s, CollegeModel college) {
         //TODO: how do we decide if students are having fun?
        s.setFunHappinessRating(50);
    }

    /**
     * Sets the money/financial happiness of a single student
     *
     * @param s The student having their happiness updated
     * @param college The college that is currently in use
     * @param initial Value to determine if this is the first time setup of the college
     */
    private void setStudentMoneyHappiness(StudentModel s, CollegeModel college, Boolean initial) {
        int rating; //The rating of the student
        if(initial) {
            //For the initial start, students have a rating of 0 so this needs to be set
            rating = SimulatorUtilities.getRatingZeroToOneHundred(70000, 25000, college.getYearlyTuitionCost());
        }
        else {
            rating = college.getYearlyTuitionRating(); //Previous rating the student had
        }
        s.setMoneyHappinessRating(SimulatorUtilities.getRandomNumberWithNormalDistribution(rating, 15, 0, 100));
    }

    private void setStudentAdvisorHappiness(StudentModel s, CollegeModel college, boolean initial) {

        // TODO: Bug-fix function to set value that isn't 0.

        Random r = new Random();
        int happinessRating;
        if (initial){
            happinessRating = SimulatorUtilities.getRandomNumberWithNormalDistribution(75, 15, 0, 100);
        }
        else{
            happinessRating = s.getAdvisorHappinessHappinessRating();
            if(s.getAdvisor() == null)
                Logger.getAnonymousLogger().warning("null advisor on student " + s.getName());
            if((FacultyDao.getAdvisor(college.getRunId(), s.getAdvisor())).getPerformance() > 75){
                int happinessIncrease = r.nextInt((5 - 2) + 1) + 2;
                happinessRating += 5;
            }
            else if((FacultyDao.getAdvisor(college.getRunId(), s.getAdvisor())).getPerformance() < 50){
                double rand = Math.random();
                int happinessNumber = r.nextInt((3 - 1) + 1) + 1;
                if(rand <= 0.5)
                    happinessRating += happinessNumber;
                else
                    happinessRating -= happinessNumber;
                happinessRating -= 5;
            }
            else{
                int happinessDecrease = r.nextInt((5 - 2) + 1) + 2;
                happinessRating -= happinessDecrease;
            }
        }
        happinessRating = Math.max(happinessRating, 0);
        happinessRating = Math.min(happinessRating, 100);
        s.setAdvisorHappinessHappinessRating(happinessRating);
    }

    private void setStudentAcademicHappiness(StudentModel s, CollegeModel college) {
        int happiness =
                (int) (0.4 * college.getStudentFacultyRatioRating() +
                        0.2 * s.getAdvisorHappinessHappinessRating() +
                        0.4 * s.getProfessorHappinessRating());
        if(s.getNature() == "Studious"){
            happiness += 10;
        }
        else if(s.getNature() == "Lazy" || s.getNature() == "Rebellious"){
            happiness -= 10;
        }
        happiness = Math.min(100, happiness);
        int happinessRating = SimulatorUtilities.getRandomNumberWithNormalDistribution(happiness, 5, 0, 100);

        s.setAcademicHappinessRating(happinessRating);
    }

    private void setStudentProfessorHappiness(String collegeId, StudentModel s, int aveFacultyPerformance){
        int happinessRating = SimulatorUtilities.getRandomNumberWithNormalDistribution(aveFacultyPerformance, 10, 0, 100);
        if(s.getNature() == "Studious"){
            happinessRating += 5;
        }
        else if(s.getNature() == "Lazy" || s.getNature() == "Rebellious"){
            happinessRating -= 5;
        }
        Math.min(100, happinessRating);
        s.setProfessorHappinessRating(happinessRating);
    }

    /**
     * Changed!!!
     * Set the health happiness of a single student
     *
     * @param s The student to update
     * @param initial If this is a first time setup of the college
     */
    private void setStudentHealthHappiness(StudentModel s, boolean initial) {
        if (initial) {
            //When college starts
            s.setHealthHappinessRating(100);
        }
        else {
            //Calculation: Everyday they are sick is -48 happiness, plus a bit of random deviation
            int happiness = (100-2*s.getNumberHoursLeftBeingSick());
            happiness = SimulatorUtilities.getRandomNumberWithNormalDistribution(happiness, 10, 0, 100);
            s.setHealthHappinessRating(happiness);
        }
    }

    /**
     * This controls how the buildings affect the students
     * Their happiness can go up or down depending on the quality of the buildings
     *
     * @param s - Single instance of student
     * @param college - The college
     */
    private void setStudentOverallBuildingHappinessRating(StudentModel s, CollegeModel college){
        List<BuildingModel> allBuildings = BuildingDao.getBuildings(college.getRunId());
        String studentDormName = s.getDorm();
        String studentDiningHallName = s.getDiningHall();
        String studentAcademicCenterName = s.getAcademicBuilding();

        List<BuildingModel> studentsBuildingsOnly = new ArrayList<>();
        studentsBuildingsOnly.add(BuildingManager.getBuildingByName(studentDormName, college.getRunId())); // Add only the dorm the student is in
        studentsBuildingsOnly.add(BuildingManager.getBuildingByName(studentDiningHallName, college.getRunId())); // Add only the dining hall the student is in
        studentsBuildingsOnly.add(BuildingManager.getBuildingByName(studentAcademicCenterName, college.getRunId())); // Add only the academic center the student is in

        int totalBuildingQuality = 0;
        int avgBuildingQuality;
        int entertainmentHappiness = 0;

        int buildingHappinessLevel;

        for(BuildingModel b : allBuildings){
            // Certain buildings have to be added to the student no matter what
            // These include Health Center, Library, Sports Center, Baseball Diamond, Football Stadium, and Hockey Rink
            if(b.getKindOfBuilding().equals(BuildingType.health().getType()) ||
                    b.getKindOfBuilding().equals(BuildingType.library().getType()) ||
                    b.getKindOfBuilding().equals(BuildingType.sports().getType()) ||
                    b.getKindOfBuilding().equals(BuildingType.baseballDiamond().getType()) ||
                    b.getKindOfBuilding().equals(BuildingType.footballStadium().getType()) ||
                    b.getKindOfBuilding().equals(BuildingType.hockeyRink().getType())){
                studentsBuildingsOnly.add(b);
            }
            else if(b.getKindOfBuilding().equals(BuildingType.entertainment().getType())){
                // The entertainment center always adds happiness, but will add more for a higher quality center
                entertainmentHappiness += (int)b.getShownQuality()/25; //Should never be more than 4
            }
            else{
                continue; // If the student isn't in the building or if it's not one of the ones from above, skip it
            }
        }

        for (BuildingModel b : studentsBuildingsOnly){
            if (b != null) {
                totalBuildingQuality += (int) b.getShownQuality();
            }
        }

        avgBuildingQuality = totalBuildingQuality/studentsBuildingsOnly.size();


        // This creates a bell-curve average where:
        // avgBuildingQuality is between 0 and 100
        // Standard deviation is 15
        // Min is 0 since that's the lowest possible building quality
        // Max is 100 since that's the highest possible building quality
        // The entertainment happiness is added at the end since it should ALWAYS add happiness
        buildingHappinessLevel = SimulatorUtilities.getRandomNumberWithNormalDistribution(avgBuildingQuality, 15, 0, 100) + entertainmentHappiness;
        buildingHappinessLevel = Math.min(buildingHappinessLevel, 100);
        s.setOverallBuildingHappinessRating(buildingHappinessLevel);
    }

    private void calculateStudentFacultyRatio(String collegeId, List<StudentModel> students) {
        CollegeModel college = CollegeDao.getCollege(collegeId);
        List<FacultyModel> faculty = facultyDao.getFaculty(college.getRunId());

        college.setStudentFacultyRatio(students.size() / Math.max(faculty.size(),1));

        //CollegeDao.saveCollege(college);
    }

    private void calculateStudentFacultyRatioRating(String collegeId) {
        CollegeModel college = CollegeDao.getCollege(collegeId);
        int rating = SimulatorUtilities.getRatingZeroToOneHundred(20, 5, college.getStudentFacultyRatio());
        college.setStudentFacultyRatioRating(rating);
        CollegeDao.saveCollege(college);
    }

    /**
     * Calculate retention rate of students.  This is based on how many students
     * withdrew compare to students that remained.
     *
     * @param collegeId
     */
    private void calculateRetentionRate(String collegeId) {
        college = CollegeDao.getCollege(collegeId);
        int retentionRate;
        int nAdmit = Math.max(1, college.getNumberStudentsAdmitted());
        int nWithdrawn = Math.max(0, college.getNumberStudentsWithdrew());
        nWithdrawn = Math.min(nAdmit, nWithdrawn);
        retentionRate = 100 * (nAdmit - nWithdrawn) / nAdmit;
        college.setRetentionRate(retentionRate);

        //CollegeDao.saveCollege(college);
    }

    /**
     * Rolls a number and returns whether or not the roll was higher than the odds
     *
     * @param oddsBetween0And1 The odds of success (.64 = 64% of returning false)
     *
     * @return True if the roll was higher, false if lower
     */
    private boolean didItHappen(double oddsBetween0And1) {
        double roll = Math.random();
        return (roll > oddsBetween0And1);
    }

    /**
     * Check if a student is very unhappy in any of the overallHappiness categories
     *
     * @param s The student to be checked
     *
     * @return True if a student is very unhappy in any category, false if not
     */
    private boolean checkForUnhappiness(StudentModel s) {
        return s.getAcademicHappinessRating() < 20 || s.getHealthHappinessRating() < 20 ||
                s.getMoneyHappinessRating() < 20 || s.getOverallBuildingHappinessRating() < 20
                || s.getProfessorHappinessRating() < 20;
    }

    /**
     * Load up the tips in the Students Tab
     *
     * @param collegeId The id of the college in use
     */
    private void loadTips(String collegeId) {
        // Only the first tip should be set to true.
        //Add any new tips in here to be displayed in the Student tab
        TutorialManager.saveNewTip(collegeId, 0,"viewStudent", "If the students aren't happy they might leave.", true, "student.png");
        TutorialManager.saveNewTip(collegeId, 1,"viewStudent", "Click on different students to see how they are feeling.", false);
        TutorialManager.saveNewTip(collegeId, 1,"viewStudent", "Tuition is a major factor in student happiness.", false);
    }

    /**
     * @param collegeId
     * @param buildingName
     * @param  buildingType
     *
     * Checks if the building destroyed was one the student wass assigned to and moves them if necessary
     */
    public void removeFromBuildingAndReassignAfterDisaster(String collegeId, String buildingName, String buildingType){
        List<StudentModel> students = dao.getStudents(collegeId);
        for(StudentModel s : students){
            if(buildingType.equals(BuildingType.dorm().getType())){
                if(buildingName.equals(s.getDorm())){
                    buildingMgr.assignDorm(collegeId);
                }
            }
            else if(buildingType.equals(BuildingType.dining().getType())){
                if(buildingName.equals(s.getDiningHall())){
                    buildingMgr.assignDiningHall(collegeId);
                }
            }
            else if(buildingType.equals(BuildingType.academic().getType())){
                if(buildingName.equals(s.getAcademicBuilding())){
                    buildingMgr.assignAcademicBuilding(collegeId);
                }
            }
        }
    }

    /**
     * @param student   The student who's giving feedback
     */
    private static void setStudentFeedback(StudentModel student, String collegeId) {
        Boolean usesVerb        = (Math.random() >= 0.5);
        Boolean useNoun         = true;
        String feedback         = (usesVerb ? "I " : "The ");
        int useNeutralIntro;

        List<String> verbs      = new ArrayList<>();
        List<String> adjectives = new ArrayList<>();

        HashMap<String, Integer> happinessLevels = new HashMap<>();



        happinessLevels.put("academic", student.getAcademicHappinessRating());
//      happinessLevels.put("advisor", student.getAdvisorHappinessHappinessRating());
        happinessLevels.put("health", student.getHealthHappinessRating());
        happinessLevels.put("money", student.getMoneyHappinessRating());
//      happinessLevels.put("fun" , student.getFunHappinessRating());

        // Negative feedback
        if(student.getHappiness() < 50) {
            Collections.addAll(verbs,"hate ", "don't like ", "dislike ", "am displeased with ", "am not a fan of ");
            Collections.addAll(adjectives, "awful ", "bad ", "terrible ", "crummy ", "lousy ", "sad ");

        // Positive feedback
        } else if(student.getHappiness() > 70) {
            Collections.addAll(verbs, "love ", "am very pleased with ", "am ecstatic about ");
            Collections.addAll(adjectives, "great ", "fantastic ", "super ", "surprisingly well ");

        // Neutral feedback
        } else {
            useNoun                 = false;
            String[] neutralIntro   = {"All things considered, ", "All together, ", ""};
            String[] neutralMidro   = {"Everything is ", "All is ", "Things are "};
            String[] neutralOutro   = {"okay", "alright", "in order", "not bad", "fine", "reasonable", "average"};

            useNeutralIntro = new Random().nextInt(neutralIntro.length);

            if(useNeutralIntro==neutralIntro.length-1) {
                feedback = neutralMidro[new Random().nextInt(neutralMidro.length)];
            }
            else {
                feedback =  neutralIntro[useNeutralIntro] +
                            neutralMidro[new Random().nextInt(neutralMidro.length)].toLowerCase();
            }
                feedback += neutralOutro[new Random().nextInt(neutralOutro.length)];
        }

        if(useNoun) {       //If it was not a neutral response
            if (usesVerb) {     //If random chance when usesVerb was assigned was true
                feedback += verbs.get(new Random().nextInt(verbs.size())) +
                        "the " + getFeedbackNoun(happinessLevels, true);
            }
            else {
                feedback += getFeedbackNoun(happinessLevels, true) + "is " +
                        adjectives.get(new Random().nextInt(adjectives.size()));
            }
        }
        // Conclusion
        String[] conclusion =  {" here", " at this school", " at "+collegeId, ""};
        feedback += conclusion[new Random().nextInt(conclusion.length)];
        String[] positivePunctuation = {"!", "!!", "!!!"};
        String[] negativePunctuation = {".", "..", "..."};
        if(student.getHappiness() < 50) {
            feedback += negativePunctuation[new Random().nextInt(negativePunctuation.length)];
        } else if (student.getHappiness() > 70) {
            feedback += positivePunctuation[new Random().nextInt(positivePunctuation.length)];
        } else {
            feedback += ".";
        }
        student.setFeedback(feedback);
    }


    private static String getFeedbackNoun(HashMap<String, Integer> vals, Boolean isNegative) {

        Map.Entry<String, Integer> current = vals.entrySet().iterator().next();
        List<String> nouns = new ArrayList<>();

        for(Map.Entry<String, Integer> e : vals.entrySet()) {
            if(isNegative && e.getValue() < current.getValue())
                current = e;
            if(!isNegative && e.getValue() > current.getValue())
                current = e;
        }

        if(current.getKey().equals("money"))
            Collections.addAll(nouns,"tuition ", "tuition rate ");

        else if(current.getKey().equals("academic"))
            Collections.addAll(nouns,"number of professors ", "student professor ratio ");

        else if(current.getKey().equals("health"))
            Collections.addAll(nouns,"health care ", "healthiness ", "health system ");

        return nouns.get(new Random().nextInt(nouns.size()));
    }

    public int getNumRebelliousStudentsRatio(String collegeId) {
        int numStudents=0;
        int totalStudents=0;
        List<StudentModel> students = dao.getStudents(collegeId);
        for(StudentModel s : students) {
            if (s.getNature() == "Rebellious")
                numStudents += 1;
            totalStudents += 1;
        }
        return totalStudents / numStudents;
    }

    private String assignRandomNature() {
        if (rand.nextInt(10) > 8)
            return "Impulsive";
        else if (rand.nextInt(10) > 6)
            return "Studious";
        else if (rand.nextInt(10) > 4)
            return "Lazy";
        else if (rand.nextInt(10) > 2)
            return "Rebellious";
        else if (rand.nextInt(10) > 1)
            return "Party Fiend";
        else
            return "Normal";
    }
}

