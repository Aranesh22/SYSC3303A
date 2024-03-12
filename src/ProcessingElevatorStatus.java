/**
 * The ProcessingElevatorStatus class models the state of the Scheduler
 * where the received ElevatorStatus is sent to the Floor subsystem, and
 * the Scheduler's queue is updated accordingly.
 *
 * @author Pathum Danthanarayana, 101181411
 * @date March 11, 2024
 */

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class ProcessingElevatorStatus extends SchedulerState {

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
