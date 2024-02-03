/**
 * The Elevator thread class models a traction elevator used to move people around in a building.
 * It realistically models an elevator by replicating the various timing aspects of an elevator that were measured in
 * iteration 0 of the project.
 * Once the scheduler communicates a target floor to the elevator, it will attempt to move to that floor and notify
 * of all floors it passes by or arrives at.
 *
 * @author Yehan De Silva
 * @version iteration1
 * @date Feb 2, 2024
 */

public class Elevator extends Thread {

    // Fields
    private final Synchronizer synchronizer;
    private final int id;
    private int curFloor;
    private final float velocity;
    private final float floorHeight;
    private final float loadUnloadTime;

    // Constants
    public final static float DEFAULT_VELOCITY = 1.75f * 1000;
    public final static float DEFAULT_LOAD_UNLOAD_TIME = 7.85f * 1000;

    /**
     * Default constructor.
     * @param synchronizer Synchronizer that elevator will be using.
     */
    public Elevator(Synchronizer synchronizer) {
        this(synchronizer, 1, 1, Elevator.DEFAULT_VELOCITY, Floor.DEFAULT_FLOOR_HEIGHT, Elevator.DEFAULT_LOAD_UNLOAD_TIME);
    }

    /**
     * Overloaded constructor.
     * @param synchronizer Synchronizer that elevator will be using.
     * @param id Id of elevator.
     * @param curFloor Floor elevator is starting at.
     * @param velocity Speed of the elevator.
     * @param floorHeight Height of a floor in a building.
     * @param loadUnloadTime Total time to load and unload elevator (Doors opening -> Doors closing).
     */
    public Elevator(Synchronizer synchronizer, int id, int curFloor, float velocity, float floorHeight, float loadUnloadTime) {
        this.synchronizer = synchronizer;
        this.id = id;
        this.curFloor = curFloor;
        this.velocity = velocity;
        this.floorHeight = floorHeight;
        this.loadUnloadTime = loadUnloadTime;
    }

    /**
     * Main task of elevator. Keep getting and going to floors.
     */
    @Override
    public void run() {
        // Start off by notifying of starting floor.
        this.synchronizer.putElevatorStatus(this.curFloor);

        // While synchronizer is running, go to the floor we get from synchronizer.
        while (this.synchronizer.isRunning()) {
            this.goToDestinationFloor(this.synchronizer.getDestinationFloor());
        }
    }

    /**
     * Moves elevator to the specified destination floor, passing through all floors in between.
     * @param destinationFloor Integer specifying the target floor we need to end up at.
     */
    public void goToDestinationFloor(int destinationFloor) {
        // Determine if elevator is going up or down and go to each floor in between.
        boolean movingUp = destinationFloor >= this.curFloor;
        while (this.curFloor != destinationFloor) {
            this.goToFloor(((movingUp)? (this.curFloor + 1 ): (this.curFloor - 1)));
        }
        // Once we arrive at destination, load/unload the elevator.
        this.loadUnloadElevator();
    }

    /**
     * Moves elevator to the specified floor.
     * @param floor Integer specifying the target floor we get to.
     */
    public void goToFloor(int floor) {
        // Sleep for the time it takes to go to specified floor.
        try {
            Thread.sleep(this.calculateTimeTravelFloor() * Math.abs(floor - this.curFloor));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        // Update current floor and notify we have arrived at this floor.
        this.curFloor = floor;
        System.out.println(this + ": Currently at floor " + this.curFloor);
        this.synchronizer.putElevatorStatus(this.curFloor);
    }

    /**
     * Simulates an elevator loading/unloading.
     * (Doors opening -> Doors closing).
     */
    public void loadUnloadElevator() {
        System.out.println(this + ": Opening doors.");
        // Sleep for the time it takes to load/unload elevator.
        try {
            Thread.sleep((long) this.loadUnloadTime);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println(this + ": Closing doors");
    }

    /**
     * Calculates the time it takes to travel one floor.
     * @return Long specifying the time it takes to travel one floor.
     */
    public long calculateTimeTravelFloor() {
        return (long) (this.velocity * this.floorHeight);
    }

    /**
     * Converts this elevator object to a readable string.
     * @return String representing this elevator.
     */
    @Override
    public String toString() {
        return "Elevator " + this.id;
    }
}