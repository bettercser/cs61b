package deque;

import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque<T> {
    private Comparator<T> comparator;
    private final int initMaxIdx = 0;
    public MaxArrayDeque(Comparator<T> comparator) {
        super();
        this.comparator = comparator;
    }
    public T max(Comparator<T> otherComparator) {
        if (size() == 0) {
            return null;
        }
        T maxVal = get(initMaxIdx);
        for (int i = 1; i < size(); i++) {
            T item = get(i);
            if (otherComparator.compare(maxVal, item) < 0) {
                maxVal = item;
            }
        }

        return maxVal;
    }
    public T max() {
        return max(comparator);
    }
}
