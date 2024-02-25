/**
 * The SchedulerState interface defines the methods that handle the state transitions in the Scheduler.
 * These methods are invoked when the Scheduler receives a new floor request, an elevator status update, or an end signal.
 *
 * @author Harishan Amutheesan, 101154757
 * @version Iteration2
 * @date Feb 22, 2024
 *
 */
public interface SchedulerState {

    /**
     * Handles the event of receiving a new floor request from a floor.
     *
     * @param context The Scheduler context in which the state transition occurs.
     */
    void receivedFloorRequest(Scheduler context);

    /**
     * Handles the event of receiving an elevator status update.
     *
     * @param context The Scheduler context in which the state transition occurs.
     */
    void receivedElevatorStatus(Scheduler context);

    /**
     * Handles the event of receiving an end signal from the floor request.
     *
     * @param context The Scheduler context in which the state transition occurs.
     */
    void endReceived(Scheduler context);

    /**
     * Get the name of the state
     */
    String getStateName();
}

/**
 * Concrete state class representing when the Scheduler is waiting for a floor request.
 * In this state, the Scheduler can transition to the SendingElevatorToStartingFloor state when a valid floor request is received.
 */
class WaitingForFloorRequest implements SchedulerState {
    private final String stateName = "WaitingForFloorRequest";
    @Override
    public void receivedFloorRequest(Scheduler context) {
        // Transition to the SendingElevatorToStartingFloor state when a floor request is received.
        context.setState("SendingElevatorToStartingFloor");
    }

    @Override
    public void receivedElevatorStatus(Scheduler context) {
        // No state transition is needed when an elevator status update is received in this state.
    }

    @Override
    public void endReceived(Scheduler context) {
        // Final State is reached when an end signal is received in this state.
    }

    /**
     * @return name of state
     */
    public String getStateName() {
        return stateName;
    }
}

/**
 * Concrete state class representing when the Scheduler is sending the elevator to the starting floor.
 * In this state, the Scheduler can transition to the SendingElevatorToDestinationFloor state when the elevator reaches the starting floor.
 */
class SendingElevatorToStartingFloor implements SchedulerState {
    private final String stateName = "SendingElevatorToStartingFloor";
    @Override
    public void receivedFloorRequest(Scheduler context) {
        // No state transition is needed when a floor request is received in this state.
    }

    @Override
    public void receivedElevatorStatus(Scheduler context) {
        // Transition to the SendingElevatorToDestinationFloor state when the elevator reaches the starting floor.
        context.setState("SendingElevatorToDestinationFloor");
    }

    @Override
    public void endReceived(Scheduler context) {
        // No state transition is needed when an end signal is received in this state.
    }

    /**
     * @return the name of the state.
     */
    public String getStateName() {
        return stateName;
    }
}

/**
 * Concrete state class representing when the Scheduler is sending the elevator to the destination floor.
 * In this state, the Scheduler can transition to the WaitingForFloorRequest state when the elevator reaches the destination floor.
 */
class SendingElevatorToDestinationFloor implements SchedulerState {
    private final String stateName = "SendingElevatorToDestinationFloor";
    @Override
    public void receivedFloorRequest(Scheduler context) {
        // No state transition is needed when a floor request is received in this state.
    }

    @Override
    public void receivedElevatorStatus(Scheduler context) {
        // Transition to the WaitingForFloorRequest state when the elevator reaches the destination floor.
        context.setState("WaitingForFloorRequest");
    }

    @Override
    public void endReceived(Scheduler context) {
        // No state transition is needed when an end signal is received in this state.
    }

    /**
     * @return the name of the state.
     */
    public String getStateName() {
        return stateName;
    }
}