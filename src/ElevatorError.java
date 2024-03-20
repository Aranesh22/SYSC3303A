/**
 * Represents an elevator error object that stores information to inject errors into an elevator.
 * @author YehanDeSilva
 */
public class ElevatorError {
    // Fields
    private final int floor; // Floor # the error will occur.
    private final int travelTime; // Travel time from the error floor.
    private final int loadTime; // Load time on the error floor.

    // Constants
    public static final int GOOD = 0;
    public static final int DOOR = 1;
    public static final int STUCK = 2;

    /**
     * Default Constructor
     */
    public ElevatorError(int floor, int travelTime, int loadTime) {
        this.floor = floor;
        this.travelTime = travelTime;
        this.loadTime = loadTime;
    }

    /**
     * Constructor which takes in a string to construct an elevator error
     * @param msg  string to be used to make request
     */
    public ElevatorError(String msg) {
        this(Integer.parseInt(msg.split(",")[0]), Integer.parseInt(msg.split(",")[1]),
                Integer.parseInt(msg.split(",")[2]));
    }

    /**
     * Returns error message of code.
     * @param errorCode code to get error message.
     * @return error message of code.
     */
    public static String getErrorMessage(int errorCode) {
        return switch (errorCode) {
            case GOOD -> "GOOD";
            case DOOR -> "DOOR";
            case STUCK -> "STUCK";
            default -> "UNKNOWN";
        };
    }

    /**
     * Returns error floor.
     * @return error floor.
     */
    public int getFloor() {
        return this.floor;
    }

    /**
     * Returns travel time.
     * @return travel time.
     */
    public int getTravelTime() {
        return travelTime;
    }

    /**
     * Returns load time.
     * @return load time.
     */
    public int getLoadTime() {
        return loadTime;
    }
}
