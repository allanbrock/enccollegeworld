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
        createItem("Smoke Detectors", false, "smokedetector.png", 50000, true,collegeId);
        createItem("Drains",false,"drain.png",2500, true, collegeId);
        createItem("Hand Sanitizers", false, "handsanitizer.jpg", 10000, true, collegeId);
        createItem("Snowplows",false, "snowplow.png", 4200, true, collegeId);
        createItem("Pipes", false, "pipes.png", 47000, true, collegeId);
        createItem("Snow Pushers", false, "snowPusher.png", 2200, true, collegeId);
    }

    public void createItem(String name, Boolean isPurchased, String imageName, int cost, Boolean isUnlocked, String collegeId){
        ItemModel newItem = new ItemModel(name, isPurchased, imageName, cost, isUnlocked);
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
}

