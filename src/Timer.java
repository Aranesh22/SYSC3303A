/**
 * Timer class to properly measure the timeout events of the state machine.
 *
 * @author YehanDeSilva (101185388)
 */
public class Timer extends Thread {
    // Fields
    private final long duration;
    private final long expectedDuration;
    private final Elevator context;

    /**
     * Constructor for timer object.
     * @param duration Duration (Seconds) of timer.
     * @param expectedDuration Expected duration (Seconds) of timer.
     * @param context Current context of the state machine.
     */
    public Timer(long duration, long expectedDuration, Elevator context) {
        this.duration = duration;
        this.expectedDuration = expectedDuration;
        this.context = context;
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

        if (duration > expectedDuration) {
            // Generate error detected event on the context.
            context.errorDetected();
        } else {
            // Generate timeout event on the context.
            context.timerExpired();
        }
    }
}
