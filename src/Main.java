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

        Thread scheduler, floorSubsystem, floorRequestSimulator;
        ElevatorSubsystem elevatorSubsystem;
        ElevatorRequestBox elevatorRequestBox;


        scheduler = new Thread(new Scheduler(), "Scheduler");
        floorSubsystem = new Thread(new FloorSubsystem(), "FloorSubsystem");
        floorRequestSimulator = new Thread(new FloorRequestSimulator(), "FloorRequestSimulator");
        elevatorSubsystem = new ElevatorSubsystem(1);


        scheduler.start();
        floorSubsystem.start();
        floorRequestSimulator.start();
    }
}