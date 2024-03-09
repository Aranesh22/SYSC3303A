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
     * Handles the flow of the state
     */
    void handleState();
    /**
     * Handles the event of receiving a new request from Scheduler
     *  Elevator context in which the state transition occurs.
     *
     */
    void receiveRequest();

    /**
     * Handles the event of when the elevator door timer expires
     The Elevator context in which the state transition occurs.
     */
    void timerExpired();

    /**
     * Handles the event of when the elevator reaches its destination floor
     *  The Elevator context in which the state transition occurs.
     */
    void  arriveAtFloor();


     String getSTATENAME() ;
}

/**
 * Concrete state class representing when an elevator is stationary and its doors are closed
 */
class StationaryDoorsClosed implements  ElevatorState{
    private Elevator context;

    public StationaryDoorsClosed(Elevator context){
        this.context = context;
        System.out.println("[STATE]["+this.context+"]: State changed to " + getClass().getSimpleName());
        onEntry();
    }

    @Override
    public void handleState(){
        int desFloor = context.getSynchronizer().getDestinationFloor();
        //context.updateDestFloor(desFloor);

        // Wait to receive elevator message (with destination floor) since elevator is stationary
        context.getElevatorMessage();

        //conditional as to whether the current floor == desFLoor
        if(context.getCurFloor() == desFloor){
            arriveAtFloor();
        }else{
            receiveRequest();
        }
    }
    /**
     * Event conditional on curFloor != desFloor
     */
    @Override
    public void receiveRequest() {
        context.setState(new MovingDoorsClosed(context));
    }

    public void timerExpired(){ /*Timer is not set*/ }

    /**
     * Event conditional on CurFloor == desFloor
     */
    public void arriveAtFloor(){
        System.out.println(this.context + ": Opening Doors ");
        context.setState(new StationaryDoorsOpen(context)); }

    /**
     * Entry Action to state: Close Doors
     */
    public void onEntry(){
        System.out.println(this.context + ": Closing Doors");
    }

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
    private Elevator context;

    public StationaryDoorsOpen(Elevator context){
        this.context = context;
        System.out.println("[STATE]["+this.context+"]: State changed to " + getClass().getSimpleName());
        onEntry();
    }

    @Override
    public void handleState(){
        context.loadUnloadElevator();
        timerExpired();
    }

    @Override
    public void receiveRequest() { /*Request is being processed*/ }

    /**
     * Event Conditional on the expiry of the timer for elevator
     */
    @Override
    public void timerExpired() {
        context.setState(new StationaryDoorsClosed(context));
    }
    @Override
    public void arriveAtFloor() { /* Elevator is Stationary and doors are open*/ }

    /**
     * Entry Action to restart the door timer
     */
    public void onEntry(){
        System.out.println(this.context+ ": Door Timer Reset");
    }

    private final String STATENAME = "StationaryDoorsOpen";
    public String getSTATENAME() {

        return STATENAME;
    }
}

/**
 * Concrete state class representing when an elevator is in motion and its doors are closed
 */
class MovingDoorsClosed implements ElevatorState{
    private Elevator context;
    private int destination;

    public MovingDoorsClosed(Elevator context){
        this.context = context;
        System.out.println("[STATE]["+this.context+"]: State changed to " + getClass().getSimpleName());
//        destination = this.context.getSynchronizer().getRequest().getDestinationFloor();
        destination = context.getDestFloor();
    }

    /**
     * This function has replaced  goToDestinationFloor() in the Elevator class
     */
    @Override
    public void handleState(){
        boolean movingUp = destination >= this.context.getCurFloor();
        // Update the context with the current direction of the elevator
        context.setDirection(movingUp);
        this.context.goToFloor(((movingUp)? (this.context.getCurFloor() + 1 ): (this.context.getCurFloor() - 1)));
        if(this.context.getCurFloor()==destination){
            arriveAtFloor();
        }

    }
    @Override
    public void receiveRequest() { /*Elevator is currently servicing a request*/ }
    @Override
    public void timerExpired() { /* No timer active */ }

    /**
     * Event conditional on curFloor == desFloor
     */
    @Override
    public void arriveAtFloor() {
        onExit();
        System.out.println(this.context + ": Opening Doors");
        context.setState(new StationaryDoorsOpen(context));
    }

    public void onExit(){
        this.context.getSynchronizer().putElevatorStatus(this.context.getCurFloor());
        // Send ElevatorStatus to Scheduler
        context.sendElevatorStatus();
        // Check if new elevator message came while elevator was moving
        if (!context.requestBoxIsEmpty()) {
            // Update destination floor
            context.getElevatorMessage();
        }
    }

    private final String STATENAME = "MovingDoorsClosed";
    public String getSTATENAME() {

        return STATENAME;
    }

}


