import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.HashMap;

/**
 * SchedulerState is an interface that defines the state of the Scheduler.
 * @author Pathum Danthanarayana, 101181411
 * @version Iteration 3
 * @date March 11, 2024
 *
 */
public abstract class SchedulerState {
    protected Scheduler schedulerContext;

    /**
     * Constructor
     */
    protected SchedulerState(Scheduler schedulerContext) {
        this.schedulerContext = schedulerContext;
    }

    /**
     * Starts the state.
     */
    void start() {
        // Empty by default
    }

    /**
     * Handles event where a packet is received to the Scheduler's socket
     */
    SchedulerState packetReceived() {
        System.out.println("(Scheduler's current state does not handle this event)");
        return null;
    }

    /**
     * Handles event where the received packet was an ElevatorStatus (from ElevatorCar)
     */
    SchedulerState elevatorStatusReceived() {
        System.out.println("(Scheduler's current state does not handle this event)");
        return null;
    }

    /**
     * Handles event where the received packet was a FloorRequest (from Floor subsystem)
     */
    SchedulerState floorRequestReceived() {
        System.out.println("(Scheduler's current state does not handle this event)");
        return null;
    }

    /**
     * Handles the event where either the FloorRequest or ElevatorStatus was sent to its destination.
     */
    SchedulerState packetSent() {
        System.out.println("(Scheduler's current state does not handle this event)");
        return null;
    }

    /**
     * Displays info about the current state
     */
    void displayState() {
        // Empty by default
    }
}

/**
 * The WaitingForPacket class models the state in the Scheduler
 * where the scheduler waits indefinitely until a UDP packet is
 * received by its socket (either a FloorRequest or ElevatorStatus).
 *
 * @author Pathum Danthanarayana, 101181411
 * @date March 11, 2024
 */
class WaitingForPacket extends SchedulerState {

    /**
     * Constructor
     *
     * @param schedulerContext - context
     */
    protected WaitingForPacket(Scheduler schedulerContext) {
        super(schedulerContext);
    }

    /**
     * Start the state
     */
    public void start() {
        entry();
    }

    /**
     * Entry actions
     */
    private void entry() {
        waitForPacket();
    }

    /**
     * Waits for a packet to arrive at it's receive socket
     */
    private void waitForPacket() {
        try {
            // Wait to receive packet
            byte[] data = new byte[100];
            DatagramSocket receiveSocket = schedulerContext.getReceiveSocket();
            DatagramPacket packet = new DatagramPacket(data, data.length);
            receiveSocket.receive(packet);
            // Save received packet
            schedulerContext.saveReceivedPacket(packet);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // Generate Packet Received event
        schedulerContext.packetReceived();
    }

    /**
     * Packet received event handler
     */
    public SchedulerState packetReceived() {
        // Return next state
        return schedulerContext.getState("CheckingPacketType");
    }
}

/**
 * The CheckingPacketType class models the state of the Scheduler
 * where the received packet is checked to determine whether it is
 * a FloorRequest (from Floor Subsystem) or an ElevatorStatus (from ElevatorCar).
 *
 * @author Pathum Danthanarayana, 101181411
 * @date March 11, 2024
 */
class CheckingPacketType extends SchedulerState {

    /**
     * Constructor
     *
     * @param schedulerContext - context
     */
    protected CheckingPacketType(Scheduler schedulerContext) {
        super(schedulerContext);
    }

    /**
     * Start the state.
     */
    public void start() {
        doActivities();
    }

    /**
     * Do activities
     */
    public void doActivities() {
        checkPacketType();
    }

    /**
     * Checks whether the received packet is a
     * FloorRequest or ElevatorStatus
     */
    private void checkPacketType() {
        DatagramPacket receivedPacket = schedulerContext.getReceivedPacket();
        if (receivedPacket.getPort() == FloorSubsystem.FLOOR_SUBSYSTEM_PORT) {
            // Received packet is a FloorRequest
            schedulerContext.floorRequestReceived();
        }
        else {
            // Received packet is an ElevatorStatus
            schedulerContext.elevatorStatusReceived();
        }
    }

    /**
     * Handles the FloorRequest received event
     */
    public SchedulerState floorRequestReceived() {
        // Return next state
        return schedulerContext.getState("ProcessingFloorRequest");
    }

    /**
     * Handles the ElevatorStatus received event.
     */
    public SchedulerState elevatorStatusReceived() {
        // Return next state
        return schedulerContext.getState("ProcessingElevatorStatus");
    }
}


/**
 * The ProcessingFloorRequest models the state of the Scheduler
 * where the received FloorRequest is processed such that the
 * most suitable elevator is selected to service the request.
 *
 * @author Pathum Danthanarayana, 101181411
 * @date March 11, 2024
 */
class ProcessingFloorRequest extends SchedulerState {

    /**
     * Constructor
     *
     * @param schedulerContext - context
     */
    protected ProcessingFloorRequest(Scheduler schedulerContext) {
        super(schedulerContext);
    }

    /**
     * Start the state
     */
    public void start() {
        doActivities();
        exit();
    }

    /**
     * Do activities
     */
    private void doActivities() {
        int suitableElevator = chooseElevatorForRequest();
        sendTargetFloor(suitableElevator);
    }

    /**
     * Exit actions
     */
    private void exit() {
        schedulerContext.packetSent();
    }

    private int chooseElevatorForRequest() {
        // Recreate FloorRequest
        byte[] floorRequestData = schedulerContext.getReceivedPacket().getData();
        FloorRequest floorRequest = new FloorRequest(floorRequestData, floorRequestData.length);

        HashMap<Integer, ElevatorTaskQueue> elevatorTaskQueueHashMap = schedulerContext.getElevatorTaskQueueHashMap();
        HashMap<Integer, Double> suitableElevators = new HashMap<>();
        // Iterate through each entry
        for (Integer elevatorId : elevatorTaskQueueHashMap.keySet()) {
            ElevatorStatus elevatorStatus = elevatorTaskQueueHashMap.get(elevatorId).getElevatorStatus();
            // Check if elevator is not moving
            if (!elevatorStatus.getMoving()) {
                suitableElevators.put(elevatorId, (double) Math.abs(elevatorStatus.getCurrentFloor() - floorRequest.getStartFloor()));
            }
            // Check if elevator is moving in same direction
            if (floorRequest.getDirection() == elevatorStatus.getDirection()) {
                // Compute floor difference and add to map
                suitableElevators.put(elevatorId, Math.abs(elevatorStatus.getCurrentFloor() - floorRequest.getStartFloor() - 0.5));
            }
        }
        // Check if no elevators suitable
        if (suitableElevators.isEmpty()) {
            for (Integer elevatorId: elevatorTaskQueueHashMap.keySet()) {
                ElevatorStatus elevatorStatus = elevatorTaskQueueHashMap.get(elevatorId).getElevatorStatus();
                suitableElevators.put(elevatorId, Math.abs(elevatorStatus.getCurrentFloor() - floorRequest.getStartFloor() + 0.5));
            }
        }
        // Add FloorRequest to suitable elevator's task queue
        int suitableElevator = getClosestElevator(suitableElevators);
        schedulerContext.addFloorRequestToTaskQueue(suitableElevator, floorRequest);
        return suitableElevator;
    }

    private int getClosestElevator(HashMap<Integer, Double> suitableElevators) {
        // Iterate through each elevator
        int suitableElevator = 0;
        double minFloorDistance = FloorSubsystem.DEFAULT_MAX_FLOOR - FloorSubsystem.DEFAULT_MIN_FLOOR;
        for (Integer elevatorId : suitableElevators.keySet()) {
            double floorDistance = suitableElevators.get(elevatorId);
            if (floorDistance < minFloorDistance) {
                suitableElevator = elevatorId;
                minFloorDistance = floorDistance;
            }
        }
        return suitableElevator;
    }

    /**
     * Sends the target floor to the elevator's receiver.
     */
    private void sendTargetFloor(int elevatorId) {
        // Get Elevator Task Queue
        ElevatorTaskQueue taskQueue = schedulerContext.getElevatorTaskQueueHashMap().get(elevatorId);
        String nextFloorStr = String.valueOf(taskQueue.nextFloorToVisit());

        // Get port number of elevator car's receiver
        int elevatorReceiverPortNum = schedulerContext.getElevatorStatus(elevatorId).getReceiverPortNum();
        try {
            // Send packet
            DatagramSocket sendSocket = schedulerContext.getSendSocket();
            DatagramPacket sendPacket = new DatagramPacket(nextFloorStr.getBytes(), nextFloorStr.getBytes().length, taskQueue.getElevatorIpAddress(), elevatorReceiverPortNum);
            sendSocket.send(sendPacket);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Handles the packet sent event.
     */
    public SchedulerState packetSent() {
        return schedulerContext.getState("WaitingForPacket");
    }
}

/**
 * The ProcessingElevatorStatus class models the state of the Scheduler
 * where the received ElevatorStatus is sent to the Floor subsystem, and
 * the Scheduler's queue is updated accordingly.
 *
 * @author Pathum Danthanarayana, 101181411
 * @date March 11, 2024
 */
class ProcessingElevatorStatus extends SchedulerState {

    /**
     * Constructor
     *
     * @param schedulerContext - context
     */
    protected ProcessingElevatorStatus(Scheduler schedulerContext) {
        super(schedulerContext);
    }

    /**
     * Start the state.
     */
    public void start() {
        entry();
        doActivities();
        exit();
    }

    /**
     * Entry actions
     */
    private void entry() {
        checkedReceivedPriorMessage();
    }

    /**
     * Do activities
     */
    private void doActivities() {
        sendElevatorStatus();
    }

    /**
     * Exit actions
     */
    private void exit() {
        schedulerContext.packetSent();
    }

    /**
     * Checks whether the scheduler has received a message from
     * the Elevator before, and based on this, performs the appropriate
     * actions
     */
    private void checkedReceivedPriorMessage() {
        // Construct ElevatorStatus from received packet
        byte[] statusData = schedulerContext.getReceivedPacket().getData();
        ElevatorStatus elevatorStatus = new ElevatorStatus(statusData, statusData.length);

        // Check if there's already an elevator task queue for the elevator
        if (!schedulerContext.containsElevatorId(elevatorStatus.getElevatorId())) {
            // If not, create elevator task queue for the elevator
            schedulerContext.addElevator(elevatorStatus.getElevatorId(), elevatorStatus, schedulerContext.getReceivedPacket().getAddress());
        }
        else {
            // Update the elevator's elevator status
            schedulerContext.updateElevatorStatus(elevatorStatus.getElevatorId(), elevatorStatus);
        }
    }

    /**
     * Sends the ElevatorStatus to the Floor subsystem.
     */
    private void sendElevatorStatus() {
        try {
            DatagramSocket sendSocket = schedulerContext.getSendSocket();
            byte[] statusData = schedulerContext.getReceivedPacket().getData();
            DatagramPacket statusPacket = new DatagramPacket(statusData, statusData.length, FloorSubsystem.FLOOR_SUBSYSTEM_IP, FloorSubsystem.FLOOR_SUBSYSTEM_PORT);
            // Send the packet to the Floor subsystem
            sendSocket.send(statusPacket);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Handles the Packet sent event.
     */
    public SchedulerState packetSent() {
        // Return next state
        return schedulerContext.getState("WaitingForPacket");
    }
}



