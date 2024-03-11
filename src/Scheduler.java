import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;

/**
 *
 * The Scheduler class manages elevator requests and coordinates
 * the elevators based on the current system state. It receives requests
 * from floors, processes them, and assigns elevators to fulfill those requests.
 * It also checks the current floor status of elevators and sends the status to the floor.
 * Scheduler class extends Thread and is responsible for managing the state of the elevator system.
 * It interacts with the Synchronizer to get the status of the elevator and handle requests.
 *
 * @author Harishan Amutheesan, 101154757
 * @author Yehan De Silva, 101185388
 * @date February 2nd, 2024 / February 22nd, 2024
 * @version iteration1, iteration2
 */
public class Scheduler extends Thread {
    public static final InetAddress SCHEDULER_IP;   // IP address of Scheduler
    static {
        try {
            SCHEDULER_IP = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }
    public static final int SCHEDULER_PORT = 25; //Current Port
    private DatagramSocket sendSocket, receiveSocket;
    private DatagramPacket receivePacket;
    private SchedulerState currentState;
    private HashMap<String, SchedulerState> states;

    /**
     * Constructor for the Scheduler class
     */
    public Scheduler() {
        // Initialize sockets
        try {
            // For receiving FloorRequests and ElevatorStatus (bind to dedicated port)
            receiveSocket = new DatagramSocket(SCHEDULER_PORT);
            // For sending FloorRequests and ElevatorStatus (bind to any available port)
            sendSocket = new DatagramSocket();
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }

        // Add all states
        states = new HashMap<>();
        states.put("WaitingForPacket", new WaitingForPacket(this));
        states.put("CheckingPacketType", new CheckingPacketType(this));
        states.put("ProcessingElevatorStatus", new ProcessingElevatorStatus(this));
        states.put("ProcessingFloorRequest", new ProcessingFloorRequest(this));
        // Set the initial state
        setState(new WaitingForPacket(this));
    }

    /**
     * Run method
     */
    public void run() {
        while(true) {
            // Start the current state
            currentState.start();
        }
    }

    /**
     * Handles event where packet is received
     */
    public void packetReceived() {
        setState(currentState.packetReceived());
    }

    /**
     * Handles event where ElevatorStatus is received
     */
    public void elevatorStatusReceived() {
        setState(currentState.elevatorStatusReceived());
    }

    /**
     * Handles event where FloorRequest is received
     */
    public void floorRequestReceived() {
        setState(currentState.floorRequestReceived());
    }

    /**
     * Handles the event where a packet is sent by the Scheduler
     */
    public void packetSent() {
        setState(currentState.packetSent());
    }

    /**
     * Sets the current state of the Scheduler
     */
    private void setState(SchedulerState nextState) {
        // Set the current state
        currentState = nextState;
        // Display current state info
        currentState.displayState();
    }

    /**
     * Returns the state object according to the specified state name
     * @param stateName - name of the state to be returned
     * @return a state
     */
    public SchedulerState getState(String stateName) {
        return states.get(stateName);
    }

    public DatagramSocket getSendSocket() {
        return sendSocket;
    }

    public DatagramSocket getReceiveSocket() {
        return receiveSocket;
    }

    public void saveReceivedPacket(DatagramPacket packet) {
        receivePacket = packet;
    }

    public DatagramPacket getReceivedPacket() {
        return receivePacket;
    }
}