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
        chooseElevatorForRequest();
        addFloorRequest(0);
    }

    /**
     * Exit actions
     */
    private void exit() {
        sendTargetFloor();
    }

    private void chooseElevatorForRequest() {
        //TODO
        // return elevatorID of selected elevator
    }

    /**
     * Adds the FloorRequest to the queue data structure.
     */
    private void addFloorRequest(int elevatorId) {
        //TODO
    }

    /**
     * Sends the target floor to the elevator's receiver.
     */
    private void sendTargetFloor() {
        //TODO
    }

    /**
     * Handles the packet sent event.
     */
    public SchedulerState packetSent() {
        return schedulerContext.getState("WaitingForPacket");
    }
}
