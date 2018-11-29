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
        unlockItems(collegeId, 1);
    }

    /**
     * Gets the list of all items.
     * Checks each item till it finds the one with the name provided.
     * Returns the Bool value of the item's field 'isPurchased'
     * @return whether or not the item has been purchased.
     * */
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
        createItem("Snowplows",false, "snowplow.png", 4200, 2, "Buying Snowplows reduces both future maintenance costs and the probability of a 'Severe High-Intensity Snow Storm' from happening again.", collegeId);
        createItem("Pipes", false, "pipes.png", 47000, 2, "Investing in better and newer Pipes decreases the chances of 'Mid-Intensity Blizzards' from occurring often and avoids high snow-removals costs.", collegeId);
        createItem("Snow Pushers", false, "snowPusher.png", 2200, 0, "Purchasing Snow Pushers prevents 'Low-Intensity Snow Storms' from happening so often, as well as reducing future costs of snow removal.", collegeId);
        createItem("Mainstage Production", false, "drama.png", 2000, 3, "", collegeId);
    }


    public void createItem(String name, Boolean isPurchased, String imageName, int cost, int availableAtGate, String description, String collegeId){
        ItemModel newItem = new ItemModel(name, isPurchased, imageName, cost, false, availableAtGate, description);
        // TODO: Add description to the model (and to the display page)
        // TODO: Add gate number to the model
        inventory.saveNewItem(collegeId, newItem);
//        if(newItem.getName().equals("Mainstage Production")){
//            PlayManager.beginPlay();
//        }
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
    
    public static void unlockItems(String collegeId, int gate){
        List<ItemModel> items = InventoryDao.getItems(collegeId);

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
        int gate = CollegeManager.getGate(collegeId);
        unlockItems(collegeId, gate);
    }
}

