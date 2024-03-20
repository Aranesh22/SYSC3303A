/**
 * Elevator State Interface Class
 * corresponding context class: Elevator
 * Simulates the following states and events for the Elevator state machine as outlined in the diagram
 *
 * @author Lindsay Dickson
 * @author YehanDeSilva
 * @version Iteration4
 * @date Feb 21, 2024
 */

public abstract class ElevatorState {
    /**
     * Handles the flow of the state
     */
    public void handleState(Elevator context) {
        this.onEntry(context);
        this.doActions(context);
        this.onExit(context);
    };

    /**
     * Actions to do on entry of state
     * @param context Current context of the state machine.
     */
    public void onEntry(Elevator context) {};

    /**
     * Actions to do within a state
     * @param context Current context of the state machine.
     */
    public void doActions(Elevator context){};

    /**
     * Actions to do on exit of state
     * @param context Current context of the state machine.
     */
    public void onExit(Elevator context) {};

    /**
     * Handles the event of receiving a new request from Scheduler
     * Throws error as this method should never be called through polymorphism.
     * @param context Elevator context in which the state transition occurs.
     */
    public void requestReceived(Elevator context) {
        throw new RuntimeException("Illegal Receive Request Event In State " + this);
    };

    /**
     * Handles the event of when the elevator door timer expires
     * Throws error as this method should never be called through polymorphism.
     * @param context The Elevator context in which the state transition occurs.
     */
    public void timerExpired(Elevator context) {
        throw new RuntimeException("Illegal Timer Expired Event In State " + this);
    };

    /**
     * Handles the event of when an elevator error is detected.
     * Throws error as this method should never be called through polymorphism.
     * @param context The Elevator context in which the state transition occurs.
     */
    public void errorDetected(Elevator context) {
        throw new RuntimeException("Illegal Error Detected Event In State " + this);
    };

    /**
     * Returns a string representation of class.
     * @return string representation of class.
     */
    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}

/**
 * Concrete state class representing when an elevator is stationary and its doors are closed
 */
class StationaryDoorsClosed extends ElevatorState {

    /**
     * Actions to do on entry of state
     * @param context Current context of the state machine.
     */
    @Override
    public void onEntry(Elevator context) {
        context.closeDoors();
        context.sendElevatorStatus();
    }

    /**
     * Actions to do on exit of state
     * @param context Current context of the state machine.
     */
    @Override
    public void onExit(Elevator context) {
        context.setState("WaitingForReceiver");
    }

}

/**
 * Concrete state class representing when an elevator is waiting for a request.
 */
class WaitingForReceiver extends ElevatorState {
    /**
     * How to handle current state.
     * @param context Current context of the state machine.
     */
    @Override
    public void handleState(Elevator context) {
        this.doActions(context);
    }

    /**
     * Actions to do within a state
     * @param context Current context of the state machine.
     */
    @Override
    public void doActions(Elevator context) {
        context.getElevatorMessage();
    }

    /**
     * Handles the event of receiving a new request from Scheduler
     * @param context Elevator context in which the state transition occurs.
     */
    @Override
    public void requestReceived(Elevator context) {
        if (context.getDestFloor() == 0) {
            context.sendElevatorStatus();
            context.setState("WaitingForReceiver");
        } else if (context.getCurFloor() == context.getDestFloor()) {
            context.setState("StationaryDoorsOpen");
        } else {
            context.sendElevatorStatus();
            context.setState("MovingDoorsClosed");
        }
    }
}

/**
 * Concrete state class representing when an elevator is stationary and its doors are open
 * to allow passengers to load/unload
 */
class StationaryDoorsOpen extends ElevatorState {
    /**
     * Actions to do on entry of state
     * @param context Current context of the state machine.
     */
    public void onEntry(Elevator context) {
        context.openDoors();
        context.sendElevatorStatus();
        new Timer(context.getLoadUnloadTime(), Elevator.DEFAULT_LOAD_UNLOAD_TIME, context, ElevatorError.DOOR);
    }

    /**
     * Handles the event of when the elevator door timer expires
     * @param context The Elevator context in which the state transition occurs.
     */
    @Override
    public void timerExpired(Elevator context) {
        context.setState("StationaryDoorsClosed");
    }

    /**
     * Handles the event of when an error is detected.
     * @param context The Elevator context in which the state transition occurs.
     */
    @Override
    public void errorDetected(Elevator context) {
        context.resetErrorValues();
        context.setState("StationaryDoorsOpen");
    }

}

/**
 * Concrete state class representing when an elevator is in motion and its doors are closed
 */
class MovingDoorsClosed extends ElevatorState {

    /**
     * How to handle current state.
     * @param context Current context of the state machine.
     */
    @Override
    public void handleState(Elevator context) {
        this.onEntry(context);
    }

    /**
     * Actions to do on entry of state
     * @param context Current context of the state machine.
     */
    public void onEntry(Elevator context) {
        new Timer(context.getFloorTravelTime(), Elevator.DEFAULT_FLOOR_TRAVEL_TIME, context, ElevatorError.STUCK);
    }

    /**
     * Actions to do on exit of state
     * @param context Current context of the state machine.
     */
    public void onExit(Elevator context) {
        context.goToFloor();
    }

    /**
     * Handles the event of when the elevator door timer expires
     * @param context The Elevator context in which the state transition occurs.
     */
    @Override
    public void timerExpired(Elevator context) {
        this.onExit(context);
        if (!context.requestBoxIsEmpty()) {
            context.setState("WaitingForReceiver");
        } else if (context.getCurFloor() == context.getDestFloor()) {
            context.setState("StationaryDoorsOpen");
        } else {
            context.sendElevatorStatus();
            context.setState("MovingDoorsClosed");
        }
    }

    /**
     * Handles the event of when an error is detected.
     * @param context The Elevator context in which the state transition occurs.
     */
    @Override
    public void errorDetected(Elevator context) {
        context.resetErrorValues();
        context.sendElevatorStatus();
        context.shutdown();
    }
}


