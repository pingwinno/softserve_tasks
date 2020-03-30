package module1.task2;

import java.util.*;

public class SSArrayList<T> implements List<T> {
    private static final int DEFAULT_CAPACITY = 10;
    boolean isModifiedOutsideIterator;
    private Object[] dataArray;
    private List<SubList> subListsList;
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
        subListsList = new LinkedList<>();
    }


    @Override
    public int size() {
        return size;
    }

    @Override
    public T get(int index) {
        indexCheck(index);
        return (T) dataArray[index];
    }

    @Override
    public T set(int index, T element) {
        indexCheck(index);
        try {
            return (T) dataArray[index];
        } finally {
            dataArray[index] = element;
        }
    }

    @Override
    public void add(int index, T element) {
        if (size >= capacity) {
            ensureCapacity();
        }
        indexCheck(index);
        isModifiedOutsideIterator = true;
        System.arraycopy(dataArray, index, dataArray, index + 1, size - index);
        dataArray[index] = element;
        modifySize(1, index);
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
        return new Itr(0);
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

    @Override
    public int indexOf(Object o) {
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
    public int lastIndexOf(Object o) {
        if (o == null) {
            for (int i = size - 1; i > -1; i--) {
                if (dataArray[i] == null) {
                    return i;
                }
            }
        } else {
            for (int i = size - 1; i > -1; i--) {
                if (o.equals(dataArray[i])) {
                    return i;
                }
            }
        }
        return -1;
    }

    @Override
    public ListIterator<T> listIterator() {
        return new Itr(0);
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        indexCheck(index);
        return new Itr(index);
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        return new SubList(fromIndex, toIndex);
    }


    @Override
    public boolean add(T t) {
        isModifiedOutsideIterator = true;
        if (size >= capacity) {
            ensureCapacity();
        }
        dataArray[size] = t;
        modifySize(1, dataArray.length - 1);
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int elementIndex = indexOf(o);
        if (elementIndex < 0) {
            return false;
        }
        this.remove(elementIndex);
        return true;
    }

    @Override
    public T remove(int index) {
        indexCheck(index);
        T element = (T) dataArray[index];

        try {
            return element;
        } finally {
            modifySize(-1, index);
            if (size != 0) {
                System.arraycopy(dataArray, index + 1, dataArray, index, size - index);
            } else {
                dataArray = new Object[DEFAULT_CAPACITY];
            }
            isModifiedOutsideIterator = true;
        }
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
        boolean isModified = false;
        for (T element : collection) {
            if (add(element) && !isModified) {
                isModified = true;
            }
        }
        return isModified;
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        for (Object o : c) {
            add(index++, (T) o);
        }
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        boolean isModified = false;
        for (Object element : collection) {
            if (remove(element) && !isModified) {
                isModified = true;
            }
        }
        return isModified;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SSArrayList<?> arrayList = (SSArrayList<?>) o;
        return Arrays.equals(dataArray, arrayList.dataArray);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(dataArray);
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        boolean isModified = false;
        for (Object element : dataArray) {
            if (!collection.contains(element)) {
                if (remove(element) && !isModified) {
                    isModified = true;
                }
            }
        }
        return isModified;
    }

    @Override
    public void clear() {
        Arrays.stream(dataArray).parallel().forEach(e -> e = null);
        size = 0;
        subListsList.forEach(list -> {
            list.isValid = false;
            list = null;
        });
    }

    private void ensureCapacity() {
        dataArray = Arrays.copyOf(dataArray, capacity = (capacity * 3) / 2 + 1);
    }

    @Override
    public String toString() {
        return Arrays.toString(Arrays.copyOf(dataArray, size));
    }

    private void indexCheck(int index) {
        if (index < -1 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
    }

    private void modifySize(int modCount, int modifiedIndex) {
        size += modCount;
        for (SubList subList : subListsList) {
            if (modifiedIndex >= subList.begin && subList.convertIndex(subList.sublistSize) > modifiedIndex) {
                subList.sublistSize += modCount;
            }
            if (modifiedIndex < subList.begin) {
                subList.begin += modCount;
            }
        }
    }

    private final class Itr implements ListIterator<T> {

        private T lastReturnedElement;
        private int lastReturnedIndex;
        private int cursor;

        private Itr(int cursorPosition) {
            cursor = cursorPosition;
            isModifiedOutsideIterator = false;
            lastReturnedIndex = -1;
        }


        @Override
        public boolean hasNext() {
            return cursor != size;
        }

        @Override
        public T next() {
            outsideModificationCheck();
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            lastReturnedIndex = cursor;
            lastReturnedElement = SSArrayList.this.get(cursor++);
            return lastReturnedElement;
        }

        @Override
        public boolean hasPrevious() {
            return cursor > 0;
        }

        @Override
        public T previous() {
            outsideModificationCheck();
            lastReturnedIndex = cursor - 1;
            if (!hasPrevious()) {
                throw new NoSuchElementException();
            }
            lastReturnedElement = SSArrayList.this.get(lastReturnedIndex);
            cursor = lastReturnedIndex;
            return lastReturnedElement;
        }

        @Override
        public int nextIndex() {
            return cursor;
        }

        @Override
        public int previousIndex() {
            return cursor - 1;
        }

        @Override
        public void remove() {
            if (lastReturnedIndex <= -1) {
                throw new IllegalStateException();
            }
            SSArrayList.this.remove(lastReturnedIndex);
            cursor = lastReturnedIndex;
            lastReturnedElement = null;
            lastReturnedIndex = -1;
            isModifiedOutsideIterator = false;

        }

        @Override
        public void set(T t) {
            if (lastReturnedElement == null) {
                throw new IllegalStateException();
            }
            SSArrayList.this.set(lastReturnedIndex, t);
        }

        @Override
        public void add(T t) {
            outsideModificationCheck();
            SSArrayList.this.add(cursor, t);
            isModifiedOutsideIterator = false;
        }

        private void outsideModificationCheck() {
            if (isModifiedOutsideIterator) {
                throw new ConcurrentModificationException();
            }
        }
    }

    private class SubList implements List<T> {

        int sublistSize;
        int begin;
        boolean isValid;
        boolean isModifiedOutsideIterator;

        private SubList(int begin, int end) {
            subListsList.add(this);
            this.sublistSize = end - begin;
            this.begin = begin;
            isValid = true;
        }

        @Override
        public int size() {
            validateSubList();
            return sublistSize;
        }

        @Override
        public boolean isEmpty() {
            validateSubList();
            return sublistSize == 0;
        }

        @Override
        public boolean contains(Object o) {
            validateSubList();
            return indexOf(o) > -1;
        }

        @Override
        public Iterator<T> iterator() {
            validateSubList();
            return new Itr(0);
        }

        @Override
        public Object[] toArray() {
            validateSubList();
            Object[] arrayCopy = new Object[sublistSize];
            System.arraycopy(dataArray, begin, arrayCopy, 0, sublistSize);
            return arrayCopy;
        }

        @Override
        public <T> T[] toArray(T[] a) {
            validateSubList();
            if (a.length < sublistSize) {
                T[] newArray = (T[]) Arrays.copyOf(dataArray, sublistSize - 1, a.getClass());
                System.arraycopy(dataArray, begin, newArray, 0, sublistSize - 1);
                return newArray;
            }
            System.arraycopy(dataArray, begin, a, 0, sublistSize - 1);
            if (a.length > size) {
                a[size] = null;
            }
            return a;
        }

        @Override
        public boolean add(T t) {
            validateSubList();
            if (size == convertIndex(sublistSize)) {
                SSArrayList.this.add(t);
            } else {
                SSArrayList.this.add(convertIndex(sublistSize), t);
            }
            isModifiedOutsideIterator = true;
            sublistSize++;
            return true;
        }

        @Override
        public boolean remove(Object o) {
            validateSubList();
            if (contains(o)) {
                isModifiedOutsideIterator = true;
                return SSArrayList.this.remove(o);
            }
            return false;
        }

        @Override
        public boolean containsAll(Collection<?> c) {
            validateSubList();
            for (Object element : c) {
                if (!this.contains(element)) {
                    return false;
                }
            }
            return true;
        }

        @Override
        public boolean addAll(Collection<? extends T> c) {
            validateSubList();
            int indexOffset = begin + sublistSize;
            for (T element : c) {
                SSArrayList.this.add(indexOffset++, element);
            }
            return true;
        }

        @Override
        public boolean addAll(int index, Collection<? extends T> c) {
            validateSubList();
            int indexOffset = convertIndex(index);
            for (T element : c) {
                SSArrayList.this.add(indexOffset++, element);
            }
            return true;
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            validateSubList();
            Itr itr = new Itr(0);
            boolean isModified = false;
            while (itr.hasNext() && itr.cursor != sublistSize) {
                if (c.contains(itr.next())) {
                    itr.remove();
                    isModified = true;
                }
            }
            return isModified;
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            validateSubList();
            Itr itr = new Itr(0);
            boolean isModified = false;
            while (itr.hasNext() && itr.cursor != sublistSize) {
                if (!c.contains(itr.next())) {
                    itr.remove();
                    isModified = true;
                }
            }
            return isModified;
        }

        @Override
        public void clear() {
            validateSubList();
            Itr itr = new Itr(0);
            while (itr.hasNext()) {
                itr.next();
                itr.remove();
            }
        }

        @Override
        public T get(int index) {
            validateSubList();
            indexCheck(index);
            index = convertIndex(index);
            return SSArrayList.this.get(index);
        }

        @Override
        public T set(int index, T element) {
            validateSubList();
            indexCheck(index);
            index = convertIndex(index);
            return SSArrayList.this.set(index, element);
        }

        @Override
        public void add(int index, T element) {
            validateSubList();
            indexCheck(index);
            index = convertIndex(index);
            isModifiedOutsideIterator = true;
            SSArrayList.this.add(index, element);
        }

        @Override
        public T remove(int index) {
            validateSubList();
            indexCheck(index);
            index = convertIndex(index);
            isModifiedOutsideIterator = true;
            return SSArrayList.this.remove(index);
        }

        @Override
        public int indexOf(Object o) {
            validateSubList();

            if (o == null) {
                for (int i = begin; i < convertIndex(sublistSize); i++) {
                    if (dataArray[i] == null) {
                        return i - begin;
                    }
                }
            } else {
                for (int i = begin; i < convertIndex(sublistSize); i++) {
                    if (o.equals(dataArray[i])) {
                        return i - begin;
                    }
                }
            }
            return -1;
        }

        @Override
        public int lastIndexOf(Object o) {
            validateSubList();
            if (o == null) {
                for (int i = convertIndex(sublistSize) - 1; i >= begin; i--) {
                    if (dataArray[i] == null) {
                        return i - begin;
                    }
                }
            } else {
                for (int i = convertIndex(sublistSize) - 1; i >= begin; i--) {
                    if (o.equals(dataArray[i])) {
                        return i - begin;
                    }
                }
            }
            return -1;
        }

        @Override
        public ListIterator<T> listIterator() {
            validateSubList();
            return new Itr(0);
        }

        @Override
        public ListIterator<T> listIterator(int index) {
            validateSubList();
            indexCheck(index);
            return new Itr(index);
        }

        @Override
        public List<T> subList(int fromIndex, int toIndex) {
            validateSubList();
            return new SubList(fromIndex, toIndex);
        }

        int convertIndex(int index) {
            validateSubList();
            return begin + index;
        }

        void indexCheck(int index) {
            validateSubList();
            if (index < -1 || index >= sublistSize) {
                throw new IndexOutOfBoundsException();
            }
        }

        void validateSubList() {
            if (!isValid) {
                throw new ConcurrentModificationException();
            }
        }


        private final class Itr implements ListIterator<T> {

            private T lastReturnedElement;
            private int lastReturnedIndex;
            private int cursor;

            private Itr(int cursorPosition) {
                cursor = cursorPosition;
                isModifiedOutsideIterator = false;
                lastReturnedIndex = -1;
            }


            @Override
            public boolean hasNext() {
                return cursor != sublistSize;
            }

            @Override
            public T next() {
                outsideModificationCheck();
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                lastReturnedIndex = cursor;
                lastReturnedElement = SubList.this.get(cursor++);
                return lastReturnedElement;
            }

            @Override
            public boolean hasPrevious() {
                return cursor > 0;
            }

            @Override
            public T previous() {
                outsideModificationCheck();
                lastReturnedIndex = cursor - 1;
                if (!hasPrevious()) {
                    throw new NoSuchElementException();
                }
                lastReturnedElement = SubList.this.get(lastReturnedIndex);
                cursor = lastReturnedIndex;
                return lastReturnedElement;
            }

            @Override
            public int nextIndex() {
                return cursor;
            }

            @Override
            public int previousIndex() {
                return cursor - 1;
            }

            @Override
            public void remove() {
                if (lastReturnedIndex <= -1) {
                    throw new IllegalStateException();
                }
                SubList.this.remove(lastReturnedIndex);
                cursor = lastReturnedIndex;
                lastReturnedElement = null;
                lastReturnedIndex = -1;
                isModifiedOutsideIterator = false;

            }

            @Override
            public void set(T t) {
                if (lastReturnedElement == null) {
                    throw new IllegalStateException();
                }
                SubList.this.set(lastReturnedIndex, t);
            }

            @Override
            public void add(T t) {
                outsideModificationCheck();
                SubList.this.add(cursor, t);
                isModifiedOutsideIterator = false;
            }

            private void outsideModificationCheck() {
                if (isModifiedOutsideIterator) {
                    throw new ConcurrentModificationException();
                }
            }
        }
    }
}
