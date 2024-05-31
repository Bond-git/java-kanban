import dto.Epic;
import dto.Subtask;
import dto.Task;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class TaskTests {

    @Test
    void tasksWithEqualIdShouldBeEqual() {
        Task firstTask = new Task("some title", "some desc");
        Task secondTask =  new Task("some title 2", "some desc 2");

        firstTask.setId(1L);
        secondTask.setId(1L);

        assertEquals(firstTask, secondTask);
    }

    @Test
    void subtasksWithEqualIdShouldBeEqual() {
        Subtask firstSubtask = new Subtask("some title", "some desc", null);
        Subtask secondSubtask =  new Subtask("some title 2", "some desc 2", null);

        firstSubtask.setId(1L);
        secondSubtask.setId(1L);

        assertEquals(firstSubtask, secondSubtask);
    }

    @Test
    void epicsWithEqualIdShouldBeEqual() {
        Epic firstEpic = new Epic("some title", "some desc", null);
        Epic secondEpic =  new Epic("some title 2", "some desc 2", null);

        firstEpic.setId(1L);
        secondEpic.setId(1L);

        assertEquals(firstEpic, secondEpic);
    }

    @Test
    void tasksWithDifferentIdShouldNotBeEqual() {
        Task firstTask = new Task("some title", "some desc");
        Task secondTask =  new Task("some title 2", "some desc 2");

        firstTask.setId(1L);
        secondTask.setId(2L);

        assertNotEquals(firstTask, secondTask);
    }

    @Test
    void subtasksWithDifferentIdShouldNotBeEqual() {
        Subtask firstSubtask = new Subtask("some title", "some desc", null);
        Subtask secondSubtask =  new Subtask("some title 2", "some desc 2", null);

        firstSubtask.setId(1L);
        secondSubtask.setId(2L);

        assertNotEquals(firstSubtask, secondSubtask);
    }

    @Test
    void epicsWithDifferentIdShouldNotBeEqual() {
        Epic firstEpic = new Epic("some title", "some desc", null);
        Epic secondEpic =  new Epic("some title 2", "some desc 2", null);

        firstEpic.setId(1L);
        secondEpic.setId(2L);

        assertNotEquals(firstEpic, secondEpic);
    }
}
