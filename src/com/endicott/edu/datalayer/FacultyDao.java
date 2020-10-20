package com.endicott.edu.datalayer;

import com.endicott.edu.models.FacultyModel;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by Mazlin Higbee 10/02/17
 * mhigb411@mail.endicott.edu
 * This class is intended to handle to data access for faculty objects
 *
 */
public class FacultyDao {
    private static final String filename = "faculty.json";

    private static Logger logger = Logger.getLogger("FacultyDao");

    public static List<FacultyModel> getFaculty(String collegeId) {
        return DaoUtils.<FacultyModel>getListData(collegeId, filename);
    }

    public static FacultyModel[] getFacultyArray(String collegeId) {
        List<FacultyModel> faculty = getFaculty(collegeId);
        return faculty.toArray(new FacultyModel[faculty.size()]);
    }

    /**
     * This creates a new faculty member and then saves them to the master list
     * After assigning them an ID
     * THIS NEEDS TO BE USED TO CREATE NEW MEMBERS
     * @param collegeId sim id
     * @param member faculty object
     */
    public void saveNewFaculty(String collegeId, FacultyModel member) {
        List<FacultyModel> faculty = getFaculty(collegeId);
        faculty.add(member);
        saveAllFaculty(collegeId, faculty);
    }

    public static void saveAllFaculty(String collegeId, List<FacultyModel> faculty) {
        DaoUtils.<FacultyModel>saveAllListData(collegeId, faculty, filename);
    }

    /**
     * Deletes the faculty file
     * @param collegeId
     */
    public static void removeAllFaculty(String collegeId){
        File file = new File(DaoUtils.getFilePath(collegeId, filename));
        file.delete();
    }

    public static void removeSingleFaculty(String collegeId, FacultyModel member){
        logger.info("Removing faculty member..");
        String tmp = member.getFacultyID();
        List<FacultyModel> newFacultyList = getFaculty(collegeId);
        for(FacultyModel faculty : newFacultyList){
            if(tmp.equals(faculty.getFacultyID())){
                logger.info("removing " + faculty.getFacultyName());
                newFacultyList.remove(faculty);
                break;
            }

        }
        saveAllFaculty(collegeId, newFacultyList);
        logger.info("Faculty member removed: " + tmp);
    }

    public static void giveRaise(String collegeId, FacultyModel member){
        logger.info("Giving a raise...");
        int tmpSalary = member.getSalary();
        member.setSalary(tmpSalary + 25000);
    }

    /**
     * returns the number of faculty in the list for the college
     * @param collegeId
     * @return
     */
    public int numberOfFaculty(String collegeId){
        return getFaculty(collegeId).size();
    }






    public static void main(String[] args) {
        testNotes();
    }

    private static void testNotes(){
        final String collegeId = "testFaculty01";
        FacultyDao fao = new FacultyDao();
        List<FacultyModel> faculty = new ArrayList<>();

        System.out.println("Checking id's");

        FacultyModel f1 = new FacultyModel("Dr. Test","Title","Comp",125000, "LSB",collegeId);
        FacultyModel f2 = new FacultyModel("Dr. Test2","LesserTitle","Programming",125000,"LSB",collegeId);
        FacultyModel f3 = new FacultyModel("Dr. Test2","LesserTitle","Programming",125000,"LSB",collegeId);
        f3.setFacultyID("-1");
        fao.saveNewFaculty(collegeId,f3);
        System.out.println( "ID of member 3: " + String.valueOf(f3.getFacultyID()));
        fao.saveNewFaculty(collegeId,f2);
        System.out.println( "ID of member 2: " + String.valueOf(f2.getFacultyID()));
        fao.saveNewFaculty(collegeId,f1);
        System.out.println( "ID of member 1: " + String.valueOf(f1.getFacultyID()));
        faculty.add(f1);
        faculty.add(f2);
        fao.removeSingleFaculty(collegeId,f3);
        fao.saveAllFaculty(collegeId,faculty);
        assert(fao.numberOfFaculty(collegeId) == 2);
        System.out.println(fao.numberOfFaculty(collegeId));

        List<FacultyModel> outMsgs = fao.getFaculty(collegeId);

        assert(outMsgs.size() == 2);
        assert (outMsgs.get(0).getFacultyName().equals("Dr. Test"));

        FacultyModel f4 = new FacultyModel("Dr. Test23","LesserTitle","Programming",125000,"LSB",collegeId);
        fao.saveNewFaculty(collegeId,f4);
        outMsgs = fao.getFaculty(collegeId);
        System.out.println("Adding a new Faculty member..." + "ID: " + outMsgs.get(2).getFacultyID());
        assert(outMsgs.size() == 3);

        System.out.println("Removing object:  " + f4.getFacultyName() + "ID: " + f4.getFacultyID());
        fao.removeSingleFaculty(collegeId,f4);
        outMsgs = fao.getFaculty(collegeId);
        faculty.clear();
        System.out.println("Clearing list... ");

        System.out.println("Loading in faculty from list");
        faculty = fao.getFaculty(collegeId);


        assert (faculty.size() == 3);
        assert (faculty.get(2).getFacultyName().equals("Dr. Test23"));
        System.out.println("Lets remove all the faculty.... ");
        fao.removeAllFaculty(collegeId);



        File file = new File(DaoUtils.getFilePath(collegeId, filename));
        assert (!file.exists());

        System.out.println("End of testing faculty successful.");
    }

private void testId(){
    final String collegeId = "testFaculty01";
    FacultyDao fao = new FacultyDao();
    List<FacultyModel> faculty = new ArrayList<>();

    System.out.println("Checking id's");
    System.out.println("Checking id's");

    FacultyModel f1 = new FacultyModel("Dr. Test","Title","Comp",125000, "LSB",collegeId);
    FacultyModel f2 = new FacultyModel("Dr. Test2","LesserTitle","Programming",125000,"LSB",collegeId);
    FacultyModel f3 = new FacultyModel("Dr. Test2","LesserTitle","Programming",125000,"LSB",collegeId);
    f3.setFacultyID("-1");
    fao.saveNewFaculty(collegeId,f3);
    System.out.println( "ID of member 3: " + String.valueOf(f3.getFacultyID()));
    fao.saveNewFaculty(collegeId,f2);
    System.out.println( "ID of member 2: " + String.valueOf(f2.getFacultyID()));
    fao.saveNewFaculty(collegeId,f1);
    System.out.println( "ID of member 1: " + String.valueOf(f1.getFacultyID()));
}


}
