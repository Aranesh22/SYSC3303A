/**
 * The main class is responsible for creating & starting the threads
 * Starting point of the program
 *
 * @author Lindsay Dickson, 101160876
 * @version Iteration1
 * @date February 2nd, 2024
 */

public class Main {
    public static void main(String[] args) {

        Synchronizer synchronizer = new Synchronizer();

        Thread elevator, scheduler, floor;

        elevator = new Thread(new Elevator(synchronizer), "Elevator");
        scheduler = new Thread(new Scheduler(synchronizer), "Scheduler");
        floor = new Thread(new Floor(synchronizer), "Floor");

        elevator.start();
        scheduler.start();
        floor.start();
    }
}