/**
 * SchedulerState is an interface that defines the state of the Scheduler.
 * It has a single method handleState that is implemented by the classes that implement this interface.
 * @author Harishan Amutheesan, 101154757
 * @version Iteration2
 * @date Feb 22, 2024
 *
 */
public interface SchedulerState {
    /**
     * This method handles the state of the Scheduler.
     * @param scheduler The Scheduler whose state is being handled.
     */
    void handleState(Scheduler scheduler);
}

/**
 * WaitingForFloorRequest is a class that implements the SchedulerState interface.
 * It represents the state where the Scheduler is waiting for a floor request.
 */
class WaitingForFloorRequest implements SchedulerState {
    /**
     * This method handles the state of the Scheduler when it is waiting for a floor request.
     * It gets the request from the Synchronizer and checks if it is valid.
     * If the request is valid, it sets the current request in the Scheduler and changes its state to SendingElevatorToStartingFloor.
     * If the request is not valid, it skips the request.
     * @param scheduler The Scheduler whose state is being handled.
     */
    @Override
    public void handleState(Scheduler scheduler) {
        FloorRequest request = scheduler.getSynchronizer().getRequest();
        if (request == null || "END_REQUEST".equals(request.getTime())) {
            scheduler.stopScheduler();
            return;
        }
        if (isValidRequest(request)) {
            System.out.println("Scheduler: Processing valid request " + request);
            scheduler.setCurrentRequest(request);
            scheduler.setState(new SendingElevatorToStartingFloor());
        } else {
            System.out.println("Scheduler: Invalid request skipped " + request);
        }
    }

    /**
     * This method checks if a request is valid.
     * A request is valid if the start floor and the destination floor are within the default floor range.
     * @param request The request to be checked.
     * @return true if the request is valid, false otherwise.
     */
    private boolean isValidRequest(FloorRequest request) {
        return request.getStartFloor() >= FloorSubsystem.DEFAULT_MIN_FLOOR &&
                request.getStartFloor() <= FloorSubsystem.DEFAULT_MAX_FLOOR &&
                request.getDestinationFloor() >= FloorSubsystem.DEFAULT_MIN_FLOOR &&
                request.getDestinationFloor() <= FloorSubsystem.DEFAULT_MAX_FLOOR;
    }
}

/**
 * SendingElevatorToStartingFloor is a class that implements the SchedulerState interface.
 * It represents the state where the Scheduler is sending the elevator to the starting floor.
 */
class SendingElevatorToStartingFloor implements SchedulerState {
    /**
     * This method handles the state of the Scheduler when it is sending the elevator to the starting floor.
     * It gets the current request from the Scheduler, sends the elevator to the start floor, and changes its state to SendingElevatorToDestinationFloor.
     * @param scheduler The Scheduler whose state is being handled.
     */
    @Override
    public void handleState(Scheduler scheduler) {
        FloorRequest currentRequest = scheduler.getCurrentRequest();
        System.out.println("Scheduler: Elevator sent to start floor " + currentRequest.getStartFloor());
        scheduler.getSynchronizer().putDestinationFloor(currentRequest.getStartFloor());
        scheduler.handleElevatorStatus(currentRequest.getStartFloor());
        scheduler.setState(new SendingElevatorToDestinationFloor());
    }
}

/**
 * SendingElevatorToDestinationFloor is a class that implements the SchedulerState interface.
 * It represents the state where the Scheduler is sending the elevator to the destination floor.
 */
class SendingElevatorToDestinationFloor implements SchedulerState {
    /**
     * This method handles the state of the Scheduler when it is sending the elevator to the destination floor.
     * It gets the current request from the Scheduler, sends the elevator to the destination floor, and changes its state back to WaitingForFloorRequest.
     * @param scheduler The Scheduler whose state is being handled.
     */
    @Override
    public void handleState(Scheduler scheduler) {
        FloorRequest currentRequest = scheduler.getCurrentRequest();
        System.out.println("Scheduler: Elevator sent to destination floor " + currentRequest.getDestinationFloor());
        scheduler.getSynchronizer().putDestinationFloor(currentRequest.getDestinationFloor());
        scheduler.handleElevatorStatus(currentRequest.getDestinationFloor());
        scheduler.setState(new WaitingForFloorRequest()); // Transition back to waiting state or handle completion logic
    }
}
