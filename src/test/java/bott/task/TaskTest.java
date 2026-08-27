package bott.task;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Tests {@link Task}: status tracking and its two textual representations
 * ({@code toString} for display, {@code toFileFormat} for the save file).
 */
public class TaskTest {

    @Test
    public void getStatusIcon_notDone_returnsSpace() {
        Task task = new Task("read book", TaskType.TODO);
        assertEquals(" ", task.getStatusIcon());
    }

    @Test
    public void getStatusIcon_done_returnsX() {
        Task task = new Task("read book", TaskType.TODO);
        task.markAsDone();
        assertEquals("X", task.getStatusIcon());
    }

    @Test
    public void markAsNotDone_afterMarkAsDone_revertsToNotDone() {
        Task task = new Task("read book", TaskType.TODO);
        task.markAsDone();
        task.markAsNotDone();
        assertEquals(" ", task.getStatusIcon());
    }

    @Test
    public void toString_notDoneTodo_correctFormat() {
        Task task = new Task("read book", TaskType.TODO);
        assertEquals("[T][ ] read book", task.toString());
    }

    @Test
    public void toString_doneTodo_correctFormat() {
        Task task = new Task("read book", TaskType.TODO);
        task.markAsDone();
        assertEquals("[T][X] read book", task.toString());
    }

    @Test
    public void toString_deadlineType_usesDIcon() {
        Task task = new Task("return book", TaskType.DEADLINE);
        assertEquals("[D][ ] return book", task.toString());
    }

    @Test
    public void toString_eventType_usesEIcon() {
        Task task = new Task("trip", TaskType.EVENT);
        assertEquals("[E][ ] trip", task.toString());
    }

    @Test
    public void toFileFormat_notDone_correctFormat() {
        Task task = new Task("read book", TaskType.TODO);
        assertEquals("T | 0 | read book", task.toFileFormat());
    }

    @Test
    public void toFileFormat_done_correctFormat() {
        Task task = new Task("read book", TaskType.TODO);
        task.markAsDone();
        assertEquals("T | 1 | read book", task.toFileFormat());
    }
}
