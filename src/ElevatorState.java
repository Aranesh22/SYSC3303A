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
     * @param context The Elevator context in which the state transition occurs.
     */
    void receiveRequest(Elevator context);

    /**
     * Handles the event of when the elevator door timer expires
     * @param context The Elevator context in which the state transition occurs.
     */
    void timerExpired(Elevator context);

    /**
     * Handles the event of when the elevator reaches its destination floor
     * @param context The Elevator context in which the state transition occurs.
     */
    void  arriveAtFloor(Elevator context);

     String getSTATENAME() ;
}

/**
 * Concrete state class representing when an elevator is stationary and its doors are closed
 */
class StationaryDoorsClosed implements  ElevatorState{
    /**
     * Event conditional on curFloor != desFloor
     */
    @Override
    public void receiveRequest(Elevator context) {
        context.setState("MovingDoorsClosed");
    }
    public void timerExpired(Elevator context){ /*Timer is not set*/ }
    public void arriveAtFloor(Elevator context){ /*Elevator has not arrived*/ }
    private final String STATENAME = "StationaryDoorsClosed";
    public String getSTATENAME() {

        return STATENAME;
    }
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
     */
    @Override
    public void timerExpired(Elevator context) {
        context.setState("StationaryDoorsClosed");
    }
    @Override
    public void arriveAtFloor(Elevator context) { /* Elevator is Stationary and doors are open*/ }

    private final String STATENAME = "StationaryDoorsOpen";
    public String getSTATENAME() {

        return STATENAME;
    }
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
     */
    @Override
    public void arriveAtFloor(Elevator context) {
        context.setState("StationaryDoorsOpen");
    }

    private final String STATENAME = "MovingDoorsClosed";
    public String getSTATENAME() {

        return STATENAME;
    }

}


