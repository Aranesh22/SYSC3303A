/**
 * Request Class Unit Tests
 * @version Iteration1
 * @date February 2, 2024
 */

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UTestRequest {

    @Test
    void testRequest_getTime() {
        Request r = new Request("06:10:04:00", 1, 5, "up");
        assertEquals("06:10:04:00", r.getTime());
    }

    @Test
    void testRequest_getStartFloor() {
        Request r = new Request("06:10:04:00", 1, 5, "up");
        assertEquals(1, r.getStartFloor());
    }

    @Test
    void testRequest_getDestinationFloor() {
        Request r = new Request("06:10:04:00", 1, 5, "up");
        assertEquals(5, r.getDestinationFloor());
    }

    @Test
    void testRequest_getDirection() {
        Request r = new Request("06:10:04:00", 1, 5, "up");
        assertEquals("up", r.getDirection());
    }

    @Test
    void testRequest_getElevatorId(){
        Request r = new Request("06:10:04:00", 1, 5, "up");
        r.updateElevatorId(1);
        assertEquals(1, r.getElevatorId());
    }
}