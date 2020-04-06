package module1.task2;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static module1.task2.SSHashMapTest.stringGenerator;

public class SSHashMapEntrySetTest {


    private SSHashMap<Integer, Integer> intMap;
    private SSHashMap<String, String> stringSSHashMap;

    @BeforeEach
    void init() {
        stringSSHashMap = new SSHashMap<>();
        intMap = new SSHashMap<>();
    }

    //Test positive case of SSHashMap.EntrySet#contains method
    @Test
    void shouldReturnTrueWhenContainsOnEntrySet() {
        Map<Integer, Integer> sourceMap = new HashMap<>();
        sourceMap.put(1, 1);
        intMap.put(1, 1);
        Map.Entry<Integer, Integer> entry = null;
        for (Map.Entry<Integer, Integer> element : sourceMap.entrySet()) {
            entry = element;
        }
        Assertions.assertTrue(intMap.entrySet().contains(entry));
    }

    //Test negative case of SSHashMap.EntrySet#contains method
    @Test
    void shouldReturnFalseWhenContainsOnEntrySet() {
        Map<Integer, Integer> sourceMap = new HashMap<>();
        sourceMap.put(2, 2);
        intMap.put(1, 1);
        Map.Entry<Integer, Integer> entry = null;
        for (Map.Entry<Integer, Integer> element : sourceMap.entrySet()) {
            entry = element;
        }
        Assertions.assertFalse(intMap.entrySet().contains(entry));
    }

    //Test work of SSHashMap.EntrySet#remove method
    @Test
    void shouldReturnNullWhenRemoveFromEntrySet() {
        Map<Integer, Integer> sourceMap = new HashMap<>();
        sourceMap.put(1, 1);
        intMap.put(1, 1);
        Map.Entry<Integer, Integer> entry = null;
        for (Map.Entry<Integer, Integer> element : sourceMap.entrySet()) {
            entry = element;
        }
        intMap.entrySet().remove(entry);
        Assertions.assertNull(intMap.get(1));
        Assertions.assertEquals(0, intMap.size());
        Assertions.assertEquals(0, intMap.entrySet().size());
    }

    //Test effect of SSHashMap.EntrySet#remove on parent map
    @Test
    void shouldBeTrueWhenCallClearOnEntrySet() {
        for (int i = 0; i < 1000; i++) {
            intMap.put(i, i);
        }
        intMap.entrySet().clear();
        Assertions.assertTrue(intMap.isEmpty());
    }

    //Test effect of SSHashMap.EntrySet#remove on it self in positive case
    @Test
    void shouldReturnTrueWhenEmpty() {
        intMap.put(1, 1);
        intMap.entrySet().clear();
        Assertions.assertTrue(intMap.entrySet().isEmpty());
    }

    //Test effect of SSHashMap.EntrySet#remove on it self in negative case
    @Test
    void shouldReturnFalseWhenEmpty() {
        for (int i = 0; i < 1000; i++) {
            intMap.put(i, i);
        }
        Assertions.assertFalse(intMap.entrySet().isEmpty());
    }

    //Test SSHashMap.EntrySet iterator realization
    @Test
    void shouldReturnNullWhenCallRemoveFromEntryIterator() {
        Map<String, String> sourceMap = new HashMap<>();
        for (int i = 0; i < 100; i++) {
            sourceMap.put(stringGenerator(), stringGenerator());
        }
        for (Map.Entry<String, String> entry : sourceMap.entrySet()) {
            stringSSHashMap.put(entry.getKey(), entry.getValue());
        }
        Set<Map.Entry<String, String>> set = stringSSHashMap.entrySet();
        Iterator<Map.Entry<String, String>> iterator = set.iterator();
        while (iterator.hasNext()) {
            iterator.next();
            iterator.remove();
        }
        for (Map.Entry<String, String> entry : sourceMap.entrySet()) {
            Assertions.assertFalse(stringSSHashMap.containsKey(entry.getKey()));
        }
        Assertions.assertEquals(0, stringSSHashMap.size());
    }

    //Test proper work SSHashMap.EntrySet#removeAll
    @Test
    void shouldReturnNullWhenCallRemoveAllEntrySet() {
        Map<String, String> sourceMap = new HashMap<>();
        for (int i = 0; i < 1000; i++) {
            sourceMap.put(stringGenerator(), stringGenerator());
        }

        for (Map.Entry<String, String> entry : sourceMap.entrySet()) {
            stringSSHashMap.put(entry.getKey(), entry.getValue());
        }
        stringSSHashMap.entrySet().removeAll(sourceMap.entrySet());
        for (Map.Entry<String, String> entry : sourceMap.entrySet()) {
            Assertions.assertNull(stringSSHashMap.get(entry.getKey()));
        }
        Assertions.assertEquals(0, stringSSHashMap.size());
    }

    // Test positive case of Iterator#hasNext method on SSHashMap.EntrySet
    @Test
    void shouldReturnTrueWhenCallHasNext() {
        intMap.put(1, 1);
        Iterator iterator = intMap.entrySet().iterator();
        Assertions.assertTrue(iterator.hasNext());
    }

    // Test negative case of Iterator#hasNext method on SSHashMap.EntrySet
    @Test
    void shouldReturnFalseWhenWhenHCallasNext() {
        Iterator iterator = intMap.entrySet().iterator();
        Assertions.assertFalse(iterator.hasNext());
    }

    // Test work of Iterator#next method on SSHashMap.EntrySet
    @Test
    void shouldBeEqualsWhenNext() {
        intMap.put(1, 1);
        Iterator iterator = intMap.entrySet().iterator();
        Map.Entry<Integer, Integer> entry = null;
        for (Map.Entry<Integer, Integer> element : intMap.entrySet()) {
            entry = element;
        }
        Assertions.assertEquals(iterator.next(), entry);
    }

    //Test iterator flow violation handling trough Iterator#next call
    @Test
    void shouldThrowNoSuchElementExceptionWhenWhenNext() {
        Iterator iterator = intMap.entrySet().iterator();
        Assertions.assertThrows(NoSuchElementException.class, iterator::next);
    }


}
