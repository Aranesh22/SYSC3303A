import java.io.IOException;
import java.net.*;
import java.util.Arrays;

/**
 * The FloorSubsystem class is responsible for sending floor requests to the scheduler as well as receiving elevator
 * status updates from the scheduler.
 *
 *  @author Yehan De Silva (101185388)
 *  @version iteration3
 *  @date Mar 6, 2024
 */
public class FloorSubsystem extends Thread {

    // Fields
    private final Synchronizer synchronizer;
    private DatagramSocket receiveSocket;
    private DatagramPacket receivePacket, sendPacket;

    /**
     * Initializing static data to be used throughout the program
     */
    public final static float DEFAULT_FLOOR_HEIGHT = 3.916f;
    public final static int DEFAULT_MIN_FLOOR = 1;
    public final static int DEFAULT_MAX_FLOOR = 7;
    public static final int FLOOR_SUBSYSTEM_PORT = 24;


    public FloorSubsystem(Synchronizer synchronizer) {
        this.synchronizer = synchronizer;
        try {
            this.receiveSocket = new DatagramSocket(FLOOR_SUBSYSTEM_PORT);
        } catch (SocketException se) {
            this.close(se);
        }
    }

    @Override
    public void run() {
        while (true) {
            this.getMsg();
            this.processMsg();
        }
        // TODO This should be moved to processMsg() when we determine that we received an elevator status message
        /*while (synchronizer.isRunning()) {
            System.out.println("FloorRequestSimulator: Elevator at " + synchronizer.getCurrentFloor());
        }*/
    }

    /**
     * Floor subsystem exits with error. Closes the DataSocket before exiting.
     * @param e Error received during runtime.
     */
    private void close(Exception e) {
        if (this.receiveSocket != null) {
            this.receiveSocket.close();
        }
        e.printStackTrace();
        System.exit(1);
    }

    /**
     * Get a UDP packet from socket.
     */
    private void getMsg() {
        byte[] data = new byte[100];
        this.receivePacket = new DatagramPacket(data, data.length);

        try {
            receiveSocket.receive(this.receivePacket);
        } catch(IOException e) {
            this.close(e);
        }
    }

    /**
     * Process the received packet.
     * Packet could be a floor request or an elevator status.
     */
    private void processMsg() {
        // TODO This method should differentiate between floor requests and elevator status before deciding on appropriate method to service method
        FloorRequest floorRequest = new FloorRequest(new String(this.receivePacket.getData(), 0, this.receivePacket.getLength()));
        System.out.println("FloorSubsystem: Received floor request: " + floorRequest);
        this.sendMsgToScheduler();
        this.synchronizer.putRequest(floorRequest);
    }

    /**
     * Relay floor request to scheduler.
     */
    private void sendMsgToScheduler() {
        try {
            this.sendPacket = new DatagramPacket(this.receivePacket.getData(), this.receivePacket.getLength(),
                    InetAddress.getLocalHost(), Scheduler.SCHEDULER_PORT);
        } catch (UnknownHostException e) {
            this.close(e);
        }
        try {
            DatagramSocket sendSocket = new DatagramSocket();
            // TODO Remove timeout one synchronizer is removed
            sendSocket.setSoTimeout(1000);
            sendSocket.send(this.sendPacket);
            sendSocket.close();
        } catch (IOException e) {
            this.close(e);
        }
    }
}
