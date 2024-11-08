package deque;

import java.util.Iterator;
import java.util.Objects;

public class ArrayDeque<T> implements Deque<T>, Iterable<T> {
    private T[] array;
    private int size;

    private int capacity;
    private int head;
    private int tail;
    @SuppressWarnings("unchecked")
    public ArrayDeque() {
        capacity = 8;
        array = (T[]) new Object[capacity];
        size = 0;
        head = 0;
        tail = 0;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void addFirst(T item) {
        if (size == capacity - 1) {
            int newCapacity = capacity * 2;
            moveToNewArray(newCapacity);
            capacity = newCapacity;
        }
        head = (head - 1 + capacity) % capacity;
        array[head] = item;
        size++;
    }

    public void addLast(T item) {
        if (size == capacity - 1) {
            int newCapacity = capacity * 2;
            moveToNewArray(newCapacity);
            capacity = newCapacity;
        }
        array[tail] = item;
        tail = (tail + 1) % capacity;

        size++;
    }

    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }
        T item = array[head];
        array[head] = null;
        head = (head + 1) % capacity;
        size--;
        reSize();
        return item;
    }

    public T removeLast() {
        if (isEmpty()) {
            return null;
        }
        tail = (tail - 1 + capacity) % capacity;
        T item = array[tail];
        array[tail] = null;
        size--;
        reSize();
        return item;
    }
    @SuppressWarnings("unchecked")
    private void moveToNewArray(int newCapacity) {
        T[] newArray = (T[]) new Object[newCapacity];
        int index = 0;


        for (int i = 0; i < size; i++) {
            newArray[index++] = array[(head + i) % capacity];
        }
        head = 0;
        tail = size;
        array = newArray;
    }
    private void reSize() {
        if (capacity >= 16 && size < capacity / 4) {
            int newCapacity = capacity / 4;
            moveToNewArray(newCapacity);
            capacity = newCapacity;
        }

    }
    public void printDeque() {
        int len = 0;
        while (len < size) {
            System.out.print(array[(head + len) % capacity] + " ");
            len += 1;
        }
        System.out.println();
    }
    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        return array[(head + index) % capacity];
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof Deque) {
            Deque<T> other = (Deque<T>) o;
            if (size != other.size()) {
                return false;
            }
            for (int i = 0; i < size; i++) {
                if (!Objects.equals(get(i), other.get(i))) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public Iterator<T> iterator() {
        return new ArrayDequeIterator();
    }

    private class ArrayDequeIterator implements Iterator<T> {
        private int wiz = 0;

        @Override
        public boolean hasNext() {
            return wiz < size;
        }

        @Override
        public T next() {
            if (hasNext()) {
                T item = get(wiz);
                wiz++;
                return item;
            }
            return null;
        }
    }
}
