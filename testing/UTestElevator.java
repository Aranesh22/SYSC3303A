/**
 * Elevator Class Unit Tests
 * @version Iteration1
 * @date February 3, 2024
 */
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UTestElevator {
    Synchronizer synchronizer = new Synchronizer();
    Elevator elevator = new Elevator(synchronizer);

    /**
     * Testing Elevator Class method: calculateTimeTravelFloor()
     */
    @Test
    void test_calculateTimeTravelFloor() {

        assertEquals((long) (elevator.DEFAULT_VELOCITY*Floor.DEFAULT_FLOOR_HEIGHT), elevator.calculateTimeTravelFloor());
    }

    /**
     * Testing Elevator Class method: toString()
     */
    @Test
    void test_toString() {
        assertEquals("Elevator 1", elevator.toString());
    }

}