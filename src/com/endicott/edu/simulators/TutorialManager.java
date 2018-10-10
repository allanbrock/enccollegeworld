package com.endicott.edu.simulators;

import com.endicott.edu.datalayer.TutorialDao;
import com.endicott.edu.models.TutorialModel;

public class TutorialManager {
    static private TutorialDao tutorialDao = new TutorialDao();
    private int counter = 0;
    private String description = "";


    public static void loadTip(String collegeId, int i, String page, String tip){
        // TODO: call TutorialDao to save the tip.
        TutorialModel tutorial = new TutorialModel(tip, page, i);
        tutorialDao.saveNewTutorial(collegeId, tutorial);
    }

    public void nextButtonManager(){

    }
}
