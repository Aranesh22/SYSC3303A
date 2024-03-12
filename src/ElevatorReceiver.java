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
    private final ElevatorRequestBox requestBox;

    // Port number

    /**
     * Constructor
     */
    public ElevatorReceiver (ElevatorRequestBox requestBox) {
        this.requestBox = requestBox;
        try {
            // Bind socket to specific port
            receiveSocket = new DatagramSocket();
        } catch(SocketException e) {
            System.exit(1);
        }
    }

    /**
     * Run method
     */
    public void run() {
        this.putInitialElevatorMsg();
        while (true) {
            getMsg();
            putElevatorMsg();
        }
    }

    /**
     * Put elevator msg in shared box for elevator to access.
     */
    private void putElevatorMsg() {
        // Create ElevatorMessage: Port number of this Elevator Receiver and target floor
        ElevatorMessage request = new ElevatorMessage(receiveSocket.getPort(),
                Integer.parseInt(new String(receivePacket.getData(), 0, receivePacket.getLength())));

        // Put ElevatorMessage in request box for elevator
        requestBox.putRequest(request);
    }

    /**
     * Puts initial message into request box.
     */
    private void putInitialElevatorMsg() {
        ElevatorMessage msg = new ElevatorMessage(receiveSocket.getLocalPort());
        requestBox.putRequest(msg);
    }

    /**
     * Get message from scheduler.
     */
    private void getMsg() {
        // Wait to receive floor request from Scheduler
        byte[] data = new byte[100];
        receivePacket = new DatagramPacket(data, data.length);
        try {
            receiveSocket.receive(receivePacket);
        } catch (IOException e) {
            System.exit(1);
        }
    }
}
