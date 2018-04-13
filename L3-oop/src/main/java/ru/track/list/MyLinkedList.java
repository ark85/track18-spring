package ru.track.list;

import java.util.NoSuchElementException;

/**
 * Должен наследовать List
 * Односвязный список
 */
public class MyLinkedList extends List implements Queue, Stack {

    Node root;
    Node least;

    @Override
    public void enqueue(int value) {
        add(value);
    }

    @Override
    public int dequeu() {
        return remove(0);
    }

    @Override
    public void push(int value) {
        add(value);
    }

    @Override
    public int pop() {
        return remove(size - 1);
    }

    /**
     * private - используется для сокрытия этого класса от других.
     * Класс доступен только изнутри того, где он объявлен
     * <p>
     * static - позволяет использовать Node без создания экземпляра внешнего класса
     */
    private static class Node {
        Node prev;
        Node next;
        int val;

        Node(Node prev, Node next, int val) {
            this.prev = prev;
            this.next = next;
            this.val = val;
        }
    }

    @Override
    void add(int item) {
        if (root == null) {
            root = new Node(null, null, item);
            size++;
        } else if (least == null) {
            least = new Node(root, null, item);
            root.next = least;
            size++;
        } else {
            Node node = new Node(least, null, item);
            least.next = node;
            size++;
        }
    }

    @Override
    int remove(int idx) throws NoSuchElementException {
        if (idx > size - 1 || idx < 0) {
            throw new NoSuchElementException();
        }
        Node node = root;

        if (idx == 0) {
            int rootVal = root.val;
            root = root.next;
            size--;
            return rootVal;
        }

        for (int i = 1; i < idx + 1; i++) {
            node = node.next;
        }
        size--;
        int nodeVal = node.val;
        node.prev.next = node.next;
        if (node.next != null) {
            node.next.prev = node.prev;
        }
        return nodeVal;
    }

    @Override
    int get(int idx) throws NoSuchElementException {
        if (idx > size - 1 || idx < 0) {
            throw new NoSuchElementException();
        }
        Node node = root;

        if (idx == 0) {
            return root.val;
        }


        for (int i = 1; i < idx + 1; i++) {
            node = node.next;
        }
        return node.val;
    }
}
