
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
            Request floorRequest = synchronizer.getDestinationFloor();

            if (floorRequest != null) {
                // Handle the request, for iteration 1, a simple FIFO strategy
                processFloorRequest(floorRequest);

                Response elevatorResponse = synchronizer.getElevatorResponse();
                synchronizer.sendResponseToFloor(elevatorResponse);
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
    private void processFloorRequest(Request request) {
        // In Iteration 1 we return the first elevator (ID 1) for all requests
        // In later iterations method will include logic to select the best elevator
        System.out.println("Scheduler - Received request from floor: " + request.getStartFloor());
        int elevatorId = 1; // Placeholder for the chosen elevator ID.

        // Send request to elevator
        synchronizer.putRequest(elevatorId, request);
        System.out.println("Scheduler - Sent request to Elevator: ");
    }




}
