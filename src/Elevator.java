import javax.xml.crypto.Data;
import java.io.IOException;
import java.net.*;
import java.util.Objects;

/**
 * The Elevator thread class models a traction elevator used to move people around in a building.
 * It realistically models an elevator by replicating the various timing aspects of an elevator that were measured in
 * iteration 0 of the project.
 * Once the scheduler communicates a target floor to the elevator, it will attempt to move to that floor and notify
 * of all floors it passes by or arrives at.
 *
 * @author Yehan De Silva
 * @version iteration1, iteration2
 * @date Feb 2, 2024 / Feb 21, 2024
 *
 */

public class Elevator {

    private ElevatorState currentState;
    // Fields
    private final ElevatorRequestBox requestBox;
    private DatagramSocket sendSocket;
    private int elevatorReceiverPortNum;
    private final int id;
    private int curFloor;
    private int destFloor;
    private boolean moving;
    private String direction;

    // Constants
    public final static float DEFAULT_VELOCITY = 1.75f * 1000;
    public final static long DEFAULT_LOAD_UNLOAD_TIME = 5;

    /**
     * Default constructor.
     * @param requestBox Request box elevator shares with Elevator Receiver
     */
    public Elevator(ElevatorRequestBox requestBox, int id) {
        this(requestBox, id, 1);
    }

    /**
     * Overloaded constructor.
     * @param id Id of elevator.
     * @param curFloor FloorRequestSimulator elevator is starting at.
     */
    public Elevator(ElevatorRequestBox requestBox, int id, int curFloor) {
        this.requestBox = requestBox;
        this.id = id;
        this.curFloor = curFloor;
        this.moving = false;
        this.direction = "N/A";
        this.destFloor = -1;
        elevatorReceiverPortNum = -1;

        // Set up socket for sending (bind to any available port)
        try {
            sendSocket = new DatagramSocket();
        } catch (SocketException e) {
            System.exit(1);
        }
        //Set initial state
        this.setState(new StationaryDoorsClosed());

    }

    /**
     * Gets an ElevatorMessage from the request box,
     * and stores the elevator receiver's port number and the destination floor
     *
     */
    public void getElevatorMessage() {
        ElevatorMessage request = requestBox.getRequest();
        this.elevatorReceiverPortNum = request.getElevatorReceiverPortNum();
        this.destFloor = request.getTargetFloor();
        // Request received event
        this.requestReceived();
    }

    /**
     * @return true if the request box is empty, and false otherwise
     */
    public boolean requestBoxIsEmpty() {
        return requestBox.isEmpty();
    }

    /**
     * Creates an ElevatorStatus message to send to the scheduler,
     * which notifies the Scheduler (and in turn the floor subsystem).
     */
    public void sendElevatorStatus() {
        // Create ElevatorStatus message
        ElevatorStatus status = new ElevatorStatus(id, curFloor, destFloor, elevatorReceiverPortNum, true, direction);
        // Send message to Scheduler
        try {
            // Get IP address of Scheduler
            byte[] data = status.toUdpStringBytes();
            DatagramPacket sendPacket = new DatagramPacket(data, data.length, Scheduler.SCHEDULER_IP, Scheduler.SCHEDULER_PORT);
            // Send packet
            sendSocket.send(sendPacket);
        } catch (IOException e) {
            System.exit(1);
        }
    }

    /**
     * Update moving field.
     * @param moving if the elevator is moving or is stationary.
     */
    public void setMoving(boolean moving) {
        this.moving = moving;
        this.updateDirection();
    }

    /**
     * Sets the direction of the elevator
     */
    private void updateDirection () {
        if (this.moving) {
            if (this.destFloor > this.curFloor) {
                direction = "up";
            } else {
                direction = "down";
            }
        }
        else {
            direction = "N/A";
        }
    }

    /**
     * Moves elevator to the specified floor.
     */
    public void goToFloor() {
        // Update current floor and notify we have arrived at this floor.
        if (Objects.equals(this.direction, "up")) {
            this.curFloor++;
        } else {
            this.curFloor--;
        }
        this.setMoving(this.curFloor == this.destFloor);
        System.out.println(this + ": Currently at floor " + this.curFloor);
        if (!this.requestBoxIsEmpty()) {
            this.getElevatorMessage();
        }
    }

    /**
     * Converts this elevator object to a readable string.
     * @return String representing this elevator.
     */
    @Override
    public String toString() {
        return "Elevator " + this.id;
    }



//---------Iteration 2/3 Code

    public int getCurFloor(){ return this.curFloor; }
    public int getDestFloor(){ return this.destFloor; }

    /**
     * Sets the current state of the state machine
     */
    public void setState(ElevatorState newState) {
        this.currentState = newState;
        this.currentState.handleState(this);
    }

    /**
     * Event of a timer expiring.
     */
    public void timerExpired() {
        this.currentState.timerExpired(this);
    }

    /**
     * Event of the Elevator receiving a request.
     */
    public void requestReceived() {
       this.currentState.requestReceived(this);
    }

}