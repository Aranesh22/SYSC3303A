/**
 * The TimeStamp class is used to measure the time taken by certain operations in the program.
 * It also keeps track of the number of movements made during the timed operation.
 */
public class TimeStamp {
    private long startTime; // The start time of the operation in nanoseconds
    private long endTime; // The end time of the operation in nanoseconds
    private int movements; // The number of movements made during the operation
    private static boolean isTimerStarted = false; // A flag indicating whether the timer has started

    /**
     * The default constructor for the TimeStamp class.
     * It initializes the movements field to 0.
     */
    public TimeStamp() {
        this.movements = 0;
    }

    /**
     * This method starts the timer by setting the startTime to the current time in nanoseconds.
     * It also sets the isTimerStarted flag to true.
     * The timer is only started if it is not already running.
     */
    public void startTimer() {
        if (!isTimerStarted) {
            this.startTime = System.nanoTime();
            isTimerStarted = true;
        }
    }

    /**
     * This method stops the timer by setting the endTime to the current time in nanoseconds.
     */
    public void endTimer() {
        this.endTime = System.nanoTime();
    }

    /**
     * This method increments the movements field by 1.
     */
    public void incrementMovements() {
        this.movements++;
    }

    /**
     * This method prints the time taken by the operation and the number of movements made.
     * It first sets the endTime to the current time in nanoseconds.
     * It then calculates the duration of the operation in milliseconds and converts it to hours, minutes, seconds, and milliseconds.
     * Finally, it prints the number of movements and the duration of the operation.
     */
    public void printMovementTimeStamp() {
        this.endTime = System.nanoTime();
        long durationInMilliseconds = (this.endTime - this.startTime) / 1000000;
        long hours = durationInMilliseconds / 3600000;
        long minutes = (durationInMilliseconds % 3600000) / 60000;
        long seconds = (durationInMilliseconds % 60000) / 1000;
        long milliseconds = durationInMilliseconds % 1000;

        System.out.println("[LOG] Movements: " + this.movements);
        System.out.println(String.format("[LOG] Time taken: %02d:%02d:%02d.%03d", hours, minutes, seconds, milliseconds));
    }

    /**
     * This method prints the time taken by the operation.
     * It first sets the endTime to the current time in nanoseconds.
     * It then calculates the duration of the operation in milliseconds and converts it to hours, minutes, seconds, and milliseconds.
     * Finally, it prints the duration of the operation.
     */
    public void printTimeStamp() {
        this.endTime = System.nanoTime();
        long durationInMilliseconds = (this.endTime - this.startTime) / 1000000;
        long hours = durationInMilliseconds / 3600000;
        long minutes = (durationInMilliseconds % 3600000) / 60000;
        long seconds = (durationInMilliseconds % 60000) / 1000;
        long milliseconds = durationInMilliseconds % 1000;

        System.out.println(String.format("[LOG] State Change Time: %02d:%02d:%02d.%03d", hours, minutes, seconds, milliseconds));
    }
}