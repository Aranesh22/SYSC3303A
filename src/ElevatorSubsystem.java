public class ElevatorSubsystem extends Thread {
    private ElevatorRequestBox elevatorRequestBox;
    private ElevatorReceiver elevatorReceiver;
    private Elevator elevator;

    public ElevatorSubsystem(int id, String errorFileName) {
        elevatorRequestBox = new ElevatorRequestBox();
        elevatorReceiver = new ElevatorReceiver(elevatorRequestBox);
        elevator = new Elevator(elevatorRequestBox, id, errorFileName);
    }

    public void run() {
        this.elevatorReceiver.start();
        this.elevator.start();
    }

    public static void main(String[] args) {
        Thread elevatorSubsystem1 = new Thread(new ElevatorSubsystem(Integer.parseInt(args[0]), args[1]), "ElevatorSubsystem");
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        elevatorSubsystem1.start();
    }
}
