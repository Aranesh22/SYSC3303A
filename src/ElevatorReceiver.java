import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * The ElevatorReceiver class is responsible for receiving
 * floor requests from the Scheduler, on behalf of the elevator
 * subsystem. This is because the elevator subsystem will be busy
 * moving between floors, and will not be always reachable via UDP.
 * This thread will always be expecting to receive floor requests on its socket.
 */
public class ElevatorReceiver extends Thread {

    // Fields
    private DatagramSocket receiveSocket;
    private DatagramPacket receivePacket;
    private ElevatorRequestBox requestBox;
    private boolean stopReceiving;
    // Port number
    public static final int ELEVATOR_RECEIVER_PORT=35;

    /**
     * Constructor
     */
    public ElevatorReceiver (ElevatorRequestBox requestBox) {
        this.requestBox = requestBox;
        stopReceiving = false;
        try {
            // Bind socket to specific port
            receiveSocket = new DatagramSocket(ELEVATOR_RECEIVER_PORT);
        } catch(SocketException e) {
            System.exit(1);
        }
    }

    /**
     * Run method
     */
    public void run() {
        while (!stopReceiving) {
            // Wait to receive floor request from Scheduler
            byte[] data = new byte[100];
            receivePacket = new DatagramPacket(data, data.length);
            try {
                receiveSocket.receive(receivePacket);
            } catch (IOException e) {
                System.exit(1);
            }

            // Create ElevatorMessage: Port number of this Elevator Receiver and target floor
            FloorRequest floorRequest = new FloorRequest(new String(receivePacket.getData(), 0, receivePacket.getLength()));
            ElevatorMessage request = new ElevatorMessage(receiveSocket.getPort(), floorRequest.getDestinationFloor());

            // Put ElevatorMessage in request box for elevator
            requestBox.putRequest(request);
        }
    }
}
