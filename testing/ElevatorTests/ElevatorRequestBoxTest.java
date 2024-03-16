import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testing for ElevatorRequestBox class
 *
 * @author Lindsay
 * @version Iteration 3
 * @date March 2024
 */

class ElevatorRequestBoxTest {

    ElevatorRequestBox elevatorRequestBox;
//    ElevatorMessage elevatorMessage;

    /**
     * Initalize the elevatorRequestBox Object
     */
    @BeforeEach
    void setUp() {
        elevatorRequestBox = new ElevatorRequestBox();
//        elevatorMessage = new ElevatorMessage(52054);

    }

    @Test
    void putRequest() { }
    @Test
    void getRequest() { }

    /**
     * Test that the elevator Request box is first empty  (true)
     * then put a request
     * Test that the elevator Request box is not empty  (false)
     */
    @Test
    void isEmpty() {
        assertEquals(true, elevatorRequestBox.isEmpty());
        elevatorRequestBox.putRequest(new ElevatorMessage(52054));
        assertEquals(false, elevatorRequestBox.isEmpty());
    }
}