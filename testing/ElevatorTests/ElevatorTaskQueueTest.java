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

    @Test
    void getElevatorStatus() {

    }

    @Test
    void getElevatorIpAddress() {
    }

    @Test
    void setElevatorStatus() {
    }

    @Test
    void addFloorRequest() {
    }

    @Test
    void addFloorToVisit() {
    }

    @Test
    void nextFloorToVisit() {
    }

    @Test
    void nextFloorVisited() {
    }
}