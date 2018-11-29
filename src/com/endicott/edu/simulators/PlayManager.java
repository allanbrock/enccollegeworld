package com.endicott.edu.simulators;

import com.endicott.edu.datalayer.PlayDao;
import com.endicott.edu.models.PlayModel;
import com.endicott.edu.models.StudentModel;

import java.util.ArrayList;

public class PlayManager {
    private static PlayDao playDao = new PlayDao();
    private static InventoryManager inventoryManager;


    public static void handleTimeChange(String collegeId, int hoursAlive, PopupEventManager popupManager) {
//        // Check if play is in production
//        PlayModel play = playDao.getPlay(collegeId);
//
//        if (play != null) {// Is play in production
//            // advance days since purchase by 1
//
//            // figure out what happens next.
//            // probably case statement based on play state?
//            // you'll be looking days since purchased && play state && isProfessional?
//            // decide popup.
//        }
//        else {
//            // is play bought. Look InventoryManager.
//            // if purchased - create a play
//            if(inventoryManager.isPurchased("Mainstage Prodection", collegeId)) {
//                play = new PlayModel();
//                playDao.saveThePlay(collegeId, play);
//                popupManager.newPopupEvent("It Begins!",
//                        "You need to choose a director to direct the play.  You can either choose a student to run it, or hire a professional.",
//                        "Student", "picked_student", "Professional", "picked_pro",
//                        "resources/images/drama.jpg", "Drama Club");
//            }
//        }
    }

    public static void handleProfessionalDirectorPicked(String collegeId) {
        // Store the fact that a professional was picked.  Maybe another attributed?  boolean isProDirector?
        // get play.
        // play and set isProDirector
        // and set play state to be director picked
        // save play
    }
}
