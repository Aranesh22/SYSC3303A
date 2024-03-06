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

        Synchronizer synchronizer = new Synchronizer();
        Thread elevator, scheduler, floor;

        scheduler = new Thread(new Scheduler(synchronizer), "Scheduler");
        floor = new Thread(new FloorRequestSimulator(synchronizer), "FloorRequestSimulator");
        elevator = new Thread(new Elevator(synchronizer), "Elevator");

        scheduler.start();
        floor.start();
        elevator.start();
    }
}