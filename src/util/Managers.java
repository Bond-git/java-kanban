package util;

import service.managers.HistoryManager;
import service.managers.TaskManager;
import service.managers.impl.InMemoryHistoryManager;
import service.managers.impl.InMemoryTaskManager;

public class Managers {

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
