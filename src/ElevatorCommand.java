/**
 * Models a command sent to an elevator.
 * @author Yehan De Silva
 */
public class ElevatorCommand {
    //Fields
    private final int firstFloor;
    private final int secondFloor;
    private final int passengerCount;

    /**
     * Constructor for elevator command.
     * @param firstFloor target floor of command.
     * @param passengerCount capacity of command.
     */
    public ElevatorCommand(int firstFloor, int secondFloor, int passengerCount) {
        this.firstFloor = firstFloor;
        this.secondFloor = secondFloor;
        this.passengerCount = passengerCount;
    }

    /**
     * Constructor which takes in a UDP string to construct an elevator command.
     * @param udpString UDP string to be used to make command
     */
    public ElevatorCommand(String udpString) {
        this(Integer.parseInt(udpString.split(",")[0]), Integer.parseInt(udpString.split(",")[1]),
                Integer.parseInt(udpString.split(",")[2]));
    }

    /**
     * Constructor which takes in a byte array and length to create an elevator command.
     * @param msg Entire elevator command message
     * @param length Length of command
     */
    public ElevatorCommand(byte[] msg, int length) {
        this(new String(msg, 0, length));
    }

    /**
     * Returns first floor.
     * @return first floor.
     */
    public int getFirstFloor() {
        return firstFloor;
    }

    /**
     * Returns second floor.
     * @return second floor.
     */
    public int getSecondFloor() {
        return secondFloor;
    }

    /**
     * Returns passenger count.
     * @return passenger count.
     */
    public int getPassengerCount() {
        return passengerCount;
    }

    /**
     * Returns udp representation of elevator command.
     * @return udp representation of elevator command.
     */
    public byte[] toUdpStringBytes() {
        return (this.firstFloor + "," + this.secondFloor + "," + this.passengerCount).getBytes();
    }

    /**
     * String representation of elevator command.
     * @return string representation of elevator command.
     */
    @Override
    public String toString() {
        return "First Floor: " + this.firstFloor + " | Second Floor: " + this.secondFloor + " | Passenger Count: " + this.passengerCount;
    }
}
