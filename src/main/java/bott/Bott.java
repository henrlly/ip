package bott;

import bott.parser.Parser;
import bott.storage.Storage;
import bott.task.Task;
import bott.task.TaskList;
import bott.ui.Ui;

/**
 * The main entry point for the Bott chatbot.
 */
public class Bott {

    private static final String SAVED_TASKS_FILE_PATH = "./data/bott.txt";

    private final Ui ui;
    private final Storage storage;
    private final TaskList tasks;

    /**
     * Creates a new Bott instance, loading any tasks previously saved to
     * {@code filePath}.
     *
     * @param filePath Path of the file tasks are loaded from and saved to.
     */
    public Bott(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        tasks = new TaskList(storage.load());
    }

    /** Runs Bott's read-execute loop until the user types "bye". */
    public void run() {
        ui.showWelcome();
        String input = ui.readCommand();
        while (!input.equals("bye")) {
            try {
                executeCommand(input);
            } catch (BottException exception) {
                ui.showError(exception.getMessage());
            }
            input = ui.readCommand();
        }
        ui.showGoodbye();
    }

    /** Starts Bott, backed by the save file at {@link #SAVED_TASKS_FILE_PATH}. */
    public static void main(String[] args) {
        new Bott(SAVED_TASKS_FILE_PATH).run();
    }

    /**
     * Executes a single command entered by the user, other than "bye"
     * (which the caller handles by ending the input loop).
     *
     * @param input Command entered by the user.
     * @throws BottException If {@code input} is not a recognized command,
     *         or is missing information the command needs.
     */
    private void executeCommand(String input) throws BottException {
        String command = Parser.getCommandWord(input);
        String args = Parser.getArguments(input);

        switch (command) {
            case "list":
                ui.showTaskList(tasks.getTasks());
                break;
            case "mark":
                setTaskStatus("mark", args, true);
                break;
            case "unmark":
                setTaskStatus("unmark", args, false);
                break;
            case "delete":
                deleteTask(args);
                break;
            case "todo":
                addTask(Parser.parseTodo(args));
                break;
            case "deadline":
                addTask(Parser.parseDeadline(args));
                break;
            case "event":
                addTask(Parser.parseEvent(args));
                break;
            default:
                throw new BottException(
                        "I don't recognize \"" + command
                                + "\" as a command. Try: list, todo, deadline, event, mark, unmark, delete, or bye.");
        }
    }

    /**
     * Stores a newly created task and prints Bott's acknowledgement.
     *
     * @param task Newly created task to store.
     * @throws BottException If the updated task list cannot be saved.
     */
    private void addTask(Task task) throws BottException {
        tasks.add(task);
        storage.save(tasks.getTasks());
        ui.showTaskAdded(task, tasks.size());
    }

    /**
     * Removes the task named in a "delete" command's arguments and prints
     * Bott's acknowledgement.
     *
     * @param args Text after the "delete" command word.
     * @throws BottException If {@code args} does not name an existing task,
     *         or the updated task list cannot be saved.
     */
    private void deleteTask(String args) throws BottException {
        int taskNumber = Parser.parseTaskNumber("delete", args, tasks.size());
        Task removedTask = tasks.remove(taskNumber);
        storage.save(tasks.getTasks());
        ui.showTaskDeleted(removedTask, tasks.size());
    }

    /**
     * Marks or unmarks the task named in a "mark"/"unmark" command's
     * arguments and prints Bott's response.
     *
     * @param commandName Command word the user typed, "mark" or "unmark",
     *         used to phrase error messages.
     * @param args Text after the command word.
     * @param isDone Whether the task should be marked as done.
     * @throws BottException If {@code args} does not name an existing task,
     *         or the updated task list cannot be saved.
     */
    private void setTaskStatus(String commandName, String args, boolean isDone) throws BottException {
        int taskNumber = Parser.parseTaskNumber(commandName, args, tasks.size());
        Task task = tasks.get(taskNumber);
        if (isDone) {
            task.markAsDone();
            ui.showTaskMarked(task);
        } else {
            task.markAsNotDone();
            ui.showTaskUnmarked(task);
        }
        storage.save(tasks.getTasks());
    }
}
