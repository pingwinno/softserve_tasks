package module1.task2;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class SSArrayListIteratorTest {

    SSArrayList<Integer> arrayList;
    Iterator<Integer> iterator;

    @BeforeEach
    void init() {
        arrayList = new SSArrayList<>();
        for (int i = 0; i < 10; i++) {
            arrayList.add(i);
        }
        iterator = arrayList.iterator();
    }

    @Test
    void shouldReturnTrueWhenHasNext() {
        Assertions.assertTrue(iterator.hasNext());
    }

    @Test
    void shouldReturnFalseWhenHasNext() {
        for (int element : arrayList) {
            iterator.next();
        }
        Assertions.assertFalse(iterator.hasNext());
    }

    @Test
    void shouldBeEqualsWhenNext() {
        int cursor = 0;
        while (iterator.hasNext()) {
            Assertions.assertEquals(cursor++, iterator.next());
        }
    }

    @Test
    void shouldThrowWhenNext() {
        for (int element : arrayList) {
            iterator.next();
        }
        Assertions.assertThrows(NoSuchElementException.class, () -> iterator.next());
    }

    @Test
    void shouldBeEmptyWhenRemove() {
        while (iterator.hasNext()) {
            iterator.next();
            iterator.remove();
        }
        Assertions.assertTrue(arrayList.isEmpty());
    }

    @Test
    void shouldThrowWhenRemove() {
        Assertions.assertThrows(IllegalStateException.class, () -> iterator.remove());
    }

}
