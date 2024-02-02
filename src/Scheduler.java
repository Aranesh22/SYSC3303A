
public class Scheduler implements Runnable {
    private final Synchronizer synchronizer;

    // Constructor takes Synchronizer object to communicate between components
    public Scheduler(Synchronizer synchronizer) {
        this.synchronizer = synchronizer;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            // Check for new requests from floors
            int floorRequest = synchronizer.getDestinationFloor();

            // Check for Elevators Current floor status
            int elevatorStatus = synchronizer.getCurrentFloor();

            //Send Elevators current floor status to the floor
            synchronizer.putCurrentFloor(elevatorStatus);


            if (floorRequest < 1 || floorRequest > 7) { // Check if the floor request is valid
                // Handle the request, for iteration 1, a simple FIFO strategy
                processRequest(floorRequest);
//                Response elevatorResponse = synchronizer.getElevatorResponse();
//                synchronizer.sendResponseToFloor(elevatorResponse);
            }else {
                System.out.println("Invalid floor request");
            }

            // Sleep to simulate processing
            try {
                Thread.sleep(100); // Sleep for 100 milliseconds.
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    // Processes floor request and determines which elevator to service the request
    private void processRequest(int request) {
        // In Iteration 1 we return the first elevator (ID 1) for all requests
        // In later iterations method will include logic to select the best elevator
        System.out.println("Scheduler - Received request from floor: " + synchronizer.getRequest().getStartFloor());

        int elevatorId = 1; // Placeholder for the chosen elevator ID.

        // Send request to elevator
        synchronizer.putDestinationFloor(elevatorId, request);
        System.out.println("Scheduler - Sent request to Elevator: ");
    }




}
