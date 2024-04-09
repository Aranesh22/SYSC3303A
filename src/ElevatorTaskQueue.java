import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Objects;

/**
 * ElevatorTaskQueue is the data structure holding queue information for each elevator that is stored within the
 * scheduler to be used in the scheduler algorithm.
 *
 * @author Yehan De Silva (101185388)
 * @version iteration3
 */
public class ElevatorTaskQueue {
    // Fields
    private ElevatorStatus elevatorStatus;
    private final InetAddress elevatorIpAddress;
    private final ArrayList<Integer> floorsToVisit;

    /**
     * Constructor
     * @param elevatorStatus Elevator status for the task queue.
     */
    public ElevatorTaskQueue(ElevatorStatus elevatorStatus, InetAddress elevatorIpAddress) {
        this.elevatorStatus = elevatorStatus;
        this.elevatorIpAddress = elevatorIpAddress;
        this.floorsToVisit = new ArrayList<>();
    }

    /**
     * Returns elevator status
     * @return elevator status
     */
    public ElevatorStatus getElevatorStatus() {
        return this.elevatorStatus;
    }

    /**
     * Returns elevator's ip address
     * @return IP address of elevator.
     */
    public InetAddress getElevatorIpAddress() {
        return this.elevatorIpAddress;
    }

    /**
     * Update elevator status.
     * @param elevatorStatus Elevator status to be updated to.
     */
    public void setElevatorStatus(ElevatorStatus elevatorStatus) {
        // Set previous elevator status
        // Set new elevator status
        this.elevatorStatus = elevatorStatus;
    }

    /**
     * Adds both the starting and destination floors to floorsToVisit.
     * @param floorRequest floor request to be added.
     */
    public void addFloorRequest(FloorRequest floorRequest) {
        this.addFloorToVisit(floorRequest.getStartFloor(), floorRequest.getDirection());
        this.addFloorToVisit(floorRequest.getDestinationFloor(), floorRequest.getDirection());
    }

    /**
     * Add new floor to visit into sorted queue.
     * @param floor Floor to add.
     */
    public void addFloorToVisit(int floor, String direction) {
        // Do not add duplicates to array list
        if (!this.floorsToVisit.contains(floor)) {
            boolean ascending = Objects.equals(direction, "up");
            // Add floor if list is empty
            if (this.floorsToVisit.isEmpty()) {
                this.floorsToVisit.add(floor);
            }
            // Add floor if it should be last in the list
            else if (((this.floorsToVisit.getLast() < floor) && ascending) || ((this.floorsToVisit.getLast() > floor) && !ascending)) {
                this.floorsToVisit.add(floor);
            }
            // Add floor into sorted position
            else {
                for (int i = 0; i < this.floorsToVisit.size(); i++) {
                    if (((floor < this.floorsToVisit.get(i)) && ascending) || ((floor > this.floorsToVisit.get(i)) && !ascending)) {
                        this.floorsToVisit.add(i, floor);
                        break;
                    }
                }
            }
        }
    }

    /**
     * Get the next floor to visit.
     * @return the next floor to visit.
     */
    public int nextFloorToVisit() {
        if (!this.floorsToVisit.isEmpty()) {
            return this.floorsToVisit.getFirst();
        }
        return 0;
    }

    /**
     * Removes the next floor to visit.
     */
    public void nextFloorVisited() {
        this.floorsToVisit.removeFirst();
    }

    /**
     * Returns size of floors to visit.
     * @return number of floors to visit.
     */
    public int numNextFloorsToVisit() {
        return this.floorsToVisit.size();
    }

}
