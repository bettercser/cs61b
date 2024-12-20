package deque;

import java.util.Iterator;
import java.util.Objects;

public class LinkedListDeque<T> implements Deque<T>, Iterable<T> {

    private class Node {
        private T data;
        private Node next;
        private Node prev;
        Node(T data) {
            this.data = data;
        }
    }
    private Node head;
    private Node tail;
    private int size;
    public LinkedListDeque() {
        head = new Node(null);
        tail = new Node(null);
        head.next = tail;
        tail.prev = head;
        head.prev = tail;
        tail.next = head;
        size = 0;
    }
    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public void addFirst(T data) {
        Node newNode = new Node(data);
        newNode.next = head.next;
        head.next.prev = newNode;
        head.next = newNode;
        newNode.prev = head;
        size++;
    }

    public void addLast(T data) {
        Node newNode = new Node(data);
        newNode.prev = tail.prev;
        tail.prev.next = newNode;
        newNode.next = tail;
        tail.prev = newNode;
        size++;
    }

    public void printDeque() {
        Node current = head.next;
        while (current != tail) {
            System.out.print(current.data + " ");
            current = current.next;

        }
        System.out.println();
    }

    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }
        T data = head.next.data;
        head.next = head.next.next;
        head.next.prev = head;
        size--;
        return data;
    }

    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        Node current = head.next;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current.data;
    }

    public T getRecursive(int index) {
        return getRecursiveinner(index, head.next);
    }

    private T getRecursiveinner(int index, Node current) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        if (index == 0) {
            return current.data;
        }
        return getRecursiveinner(index - 1, current.next);
    }

    public T removeLast() {

        if (isEmpty()) {
            return null;
        }
        T data = tail.prev.data;
        tail.prev = tail.prev.prev;
        tail.prev.next = tail;
        size--;
        return data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof Deque) {
            Deque<T> other = (Deque<T>) o;

            if (other.size() != size()) {
                return false;
            }
            for (int i = 0; i < size(); i++) {
                if (!Objects.equals(get(i), other.get(i))) {
                    return false;
                }
            }
            return true;

        } else {
            return false;
        }
    }

    private class ListIterator implements Iterator<T> {
        private Node current = head.next;
        @Override
        public boolean hasNext() {
            return current != tail;
        }

        @Override
        public T next() {
            T item = current.data;
            current = current.next;
            return item;
        }
    }



    @Override
    public Iterator<T> iterator() {
        return new ListIterator();
    }
}
