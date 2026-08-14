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

        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        while (!input.equals("bye")) {
            printMessage(input);
            input = scanner.nextLine();
        }
        printMessage("Bye. Hope to see you again soon!");
        scanner.close();
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
