public class Elevator extends Thread {

    private final Synchronizer synchronizer;
    private final int id;
    private int curFloor;
    private final float velocity;
    private final float floorHeight;
    private final float loadUnloadTime;

    public Elevator(Synchronizer synchronizer) {
        this(synchronizer, 1, 1, 1.75f, 3.916f, 7.85f);
    }

    public Elevator(Synchronizer synchronizer, int id, int curFloor, float velocity, float floorHeight, float loadUnloadTime) {
        this.synchronizer = synchronizer;
        this.id = id;
        this.curFloor = curFloor;
        this.velocity = velocity;
        this.floorHeight = floorHeight;
        this.loadUnloadTime = loadUnloadTime;
    }

    @Override
    public void run() {
        // Start off by notifying of starting floor
        this.synchronizer.putElevatorStatus(this.curFloor);
        while (this.synchronizer.isRunning()) {
            this.goToDestinationFloor(this.synchronizer.getDestinationFloor());
        }
    }

    public void goToDestinationFloor(int destinationFloor) {
        boolean movingUp = destinationFloor >= this.curFloor;
        while (this.curFloor != destinationFloor) {
            this.goToFloor(((movingUp)? (this.curFloor + 1 ): (this.curFloor - 1)));
        }
        this.loadUnloadElevator();
    }

    public void goToFloor(int floor) {
        try {
            Thread.sleep(this.calculateTimeTravelFloor() * Math.abs(floor - this.curFloor));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        this.curFloor = floor;
        System.out.println(this + " currently at floor " + this.curFloor);
        this.synchronizer.putElevatorStatus(this.curFloor);
    }

    public void loadUnloadElevator() {
        System.out.println(this + " opening doors.");
        try {
            Thread.sleep((long) this.loadUnloadTime);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println(this + " closing doors");
    }

    public long calculateTimeTravelFloor() {
        return (long) (this.velocity * this.floorHeight);
    }

    @Override
    public String toString() {
        return "Elevator " + this.id;
    }
}