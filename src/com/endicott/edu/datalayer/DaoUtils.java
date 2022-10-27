package com.endicott.edu.datalayer;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by abrocken on 7/20/2017. 
 */
public class DaoUtils {
    private static Logger logger = Logger.getLogger("DaoUtils");
    private static File home = null;
    private static final String collegeDir = System.getProperty("SystemDrive") + File.separator + "collegesim";

    static public String getFilePathPrefix(String collegeId) {
        //logger.info("Location of colleges: " + getCollegeStorageDirectory());
        return getCollegeStorageDirectory() + File.separator + collegeId;
    }

    static private String getCollegeStorageDirectory() {
        String collegeDir = System.getProperty("SystemDrive") + File.separator + "collegesim";
        new File(collegeDir).mkdirs();
        return collegeDir;
    }
    static private String getCollegeStorageDirectoryNew() {
        // 'SystemDrive' is not a real System property.
        //String collegeDir = System.getProperty("SystemDrive")+ File.separator +"collegesim";

        // use a singleton instance to only make the folder on first run
        if(home==null) {
            home = new File(collegeDir);
            home.mkdirs();
        }

        return collegeDir;
    }

    public static String getFilePath(String collegeId, String filename) {
        return getFilePathPrefix(collegeId) +  filename;
    }

    /**
     * This function returns a list of all the TYPE for a college
     * The college is defined by its collegeId
     * @param collegeId
     * @param filename
     * @return list of data of type <T> loaded from the appropriate filename
     */
    public static <T> List<T> getListData(String collegeId, String filename) {
        ArrayList<T> listOfData = new ArrayList<T>();
        try {
            File file = new File(getFilePath(collegeId, filename));
            if (!file.exists()) {
                return listOfData;  // No data exists
            }
            else{ //data exist lets return the objects....
                FileInputStream fis = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(fis);
                listOfData = (ArrayList<T>) ois.readObject();
                ois.close();
            }
        } catch (IOException | ClassNotFoundException e) {
            logger.warning("IO exception in loading " + filename + ".. ");
            e.printStackTrace();
        }
        return listOfData;
    }

    /**
     * This function returns the data of TYPE for a college
     * The college is defined by its collegeId
     * @param collegeId
     * @param filename
     * @return data of type <T> loaded from the appropriate filename
     */
    public static <T> T getData(String collegeId, String filename) {
        T data = null;
        logger.info("Reading Potential Students from disk.");
        try {
            File file = new File(getFilePath(collegeId, filename));

            if (!file.exists()) {
                return null;
            }
            //data exist lets return the data object....
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            data = (T) ois.readObject();
            logger.info("Read Object");
            ois.close();

        } catch (IOException | ClassNotFoundException e) {
            logger.info("error in getData");
            e.printStackTrace();
        }
        logger.info("Return Object");
        return data;
    }

    /**
     * This function writes a list of TYPE objects to the disk...
     * @param collegeId
     * @param data
     * @param filename
     */
    public static <T> void saveData(String collegeId, T data, String filename) {
        try {
            File file = new File(getFilePath(collegeId, filename));
            file.createNewFile();
            FileOutputStream fos;
            fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(data);
            oos.close();
        } catch (FileNotFoundException e) {
            logger.info("Got FileNotFoundException when attempting to create: " + getFilePath(collegeId, filename));
            e.printStackTrace();
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        } catch (IOException e) {
            logger.info("Got IOException when attempting to create: " + getFilePath(collegeId, filename));
            e.printStackTrace();
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }

        logger.info("Saved " + filename + "...");

    }


}
