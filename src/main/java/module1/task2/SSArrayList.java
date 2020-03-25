package module1.task2;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class SSArrayList<T> implements Collection<T> {
    public static final int DEFAULT_CAPACITY = 10;
    private Object[] dataArray;
    private int size;
    private int capacity;

    public SSArrayList() {
        this(DEFAULT_CAPACITY);
    }

    public SSArrayList(int capacity) {
        if (capacity < 0) {
            throw new IllegalArgumentException("Size can't be negative");
        }
        this.capacity = capacity;
        dataArray = new Object[capacity];
    }

    @Override
    public int size() {
        return size;
    }

    public T get(int index) {
        if (index > size) {
            throw new IndexOutOfBoundsException();
        }
        return (T) dataArray[index];
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    @Override
    public Iterator<T> iterator() {
        return new Itr();
    }

    @Override
    public Object[] toArray() {
        return Arrays.copyOf(dataArray, size);
    }

    @Override
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            return (T[]) Arrays.copyOf(dataArray, size, a.getClass());
        }
        System.arraycopy(dataArray, 0, a, 0, size);
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }

    int indexOf(Object o) {
        if (o == null) {
            for (int i = 0; i < size; i++) {
                if (dataArray[i] == null) {
                    return i;
                }
            }
        } else {
            for (int i = 0; i < size; i++) {
                if (o.equals(dataArray[i])) {
                    return i;
                }
            }
        }
        return -1;
    }


    @Override
    public boolean add(T t) {
        if (size >= capacity) {
            ensureCapacity();
        }
        dataArray[size++] = t;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int elementIndex;
        if ((elementIndex = indexOf(o)) < 0) {
            return false;
        }
        System.arraycopy(dataArray, elementIndex + 1, dataArray, elementIndex, --size - elementIndex);
        return true;
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        for (Object element : collection) {
            if (!this.contains(element)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends T> collection) {
        boolean flag = false;
        for (T element : collection) {
            if (add(element) && !flag) {
                flag = true;
            }
        }
        return flag;
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        boolean flag = false;
        for (Object element : collection) {
            if (remove(element) && !flag) {
                flag = true;
            }
        }
        return flag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SSArrayList<?> arrayList = (SSArrayList<?>) o;
        return Arrays.equals(dataArray, arrayList.dataArray);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(dataArray);
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        boolean flag = false;
        for (Object element : dataArray) {
            if (!collection.contains(element)) {
                if (remove(element) && !flag) {
                    flag = true;
                }
            }
        }
        return flag;
    }

    @Override
    public void clear() {
        dataArray = new Object[DEFAULT_CAPACITY];
        size = 0;
    }

    private void ensureCapacity() {
        dataArray = Arrays.copyOf(dataArray, capacity = (capacity * 3) / 2 + 1);
    }

    @Override
    public String toString() {
        return Arrays.toString(Arrays.copyOf(dataArray, size));
    }

    private final class Itr implements Iterator<T> {

        T lastReturnedElement;
        private int cursor = 0;

        private Itr() {
        }

        @Override
        public boolean hasNext() {
            return cursor != size;
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            lastReturnedElement = SSArrayList.this.get(cursor++);
            return lastReturnedElement;
        }

        @Override
        public void remove() {
            if (lastReturnedElement == null) {
                throw new IllegalStateException();
            }
            SSArrayList.this.remove(lastReturnedElement);
            lastReturnedElement = null;
            --cursor;
        }

    }
}
