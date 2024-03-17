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

        Thread scheduler, floorSubsystem, floorRequestSimulator, elevatorSubsystem1, elevatorSubsystem2;


        scheduler = new Thread(new Scheduler(), "Scheduler");
        elevatorSubsystem1 = new Thread(new ElevatorSubsystem(1), "ElevatorSubsystem");
        elevatorSubsystem2 = new Thread(new ElevatorSubsystem(2), "ElevatorSubsystem");
        floorSubsystem = new Thread(new FloorSubsystem(), "FloorSubsystem");
        floorRequestSimulator = new Thread(new FloorRequestSimulator(), "FloorRequestSimulator");

        scheduler.start();
        elevatorSubsystem1.start();
        //elevatorSubsystem2.start();
        floorSubsystem.start();
        floorRequestSimulator.start();
    }
}