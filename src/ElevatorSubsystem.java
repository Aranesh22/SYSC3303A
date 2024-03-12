public class ElevatorSubsystem {
    private ElevatorRequestBox elevatorRequestBox;
    private ElevatorReceiver elevatorReceiver;
    private Elevator elevator;

    public ElevatorSubsystem(int id) {
        elevatorRequestBox = new ElevatorRequestBox();
        elevatorReceiver = new ElevatorReceiver(elevatorRequestBox);
        elevator = new Elevator(elevatorRequestBox, id);
        elevatorReceiver.start();
    }
}
