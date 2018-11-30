package com.endicott.edu.simulators;

import com.endicott.edu.datalayer.PlagueDao;
import com.endicott.edu.datalayer.StudentDao;
import com.endicott.edu.models.*;

import java.util.List;
import java.util.Random;

/**
 * Responsible for simulating plagues at the college.
 */
public class PlagueManager {
    PlagueDao dao = new PlagueDao();
    private double plagueProbablity = .02;
    private boolean purellUpgradePurchased = false;
    private InventoryManager inventoryManager = new InventoryManager();
    private String purellUpgradeName = "Purell Dispensers";
    private int plagueMutationProb = 15;
    private boolean didPlagueMutate = false;


    /**
     * Simulate changes in the plague due to passage of time.
     * NOTE: THERE IS ONLY ONE PLAGUE AT A TIME.
     *
     * @param collegeId
     * @param hoursAlive  number of hours since college was created
     */
    public void handleTimeChange(String collegeId, int hoursAlive, PopupEventManager popupManager) {
        checkForPurellUpgrade(collegeId);
        List<PlagueModel> plagues = dao.getPlagues(collegeId);

        // First work on the overall health of students.
        makeStudentsBetter(collegeId, hoursAlive);
        randomlyMakeSomeStudentsSicker(collegeId, hoursAlive);

        boolean quarantine = false;
        boolean mutation = false;

        int hoursLeftInPlague = 0;

        // Reduce the time left in any active plague.
        boolean didPlagueEnd = false;
        for (PlagueModel plague : plagues) {
            int timePassed = hoursAlive - plague.getHourLastUpdated();
            int oldHoursLeftInPlague = plague.getNumberOfHoursLeftInPlague();
            plague.setHourLastUpdated(hoursAlive);
            hoursLeftInPlague = Math.max(0,oldHoursLeftInPlague - timePassed);
            plague.setNumberOfHoursLeftInPlague(hoursLeftInPlague);
            didPlagueEnd = (oldHoursLeftInPlague > 0 && hoursLeftInPlague <= 0);
            quarantine = plague.isQuarantine();

        }

        if (didPlagueEnd) {
            popupManager.newPopupEvent("Plague Ends",
                    "It seems like the plague is ending.",
                    "Ok", "plagueAckCallback1", "resources/images/plague.jpg", "Plague Doctor");
        }
        // Spread the plague
        if (hoursLeftInPlague > 0) {
            int oldNumberSick = getNumberSick(collegeId);
            plagueSpreadsThroughStudents(collegeId, hoursAlive, hoursLeftInPlague, getNumberSick(collegeId), quarantine);
            //TODO: Not sure how to go about accessing current plague here
//            mutation = plague.isMutation();
            int newNumberSick = getNumberSick(collegeId);
            int newVictims = newNumberSick - oldNumberSick;
            if (newVictims > 15) {
                if (!isPurellUpgradePurchased()) {
                    popupManager.newPopupEvent("Plague Spreads!",
                            "And the plague continues. " + newVictims +
                                    " more students are infected.  There are now " + newNumberSick + " ill.\n" +
                                    "Purchase a Purell dispensers from the store to reduce the risk of plagues on campus.",
                            "Ok", "plagueAckCallback2", "Visit Store", "goToStore",
                            "resources/images/plague.jpg", "Plague Doctor");

                }
                else{
                    popupManager.newPopupEvent("Plague Spreads!",
                            "And the plague continues. " + newVictims +
                                    " more students are infected.  There are now " + newNumberSick + " ill.\n",
                            "Ok", "plagueAckCallback2", "resources/images/plague.jpg", "Plague Doctor");
                }
            }else if(mutation){
                handlePlagueMutation(popupManager);
            }
        }

        // or possibly start a new plague

        else
        {
            if (DisasterManager.isEventPermitted(collegeId) && Math.random() <= plagueProbablity || CollegeManager.isMode(collegeId, CollegeMode.DEMO_PLAGUE)) {
                startNewPlague(plagues);
                DisasterManager.newEventStart(collegeId);
                popupManager.newPopupEvent("Plague!",
                        "An illness is starting to starting to sweep through the campus. What would you like to do?",
                        "Quarantine the sick students ($5,000)", "quarantineStudents", "Do Nothing", "plagueCallback4",
                        "resources/images/plague.jpg", "Plague Doctor");
            }
        }

        dao.saveAllPlagues(collegeId, plagues);
    }

    /**
     * Start a plague
     *
     * 
     * @param plagues
     */
    private void startNewPlague(List<PlagueModel> plagues) {
        Random rand = new Random();
        int plagueLengthInHours = rand.nextInt(72) + 72;

        for (PlagueModel plague : plagues) {
            plague.setNumberOfHoursLeftInPlague(plagueLengthInHours);
            plague.setQuarantine(false);
            plague.setMutation(false);
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
        InventoryManager.createItem("Hand Sanitizers", false, "handsanitizer.png", 10000, 0, "Helps stop the spread of disease. Who knows what weird disease might develop otherwise?", collegeId);
//        plagueSpreadsThroughStudents(collegeId, 0, hoursInPlague, 0);
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
    private static int plagueSpreadsThroughStudents(String collegeId, int currentHour, int hoursLeftInPlague, int studentSickCount, boolean quarantine) {
        StudentDao dao = new StudentDao();
        List<StudentModel> students = dao.getStudents(collegeId);
        int nSick = 0;
        String someoneSick = "";

        if (students.size() <= 0) {
            return nSick;
        }
        int probCatchesFromOthers;
        Random rand = new Random();

        if(quarantine){
            int plagueMutationProb = rand.nextInt(100);
            if(plagueMutationProb >= 15)
                return nSick;
            else{
                PlagueDao plagueDao = new PlagueDao();
                List<PlagueModel> plagues = plagueDao.getPlagues(collegeId);
                for (PlagueModel plague : plagues) {
                    plague.setMutation(true);
                }

                return nSick;
            }

        }else{

            probCatchesFromOthers = (studentSickCount * 100) / students.size();
            int probCatchesFromOutside = 10; // out of 100
            int totalProb = Math.min(100, probCatchesFromOthers + probCatchesFromOutside);
            for (int i = 0; i < students.size(); i++) {
                StudentModel student = students.get(i);
                if (student.getNumberHoursLeftBeingSick() <= 0 && rand.nextInt(100) <= totalProb) {
                    student.setNumberHoursLeftBeingSick(hoursLeftInPlague);
                    nSick++;
                    someoneSick = student.getName();
                } else {
                    student.setNumberHoursLeftBeingSick(0);
                }
            }

            if (nSick == 1) {
                NewsManager.createNews(collegeId, currentHour, "Student " + someoneSick + " has fallen ill.", NewsType.COLLEGE_NEWS, NewsLevel.BAD_NEWS);
            } else if (nSick > 1) {
                NewsManager.createNews(collegeId, currentHour, "Student " + someoneSick + " and " + (nSick - 1) + " others have fallen ill.", NewsType.COLLEGE_NEWS, NewsLevel.BAD_NEWS);
            }

            dao.saveAllStudents(collegeId, students);

        }

        return nSick;


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
    private void handlePlagueMutation(PopupEventManager popupMan){

    }
    /**
     * For those students that are sick, reduce the time left in there illness.
     *
     * @param collegeId
     * @param hoursAlive
     */
    private int makeStudentsBetter(String collegeId, int hoursAlive) {
        StudentDao dao = new StudentDao();
        List<StudentModel> students = dao.getStudents(collegeId);
        int nSick = 0;

        for(int i = 0; i < students.size(); i++){
            StudentModel student = students.get(i);
            if(students.get(i).getNumberHoursLeftBeingSick() > 0){
                int studentLastUpdated = students.get(i).getHourLastUpdated();
                int timeChange = hoursAlive - studentLastUpdated;
                int sickTime = students.get(i).getNumberHoursLeftBeingSick() - timeChange;
                sickTime = Math.max(0,sickTime);
                students.get(i).setNumberHoursLeftBeingSick(sickTime);
                if (sickTime > 0)
                    nSick++;
            }
        }

        dao.saveAllStudents(collegeId, students);
        return nSick;
    }
    //Checks to see if the Purell Upgrade is puchased, and if so lowers the plague probability to .01
    public void checkForPurellUpgrade(String runId){
        if (inventoryManager.isPurchased(purellUpgradeName,runId)){
            this.purellUpgradePurchased = true;
            this.plagueProbablity = .01;
        }
    }
    public boolean isPurellUpgradePurchased() {
        return purellUpgradePurchased;
    }

    public boolean isPlagueMuated() { return didPlagueMutate; }



    public boolean isEventActive(String collegeId) {
        List<PlagueModel> plagues = dao.getPlagues(collegeId);
        for (PlagueModel plague : plagues) {
            if (plague.getNumberOfHoursLeftInPlague() > 0)
                return true;
        }
        return false;
    }
}
