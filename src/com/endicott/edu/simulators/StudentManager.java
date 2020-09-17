package com.endicott.edu.simulators;
import com.endicott.edu.datalayer.*;
import com.endicott.edu.models.*;

import java.util.*;

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
     * @param collegeId
     */
    public void establishCollege(String collegeId) {
        loadTips(collegeId);
        admitStudents(collegeId, college.getCurrentDay()/24, true, null);
        calculateStatistics(collegeId, true);
    }

    /**
     * Simulate the changes in students and student finances
     * due to passage of time at the college.
     *
     * @param collegeId
     * @param hoursAlive
     */
    public void handleTimeChange(String collegeId, int hoursAlive, PopupEventManager popupManager) {
        acceptStudents(collegeId, hoursAlive);

        CollegeManager.logger.info("Advance Time Students: admit - " + CollegeManager.getDate());
        admitStudents(collegeId, hoursAlive, false, popupManager);

        CollegeManager.logger.info("Advance Time Students: tuition - " + CollegeManager.getDate());
        receiveStudentTuition(collegeId);

        CollegeManager.logger.info("Advance Time Students: withdraw - " + CollegeManager.getDate());
        withdrawStudents(collegeId, hoursAlive);

        CollegeManager.logger.info("Advance Time Students: statistics - " + CollegeManager.getDate());
        calculateStatistics(collegeId, false);

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
        Accountant.receiveIncome(collegeId,"Student tuition received.",dailyTuitionSum);
    }

    /**
     * Admit new students to the college.
     *  @param collegeId
     * @param hoursAlive
     * @param isNewCollege
     * @param popupManager
     */
    public void admitStudents(String collegeId, int hoursAlive, boolean isNewCollege, PopupEventManager popupManager) {
        int openBeds = buildingMgr.getOpenBeds(collegeId);
        int openPlates = buildingMgr.getOpenPlates(collegeId);
        int openDesks = buildingMgr.getOpenDesks(collegeId);
        int numNewStudents = 0;

        //Get the administrative building quality
        int adminBuildingQuality = 0;
        try {
            adminBuildingQuality = (int) BuildingManager.getBuildingListByType(BuildingType.admin().getType(), collegeId).get(0).getShownQuality();
        } catch (Exception e) {
        }

        List<StudentModel> students = StudentDao.getStudents(collegeId);
        //Date currDate = CollegeManager.getCollegeDate(collegeId);

        if(isNewCollege) {
            numNewStudents = 140;
        }
        else if(CollegeManager.getDaysOpen(collegeId) % 7 == 0){   // Students admitted every 7 days
            NewsManager.createNews(collegeId, hoursAlive, "This is admissions day!", NewsType.COLLEGE_NEWS, NewsLevel.GOOD_NEWS);
            int leastOpenings = Math.min(openBeds, openDesks); //Since math.min doesn't take 3 params, continue onto next line
            leastOpenings = Math.min(leastOpenings, openPlates);

            numNewStudents = college.getNumberStudentsAccepted();
            if (numNewStudents > leastOpenings) {
                numNewStudents = leastOpenings;
            }

            if(adminBuildingQuality <= 90) {
                // This will produce a number 1-10, representing every 10% BELOW 100%
                int adminQualityLossByTens = (100 - adminBuildingQuality) / 10;
                // The college should LOSE 5% of the accepted students for every 10% under 100% quality of the admin building
                int lostStudents = (int)(numNewStudents * (0.05 * adminQualityLossByTens));
                // Take the students away
                numNewStudents = numNewStudents - lostStudents;
                // Notify the player that some students didn't end up coming due to admin office quality
                NewsManager.createNews(collegeId, hoursAlive, "The administrative building lost papers and "
                        + lostStudents + " students weren't enrolled! Be sure to repair your administrative building!",
                        NewsType.COLLEGE_NEWS, NewsLevel.BAD_NEWS);
            }
        }

        if (numNewStudents > 0) {
            createStudents(numNewStudents, collegeId, students, false);

            college = CollegeDao.getCollege(collegeId);
            college.setNumberStudentsAdmitted(college.getNumberStudentsAdmitted() + numNewStudents);
            college.setNumberStudentsAccepted(0);
            //CollegeDao.saveCollege(college);
            NewsManager.createNews(collegeId, hoursAlive, Integer.toString(numNewStudents) +
                    " students joined the college.", NewsType.COLLEGE_NEWS, NewsLevel.GOOD_NEWS);
//            if (popupManager != null)
//                popupManager.newPopupEvent(collegeId, "Admissions Day",
//                    "" + numNewStudents +" new students joined!","Ok",
//                    "ok", "resources/images/students.png", "Admissions");

        }

        StudentDao.saveAllStudentsUsingCache(collegeId);
    }

    static public void setStudentIndex(int i) {
        studentIndex = i;
    }

    static public int getStudentIndex() {
        return studentIndex;
    }

    /**
     * Determine how many students to accept in one day.
     */
    private void acceptStudents(String collegeId, int hoursAlive){
        int numNewStudents;

        // The number of accepted students depends on the happiness of the student body.
        college = CollegeDao.getCollege(collegeId);
        int happiness = college.getStudentBodyHappiness();

        if (happiness <= 50) {
            numNewStudents = 0;
        } else {
            int meanAdmittedStudents = happiness/10; // Example: happiness = 50, then 5 students
            numNewStudents =  SimulatorUtilities.getRandomNumberWithNormalDistribution(meanAdmittedStudents,1, 0, 10);
        }

        college.setNumberStudentsAccepted(college.getNumberStudentsAccepted() + numNewStudents);
        //CollegeDao.saveCollege(college);

        if (numNewStudents > 0) {
            NewsManager.createNews(collegeId, hoursAlive, Integer.toString(numNewStudents) + " students have been accepted to the college.", NewsType.COLLEGE_NEWS, NewsLevel.GOOD_NEWS);
        }
    }

    private void createStudents(int numNewStudents, String collegeId, List<StudentModel>students, boolean initial){
        for (int i = 0; i < numNewStudents; i++) {
            StudentModel student = new StudentModel();
            if (rand.nextInt(10) + 1 > 5) {
                student.setName(NameGenDao.generateName(false));
                student.setGender("Male");
            } else {
                student.setName(NameGenDao.generateName(true));
                student.setGender("Female");
            }

            student.setIdNumber(IdNumberGenDao.getID(collegeId));
            student.setHappinessLevel(70);
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
            student.setAdvisor(FacultyManager.assignAdvisorToStudent(collegeId, student));
            students.add(student);
            dao.saveAllStudentsJustToCache(collegeId, students);
        }
    }

    /**
     * Is this a day on which figure out which students are leaving the college?
     */
    private boolean isWithdrawlDay(String collegeId) {
        int day = CollegeManager.getCollegeCurrentDay(collegeId);
        return (day % 7 == 0);
    }
    /**
     * Withdraw students from the college.
     *
     * @param collegeId
     * @param hoursAlive
     */
    private void withdrawStudents(String collegeId, int hoursAlive) {

//        if (!isWithdrawlDay(collegeId))
//            return;

        float scalingFactor = .01f;
        List<StudentModel> students = dao.getStudents(collegeId);
        int nStudents = students.size();

        // scroll through students list and remove student based upon a probability determined by their happiness level
        // the lower the happiness the greater the chance they have of withdrawing
        int studentsWithdrawn = 0;

        for (int i = 0; i < students.size(); i++){
            int h = students.get(i).getHappinessLevel();
            double odds = Math.max(0, h/100.0); //Odds are just a percentage of happiness (.64 = 64% they stay)
            CollegeManager.logger.info("Odds are: " + odds);
            if (students.get(i).getHappinessLevel() < .65 && didItHappen(odds)) {
                CollegeManager.logger.info("A student withdrew!");
                buildingMgr.removeStudent(collegeId, students.get(i).getDorm(), students.get(i).getDiningHall(), students.get(i).getAcademicBuilding());
                students.remove(i);
                studentsWithdrawn++;
            }
        }

        dao.saveAllStudents(collegeId, students);

        college = CollegeDao.getCollege(collegeId);
        college.setNumberStudentsAdmitted(Math.max(college.getNumberStudentsAdmitted(), nStudents));
        college.setNumberStudentsWithdrew(college.getNumberStudentsWithdrew() + studentsWithdrawn);

        //CollegeDao.saveCollege(college);
        // Don't create a news story if no students leave
        if ((nStudents - students.size()) > 0) {
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
        List<StudentModel> students = dao.getStudents(collegeId);
        calculaterOverallStudentHealth(collegeId, students);
        calculateStudentFacultyRatio(collegeId, students);
        calculateStudentFacultyRatioRating(collegeId);

        setHappinessForEachStudent(collegeId, initial, students);

        calculateOverallStudentHappiness(collegeId, students);
        calculateOverallStudentRecreationalHappiness(collegeId, students);
        calculateOverallStudentFinancialHappiness(collegeId, students);

        calculateRetentionRate(collegeId);
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

        int percentageWell = 100 - (2*(nSick * 100) / Math.max(students.size(), 1));
        int rating = Math.max(0, percentageWell);
        rating = Math.min(rating, 100);
        //int rating = SimulatorUtilities.getRatingZeroToOneHundred(50, 95, percentageWell);
        college.setStudentHealthRating(rating);

        //CollegeDao.saveCollege(college);
    }

    private void calculateOverallStudentHappiness(String collegeId, List<StudentModel> students) {
        CollegeManager.logger.info("StudentManager: happiness statistics");
        CollegeModel college = CollegeDao.getCollege(collegeId);

        int happinessSum = 0;
        for (int i = 0; i < students.size(); i++) {
            happinessSum += students.get(i).getHappinessLevel();
        }

        int aveHappiness = happinessSum/Math.max(1,students.size());
        // Add a little randomness. Currently happiness doesn't seem to
        // vary much from day to day.  That is an issue.
        Random rand = new Random();
        college.setStudentBodyHappiness(Math.min(100, aveHappiness + rand.nextInt(8)));

        //CollegeDao.saveCollege(college);
    }

    private void calculateOverallStudentRecreationalHappiness(String collegeId, List<StudentModel> students) {
        CollegeManager.logger.info("StudentManager: recreational happiness statistics");
        CollegeModel college = CollegeDao.getCollege(collegeId);

        int recHappinessSum = 0;
        for (int i=0; i<students.size(); i++) {
            recHappinessSum += students.get(i).getFunHappinessRating();
        }

        int avgRecHappiness = recHappinessSum/Math.max(1,students.size());

        Random rand = new Random();
        college.setStudentRecreationalHappiness(Math.min(100, avgRecHappiness + rand.nextInt(8)));
    }

    private void calculateOverallStudentFinancialHappiness(String collegeId, List<StudentModel> students) {
        CollegeManager.logger.info("StudentManager: financial happiness statistics");
        CollegeModel college = CollegeDao.getCollege(collegeId);

        int finHappinessSum = 0;
        for (int i=0; i<students.size(); i++) {
            finHappinessSum += students.get(i).getMoneyHappinessRating();
        }

        int avgFinHappiness = finHappinessSum/Math.max(1,students.size());

        Random rand = new Random();
        college.setStudentFinancialHappiness(Math.min(100, avgFinHappiness + rand.nextInt(8)));
    }

    private void setHappinessForEachStudent(String collegeId, boolean initial, List<StudentModel> students){
        CollegeManager.logger.info("StudentManager: set happiness");
        CollegeModel college = CollegeDao.getCollege(collegeId);

        int aveFacultyRating = FacultyManager.getAverageFacultyPerformance(collegeId);

        for(int i = 0; i < students.size(); i++){
           StudentModel student = students.get(i);
           setStudentHealthHappiness(student, initial);
           setStudentAcademicHappiness(student, college);
           setStudentAdvisorHappiness(student, college, initial);
           setStudentMoneyHappiness(student, college);
           setStudentFunHappiness(student, college);
           setDiningHallHappinessRating(student, college);
           setAcademicCenterHappinessRating(student, college);
           setDormHappinessRating(student, college);
           setStudentProfessorHappiness(collegeId, student, aveFacultyRating);
           setStudentOverallBuildingHappinessRating(student, college);
           setStudentFeedback(student, collegeId);

           // Below if you add up the factors on happiness, they sum to 1.0

           int happiness =
                   (int) (0.1 * student.getAcademicHappinessRating() +
                          0.2 * student.getHealthHappinessRating() +
                          0.5 * student.getMoneyHappinessRating() +
                          //0.1 * student.getFunHappinessRating() +     Uncomment this once Fun can be interacted with
                          0.1 * student.getOverallBuildingHappinessRating() +
                          0.1 * student.getProfessorHappinessRating());

           //If the rating of any one happiness is very low, their overall happiness will drop further
            if(checkForUnhappiness(student)) {
                happiness -= 30;
            }
            happiness = Math.min(happiness, 100);
            happiness = Math.max(happiness, 0);

            students.get(i).setHappinessLevel(happiness);
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
        setStudentFeedback(student, college.getRunId());
    }

    private void setAcademicCenterHappinessRating(StudentModel student, CollegeModel college) {
        String studentAcademicCenter = student.getAcademicBuilding();
        int academicBuildingQuality = (int)BuildingManager.getBuildingByName(studentAcademicCenter, college.getRunId()).getShownQuality();
        int acadmicBuildingHappinessLevel = SimulatorUtilities.getRandomNumberWithNormalDistribution(academicBuildingQuality, 15, 0, 100);

        student.setAcademicCenterHappinessRating(acadmicBuildingHappinessLevel);
        setStudentFeedback(student, college.getRunId());
    }

    private void setDormHappinessRating(StudentModel student,CollegeModel college){
        String studentDorm = student.getDorm();
        int dormQuality = (int)BuildingManager.getBuildingByName(studentDorm, college.getRunId()).getShownQuality();
        int dormHappinessLevel = SimulatorUtilities.getRandomNumberWithNormalDistribution(dormQuality, 15, 0, 100);

        student.setDormHappinessRating(dormHappinessLevel);
        setStudentFeedback(student, college.getRunId());
    }

    private void setStudentFunHappiness(StudentModel s, CollegeModel college) {
        // TODO: how do we decide if students are having fun?
        s.setFunHappinessRating(50);
    }

    private void setStudentMoneyHappiness(StudentModel s, CollegeModel college) {

        // TODO: Bug-fix function to set value that isn't 0.

        int rating = college.getYearlyTuitionRating(); // This is rating 0 to 100
        s.setMoneyHappinessRating(SimulatorUtilities.getRandomNumberWithNormalDistribution(rating, 15, 0, 100));
    }

    private void setStudentAdvisorHappiness(StudentModel s, CollegeModel college, boolean initial) {

        // TODO: Bug-fix function to set value that isn't 0.

        Random r = new Random();
        int happinessRating;
        if (initial){
            happinessRating = SimulatorUtilities.getRandomNumberWithNormalDistribution(50, 15, 0, 100);
        }
        else{
            happinessRating = s.getAdvisorHappinessHappinessRating();
            if(s.getAdvisor().getPerformance() > 75){
                int happinessIncrease = r.nextInt((5 - 2) + 1) + 2;
                happinessRating += happinessIncrease;
            }
            else if(s.getAdvisor().getPerformance() > 50){
                double rand = Math.random();
                int happinessNumber = r.nextInt((3 - 1) + 1) + 1;
                if(rand <= 0.5)
                    happinessRating += happinessNumber;
                else
                    happinessRating -= happinessNumber;
            }
            else{
                int happinessDecrease = r.nextInt((5 - 2) + 1) + 2;
                happinessRating -= happinessDecrease;
            }
        }
        happinessRating = Math.max(happinessRating, 0);
        happinessRating = Math.min(happinessRating, 100);
        s.setAdvisorHappinessHappinessRating(happinessRating);
        setStudentFeedback(s, college.getRunId());
    }

    private void setStudentAcademicHappiness(StudentModel s, CollegeModel college) {
        int rating = college.getStudentFacultyRatioRating(); // This is rating 0 to 100
        int happinessRating = SimulatorUtilities.getRandomNumberWithNormalDistribution(rating, 15, 0, 100);

        happinessRating = Math.max(happinessRating, 0);
        happinessRating = Math.min(happinessRating, 100);

        s.setAcademicHappinessRating(happinessRating);
        setStudentFeedback(s, college.getRunId());
    }

    private void setStudentProfessorHappiness(String collegeId, StudentModel s, int aveFacultyPerformance){
        int happinessRating = SimulatorUtilities.getRandomNumberWithNormalDistribution(aveFacultyPerformance, 10, 0, 100);
        s.setProfessorHappinessRating(happinessRating);
        setStudentFeedback(s, college.getRunId());
    }

    /**
     * Set the health happiness of a single student
     *
     * @param s The student to update
     * @param initial If this is a first time setup of the college
     */
    private void setStudentHealthHappiness(StudentModel s, boolean initial) {
        if (initial) {
            s.setHealthHappinessRating(100);
        }
        else if (s.getNumberHoursLeftBeingSick() > 0){
            s.setHealthHappinessRating(0);
        }
        else if(s.getNumberHoursLeftBeingSick() < 100) {
            s.setHealthHappinessRating(s.getHealthHappinessRating()+25);
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
        setStudentFeedback(s, college.getRunId());
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

    private boolean didItHappen(double oddsBetween0And1) {
        double roll = Math.random();
        CollegeManager.logger.info("The roll: " + roll);
        return (roll > oddsBetween0And1);
    }

    private boolean checkForUnhappiness(StudentModel s) {
        return s.getAcademicHappinessRating() < 20 || s.getHealthHappinessRating() < 20 ||
                s.getMoneyHappinessRating() < 20 || s.getOverallBuildingHappinessRating() < 20
                || s.getProfessorHappinessRating() < 20; }

    private void loadTips(String collegeId) {
        // Only the first tip should be set to true.
        TutorialManager.saveNewTip(collegeId, 0,"viewStudent", "If the students aren't happy they might leave.", true, "student.png");
        TutorialManager.saveNewTip(collegeId, 1,"viewStudent", "Click on different students to see how they feel.", false);
        TutorialManager.saveNewTip(collegeId, 1,"viewStudent", "There are many different things that makes a student happy.", false);
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
     * @return          The feedback based on the students happiness levels
     */
    private static String setStudentFeedback(StudentModel student, String collegeId) {
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
        if(student.getHappinessLevel() < 50) {
            Collections.addAll(verbs,"hate ", "don't like ", "dislike ", "am displeased with ", "am not a fan of ");
            Collections.addAll(adjectives, "awful ", "bad ", "terrible ", "crummy ", "lousy ", "sad ");

        // Positive feedback
        } else if(student.getHappinessLevel() > 70) {
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
            } else {
                feedback =  neutralIntro[useNeutralIntro] +
                            neutralMidro[new Random().nextInt(neutralMidro.length)].toLowerCase();
            }
                feedback += neutralOutro[new Random().nextInt(neutralOutro.length)];

        }

        if(useNoun)
            if (usesVerb)
                feedback += verbs.get(new Random().nextInt(verbs.size())) +
                            "the " + getFeedbackNoun(happinessLevels, true);
            else
                feedback += getFeedbackNoun(happinessLevels, true) + "is " +
                            adjectives.get(new Random().nextInt(adjectives.size()));

        // Conclusion
        String[] conclusion =  {" here", " at this school", " at "+collegeId, ""};
        feedback += conclusion[new Random().nextInt(conclusion.length)];
        String[] positivePunctuation = {"!", "!!", "!!!"};
        String[] negativePunctuation = {".", "..", "..."};
        if(student.getHappinessLevel() < 50) {
            feedback += negativePunctuation[new Random().nextInt(negativePunctuation.length)];
        } else if (student.getHappinessLevel() > 70) {
            feedback += positivePunctuation[new Random().nextInt(positivePunctuation.length)];
        } else {
            feedback += ".";
        }

        student.setFeedback(feedback);

        return feedback;
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
}

