import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Testing for FloorRequest Class
 *
 * @author Lindsay Dickson 101160876
 * @version Iteration 3
 * @date March 2024
 */

class FloorRequestTest {
    FloorRequest floorRequest;
    String udpString;

    String time = "14:05:15";
    int startFloor = 2;
    int destinationFloor = 7;
    String direction = "up";

    int errorCode = 0;

    @BeforeEach
    void setUp() {
       udpString = "14:05:15,2,7,up,0";
       floorRequest = new FloorRequest(udpString);
    }

    /**
     * Unit Test
     * Assert that correct time is returned
     */
    @Test
    void getTime() {
        assertEquals(time, floorRequest.getTime());
    }

    /**
     * Unit Test
     * Assert that the correct start floor is returned
     */
    @Test
    void getStartFloor() {
        assertEquals(startFloor, floorRequest.getStartFloor());
    }


    /**
     * Unit Test
     * Assert that the correct destination floor is returned
     */
    @Test
    void getDestinationFloor() {
        assertEquals(destinationFloor, floorRequest.getDestinationFloor());
    }

    /**
     * Unit Test
     * Assert that the correct direction is returned
     */
    @Test
    void getDirection() {
        assertEquals(direction, floorRequest.getDirection());
    }

    /**
     * Unit test
     * Asserts that the udpString is corerctly created
     */
    @Test
    void toUdpStringBytes() {
        byte[] udpStr = (time + "," + startFloor + "," + destinationFloor+ "," + direction + "," + errorCode + ",").getBytes();
        assertArrayEquals(udpStr, floorRequest.toUdpStringBytes());
    }

    /**
     * Unit test
     * Test the overridden toString method
     */
    @Test
    void testToString() {
        String testString = "Time:"+ time +" | FloorRequest:"+ startFloor +"->"+ destinationFloor + " Injected Error Code: " + errorCode;
        assertEquals(testString, floorRequest.toString());
    }
}