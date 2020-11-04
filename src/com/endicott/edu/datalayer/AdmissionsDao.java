package com.endicott.edu.datalayer;

import com.endicott.edu.models.AdmissionsModel;
import com.endicott.edu.models.FacultyModel;

import java.io.File;
import java.util.List;
import java.util.logging.Logger;

/**
 * This class should handle all Data access for Admissions/Potential students
 */
public class AdmissionsDao {
    private static final String filename = "admissions.json";

    private static Logger logger = Logger.getLogger("AdmissionsDao");

    private AdmissionsDao(){
        // no one should be instantiating DAOs...
    }

    public static AdmissionsModel getAdmissions(String collegeId) {
        return DaoUtils.<AdmissionsModel>getData(collegeId, filename);
    }

    public static void saveAdmissionsData(String collegeId, AdmissionsModel admissionsModel) {
        DaoUtils.<AdmissionsModel>saveData(collegeId, admissionsModel, filename);
    }

    public static void removeAdmissionsData(String collegeId) {
        File file = new File(DaoUtils.getFilePath(collegeId, filename));
        file.delete();
    }
}

