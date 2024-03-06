
/**
 * FloorRequest class is responsible for providing the message structure and information of requests
 * utilized by the synchronizer class
 *
 * @author Lindsay Dickson, 101160876
 * @author Yehan De Silva (101185388)
 * @version Iteration3
 * @date March 6th, 2024
 */

public class FloorRequest {

    //Fields
    private final String time;
    private final int startFloor;
    private final int destinationFloor;
    private final String direction;

    /*
    Default constructor.
     */
    public FloorRequest(){
        time = null;
        startFloor = -1;
        destinationFloor = -1;
        direction = null;
    }

    /**
     * Overloaded constructor
     * @param time time the request was made
     * @param startFloor floor the request was made from
     * @param destinationFloor floor that user is attempting to travel to
     * @param direction will be either up/down
     *
     */
    public FloorRequest(String time, int startFloor, int destinationFloor, String direction) {
        this.time = time;
        this.startFloor = startFloor;
        this.destinationFloor = destinationFloor;
        this.direction = direction;
    }

    /**
     * Constructor which takes in a UDP string to construct a floor request
     * @param udpString UDP string to be used to make request
     */
    public FloorRequest(String udpString) {
        String[] data = udpString.split(",");
        this.time = data[0];
        this.startFloor = Integer.parseInt(data[1]);
        this.destinationFloor = Integer.parseInt(data[3]);
        this.direction = data[2];
    }

    /**
     * Returns time of request.
     * @return Time of request.
     */
    public String getTime() { return time; }

    /**
     * Returns starting floor of request.
     * @return Integer representing the starting floor of the request.
     */
    public int getStartFloor() { return startFloor; }

    /**
     * Returns destination floor of the request.
     * @return Integer representing the destination floor of the request.
     */
    public int getDestinationFloor() { return destinationFloor; }

    /**
     * Will be used for later iterations.
     * @return Direction elevator is traveling.
     */
    public String getDirection() { return direction; }

    /**
     * Returns udp representation of floor request.
     * @return udp representation of floor request
     */
    public byte[] toUdpStringBytes() {
        return (this.time + "," + this.startFloor + "," + this.direction + "," + this.destinationFloor).getBytes();
    }

    /**
     * Returns a readable string representation of request.
     * @return String representation of request.
     */
    @Override
    public String toString() {
        return "Time:" + this.time + " | FloorRequest:" + this.startFloor + "->" + this.destinationFloor;
    }
}