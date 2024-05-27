package service;

import dto.Epic;
import dto.Subtask;
import dto.Task;
import dto.TaskStatus;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class TaskManager {

    private static Long taskNumber = 0L;

    private final HashMap<Long, Task> tasksMap = new HashMap<>();
    private final HashMap<Long, Subtask> subtasksMap = new HashMap<>();
    private final HashMap<Long, Epic> epicsMap = new HashMap<>();

    public Collection<Task> getAllTasks() {
        return tasksMap.values();
    }

    public Collection<Subtask> getAllSubtasks() {
        return subtasksMap.values();
    }

    public Collection<Epic> getAllEpics() {
        return epicsMap.values();
    }

    public void removeAllTasks() {
        tasksMap.clear();
    }

    public void removeAllSubtasks() {
        subtasksMap.clear();
    }

    public void removeAllEpics() {
        epicsMap.clear();
    }

    public Task getTaskById(Long id) {
        return tasksMap.get(id);
    }

    public Subtask getSubtaskById(Long id) {
        return subtasksMap.get(id);
    }

    public Epic getEpicById(Long id) {
        return epicsMap.get(id);
    }

    public void createTask(Task task) {
        taskNumber++;
        task.setId(taskNumber);
        tasksMap.put(taskNumber, task);
    }

    public void createSubtask(Subtask subtask) {
        taskNumber++;
        subtask.setId(taskNumber);
        subtasksMap.put(taskNumber, subtask);
    }

    public void createEpic(Epic epic) {
        taskNumber++;
        epic.setId(taskNumber);
        epicsMap.put(taskNumber, epic);

        for (Subtask subtask : epic.getSubtasks()) {
            if (subtask.getEpic() == null) {
                subtask.setEpic(epic);
            }
        }
    }

    public void updateTask(Task task) {
        tasksMap.put(task.getId(), task);
    }

    public void updateSubtask(Subtask subtask) {
        subtasksMap.put(subtask.getId(), subtask);

        Epic epic = subtask.getEpic();
        List<Subtask> epicSubtasks = epic.getSubtasks();
        epicSubtasks.remove(subtask);
        epicSubtasks.add(subtask);

        if (!subtask.getStatus().equals(epic.getStatus())) {
            switch (subtask.getStatus()) {
                /*
                  Since according to the requirements “the status of a task is updated along with the full update of the task”
                  instead of setting the status, we update the entire epic.
                 */
                case NEW -> {
                    TaskStatus taskStatus = calculateEpicStatus(epic);
                    if (!epic.getStatus().equals(taskStatus)) {
                        epic = new Epic(epic.getId(), epic.getTitle(), epic.getDescription(), taskStatus, epicSubtasks);
                    }
                }
                case IN_PROGRESS -> {
                    if (!epic.getStatus().equals(TaskStatus.IN_PROGRESS)) {
                        epic = new Epic(epic.getId(), epic.getTitle(), epic.getDescription(), TaskStatus.IN_PROGRESS, epicSubtasks);
                    }
                }
                case DONE -> {
                    TaskStatus taskStatus = calculateEpicStatus(epic);
                    if (!epic.getStatus().equals(taskStatus)) {
                        epic = new Epic(epic.getId(), epic.getTitle(), epic.getDescription(), taskStatus, epicSubtasks);
                    }
                }
            }
        }

        epicsMap.put(epic.getId(), epic);
    }

    public void updateEpic(Epic epic) {
        epicsMap.put(epic.getId(), epic);
    }

    public void removeTaskById(Long id) {
        tasksMap.remove(id);
    }

    public void removeSubtaskById(Long id) {
        Subtask subtask = subtasksMap.get(id);
        Epic epic = subtask.getEpic();
        epic.getSubtasks().remove(subtask);

        TaskStatus status = calculateEpicStatus(epic);
        if (!status.equals(epic.getStatus())) {
            epic = new Epic(epic.getId(), epic.getTitle(), epic.getDescription(), status, epic.getSubtasks());
            updateEpic(epic);
        }

        subtasksMap.remove(id);
    }

    public void removeEpicById(Long id) {
        epicsMap.remove(id);
    }

    public List<Subtask> getAllSubTasksByEpic(Epic epic) {
        return epic.getSubtasks();
    }

    private TaskStatus calculateEpicStatus(Epic epic) {
        int statusNewCounter = 0;
        int statusDoneCounter = 0;

        List<Subtask> subtasks = epic.getSubtasks();
        for (Subtask subtask : subtasks) {
            switch (subtask.getStatus()) {
                case NEW -> statusNewCounter++;
                case DONE -> statusDoneCounter++;
            }
        }

        int subtasksSize = subtasks.size();
        if (statusNewCounter == subtasksSize) {
            return TaskStatus.NEW;
        }
        if (statusDoneCounter == subtasksSize) {
            return TaskStatus.DONE;
        }

        return TaskStatus.IN_PROGRESS;
    }
}
