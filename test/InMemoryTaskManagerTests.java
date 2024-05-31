import dto.Epic;
import dto.Subtask;
import dto.Task;
import dto.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.TaskManager;
import service.impl.InMemoryTaskManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryTaskManagerTests {

    private TaskManager taskManager;

    @BeforeEach
    void beforeEach() {
        taskManager = new InMemoryTaskManager();
    }

    @Test
    void shouldReturnNotNullTaskAfterCreateTaskWhenInvokeGetTaskById() {
        Task taskBeforeCreate = taskManager.getTaskById(1L);

        taskManager.createTask(new Task("title", "desc"));
        Task taskAfterCreate = taskManager.getTaskById(1L);

        assertNull(taskBeforeCreate);
        assertNotNull(taskAfterCreate);
        assertEquals(1L, taskAfterCreate.getId());
        assertEquals("title", taskAfterCreate.getTitle());
        assertEquals("desc", taskAfterCreate.getDescription());
        assertEquals(TaskStatus.NEW, taskAfterCreate.getStatus());
    }

    @Test
    void shouldReturnNotNullSubtaskAfterCreateSubtaskWhenInvokeGetSubtaskById() {
        Subtask subtaskBeforeCreate = taskManager.getSubtaskById(2L);

        Epic epic = new Epic("title epic", "desc epic", new ArrayList<>());
        taskManager.createEpic(epic);
        taskManager.createSubtask(new Subtask("title", "desc", epic));
        Subtask subtaskAfterCreate = taskManager.getSubtaskById(2L);

        assertNull(subtaskBeforeCreate);
        assertNotNull(subtaskAfterCreate);
        assertEquals(2L, subtaskAfterCreate.getId());
        assertEquals("title", subtaskAfterCreate.getTitle());
        assertEquals("desc", subtaskAfterCreate.getDescription());
        assertEquals(epic, subtaskAfterCreate.getEpic());
        assertEquals(TaskStatus.NEW, subtaskAfterCreate.getStatus());
    }

    @Test
    void shouldReturnNotNullEpicAfterCreateEpicWhenInvokeGetEpicById() {
        Epic epicBeforeCreate = taskManager.getEpicById(2L);

        Subtask subtask = new Subtask("title", "desc", null);
        taskManager.createSubtask(subtask);

        List<Subtask> epicSubtasks = new ArrayList<>();
        epicSubtasks.add(subtask);
        taskManager.createEpic(new Epic("title epic", "desc epic", epicSubtasks));
        Epic epicAfterCreate = taskManager.getEpicById(2L);

        assertNull(epicBeforeCreate);
        assertNotNull(epicAfterCreate);
        assertEquals(2L, epicAfterCreate.getId());
        assertEquals("title epic", epicAfterCreate.getTitle());
        assertEquals("desc epic", epicAfterCreate.getDescription());
        assertIterableEquals(epicSubtasks, epicAfterCreate.getSubtasks());
        assertEquals(TaskStatus.NEW, epicAfterCreate.getStatus());
    }

    @Test
    void shouldPossibleCreateTasksWithEqualGeneratedIdAndGivenId() {
        Task taskViaManager = new Task("title task via manager", "desc task via manager");
        taskManager.createTask(taskViaManager);

        Task task = new Task("title", "desc");
        task.setId(1L);

        assertEquals(taskViaManager.getId(), task.getId());
        assertNotEquals(taskViaManager.getTitle(), task.getTitle());
        assertNotEquals(taskViaManager.getDescription(), task.getDescription());
    }

    @Test
    void shouldReturnAllCreatedTasksWhenTheyCreated() {
        Task firstTask = new Task("title", "desc");
        Task secondTask = new Task("title", "desc");

        taskManager.createTask(firstTask);
        taskManager.createTask(secondTask);
        Collection<Task> allTasks = taskManager.getAllTasks();

        assertEquals(2, allTasks.size());
        assertIterableEquals(List.of(firstTask, secondTask), allTasks);
    }

    @Test
    void shouldReturnAllCreatedSubtasksWhenTheyCreated() {
        Subtask firstSubtask = new Subtask("title", "desc", null);
        Subtask secondSubtask = new Subtask("title", "desc", null);

        taskManager.createSubtask(firstSubtask);
        taskManager.createSubtask(secondSubtask);
        Collection<Subtask> allSubtasks = taskManager.getAllSubtasks();

        assertEquals(2, allSubtasks.size());
        assertIterableEquals(List.of(firstSubtask, secondSubtask), allSubtasks);
    }

    @Test
    void shouldReturnAllCreatedEpicsWhenTheyCreated() {
        Epic firstEpic = new Epic("title", "desc", null);
        Epic secondEpic = new Epic("title", "desc", null);

        taskManager.createEpic(firstEpic);
        taskManager.createEpic(secondEpic);
        Collection<Epic> allEpics = taskManager.getAllEpics();

        assertEquals(2, allEpics.size());
        assertIterableEquals(List.of(firstEpic, secondEpic), allEpics);
    }

    @Test
    void shouldReturnEmptyListOfTasksWhenTheyRemoved() {
        Task firstTask = new Task("title", "desc");
        Task secondTask = new Task("title", "desc");

        taskManager.createTask(firstTask);
        taskManager.createTask(secondTask);
        taskManager.removeAllTasks();
        Collection<Task> allTasks = taskManager.getAllTasks();

        assertTrue(allTasks.isEmpty());
    }

    @Test
    void shouldReturnEmptyListOfSubtasksWhenTheyRemoved() {
        Subtask firstSubtask = new Subtask("title", "desc", null);
        Subtask secondSubtask = new Subtask("title", "desc", null);

        List<Subtask> epicSubtasks = new ArrayList<>();
        epicSubtasks.add(firstSubtask);
        epicSubtasks.add(secondSubtask);
        Epic epic = new Epic("title", "desc", epicSubtasks);

        taskManager.createSubtask(firstSubtask);
        taskManager.createSubtask(secondSubtask);
        taskManager.createEpic(epic);
        taskManager.removeAllSubtasks();
        Collection<Subtask> allSubtasks = taskManager.getAllSubtasks();

        assertTrue(allSubtasks.isEmpty());
        assertTrue(epic.getSubtasks().isEmpty());
    }

    @Test
    void shouldReturnEmptyListOfEpicsWhenTheyRemoved() {
        Epic firstEpic = new Epic("title", "desc", null);
        Epic secondEpic = new Epic("title", "desc", null);
        Subtask subtask = new Subtask("title", "desc", firstEpic);

        taskManager.createEpic(firstEpic);
        taskManager.createEpic(secondEpic);
        taskManager.createSubtask(subtask);
        taskManager.removeAllEpics();
        Collection<Epic> allEpics = taskManager.getAllEpics();
        Collection<Subtask> allSubtasks = taskManager.getAllSubtasks();

        assertTrue(allEpics.isEmpty());
        assertTrue(allSubtasks.isEmpty());
    }

    @Test
    void shouldUpdateTaskInTaskManagerMapAfterUpdateTask() {
        Task task = new Task("title", "desc");

        taskManager.createTask(task);
        task.setTitle("new title");
        taskManager.updateTask(task);
        Collection<Task> tasks = taskManager.getAllTasks();

        assertEquals(1, tasks.size());
        assertIterableEquals(tasks, List.of(task));
    }

    @Test
    void shouldReturnStatusInProgressForEpicAfterUpdateSubtaskToInProgress() {
        Subtask subtask = new Subtask("title", "desc", null);
        List<Subtask> epicSubtasks = new ArrayList<>();
        epicSubtasks.add(subtask);
        Epic epic = new Epic("title", "desc", epicSubtasks);

        taskManager.createTask(subtask);
        taskManager.createEpic(epic);
        subtask.setStatus(TaskStatus.IN_PROGRESS);
        taskManager.updateSubtask(subtask);
        Collection<Task> tasks = taskManager.getAllTasks();

        assertEquals(1, tasks.size());
        assertIterableEquals(tasks, List.of(subtask));
        assertEquals(TaskStatus.IN_PROGRESS, subtask.getEpic().getStatus());
    }

    @Test
    void shouldReturnStatusDoneForEpicAfterUpdateSubtaskToDone() {
        Subtask subtask = new Subtask("title", "desc", null);
        List<Subtask> epicSubtasks = new ArrayList<>();
        epicSubtasks.add(subtask);
        Epic epic = new Epic("title", "desc", epicSubtasks);

        taskManager.createTask(subtask);
        taskManager.createEpic(epic);
        subtask.setStatus(TaskStatus.DONE);
        taskManager.updateSubtask(subtask);
        Collection<Task> tasks = taskManager.getAllTasks();

        assertEquals(1, tasks.size());
        assertIterableEquals(tasks, List.of(subtask));
        assertEquals(TaskStatus.DONE, subtask.getEpic().getStatus());
    }

    @Test
    void shouldReturnStatusInProgressForEpicAfterUpdateOneOfSubtaskToDoneWithAnotherInNew() {
        Subtask firstTask = new Subtask("title", "desc", null);
        Subtask secondTask = new Subtask("title", "desc", null);

        List<Subtask> epicSubtasks = new ArrayList<>();
        epicSubtasks.add(firstTask);
        epicSubtasks.add(secondTask);
        Epic epic = new Epic("title", "desc", epicSubtasks);

        taskManager.createTask(firstTask);
        taskManager.createEpic(epic);
        firstTask.setStatus(TaskStatus.DONE);
        taskManager.updateSubtask(firstTask);
        Collection<Task> tasks = taskManager.getAllTasks();

        assertEquals(1, tasks.size());
        assertIterableEquals(tasks, List.of(firstTask));
        assertEquals(TaskStatus.IN_PROGRESS, firstTask.getEpic().getStatus());
    }

    @Test
    void shouldReturnStatusInProgressForEpicAfterUpdateOneOfSubtaskToNew() {
        Subtask firstTask = new Subtask("title", "desc", null);
        Subtask secondTask = new Subtask("title", "desc", null);

        List<Subtask> epicSubtasks = new ArrayList<>();
        epicSubtasks.add(firstTask);
        epicSubtasks.add(secondTask);
        Epic epic = new Epic("title", "desc", epicSubtasks);

        taskManager.createTask(firstTask);
        taskManager.createTask(secondTask);
        taskManager.createEpic(epic);
        epic.setStatus(TaskStatus.DONE);
        firstTask.setStatus(TaskStatus.DONE);
        secondTask.setStatus(TaskStatus.NEW);
        taskManager.updateSubtask(secondTask);
        Collection<Task> tasks = taskManager.getAllTasks();

        assertEquals(2, tasks.size());
        assertIterableEquals(tasks, List.of(firstTask, secondTask));
        assertEquals(TaskStatus.IN_PROGRESS, firstTask.getEpic().getStatus());
    }

    @Test
    void shouldReturnStatusNewForEpicAfterUpdateSubtaskToNew() {
        Subtask subtask = new Subtask("title", "desc", null);
        List<Subtask> epicSubtasks = new ArrayList<>();
        epicSubtasks.add(subtask);
        Epic epic = new Epic("title", "desc", epicSubtasks);

        taskManager.createTask(subtask);
        taskManager.createEpic(epic);
        epic.setStatus(TaskStatus.DONE);
        taskManager.updateSubtask(subtask);
        Collection<Task> tasks = taskManager.getAllTasks();

        assertEquals(1, tasks.size());
        assertIterableEquals(tasks, List.of(subtask));
        assertEquals(TaskStatus.NEW, subtask.getEpic().getStatus());
    }

    @Test
    void shouldUpdateEpicInTaskManagerMapAfterUpdateEpic() {
        Epic epic = new Epic("title", "desc", null);

        taskManager.createEpic(epic);
        epic.setTitle("new title");
        taskManager.updateEpic(epic);
        Collection<Epic> epics = taskManager.getAllEpics();

        assertEquals(1, epics.size());
        assertIterableEquals(epics, List.of(epic));
    }

    @Test
    void shouldReturnNullWhenGetTaskByIdAfterRemoveTaskById() {
        Task task = new Task("title", "desc");

        taskManager.createTask(task);
        taskManager.removeTaskById(task.getId());
        Task taskById = taskManager.getTaskById(task.getId());

        assertNull(taskById);
    }

    @Test
    void shouldReturnNullWhenGetSubtaskByIdAfterRemoveSubtaskById() {
        Epic epic = new Epic("title", "desc", null);
        Subtask subtask = new Subtask("title", "desc", epic);

        taskManager.createEpic(epic);
        taskManager.createSubtask(subtask);
        taskManager.removeSubtaskById(subtask.getId());
        Subtask subtaskById = taskManager.getSubtaskById(subtask.getId());

        assertNull(subtaskById);
        assertTrue(epic.getSubtasks().isEmpty());
    }

    @Test
    void shouldReturnEpicStatusNewWhenRemoveOneOfSubtaskAndStaysAnotherSubtaskWithStatusDone() {
        Subtask subtask = new Subtask("title", "desc", null);
        Subtask secondSubtask = new Subtask("title", "desc", null);

        List<Subtask> epicSubtasks = new ArrayList<>();
        epicSubtasks.add(subtask);
        epicSubtasks.add(secondSubtask);
        Epic epic = new Epic("title", "desc", epicSubtasks);

        taskManager.createSubtask(subtask);
        taskManager.createSubtask(secondSubtask);
        taskManager.createEpic(epic);

        secondSubtask.setStatus(TaskStatus.DONE);
        taskManager.updateSubtask(secondSubtask);

        assertEquals(TaskStatus.IN_PROGRESS, epic.getStatus());
        taskManager.removeSubtaskById(subtask.getId());
        assertEquals(TaskStatus.DONE, epic.getStatus());
    }

    @Test
    void shouldReturnNullWhenGetEpicByIdAfterRemoveEpicById() {
        Epic epic = new Epic("title", "desc", null);

        taskManager.createEpic(epic);
        taskManager.removeEpicById(epic.getId());
        Epic epicById = taskManager.getEpicById(epic.getId());

        assertNull(epicById);
    }

    @Test
    void shouldReturnAllEpicSubtasksAfterInbokeGetAllSubtasksByEpic() {
        Subtask firstTask = new Subtask("title", "desc", null);
        Subtask secondTask = new Subtask("title", "desc", null);

        List<Subtask> epicSubtasks = new ArrayList<>();
        epicSubtasks.add(firstTask);
        epicSubtasks.add(secondTask);
        Epic epic = new Epic("title", "desc", epicSubtasks);

        taskManager.createSubtask(firstTask);
        taskManager.createSubtask(secondTask);
        taskManager.createEpic(epic);
        List<Subtask> allSubtasksByEpic = taskManager.getAllSubtasksByEpic(epic);

        assertIterableEquals(allSubtasksByEpic, List.of(firstTask, secondTask));
    }
}
