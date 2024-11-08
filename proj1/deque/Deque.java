package deque;

public interface Deque<T> {
    int size();

    boolean isEmpty();


    void addFirst(T item);

    void addLast(T item);

    void printDeque();

    T removeFirst();

    T removeLast();

    T get(int index);

}
