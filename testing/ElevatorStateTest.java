import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ElevatorStateTest {

    /**
     * Test case to setTheState
     * Aranesh Athavan
     */
    @Test
    void test_setState() {
        Synchronizer synchronizer = new Synchronizer();
        Elevator elevator = new Elevator(synchronizer);

        ElevatorState elevState = elevator.getCurrentState();
        assertEquals(elevator.getCurrentState().getSTATENAME(), "StationaryDoorsClosed");

    }

}