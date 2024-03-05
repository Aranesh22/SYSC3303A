public class Scheduler extends Thread {
    private final Synchronizer synchronizer;
    private SchedulerState currentState;
    private Request currentRequest;

    public Scheduler(Synchronizer synchronizer) {
        this.synchronizer = synchronizer;
        setState(new WaitingForFloorRequest()); // Set initial state
    }

    @Override
    public void run() {
        while (synchronizer.isRunning()) {
            currentState.handleState(this);
        }
    }

    public void stopScheduler() {
        System.out.println("Scheduler: Stopping scheduler and exiting.");
        synchronizer.stopRunning();
    }

    // State management methods
    public void setState(SchedulerState newState) {
        this.currentState = newState;
        System.out.println("[Scheduler-STATE]: State changed to " + newState.getClass().getSimpleName());
    }

    //Message passing methods
    public void handleElevatorStatus(int targetFloor) {
        int elevatorStatus;
        do {
            elevatorStatus = synchronizer.getElevatorStatus();
            synchronizer.putCurrentFloor(elevatorStatus);
        } while (elevatorStatus != targetFloor);
        System.out.println("Scheduler: Elevator has arrived at requested floor " + elevatorStatus);
    }

    // Getters and Setters
    public Synchronizer getSynchronizer() {
        return synchronizer;
    }

    public Request getCurrentRequest() {
        return currentRequest;
    }

    public void setCurrentRequest(Request request) {
        this.currentRequest = request;
    }
}
