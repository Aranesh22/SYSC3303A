import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testing for ElevatorTaskQueue Class
 *
 * @author Lindsay Dickson 101160876
 * @version Iteration 3
 * @date March 2024
 *
 * Notes: will later develop acceptance testing for the scenario of
 * requesting a floor, retrieving the requested floor and then removing it from the queue
 */
class ElevatorTaskQueueTest {
    ElevatorStatus elevatorStatus;

    int elevatorId, currentFloor, targetFloor, receivePortNum;
    boolean doorsOpened, moving;
    String direction;
    public static final InetAddress SCHEDULER_IP;
    static {
        try {
            SCHEDULER_IP = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }
    ElevatorTaskQueue elevatorTaskQueue;


    @BeforeEach
    void setUp() {
        elevatorId = 1;
        currentFloor = 1;
        targetFloor = 7;
        receivePortNum = 52054;
        doorsOpened = false;
        moving = false;
        direction = "up";
        elevatorStatus = new ElevatorStatus(elevatorId, currentFloor, targetFloor, receivePortNum, doorsOpened, moving, direction);

        elevatorTaskQueue = new ElevatorTaskQueue(elevatorStatus, SCHEDULER_IP);
    }

    /**
     * Unit Test
     * Asserts that the elevator status is returned correctly
     */
    @Test
    void getElevatorStatus() {
        String statusString = "Current Floor:1 | Target Floor:7 | PortNum:52054 | Direction of Car-->up | Moving: false | Doors Closed";
        assertEquals(statusString, elevatorTaskQueue.getElevatorStatus().toString());
    }


    /**
     * Unit Test
     * Asserts that the IP address is reachable via get method
     * Note: We assert NotNull as this is a value unique to the host machine
     */
    @Test
    void getElevatorIpAddress() {
        assertNotNull(elevatorTaskQueue.getElevatorIpAddress());
    }

    /**
     * Test for setting the elevator Status
     */
    @Test
    void setElevatorStatus() {
        //Store previous elevator status
        ElevatorStatus oldStatus = elevatorTaskQueue.getElevatorStatus();
        //Change status values
        elevatorId = 2;
        currentFloor = 4;
        targetFloor = 2;
        receivePortNum = 52054;
        doorsOpened = true;
        moving = false;
        direction = "down";
        //set the new status
        elevatorTaskQueue.setElevatorStatus(new ElevatorStatus(elevatorId, currentFloor, targetFloor, receivePortNum, doorsOpened, moving, direction));

        //Assert the change in status
        assertNotEquals(oldStatus, elevatorTaskQueue.getElevatorStatus());

    }


    /**
     * Test to assert that the floorRequest is properly added to the request queue
     */
    @Test
    void addFloorRequest() {
        assertEquals(0, elevatorTaskQueue.nextFloorToVisit());
        elevatorTaskQueue.addFloorRequest(new FloorRequest("14:05:15,2,7,up"));
        assertEquals(2, elevatorTaskQueue.nextFloorToVisit());
    }

    /**
     * Asserts that the floor Request is added
     * Note: a value of 0 indicates no requests
     */
    @Test
    void addFloorToVisit() {
        //Check that the request queue is empty
        assertEquals(0, elevatorTaskQueue.nextFloorToVisit());
        //add the floor to the request queue
        elevatorTaskQueue.addFloorToVisit(4);
        //assert that the request queue is no longer empty and the requested floor is added correctly
        assertEquals(4, elevatorTaskQueue.nextFloorToVisit());

    }


    /**
     * Test that floors are retrievable from the queue
     * Note: a value of 0 indicates no requests
     */
    @Test
    void nextFloorToVisit() {
        elevatorTaskQueue.addFloorToVisit(6);
        assertEquals(6, elevatorTaskQueue.nextFloorToVisit());
    }

    /**
     * Tests that a floor is removed from the queue once visited
     */
    @Test
    void nextFloorVisited() {
        //ensure the queue is not empty
        elevatorTaskQueue.addFloorToVisit(6);
        assertEquals(6, elevatorTaskQueue.nextFloorToVisit());
        //remove the floor from the queue
        elevatorTaskQueue.nextFloorVisited();
        assertEquals(0, elevatorTaskQueue.nextFloorToVisit());
    }
}