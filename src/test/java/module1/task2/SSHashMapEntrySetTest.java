package module1.task2;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static module1.task2.SSHashMapTest.stringGenerator;

public class SSHashMapEntrySetTest {


    SSHashMap<Integer, Integer> intMap;
    SSHashMap<String, String> stringSSHashMap;

    @BeforeEach
    void init() {
        stringSSHashMap = new SSHashMap<>();
        intMap = new SSHashMap<>();
    }

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



    @Test
    void shouldReturnTrueWhenContainsOnKeySet() {
        intMap.put(1, 1);
        for (int element : intMap.keySet()) {
            Assertions.assertTrue(intMap.keySet().contains(element));
        }
    }


    @Test
    void shouldReturnNullWhenRemoveFromKeySet() {
        intMap.put(1, 1);
        intMap.keySet().remove(1);
        Assertions.assertNull(intMap.get(1));
    }


    @Test
    void shouldBeEqualsWhenCompareCounterAndSizeAfterPut() {
        Set<Map.Entry<Integer, Integer>> collection = intMap.entrySet();
        for (int i = 0; i < 1000; i++) {
            intMap.put(i, i);
            Assertions.assertEquals(i, collection.size() - 1);
        }
    }

    @Test
    void shouldBeEqualsWhenCompareCounterAndSizeAfterRemove() {
        Map<Integer, Integer> referenceMap = new HashMap<>();
        for (int i = 0; i < 1000; i++) {
            referenceMap.put(i, i);
            intMap.put(i, i);
        }
        Set<Map.Entry<Integer, Integer>> referenceCollection = referenceMap.entrySet();
        Set<Map.Entry<Integer, Integer>> collection = intMap.entrySet();
        int counter = 1000;
        for (Map.Entry<Integer, Integer> entry : referenceCollection) {
            collection.remove(entry);
            --counter;
            Assertions.assertEquals(counter, collection.size());
            Assertions.assertEquals(counter, intMap.size());
        }
    }

    @Test
    void shouldBeTrueWhenClear() {
        for (int i = 0; i < 1000; i++) {
            intMap.put(i, i);
        }
        intMap.entrySet().clear();
        Assertions.assertTrue(intMap.isEmpty());
    }

    @Test
    void shouldReturnTrueWhenEmpty() {
        intMap.put(1,1);
        intMap.entrySet().clear();
        Assertions.assertTrue(intMap.entrySet().isEmpty());
    }
    @Test
    void shouldReturnFalseWhenEmpty() {
        for (int i = 0; i < 1000; i++) {
            intMap.put(i, i);
        }
        Assertions.assertFalse(intMap.entrySet().isEmpty());
    }

    @Test
    void shouldBeNullWhenGetAfterClear() {
        for (int i = 0; i < 1000; i++) {
            intMap.put(i, i);
        }
        intMap.entrySet().clear();
        for (int i = 0; i < 1000; i++) {
            Assertions.assertNull(intMap.get(i));
        }
    }

    @Test
    void shouldReturnNullWhenRemoveEntitiesFromEntryySet() {
        
        Map<String, String> sourceMap = new HashMap<>();
        for (int i = 0; i < 1000; i++) {
            sourceMap.put(stringGenerator(), stringGenerator());
        }

        for (Map.Entry<String, String> entry : sourceMap.entrySet()) {
            stringSSHashMap.put(entry.getKey(), entry.getValue());
        }
        Set<Map.Entry<String, String>> set = stringSSHashMap.entrySet();
        for (Map.Entry<String, String> entry : sourceMap.entrySet()) {
            set.remove(entry);
            Assertions.assertNull(stringSSHashMap.get(entry.getKey()));
        }
        Assertions.assertEquals(0, stringSSHashMap.size());
    }

    @Test
    void shouldReturnNullWhenRemoveFromKeyIterator() {
        
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

    @Test
    void shouldReturnNullWhenRemoveAllEntrySet() {
        
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

    @Test
    void shouldReturnTrueWhenHasNext() {
        intMap.put(1, 1);
        Iterator iterator = intMap.entrySet().iterator();
        Assertions.assertTrue(iterator.hasNext());
    }

    @Test
    void shouldReturnFalseWhenWhenHasNext() {
        Iterator iterator = intMap.entrySet().iterator();
        Assertions.assertFalse(iterator.hasNext());
    }

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

    @Test
    void shouldThrowWhenWhenNext() {
        Iterator iterator = intMap.entrySet().iterator();
        Assertions.assertThrows(NoSuchElementException.class,iterator::next);
    }


}
