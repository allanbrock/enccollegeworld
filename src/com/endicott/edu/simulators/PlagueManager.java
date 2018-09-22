package com.endicott.edu.simulators;

import com.endicott.edu.datalayer.PlagueDao;
import com.endicott.edu.datalayer.StudentDao;
import com.endicott.edu.models.NewsLevel;
import com.endicott.edu.models.NewsType;
import com.endicott.edu.models.PlagueModel;
import com.endicott.edu.models.StudentModel;

import java.util.List;
import java.util.Random;

/**
 * Responsible for simulating plagues at the college.
 */
public class PlagueManager {
    PlagueDao dao = new PlagueDao();

    /**
     * Simulate changes in the plague due to passage of time.
     * NOTE: THERE IS ONLY ONE PLAGUE AT A TIME.
     *
     * @param collegeId
     * @param hoursAlive  number of hours since college was created
     */
    public void handleTimeChange(String collegeId, int hoursAlive) {
        List<PlagueModel> plagues = dao.getPlagues(collegeId);

        int hoursLeftInPlague = 0;

        // Reduce the time left in any active plague.
        for (PlagueModel plague : plagues) {
            int timePassed = hoursAlive - plague.getHourLastUpdated();
            plague.setHourLastUpdated(hoursAlive);
            hoursLeftInPlague = Math.max(0,plague.getNumberOfHoursLeftInPlague() - timePassed);
            plague.setNumberOfHoursLeftInPlague(hoursLeftInPlague);
        }

        // Make any sick students improve.
        makeStudentsBetter(collegeId, hoursAlive);
        randomlyMakeSomeStudentsSicker(collegeId, hoursAlive);

        // Spread the plague
        if (hoursLeftInPlague > 0) {
            plagueSpreadsThroughStudents(collegeId, hoursAlive, hoursLeftInPlague, getNumberSick(collegeId));
        }

        // or possibly start a new plague
        else
        {
            if (Math.random() <= 0.2) {
                refreshPlague(plagues);
            }
        }

        dao.saveAllPlagues(collegeId, plagues);
    }

    /**
     * Start a plague
     *
     * @param plagues
     */
    private void refreshPlague(List<PlagueModel> plagues) {
        Random rand = new Random();
        int plagueLengthInHours = rand.nextInt(72) + 72;

        for (PlagueModel plague : plagues) {
            plague.setNumberOfHoursLeftInPlague(plagueLengthInHours);
        }
    }

    /**
     * Return the number of students that are sick at the college.
     *
     * @param collegeId
     * @return
     */
    private int getNumberSick(String collegeId) {
        StudentDao dao = new StudentDao();
        List<StudentModel> students = dao.getStudents(collegeId);
        int studentSickCount = 0;
        for(int i = 0; i < students.size(); i++){
            StudentModel student = students.get(i);
            if(students.get(i).getNumberHoursLeftBeingSick() > 0){
                studentSickCount++;
            }
        }

        return studentSickCount;
    }

    /**
     * Take care of any plague business when the college is first created.
     * @param collegeId
     */
    static public void establishCollege(String collegeId){
        int hoursInPlague = createInitialPlague(collegeId);
        plagueSpreadsThroughStudents(collegeId, 0, hoursInPlague, 0);
    }

    /**
     * Create the initial plague.  This initial plague keeps track of time left in
     * plague and is randomly refreshed to start new plagues.
     *
     * @param collegeId
     * @return
     */
    static private int createInitialPlague(String collegeId) {
        Random rand = new Random();
        int plagueLengthInHours = 0;  // We are just setting up the structure.  No length to this plague.
        PlagueModel plague = new PlagueModel( 0, 0, "Hampshire Hall",
                "none", 0, 0, 1000, plagueLengthInHours, 0);
        PlagueDao plagueDao = new PlagueDao();
        plagueDao.saveNewPlague(collegeId, plague);
        return plagueLengthInHours;
    }

    /**
     * Make more students sick at the college.
     *
     * @param collegeId
     * @param currentHour
     * @param hoursLeftInPlague
     * @param studentSickCount  number of students currently sick.
     */
    private static void plagueSpreadsThroughStudents(String collegeId, int currentHour, int hoursLeftInPlague, int studentSickCount) {
        StudentDao dao = new StudentDao();
        List<StudentModel> students = dao.getStudents(collegeId);
        int nSick = 0;
        String someoneSick = "";

        if (students.size() <= 0) {
            return;
        }

        int probCatchesFromOthers = (studentSickCount * 100)/students.size();
        int probCatchesFromOutside = 10; // out of 100
        int totalProb = Math.min(100,probCatchesFromOthers + probCatchesFromOutside);
        Random rand = new Random();

        for(int i = 0; i < students.size(); i++){
            StudentModel student = students.get(i);
             if(rand.nextInt(100) <= totalProb){
                student.setNumberHoursLeftBeingSick(hoursLeftInPlague);
                nSick++;
                someoneSick = student.getName();
            } else {
                student.setNumberHoursLeftBeingSick(0);
            }
        }

        if (nSick == 1) {
            NewsManager.createNews(collegeId,currentHour, "Student " + someoneSick + " has fallen ill.", NewsType.COLLEGE_NEWS, NewsLevel.BAD_NEWS);
        } else if (nSick > 1) {
            NewsManager.createNews(collegeId,currentHour, "Student " + someoneSick + " and " + (nSick-1) + " others have fallen ill.", NewsType.COLLEGE_NEWS, NewsLevel.BAD_NEWS);
        }

        dao.saveAllStudents(collegeId, students);
    }

    /**
     * Randomly extend the sick time of some studetns.
     *
     * @param collegeId
     * @param currentDay
     */
    private void randomlyMakeSomeStudentsSicker(String collegeId, int currentDay){
        StudentDao dao = new StudentDao();
        List<StudentModel> students = dao.getStudents(collegeId);

        Random rand = new Random();
        int x = rand.nextInt(1) + 6;

        for(int i = 0; i < students.size(); i++){
            StudentModel student = students.get(i);
            int sickTime = student.getNumberHoursLeftBeingSick();
            if(sickTime > 0) {
                if(x == 4){
                    student.setNumberHoursLeftBeingSick(sickTime + 24);
                    NewsManager.createNews(collegeId,currentDay, student.getName() + " is sicker", NewsType.COLLEGE_NEWS, NewsLevel.BAD_NEWS);

                }
            }
        }
        dao.saveAllStudents(collegeId, students);
    }

    /**
     * For those students that are sick, reduce the time left in there illness.
     *
     * @param collegeId
     * @param hoursAlive
     */
    private void makeStudentsBetter(String collegeId, int hoursAlive) {
        StudentDao dao = new StudentDao();
        List<StudentModel> students = dao.getStudents(collegeId);
        for(int i = 0; i < students.size(); i++){
            StudentModel student = students.get(i);
            if(students.get(i).getNumberHoursLeftBeingSick() > 0){
                int studentLastUpdated = students.get(i).getHourLastUpdated();
                int timeChange = hoursAlive - studentLastUpdated;
                int sickTime = students.get(i).getNumberHoursLeftBeingSick() - timeChange;
                sickTime = Math.max(0,sickTime);
                students.get(i).setNumberHoursLeftBeingSick(sickTime);
            }
        }

        dao.saveAllStudents(collegeId, students);
    }
}