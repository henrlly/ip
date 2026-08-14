/**
 * Represents a task with a description, a type, and a done/not-done status.
 */
public class Task {

    protected String description;
    protected boolean isDone;
    protected TaskType type;

    /**
     * Creates a new task with the given description and type. The task
     * starts out not done.
     *
     * @param description Description of the task.
     * @param type Type of the task.
     */
    public Task(String description, TaskType type) {
        this.description = description;
        this.type = type;
        this.isDone = false;
    }

    /**
     * Returns the icon representing this task's status: "X" if done, or a
     * blank space if not done.
     */
    public String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    /** Marks this task as done. */
    public void markAsDone() {
        isDone = true;
    }

    /** Marks this task as not done. */
    public void markAsNotDone() {
        isDone = false;
    }

    /**
     * Returns this task's textual representation, e.g. "[T][X] read book".
     */
    @Override
    public String toString() {
        return "[" + type.getIcon() + "][" + getStatusIcon() + "] " + description;
    }
}
