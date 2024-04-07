import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * Testing of FloorSubSystem Class
 *
 * NOTE: Each test must be run individually otherwise address binding errors will occur.
 *  issue is being looked at
 *
 * @author Lindsay Dickson 101160876
 * @version Iteration 4
 * @date March 2024
 *
 */
public class FloorSubSystemTest {
    int elevatorId = 1;
    int currentFloor = 1;
    int targetFloor = 7;
    int receivePortNum = 52054;
    boolean doorsOpened = false;
    boolean moving = false;
    String direction = "up";
    int errorCode = 0;

    DatagramSocket sendSocket;

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
     * Test that the FloorSubSystem is correctly recieving and extracting packets for elevator status
     */
    @Test
    public void testProccessMsgElevatorStatus(){
        ElevatorStatus elevatorStatus = new ElevatorStatus(elevatorId, currentFloor, targetFloor, receivePortNum, doorsOpened, moving, direction, 0, 0);
        byte[] data = elevatorStatus.toUdpStringBytes();

        FloorSubsystem floorSubsystem = new FloorSubsystem(new ElevatorFrame());

        helperSendPacket(data);

        floorSubsystem.getMsg();
        floorSubsystem.processMsg();
        assertEquals("FloorSubsystem: Received elevator status: Elevator 1 at floor 1 (Stationary Doors Closed)", outContent.toString().trim());

    }

    /**
     * Test that the FloorSubSystem is correctly recieving and extracting packets for floor status
     */
    @Test
    public void testProcessMsgFloorStatus() {

        FloorSubsystem floorSubsystem = new FloorSubsystem(new ElevatorFrame());
        Thread floorRequestSimulator = new Thread(new FloorRequestSimulator(), "FloorRequestSimulator");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        floorRequestSimulator.start();
        floorSubsystem.getMsg();

        floorSubsystem.processMsg();

        assertEquals("FloorSubsystem: Received floor request: Time:14:05:15 | FloorRequest:1->7", outContent.toString().trim());
    }


    // Unsure if this is truly testable - likely will be tested by shceduler class
    @Test
    public void testSendMsgToScheduler() {
        ElevatorStatus elevatorStatus = new ElevatorStatus(elevatorId, currentFloor, targetFloor, receivePortNum, doorsOpened, moving, direction, 0, 0);
        byte[] data = elevatorStatus.toUdpStringBytes();
//
//        FloorSubsystem floorSubsystem = new FloorSubsystem();
//
//        helperSendPacket(data);
//
//        floorSubsystem.getMsg();
//        floorSubsystem.processMsg();
//
//        // Call the method
//        floorSubsystem.sendMsgToScheduler();

    }

    public void helperSendPacket(byte[] data){
        DatagramPacket packet = new DatagramPacket(data, data.length, FloorSubsystem.FLOOR_SUBSYSTEM_IP, 24);
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

        sendSocket.close();
    }

}