package bott;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Tests {@link Bott#getResponse(String)}, the entry point used by the GUI.
 * The command handling it delegates to (Parser, TaskList, Storage) is
 * covered by those classes' own tests; these cases check that a response
 * string comes back for the happy path, the error path, and "bye", and
 * that state carries across calls on the same instance.
 */
public class BottTest {

    @TempDir
    private Path tempDir;

    private Bott newBott() {
        return new Bott(tempDir.resolve("bott.txt").toString());
    }

    @Test
    public void getResponse_todoCommand_addsTaskAndConfirms() {
        String response = newBott().getResponse("todo read book");
        assertEquals(
                "Got it. I've added this task:\n"
                        + "  [T][ ] read book\n"
                        + "Now you have 1 tasks in the list.",
                response);
    }

    @Test
    public void getResponse_listAfterAdds_showsTasksInOrder() {
        Bott bott = newBott();
        bott.getResponse("todo read book");
        bott.getResponse("todo return book");

        String response = bott.getResponse("list");

        assertEquals(
                "Here are the tasks in your list:\n"
                        + "1.[T][ ] read book\n"
                        + "2.[T][ ] return book",
                response);
    }

    @Test
    public void getResponse_unknownCommand_returnsErrorMessage() {
        String response = newBott().getResponse("blah");
        assertTrue(response.startsWith("OOPS!!! I don't recognize \"blah\" as a command."));
    }

    @Test
    public void getResponse_missingTaskNumber_returnsErrorMessage() {
        assertEquals(
                "OOPS!!! Please specify a task number. Try: mark <task number>",
                newBott().getResponse("mark"));
    }

    @Test
    public void getResponse_byeCommand_returnsGoodbyeMessage() {
        assertEquals("Bye. Hope to see you again soon!", newBott().getResponse("bye"));
    }

    @Test
    public void isExitCommand_onlyExactByeMatches() {
        assertTrue(Bott.isExitCommand("bye"));
        assertFalse(Bott.isExitCommand("byebye"));
        assertFalse(Bott.isExitCommand("list"));
    }
}
