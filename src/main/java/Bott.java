/**
 * The main entry point for the Bott chatbot.
 */
public class Bott {

    public static void main(String[] args) {
        String horizontalLine = "____________________________________________________________";
        String banner =
            " ____     ___     _____   _____ \n" +
            "|  _ \\   / _ \\   |_   _| |_   _|\n" +
            "| |_) | | | | |    | |     | |  \n" +
            "|  _ <  | | | |    | |     | |  \n" +
            "| |_) | | |_| |    | |     | |  \n" +
            "|____/   \\___/     |_|     |_|  \n";

        System.out.println(horizontalLine);
        System.out.print(banner);
        System.out.println("Hello! I'm Bott.");
        System.out.println("What can I do for you?");
        System.out.println(horizontalLine);
        System.out.println("Bye. Hope to see you again soon!");
        System.out.println(horizontalLine);
    }
}
