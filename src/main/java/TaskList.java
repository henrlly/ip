import java.util.List;

/**
 * Represents the list of tasks Bott is tracking, and the operations
 * available on it. Task positions are 1-based, matching how the user
 * refers to tasks (e.g. "mark 2").
 */
public class TaskList {

    private final List<Task> tasks;

    /**
     * Creates a task list containing the given tasks.
     *
     * @param tasks Tasks to start with.
     */
    public TaskList(List<Task> tasks) {
        this.tasks = tasks;
    }

    /**
     * Adds a task to the end of the list.
     *
     * @param task Task to add.
     */
    public void add(Task task) {
        tasks.add(task);
    }

    /**
     * Removes and returns the task at the given position.
     *
     * @param taskNumber 1-based position of the task to remove.
     * @return Removed task.
     */
    public Task remove(int taskNumber) {
        return tasks.remove(taskNumber - 1);
    }

    /**
     * Returns the task at the given position.
     *
     * @param taskNumber 1-based position of the task.
     * @return Task at that position.
     */
    public Task get(int taskNumber) {
        return tasks.get(taskNumber - 1);
    }

    /** Returns the number of tasks in the list. */
    public int size() {
        return tasks.size();
    }

    /** Returns the tasks in the list, in order. */
    public List<Task> getTasks() {
        return tasks;
    }
}
