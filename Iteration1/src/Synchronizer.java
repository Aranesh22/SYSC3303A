public class Synchronizer {

    private boolean running;
    private ArrayList<Request> requests;
    private int destinationFloor;
    private int currentFloor;

    public Synchronizer() {

    }

    public synchronized Request getRequest() {
        return null;
    }

    public synchronized void putRequest(Request req) {

    }

    public synchronized int getDestinationFloor() {
        return null;
    }

    public synchronized void putDestinationFloor(int floor) {

    }

    public synchronized int getCurrentFloor() {
        return null;
    }

    public synchronized void putCurrentFloor(int floor) {

    }

    public synchronized boolean isRunning() {
        return null;
    }

    public synchronized void setRunning(boolean running) {

    }


}