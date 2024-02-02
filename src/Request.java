/**
 * Request class is responsible for providing the message structure and information of requests
 * utilized by the synchronizer class
 *
 * @author Lindsay
 * @date February 2nd, 2024
 */

public class Request {
    private String time;
    private int startFloor;
    private int destinationFloor;
    private String direction;

    public Request(){
        time = null;
        startFloor = -1;
        destinationFloor = -1;
        direction = null;
    }
    public Request(String time, int startFloor, int destinationFloor, String direction) {
        this.time = time;
        this.startFloor = startFloor;
        this.destinationFloor = destinationFloor;
        this.direction = direction;
    }

    public String getTime() { return time; }

    public int getStartFloor() { return startFloor; }

    public int getDestinationFloor() { return destinationFloor; }

    public String getDirection() { return direction; }
}