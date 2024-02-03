/**
 * The Scheduler class manages elevator requests and coordinates
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
        while (this.synchronizer.isRunning()) {
            processRequest(getValidFloorRequest());
        }
    }

    public Request getValidFloorRequest() {
        Request floorRequest;
        // Loop until we get a valid floor request
        while (true) {
            floorRequest = synchronizer.getRequest();
            if (isValidFloorRequest(floorRequest)) {
                break;
            } else {
                System.out.println("Scheduler: Invalid floor request - " + floorRequest);
            }
        }
        return floorRequest;
    }

    /**
     * Checks the current floor of elevators and updates the floor status
     */
    private void handleElevatorStatus(int targetFloor) {
        int elevatorStatus;
        do {
            elevatorStatus = synchronizer.getElevatorStatus();
            //synchronizer.putCurrentFloor(elevatorStatus);
        } while (elevatorStatus != targetFloor);
        System.out.println("Scheduler: Elevator has arrived at requested floor " + elevatorStatus);
    }


    /**
     * Validates if the floor request is valid
     *
     * @param floorRequest The request to be validated
     * @return true if the floor request is within range, false otherwise
     */
    private boolean isValidFloorRequest(Request floorRequest) {
        return (floorRequest.getStartFloor() >= Floor.MIN_FLOOR ) && (floorRequest.getStartFloor() <= Floor.MAX_FLOOR)
                && (floorRequest.getDestinationFloor() >= Floor.MIN_FLOOR ) && (floorRequest.getDestinationFloor() <= Floor.MAX_FLOOR);
    }

    /**
     * Processes the floor request by determining which elevator should be dispatched.
     * This method is will be more complex in later iterations
     *
     * @param request The request to be processed
     */
    private void processRequest(Request request) {
        System.out.println("Scheduler: Received request: " + request);
        this.sendElevatorToFloor(request.getStartFloor());
        this.handleElevatorStatus(request.getStartFloor());
        this.sendElevatorToFloor(request.getDestinationFloor());
        this.handleElevatorStatus(request.getDestinationFloor());
    }

    public void sendElevatorToFloor(int floor) {
        synchronizer.putDestinationFloor(floor);
        System.out.println("Scheduler: Dispatched elevator to floor: " + floor);
    }

}