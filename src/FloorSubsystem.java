import java.io.IOException;
import java.net.*;

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
    private DatagramSocket sendReceiveSocket;
    private DatagramPacket receivePacket;

    /**
     * Initializing static data to be used throughout the program
     */
    public final static float DEFAULT_FLOOR_HEIGHT = 3.916f;
    public final static int DEFAULT_MIN_FLOOR = 1;
    public final static int DEFAULT_MAX_FLOOR = 7;

    public static final InetAddress FLOOR_SUBSYSTEM_IP;   // IP address of Scheduler
    static {
        try {
            FLOOR_SUBSYSTEM_IP = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }
    public static final int FLOOR_SUBSYSTEM_PORT = 24;

    /**
     * Floor Subsystem constructor.
     */
    public FloorSubsystem() {
        try {
            this.sendReceiveSocket = new DatagramSocket(FLOOR_SUBSYSTEM_PORT);
        } catch (SocketException se) {
            this.close(se);
        }
    }

    /**
     * Main method of floor subsystem thread. Get message, process it and loop.
     */
    @Override
    public void run() {
        while (true) {
            this.getMsg();
            this.processMsg();
        }
    }

    /**
     * Floor subsystem exits with error. Closes the DataSocket before exiting.
     * @param e Error received during runtime.
     */
    private void close(Exception e) {
        if (this.sendReceiveSocket != null) {
            this.sendReceiveSocket.close();
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
            sendReceiveSocket.receive(this.receivePacket);
        } catch(IOException e) {
            this.close(e);
        }
    }

    /**
     * Process the received packet.
     * Packet could be a floor request or an elevator status.
     */
    private void processMsg() {
        // Try creating an elevator status object.
        try {
            ElevatorStatus elevatorStatus = new ElevatorStatus(this.receivePacket.getData(), this.receivePacket.getLength());
            System.out.println("FloorSubsystem: Received elevator status: Elevator " + elevatorStatus.getElevatorId() +
                    " at floor " + elevatorStatus.getCurrentFloor());
        } catch (Exception e) {
            // If it throws an exception, try creating a floor request object.
            try {
                FloorRequest floorRequest = new FloorRequest(this.receivePacket.getData(), this.receivePacket.getLength());
                System.out.println("FloorSubsystem: Received floor request: " + floorRequest);
                this.sendMsgToScheduler();
            } catch (Exception e2) {
                this.close(e2);
            }
        }
    }

    /**
     * Relay floor request to scheduler.
     */
    private void sendMsgToScheduler() {
        DatagramPacket sendPacket = new DatagramPacket(this.receivePacket.getData(), this.receivePacket.getLength(),
                Scheduler.SCHEDULER_IP, Scheduler.SCHEDULER_PORT);
        try {
            sendReceiveSocket.send(sendPacket);
        } catch (IOException e) {
            this.close(e);
        }
    }
}
