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

    public static final int FLOOR_REQUEST_SIMULATOR_PORT = 23;

    /**
     * Default constructor
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
     * The thread's main task is to parse the data file for requests and send them to the
     * floor subsystem.
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
    private void close(Exception e) {
        if (this.sendSocket != null) {
            this.sendSocket.close();
        }
        e.printStackTrace();
        System.exit(1);
    }

    /**
     * Parses data file for requests.
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
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

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
            } catch (ParseException | InterruptedException e) {
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
        DatagramPacket sendPacket = new DatagramPacket(sendReqMsg, sendReqMsg.length,
                FloorSubsystem.FLOOR_SUBSYSTEM_IP, FloorSubsystem.FLOOR_SUBSYSTEM_PORT);

        // Sends the message
        try {
            sendSocket.send(sendPacket);
        } catch (IOException e) {
            this.close(e);
        }
    }
}