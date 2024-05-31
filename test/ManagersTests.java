import org.junit.jupiter.api.Test;
import service.HistoryManager;
import service.TaskManager;
import util.Managers;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ManagersTests {

    @Test
    void shouldReturnNotNullHistoryManagerWhenInvokeGetDefaultHistory() {
        HistoryManager historyManager = Managers.getDefaultHistory();

        assertNotNull(historyManager);
    }

    @Test
    void shouldReturnNotNullHistoryManagerWhenInvokeGetDefault() {
        TaskManager taskManager = Managers.getDefault();

        assertNotNull(taskManager);
    }
}
