import org.junit.jupiter.api.Test;

class ElevatorStateTest {

    /**
     * Still wokring on
     * Elevator State Class Unit Tests
     * @author Aranesh Athavan
     * @author Pathum Danthanarayana, 101181411
     * @version Iteration2
     * @date February 24, 2024
     */
    @Test
    void testElevatorSetState() {
//        Synchronizer synchronizer = new Synchronizer();
        Elevator elevator = new Elevator(new ElevatorRequestBox(), 1);

//System.out.println(elevator.getState());
        // Set to a random state
//        elevator.setState("MovingDoorsClosed");
//        System.out.println(elevator.getState());
//
//
////        assertEquals("MovingDoorsClosed", elevator.getState());
//        assertEquals("MovingDoorsClosed", elevator.getState());


    }

    @Test
    synchronized void testElevatorInitialState() {
//        Elevator elevator = new Elevator(new ElevatorRequestBox(), 1);
//        // See if elevator is at its initial state
//        elevator.setState("StationaryDoorsClosed");
//        System.out.println(elevator.getState().getClass().getSimpleName());
//        assertEquals("StationaryDoorsClosed", elevator.getState().getClass().getSimpleName());

    }

    @Test
    void testElevatorStateTransitions() {

//        Synchronizer synchronizer = new Synchronizer();
//        Elevator elevator = new Elevator(synchronizer);
        Elevator elevator = new Elevator(new ElevatorRequestBox(), 1);

        // Test if states transition as expected
//        elevator.receiveRequest();
////        assertEquals(elevator.getCurrentState().getSTATENAME(), "MovingDoorsClosed");
//
//        elevator.arriveAtFloor();
//        assertEquals(elevator.getCurrentState().getSTATENAME(), "StationaryDoorsOpen");
//
//        elevator.timerExpired();
//        assertEquals(elevator.getCurrentState().getSTATENAME(), "StationaryDoorsClosed");
    }

}