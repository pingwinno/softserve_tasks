package module1.task2;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public class SSArrayListISublistIteratorTest {

    private List<Integer> arrayList;
    private ListIterator<Integer> listIterator;


    @BeforeEach
    void init() {
        arrayList = new SSArrayList<>();
        arrayList.add(1);
        arrayList = arrayList.subList(0, 1);
        arrayList.clear();
        for (int i = 0; i < 10; i++) {
            arrayList.add(i);
        }
        listIterator = arrayList.listIterator();
    }

    //Test hasNext method on positive case
    @Test
    void shouldReturnTrueWhenCallHasNext() {
        Assertions.assertTrue(listIterator.hasNext());
    }

    //Test hasNext method on negative case
    @Test
    void shouldReturnFalseWhenCallHasNext() {
        for (int element : arrayList) {
            listIterator.next();
        }
        Assertions.assertFalse(listIterator.hasNext());
    }

    //Test proper iterator indexation using hasNext/next flow
    @Test
    void shouldBeEqualsWhenCallNext() {
        int cursor = 0;
        while (listIterator.hasNext()) {
            Assertions.assertEquals(cursor++, listIterator.next());
        }
    }

    //Test iterator flow violation handling trough Iterator#next call
    @Test
    void shouldThrowNoSuchElementExceptionWhenCallNext() {
        for (int element : arrayList) {
            listIterator.next();
        }
        Assertions.assertThrows(NoSuchElementException.class, () -> listIterator.next());
    }

    //Test remove method
    @Test
    void shouldBeEmptyWhenCallRemove() {
        while (listIterator.hasNext()) {
            listIterator.next();
            listIterator.remove();
        }
        Assertions.assertTrue(arrayList.isEmpty());
    }

    //Test iterator flow violation handling trough Iterator#remove call
    @Test
    void shouldThrowIllegalStateExceptionWhenCallRemove() {
        Assertions.assertThrows(IllegalStateException.class, () -> listIterator.remove());
    }

    //Test hasPrevious method on positive case
    @Test
    void shouldReturnTrueWhenCallHasPrevious() {
        listIterator.next();
        Assertions.assertTrue(listIterator.hasPrevious());
    }

    //Test hasPrevious method on negative case
    @Test
    void shouldReturnFalseWhenCallHasNextOnListIterator() {
        for (int element : arrayList) {
            listIterator.next();
        }
        Assertions.assertFalse(listIterator.hasNext());
    }

    //Test proper iterator indexation using hasPrevious/previous flow
    @Test
    void shouldBeEqualsWhenCallNextOnListIterator() {
        int cursor = arrayList.size() - 1;
        listIterator = arrayList.listIterator(cursor);
        while (listIterator.hasPrevious()) {

            Assertions.assertEquals(--cursor, listIterator.previous());
        }
    }

    //Test iterator flow violation handling trough Iterator#previous call
    @Test
    void shouldThrowNoSuchElementExceptionWhenCallNextOnListIterator() {
        Assertions.assertThrows(NoSuchElementException.class, () -> listIterator.previous());
    }

    //Test remove method
    @Test
    void shouldBeEmptyWhenCallRemoveOnListIterator() {
        listIterator = arrayList.listIterator(arrayList.size() - 1);
        while (listIterator.hasPrevious()) {
            listIterator.previous();
            listIterator.remove();
        }
        listIterator.next();
        listIterator.remove();
        Assertions.assertTrue(arrayList.isEmpty());
    }

    //Test nextIndex method on negative
    @Test
    void shouldBeEqualsWhenCallNextIndex() {
        for (int element : arrayList) {
            Assertions.assertEquals(listIterator.next() + 1, listIterator.nextIndex());
        }
    }

    //Test nextIndex method on negative
    @Test
    void shouldBeEqualsWhenCallPreviousIndex() {
        for (int element : arrayList) {
            Assertions.assertEquals(listIterator.next(), listIterator.previousIndex());
        }
    }

    //Test iterator flow violation handling trough Iterator#remove call
    @Test
    void shouldThrowIllegalStateExceptionWhenCallRemoveNewOnListIterator() {
        Assertions.assertThrows(IllegalStateException.class, () -> listIterator.remove());
    }

    //Test Iterator#add method
    @Test
    void shouldRBeEqualsWhenCallNextAfterAdd() {
        Integer element = 20;
        listIterator.add(element);
        Assertions.assertEquals(element, listIterator.next());
    }

    //Test Iterator#set method
    @Test
    void shouldRBeEqualsWhenCallPreviousAfterSet() {
        Integer element = 20;
        listIterator.next();
        listIterator.set(element);
        Assertions.assertEquals(element, listIterator.previous());
    }

    //Test iterator flow violation handling trough Iterator#set call
    @Test
    void shouldThrowIllegalStateExceptionWhenCallSet() {
        Integer element = 20;
        Assertions.assertThrows(IllegalStateException.class, () -> listIterator.set(element));
    }

}
