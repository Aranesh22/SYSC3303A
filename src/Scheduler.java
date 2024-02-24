import java.util.Objects;
import java.util.HashMap;
import java.util.Map;

/**
 * The Scheduler class manages elevator requests and coordinates
 * the elevators based on the current system state. It receives requests
 * from floors, processes them, and assigns elevators to fulfill those requests.
 * It also checks the current floor status of elevators and sends the status to the floor.
 *
 * @author Harishan Amutheesan, 101154757
 * @author Yehan De Silva, 101185388
 * @date February 2nd, 2024 / February 22nd, 2024
 * @version iteration1, iteration2
 */
public class Scheduler extends Thread {

    // Fields
    private final Synchronizer synchronizer;
    private Map<String, SchedulerState> states;
    private SchedulerState currentState;

    /**
     * Constructor for Scheduler
     *
     * @param synchronizer An instance of Synchronizer for coordinating with other subsystems
     */
    public Scheduler(Synchronizer synchronizer) {
        this.synchronizer = synchronizer;
        this.states = new HashMap<>();
        initializeStates();
    }

    /**
     * Initializes the states for the Scheduler.
     */
    private void initializeStates() {
        addState("WaitingForFloorRequest", new WaitingForFloorRequest());
        addState("SendingElevatorToStartingFloor", new SendingElevatorToStartingFloor());
        addState("SendingElevatorToDestinationFloor", new SendingElevatorToDestinationFloor());
        setState("WaitingForFloorRequest");
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
        System.out.println("Scheduler: Has exited");
    }

    /**
     * Processes the floor request by determining which elevator should be dispatched.
     * This method is will be more complex in later iterations
     *
     * @param request The request to be processed
     */
    private void processRequest(Request request) {
        if (request == null) {
            this.synchronizer.stopRunning();
            return;
        }

        System.out.println("Scheduler: Received request: " + request);

        this.sendElevatorToFloor(request.getStartFloor());
        this.handleElevatorStatus(request.getStartFloor());
        this.receivedElevatorStatus(); //State transition
        this.sendElevatorToFloor(request.getDestinationFloor());
        this.handleElevatorStatus(request.getDestinationFloor());
        this.endReceived(); //State transition


    }

    /**
     * Continuously attempts to get a valid request or null if no more requests.
     * @return Next Request to service or null if no more requests.
     */
    public Request getValidFloorRequest() {
        Request floorRequest;
        // Loop until we get a valid floor request
        while (true) {
            floorRequest = synchronizer.getRequest();
            // Signifies no more requests
            if (Objects.equals(floorRequest.getTime(), "END_REQUEST")) {

                return null;
            }
            else if (isValidFloorRequest(floorRequest)) {
                this.receivedFloorRequest(); //State transition
                break;
            } else {
                System.out.println("Scheduler: Invalid floor request - " + floorRequest);
            }
        }
        return floorRequest;
    }

    /**
     * Validates if the floor request range is valid
     *
     * @param floorRequest The request to be validated
     * @return true if the floor request is within range, false otherwise
     */
    private boolean isValidFloorRequest(Request floorRequest) {
        return (floorRequest.getStartFloor() >= Floor.DEFAULT_MIN_FLOOR) && (floorRequest.getStartFloor() <= Floor.DEFAULT_MAX_FLOOR)
                && (floorRequest.getDestinationFloor() >= Floor.DEFAULT_MIN_FLOOR) && (floorRequest.getDestinationFloor() <= Floor.DEFAULT_MAX_FLOOR);
    }

    /**
     * Sends elevator to specified floor.
     * @param floor Integer representing floor to send elevator to.
     */
    public void sendElevatorToFloor(int floor) {
        synchronizer.putDestinationFloor(floor);
        System.out.println("Scheduler: Dispatched elevator to floor: " + floor);

    }

    /**
     * Monitors the status of the elevator which it relays to the floor system.
     * Once the elevator arrives at the intended floor, method returns.
     * @param targetFloor Integer representing the final destination floor of the elevator.
     */
    private void handleElevatorStatus(int targetFloor) {
        int elevatorStatus;
        do {
            elevatorStatus = synchronizer.getElevatorStatus();
            synchronizer.putCurrentFloor(elevatorStatus);
        } while (elevatorStatus != targetFloor);

        System.out.println("Scheduler: Elevator has arrived at requested floor " + elevatorStatus);
        endReceived(); //State transition

    }

//-----------------------------------------------Iteration 2--------------------------------------------//
    /**
     * Adds a new state to the state machine.
     * This method is used for the state machine with all possible states.
     *
     * @param stateName The name of the state. This is used as the key in the map.
     * @param state     The state to be added. This is the value associated with the key in the map.
     */
    public void addState(String stateName, SchedulerState state) {
        states.put(stateName, state);
    }

    /**
     * Changes the current state of the state machine.
     * This method is used to transition from one state to another in response to an event.
     *
     * @param stateName The name of the state to transition to. This should match a key in the map.
     */
    public void setState(String stateName){
        System.out.println("[Scheduler-STATE] " + stateName);
        this.currentState = states.get(stateName);
    }

    /**
     * @return the current state of the Scheduler.
     */
    public SchedulerState getCurrentState() {
        return currentState;
    }

    /**
     * Handles the event of receiving an end signal from the floor request.
     * This method is used to delegate the handling of the endReceived event to the current state.
     */
    public void endReceived(){
        currentState.endReceived(this);
    }

    /**
     * Handles the event of receiving a new floor request from a floor.
     * This method is used to delegate the handling of the receivedFloorRequest event to the current state.
     */
    public void receivedFloorRequest(){
        currentState.receivedFloorRequest(this);
    }

    /**
     * Handles the event of receiving an elevator status update.
     * This method is used to delegate the handling of the receivedElevatorStatus event to the current state.
     */
    public void receivedElevatorStatus(){
        currentState.receivedElevatorStatus(this);
    }
}