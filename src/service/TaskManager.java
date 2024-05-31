package service;

import dto.Epic;
import dto.Subtask;
import dto.Task;

import java.util.Collection;
import java.util.List;

public interface TaskManager {
    Collection<Task> getAllTasks();

    Collection<Subtask> getAllSubtasks();

    Collection<Epic> getAllEpics();

    void removeAllTasks();

    void removeAllSubtasks();

    void removeAllEpics();

    Task getTaskById(Long id);

    Subtask getSubtaskById(Long id);

    Epic getEpicById(Long id);

    void createTask(Task task);

    void createSubtask(Subtask subtask);

    void createEpic(Epic epic);

    void updateTask(Task task);

    void updateSubtask(Subtask subtask);

    void updateEpic(Epic epic);

    void removeTaskById(Long id);

    void removeSubtaskById(Long id);

    void removeEpicById(Long id);

    List<Subtask> getAllSubtasksByEpic(Epic epic);
}
