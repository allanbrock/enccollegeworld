package com.endicott.edu.simulators;
import com.endicott.edu.datalayer.*;
import com.endicott.edu.models.*;
import java.util.List;

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
        createItem("Smoke Detectors", false, "smokedetector.png", 50000, 1, "", collegeId);
        createItem("Drains",false,"drain.png",15000, 0, "", collegeId);
        createItem("Hand Sanitizers", false, "handsanitizer.png", 10000, 0, "", collegeId);
        createItem("Snowplows",false, "snowplow.png", 4200, 2, "", collegeId);
        createItem("Pipes", false, "pipes.png", 47000, 2, "", collegeId);
        createItem("Snow Pushers", false, "snowPusher.png", 2200, 0, "", collegeId);
        createItem("Mainstage Production", false, "drama.png", 2000, 3, "", collegeId);
    }

    public void createItem(String name, Boolean isPurchased, String imageName, int cost, int availableAtGate, String description, String collegeId){
        ItemModel newItem = new ItemModel(name, isPurchased, imageName, cost, availableAtGate <= 0);
        // TODO: Add description to the model (and to the display page)
        // TODO: Add gate number to the model
        inventory.saveNewItem(collegeId, newItem);
    }

    public static void buyItem(String name, String collegeId){
        List<ItemModel> items = InventoryDao.getItems(collegeId);

        if(items.size() > 0) {
            for (int i = 0; i < items.size(); i++){
                if(items.get(i).getName().equals(name)){
                    items.get(i).setPurchased(true);
                    Accountant.payBill(collegeId,"Upgrade: "+items.get(i).getName()+". Cost: ", items.get(i).getCost());
                }
            }
        }
        inventory.saveAllItems(collegeId, items);
    }

    public static void unlockItem(String name, String collegeId){
        List<ItemModel> items = InventoryDao.getItems(collegeId);

        if(items.size() > 0) {
            for (int i = 0; i < items.size(); i++){
                if(items.get(i).getName().equals(name)){
                    items.get(i).setUnlocked(true);
                }
            }
        }
        inventory.saveAllItems(collegeId, items);
    }

    public static void handleTimeChange(String collegeId, int hoursAlive, PopupEventManager popupManager) {
        // Get the gate that we are at an unlock inventory items.
        // But be careful not to lock an item that was already unlocked.
    }
}

