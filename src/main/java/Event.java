/**
 * Represents a task that starts and ends at specific dates/times.
 */
public class Event extends Task {

    protected String from;
    protected String to;

    /**
     * Creates a new event task.
     *
     * @param description Description of the task.
     * @param from Date/time the event starts.
     * @param to Date/time the event ends.
     */
    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    /**
     * Returns this task's textual representation, e.g.
     * "[E][ ] project meeting (from: Mon 2pm to: 4pm)".
     */
    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + from + " to: " + to + ")";
    }
}
