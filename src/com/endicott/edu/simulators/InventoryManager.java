package com.endicott.edu.simulators;
import com.endicott.edu.datalayer.*;
import com.endicott.edu.models.*;
import java.util.List;

import static com.endicott.edu.datalayer.InventoryDao.*;

/**
 * Responsible for simulating students at the college.
 */
public class InventoryManager {
    private static InventoryDao inventory= new InventoryDao();

    /**
     * The college has just been created.  Add initial students and calculate
     * student statistics.
     *
     * @param collegeId
     */
    public static void establishCollege(String collegeId) {
        // Note that each manager is responsible for creating items in the store.
        int gate = CollegeManager.getGate(collegeId);
        unlockItems(collegeId, gate);
    }

    /**
     * Gets the list of all items.
     * Checks each item till it finds the one with the name provided.
     * Returns the Bool value of the item's field 'isPurchased'
     * @return whether or not the item has been purchased.
     * */
    public static boolean isPurchased(String name, String collegeId){
        List<ItemModel> items = getItems(collegeId);

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
   }


    public static void createItem(String name, Boolean isPurchased, String imageName, int cost, int availableAtGate, String description, String collegeId){
        ItemModel newItem = new ItemModel(name, isPurchased, imageName, cost, false, availableAtGate, description);
        InventoryDao.saveNewItem(collegeId, newItem);
        if (availableAtGate > 0)
            GateManager.createGate(collegeId, name, description, "resources/images/" + imageName, availableAtGate);
    }

    public static void buyItem(String name, String collegeId){
        List<ItemModel> items = getItems(collegeId);
        CollegeModel college = CollegeDao.getCollege(collegeId);

        if(items.size() > 0) {
            for (int i = 0; i < items.size(); i++){
                if(items.get(i).getName().equals(name)){
                    items.get(i).setPurchased(true);
                    Accountant.payBill(collegeId,"Upgrade: "+items.get(i).getName()+". Cost: ", items.get(i).getCost());
                    college.getExpensesGraph().setStore(items.get(i).getCost());
                }
            }
        }
        college.getExpensesGraph().calculateExpenses();

        inventory.saveAllItems(collegeId, items);
    }
    
    public static void unlockItems(String collegeId, int gate){
        List<ItemModel> items = getItems(collegeId);

        if(items.size() > 0) {
            for (int i = 0; i < items.size(); i++){
                if(items.get(i).getGateNum() <= gate && items.get(i).getUnlocked().equals(false)){
                    items.get(i).setUnlocked(true);
                }
            }
        }
        inventory.saveAllItems(collegeId, items);
    }

    public static void handleTimeChange(String collegeId, int hoursAlive, PopupEventManager popupManager) {
        // Get the gate that we are at an unlock inventory items.
        // But be careful not to lock an item that was already unlocked.
//        int gate = CollegeManager.getGate(collegeId);
        int gate = GateManager.calculateGateLevel(collegeId);
        unlockItems(collegeId, gate);
    }
}

