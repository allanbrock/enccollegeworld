package com.endicott.edu.models;

import com.endicott.edu.simulators.CoachManager;

import java.io.Serializable;
import java.util.List;

public class EverythingModel implements Serializable {
    public CollegeModel college;
    public BuildingModel[] buildings;
    public List<SportModel> sports;
    public ObjectivesModel objectives;
    public List<FacultyModel> faculty;
    public List<EventsModel> events;
    public List<ItemModel> store;
    public StudentModel[] students;
    public NewsFeedItemModel[] news;
    public List<CoachModel> coaches;
    public List<PopupEventModel> popupEvent;
    public AdmissionsModel admissions;
}