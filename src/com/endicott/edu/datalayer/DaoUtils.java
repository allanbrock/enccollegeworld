package com.endicott.edu.datalayer;

import java.io.File;
import java.util.logging.Logger;

/**
 * Created by abrocken on 7/20/2017. 
 */
public class DaoUtils {
    private String REST_SERVICE_URL = "http://localhost:8080/College_sim3_war_exploded/finances";
    private static Logger logger = Logger.getLogger("DaoUtils");

    static public String getFilePathPrefix(String collegeId) {
        logger.info("Location of colleges: " + getCollegeStorageDirectory());
        return getCollegeStorageDirectory() + File.separator + collegeId;
    }

    static private String getCollegeStorageDirectory() {
        String collegeDir = System.getProperty("SystemDrive")+ File.separator +"collegesim";
//        if (collegeDir == null)
//            collegeDir = System.getenv("SystemDrive")+ File.separator +"collegesim";
        new File(collegeDir).mkdirs();
        return collegeDir;
    }
}
