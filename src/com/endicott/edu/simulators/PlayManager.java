package com.endicott.edu.simulators;

import com.endicott.edu.datalayer.PlayDao;
import com.endicott.edu.models.PlayModel;
import com.endicott.edu.models.StudentModel;

import java.util.ArrayList;

public class PlayManager {
    private static PlayDao playDao = new PlayDao();
    private static ArrayList<StudentModel> cast = new ArrayList<StudentModel>();

    public static void beginPlay(){
        PlayModel play = new PlayModel(cast.size(), cast);
    }

    public static void handleTimeChange(String collegeId, int hoursAlive, PopupEventManager popupManager) {
        // Check if play is in production
        PlayModel play = playDao.getPlay(collegeId);

        if (play != null) {// Is play in production
            // advance days since purchase by 1

            // figure out what happens next.
            // probably case statement based on play state?
            // you'll be looking days since purchased && play state && isProfessional?
            // decide popup.  
        }
        else {
            // is play bought. Look InventoryManager.
            // if purchased - create a play
            popupManager.newPopupEvent("Plague!",
                    "Dir?",
                    "Student", "picked_student", "Pro", "picked_pro",
                    "resources/images/plague.jpg", "Plague Doctor");
        }
    }

    public static void handleProfessionalDirectorPicked(String collegeId) {
        // Store the fact that a professional was picked.  Maybe another attributed?  boolean isProDirector?
        // get play.
        // play and set isProDirector
        // and set play state to be director picked
        // save play
    }
}
