import java.time.LocalDate;

/**
 * Represents a task that starts and ends on specific dates.
 */
public class Event extends Task {

    protected LocalDate from;
    protected LocalDate to;

    /**
     * Creates a new event task.
     *
     * @param description Description of the task.
     * @param from Date the event starts.
     * @param to Date the event ends.
     */
    public Event(String description, LocalDate from, LocalDate to) {
        super(description, TaskType.EVENT);
        this.from = from;
        this.to = to;
    }

    /**
     * Returns this task's textual representation, e.g.
     * "[E][ ] project meeting (from: Aug 06 2019 to: Aug 07 2019)".
     */
    @Override
    public String toString() {
        return super.toString()
                + " (from: " + from.format(DISPLAY_DATE_FORMAT)
                + " to: " + to.format(DISPLAY_DATE_FORMAT) + ")";
    }

    @Override
    public String toFileFormat() {
        return super.toFileFormat() + " | " + from + " | " + to;
    }
}
