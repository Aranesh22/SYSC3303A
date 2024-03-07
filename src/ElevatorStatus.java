public class ElevatorStatus {

    //Fields
    private final int currentFloor;

    private final int targetFloor;

    private final int recieve;
    

    public ElevatorStatus(int currentFloor, int targetFloor, int recieve) {
        this.currentFloor = currentFloor;
        this.targetFloor = targetFloor;
        this.recieve = recieve;
    }
}
