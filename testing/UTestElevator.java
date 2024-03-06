import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Elevator Class Unit Tests
 * @author Lindsay Dickson, 101160876
 * @version Iteration1
 * @date February 3, 2024
 */

public class UTestElevator {
    Synchronizer synchronizer = new Synchronizer();
    Elevator elevator = new Elevator(synchronizer);
    @Test
    void test_calculateTimeTravelFloor() {

        assertEquals((long) (elevator.DEFAULT_VELOCITY* FloorRequestSimulator.DEFAULT_FLOOR_HEIGHT), elevator.calculateTimeTravelFloor());
    }

    @Test
    void test_toString() {
        assertEquals("Elevator 1", elevator.toString());
    }

}