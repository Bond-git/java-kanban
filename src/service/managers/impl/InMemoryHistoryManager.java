package service.managers.impl;

import dto.Task;
import service.CustomLinkedHashMap;
import service.managers.HistoryManager;

import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private static final int HISTORY_MAX_SIZE = 10;

    private final CustomLinkedHashMap historyTaskMap = new CustomLinkedHashMap();

    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }

        remove(task.getId());

        if (historyTaskMap.size() == HISTORY_MAX_SIZE) {
            historyTaskMap.removeFirst();
        }

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