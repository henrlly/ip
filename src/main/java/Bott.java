import java.util.Scanner;

/**
 * The main entry point for the Bott chatbot.
 */
public class Bott {

    /** Maximum number of tasks Bott can remember in a single session. */
    private static final int MAX_TASKS = 100;

    /** Indent shared by the horizontal divider and every line of message text. */
    private static final String INDENT = "    ";

    /** Horizontal divider printed around every message, indented to match the message text. */
    private static final String HORIZONTAL_LINE =
            INDENT + "____________________________________________________________";

    public static void main(String[] args) {
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

        Task[] tasks = new Task[MAX_TASKS];
        int taskCount = 0;

        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        while (!input.equals("bye")) {
            if (input.equals("list")) {
                printMessage(buildTaskListMessage(tasks, taskCount));
            } else if (input.startsWith("mark ")) {
                setTaskStatus(tasks, input, true);
            } else if (input.startsWith("unmark ")) {
                setTaskStatus(tasks, input, false);
            } else {
                tasks[taskCount] = new Task(input);
                taskCount++;
                printMessage("added: " + input);
            }
            input = scanner.nextLine();
        }
        printMessage("Bye. Hope to see you again soon!");
        scanner.close();
    }

    /**
     * Builds the full "list" response: a header line followed by one
     * numbered line for each task stored so far.
     *
     * @param tasks Tasks stored so far.
     * @param taskCount Number of tasks currently held in {@code tasks}.
     * @return Lines to print for the "list" command.
     */
    private static String[] buildTaskListMessage(Task[] tasks, int taskCount) {
        String[] lines = new String[taskCount + 1];
        lines[0] = "Here are the tasks in your list:";
        for (int i = 0; i < taskCount; i++) {
            lines[i + 1] = (i + 1) + "." + tasks[i];
        }
        return lines;
    }

    /**
     * Marks or unmarks the task named in a "mark"/"unmark" command and
     * prints Bott's response.
     *
     * @param tasks Tasks stored so far.
     * @param command Full command entered by the user, e.g. "mark 2".
     * @param isDone Whether the task should be marked as done.
     */
    private static void setTaskStatus(Task[] tasks, String command, boolean isDone) {
        int taskNumber = Integer.parseInt(command.substring(command.indexOf(' ') + 1));
        Task task = tasks[taskNumber - 1];
        if (isDone) {
            task.markAsDone();
            printMessage("Nice! I've marked this task as done:", "  " + task);
        } else {
            task.markAsNotDone();
            printMessage("OK, I've marked this task as not done yet:", "  " + task);
        }
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
