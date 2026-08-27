package bott.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * Tests {@link TaskList}. Positions are 1-based (matching how the user
 * refers to tasks), while the underlying storage is 0-based, so the
 * add/remove/get boundary cases here specifically target that conversion.
 */
public class TaskListTest {

    private static TaskList threeTaskList() {
        List<Task> tasks = new ArrayList<>();
        tasks.add(new Todo("first"));
        tasks.add(new Todo("second"));
        tasks.add(new Todo("third"));
        return new TaskList(tasks);
    }

    @Test
    public void size_emptyList_returnsZero() {
        assertEquals(0, new TaskList(new ArrayList<>()).size());
    }

    @Test
    public void add_toEmptyList_sizeBecomesOne() {
        TaskList taskList = new TaskList(new ArrayList<>());
        Task task = new Todo("read book");
        taskList.add(task);
        assertEquals(1, taskList.size());
        assertSame(task, taskList.get(1));
    }

    @Test
    public void add_multipleTasks_appendsInOrder() {
        TaskList taskList = threeTaskList();
        assertEquals(3, taskList.size());
        assertEquals("[T][ ] first", taskList.get(1).toString());
        assertEquals("[T][ ] second", taskList.get(2).toString());
        assertEquals("[T][ ] third", taskList.get(3).toString());
    }

    @Test
    public void get_firstPosition_returnsFirstTask() {
        TaskList taskList = threeTaskList();
        assertEquals("[T][ ] first", taskList.get(1).toString());
    }

    @Test
    public void get_lastPosition_returnsLastTask() {
        TaskList taskList = threeTaskList();
        assertEquals("[T][ ] third", taskList.get(3).toString());
    }

    @Test
    public void remove_firstPosition_removesFirstAndShiftsRestDown() {
        TaskList taskList = threeTaskList();
        Task removed = taskList.remove(1);
        assertEquals("[T][ ] first", removed.toString());
        assertEquals(2, taskList.size());
        assertEquals("[T][ ] second", taskList.get(1).toString());
        assertEquals("[T][ ] third", taskList.get(2).toString());
    }

    @Test
    public void remove_lastPosition_removesLastWithoutAffectingRest() {
        TaskList taskList = threeTaskList();
        Task removed = taskList.remove(3);
        assertEquals("[T][ ] third", removed.toString());
        assertEquals(2, taskList.size());
        assertEquals("[T][ ] first", taskList.get(1).toString());
        assertEquals("[T][ ] second", taskList.get(2).toString());
    }

    @Test
    public void remove_middlePosition_removesOnlyThatTask() {
        TaskList taskList = threeTaskList();
        Task removed = taskList.remove(2);
        assertEquals("[T][ ] second", removed.toString());
        assertEquals(2, taskList.size());
        assertEquals("[T][ ] first", taskList.get(1).toString());
        assertEquals("[T][ ] third", taskList.get(2).toString());
    }

    @Test
    public void getTasks_returnsUnderlyingListInOrder() {
        TaskList taskList = threeTaskList();
        List<Task> tasks = taskList.getTasks();
        assertEquals(3, tasks.size());
        assertEquals("[T][ ] first", tasks.get(0).toString());
        assertEquals("[T][ ] third", tasks.get(2).toString());
    }
}
