/**
 * The Scheduler class manages elevator requests and coordinating
 * the elevators based on the current system state. It receives requests
 * from floors, processes them, and assigns elevators to fulfill those requests.
 * It also checks the current floor status of elevators and sends the status to the floor.
 *
 * @author Harishan Amutheesan, 101154757
 * @date February 2nd, 2024
 */
public class Scheduler implements Runnable {
    private final Synchronizer synchronizer;

    /**
     * Constructor for Scheduler
     *
     * @param synchronizer An instance of Synchronizer for coordinating with other subsystems
     */
    public Scheduler(Synchronizer synchronizer) {
        this.synchronizer = synchronizer;
    }

    /**
     * The main run method executes when the Scheduler thread is started
     * It continuously checks for new requests, the current status of elevators, and processes requests
     */
    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            handleFloorRequests();
            handleElevatorStatus();
        }
    }

    /**
     * Validates incoming floorRequests and processes them if they are valid
     */
    private void handleFloorRequests() {
        int floorRequest = synchronizer.getDestinationFloor();
        if (isValidFloorRequest(floorRequest)) {
            processRequest(floorRequest);
        } else {
            System.out.println("Scheduler: Invalid floor request - " + floorRequest);
        }
    }

    /**
     * Checks the current floor of elevators and updates the floor status
     */
    private void handleElevatorStatus() {
        int elevatorStatus = synchronizer.getElevatorStatus();
        int floorRequest = synchronizer.getDestinationFloor();

        synchronizer.putCurrentFloor(elevatorStatus);

        if (elevatorStatus == floorRequest) {
            System.out.println("Scheduler: Elevator has arrived at requested floor " + elevatorStatus);
        }
    }


    /**
     * Validates if the floor request is valid
     *
     * @param floorRequest The requested floor to be validated
     * @return true if the floor request is within range, false otherwise
     */
    private boolean isValidFloorRequest(int floorRequest) {
        return floorRequest >= 1 && floorRequest <= 7;
    }

    /**
     * Processes the floor request by determining which elevator should be dispatched.
     * This method is will be more complex in later iterations
     *
     * @param request The floor number being requested
     */
    private void processRequest(int request) {
        System.out.println("Scheduler: Received request for floor: " + request);
        synchronizer.putDestinationFloor(request);
        System.out.println("Scheduler: Dispatched elevator to floor: " + request);
    }

}