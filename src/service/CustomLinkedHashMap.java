package service;

import dto.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CustomLinkedHashMap {

    private HashMap<Long, Node<Task>> tasksMap;
    private Node<Task> head;
    private Node<Task> tail;

    public CustomLinkedHashMap() {
        this.tasksMap = new HashMap<>();
    }

    public void linkLast(Task element) {
        final Node<Task> oldTail = tail;
        final Node<Task> newNode = new Node<Task>(oldTail, element, null);
        tail = newNode;
        tasksMap.put(element.getId(), newNode);
        if (oldTail == null) {
            head = newNode;
        } else {
            oldTail.next = newNode;
        }
    }


    public List<Task> getTasks() {
        List<Task> tasks = new ArrayList<>();
        Node<Task> currentNode = head;
        while (currentNode != null) {
            tasks.add(currentNode.data);
            currentNode = currentNode.next;
        }
        return tasks;
    }

    public void removeNode(Node<Task> node) {
        if (node != null) {
            final Node<Task> next = node.next;
            final Node<Task> prev = node.prev;

            long nodeTaskId = node.data.getId();
            if (head != null && head.data.getId() == nodeTaskId && tail != null && tail.data.getId() == nodeTaskId) {
                head = null;
                tail = null;
            } else if (head != null && head.data.getId() == nodeTaskId) {
                head = next;
                head.prev = null;
            } else if (tail != null && tail.data.getId() == nodeTaskId) {
                tail = prev;
                tail.next = null;
            } else {
                prev.next = next;
                next.prev = prev;
            }

        }
    }

    public Node<Task> get(Long id) {
        return tasksMap.get(id);
    }
}