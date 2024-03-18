public class ElevatorSubsystem extends Thread {
    private ElevatorRequestBox elevatorRequestBox;
    private ElevatorReceiver elevatorReceiver;
    private Elevator elevator;

    public ElevatorSubsystem(int id) {
        elevatorRequestBox = new ElevatorRequestBox();
        elevatorReceiver = new ElevatorReceiver(elevatorRequestBox);
        elevator = new Elevator(elevatorRequestBox, id);
    }

    public void run() {
        this.elevatorReceiver.start();
        this.elevator.start();
    }
}
