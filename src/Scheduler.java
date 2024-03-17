import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
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
    private FloorRequest floorRequest;
    private ElevatorStatus elevatorStatus;
    private SchedulerState currentState;
    private HashMap<String, SchedulerState> states;
    private HashMap<Integer, ElevatorTaskQueue> elevatorTaskQueueHashMap;
    private ArrayList<FloorRequest> floorRequestsToServe;

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

        // Initialize elevator task queue hashmap
        elevatorTaskQueueHashMap =  new HashMap<>();
        // Initialize floor request list
        floorRequestsToServe = new ArrayList<>();

        // Initialize current FloorRequest and ElevatorStatus
        floorRequest = null;
        elevatorStatus = null;

        // Add all states
        states = new HashMap<>();
        states.put("WaitingForPacket", new WaitingForPacket(this));
        states.put("CheckingPacketType", new CheckingPacketType(this));
        states.put("ProcessingElevatorStatus", new ProcessingElevatorStatus(this));
        states.put("ProcessingFloorRequest", new ProcessingFloorRequest(this));
        states.put("SavingFloorRequest", new SavingFloorRequest(this));
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
     * Handles event where FloorRequest is saved instead of being serviced
     */
    public void floorRequestSaved() {
        setState(currentState.floorRequestSaved());
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

    /**
     * @return the DatagramSocket used for sending packets
     */
    public DatagramSocket getSendSocket() {
        return sendSocket;
    }

    /**
     * @return the DatagramSocket used for receiving packets
     */
    public DatagramSocket getReceiveSocket() {
        return receiveSocket;
    }

    /**
     * Stores/saves the received packet
     * @param packet - the packet to save
     */
    public void saveReceivedPacket(DatagramPacket packet) {
        receivePacket = packet;
    }

    /**
     * @return the DatagramPacket that was received
     */
    public DatagramPacket getReceivedPacket() {
        return receivePacket;
    }

    /**
     * Constructs a FloorRequest object based on the data
     * from the received packet.
     */
    public void constructFloorRequest() {
        // Recreate FloorRequest from received packet
        byte[] floorRequestData = receivePacket.getData();
        floorRequest = new FloorRequest(floorRequestData, floorRequestData.length);
    }

    /**
     * Constructs an ElevatorStatus object based on the data
     * from the received packet.
     */
    public void constructElevatorStatus() {
        // Construct ElevatorStatus from received packet
        byte[] statusData = receivePacket.getData();
        elevatorStatus = new ElevatorStatus(statusData, statusData.length);
    }

    /**
     * @return the (currently received) FloorRequest
     */
    public FloorRequest getFloorRequest() {
        return floorRequest;
    }

    /**
     * @return the (currently received) ElevatorStatus
     */
    public ElevatorStatus getElevatorStatus() {
        return elevatorStatus;
    }

    /**
     * @param elevatorId - the ID of the elevator
     * @return true if the specified elevator has its own task queue,
     * and false otherwise
     */
    public boolean containsElevatorId(int elevatorId) {
        return elevatorTaskQueueHashMap.containsKey(elevatorId);
    }

    /**
     * Adds a new elevator to the Elevator Task Queue hashmap
     * @param elevatorId - the ID of the elevator
     * @param elevatorStatus - the elevator's ElevatorStatus
     * @param elevatorIP - the IP address of the Elevator's receiver
     */
    public void addElevator(int elevatorId, ElevatorStatus elevatorStatus, InetAddress elevatorIP) {
        elevatorTaskQueueHashMap.put(elevatorId, new ElevatorTaskQueue(elevatorStatus, elevatorIP));
    }

    /**
     * Updates the elevator's ElevatorStatus (from the Elevator's Task Queue)
     * @param elevatorId - the ID of the elevator
     * @param elevatorStatus - the elevator's new ElevatorStatus
     */
    public void updateElevatorStatus(int elevatorId, ElevatorStatus elevatorStatus) {
        elevatorTaskQueueHashMap.get(elevatorId).setElevatorStatus(elevatorStatus);
    }

    /**
     * @param elevatorId - the ID of the elevator
     * @return the ElevatorStatus of the specified elevator
     */
    public ElevatorStatus getElevatorStatus(int elevatorId) {
        return elevatorTaskQueueHashMap.get(elevatorId).getElevatorStatus();
    }

    /**
     * Adds a FloorRequest to the specified elevator's task queue
     * @param elevatorId - the ID of the elevator
     * @param floorRequest - the FloorRequest to be added
     */
    public void addFloorRequestToTaskQueue(int elevatorId, FloorRequest floorRequest) {
        elevatorTaskQueueHashMap.get(elevatorId).addFloorRequest(floorRequest);
    }

    /**
     * Adds a FloorRequest to the list of FloorRequests to be served later.
     * @param floorRequest - the FloorRequest to add
     */
    public void addFloorRequestToServe(FloorRequest floorRequest) {
        floorRequestsToServe.add(floorRequest);
    }

    /**
     * @return true if there's at least one FloorRequest
     * that needs to be served, or false otherwise
     */
    public boolean hasFloorRequestsToServe() {
        return !floorRequestsToServe.isEmpty();
    }

    /**
     * @param currentFloor - the current floor that the elevator is on
     * @return the FloorRequest that is closest to the current floor
     */
    public FloorRequest getFloorRequestToServe(int currentFloor) {
        FloorRequest closestFloorRequest = floorRequestsToServe.getFirst();
        for (FloorRequest floorRequest : floorRequestsToServe) {
            // Check if current FloorRequest is closer to currentFloor
            if ((Math.abs(floorRequest.getDestinationFloor() - currentFloor)) < (Math.abs(closestFloorRequest.getDestinationFloor() - currentFloor))) {
                closestFloorRequest = floorRequest;
            }
        }
        floorRequestsToServe.remove(closestFloorRequest);
        return closestFloorRequest;
    }

    /**
     * Sends the target floor to the elevator's receiver.
     */
    public void sendTargetFloor(int elevatorId, int nextFloor) {
        // Get Elevator Task Queue
        ElevatorTaskQueue taskQueue = elevatorTaskQueueHashMap.get(elevatorId);
        String nextFloorStr = String.valueOf(nextFloor);
        // Get port number of elevator car's receiver
        int elevatorReceiverPortNum = getElevatorStatus(elevatorId).getReceiverPortNum();
        try {
            // Send packet
            DatagramPacket sendPacket = new DatagramPacket(nextFloorStr.getBytes(), nextFloorStr.getBytes().length, taskQueue.getElevatorIpAddress(), elevatorReceiverPortNum);
            sendSocket.send(sendPacket);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @return the elevator task queue hashmap
     */
    public HashMap<Integer, ElevatorTaskQueue> getElevatorTaskQueueHashMap() {
        return elevatorTaskQueueHashMap;
    }
}