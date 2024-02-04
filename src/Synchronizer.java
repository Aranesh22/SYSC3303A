import java.util.ArrayList;

/**
 * The Synchronizer class is a thread-safe class responsible for synchronizing the
 * Floor, Scheduler, and Elevator subsystems.
 * This includes synchronizing creating/retrieving requests,
 * setting/retrieving destination floor numbers, and setting/retrieving
 * current floor numbers.
 *
 * @author Pathum Danthanarayana, 101181411
 * @version iteration1
 * @date February 1st, 2024
 */
public class Synchronizer {

    // Fields
    private boolean running;
    private int destinationFloor;
    private int elevatorStatus;
    private int currentFloor;
    private ArrayList<Request> requests;

    /**
     * Constructor
     */
    public Synchronizer() {
        // Initialize fields
        running = true;
        elevatorStatus = -1;
        destinationFloor = -1;
        currentFloor = -1;
        requests = new ArrayList<>();
    }

    /**
     * Invoked by the Scheduler subsystem to consume requests.
     * @return a request from the list of requests
     */
    public synchronized Request getRequest() {
        // Condition synchronization (wait until request is added)
        while(requests.isEmpty() && running) {
            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println("Exception when invoking wait() in getRequest()");
            }
        }
        // Check if running (again)
        if (running) {
            // Check if last request
            Request request = requests.removeFirst();
            // Notify threads in wait set
            notifyAll();
            return request;
        }
        notifyAll();
        return null;
    }

    /**
     * Invoked by the Floor subsystem to produce a new request.
     */
    public synchronized void putRequest(Request request) {
        requests.add(request);
        // Notify threads in wait set
        notifyAll();
    }

    /**
     * Invoked by the Elevator subsystem to retrieve its destination floor.
     * @return the destination floor for the elevator
     */
    public synchronized int getDestinationFloor() {
        // Condition synchronization
        while (destinationFloor == -1 && running) {
            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println("Exception when invoking wait() in getDestinationFloor()");
            }
        }
        // Check if running (again)
        if (running) {
            int destinationFloorToReturn = destinationFloor;
            // Set to -1, indicating it has been consumed
            destinationFloor = -1;
            notifyAll();
            return destinationFloorToReturn;
        }
        notifyAll();
        return -1;
    }

    /**
     * Invoked by the Scheduler subsystem to produce a destination floor
     * for the Elevator to consume.
     * @param floor - the destination floor that the elevator should reach
     */
    public synchronized void putDestinationFloor(int floor) {
        // Condition synchronization
        while (destinationFloor != -1 && running) {
            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println("Exception occurred when trying to wait() in putDestinationFloor()");
            }
        }
        // Check if running (again)
        if (running) {
            destinationFloor = floor;
        }
        notifyAll();
    }

    /**
     * Invoked by the Scheduler subsystem to get the current status
     * of the elevator, so that it knows when to send the next requests.
     * @return the current status of the elevator (floor number)
     */
    public synchronized int getElevatorStatus() {
        // Condition synchronization
        while (elevatorStatus == -1 && running) {
            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println("Exception occurred when trying to wait() in getElevatorStatus()");
            }
        }
        // Check if running (again)
        if (running) {
            int elevatorStatusToReturn = elevatorStatus;
            // Set to -1, indicating it has been consumed
            elevatorStatus = -1;
            notifyAll();
            return elevatorStatusToReturn;
        }
        notifyAll();
        return -1;
    }

    /**
     * Invoked by the Elevator subsystem to notify the Scheduler
     * subsystem of its status.
     * @param status - the current status of the elevator (floor number)
     */
    public synchronized void putElevatorStatus(int status) {
        // Condition synchronization
        while (elevatorStatus != -1 && running) {
            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println("Exception occurred when trying to wait() in putElevatorStatus()");
            }
        }
        // Check if running (again)
        if (running) {
            elevatorStatus = status;
        }
        notifyAll();
    }

    /**
     * Invoked by the Floor subsystem to get the current floor of the elevator.
     * @return the current floor of the elevator
     */
    public synchronized int getCurrentFloor() {
        // Condition synchronization
        while (currentFloor == -1 && running) {
            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println("Exception occurred when trying to wait() in getCurrentFloor()");
            }
        }
        // Check if running (again)
        if (running) {
            int currentFloorToReturn = currentFloor;
            // Set to -1, indicating it has been consumed
            currentFloor = -1;
            notifyAll();
            return currentFloorToReturn;
        }
        notifyAll();
        return -1;
    }

    /**
     * Invoked by the Scheduler subsystem to update the current floor that the elevator is on
     * (for the Floor subsystem to later consume).
     * @param floor - the current floor of the elevator
     */
    public synchronized void putCurrentFloor(int floor) {
        // Condition synchronization
        while (currentFloor != -1 && running) {
            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println("Exception occurred when trying to wait() in putCurrentFloor()");
            }
        }
        // Check if running (again)
        if (running) {
            currentFloor = floor;
        }
        notifyAll();
    }

    /**
     * @return whether the synchronizer is running or not.
     */
    public synchronized boolean isRunning() {
        notifyAll();
        return running;
    }

    /**
     * Stops synchronizer (Sets it to false).
     */
    public synchronized void stopRunning() {
        notifyAll();
        running = false;
    }

    /**
     * @return the destination floor of the elevator (for testing).
     */
    public int testGetDestinationFloor() {
        return destinationFloor;
    }

    /**
     * @return the elevator's status (for testing).
     */
    public int testGetElevatorStatus() {
        return elevatorStatus;
    }

    /**
     * @return the current floor of the elevator (for testing).
     */
    public int testGetCurrentFloor() {
        return currentFloor;
    }

    /**
     * @return the list of requests (for testing).
     */
    public ArrayList<Request> testGetRequests() {
        return requests;
    }
}