package com.endicott.edu.datalayer;

import com.endicott.edu.models.EverythingModel;

public class EverythingDao {
    public static EverythingModel getEverything(String collegeId) {
       EverythingModel model = new EverythingModel();
       model.college = CollegeDao.getCollege(collegeId);
       model.buildings = BuildingDao.getBuildingsArray(collegeId);
       model.sports = SportsDao.getSports(collegeId);
       model.objectives = GateDao.getGatesArray(collegeId);
       model.faculty = FacultyDao.getFaculty(collegeId);
       model.events = EventsDao.getEvents(collegeId);
       model.store = InventoryDao.getItems(collegeId);
       model.students = StudentDao.getStudentsArray(collegeId);
       return model;
    }
}