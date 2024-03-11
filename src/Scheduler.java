import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;

/**
 *
 * The Scheduler class manages elevator requests and coordinates
 * the elevators based on the current system state. It receives requests
 * from floors, processes them, and assigns elevators to fulfill those requests.
 * It also checks the current floor status of elevators and sends the status to the floor.
 * Scheduler class extends Thread and is responsible for managing the state of the elevator system.
 * It interacts with the Synchronizer to get the status of the elevator and handle requests.
 *
 * @author Harishan Amutheesan, 101154757
 * @author Yehan De Silva, 101185388
 * @date February 2nd, 2024 / February 22nd, 2024
 * @version iteration1, iteration2
 */
public class Scheduler extends Thread {
    private final Synchronizer synchronizer; // Synchronizer object to interact with the elevator system
    private SchedulerState currentState; // Current state of the Scheduler
    private FloorRequest currentRequest; // Current request being handled by the Scheduler

    public static final InetAddress SCHEDULER_IP;   // IP address of Scheduler
    static {
        try {
            SCHEDULER_IP = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    public static final int SCHEDULER_PORT = 25; //Current Port

    private DatagramSocket sendSocket,receiveSocket;
    private DatagramPacket sendPacket,receivePacket;
    /**
     * Constructor for the Scheduler class.
     * @param synchronizer Synchronizer object to interact with the elevator system
     */
    public Scheduler(Synchronizer synchronizer) {
        this.synchronizer = synchronizer;
        setState(new WaitingForFloorRequest()); // Set initial state
    }

    private void getMsg() {
        byte[] data = new byte[100];
        this.receivePacket = new DatagramPacket(data, data.length);

        try {
            receiveSocket.receive(this.receivePacket);
        } catch (IOException e) {

        }

        //Case to check what function to switch

        //if message is for Elevator call elevator
        getMsgElvStatus();
        //else call Floor Request
        getMsgFloorReq();

    }

    private void getMsgElvStatus() {

        try {
            this.sendPacket = new DatagramPacket(this.receivePacket.getData(), this.receivePacket.getLength(), InetAddress.getLocalHost(),
                    FloorSubsystem.FLOOR_SUBSYSTEM_PORT);
        } catch (UnknownHostException e) {

        }

        try {
            DatagramSocket sendSocket = new DatagramSocket();
            sendSocket.setSoTimeout(1000);
            sendSocket.send(this.sendPacket);
            sendSocket.close();
        } catch (IOException e) {
        }

    }
    private void getMsgFloorReq() {


    }

    private void sendReqtoRecieve() {
        try {
            this.sendPacket = new DatagramPacket(this.receivePacket.getData(), this.receivePacket.getLength(),
                    InetAddress.getLocalHost(), ElevatorReceiver.ELEVATOR_RECEIVER_PORT);
        } catch (UnknownHostException e) {
        }
        try {
            DatagramSocket sendSocket = new DatagramSocket();
            sendSocket.setSoTimeout(1000);
            sendSocket.send(this.sendPacket);
            sendSocket.close();
        } catch (IOException e) {
        }
    }
    /**
     * Overridden run method from Thread class.
     * Continuously handles the current state of the Scheduler as long as the Synchronizer is running.
     */
    @Override
    public void run() {
        while (synchronizer.isRunning()) {
            currentState.handleState(this);
        }
    }

    /**
     * Stops the Scheduler and the Synchronizer.
     */
    public void stopScheduler() {
        System.out.println("Scheduler: Stopping scheduler and exiting.");
        synchronizer.stopRunning();
    }

    /**
     * Sets the current state of the Scheduler.
     * @param newState New state to be set
     */
    public void setState(SchedulerState newState) {
        this.currentState = newState;
        System.out.println("[Scheduler-STATE]: State changed to " + newState.getClass().getSimpleName());
    }

    /**
     * Handles the status of the elevator.
     * Continuously gets the status of the elevator from the Synchronizer until the elevator reaches the target floor.
     * @param targetFloor The floor that the elevator is supposed to reach
     */
    public void handleElevatorStatus(int targetFloor) {
        int elevatorStatus;
        do {
            elevatorStatus = synchronizer.getElevatorStatus();
            // TODO had to remove this to prevent the scheduler from getting stuck. Remove when removing synchronizer.
            // synchronizer.putCurrentFloor(elevatorStatus);
        } while (elevatorStatus != targetFloor);
        System.out.println("Scheduler: Elevator has arrived at requested floor " + elevatorStatus);
    }

    /**
     * Returns the Synchronizer object.
     * @return Synchronizer object
     */
    public Synchronizer getSynchronizer() {
        return synchronizer;
    }

    /**
     * Returns the current request being handled by the Scheduler.
     * @return Current request
     */
    public FloorRequest getCurrentRequest() {
        return currentRequest;
    }

    /**
     * Sets the current request to be handled by the Scheduler.
     * @param request New request to be handled
     */
    public void setCurrentRequest(FloorRequest request) {
        this.currentRequest = request;
    }
}