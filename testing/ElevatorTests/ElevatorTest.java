//package ElevatorTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Still working on
 *
 * Elevator Class Unit Tests
 * @author Lindsay Dickson, 101160876
 * @version Iteration 3
 * @date March 2024
 */

public class ElevatorTest {
    ElevatorRequestBox elevatorRequestBox;
    Elevator elevator;
//    Main m;

    @BeforeEach
    void initalize() {
        elevator = new Elevator(new ElevatorRequestBox(), 1);

    }
    @Test
    void test_setState() {

//
//        elevator.setState("StationaryDoorsClosed");
//
//        assertEquals("StationaryDoorsClosed", elevator.getState());



    }
    @Test
    void test_calculateTimeTravelFloor() {
//        assertEquals((long) (elevator.DEFAULT_VELOCITY* FloorSubsystem.DEFAULT_FLOOR_HEIGHT), elevator.calculateTimeTravelFloor());
    }

    @Test
    void test_toString() {
        assertEquals("Elevator 1", elevator.toString());
    }

}