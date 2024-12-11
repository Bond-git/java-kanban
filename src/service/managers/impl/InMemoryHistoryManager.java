package service.managers.impl;

import dto.Task;
import service.CustomLinkedHashMap;
import service.managers.HistoryManager;

import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private final CustomLinkedHashMap historyTaskMap = new CustomLinkedHashMap();

    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }

        remove(task.getId());
        historyTaskMap.linkLast(task.copyOf(task));
    }

    @Override
    public List<Task> getHistory() {
        return historyTaskMap.getTasks();
    }

    @Override
    public void remove(Long id) {
        historyTaskMap.removeNode(historyTaskMap.get(id));
    }
}