package deque;

public interface Deque<T> {
    public int size();
    public boolean isEmpty();
    public void addFirst(T item);
    public void addLast(T item);
    public void printDeque();
    public T removeFirst();
    public T removeLast();
    public T get(int index);

}
