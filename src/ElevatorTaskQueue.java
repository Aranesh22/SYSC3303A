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
    private final ArrayList<Integer> floorsToVisit;

    /**
     * Constructor
     * @param elevatorStatus Elevator status for the task queue.
     */
    public ElevatorTaskQueue(ElevatorStatus elevatorStatus) {
        this.elevatorStatus = elevatorStatus;
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
     * Update elevator status.
     * @param elevatorStatus Elevator status to be updated to.
     */
    public void setElevatorStatus(ElevatorStatus elevatorStatus) {
        this.elevatorStatus = elevatorStatus;
    }

    /**
     * Adds both the starting and destination floors to floorsToVisit.
     * @param floorRequest floor request to be added.
     */
    public void addFloorRequest(FloorRequest floorRequest) {
        this.addFloorToVisit(floorRequest.getStartFloor());
        this.addFloorToVisit(floorRequest.getDestinationFloor());
    }
    /**
     * Add new floor to visit into sorted queue.
     * @param floor Floor to add.
     */
    public void addFloorToVisit(int floor) {
        // Do not add duplicates to array list
        if (!this.floorsToVisit.contains(floor)) {
            boolean ascending = Objects.equals(this.elevatorStatus.getDirection(), "up");
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
                    if (((this.floorsToVisit.get(i) < floor) && ascending) || ((this.floorsToVisit.get(i) > floor) && !ascending)) {
                        this.floorsToVisit.add(i, floor);
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
            return this.floorsToVisit.removeFirst();
        }
        return 0;
    }

}
