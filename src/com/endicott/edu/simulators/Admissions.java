package com.endicott.edu.simulators;

//import com.endicott.edu.datalayer.AdmissionsDao;
import com.endicott.edu.datalayer.StudentDao;

import java.util.logging.Logger;

/***
 * Admissions simulates everything having to do with potential students considering
 * attending the virtual college.  The Admissions model generates potential students
 * and tracks their interest as we approach the admissions date
 */
public class Admissions {
    //static private AdmissionsDao dao = new AdmissionsDao();
    static private Logger logger = Logger.getLogger("Admissions");
    static private StudentDao studentDao = new StudentDao();

}
