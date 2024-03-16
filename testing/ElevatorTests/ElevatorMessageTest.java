import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit testing for the ElevatorMessage Class
 *
 * @author Lindsay Dickson 1011
 * @version Iteration 3
 * @date March 2024
 */

class ElevatorMessageTest {
    ElevatorMessage elevatorMessage;
    int portNum;
    int targetFloor;

    /**
     * Initialize the elevatorMessage object, PortNumber and Target floor at the start of each test
     */
    @BeforeEach
    void setUp(){
        portNum = 52054;
        targetFloor = 6;
        elevatorMessage = new ElevatorMessage(portNum, targetFloor);
    }

    /**
     * Unit Test
     * Ensures that the port number is correctly initalized
     */
    @Test
    void getElevatorReceiverPortNum() {
        assertEquals(portNum, elevatorMessage.getElevatorReceiverPortNum());
    }

    /**
     * Unit Test
     * Ensures that the target floor is correctly initalized
     */
    @Test
    void getTargetFloor() {
        assertEquals(targetFloor, elevatorMessage.getTargetFloor());
    }
}