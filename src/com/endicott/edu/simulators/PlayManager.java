package com.endicott.edu.simulators;

import com.endicott.edu.datalayer.PlayDao;
import com.endicott.edu.models.PlayModel;
import com.endicott.edu.models.StudentModel;

import java.util.ArrayList;

public class PlayManager {
    private static PlayDao playDao = new PlayDao();
    private static InventoryManager inventoryManager = new InventoryManager();
    private static PlayModel play;
    private static double chanceNumber;


    public static void handleTimeChange(String collegeId, int hoursAlive, PopupEventManager popupManager) {
        // Check if play is in production
        play = playDao.getPlay(collegeId);

        if (play != null) {// Is play in production
            // advance days since purchase by 1
            play.setDaysSincePurchase(play.getDaysSincePurchase() + 1);
            playDao.saveThePlay(collegeId, play);
            // figure out what happens next.
            if (play.getState() == PlayModel.PlayState.DIRECTOR_PICKED && play.getDaysSincePurchase() == 3 && play.getDirector().equals("Pro")){
                chanceNumber = Math.random() * 100;
                if (chanceNumber > 20){
                    popupManager.newPopupEvent("Showtime", "The show has just been performed, it was a hit!", "Yay!", "hit_show", "smile", "great show");
                    play.setPayout(play.getPayout() + 1000);
                    Accountant.receiveIncome(collegeId, "Drama Club Performance", play.getPayout());
                    playDao.deletePlay(collegeId);
                    play.setDone(true);
                }
                else {
                    popupManager.newPopupEvent("Oh no!", "Right before the show started, your lead actor broke his leg.  His understudy was forced to step in.  The show did not do so well...", "oof", "broken_leg", "HEALTH", "hospital visit required");
                    play.setPayout(play.getPayout() - 1000);
                    Accountant.receiveIncome(collegeId, "Drama Club Performance", play.getPayout());
                    playDao.deletePlay(collegeId);
                    play.setDone(true);
                }
            }
            // probably case statement based on play state?
            // you'll be looking days since purchased && play state && isProfessional?
            // decide popup.
        }
        else {
            // is play bought. Look InventoryManager.
            // if purchased - create a play
            if(inventoryManager.isPurchased("Mainstage Production", collegeId) && !play.isDone()) {
                play = new PlayModel();
                playDao.saveThePlay(collegeId, play);
                popupManager.newPopupEvent("It Begins!",
                        "You need to choose a director to direct the play.  You can either choose a student to run it, or hire a professional.",
                        "Student", "picked_student", "Professional", "picked_pro",
                        "resources/images/drama.png", "Drama Club");
            }
        }
    }

    public static void handleProfessionalDirectorPicked(String collegeId) {
        // Store the fact that a professional was picked.  Maybe another attributed?  boolean isProDirector?
        play = playDao.getPlay(collegeId);
        play.setState(PlayModel.PlayState.DIRECTOR_PICKED);
        play.setPayout(play.getPayout() + 1000);
        play.setDirector("Pro");
        playDao.saveThePlay(collegeId, play);
        // get play.
        // play and set isProDirector
        // and set play state to be director picked
        // save play
    }

    public static void handleStudentDirectorPicked(String collegeId) {
        play = playDao.getPlay(collegeId);
        play.setState(PlayModel.PlayState.DIRECTOR_PICKED);
        play.setDirector("Student");
        playDao.saveThePlay(collegeId, play);
    }

    public static void establishCollege(String collegeId) {
        InventoryManager.createItem("Mainstage Production", false, "drama.png", 2000, 0, "Providing funding for a main stage production can provide your college with extra income.", collegeId);
    }
}
