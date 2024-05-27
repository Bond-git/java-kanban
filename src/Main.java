import dto.Epic;
import dto.Subtask;
import dto.Task;
import dto.TaskStatus;
import service.TaskManager;

import java.util.ArrayList;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");

        TaskManager taskManager = new TaskManager();

        Task firstTask = new Task("task 1", "desc task 1");
        Task secondTask = new Task("task 2", "desc task 2");
        taskManager.createTask(firstTask);
        taskManager.createTask(secondTask);

        Subtask firstSubtask = new Subtask("sub 1", "desc sub 1", null);
        Subtask secondSubtask = new Subtask("sub 2", "desc sub 2", null);
        Subtask thirdSubtask = new Subtask("sub 3", "desc sub 3", null);
        taskManager.createSubtask(firstSubtask);
        taskManager.createSubtask(secondSubtask);
        taskManager.createSubtask(thirdSubtask);

        Epic firstEpic = new Epic("epic 1", "desc epic 1", new ArrayList<>(Arrays.asList(firstSubtask, secondSubtask)));
        Epic secondEpic = new Epic("epic 2", "desc epic 2", new ArrayList<>(Arrays.asList(thirdSubtask)));
        taskManager.createEpic(firstEpic);
        taskManager.createEpic(secondEpic);

        System.out.println("Созданные задачи:");
        System.out.println(taskManager.getAllEpics());
        System.out.println(taskManager.getAllTasks());
        System.out.println(taskManager.getAllSubtasks());

        taskManager.updateTask(new Task(firstTask.getId(), "updated task 1", "updated desc task 1", TaskStatus.DONE));
        taskManager.updateSubtask(new Subtask(firstSubtask.getId(), "updated sub 1", "updated desc sub 1", TaskStatus.DONE, firstSubtask.getEpic()));
        taskManager.updateSubtask(new Subtask(thirdSubtask.getId(), "updated sub 3", "updated desc sub 3", TaskStatus.DONE, thirdSubtask.getEpic()));

        System.out.println("Задачи обновлены.");
        System.out.println(taskManager.getAllEpics());
        System.out.println(taskManager.getAllSubtasks());

        taskManager.removeSubtaskById(secondSubtask.getId());
        taskManager.removeEpicById(secondEpic.getId());
        taskManager.removeTaskById(secondTask.getId());

        System.out.println("Задачи обновлены, удаление прошло успешно.");
        System.out.println(taskManager.getAllEpics());
        System.out.println(taskManager.getAllTasks());
        System.out.println(taskManager.getAllSubtasks());
    }
}
