package com.endicott.edu.datalayer;
import com.endicott.edu.models.PlayModel;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.io.*;
import java.util.logging.Logger;

public class PlayDao {
    private Logger logger = Logger.getLogger("PlayDao");

    private static String getFilePath(String collegeId) {
        return DaoUtils.getFilePathPrefix(collegeId) + "play.dat";
    }

    /**
     * Checks if there is a Flood in the current college and returns it.
     * If the file does not exist, returns NULL .
     *      - used in: InterfaceUtils.java (openCollegeAndStoreInRequest(..))
     *                 FloodManager.java (handleTimeChange(..))
     *
     * @param collegeId a String representing the ID of the current college
     * @return the existing FloodModel, Null if it doesnt exist
     */
    public static PlayModel getPlay(String collegeId) {

        PlayModel play = null;
        try {
            File file = new File(getFilePath(collegeId));   //Creates a new File instance by converting the given pathname(string) into an abstract pathname.

            if (!file.exists()) {
                return play;  // There are no floods yet, return Null.
            } else {
                FileInputStream fis = new FileInputStream(file);    //creates a FileInputStream by opening a connection to an actual File.
                ObjectInputStream ois = new ObjectInputStream(fis);
                play = (PlayModel) ois.readObject();
                ois.close();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return play;
    }

    /**
     * Creates a new file with its appropriate path and saves theFlood in it.
     *      - used in: FloodManager.java (handleTimeChange(..) & didFloodStartAtThisDorm(..))
     *
     * @param collegeId a String representing the ID of the current college
     * @param thePlay  the PlayModel to save
     */
    public void saveThePlay(String collegeId, PlayModel thePlay) {
        logger.info("Saving one play...");
        try {
            File file = new File(getFilePath(collegeId));
            file.createNewFile();
            FileOutputStream fos;
            fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(thePlay);
            oos.close();

        } catch (FileNotFoundException e) {
            logger.info("Got file not found when attempting to create: " + getFilePath(collegeId));
            e.printStackTrace();
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        } catch (IOException e) {
            logger.info("Got io exceptionfound when attempting to create: " + getFilePath(collegeId));
            e.printStackTrace();
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }

        logger.info("Saved one flood...");
    }

    /**
     * Deletes the file of the flood.
     *  - used in: CollegeManager.java (sellCollege(..))
     *             FloodManager.java (handleTimeChange(..))
     *
     * @param collegeId a String representing the ID of the current college
     */
    public static void deletePlay(String collegeId) {
        File file = new File(getFilePath(collegeId));
        file.delete();
    }
}
