import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testing for ElevatorRequestBox class
 *
 * @author Lindsay Dickson 101160876
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

    /**
     * Assert that after calling putRequest that the box is no longer empty
     */
    @Test
    void putRequest() {
        assertEquals(true, elevatorRequestBox.isEmpty());
        elevatorRequestBox.putRequest(new ElevatorMessage(52054));
        assertEquals(false, elevatorRequestBox.isEmpty());
    }

    /**
     * Test that the getRequest returns an initialized ElevatorMessage
     */
    @Test
    void getRequest() {
        ElevatorMessage elevatorMessage = null;
        assertNull(elevatorMessage);
        //put something in the box
        elevatorRequestBox.putRequest(new ElevatorMessage(52054));
        //set the originally null elevatorMessage to the new elevatorMessage
        elevatorMessage = elevatorRequestBox.getRequest();
        assertNotNull(elevatorMessage);
    }

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