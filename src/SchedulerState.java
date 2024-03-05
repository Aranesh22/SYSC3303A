public interface SchedulerState {
    void handleState(Scheduler scheduler);
    boolean isValidRequest(Request request);
    void sendElevatorToFloor(Scheduler scheduler, int floor);
}
class WaitingForFloorRequest implements SchedulerState {
    @Override
    public void handleState(Scheduler scheduler) {
        Request request = scheduler.getSynchronizer().getRequest();
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

    @Override
    public boolean isValidRequest(Request request) {
        return request.getStartFloor() >= Floor.DEFAULT_MIN_FLOOR &&
                request.getStartFloor() <= Floor.DEFAULT_MAX_FLOOR &&
                request.getDestinationFloor() >= Floor.DEFAULT_MIN_FLOOR &&
                request.getDestinationFloor() <= Floor.DEFAULT_MAX_FLOOR;
    }

    @Override
    public void sendElevatorToFloor(Scheduler scheduler, int floor) {
        // This method is implemented to adhere to the interface but not used in this state.
    }
}

class SendingElevatorToStartingFloor implements SchedulerState {
    @Override
    public void handleState(Scheduler scheduler) {
        sendElevatorToFloor(scheduler, scheduler.getCurrentRequest().getStartFloor());
    }

    @Override
    public boolean isValidRequest(Request request) {
        // Not used in this state, but required by the interface.
        return true;
    }

    @Override
    public void sendElevatorToFloor(Scheduler scheduler, int floor) {
        System.out.println("Scheduler: Elevator sent to start floor " + floor);
        scheduler.getSynchronizer().putDestinationFloor(floor);
        scheduler.handleElevatorStatus(floor);
        scheduler.setState(new SendingElevatorToDestinationFloor());
    }
}

class SendingElevatorToDestinationFloor implements SchedulerState {
    @Override
    public void handleState(Scheduler scheduler) {
        sendElevatorToFloor(scheduler, scheduler.getCurrentRequest().getDestinationFloor());
    }

    @Override
    public boolean isValidRequest(Request request) {
        // Not used in this state, but required by the interface.
        return true;
    }

    @Override
    public void sendElevatorToFloor(Scheduler scheduler, int floor) {
        System.out.println("Scheduler: Elevator sent to destination floor " + floor);
        scheduler.getSynchronizer().putDestinationFloor(floor);
//        scheduler.handleElevatorStatus(floor);
        // Transition back to waiting state or handle completion logic.
        scheduler.setState(new WaitingForFloorRequest());
    }
}


