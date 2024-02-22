/**
 * Elevator State Interface Class
 * corresponding context class: Elevator
 * Simulates the following states and events for the Elevator state machine as outlined in the diagram
 *
 * @author Lindsay Dickson
 * @version Iteration2
 * @date Feb 21, 2024
 */

interface ElevatorState {
    /**
     * Handles the event of receiving a new request from Scheduler
     * @param context
     */
    void receiveRequest(Elevator context);

    /**
     * Handles the event of when the elevator door timer expires
     * @param context
     */
    void timerExpired(Elevator context);

    /**
     * Handles the event of when the elevator reaches its destination floor
     * @param context
     */
    void  arriveAtFloor(Elevator context);
}


/**
 * Concrete state class representing when an elevator is stationary and its doors are closed
 */
class StationaryDoorsClosed implements  ElevatorState{
    /**
     * Event conditional on curFloor != desFloor
     * @param context
     */
    @Override
    public void receiveRequest(Elevator context) {
        context.setState("MovingDoorsClosed");
    }
    public void timerExpired(Elevator context){ /*Timer is not set*/ }
    public void arriveAtFloor(Elevator context){ /*Elevator has not arrived*/ }
}

/**
 * Concrete state class representing when an elevator is stationary and its doors are open
 * to allow passengers to load/unload
 */
class StationaryDoorsOpen implements ElevatorState{
    @Override
    public void receiveRequest(Elevator context) { /*Request is being processed*/ }

    /**
     * Event Conditional on the expiry of the timer for elevator
     * @param context
     */
    @Override
    public void timerExpired(Elevator context) {
        context.setState("StationaryDoorsClosed");
    }
    @Override
    public void arriveAtFloor(Elevator context) { /* Elevator is Stationary and doors are open*/ }
}

/**
 * Concrete state class representing when an elevator is in motion and its doors are closed
 */
class MovingDoorsClosed implements ElevatorState{
    @Override
    public void receiveRequest(Elevator context) { /*Elevator is currently servicing a request*/ }

    @Override
    public void timerExpired(Elevator context) { /* No timer active */ }

    /**
     * Event conditional on curFloor == desFloor
     * @param context
     */
    @Override
    public void arriveAtFloor(Elevator context) {
        context.setState("StationaryDoorsOpen");
    }

}


