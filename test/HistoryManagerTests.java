import dto.Epic;
import dto.Subtask;
import dto.Task;
import dto.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.HistoryManager;
import service.impl.InMemoryHistoryManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class HistoryManagerTests {

    private HistoryManager historyManager;

    @BeforeEach
    void beforeEach() {
        historyManager = new InMemoryHistoryManager();
    }

    @Test
    void shouldSaveTaskInHistoryWithoutChangingTaskState() {
        Task task = new Task("title", "desc");
        task.setStatus(TaskStatus.NEW);
        historyManager.add(task);

        task.setDescription("new desc");
        task.setTitle("new title");
        task.setStatus(TaskStatus.DONE);
        historyManager.add(task);

        List<Task> history = historyManager.getHistory();

        Task firstHistoryTask = history.get(0);
        Task secondHistoryTask = history.get(1);

        assertEquals(2, history.size());
        assertEquals("title", firstHistoryTask.getTitle());
        assertEquals("desc", firstHistoryTask.getDescription());
        assertEquals(TaskStatus.NEW, firstHistoryTask.getStatus());
        assertEquals("new title", secondHistoryTask.getTitle());
        assertEquals("new desc", secondHistoryTask.getDescription());
        assertEquals(TaskStatus.DONE, secondHistoryTask.getStatus());
    }

    @Test
    void shouldSaveSubtaskInHistoryWithoutChangingTaskState() {
        Subtask subtask = new Subtask("title", "desc", null);
        historyManager.add(subtask);

        subtask.setTitle("new title");
        List<Task> history = historyManager.getHistory();
        Task historySubtask = history.get(0);

        assertEquals(1, history.size());
        assertEquals("title", historySubtask.getTitle());
    }

    @Test
    void shouldSaveEpicInHistoryWithoutChangingTaskState() {
        Epic epic = new Epic("title", "desc", null);
        historyManager.add(epic);

        epic.setTitle("new title");
        List<Task> history = historyManager.getHistory();
        Task historyEpic = history.get(0);

        assertEquals(1, history.size());
        assertEquals("title", historyEpic.getTitle());
    }

    @Test
    void shouldRemoveFirstTaskAfterFillHistoryList() {
        for (int i = 0; i <= 10; i++) {
            Task task = new Task("title", "desc");
            task.setId((long) i);
            historyManager.add(task);
        }

        List<Task> history = historyManager.getHistory();
        Task secondTask = history.get(0);
        Task lastTask = history.get(9);
        Task firstTask = new Task("title", "desc");
        firstTask.setId(0L);

        assertEquals(10, history.size());
        assertEquals(1L, secondTask.getId());
        assertEquals(10L, lastTask.getId());
        assertFalse(history.contains(firstTask));
    }
}
