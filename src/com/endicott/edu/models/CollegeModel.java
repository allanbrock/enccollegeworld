package com.endicott.edu.models;

import com.endicott.edu.simulators.CollegeManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class CollegeModel implements Serializable {
    private int hoursAlive = 0;                  //Hours the college has existed for
    private int timeLeftInSemester = 0;
    private int numSemesters = 0;
    public static final int daysAdvance = 7; // 1 week
    private final String timeAdvanceBy = "Week"; // define based on daysAdvance
    private int reputation = 50;                 //Reputation of college based on 1-100
    private int yearlyTuitionCost = 40000;       //Amount of money a student pays per year
    private int previousTuitionCost = 0;         //The most recent previous tuition change
    private int availableCash = 20000;           //The balance of the college
    private int totalIncome = 0;                 //The amount of money that the college earned over the last period of time
    private int totalExpenditure = 0;            //The amount of money that the college lost/spent over the last period of time
    private int debt = 0;                        //the amount of debt the college is currently in
    private int credit = 580;                    //The current credit score of the college (300-850 is the range)
    private String runId = "unknown";            //Name of the college
    private String note = "empty";               //Note for debugging
    private int studentBodyHappiness;            //Happiness of all students (0-100)
    private int facultyBodyHappiness;            //Happiness of all faculty (0-100)
    private int studentFacultyRatio = 100;       //Number of students per faculty member
    private int numberStudentsAdmitted = 0;      //Total number of students admitted since college created.
    private int numberStudentsAccepted = 0;      //Total number of students accepted to the school (considering).
    private int numberStudentsWithdrew = 0;      //Total number of students withdrawn since college created.
    private int numberStudentsGraduated = 0;     //Total number of students graduate since college created
    private float retentionRate = 100f;          //Percentage of students retained (or graduated) since college created
    private int yearlyTuitionRating = 0;         //0 to 100 rating of happiness corresponding to tuition.
    private int studentFacultyRatioRating = 0;   //0 to 100 rating of student faculty ratio
    private int studentHealthRating = 0;            //0 to 100 rating of student health
    private int studentHealthHappiness = 0;         //0 to 100 rating of student health
    private int studentRecreationalHappiness = 0;   //0 to 100 rating of student recreational happiness
    private int studentFinancialHappiness = 0;      //0 to 100 rating of student financial happiness
    private int studentBuildingHappiness = 0;       //0 to 100 rating of student building happiness
    private int studentProfessorHappiness = 0;      //0 to 100 rating of student professor happiness
    private int totalBuildingHealth = 0;            //0 to 100 average of all buildings health
    private int studentAcademicHappiness = 0;       //0 to 100 rating of student academic happiness
    private int departmentCount = 4;                    //Number of departments/schools in the college
    private int gate = 0;                               //This is the current gate or level that is open.
    private CollegeMode mode = CollegeMode.PLAY;        //Current mode of the game, used for play testing
    private int daysUntilNextEvent = 3;                 //Number of days until the next event
    private boolean isTimePaused = true;                //Boolean for if the game is paused or not
    private TipsModel collegeTips = new TipsModel();    //Holds all the tips that the user should know about to improve trait ratings
    //public PopupEventModel;
    private FinancesModel financialGraph = new FinancesModel();
    private ExpensesModel expensesGraph = new ExpensesModel();

    //Loan Fields
    private ArrayList<LoanModel> loans = new ArrayList<>();  //Holds all of the loans the player currently has
    private LoanModel proposedLoan = new LoanModel(0, 0, 0);    //Loan that is in progress of being accepted

    //Counts amount of total championships won to be tracked in the trophy case
    private int footballChampionships = 0;
    private int mHockeyChampionships = 0;
    private int wHockeyChampionships = 0;
    private int mSoccerChampionships = 0;
    private int wSoccerChampionships = 0;
    private int baseballChampionships = 0;
    private int softballChampionships = 0;
    private int mBasketballChampionships = 0;
    private int wBasketballChampionships = 0;

    // School Traits:
    private int academicRating = 0;
    private int athleticRating = 0;
    private int socialRating = 0;
    private int infrastructureRating = 0;
    private int safetyRating = 0;
    private int schoolValue = 0;


    private Date currentDate;

    public int getTimeLeftInSemester() { return this.timeLeftInSemester; }
    public void advanceTime() {
        this.timeLeftInSemester -= 1;
        // if semester has no weeks left, set new semester
        if(this.timeLeftInSemester == 0){
            setNewSemester();
        }
    }
    public void setNewSemester(){
        // reset time left in semester and increase number of semesters
        this.timeLeftInSemester = 105 / CollegeModel.daysAdvance;
        this.numSemesters += 1;
    }

    public int getFacultyBodyHappiness() {return this.facultyBodyHappiness;}
    public void setFacultyBodyHappiness(int n) {this.facultyBodyHappiness = n;}

    public Date getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(Date currentDate) {
        this.currentDate = currentDate;
    }

    public int getDepartmentCount(){ return departmentCount; }
    public void setDepartmentCount(int departmentCount){ this.departmentCount = departmentCount; }

    public int getStudentHealthRating() {
        return studentHealthRating;
    }

    public void setStudentHealthRating(int studentHealthRating) {
        this.studentHealthRating = studentHealthRating;
    }

    public int getStudentHealthHappiness() {
        return studentHealthHappiness;
    }

    public void setStudentHealthHappiness(int studentHealthHappiness) {
        this.studentHealthHappiness = studentHealthHappiness;
    }

    public int getDebt() {return this.debt;}

    public void setDebt(int d) {this.debt = d;}

    public int getCredit() {return this.credit;}

    public void setCredit(int c) { this.credit = c;}

    public void setStudentRecreationalHappiness(int studentRecreationalHappiness) {this.studentRecreationalHappiness = studentRecreationalHappiness;}

    public int getStudentRecreationalHappiness() {return studentRecreationalHappiness;}

    public void setStudentFinancialHappiness(int studentFinancialHappiness) { this.studentFinancialHappiness = studentFinancialHappiness;}

    public int getStudentFinancialHappiness() {return studentFinancialHappiness;}

    public void setStudentBuildingHappiness(int studentBuildingHappiness) { this.studentBuildingHappiness = studentBuildingHappiness;}

    public int getStudentBuildingHappiness() {return studentBuildingHappiness;}

    public void setStudentProfessorHappiness(int studentProfessorHappiness) { this.studentProfessorHappiness = studentProfessorHappiness;}

    public int getStudentProfessorHappiness() {return studentProfessorHappiness;}

    public int getStudentFacultyRatioRating() {
        return studentFacultyRatioRating;
    }

    public void setTotalBuildingHealth(int totalBuildingHealth) {this.totalBuildingHealth= totalBuildingHealth;}

    public int getTotalBuildingHealth() {return totalBuildingHealth;}

    public void setStudentFacultyRatioRating(int studentFacultyRatioRating) {
        this.studentFacultyRatioRating = studentFacultyRatioRating;
    }

    public int getYearlyTuitionRating() {
        return yearlyTuitionRating;
    }

    public void setYearlyTuitionRating(int yearlyTuitionRating) {
        this.yearlyTuitionRating = yearlyTuitionRating;
    }

    public int getReputation() { return reputation; }

    public void setReputation(int reputation) { this.reputation = reputation; }

    public int getYearlyTuitionCost() {
        return yearlyTuitionCost;
    }

    public void setYearlyTuitionCost(int yearlyTuitionCost) {
        this.yearlyTuitionCost = yearlyTuitionCost;
    }

    public int getPreviousTuitionCost() {return previousTuitionCost;}

    public void setPreviousTuitionCost(int cost) {this.previousTuitionCost = cost;}

    public int getAvailableCash() {
        return availableCash;
    }

    public void setAvailableCash(int availableCash) {
        this.availableCash = availableCash;
    }

    public String getRunId() { return runId; }

    public int getStudentFacultyRatio() { return studentFacultyRatio; }

    public void setRunId(String runId) {
        this.runId = runId;
    }

    public int getHoursAlive() {
        return hoursAlive;
    }

    public void setHoursAlive(int hoursAlive) {
        this.hoursAlive = hoursAlive;
        this.currentDate = CollegeManager .hoursToDate(getHoursAlive());
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void advanceClock(int hours) {
        setHoursAlive(getHoursAlive() + hours);
    }

    public int getCurrentDay() {
        return hoursAlive / 24 + 1;
    }

    public int getStudentBodyHappiness() { return studentBodyHappiness; }

    public void setStudentBodyHappiness(int studentBodyHappiness) { this.studentBodyHappiness = studentBodyHappiness; }

    public void setStudentFacultyRatio(int studentFacultyRatio) { this.studentFacultyRatio = studentFacultyRatio; }

    public int getNumberStudentsAdmitted() { return numberStudentsAdmitted; }

    public void setNumberStudentsAdmitted(int numberStudentsAdmitted) { this.numberStudentsAdmitted = numberStudentsAdmitted; }

    public int getNumberStudentsWithdrew() { return numberStudentsWithdrew; }

    public void setNumberStudentsWithdrew(int numberStudentsWithdrew) { this.numberStudentsWithdrew = numberStudentsWithdrew; }

    public int getNumberStudentsGraduated() { return numberStudentsGraduated; }

    public void setNumberStudentsGraduated(int numberStudentsGraduated) { this.numberStudentsGraduated = numberStudentsGraduated; }

    public float getRetentionRate() { return retentionRate; }

    public void setRetentionRate(float retentionRate) { this.retentionRate = retentionRate; }

    public int getNumberStudentsAccepted() { return numberStudentsAccepted; }

    public void setNumberStudentsAccepted(int numberStudentsAccepted) { this.numberStudentsAccepted = numberStudentsAccepted; }

    public boolean getIsTimePaused() { return isTimePaused; }

    public void setIsTimePaused(boolean condition) { this.isTimePaused = condition; }

    public CollegeMode getMode() {
        return mode;
    }

    public void setMode(CollegeMode mode) {
        this.mode = mode;
    }

    public TipsModel getTips() { return this.collegeTips; }

    public void setTips(TipsModel tm) { this.collegeTips = tm; }

    public  int getTotalIncome(){
        return totalIncome;
    }

    public void setTotalIncome(int totalIncome) {
        this.totalIncome = totalIncome;
    }

    public int getTotalExpenditure(){
        return totalExpenditure;
    }

    public void setTotalExpenditure(int totalExpenditure){
        this.totalExpenditure = totalExpenditure;
    }

    public ArrayList<LoanModel> getLoans() {return this.loans;}

    public void setLoans(ArrayList<LoanModel> l) {this.loans = l;}

    public LoanModel getProposedLoan() {return this.proposedLoan;}

    public void setProposedLoan(LoanModel lm) {this.proposedLoan = lm;}

    public void setMode(String mode) {
        //Testing the "PopupEventDao"
//        if (mode.equals("Demo")){
//            new PopupEventDao().newPopupEvent("DEMO", "DEMO","TEXT", "CallBack", "image", "TEXT" );
//        }

        this.mode = CollegeMode.PLAY;

        if (mode.equals("Play")) this.mode = CollegeMode.PLAY;
        else if (mode.equals("Demo")) this.mode = CollegeMode.DEMO;
        else if (mode.equals("Demo Fire")) this.mode = CollegeMode.DEMO_FIRE;
        else if (mode.equals("Demo Plague")) this.mode = CollegeMode.DEMO_PLAGUE;
        else if (mode.equals("Demo Riot")) this.mode = CollegeMode.DEMO_RIOT;
        else if (mode.equals("Demo Snow")) this.mode = CollegeMode.DEMO_SNOW;
        else if (mode.equals("Demo Flood")) this.mode = CollegeMode.DEMO_FLOOD;
        else if (mode.equals("Demo Championship")) this.mode = CollegeMode.DEMO_CHAMPIONSHIP;
        else if (mode.equals("Demo Plague Mutation")) this.mode = CollegeMode.DEMO_ZOMBIE_MUTATION;
    }

    //Getters for Championships Won
    public int getFootballChampionships() { return footballChampionships;}

    public int getMSoccerChampionships() { return mSoccerChampionships;}

    public int getWSoccerChampionships() { return wSoccerChampionships;}

    public int getMHockeyChampionships() { return mHockeyChampionships;}

    public int getWHockeyChampionships() { return wHockeyChampionships;}

    public int getMBasketballChampionships() { return mBasketballChampionships;}

    public int getWBasketballChampionships() { return wBasketballChampionships;}

    public int getBaseballChampionships() { return baseballChampionships;}

    public int getSoftballChampionships() { return softballChampionships;}

    public int getNumChampionshipsWon() {
        return footballChampionships + mHockeyChampionships + wHockeyChampionships +
                mSoccerChampionships + wSoccerChampionships + baseballChampionships +
                softballChampionships + mBasketballChampionships + wBasketballChampionships;
    }

    // Getters / Setters for School Traits:
    public int getAcademicRating() {return academicRating;}
    public void setAcademicRating(int academicRating) {this.academicRating = academicRating;}

    public int getAthleticRating() {return athleticRating;}
    public void setAthleticRating(int athleticRating) {this.athleticRating = athleticRating;}

    public int getSocialRating() {return socialRating;}
    public void setSocialRating(int socialRating) {this.socialRating = socialRating;}

    public int getInfrastructureRating() {return infrastructureRating;}
    public void setInfrastructureRating(int infrastructureRating) {this.infrastructureRating = infrastructureRating;}

    public int getSafetyRating() {return safetyRating;}
    public void setSafetyRating(int safetyRating) {this.safetyRating = safetyRating;}

    public int getSchoolValue() {return schoolValue;}
    public void setSchoolValue(int schoolValue) {this.schoolValue = schoolValue;}

    public int getStudentAcademicHappiness() { return studentAcademicHappiness; }

    public void setStudentAcademicHappiness(int studentAcademicHappiness) { this.studentAcademicHappiness = studentAcademicHappiness; }

    public int getGate() {
        return gate;
    }

    public void setGate(int gate) {
        this.gate = gate;
    }

    public int getDaysUntilNextEvent() {
        return daysUntilNextEvent;
    }

    public void setDaysUntilNextEvent(int daysUntilNextEvent) {
        this.daysUntilNextEvent = daysUntilNextEvent;
    }

    public FinancesModel getFinancialGraph(){
        return financialGraph;
    }

    public ExpensesModel getExpensesGraph() {
        return expensesGraph;
    }
}