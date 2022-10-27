package com.endicott.edu.datalayer;

import com.endicott.edu.models.Student;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.io.*;
import java.util.*;
import java.util.logging.Logger;


// Created by Connor Frazier on 9/28/2017.

public class StudentDao {
    private static String getFilePath(String runId) {
        return DaoUtils.getFilePathPrefix(runId) +  "student.dat";
    }
    private static Logger logger = Logger.getLogger("StudentDao");
    private static HashMap<String, List<Student>> cache = new HashMap<>();

    public static List<Student> getStudents(String runId) {
        if (cache.containsKey(runId))
            return cache.get(runId);

        logger.info("Reading students from disk.");
        ArrayList<Student> students = new ArrayList<>();
        Student studentModel = null;
        try {
            File file = new File(getFilePath(runId));

            if (!file.exists()) {
                return students;  // There are no students yet.
            }
            else{
                FileInputStream fis = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(fis);
                students = (ArrayList<Student>) ois.readObject();
                ois.close();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        Collections.sort(students, (o1, o2) -> o2.getHappiness() - o1.getHappiness());
        cache.put(runId,students);

        return students;
    }


    public static Student[] getStudentsArray(String collegeId) {
        List<Student> students = getStudents(collegeId);
        return students.toArray(new Student[students.size()]);
    }

    public static int getNumberOfStudents(String collegeId) {
        List<Student> students = getStudents(collegeId);
        if (students == null)
            return 0;
        return students.size();
    }

    public static List<Student> getStudentsOnSport(String runId, String teamName) {
        List<Student> students = new ArrayList<>();
        ArrayList<Student> playerList = new ArrayList<>();
        students = getStudents(runId);
        for( Student student : students){
            if(student.getTeam().equals(teamName)){
                playerList.add(student);
            }
        }
        return playerList;
    }

    public static void saveAllStudentsJustToCache(String runId, List<Student> students){
         cache.put(runId,students);  // We need to update the cache so that next we get the up to date college.
    }

    public static void saveAllStudentsUsingCache(String runId) {
        List<Student> students;

        if (!cache.containsKey(runId))
            return;

        students = cache.get(runId);
        saveAllStudents(runId, students);
    }

    public static void saveAllStudents(String runId, List<Student> students){
        logger.info("Saving all students to disk.");
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
        cache.put(runId,students);  // We need to update the cache so that next we get the up to date college.
    }

    public void saveNewStudent(String runId, Student student) {
        List<Student> students = getStudents(runId);
        student.setRunId(runId);
        students.add(student);
        saveAllStudents(runId, students);
    }

    public static void deleteStudents(String runId) {
        File file = new File(getFilePath(runId));
        file.delete();

        if (cache.containsKey(runId)){
            cache.remove(runId);
        }
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
