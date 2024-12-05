package service;

public class Node<TaskT> {

    public TaskT data;
    public Node<TaskT> prev;
    public Node<TaskT> next;

    public Node(Node<TaskT> prev, TaskT data, Node<TaskT> next) {
        this.prev = prev;
        this.data = data;
        this.next = next;
    }
}
