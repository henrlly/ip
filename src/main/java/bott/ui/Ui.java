package bott.ui;

import java.util.List;
import java.util.Scanner;

import bott.task.Task;

/**
 * Produces the text of Bott's responses, and drives the console interface:
 * reading the commands the user types and printing Bott's replies wrapped
 * in horizontal dividers.
 *
 * <p>The {@code get...Message} methods return a reply as a plain string
 * (lines separated by {@code "\n"}, no divider or indent). The console
 * uses them via {@link #showMessage(String)}; the GUI uses the same
 * methods to get divider-free text for its dialog bubbles.
 */
public class Ui {

    /** Indent shared by the horizontal divider and every line of message text. */
    private static final String INDENT = "    ";

    /** Horizontal divider printed around every message, indented to match the message text. */
    private static final String HORIZONTAL_LINE =
            INDENT + "____________________________________________________________";

    /** Startup banner spelling out "BOTT", printed once above the greeting. */
    private static final String BANNER =
            " ____     ___     _____   _____ \n"
            + "|  _ \\   / _ \\   |_   _| |_   _|\n"
            + "| |_) | | | | |    | |     | |  \n"
            + "|  _ <  | | | |    | |     | |  \n"
            + "| |_) | | |_| |    | |     | |  \n"
            + "|____/   \\___/     |_|     |_|  \n";

    private final Scanner scanner = new Scanner(System.in);

    /** Prints Bott's startup banner and greeting to the console. */
    public void showWelcome() {
        System.out.println(HORIZONTAL_LINE);
        System.out.print(BANNER);
        showMessage(getWelcomeMessage());
    }

    /**
     * Reads the next line of input typed by the user.
     *
     * @return Line of input typed by the user.
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    /** Prints Bott's farewell message to the console and releases the input resources. */
    public void showGoodbye() {
        showMessage(getGoodbyeMessage());
        scanner.close();
    }

    /**
     * Prints an error message to the console.
     *
     * @param message Description of what went wrong.
     */
    public void showError(String message) {
        showMessage(getErrorMessage(message));
    }

    /**
     * Prints a reply to the console: each line indented and the whole
     * block wrapped in horizontal dividers, followed by a blank line.
     *
     * @param message Reply text, with lines separated by {@code "\n"}.
     */
    public void showMessage(String message) {
        System.out.println(HORIZONTAL_LINE);
        for (String line : message.split("\n", -1)) {
            System.out.println(INDENT + " " + line);
        }
        System.out.println(HORIZONTAL_LINE);
        System.out.println();
    }

    /** Returns Bott's greeting. */
    public String getWelcomeMessage() {
        return "Hello! I'm Bott.\nWhat can I do for you?";
    }

    /** Returns Bott's farewell message. */
    public String getGoodbyeMessage() {
        return "Bye. Hope to see you again soon!";
    }

    /**
     * Returns an error message prefixed with "OOPS!!!".
     *
     * @param message Description of what went wrong.
     */
    public String getErrorMessage(String message) {
        return "OOPS!!! " + message;
    }

    /**
     * Returns the full task list as a numbered message.
     *
     * @param tasks Tasks stored so far.
     */
    public String getTaskListMessage(List<Task> tasks) {
        return numberedTasks("Here are the tasks in your list:", tasks);
    }

    /**
     * Returns the tasks matching a "find" command's keyword as a numbered message.
     *
     * @param matches Tasks whose description matched the keyword, in list order.
     */
    public String getMatchingTasksMessage(List<Task> matches) {
        return numberedTasks("Here are the matching tasks in your list:", matches);
    }

    /**
     * Returns acknowledgement that a task was added.
     *
     * @param task Task that was added.
     * @param taskCount Total number of tasks now stored.
     */
    public String getTaskAddedMessage(Task task, int taskCount) {
        return String.join("\n",
                "Got it. I've added this task:",
                "  " + task,
                "Now you have " + taskCount + " tasks in the list.");
    }

    /**
     * Returns acknowledgement that a task was removed.
     *
     * @param task Task that was removed.
     * @param taskCount Total number of tasks remaining.
     */
    public String getTaskDeletedMessage(Task task, int taskCount) {
        return String.join("\n",
                "Noted. I've removed this task:",
                "  " + task,
                "Now you have " + taskCount + " tasks in the list.");
    }

    /**
     * Returns acknowledgement that a task was marked as done.
     *
     * @param task Task that was marked as done.
     */
    public String getTaskMarkedMessage(Task task) {
        return "Nice! I've marked this task as done:\n  " + task;
    }

    /**
     * Returns acknowledgement that a task was marked as not done.
     *
     * @param task Task that was marked as not done.
     */
    public String getTaskUnmarkedMessage(Task task) {
        return "OK, I've marked this task as not done yet:\n  " + task;
    }

    /**
     * Returns a header line followed by one numbered line per task, e.g.
     * "Here are the tasks in your list:\n1.[T][ ] read book".
     */
    private String numberedTasks(String header, List<Task> tasks) {
        StringBuilder message = new StringBuilder(header);
        for (int i = 0; i < tasks.size(); i++) {
            message.append("\n").append(i + 1).append(".").append(tasks.get(i));
        }
        return message.toString();
    }
}
