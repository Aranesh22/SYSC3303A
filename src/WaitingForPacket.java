import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * The WaitingForPacket class models the state in the Scheduler
 * where the scheduler waits indefinitely until a UDP packet is
 * received by its socket (either a FloorRequest or ElevatorStatus).
 *
 * @author Pathum Danthanarayana, 101181411
 * @date March 11, 2024
 */
public class WaitingForPacket extends SchedulerState {

    /**
     * Constructor
     *
     * @param schedulerContext - context
     */
    protected WaitingForPacket(Scheduler schedulerContext) {
        super(schedulerContext);
    }

    /**
     * Start the state
     */
    public void start() {
        entry();
    }

    /**
     * Entry actions
     */
    private void entry() {
        waitForPacket();
    }

    /**
     * Waits for a packet to arrive at its receive socket
     */
    private void waitForPacket() {
        try {
            // Wait to receive packet
            byte[] data = new byte[100];
            DatagramSocket receiveSocket = schedulerContext.getReceiveSocket();
            DatagramPacket packet = new DatagramPacket(data, data.length);
            receiveSocket.receive(packet);
            // Save received packet
            schedulerContext.saveReceivedPacket(packet);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // Generate Packet Received event
        schedulerContext.packetReceived();
    }

    /**
     * Packet received event handler
     */
    public SchedulerState packetReceived() {
        // Return next state
        return schedulerContext.getState("CheckingPacketType");
    }
}
