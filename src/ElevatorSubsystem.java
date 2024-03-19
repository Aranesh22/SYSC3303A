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

    public static void main(String[] args) {
        Thread elevatorSubsystem1 = new Thread(new ElevatorSubsystem(1), "ElevatorSubsystem");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        elevatorSubsystem1.start();
    }
}
