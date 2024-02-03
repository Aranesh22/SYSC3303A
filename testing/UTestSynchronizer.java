import org.junit.jupiter.api.Test;

/**
 * Synchronizer Class Unit Tests
 *
 * @version Iteration1
 * @author Pathum Danthanarayana, 101181411
 * @date February 3, 2024
 */

public class UTestSynchronizer {
    /**
     * Tests whether the Synchronizer initializes its
     * state correctly upon construction.
     */
    @Test
    void testSynchronizerInit() {
        // Create synchronizer
        Synchronizer synchronizer = new Synchronizer();

        assertEquals(synchronizer.testGetDestinationFloor(), -1);
        assertEquals(synchronizer.testGetElevatorStatus(), -1);
        assertEquals(synchronizer.testGetCurrentFloor(), -1);
        assertEquals(synchronizer.testGetRequests().size(), 0);
        assertTrue(synchronizer.isRunning());
    }

    /**
     * Tests whether the Synchronizer's running state can get retrieved.
     */
    @Test
    void testIsRunning() {
        // Create synchronizer
        Synchronizer synchronizer = new Synchronizer();
        assertTrue(synchronizer.isRunning());
    }

    /**
     * Tests whether the Synchronizer's running state can be set correctly.
     */
    @Test
    void testSetRunning() {
        // Create synchronizer
        Synchronizer synchronizer = new Synchronizer();

        synchronizer.setRunning(false);
        assertFalse(synchronizer.isRunning());

        synchronizer.setRunning(true);
        assertTrue(synchronizer.isRunning());
    }
}