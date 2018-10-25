package com.endicott.edu.datalayer;

import com.endicott.edu.models.StudentModel;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;


// Created by Connor Frazier on 9/28/2017.

public class StudentDao {
    private static String getFilePath(String runId) {
        return DaoUtils.getFilePathPrefix(runId) +  "student.dat";
    }
    private Logger logger = Logger.getLogger("StudentDao");

    public static List<StudentModel> getStudents(String runId) {
        ArrayList<StudentModel> students = new ArrayList<>();
        StudentModel studentModel = null;
        try {
            File file = new File(getFilePath(runId));

            if (!file.exists()) {
                return students;  // There are no students yet.
            }
            else{
                FileInputStream fis = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(fis);
                students = (ArrayList<StudentModel>) ois.readObject();
                ois.close();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        Collections.sort(students, (o1, o2) -> o2.getHappinessLevel() - o1.getHappinessLevel());

        return students;
    }


    public static StudentModel[] getStudentsArray(String collegeId) {
        List<StudentModel> students = getStudents(collegeId);
        return students.toArray(new StudentModel[students.size()]);
    }

    public List<StudentModel> getStudentsOnSport(String runId, String teamName) {
        List<StudentModel> students = new ArrayList<>();
        ArrayList<StudentModel> playerList = new ArrayList<>();
        students = getStudents(runId);
        for( StudentModel student : students){
            if(student.getTeam().equals(teamName)){
                playerList.add(student);
            }
        }
        return playerList;
    }

    public void saveAllStudents(String runId, List<StudentModel> students){
        logger.info("Saving all students...");
        try {
            File file = new File(getFilePath(runId));
            file.createNewFile();
            FileOutputStream fos;
            fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(students);
            oos.close();
        } catch (FileNotFoundException e) {
            logger.info("Got file not found when attempting to create: " + getFilePath(runId));
            e.printStackTrace();
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        } catch (IOException e) {
            logger.info("Got io exceptionfound when attempting to create: " + getFilePath(runId));
            e.printStackTrace();
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }

        logger.info("Saved students...");
    }

    public void saveNewStudent(String runId, StudentModel student) {
        logger.info("Saving new student...");
        List<StudentModel> students = getStudents(runId);
        student.setRunId(runId);
        students.add(student);
        saveAllStudents(runId, students);
    }

    public static void deleteStudents(String runId) {
        File file = new File(getFilePath(runId));
        file.delete();
    }

    public static void main(String[] args) {
        testNotes();
    }

    private static void testNotes() {
//        final String runId = "teststudent001";
//        StudentDao dao = new StudentDao();
//        StudentModel s1 = new StudentModel("name",337714, 80, true, 7, "Standish", "Male", runId, 0,0,0, 0);
//        StudentModel s2 = new StudentModel("name", 555555, 75, true, 9, "Winthrop", "Male", runId, 0,0,0,0);
//        ArrayList<StudentModel> students = new ArrayList<>();
//        students.add(s1);
//        students.add(s2);
//        dao.saveAllStudents(runId, students);
//
//        List<StudentModel> outMsgs = dao.getStudents(runId);
//
//        assert(outMsgs.size() == 2);
//        //assert(outMsgs.get(1).getCapacity() == 100);
//
//        StudentModel s3 = new StudentModel("name", 010101, 50, false, 0, "Marblehead", "Male", runId, 0, 0,0,0);
//        dao.saveNewStudent(runId, s3);
//        outMsgs = dao.getStudents(runId);
//        assert(outMsgs.size() == 3);
//
//        System.out.println("Test case name: testStudents, Result: pass");
    }
}
