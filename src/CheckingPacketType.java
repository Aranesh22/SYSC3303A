import java.net.DatagramPacket;

/**
 * The CheckingPacketType class models the state of the Scheduler
 * where the received packet is checked to determine whether it is
 * a FloorRequest (from Floor Subsystem) or an ElevatorStatus (from ElevatorCar).
 *
 * @author Pathum Danthanarayana, 101181411
 * @date March 11, 2024
 */
public class CheckingPacketType extends SchedulerState {

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
