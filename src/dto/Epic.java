package dto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Epic extends Task {

    private List<Subtask> subtasks;

    public Epic(String title, String description, List<Subtask> subtasks) {
        super(title, description);
        this.subtasks = subtasks;
    }

    public Epic(Long id, String title, String description, TaskStatus status, List<Subtask> subtasks) {
        super(id, title, description, status);
        this.subtasks = subtasks;
    }

    public void setSubtasks(List<Subtask> subtasks) {
        this.subtasks = subtasks;
    }

    public List<Subtask> getSubtasks() {
        return subtasks == null ? Collections.emptyList() : new ArrayList<>(subtasks);
    }

    @Override
    public String toString() {
        return "dto.Epic{" +
                "id=" + getId() +
                ", title='" + getTitle() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + getStatus() +
                ", subtasks=" + getSubtasksIds() +
                '}';
    }

    private List<Long> getSubtasksIds() {
        List<Long> subtasksIds = new ArrayList<>();
        for (Subtask subtask : subtasks) {
            subtasksIds.add(subtask.getId());
        }

        return subtasksIds;
    }
}
