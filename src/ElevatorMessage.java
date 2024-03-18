/**
 * The ElevatorMessage class models a message that is sent
 * to the Elevator subsystem from the Scheduler subsystem.
 * This message contains the following:
 * - The port number of the ElevatorReceiver thread (so that the Scheduler can send a FloorRequest to it via UDP)
 * - The target floor number for the elevator
 *
 * @author Pathum Danthanarayana, 101181411
 * @date March 7, 2024
 */
public class ElevatorMessage {

    // Fields
    private final int elevatorReceiverPortNum;
    private int targetFloor;

    /**
     * Constructor
     */
    public ElevatorMessage(int elevatorReceiverPortNum, int targetFloor) {
        this.elevatorReceiverPortNum = elevatorReceiverPortNum;
        this.targetFloor = targetFloor;
    }

    public ElevatorMessage(int elevatorReceiverPortNum) {
        this.elevatorReceiverPortNum = elevatorReceiverPortNum;
    }

    /**
     * @return the port number of the associated ElevatorReceiver thread
     */
    public int getElevatorReceiverPortNum() {
        return elevatorReceiverPortNum;
    }

    /**
     * @return the target floor number of the elevator
     */
    public int getTargetFloor() {
        return targetFloor;
    }
}
