/**
 * The main class is responsible for creating & starting the threads
 * Starting point of the program
 *
 * @author Lindsay Dickson, 101160876
 * @version Iteration1
 * @date February 2nd, 2024
 */

public class Main {
    /**
     * Starts the elevator control system/simulator.
     * Creates and starts the scheduler,floor and elevator threads.
     */
    public static void main(String[] args) {
        
        Thread elevator, elevatorReceiver, scheduler, floorSubsystem, floorRequestSimulator;
        ElevatorRequestBox elevatorRequestBox;


        scheduler = new Thread(new Scheduler(), "Scheduler");
        floorSubsystem = new Thread(new FloorSubsystem(), "FloorSubsystem");
        elevatorRequestBox = new ElevatorRequestBox();
        elevatorReceiver = new ElevatorReceiver(elevatorRequestBox);
        elevator = new Thread(new Elevator(elevatorRequestBox), "Elevator");
        floorRequestSimulator = new Thread(new FloorRequestSimulator(), "FloorRequestSimulator");

        scheduler.start();
        floorSubsystem.start();
        elevator.start();
        elevatorReceiver.start();
        floorRequestSimulator.start();
    }
}