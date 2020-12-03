package com.endicott.edu.datalayer;

import com.endicott.edu.models.AcademicModel;

import java.io.File;
import java.util.logging.Logger;

public class AcademicsDao {
    private static final String filename = "academics.dat";
    private static Logger logger = Logger.getLogger("AcademicsDao");

    private AcademicsDao(){
        // no one should be instantiating DAOs...
    }

    public static AcademicModel getAcademics(String collegeId) {
        return DaoUtils.<AcademicModel>getData(collegeId, filename);
    }

    public static void saveAcademicsData(String collegeId, AcademicModel academicModel) {
        DaoUtils.<AcademicModel>saveData(collegeId, academicModel, filename);
    }

    public static void removeAcademicData(String collegeId) {
        File file = new File(DaoUtils.getFilePath(collegeId, filename));
        file.delete();
    }


}
