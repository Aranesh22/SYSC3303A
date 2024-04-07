/**
 * Timer class to properly measure the timeout events of the state machine.
 *
 * @author YehanDeSilva (101185388)
 */
public class Timer extends Thread {
    // Fields
    private final long duration;
    private final long expectedDuration;
    private final long actualDuration;
    private final Elevator context;
    private final int errorCode;

    /**
     * Constructor for timer object.
     * @param duration Duration (Seconds) of timer.
     * @param actualDuration Actual duration (Seconds) of timer.
     * @param expectedDuration Expected duration (Seconds) of timer.
     * @param context Current context of the state machine.
     */
    public Timer(long duration, long actualDuration, long expectedDuration, Elevator context, int errorCode) {
        this.duration = duration;
        this.actualDuration = actualDuration;
        this.expectedDuration = expectedDuration;
        this.context = context;
        this.errorCode = errorCode;
        this.start();
    }

    /**
     * Main method of timer. Timeout after duration and invoke timeout event on context.
     */
    public void run() {
        // Sleep for the specified duration.
        try {
            Thread.sleep(duration * 1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        if (actualDuration > expectedDuration) {
            // Generate error detected event on the context.
            context.setErrorCode(this.errorCode);
            context.errorDetected();
        } else {
            // Generate timeout event on the context.
            context.setErrorCode(ElevatorError.GOOD);
            context.timerExpired();
        }
    }
}
