import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.HashMap;

/**
 * The ProcessingFloorRequest models the state of the Scheduler
 * where the received FloorRequest is processed such that the
 * most suitable elevator is selected to service the request.
 *
 * @author Pathum Danthanarayana, 101181411
 * @date March 11, 2024
 */
public class ProcessingFloorRequest extends SchedulerState {

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
