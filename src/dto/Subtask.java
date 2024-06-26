package dto;

public class Subtask extends Task {

    private Epic epic;

    public Subtask(String title, String description, Epic epic) {
        super(title, description);
        this.epic = epic;
    }

    public Subtask(Long id, String title, String description, TaskStatus status, Epic epic) {
        super(id, title, description, status);
        this.epic = epic;
    }

    public Epic getEpic() {
        return epic;
    }

    public void setEpic(Epic epic) {
        this.epic = epic;
    }

    @Override
    public String toString() {
        return "dto.Subtask{" +
                "id=" + getId() +
                ", title='" + getTitle() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + getStatus() +
                ", epic=" + getEpicId() +
                '}';
    }

    private String getEpicId() {
        if (epic != null) {
            return epic.getId().toString();
        }

        return null;
    }
}
