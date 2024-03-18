/**
 * The Elevator Request Box class is a thread-safe class that
 * allows the Elevator Receiver thread to put an elevator message
 * to the request box, and the Elevator subsystem to get an elevator message.
 * The get and put methods are synchronized, only allowing one thread
 * to access the class at a given time and perform its operation.
 *
 * @author Pathum Danthanarayana, 101181411
 * @date March 7, 2024
 */
public class ElevatorRequestBox {

    // Fields
    private ElevatorMessage requestBox;
    private boolean empty;

    /**
     * Constructor
     */
    public ElevatorRequestBox() {
        // Initially, box is empty
        requestBox = null;
        empty = true;
    }

    /**
     * Puts an elevator message into the request box.
     * @param request - the elevator message to put into the box
     */
    public synchronized void putRequest(ElevatorMessage request) {
        // Wait until the request box is empty to put
        while (!empty) {
            try {
                wait();
            } catch (InterruptedException e) {
                System.exit(1);
            }
        }
        requestBox = request;
        empty = false;
        // Notify any threads in the wait set
        notifyAll();
    }

    /**
     * Gets an elevator message from the request box.
     * @return an elevator message
     */
    public synchronized ElevatorMessage getRequest() {
        // Wait until the request box has an elevator message
        while (empty) {
            try {
                wait();
            } catch (InterruptedException e) {
                System.exit(1);
            }
        }
        ElevatorMessage requestToReturn = requestBox;
        requestBox = null;
        empty = true;
        // Notify any threads in the wait set
        notifyAll();
        return requestToReturn;
    }

    /**
     * @return true if the request box is empty, and false otherwise
     */
    public synchronized boolean isEmpty() {
        return empty;
    }
}
