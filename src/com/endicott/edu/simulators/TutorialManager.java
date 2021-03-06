package com.endicott.edu.simulators;

import com.endicott.edu.datalayer.TutorialDao;
import com.endicott.edu.models.TutorialModel;

import java.util.List;

public class TutorialManager {
    static private TutorialDao tutorialDao = new TutorialDao();
    private static int lastCurrent = 0;
    private String description = "";

    public static TutorialModel getCurrentTip(String page, String collegeId){
        List<TutorialModel> tips = tutorialDao.getTutorials(collegeId);
        for (TutorialModel t : tips){
            if(t.getPage().equals(page) && t.isCurrent()){
                return t;
            }
        }
        return null;
    }

    public static void showTips(String page, String collegeId){
        List<TutorialModel> tips = tutorialDao.getTutorials(collegeId);
        for (int i = 0; i < tips.size(); i++){
            TutorialModel t = tips.get(i);
            if(t.getPage().equals(page) && t.getRefNum() == lastCurrent){
                t.setCurrent(true);
                tutorialDao.saveAllTutorials(collegeId, tips);
            }
        }
    }

    public static void hideTips(String page, String collegeId){
        List<TutorialModel> tips = tutorialDao.getTutorials(collegeId);
        for (int i = 0; i < tips.size(); i++){
            TutorialModel t = tips.get(i);
            if(t.getPage().equals(page) && t.isCurrent()){
                lastCurrent = t.getRefNum();
                t.setCurrent(false);
                tutorialDao.saveAllTutorials(collegeId, tips);
            }
        }
    }

    public static void advanceTip(String page, String collegeId){
        List<TutorialModel> tips = tutorialDao.getTutorials(collegeId);
        for (int i = 0; i < tips.size(); i++){
            TutorialModel t = tips.get(i);
            if(t.getPage().equals(page) && t.isCurrent()){
                t.setCurrent(false);
                if (i < tips.size() - 1){
                    for (int k = i + 1; k < tips.size(); k++) {
                        if (tips.get(k).getPage().equals(page)) {
                            tips.get(k).setCurrent(true);
                            tutorialDao.saveAllTutorials(collegeId, tips);
                            return;
                        }
                    }
                    for (int l = 0; l < tips.size(); l++) {
                        if (tips.get(l).getPage().equals(page)) {
                            tips.get(l).setCurrent(true);
                            tutorialDao.saveAllTutorials(collegeId, tips);
                            return;
                        }
                    }
                } else{
                    for (int j = 0; j < tips.size(); j++) {
                        if (tips.get(j).getPage().equals(page)) {
                            tips.get(j).setCurrent(true);
                            tutorialDao.saveAllTutorials(collegeId, tips);
                            return;
                        }
                    }
                }
            }
        }
    }

    public static void saveNewTip(String collegeId, int i, String page, String tip, boolean isCurrent){
        TutorialModel tutorial = new TutorialModel(tip, page, i, isCurrent);
        tutorialDao.saveNewTutorial(collegeId, tutorial);
    }

    public static void saveNewTip(String collegeId, int i, String page, String tip, boolean isCurrent, String image){
        TutorialModel tutorial = new TutorialModel(tip, page, i, isCurrent, image);
        tutorialDao.saveNewTutorial(collegeId, tutorial);
    }

    public void nextButtonManager(){

    }
}
