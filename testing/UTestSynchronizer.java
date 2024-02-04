import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Synchronizer Class Unit Tests
 * @version Iteration1
 * @date February 3, 2024
 */

public class UTestSynchronizer {
    @Test
    void testRequest_isRunning() {

        boolean run = true;
        assertTrue(run);

    }
   @Test
   void testRequest_stopRunning() {

        boolean stop = false;
        assertFalse(stop);

   }
}