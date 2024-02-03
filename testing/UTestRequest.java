/**
 * Request Class Unit Tests
 * @version Iteration1
 * @date February 2, 2024
 */

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UTestRequest {

    @Test
    void test_getTime() {
        Request r = new Request("06:10:04:00", 1, 5, "up");
        assertEquals("06:10:04:00", r.getTime());
    }

    @Test
    void test_getStartFloor() {
        Request r = new Request("06:10:04:00", 1, 5, "up");
        assertEquals(1, r.getStartFloor());
    }

    @Test
    void test_getDestinationFloor() {
        Request r = new Request("06:10:04:00", 1, 5, "up");
        assertEquals(5, r.getDestinationFloor());
    }

    @Test
    void test_getDirection() {
        Request r = new Request("06:10:04:00", 1, 5, "up");
        assertEquals("up", r.getDirection());
    }

}