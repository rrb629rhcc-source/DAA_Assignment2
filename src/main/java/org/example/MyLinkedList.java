package org.example;

public class MyLinkedList {
    private static class Node {
        int data;
        Node next;

        Node(int data) {
            this.data = data;
            this.next = null;
        }
    }

    private Node head;
    private int size;
    private final Metrics metrics;

    public MyLinkedList(Metrics metrics) {
        this.head = null;
        this.size = 0;
        this.metrics = metrics;
    }

    public int size() {
        return size;
    }

    public void add(int x) {
        Node newNode = new Node(x);
        metrics.moves++;
        if (head == null) {
            head = newNode;
            metrics.moves++;
        } else {
            Node curr = head;
            metrics.steps++;
            while (curr.next != null) {
                metrics.steps++;
                curr = curr.next;
            }
            curr.next = newNode;
            metrics.moves++;
        }
        size++;
    }

    public void add(int index, int x) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index out of bounds: " + index);
        }
        Node newNode = new Node(x);
        metrics.moves++;

        if (index == 0) {
            newNode.next = head;
            metrics.moves++;
            head = newNode;
            metrics.moves++;
        } else {
            Node curr = head;
            metrics.steps++;
            for (int i = 0; i < index - 1; i++) {
                metrics.steps++;
                curr = curr.next;
            }
            newNode.next = curr.next;
            metrics.moves++;
            curr.next = newNode;
            metrics.moves++;
        }
        size++;
    }

    public int remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index out of bounds: " + index);
        }
        int removedVal;
        if (index == 0) {
            metrics.steps++;
            removedVal = head.data;
            head = head.next;
            metrics.moves++;
        } else {
            Node curr = head;
            metrics.steps++;
            for (int i = 0; i < index - 1; i++) {
                metrics.steps++;
                curr = curr.next;
            }
            metrics.steps++;
            removedVal = curr.next.data;
            curr.next = curr.next.next;
            metrics.moves++;
        }
        size--;
        return removedVal;
    }

    public int get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index out of bounds: " + index);
        }
        Node curr = head;
        metrics.steps++;
        for (int i = 0; i < index; i++) {
            metrics.steps++;
            curr = curr.next;
        }
        return curr.data;
    }

    public boolean contains(int x) {
        Node curr = head;
        while (curr != null) {
            metrics.steps++;
            metrics.comparisons++;
            if (curr.data == x) {
                return true;
            }
            curr = curr.next;
        }
        return false;
    }
}