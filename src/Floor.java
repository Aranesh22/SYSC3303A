/**
 * The floor class is responsbile or sending requests to the synchronizer by storing an array list
 * of request objects
 *
 * @author Aranesh Athavan
 * @version iteration1
 * @date Feb 2, 2024
 */

import java.io.*;
import java.io.IOException;
import java.util.ArrayList;

/**
 * The floor class is reposnible for creating requests and once it is called upon
 * it then stores the data in the Synchronized object that was created
 *
 *  @author Aranesh Athavan
 *  @version iteration1
 *  @date Feb 2, 2024
 */


public class Floor extends Thread{

    /**
     * Initlizating static data to be used through out the program
     */
    public final static float DEFAULT_FLOOR_HEIGHT = 3.916f;
    public final static int DEFAULT_MIN_FLOOR = 1;
    public final static int DEFAULT_MAX_FLOOR = 7;

    /**
     * Creating an instance of Synchfronizer and a list to contain the requests
     */
    private Synchronizer syncFloor;
    ArrayList<FloorRequest> reqList = new ArrayList<>();

    /**
     * Takes in @param sync to initalize the syncrhonnizer to be used for sending a putrequest
     */
    public Floor(Synchronizer sync) {
        this.syncFloor = sync;
    }

    /**
     * The run function is used to create requests objects from the csv data
     */
    public synchronized void run() {
        try {
            parseReqFile();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        for (int i = 0; i < reqList.size(); i++) {

            FloorRequest floorRequest = reqList.get(i);
            syncFloor.putRequest(floorRequest);

        }
        while (syncFloor.isRunning()) {
            System.out.println("Floor: Elevator at " + syncFloor.getCurrentFloor());
        }
        System.out.println("Floor: Has exited");
    }

    /**
     *
     * This function is reading through the data in csv and storing it in a request file
     */
    public void parseReqFile() throws FileNotFoundException {

        String path = "data/data.csv";
        String line;
        try {

            BufferedReader readBuff = new BufferedReader(new FileReader(path));
            while ((line = readBuff.readLine()) != null) {
                reqList.add(new FloorRequest(line));
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}