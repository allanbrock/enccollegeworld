package com.endicott.edu.datalayer;


import com.endicott.edu.models.FacultyModel;
import com.google.gson.Gson;

import java.io.*;
import java.util.logging.Logger;


/**
 * Created by Mazlin Higbee
 * 10-12-17
 * mhigb411@mail.endicott.edu
 *
 * This class will store and retreive a the last used ID number.
 * Starting at 1 it will be incremented when needed.
 * All ID's will be Unique as this object will be used to create
 * any id that is needed in the given college.
 */
public class IdNumberGenDao {
    //glorified struct for gson
    private static class ID {
        int id;

        public ID(int id) {
            this.id = id;
        }
    }
    private static Logger logger = Logger.getLogger("IdNumberGenDao");
    private static String getFilePath(String collegeId) {
        return DaoUtils.getFilePathPrefix(collegeId) +  "IdGen.json";
    }

    /**
     * Creates the first id and writes it to the disk
     * @param collegeId College instance id
     * @return unique id#
     */
    public static int firstWrite(String collegeId){
        ID id = new ID(1000);
        Gson gson = new Gson();
        FileWriter fw;
        try {
            fw = new FileWriter(getFilePath(collegeId));
            fw.write(gson.toJson(id));
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return id.id;
    }
    /**
     * Lookup the most recently used ID number
     * return an id increased by one and update the record
     *
     * @param collegeId college instance ID
     * @return unique id
     */
    public static int getID(String collegeId){
       // logger.info("Fetching ID...");
        Gson gson = new Gson();
        ID id = new ID(-1);
        try  {
            BufferedReader br = new BufferedReader(new FileReader(getFilePath(collegeId)));
            id = gson.fromJson(br,ID.class);
           // ID tmpId = new ID(id.id);
            id.id++;
            FileWriter fw = new FileWriter(getFilePath(collegeId));
            fw.write(gson.toJson(id));
            fw.close();

        } catch (FileNotFoundException e) {
            return firstWrite(collegeId);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return id.id; //return the original
    }


    public static void deleteIDs(String collegeId) {
        File file = new File(getFilePath(collegeId));
        file.delete();
    }

    public static void main(String[] args) {
        final String collegeId = "testIdGen2";
        IdNumberGenDao idGen = new IdNumberGenDao();
        idGen.deleteIDs(collegeId);
        if (getID(collegeId) == 0) {
            System.out.println("PASS");
        } else {
            System.out.println("FAIL");
        }
        if (getID(collegeId) == 1) {
            System.out.println("PASS");
        } else {
            System.out.println("FAIL");
        }
    }

    public static int randIDGeneration(){
        return (int) (100000 + Math.random() * 900000); // Generates random 6 digit ID
    }

}
