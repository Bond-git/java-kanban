package service.impl;

import dto.Epic;
import dto.Subtask;
import dto.Task;
import dto.TaskStatus;
import service.HistoryManager;
import service.TaskManager;
import util.Managers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {

    private Long taskNumber = 0L;

    private final HashMap<Long, Task> tasksMap = new HashMap<>();
    private final HashMap<Long, Subtask> subtasksMap = new HashMap<>();
    private final HashMap<Long, Epic> epicsMap = new HashMap<>();

    private final HistoryManager historyManager = Managers.getDefaultHistory();

    @Override
    public Collection<Task> getAllTasks() {
        return tasksMap.values();
    }

    @Override
    public Collection<Subtask> getAllSubtasks() {
        return subtasksMap.values();
    }

    @Override
    public Collection<Epic> getAllEpics() {
        return epicsMap.values();
    }

    @Override
    public void removeAllTasks() {
        tasksMap.clear();
    }

    @Override
    public void removeAllSubtasks() {
        subtasksMap.clear();

        Collection<Epic> epics = epicsMap.values();
        for (Epic epic : epics) {
            epic.setSubtasks(new ArrayList<>());
        }
    }

    @Override
    public void removeAllEpics() {
        epicsMap.clear();
        subtasksMap.clear();
    }

    @Override
    public Task getTaskById(Long id) {
        Task task = tasksMap.get(id);
        historyManager.add(task);
        return task;
    }

    @Override
    public Subtask getSubtaskById(Long id) {
        Subtask subtask = subtasksMap.get(id);
        historyManager.add(subtask);
        return subtask;
    }

    @Override
    public Epic getEpicById(Long id) {
        Epic epic = epicsMap.get(id);
        historyManager.add(epic);
        return epic;
    }

    @Override
    public void createTask(Task task) {
        taskNumber++;
        task.setId(taskNumber);
        tasksMap.put(taskNumber, task);
    }

    @Override
    public void createSubtask(Subtask subtask) {
        taskNumber++;
        subtask.setId(taskNumber);
        subtasksMap.put(taskNumber, subtask);
    }

    @Override
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

    @Override
    public void updateTask(Task task) {
        tasksMap.put(task.getId(), task);
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        subtasksMap.put(subtask.getId(), subtask);

        Epic epic = subtask.getEpic();
        List<Subtask> epicSubtasks = epic.getSubtasks();
        epicSubtasks.remove(subtask);
        epicSubtasks.add(subtask);

        if (!subtask.getStatus().equals(epic.getStatus())) {
            switch (subtask.getStatus()) {
                case NEW -> {
                    TaskStatus taskStatus = calculateEpicStatus(epic);
                    if (!epic.getStatus().equals(taskStatus)) {
                        epic.setStatus(taskStatus);
                    }
                }
                case IN_PROGRESS -> {
                    if (!epic.getStatus().equals(TaskStatus.IN_PROGRESS)) {
                        epic.setStatus(TaskStatus.IN_PROGRESS);
                    }
                }
                case DONE -> {
                    TaskStatus taskStatus = calculateEpicStatus(epic);
                    if (!epic.getStatus().equals(taskStatus)) {
                        epic.setStatus(taskStatus);
                    }
                }
            }
        }

        epicsMap.put(epic.getId(), epic);
    }

    @Override
    public void updateEpic(Epic epic) {
        epicsMap.put(epic.getId(), epic);
    }

    @Override
    public void removeTaskById(Long id) {
        tasksMap.remove(id);
    }

    @Override
    public void removeSubtaskById(Long id) {
        Subtask subtask = subtasksMap.get(id);
        Epic epic = subtask.getEpic();
        List<Subtask> subtasks = epic.getSubtasks();
        subtasks.remove(subtask);
        epic.setSubtasks(subtasks);

        TaskStatus status = calculateEpicStatus(epic);
        if (!status.equals(epic.getStatus())) {
            epic.setStatus(status);
        }

        subtasksMap.remove(id);
    }

    @Override
    public void removeEpicById(Long id) {
        epicsMap.remove(id);
    }

    @Override
    public List<Subtask> getAllSubtasksByEpic(Epic epic) {
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
