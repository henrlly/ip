import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * The main entry point for the Bott chatbot.
 */
public class Bott {

    /** Indent shared by the horizontal divider and every line of message text. */
    private static final String INDENT = "    ";

    /** Horizontal divider printed around every message, indented to match the message text. */
    private static final String HORIZONTAL_LINE =
        INDENT + "____________________________________________________________";

    private static final String SAVED_TASKS_FILE_PATH = "./data/bott.txt";

    /**
     * Loads previously saved tasks from {@link #SAVED_TASKS_FILE_PATH}.
     *
     * @return Tasks read from the save file, or an empty list if the file
     *         does not exist yet.
     */
    private static List<Task> loadTasks() {
        List<Task> tasks = new ArrayList<>();
        try {
            Scanner fileScanner = new Scanner(new File(SAVED_TASKS_FILE_PATH));
            while (fileScanner.hasNextLine()) {
                Task task = parseSavedTask(fileScanner.nextLine());
                if (task != null) {
                    tasks.add(task);
                }
            }
            fileScanner.close();
        } catch (FileNotFoundException exception) {
            // No save file yet - start with an empty task list.
        }
        return tasks;
    }

    /**
     * Parses one line of the save file into a task. Expected formats:
     * "T | 1 | desc", "D | 1 | desc | by", and "E | 1 | desc | from | to",
     * where the second field is "1" if the task is done, or "0" otherwise.
     *
     * @param line Line read from the save file.
     * @return Task described by {@code line}, or {@code null} if the line
     *         is corrupted and should be skipped.
     */
    private static Task parseSavedTask(String line) {
        String[] fields = line.split(" \\| ");
        try {
            Task task;
            switch (fields[0]) {
            case "T":
                task = new Todo(fields[2]);
                break;
            case "D":
                task = new Deadline(fields[2], LocalDate.parse(fields[3]));
                break;
            case "E":
                task = new Event(fields[2], LocalDate.parse(fields[3]), LocalDate.parse(fields[4]));
                break;
            default:
                throw new IllegalArgumentException("Unknown task type: " + fields[0]);
            }
            if (fields[1].equals("1")) {
                task.markAsDone();
            }
            return task;
        } catch (RuntimeException exception) {
            System.out.println("Skipping corrupted save file line: " + line);
            return null;
        }
    }

    /**
     * Overwrites the save file with the current tasks.
     *
     * @param tasks Tasks stored so far.
     */
    private static void saveTasks(List<Task> tasks) {
        File file = new File(SAVED_TASKS_FILE_PATH);
        file.getParentFile().mkdirs();
        try {
            FileWriter writer = new FileWriter(file);
            for (Task task : tasks) {
                writer.write(task.toFileFormat() + System.lineSeparator());
            }
            writer.close();
        } catch (IOException exception) {
            printMessage("OOPS!!! Could not save tasks: " + exception.getMessage());
        }
    }

    public static void main(String[] args) {
        String banner =
            " ____     ___     _____   _____ \n" +
            "|  _ \\   / _ \\   |_   _| |_   _|\n" +
            "| |_) | | | | |    | |     | |  \n" +
            "|  _ <  | | | |    | |     | |  \n" +
            "| |_) | | |_| |    | |     | |  \n" +
            "|____/   \\___/     |_|     |_|  \n";

        System.out.println(HORIZONTAL_LINE);
        System.out.print(banner);
        printMessage("Hello! I'm Bott.", "What can I do for you?");

        List<Task> tasks = loadTasks();

        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        while (!input.equals("bye")) {
            try {
                executeCommand(input, tasks);
            } catch (BottException exception) {
                printMessage("OOPS!!! " + exception.getMessage());
            }
            input = scanner.nextLine();
        }
        printMessage("Bye. Hope to see you again soon!");
        scanner.close();
    }

    /**
     * Executes a single command entered by the user, other than "bye"
     * (which the caller handles by ending the input loop).
     *
     * @param input Command entered by the user.
     * @param tasks Tasks stored so far.
     * @throws BottException If {@code input} is not a recognized command,
     *         or is missing information the command needs.
     */
    private static void executeCommand(String input, List<Task> tasks)
        throws BottException {
        String[] commandAndArgs = input.split(" ", 2);
        String command = commandAndArgs[0];
        String args = commandAndArgs.length > 1 ? commandAndArgs[1] : "";

        switch (command) {
            case "list":
                printMessage(buildTaskListMessage(tasks));
                break;
            case "mark":
                setTaskStatus(tasks, "mark", args, true);
                break;
            case "unmark":
                setTaskStatus(tasks, "unmark", args, false);
                break;
            case "delete":
                deleteTask(tasks, args);
                break;
            case "todo":
                addTask(tasks, parseTodo(args));
                break;
            case "deadline":
                addTask(tasks, parseDeadline(args));
                break;
            case "event":
                addTask(tasks, parseEvent(args));
                break;
            default:
                throw new BottException(
                    "I don't recognize \"" +
                        command +
                        "\" as a command. Try: list, todo, deadline, event, mark, unmark, delete, or bye."
                );
        }
    }

    /**
     * Builds the full "list" response: a header line followed by one
     * numbered line for each task stored so far.
     *
     * @param tasks Tasks stored so far.
     * @return Lines to print for the "list" command.
     */
    private static String[] buildTaskListMessage(List<Task> tasks) {
        String[] lines = new String[tasks.size() + 1];
        lines[0] = "Here are the tasks in your list:";
        for (int i = 0; i < tasks.size(); i++) {
            lines[i + 1] = i + 1 + "." + tasks.get(i);
        }
        return lines;
    }

    /**
     * Stores a newly created task and prints Bott's acknowledgement.
     *
     * @param tasks Tasks stored so far.
     * @param task Newly created task to store.
     */
    private static void addTask(List<Task> tasks, Task task) {
        tasks.add(task);
        saveTasks(tasks);
        printMessage(
            "Got it. I've added this task:",
            "  " + task,
            "Now you have " + tasks.size() + " tasks in the list."
        );
    }

    /**
     * Removes the task named in a "delete" command's arguments and prints
     * Bott's acknowledgement.
     *
     * @param tasks Tasks stored so far.
     * @param args Text after the "delete" command word.
     * @throws BottException If {@code args} does not name an existing task.
     */
    private static void deleteTask(List<Task> tasks, String args)
        throws BottException {
        int taskNumber = parseTaskNumber("delete", args, tasks.size());
        Task removedTask = tasks.remove(taskNumber - 1);
        saveTasks(tasks);
        printMessage(
            "Noted. I've removed this task:",
            "  " + removedTask,
            "Now you have " + tasks.size() + " tasks in the list."
        );
    }

    /**
     * Parses the arguments of a "todo" command.
     *
     * @param args Text after the "todo" command word.
     * @return Todo task described by {@code args}.
     * @throws BottException If {@code args} has no description.
     */
    private static Todo parseTodo(String args) throws BottException {
        if (args.isBlank()) {
            throw new BottException(
                "A todo needs a description. Try: todo <description>"
            );
        }
        return new Todo(args);
    }

    /**
     * Parses the arguments of a "deadline" command, of the form
     * "{@code <description>} /by {@code <by>}".
     *
     * @param args Text after the "deadline" command word.
     * @return Deadline task described by {@code args}.
     * @throws BottException If {@code args} is missing a description, the
     *         "/by" marker, or a valid date after it.
     */
    private static Deadline parseDeadline(String args) throws BottException {
        if (args.isBlank()) {
            throw new BottException(
                "A deadline needs a description. Try: deadline <description> /by <yyyy-MM-dd>"
            );
        }
        int byIndex = args.indexOf("/by");
        if (byIndex == -1) {
            throw new BottException(
                "A deadline needs a \"/by\" date. Try: deadline <description> /by <yyyy-MM-dd>"
            );
        }
        String description = args.substring(0, byIndex).trim();
        String by = args.substring(byIndex + "/by".length()).trim();
        if (description.isEmpty()) {
            throw new BottException(
                "A deadline needs a description. Try: deadline <description> /by <yyyy-MM-dd>"
            );
        }
        if (by.isEmpty()) {
            throw new BottException(
                "The \"by\" date of a deadline cannot be empty. " +
                    "Try: deadline <description> /by <yyyy-MM-dd>"
            );
        }
        return new Deadline(description, parseDate("by", by));
    }

    /**
     * Parses the arguments of an "event" command, of the form
     * "{@code <description>} /from {@code <from>} /to {@code <to>}".
     *
     * @param args Text after the "event" command word.
     * @return Event task described by {@code args}.
     * @throws BottException If {@code args} is missing a description, the
     *         "/from" or "/to" markers, or either valid date.
     */
    private static Event parseEvent(String args) throws BottException {
        if (args.isBlank()) {
            throw new BottException(
                "An event needs a description. Try: event <description> /from <yyyy-MM-dd> /to <yyyy-MM-dd>"
            );
        }
        int fromIndex = args.indexOf("/from");
        if (fromIndex == -1) {
            throw new BottException(
                "An event needs a \"/from\" start date. " +
                    "Try: event <description> /from <yyyy-MM-dd> /to <yyyy-MM-dd>"
            );
        }
        int toIndex = args.indexOf("/to", fromIndex);
        if (toIndex == -1) {
            throw new BottException(
                "An event needs a \"/to\" end date after its \"/from\" start date. " +
                    "Try: event <description> /from <yyyy-MM-dd> /to <yyyy-MM-dd>"
            );
        }
        String description = args.substring(0, fromIndex).trim();
        String from = args
            .substring(fromIndex + "/from".length(), toIndex)
            .trim();
        String to = args.substring(toIndex + "/to".length()).trim();
        if (description.isEmpty()) {
            throw new BottException(
                "An event needs a description. Try: event <description> /from <yyyy-MM-dd> /to <yyyy-MM-dd>"
            );
        }
        if (from.isEmpty()) {
            throw new BottException(
                "The \"from\" start date of an event cannot be empty. " +
                    "Try: event <description> /from <yyyy-MM-dd> /to <yyyy-MM-dd>"
            );
        }
        if (to.isEmpty()) {
            throw new BottException(
                "The \"to\" end date of an event cannot be empty. " +
                    "Try: event <description> /from <yyyy-MM-dd> /to <yyyy-MM-dd>"
            );
        }
        return new Event(description, parseDate("from", from), parseDate("to", to));
    }

    /**
     * Parses a date in "yyyy-MM-dd" format.
     *
     * @param fieldLabel Name of the field being parsed, used to phrase the error message
     *         (e.g. "by", "from", "to").
     * @param value Text to parse as a date.
     * @return Parsed date.
     * @throws BottException If {@code value} is not a valid "yyyy-MM-dd" date.
     */
    private static LocalDate parseDate(String fieldLabel, String value) throws BottException {
        try {
            return LocalDate.parse(value);
        } catch (DateTimeParseException exception) {
            throw new BottException(
                "The \"" + fieldLabel + "\" date must be in yyyy-MM-dd format (e.g. 2019-10-15). \"" +
                    value + "\" is not a valid date."
            );
        }
    }

    /**
     * Marks or unmarks the task named in a "mark"/"unmark" command's
     * arguments and prints Bott's response.
     *
     * @param tasks Tasks stored so far.
     * @param commandName Command word the user typed, "mark" or "unmark",
     *         used to phrase error messages.
     * @param args Text after the command word.
     * @param isDone Whether the task should be marked as done.
     * @throws BottException If {@code args} does not name an existing task.
     */
    private static void setTaskStatus(
        List<Task> tasks,
        String commandName,
        String args,
        boolean isDone
    ) throws BottException {
        int taskNumber = parseTaskNumber(commandName, args, tasks.size());
        Task task = tasks.get(taskNumber - 1);
        if (isDone) {
            task.markAsDone();
            printMessage("Nice! I've marked this task as done:", "  " + task);
        } else {
            task.markAsNotDone();
            printMessage(
                "OK, I've marked this task as not done yet:",
                "  " + task
            );
        }
        saveTasks(tasks);
    }

    /**
     * Parses and validates the task number argument of a "mark"/"unmark"/
     * "delete" command.
     *
     * @param commandName Command word the user typed, used to phrase error
     *         messages.
     * @param args Text after the command word.
     * @param taskCount Number of tasks currently stored.
     * @return Task number in {@code args}, as a 1-based index.
     * @throws BottException If {@code args} is missing, not a number, or
     *         does not name an existing task.
     */
    private static int parseTaskNumber(
        String commandName,
        String args,
        int taskCount
    ) throws BottException {
        if (args.isBlank()) {
            throw new BottException(
                "Please specify a task number. Try: " +
                    commandName +
                    " <task number>"
            );
        }
        int taskNumber;
        try {
            taskNumber = Integer.parseInt(args.trim());
        } catch (NumberFormatException exception) {
            throw new BottException(
                "\"" +
                    args.trim() +
                    "\" is not a valid task number. " +
                    "Try: " +
                    commandName +
                    " <task number>"
            );
        }
        if (taskNumber < 1 || taskNumber > taskCount) {
            throw new BottException(
                "There is no task number " +
                    taskNumber +
                    " in your list. You currently have " +
                    taskCount +
                    " task(s)."
            );
        }
        return taskNumber;
    }

    /**
     * Prints one or more lines of a chatbot response, wrapped in horizontal
     * dividers and indented to line up with them.
     */
    private static void printMessage(String... lines) {
        System.out.println(HORIZONTAL_LINE);
        for (String line : lines) {
            System.out.println(INDENT + " " + line);
        }
        System.out.println(HORIZONTAL_LINE);
        System.out.println();
    }
}
