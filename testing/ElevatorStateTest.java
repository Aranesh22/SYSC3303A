import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ElevatorStateTest {

    /**
     * Elevator State Class Unit Tests
     * @author Aranesh Athavan
     * @author Pathum Danthanarayana, 101181411
     * @version Iteration2
     * @date February 24, 2024
     */
    @Test
    void testElevatorSetState() {
        Synchronizer synchronizer = new Synchronizer();
        Elevator elevator = new Elevator(synchronizer);

        // Set to a random state
        elevator.setState("MovingDoorsClosed");
        assertEquals("MovingDoorsClosed", elevator.getCurrentState().getSTATENAME());
    }

    @Test
    void testElevatorInitialState() {
        Synchronizer synchronizer = new Synchronizer();
        Elevator elevator = new Elevator(synchronizer);

        // See if elevator is at its initial state
        assertEquals(elevator.getCurrentState().getSTATENAME(), "StationaryDoorsClosed");
    }

    @Test
    void testElevatorStateTransitions() {
        Synchronizer synchronizer = new Synchronizer();
        Elevator elevator = new Elevator(synchronizer);

        // Test if states transition as expected
        elevator.receiveRequest();
        assertEquals(elevator.getCurrentState().getSTATENAME(), "MovingDoorsClosed");

        elevator.arriveAtFloor();
        assertEquals(elevator.getCurrentState().getSTATENAME(), "StationaryDoorsOpen");

        elevator.timerExpired();
        assertEquals(elevator.getCurrentState().getSTATENAME(), "StationaryDoorsClosed");
    }

}