package com.endicott.edu.simulators;
import com.endicott.edu.datalayer.*;
import com.endicott.edu.models.*;

import java.util.Date;
import java.util.List;
import java.util.Random;
/**
 * Responsible for simulating students at the college.
 */
public class StudentManager {
    private StudentDao dao = new StudentDao();
    private CollegeDao collegeDao = new CollegeDao();
    private FacultyDao facultyDao = new FacultyDao();
    private BuildingManager buildingMgr = new BuildingManager();
    private CollegeModel college = new CollegeModel();
    private Random rand = new Random();

    /**
     * The college has just been created.  Add initial students and calculate
     * student statistics.
     *
     * @param collegeId
     */
    public void establishCollege(String collegeId) {
        loadTips(collegeId);
        admitStudents(collegeId, college.getCurrentDay()/24, true);
        calculateStatistics(collegeId);
    }

    /**
     * Simulate the changes in students and stutdent finances
     * due to passage of time at the college.
     *
     * @param collegeId
     * @param hoursAlive
     */
    public  void handleTimeChange(String collegeId, int hoursAlive, PopupEventManager popupManager) {
        acceptStudents(collegeId, hoursAlive);
        admitStudents(collegeId, hoursAlive, false);
        receiveStudentTuition(collegeId);
        withdrawStudents(collegeId, hoursAlive);
        calculateStatistics(collegeId);
        updateStudentsTime(collegeId, hoursAlive);
    }

    /**
     * Receive student tuition.
     *
     * @param collegeId
     */
    private void receiveStudentTuition(String collegeId) {
        List<StudentModel> students = dao.getStudents(collegeId);
        college = collegeDao.getCollege(collegeId);
        int dailyTuitionSum = (college.getYearlyTuitionCost() / 365) * students.size();
        Accountant.receiveIncome(collegeId,"Student tuition received.",dailyTuitionSum);
    }

    /**
     * Admit new students to the college.
     *
     * @param collegeId
     * @param hoursAlive
     * @param initial
     */
    public void admitStudents(String collegeId, int hoursAlive, boolean initial) {
        int openBeds = buildingMgr.getOpenBeds(collegeId);
        int openPlates = buildingMgr.getOpenPlates(collegeId);
        int openDesks = buildingMgr.getOpenDesks(collegeId);
        int numNewStudents;
        List<StudentModel> students = dao.getStudents(collegeId);

        Date currDate = CollegeManager.getCollegeDate(collegeId);
        String date = currDate.toString();

        // Are we fully booked?
        if (openBeds <= 0 || openPlates <= 0 || openDesks <= 0) {
            return;
        }

        if(initial) {

            numNewStudents = 150;

            createStudents(numNewStudents, collegeId, students, initial);

            college = collegeDao.getCollege(collegeId);
            college.setNumberStudentsAdmitted(college.getNumberStudentsAdmitted() + numNewStudents);
            collegeDao.saveCollege(college);

            if (numNewStudents > 0) {
                NewsManager.createNews(collegeId, hoursAlive, Integer.toString(numNewStudents) + " students joined the college.", NewsType.COLLEGE_NEWS, NewsLevel.GOOD_NEWS);
            }
        }
        // For testing purposes, accepting students on the 1st of every month.
        //else if((CollegeManager.getCollegeCurrentMonth(collegeId) == 9 && CollegeManager.getCollegeCurrentDay(collegeId) == 1) || (CollegeManager.getCollegeCurrentMonth(collegeId) == 1 && CollegeManager.getCollegeCurrentDay(collegeId) == 1)){
        else if(CollegeManager.getCollegeCurrentDay(collegeId) == 1){

            int leastOpenings = Math.min(openBeds, openDesks);
            leastOpenings = Math.min(leastOpenings, openPlates);

            numNewStudents = college.getNumberStudentsAccepted();
            if (numNewStudents > leastOpenings) {
                numNewStudents = leastOpenings;
            }

            createStudents(numNewStudents, collegeId, students, false);

            college = collegeDao.getCollege(collegeId);
            college.setNumberStudentsAdmitted(college.getNumberStudentsAdmitted() + numNewStudents);
            college.setNumberStudentsAccepted(0);
            collegeDao.saveCollege(college);

            if (numNewStudents > 0) {
                NewsManager.createNews(collegeId, hoursAlive, Integer.toString(numNewStudents) + " students joined the college.", NewsType.COLLEGE_NEWS, NewsLevel.GOOD_NEWS);
            }
        }
    }

    private void acceptStudents(String collegeId, int hoursAlive){
        int numNewStudents;

        numNewStudents = rand.nextInt(10);

        college = collegeDao.getCollege(collegeId);
        college.setNumberStudentsAccepted(college.getNumberStudentsAccepted() + numNewStudents);
        collegeDao.saveCollege(college);

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
            students.add(student);
            dao.saveAllStudents(collegeId, students);
        }
    }

    /**
     * Withdraw students from the college.
     *
     * @param collegeId
     * @param hoursAlive
     */
    private void withdrawStudents(String collegeId, int hoursAlive) {
        float scalingFactor = .0001f;
        List<StudentModel> students = dao.getStudents(collegeId);
        int currentSize = students.size();

        // scroll through students list and remove student based upon a probability determined by their happiness level
        // the lower the happiness the greater the chance they have of withdrawing
        int studentsWithdrawn = 0;

        for (int i = 0; i < students.size(); i++){
            int h = students.get(i).getHappinessLevel();
            float odds = (100f - h) * scalingFactor;
            if (didItHappen(odds)) {
                buildingMgr.removeStudent(collegeId, students.get(i).getDorm(), students.get(i).getDiningHall(), students.get(i).getAcademicBuilding());
                students.remove(i);
                studentsWithdrawn++;
            }
        }

        dao.saveAllStudents(collegeId, students);

        college = collegeDao.getCollege(collegeId);
        college.setNumberStudentsWithdrew(college.getNumberStudentsWithdrew() + studentsWithdrawn);
        collegeDao.saveCollege(college);

        // Don't create a news story if no students leave
        if ((currentSize - students.size()) > 0) {
            NewsManager.createNews(collegeId, hoursAlive, Integer.toString(currentSize - students.size()) + " students withdrew from college.", NewsType.COLLEGE_NEWS, NewsLevel.BAD_NEWS);
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
    public void calculateStatistics(String collegeId) {
        calculaterOverallStudentHealth(collegeId);
        calculateStudentFacultyRatio(collegeId);
        calculateStudentFacultyRatioRating(collegeId);

        setHappinessForEachStudent(collegeId);

        calculateOverallStudentHappiness(collegeId);

        calculateRetentionRate(collegeId);
    }

    private void calculaterOverallStudentHealth(String collegeId) {
        CollegeModel college = collegeDao.getCollege(collegeId);
        List<StudentModel> students = dao.getStudents(collegeId);

        int nSick = 0;
        for(int i = 0; i < students.size(); i++){
            if(students.get(i).getNumberHoursLeftBeingSick() > 0){
                nSick++;
            }
        }

        int percentageWell = 100 - (nSick * 100) / Math.max(students.size(), 1);
        int rating = SimulatorUtilities.getRatingZeroToOneHundred(50, 95, percentageWell);
        college.setStudentHealthRating(rating);

        collegeDao.saveCollege(college);
    }

    private void calculateOverallStudentHappiness(String collegeId) {
        CollegeModel college = collegeDao.getCollege(collegeId);
        List<StudentModel> students = dao.getStudents(collegeId);

        int happinessSum = 0;
        for (int i = 0; i < students.size(); i++) {
            happinessSum += students.get(i).getHappinessLevel();
        }

        int aveHappiness = happinessSum/Math.max(1,college.getNumberStudentsAdmitted());

        college.setStudentBodyHappiness(aveHappiness);
        collegeDao.saveCollege(college);
    }

    private void setHappinessForEachStudent(String collegeId){
        List<StudentModel> students = dao.getStudents(collegeId);
        CollegeModel college = collegeDao.getCollege(collegeId);

       for(int i = 0; i < students.size(); i++){
           StudentModel student = students.get(i);
           setStudentHealthHappiness(student);
           setStudentAcademicHappiness(student, college);
           setStudentMoneyHappiness(student, college);
           setStudentFunHappiness(student, college);

           // The overall student happiness is an average of the above.

            int happiness = (student.getAcademicHappinessRating() + student.getFunHappinessRating() +
                             student.getHealthHappinessRating() + student.getMoneyHappinessRating()) / 4;
            happiness = Math.min(happiness, 100);
            happiness = Math.max(happiness, 0);
            students.get(i).setHappinessLevel(happiness);
        }

        dao.saveAllStudents(collegeId, students);
    }

    private void setStudentFunHappiness(StudentModel s, CollegeModel college) {
        // TODO: how do we decide if students are having fun?
        s.setFunHappinessRating(50);
    }

    private void setStudentMoneyHappiness(StudentModel s, CollegeModel college) {
        int rating = college.getYearlyTuitionRating(); // This is rating 0 to 100
        s.setMoneyHappinessRating(SimulatorUtilities.getRandomNumberWithNormalDistribution(rating, 15, 0, 100));
    }

    private void setStudentAcademicHappiness(StudentModel s, CollegeModel college) {
        int rating = college.getStudentFacultyRatioRating(); // This is rating 0 to 100
        s.setAcademicHappinessRating(SimulatorUtilities.getRandomNumberWithNormalDistribution(rating, 15, 0, 100));
    }

    private void setStudentHealthHappiness(StudentModel s) {
        s.setHealthHappinessRating(100);
        if (s.getNumberHoursLeftBeingSick() > 0){
            s.setHealthHappinessRating(0);
        }
    }

    private void calculateStudentFacultyRatio(String collegeId) {
        CollegeModel college = collegeDao.getCollege(collegeId);
        List<StudentModel> students = dao.getStudents(college.getRunId());
        List<FacultyModel> faculty = facultyDao.getFaculty(college.getRunId());

        college.setStudentFacultyRatio(students.size() / Math.max(faculty.size(),1));

        collegeDao.saveCollege(college);
    }

    private void calculateStudentFacultyRatioRating(String collegeId) {
        CollegeModel college = collegeDao.getCollege(collegeId);
        int rating = SimulatorUtilities.getRatingZeroToOneHundred(20, 5, college.getStudentFacultyRatio());
        college.setStudentFacultyRatioRating(rating);
        collegeDao.saveCollege(college);
    }

    /**
     * Calculate retention rate of students.  This is based on how many students
     * withdrew compare to students that remained.
     *
     * @param collegeId
     */
    private void calculateRetentionRate(String collegeId) {
        college = collegeDao.getCollege(collegeId);
        int retentionRate = 100;
        if (college.getNumberStudentsAdmitted() > 0) {
            retentionRate =
                    Math.max(((college.getNumberStudentsAdmitted() - college.getNumberStudentsWithdrew()) * 100)/
                            college.getNumberStudentsAdmitted(), 0);
        }
        college.setRetentionRate(retentionRate);

        collegeDao.saveCollege(college);
    }

    private boolean didItHappen(float oddsBetween0And1) {
        return (Math.random() < oddsBetween0And1);
    }

    private void loadTips(String collegeId) {
        // TODO: need to fill this out.
        TutorialManager.saveNewTip(collegeId, 1,"viewStudent", "This is a student tip.", true);
        TutorialManager.saveNewTip(collegeId, 2,"viewStudent", "Keep students happy to keep them in school.", false);
    }
}

