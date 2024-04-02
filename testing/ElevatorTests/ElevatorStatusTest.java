import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testing for ElevatorStatus class
 *
 * @author Lindsay Dickson 101160876
 * @version Iteration 3
 * @date March 2024
 */
class ElevatorStatusTest {

    ElevatorStatus elevatorStatus;

    int elevatorId, currentFloor, targetFloor, receivePortNum, errorCode, capacity;
    boolean doorsOpened, moving;
    String direction;

    /**
     * Initialize the ElevatorStatus Object and other parameters
     */
    @BeforeEach
    void setUp() {
        elevatorId = 1;
        currentFloor = 1;
        targetFloor = 7;
        receivePortNum = 52054;
        doorsOpened = false;
        moving = false;
        direction = "up";
        errorCode = 0;
        capacity = 1;
        elevatorStatus = new ElevatorStatus(elevatorId, currentFloor, targetFloor, receivePortNum, doorsOpened, moving, direction, 0, capacity);

    }

    /**
     * Unit Test
     * Assert that the ElevatorId is initialized and able to be retrieved
     */
    @Test
    void getElevatorId() {
        assertEquals(elevatorId, elevatorStatus.getElevatorId());
    }

    /**
     * Unit Test
     * Assert that the currentFloor is initialized and able to be retrieved
     */
    @Test
    void getCurrentFloor() {
        assertEquals(currentFloor, elevatorStatus.getCurrentFloor());
    }

    /**
     * Unit Test
     * Assert that the TargetFloor is initialized and able to be retrieved
     */
    @Test
    void getTargetFloor() {
        assertEquals(targetFloor, elevatorStatus.getTargetFloor());
    }

    /**
     * Unit Test
     * Assert that the PortNumber is initialized and able to be retrieved
     */
    @Test
    void getReceiverPortNum() {
        assertEquals(receivePortNum, elevatorStatus.getReceiverPortNum());
    }

    /**
     * Unit Test
     * Assert that the elevator is not moving (false) as initialized
     */
    @Test
    void getMoving() {
        assertFalse(elevatorStatus.getMoving());
    }

    /**
     * Unit Test
     * Assert that the direction of the elevator matches the direction value passed in
     */
    @Test
    void getDirection() {
        assertEquals(direction, elevatorStatus.getDirection());
    }


    /**
     * Unit Test
     * Assert that the string containing the elevator status is correctly turned into bytes with the valid information
     */
    @Test
    void toUdpStringBytes() {
        byte[] udpString = (elevatorId + "," + currentFloor + "," + targetFloor + "," + receivePortNum + "," + doorsOpened + "," + moving + "," + direction + "," + this.errorCode + "," + this.capacity + ",").getBytes();
        assertArrayEquals(udpString, elevatorStatus.toUdpStringBytes());
    }

    /**
     * Unit Test
     * Assert that the toString method has been successfully overridden
     */
    @Test
    void testToString() {
        String str = "Current Floor:" + this.currentFloor + " | Target Floor:" + this.targetFloor + " | PortNum:"
                + this.receivePortNum + " | Direction of Car-->" + this.direction + " | Moving: " + this.moving
                + " | Doors " + ((this.doorsOpened)? "Open" : "Closed" + " | Passenger Count: " + this.capacity +
                " | Error: " + this.errorCode);
        assertEquals(str, elevatorStatus.toString());
    }
}