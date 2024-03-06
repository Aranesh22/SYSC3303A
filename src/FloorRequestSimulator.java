/**
 * The floor class is responsible or sending requests to the synchronizer by storing an array list
 * of request objects
 *
 * @author Aranesh Athavan
 * @version iteration1
 * @date Feb 2, 2024
 */

import java.io.*;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * The floor class is responsible for creating requests and once it is called upon
 * it then stores the data in the Synchronized object that was created
 *
 *  @author Aranesh Athavan
 *  @author Yehan De Silva (101185388)
 *  @version iteration3
 *  @date Feb 2, 2024
 */


public class FloorRequestSimulator extends Thread {

    /**
     * Initializing static data to be used throughout the program
     */
    public final static float DEFAULT_FLOOR_HEIGHT = 3.916f;
    public final static int DEFAULT_MIN_FLOOR = 1;
    public final static int DEFAULT_MAX_FLOOR = 7;

    /**
     * Creating an instance of Synchronizer and a list to contain the requests
     */
    private final Synchronizer syncFloor;
    ArrayList<FloorRequest> reqList = new ArrayList<>();

    /**
     * Takes in @param sync to initialize the synchronizer to be used for sending a put request
     */
    public FloorRequestSimulator(Synchronizer sync) {
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
        sendRequests();
        while (syncFloor.isRunning()) {
            System.out.println("FloorRequestSimulator: Elevator at " + syncFloor.getCurrentFloor());
        }
        System.out.println("FloorRequestSimulator: Has exited");
    }

    /**
     * Sends all requests and simulates waiting in between requests.
     */
    private void sendRequests() {
        // Time format of the requests
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

        // Sends each request and waits in between each request.
        for (int i = 0; i < reqList.size(); i++) {
            try {
                Date curReqTime = sdf.parse(reqList.get(i).getTime());
                Date prevReqTime = sdf.parse(reqList.get(Math.max((i - 1), 0)).getTime());
                long timeDif = curReqTime.getTime() - prevReqTime.getTime();
                Thread.sleep(timeDif);
                System.out.println("FloorRequestSimulator: Sent floor request: " + reqList.get(i));
                syncFloor.putRequest(reqList.get(i));
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     *
     * This function is reading through the data in csv and storing it in a request file
     */
    private void parseReqFile() throws FileNotFoundException {
        String path = "data/data.csv";
        String line;
        try {
            BufferedReader readBuff = new BufferedReader(new FileReader(path));
            // Go through lines in data file to parse floor requests
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