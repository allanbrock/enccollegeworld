package com.endicott.edu.simulators;

import com.endicott.edu.datalayer.PlayDao;
import com.endicott.edu.models.PlayModel;
import com.endicott.edu.models.StudentModel;

import java.util.ArrayList;

public class PlayManager {
    PlayDao playDao = new PlayDao();
    private static ArrayList<StudentModel> cast = new ArrayList<StudentModel>();

    public static void beginPlay(){
        PlayModel play = new PlayModel(cast.size(), cast);
    }
}
