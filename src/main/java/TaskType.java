/**
 * Represents the kind of a task, and the single-letter icon used to
 * display it, e.g. "[T]" for {@link #TODO}.
 */
public enum TaskType {
    TODO('T'),
    DEADLINE('D'),
    EVENT('E');

    private final char icon;

    TaskType(char icon) {
        this.icon = icon;
    }

    /** Returns the single-letter icon representing this task type. */
    public char getIcon() {
        return icon;
    }
}
