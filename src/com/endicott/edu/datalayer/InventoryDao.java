package com.endicott.edu.datalayer;

import com.endicott.edu.models.ItemModel;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

public class InventoryDao {
    private static String getFilePath(String runId) {
        return DaoUtils.getFilePathPrefix(runId) +  "inventory.dat";
    }
    private Logger logger = Logger.getLogger("InventoryDao");

    public static List<ItemModel> getItems(String runId) {
        ArrayList<ItemModel> inventory = new ArrayList<>();
        ItemModel itemModel = null;
        try {
            File file = new File(getFilePath(runId));

            if (!file.exists()) {
                return inventory;  // There are no items yet.
            }
            else{
                FileInputStream fis = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(fis);
                inventory = (ArrayList<ItemModel>) ois.readObject();
                ois.close();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        Collections.sort(inventory, new Comparator<ItemModel>() {
            @Override
            public int compare(ItemModel lhs, ItemModel rhs) {
                // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
                return lhs.getName().compareTo(rhs.getName());
            }
        });

        return inventory;
    }


    public static ItemModel[] getInventoryArray(String collegeId) {
        List<ItemModel> inventory = getItems(collegeId);
        return inventory.toArray(new ItemModel[inventory.size()]);
    }

    public void saveAllItems(String item, List<ItemModel> inventory){
        logger.info("Saving all students...");
        try {
            File file = new File(getFilePath(item));
            file.createNewFile();
            FileOutputStream fos;
            fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(inventory);
            oos.close();
        } catch (FileNotFoundException e) {
            logger.info("Got file not found when attempting to create: " + getFilePath(item));
            e.printStackTrace();
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        } catch (IOException e) {
            logger.info("Got io exceptionfound when attempting to create: " + getFilePath(item));
            e.printStackTrace();
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }

        logger.info("Saved items...");
    }

    public void saveNewItem(String runId, ItemModel item) {
        logger.info("Saving new item...");
        List<ItemModel> inventory = getItems(runId);
        item.setName(runId);
        inventory.add(item);
        saveAllItems(runId, inventory);
    }

    public static void deleteItem(String runId) {
        File file = new File(getFilePath(runId));
        file.delete();
    }

    public static void main(String[] args) {

    }

}
