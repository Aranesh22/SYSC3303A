import java.io.*;
import java.io.IOException;
import java.net.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * The FloorRequestSimulator class is responsible for simulating floor requests to be handled by the scheduler.
 *
 *  @author Aranesh Athavan
 *  @author Yehan De Silva (101185388)
 *  @version iteration3
 *  @date Mar 6, 2024
 */


public class FloorRequestSimulator extends Thread {

    // Fields
    ArrayList<FloorRequest> reqList;
    private DatagramSocket sendSocket;
    private DatagramPacket sendPacket;

    public static final int FLOOR_REQUEST_SIMULATOR_PORT = 23;

    /**
     * Takes in @param sync to initialize the synchronizer to be used for sending a put request
     */
    public FloorRequestSimulator() {
        reqList = new ArrayList<>();
        try {
            this.sendSocket = new DatagramSocket(FLOOR_REQUEST_SIMULATOR_PORT);
        } catch (SocketException se) {
            this.close(se);
        }
    }

    /**
     * The run function is used to create requests objects from the csv data
     */
    public synchronized void run() {
        parseReqFile();
        sendRequests();
        System.out.println("FloorRequestSimulator: Has exited");
    }

    /**
     * Simulator exits with error. Closes the DataSocket before exiting.
     * @param e Error received during runtime.
     */
    public void close(Exception e) {
        this.sendSocket.close();
        e.printStackTrace();
        System.exit(1);
    }

    /**
     *
     * This function is reading through the data in csv and storing it in a request file
     */
    private void parseReqFile() {
        String path = "data/data.csv";
        String line;
        try {
            BufferedReader readBuff = new BufferedReader(new FileReader(path));
            // Go through lines in data file to parse floor requests
            while ((line = readBuff.readLine()) != null) {
                reqList.add(new FloorRequest(line));
            }
        } catch (FileNotFoundException e) {
            this.close(e);
        } catch (IOException e) {
            this.close(e);
        }
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
                // Calculate time to wait in between requests
                Date curReqTime = sdf.parse(reqList.get(i).getTime());
                Date prevReqTime = sdf.parse(reqList.get(Math.max((i - 1), 0)).getTime());
                long timeDif = curReqTime.getTime() - prevReqTime.getTime();

                // Sleep and then send request
                Thread.sleep(timeDif);
                this.sendUdpRequest(reqList.get(i));
            } catch (ParseException e) {
                this.close(e);
            } catch (InterruptedException e) {
                this.close(e);
            }
        }
    }

    /**
     * Sends request to Floor Subsystem.
     * @param floorRequest Floor request to send.
     */
    private void sendUdpRequest(FloorRequest floorRequest) {
        // Creates a new message to send
        byte[] sendReqMsg = floorRequest.toUdpStringBytes();
        try {
            this.sendPacket = new DatagramPacket(sendReqMsg, sendReqMsg.length,
                    InetAddress.getLocalHost(), FloorSubsystem.FLOOR_SUBSYSTEM_PORT);
        } catch (UnknownHostException e) {
            this.close(e);
        }

        // Sends the message
        System.out.println("FloorRequestSimulator: Sent floor request: " + floorRequest);
        try {
            sendSocket.send(this.sendPacket);
        } catch (IOException e) {
            this.close(e);
        }
    }
}