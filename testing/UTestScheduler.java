//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import static org.junit.jupiter.api.Assertions.*;
//
///**
// * Test class for Scheduler.
// * This class uses an inner subclass of Synchronizer to test the Scheduler in isolation.
// */
//class SchedulerTest {
//
//    private TestableSynchronizer testableSynchronizer;
//    private Scheduler scheduler;
//
//    /**
//     * Sets up the testing environment before each test.
//     * Initializes a testable Synchronizer and the Scheduler.
//     */
//    @BeforeEach
//    void setUp() {
//        testableSynchronizer = new TestableSynchronizer();
//        scheduler = new Scheduler(testableSynchronizer);
//    }
//
//    /**
//     * Tests if the Scheduler stops running when there are no requests.
//     */
//    @Test
//    void testRunMethod_NoRequests_ShouldStopRunning() {
//        testableSynchronizer.setRunning(false); // Simulate no requests
//
//        scheduler.run();
//
//        assertFalse(testableSynchronizer.isRunning());
//    }
//
//
//
//    /**
//     * TestableSynchronizer class to simulate the Synchronizer behavior for testing.
//     */
//    private static class TestableSynchronizer extends Synchronizer {
//        private boolean isRunning = true;
//        private boolean isDestinationFloorProcessed = false;
//
//        @Override
//        public FloorRequest getRequest() {
//            // Return null to simulate no more requests available
//            return isRunning ? new FloorRequest("time", 1, 2, "up") : null;
//        }
//
//        @Override
//        public void putDestinationFloor(int floor) {
//            isDestinationFloorProcessed = true;
//        }
//
//        @Override
//        public boolean isRunning() {
//            return isRunning;
//        }
//
//        void setRunning(boolean running) {
//            this.isRunning = running;
//        }
//
//        boolean isDestinationFloorProcessed() {
//            return isDestinationFloorProcessed;
//        }
//
//        void addRequest(FloorRequest floorRequest) {
//            // For this subclass, we simulate that a floorRequest is always available
//        }
//
//
//    }
//}
