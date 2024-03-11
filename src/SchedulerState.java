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

