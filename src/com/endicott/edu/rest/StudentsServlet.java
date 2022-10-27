package com.endicott.edu.rest;

import com.endicott.edu.api.ServletTemplate;
import com.endicott.edu.datalayer.StudentDao;
import com.endicott.edu.models.Student;

/**
 * Created by Timothy Amello on 10/02/2019.
 */

public class StudentsServlet extends ServletTemplate<Student> {
    @Override
    protected Student handleGet(String[] pathSegments) {
        if (pathSegments.length < 2) {
            return null;
        }

        String collegeId = pathSegments[0];
        int id = Integer.parseInt(pathSegments[1]);
        Student[] students = StudentDao.getStudentsArray(collegeId);

        for (Student student : students) {
            if (student.getId() == id) {
                return student;
            }
        }

        return null;
    }

    @Override
    protected Student[] handleGetList(String[] pathSegments) {
        if (pathSegments.length > 1) {
            return null;
        }

        String collegeId = pathSegments[0];
        Student[] students = StudentDao.getStudentsArray(collegeId);
        return students;
    }
}
//
//public class StudentsServlet extends javax.servlet.http.HttpServlet {
//    private static Logger logger = Logger.getLogger("ViewAboutServlet");
//
//    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
//        String pathInfo = request.getPathInfo();
//        if (pathInfo == null) {
//            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
//            return;
//        }
//
//        String[] splits = pathInfo.split("/");
//        if(splits.length < 2) {
//            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
//            return;
//        }
//
//        String collegeId = splits[1];
//        StudentModel[] students;
//        students = StudentDao.getStudentsArray(collegeId);
//
//        response.setHeader("Access-Control-Allow-Origin", "*");
//        response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, OPTIONS");
//        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With");
//
//        RestHelper.sendAsJson(response, students);
//    }
//}
