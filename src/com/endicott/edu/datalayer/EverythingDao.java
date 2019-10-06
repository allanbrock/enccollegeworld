package com.endicott.edu.datalayer;

import com.endicott.edu.models.EverythingModel;
import com.endicott.edu.simulators.GateManager;

public class EverythingDao {
    public static EverythingModel getEverything(String collegeId) {
       EverythingModel model = new EverythingModel();
       model.college = CollegeDao.getCollege(collegeId);
       model.buildings = BuildingDao.getBuildingsArray(collegeId);
       model.sports = SportsDao.getSports(collegeId);
       model.objectives = GateManager.getObjectives(collegeId);
       model.faculty = FacultyDao.getFaculty(collegeId);
       model.events = EventsDao.getEvents(collegeId);
       model.store = InventoryDao.getItems(collegeId);
       model.students = StudentDao.getStudentsArray(collegeId);
       return model;
    }
}
