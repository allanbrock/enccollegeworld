package com.endicott.edu.simulators;
import com.endicott.edu.datalayer.*;
import com.endicott.edu.models.*;
import com.endicott.edu.ui.InterfaceUtils;

import javax.servlet.RequestDispatcher;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Responsible for simulating students at the college.
 */
public class InventoryManager {
    private StudentDao dao = new StudentDao();
    private CollegeDao collegeDao = new CollegeDao();
    private FacultyDao facultyDao = new FacultyDao();
    private BuildingManager buildingMgr = new BuildingManager();
    private CollegeModel college = new CollegeModel();
    private Random rand = new Random();
    private static InventoryDao inventory= new InventoryDao();

    /**
     * The college has just been created.  Add initial students and calculate
     * student statistics.
     *
     * @param collegeId
     */
    public void establishCollege(String collegeId) {
        createAllItems(collegeId);
    }

    public boolean isPurchased(String name, String collegeId){
        List<ItemModel> items = inventory.getItems(collegeId);

        if(items.size() > 0) {
            for (int i = 0; i < items.size(); i++){
                if(items.get(i).getName().equals(name)){
                    return items.get(i).getPurchased();
                }
            }
        }

        return false;
    }

    public void createAllItems(String collegeId){
        createItem("smoke detecters", false, null, 50000, collegeId);
    }

    public void createItem(String name, Boolean isPurchased, String imageName, int cost, String collegeId){
        ItemModel newItem = new ItemModel(name, isPurchased, imageName, cost);
        inventory.saveNewItem(collegeId, newItem);
    }

    public static void buyItem(String name, String collegeId){
        List<ItemModel> items = InventoryDao.getItems(collegeId);

        if(items.size() > 0) {
            for (int i = 0; i < items.size(); i++){
                if(items.get(i).getName().equals(name)){
                    items.get(i).setPurchased(true);
                }
            }
        }
        inventory.saveAllItems(collegeId, items);
    }
}

