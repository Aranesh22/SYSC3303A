
/**
 * Request class is responsible for providing the message structure and information of requests
 * utilized by the synchronizer class
 *
 * @author Lindsay Dickson
 * @version Iteration1
 * @date February 2nd, 2024
 */

public class Request {
    private String time;
    private int startFloor;
    private int destinationFloor;
    private String direction;
    private int elevatorId;

    public Request(){
        time = null;
        startFloor = -1;
        destinationFloor = -1;
        direction = null;
        elevatorId = -1;
    }

    /**
     * Overloaded constructor
     * @param time time the request was made
     * @param startFloor floor the request was made from
     * @param destinationFloor floor that user is attempting to travel to
     * @param direction will be either up/down
     * @param elevatorId id of the elevator that made the request
     */
    public Request(String time, int startFloor, int destinationFloor, String direction, int elevatorId) {
        this.time = time;
        this.startFloor = startFloor;
        this.destinationFloor = destinationFloor;
        this.direction = direction;
        this.elevatorId = elevatorId;
    }

    public String getTime() { return time; }

    public int getStartFloor() { return startFloor; }

    public int getDestinationFloor() { return destinationFloor; }

    public String getDirection() { return direction; }

    public int getElevatorId() { return elevatorId; }
}