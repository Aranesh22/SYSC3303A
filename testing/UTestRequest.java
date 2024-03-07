/**
 * FloorRequest Class Unit Tests
 * @author Lindsay Dickson, 101160876
 * @version Iteration1
 * @date February 2, 2024
 */

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UTestRequest {

    @Test
    void test_getTime() {
        FloorRequest r = new FloorRequest("06:10:04:00", 1, 5, "up");
        assertEquals("06:10:04:00", r.getTime());
    }

    @Test
    void test_getStartFloor() {
        FloorRequest r = new FloorRequest("06:10:04:00", 1, 5, "up");
        assertEquals(1, r.getStartFloor());
    }

    @Test
    void test_getDestinationFloor() {
        FloorRequest r = new FloorRequest("06:10:04:00", 1, 5, "up");
        assertEquals(5, r.getDestinationFloor());
    }

    @Test
    void test_getDirection() {
        FloorRequest r = new FloorRequest("06:10:04:00", 1, 5, "up");
        assertEquals("up", r.getDirection());
    }

}