package bott.ui;

import java.util.List;
import java.util.Scanner;

import bott.task.Task;

/**
 * Handles all interaction with the user: printing Bott's messages to the
 * console, and reading the commands the user types.
 */
public class Ui {

    /** Indent shared by the horizontal divider and every line of message text. */
    private static final String INDENT = "    ";

    /** Horizontal divider printed around every message, indented to match the message text. */
    private static final String HORIZONTAL_LINE =
            INDENT + "____________________________________________________________";

    private final Scanner scanner = new Scanner(System.in);

    /** Prints Bott's startup banner and greeting. */
    public void showWelcome() {
        String banner =
                " ____     ___     _____   _____ \n"
                + "|  _ \\   / _ \\   |_   _| |_   _|\n"
                + "| |_) | | | | |    | |     | |  \n"
                + "|  _ <  | | | |    | |     | |  \n"
                + "| |_) | | |_| |    | |     | |  \n"
                + "|____/   \\___/     |_|     |_|  \n";
        System.out.println(HORIZONTAL_LINE);
        System.out.print(banner);
        printMessage("Hello! I'm Bott.", "What can I do for you?");
    }

    /**
     * Reads the next line of input typed by the user.
     *
     * @return Line of input typed by the user.
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    /** Prints Bott's farewell message and releases the input resources. */
    public void showGoodbye() {
        printMessage("Bye. Hope to see you again soon!");
        scanner.close();
    }

    /**
     * Prints an error message prefixed with "OOPS!!!".
     *
     * @param message Description of what went wrong.
     */
    public void showError(String message) {
        printMessage("OOPS!!! " + message);
    }

    /**
     * Prints the full task list.
     *
     * @param tasks Tasks stored so far.
     */
    public void showTaskList(List<Task> tasks) {
        String[] lines = new String[tasks.size() + 1];
        lines[0] = "Here are the tasks in your list:";
        for (int i = 0; i < tasks.size(); i++) {
            lines[i + 1] = (i + 1) + "." + tasks.get(i);
        }
        printMessage(lines);
    }

    /**
     * Prints acknowledgement that a task was added.
     *
     * @param task Task that was added.
     * @param taskCount Total number of tasks now stored.
     */
    public void showTaskAdded(Task task, int taskCount) {
        printMessage(
                "Got it. I've added this task:",
                "  " + task,
                "Now you have " + taskCount + " tasks in the list.");
    }

    /**
     * Prints acknowledgement that a task was removed.
     *
     * @param task Task that was removed.
     * @param taskCount Total number of tasks remaining.
     */
    public void showTaskDeleted(Task task, int taskCount) {
        printMessage(
                "Noted. I've removed this task:",
                "  " + task,
                "Now you have " + taskCount + " tasks in the list.");
    }

    /**
     * Prints acknowledgement that a task was marked as done.
     *
     * @param task Task that was marked as done.
     */
    public void showTaskMarked(Task task) {
        printMessage("Nice! I've marked this task as done:", "  " + task);
    }

    /**
     * Prints acknowledgement that a task was marked as not done.
     *
     * @param task Task that was marked as not done.
     */
    public void showTaskUnmarked(Task task) {
        printMessage("OK, I've marked this task as not done yet:", "  " + task);
    }

    /**
     * Prints one or more lines of a chatbot response, wrapped in horizontal
     * dividers and indented to line up with them.
     */
    private void printMessage(String... lines) {
        System.out.println(HORIZONTAL_LINE);
        for (String line : lines) {
            System.out.println(INDENT + " " + line);
        }
        System.out.println(HORIZONTAL_LINE);
        System.out.println();
    }
}
