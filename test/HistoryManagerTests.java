import dto.Epic;
import dto.Subtask;
import dto.Task;
import dto.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.managers.HistoryManager;
import service.managers.impl.InMemoryHistoryManager;

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
    void shouldRemoveAndSaveTaskInHistoryAfterSecondAdd() {
        Task task = new Task(1l, "title", "desc", TaskStatus.NEW);
        historyManager.add(task);
        historyManager.add(task);

        List<Task> history = historyManager.getHistory();

        assertEquals(1l, history.size());
        assertEquals("title", task.getTitle());
        assertEquals("desc", task.getDescription());
        assertEquals(TaskStatus.NEW, task.getStatus());
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
    void shouldRemoveNodeAfterRemoveByTaskId() {
        Task firstTask = new Task(1l, "title", "desc", TaskStatus.NEW);
        Task secondTask = new Task(2l, "title", "desc", TaskStatus.NEW);
        historyManager.add(firstTask);
        historyManager.add(secondTask);

        historyManager.remove(firstTask.getId());

        List<Task> history = historyManager.getHistory();
        assertEquals(1, history.size());
        assertEquals(2, history.get(0).getId());
    }
}