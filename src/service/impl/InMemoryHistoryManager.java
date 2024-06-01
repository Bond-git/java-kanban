package service.impl;

import dto.Task;
import service.HistoryManager;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private static final int HISTORY_MAX_SIZE = 10;

    private final LinkedList<Task> historyList = new LinkedList<>();

    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }

        if (historyList.size() == HISTORY_MAX_SIZE) {
            historyList.removeFirst();
        }

        historyList.add(task.copyOf(task));
    }

    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(historyList);
    }
}