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
     * Handles event where the received FloorRequest is saved to be serviced at a later time
     */
    SchedulerState floorRequestSaved() {
        System.out.println("(Scheduler's current state does not handle this event");
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

    /**
     * Displays current state.
     */
    public void displayState() {
        System.out.println("[SCHEDULER STATE] Waiting to receive packet...");
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
            schedulerContext.constructFloorRequest();
            schedulerContext.floorRequestReceived();
        }
        else {
            // Received packet is an ElevatorStatus
            schedulerContext.constructElevatorStatus();
            schedulerContext.elevatorStatusReceived();
        }
    }

    /**
     * Handles the FloorRequest received event
     */
    public SchedulerState floorRequestReceived() {
        // Return next state
        return enterJunctionPoint();
    }

    private SchedulerState enterJunctionPoint() {
        // Check guard condition for entering ProcessingFloorRequest state
        if (suitableElevatorExists()) {
            return schedulerContext.getState("ProcessingFloorRequest");
        }
        // If no suitable elevator, save FloorRequest
        return schedulerContext.getState("SavingFloorRequest");
    }

    /**
     * Handles the ElevatorStatus received event.
     */
    public SchedulerState elevatorStatusReceived() {
        // Return next state
        return schedulerContext.getState("ProcessingElevatorStatus");
    }

    /**
     * @return true if there is at least 1 elevator
     * that is suitable to service the FloorRequest.
     * Otherwise, return false.
     */
    private boolean suitableElevatorExists() {
        // Iterate through all elevators
        FloorRequest floorRequest = schedulerContext.getFloorRequest();
        HashMap<Integer, ElevatorTaskQueue> elevatorTaskQueueHashMap = schedulerContext.getElevatorTaskQueueHashMap();
        for (Integer elevatorId : elevatorTaskQueueHashMap.keySet()) {
            ElevatorStatus elevatorStatus = elevatorTaskQueueHashMap.get(elevatorId).getElevatorStatus();
            // Criteria: Elevator is stationary or is moving in same direction
            if (!elevatorStatus.getMoving() || floorRequest.getDirection().equals(elevatorStatus.getDirection())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Displays current state.
     */
    public void displayState() {
        System.out.println("[SCHEDULER STATE] Checking type of received packet");
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
        selectElevatorForRequest();
    }

    /**
     * Exit actions
     */
    private void exit() {
        // Generate Packet sent event
        schedulerContext.packetSent();
    }

    private void selectElevatorForRequest() {
        // Get closest elevator
        int closestElevator = getClosestElevator(getSuitableElevators());
        // Assign them the FloorRequest
        schedulerContext.addFloorRequestToTaskQueue(closestElevator, schedulerContext.getFloorRequest());
        // Send the target floor to the elevator
        schedulerContext.sendTargetFloor(closestElevator, schedulerContext.getElevatorTaskQueueHashMap().get(closestElevator).nextFloorToVisit());
    }

    private HashMap<Integer, Double> getSuitableElevators() {
        // Select suitable elevators
        FloorRequest floorRequest = schedulerContext.getFloorRequest();
        HashMap<Integer, ElevatorTaskQueue> elevatorTaskQueueHashMap = schedulerContext.getElevatorTaskQueueHashMap();
        HashMap<Integer, Double> suitableElevators = new HashMap<>();
        for (Integer elevatorId : elevatorTaskQueueHashMap.keySet()) {
            ElevatorStatus elevatorStatus = elevatorTaskQueueHashMap.get(elevatorId).getElevatorStatus();
            // Criteria 1: Elevator is stationary
            if (!elevatorStatus.getMoving()) {
                // Compute floor difference and add to map
                suitableElevators.put(elevatorId, (double) Math.abs(elevatorStatus.getCurrentFloor() - floorRequest.getStartFloor()));
            }
            // Criteria 2: Elevator is moving in same direction as the floor request AND
            else if (floorRequest.getDirection().equals(elevatorStatus.getDirection())) {
                boolean ascending = elevatorStatus.getDirection().equals("up");
                // Criteria 3: Elevator is currently moving towards the floor request
                if ((ascending && (floorRequest.getStartFloor() > elevatorStatus.getCurrentFloor())) || (!ascending && (floorRequest.getStartFloor() < elevatorStatus.getCurrentFloor()))) {
                    // Compute floor difference and add to map
                    suitableElevators.put(elevatorId, Math.abs(elevatorStatus.getCurrentFloor() - floorRequest.getStartFloor() - 0.5));
                }
            }
        }
        return suitableElevators;
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
     * Handles the packet sent event.
     */
    public SchedulerState packetSent() {
        return schedulerContext.getState("WaitingForPacket");
    }

    /**
     * Displays current state.
     */
    public void displayState() {
        System.out.println("[SCHEDULER STATE] Processing the received FloorRequest");
    }
}


/**
 * The SavingFloorRequest models the state of the Scheduler
 * where no elevators are suitable to service the received
 * FloorRequest. In this case, the FloorRequest is saved
 * to be serviced by an elevator at a later time.
 *
 * @author Pathum Danthanarayana, 101181411
 * @date March 13, 2024
 */
class SavingFloorRequest extends SchedulerState {

    /**
     * Constructor
     *
     * @param schedulerContext - context
     */
    protected SavingFloorRequest(Scheduler schedulerContext) {
        super(schedulerContext);
    }

    /**
     * Start the state.
     */
    public void start() {
        doActivities();
        exit();
    }

    /**
     * Do activities
     */
    private void doActivities() {
        saveFloorRequest();
    }

    /**
     * Exit actions
     */
    private void exit() {
        // Generate FloorRequest saved event
        schedulerContext.floorRequestSaved();
    }

    /**
     * Saves the FloorRequest in context so that it can be
     * serviced at a later time
     */
    private void saveFloorRequest() {
        // Save the FloorRequest to be served at a later time
        schedulerContext.addFloorRequestToServe(schedulerContext.getFloorRequest());
    }

    /**
     * Handles the FloorRequest saved event
     * @return the next state
     */
    public SchedulerState floorRequestSaved() {
        return schedulerContext.getState("WaitingForPacket");
    }

    /**
     * Displays current state.
     */
    public void displayState() {
        System.out.println("[SCHEDULER STATE] Saving the FloorRequest to be serviced at a later time (no suitable elevators found)");
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
        sendElevatorStatusToFloor();
        processMessage();
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
        ElevatorStatus elevatorStatus = schedulerContext.getElevatorStatus();
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
     * Processes the received elevator status message.
     */
    private void processMessage() {
        ElevatorStatus elevatorStatus = schedulerContext.getElevatorStatus();
        int errorCode = elevatorStatus.getErrorCode();
        if (errorCode == 1) {
            System.out.println("Scheduler: " + "Elevator " + elevatorStatus.getElevatorId() + " doors stuck");
        } else if (errorCode == 2) {
            System.out.println("Scheduler: " + "Elevator " + elevatorStatus.getElevatorId() + " stuck, closing elevator");
            schedulerContext.removeElevator(elevatorStatus.getElevatorId());
        } else {
            checkElevatorDoorsOpened();
        }
    }

    /**
     * Checks if the elevator's doors are open (meaning the elevator
     * has finished serving its current request. This means that the
     * Scheduler should look if the elevator has a next floor to visit,
     * and if so, should send it to the elevator's receiver (so that
     * the elevator can begin serving its next request).
     * If the elevator has no more floors to visit, the Scheduler will
     * try and assign it a previously received floor request to serve.
     * try and assign it a previously received floor request to serve.
     */
    private void checkElevatorDoorsOpened() {
        // Check if the elevator has reached its target floor
        ElevatorStatus elevatorStatus = schedulerContext.getElevatorStatus();
        ElevatorTaskQueue taskQueue = schedulerContext.getElevatorTaskQueueHashMap().get(elevatorStatus.getElevatorId());
        // If the elevator's doors have opened, it needs a new command from the scheduler
        if (elevatorStatus.getDoorsOpened()) {
            taskQueue.nextFloorVisited();
            // Set the next floor (if it's scheduled to visit a floor)
            int nextFloorToVisit = taskQueue.nextFloorToVisit();
            if (nextFloorToVisit != 0) {
                // Send elevator to next floor
                schedulerContext.sendTargetFloor(elevatorStatus.getElevatorId(), nextFloorToVisit);
            }
            else {
                // Check if there's at least 1 FloorRequest waiting to be served
                if (schedulerContext.hasFloorRequestsToServe()) {
                    FloorRequest nextFloorRequest = schedulerContext.getFloorRequestToServe(elevatorStatus.getCurrentFloor());
                    // If the elevator is currently on the start floor, it only needs to be sent to the destination floor
                    if (elevatorStatus.getCurrentFloor() == nextFloorRequest.getStartFloor()) {
                        taskQueue.addFloorToVisit(nextFloorRequest.getDestinationFloor(), nextFloorRequest.getDirection());
                    } else {
                        taskQueue.addFloorRequest(nextFloorRequest);
                    }
                    // Send elevator to next floor
                    schedulerContext.sendTargetFloor(elevatorStatus.getElevatorId(), taskQueue.nextFloorToVisit());
                }
            }
        }
    }

    /**
     * Sends the ElevatorStatus to the Floor subsystem.
     */
    private void sendElevatorStatusToFloor() {
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

    /**
     * Displays current state.
     */
    public void displayState() {
        System.out.println("[SCHEDULER STATE] Processing the received ElevatorStatus");
    }
}
