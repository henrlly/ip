/**
 * Represents a task that needs to be done before a specific date/time.
 */
public class Deadline extends Task {

    protected String by;

    /**
     * Creates a new deadline task.
     *
     * @param description Description of the task.
     * @param by Date/time by which the task should be done.
     */
    public Deadline(String description, String by) {
        super(description, TaskType.DEADLINE);
        this.by = by;
    }

    /**
     * Returns this task's textual representation, e.g.
     * "[D][ ] return book (by: Sunday)".
     */
    @Override
    public String toString() {
        return super.toString() + " (by: " + by + ")";
    }
}
