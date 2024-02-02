public class Request {
    private String time;
    private int startFloor;
    private int destinationFloor;
    private String direction;

    public Request(){ }
    public Request(String time, String direction, int startFloor, int destinationFloor) {
        this.time = time;
        this.direction = direction;
        this.startFloor = startFloor;
        this.destinationFloor = destinationFloor;
    }

    public String getTime() { return time; }

    public int getStartFloor() { return startFloor; }

    public int getDestinationFloor() { return destinationFloor; }

    public String getDirection() { return direction; }
}