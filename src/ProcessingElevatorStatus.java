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
        updateElevatorInfo();
        schedulerContext.packetSent();
    }

    /**
     * Checks whether the scheduler has received a message from
     * the Elevator before, and based on this, performs the appropriate
     * actions
     */
    private void checkedReceivedPriorMessage() {
        //TODO
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
     * Updates the scheduler's queue of elevator data.
     */
    private void updateElevatorInfo() {
        //TODO
    }

    /**
     * Handles the Packet sent event.
     */
    public SchedulerState packetSent() {
        // Return next state
        return schedulerContext.getState("WaitingForPacket");
    }
}
