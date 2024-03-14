/**
 * Class models the status containing all pertinent fields for an elevator car.
 * @author YehanDeSilva 101185388
 * @version iteration3
 */
public class ElevatorStatus {

    //Fields
    private final int elevatorId;
    private final int currentFloor;
    private final int targetFloor;
    private final int receiverPortNum;
    private final boolean doorsOpened;
    private final boolean moving;
    private final String direction;


    /**
     * Overloaded constructor
     * @param currentFloor Current floor of elevator.
     * @param targetFloor Target floor of elevator.
     * @param receivePortNum Elevator receiver port number.
     * @param moving If the elevator is moving or is stationary.
     * @param direction Direction elevator is moving in.
     */
    public ElevatorStatus (int elevatorId, int currentFloor, int targetFloor, int receivePortNum, boolean doorsOpened, boolean moving, String direction) {
        this.elevatorId = elevatorId;
        this.currentFloor = currentFloor;
        this.targetFloor = targetFloor;
        this.receiverPortNum = receivePortNum;
        this.doorsOpened = doorsOpened;
        this.moving = moving;
        this.direction = direction;
    }

    /**
     * Constructor which takes in a UDP string to construct an elevator status object.
     * @param udpString UDP string to be used to make request
     */
    public ElevatorStatus(String udpString)  {
        this(Integer.parseInt(udpString.split(",")[0]), Integer.parseInt(udpString.split(",")[1]),
                Integer.parseInt(udpString.split(",")[2]), Integer.parseInt(udpString.split(",")[3]),
                Boolean.parseBoolean(udpString.split(",")[4]),
                Boolean.parseBoolean(udpString.split(",")[5]), udpString.split(",")[6]);
    }

    /**
     * Constructor which takes in a byte array and length to create an elevator status.
     * @param msg Entire elevator status message.
     * @param length Length of message.
     */
    public ElevatorStatus(byte[] msg, int length) {
        this(new String(msg, 0, length));
    }

    /**
     * Returns elevator id.
     * @return elevator id.
     */
    public int getElevatorId() {
        return this.elevatorId;
    }
    /**
     * Returns current floor.
     * @return Elevator current floor.
     */
    public int getCurrentFloor() {
        return currentFloor;
    }

    /**
     * Returns target floor.
     * @return Elevator target floor.
     */
    public int getTargetFloor() {
        return targetFloor;
    }

    /**
     * Returns elevator receiver port number.
     * @return Elevator receiver port number.
     */
    public int getReceiverPortNum() {
        return receiverPortNum;
    }

    /**
     * Returns whether elevator is moving.
     * @return True if elevator is moving
     */
    public boolean getMoving() {
        return moving;
    }

    /**
     * Returns direction elevator is moving in.
     * @return direction elevator is moving in.
     */
    public String getDirection() {
        return direction;
    }

    /**
     * Returns udp representation of elevator status.
     * @return udp representation of elevator status
     */
    public byte[] toUdpStringBytes() {
        return (this.elevatorId + "," + this.currentFloor + "," + this.targetFloor + "," + this.receiverPortNum + ","
                + this.doorsOpened + "," + this.moving + "," + this.direction).getBytes();
    }

    /**
     * String representation of elevator status.
     * @return string representation of elevator status.
     */
    @Override
    public String toString() {
        return "Current Floor:" + this.currentFloor + " | Target Floor:" + this.targetFloor + " | PortNum:"
                + this.receiverPortNum + " | Direction of Car-->" + this.direction + " | Moving: " + this.moving
                + " | Doors " + ((this.doorsOpened)? "Open" : "Closed");
    }
}
