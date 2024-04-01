import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.*;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class ElevatorReceiverTest {
    public static final InetAddress SCHEDULER_IP;   // IP address of Scheduler
    static {
        try {
            SCHEDULER_IP = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }
    int elevatorId = 1;
    int currentFloor = 1;
    int targetFloor = 7;
    int receivePortNum = 52054;
    boolean doorsOpened = false;
    boolean moving = false;
    String direction = "up";
    int errorCode = 0;

    private DatagramSocket sendSocket;

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }
    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
    }

    /**
     * Asserts that the initial elevator Message is put and the box is no longer empty
     */
    @Test
    public void putInitialElevatorMsg(){
        ElevatorReceiver elevatorReceiver = new ElevatorReceiver(new ElevatorRequestBox());
        //Check that box is empty
        assertTrue(elevatorReceiver.getElevatorRequestBox().isEmpty());
        elevatorReceiver.putInitialElevatorMsg();
        assertFalse(elevatorReceiver.getElevatorRequestBox().isEmpty());

    }
    /**
     * IN progress
     */
    @Test
    public void putElevatorMsg(){
        ElevatorRequestBox elevatorRequestBox = new ElevatorRequestBox();
        Elevator elevator = new Elevator(elevatorRequestBox, 1);
        ElevatorReceiver elevatorReceiver = new ElevatorReceiver(elevatorRequestBox);
        //Check that box is empty
        assertTrue(elevatorReceiver.getElevatorRequestBox().isEmpty());
        ElevatorStatus elevatorStatus = new ElevatorStatus(elevatorId, currentFloor, targetFloor, receivePortNum, doorsOpened, moving, direction, 0);
        byte[] data = elevatorStatus.toUdpStringBytes();
        elevatorReceiver.putInitialElevatorMsg();

//        helperSendPacket(data);

        elevatorReceiver.getMsg();
        elevatorReceiver.putElevatorMsg();

        assertFalse(elevatorReceiver.getElevatorRequestBox().isEmpty());



    }


    /**
     * IN progress
     */
    @Test
    public void getMsg(){
        ElevatorRequestBox elevatorRequestBox = new ElevatorRequestBox();
        Elevator elevator = new Elevator(elevatorRequestBox, 1);
        ElevatorReceiver elevatorReceiver = new ElevatorReceiver(elevatorRequestBox);
        ElevatorStatus elevatorStatus = new ElevatorStatus(elevatorId, currentFloor, targetFloor, receivePortNum, doorsOpened, moving, direction, 0);
        byte[] data = elevatorStatus.toUdpStringBytes();
        helperSendPacket(data);

        elevatorReceiver.getMsg();
    }



    public void helperSendPacket(byte[] data){
        DatagramPacket packet = new DatagramPacket(data, data.length, SCHEDULER_IP, receivePortNum);
        try {
            sendSocket = new DatagramSocket();
        } catch (SocketException se) {
            se.printStackTrace();
            System.exit(1);
        }
        try {
            sendSocket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

//        sendSocket.close();
    }

}
