package com.endicott.edu.datalayer;

import com.endicott.edu.models.CollegeModel;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.io.*;
import java.util.ArrayList;

// Created by abrocken on 7/17/2017.

public class CollegeDao {
    private static String getFilePath(String collegeId) {
        return DaoUtils.getFilePathPrefix(collegeId) +  "college.dat";
    }

    public CollegeModel getCollege(String collegeId) {
        CollegeModel college = null;
        try {
            college = new CollegeModel();
            college.setRunId(collegeId);

            File file = new File(getFilePath(collegeId));

            if (!file.exists()) {
                throw new WebApplicationException(Response.Status.NOT_FOUND);
            }
            else{
                FileInputStream fis = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(fis);
                college = (CollegeModel) ois.readObject();
                ois.close();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return college;
    }

    public static void deleteCollege(String collegeId) {
        File file = new File(getFilePath(collegeId));
        boolean result = file.delete();

        DormitoryDao.deleteDorm(collegeId);
        FacultyDao.removeAllFaculty(collegeId);
        FloodDao.deleteFloods(collegeId);
        NewsFeedDao.deleteNotes(collegeId);
        PlagueDao.deletePlagues(collegeId);
        SportsDao.deleteSports(collegeId);
        StudentDao.deleteStudents(collegeId);
    }

    public void saveCollege(CollegeModel college){
        try {
            college.setNote(getFilePath(college.getRunId()));
            File file = new File(getFilePath(college.getRunId()));
            file.createNewFile();
            FileOutputStream fos;
            fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(college);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static CollegeModel[] getColleges() {
        ArrayList<CollegeModel> list = new ArrayList<>(); //list to contain all the colleges
        CollegeDao collegeDao = new CollegeDao();
        String collegeDir = System.getenv("SystemDrive")+ File.separator +"collegesim"; //we must know where they are stored
        File folder = new File(collegeDir);
        File[] listOfFiles = folder.listFiles();//list of all files in directory

        for(int i = 0; i < listOfFiles.length; i++){
            //if the name of the file ends in college.dat we know it contains a college object
            if(listOfFiles[i].getName().endsWith("college.dat")){
                //get the name and chop off the file ending to get the collegeId
                String tmp = listOfFiles[i].getName();
                tmp = tmp.substring(0,tmp.length() - 11);
                list.add(collegeDao.getCollege(tmp)); //grab the college with the collegeId we just discovered and add it to the list
            }
        }

        return list.toArray(new CollegeModel[list.size()]);
    }

    public static void main(String[] args) {
        testCollegeDao();
    }

    private static void testCollegeDao() {
        String collegeId = "000";
        CollegeDao dao = new CollegeDao();
        CollegeModel college = new CollegeModel();
        college.setAvailableCash(100);
        college.setRunId(collegeId);
        dao.saveCollege(college);

        college = dao.getCollege(collegeId);
        assert(college.getAvailableCash() == 100);
        dao.deleteCollege(collegeId);
    }

}
