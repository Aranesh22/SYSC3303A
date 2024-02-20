import java.util.HashMap;
import java.util.Map;


/**
 * Context Class
 *
 */
public class StateMachine {
    private Map<String, ElevatorState> states;
    private ElevatorState currentState;

    /**
     * State Machine Constructor
     * Initalizes the states hashmap and sets the current state
     */
    public StateMachine(){
        states = new HashMap<>();
        //put states here
        currentState = states.get("state_nameHre");
    }


    /**
     * Sets the current state of the state machine
     * @param stateName - Name of the state to set
     */
    public void setState(String stateName){
        this.currentState = states.get(stateName);
        states.put("SampleState", new SampleState());
    }
}


/**
 * CONCRETE IMPLEMENTATIONS
 */
class SampleState implements ElevatorState{



}