import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Scheduler State Class Unit Tests
 * @author Pathum Danthanarayana, 101181411
 * @version Iteration2
 * @date February 24, 2024
 */

public class SchedulerStateTest {

    @Test
    void testSynchronizerSetState() {
        Synchronizer synchronizer = new Synchronizer();
        Scheduler scheduler = new Scheduler(synchronizer);

        // Set to a random state
        scheduler.setState("SendingElevatorToDestinationFloor");
        assertEquals("SendingElevatorToDestinationFloor", scheduler.getCurrentState().getStateName());
    }

    @Test
    void testSynchronizerInitialState() {
        Synchronizer synchronizer = new Synchronizer();
        Scheduler scheduler = new Scheduler(synchronizer);

        // See if scheduler is at its initial state
        assertEquals(scheduler.getCurrentState().getStateName(), "WaitingForFloorRequest");
    }

    @Test
    void testSynchronizerStateTransitions() {
        Synchronizer synchronizer = new Synchronizer();
        Scheduler scheduler = new Scheduler(synchronizer);

        scheduler.receivedFloorRequest(new Request());
        assertEquals(scheduler.getCurrentState().getStateName(), "SendingElevatorToStartingFloor");

        scheduler.receivedElevatorStatus();
        assertEquals(scheduler.getCurrentState().getStateName(), "SendingElevatorToDestinationFloor");

        scheduler.receivedElevatorStatus();
        assertEquals(scheduler.getCurrentState().getStateName(), "WaitingForFloorRequest");
    }

}