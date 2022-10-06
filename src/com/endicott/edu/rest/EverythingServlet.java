package com.endicott.edu.rest;

import com.endicott.edu.datalayer.EverythingDao;
import com.endicott.edu.models.EverythingModel;

/**
 * Created by Ran Ben David on 09/25/2019.
 */

public class EverythingServlet extends ServletTemplate<EverythingModel> {
    @Override
    protected EverythingModel handleGet(String[] pathSegments) {
        EverythingModel everything = EverythingDao.getEverything(pathSegments[0]);
        // Does college exist? A poor way to check is number of students.
        if (everything.students.length == 0) {
            return null;
        }
        return everything;
    }

    @Override
    protected EverythingModel[] handleGetList(String[] pathSegments) {
        return null;
    }
}
