/**
 * The FloorSubsystem class is responsible for sending floor requests to the scheduler as well as receiving elevator
 * status updates from the scheduler.
 *
 *  @author Yehan De Silva (101185388)
 *  @version iteration3
 *  @date Mar 6, 2024
 */
public class FloorSubsystem extends Thread {

    /**
     * Initializing static data to be used throughout the program
     */
    public final static float DEFAULT_FLOOR_HEIGHT = 3.916f;
    public final static int DEFAULT_MIN_FLOOR = 1;
    public final static int DEFAULT_MAX_FLOOR = 7;
    public static final int FLOOR_SUBSYSTEM_PORT = 24;

    Synchronizer synchronizer;

    public FloorSubsystem(Synchronizer synchronizer) {
        this.synchronizer = synchronizer;
    }

    @Override
    public void run() {

    }
}
