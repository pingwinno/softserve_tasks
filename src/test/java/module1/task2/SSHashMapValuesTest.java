package module1.task2;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static module1.task2.SSHashMapTest.stringGenerator;

public class SSHashMapValuesTest {

    SSHashMap<Integer, Integer> integerSSHashMap;

    SSHashMap<String, String> stringSSHashMap;

    @BeforeEach
    void init() {
        stringSSHashMap = new SSHashMap<>();
        integerSSHashMap = new SSHashMap<>();
    }

    @Test
    void shouldReturnTrueWhenContainsOnEntrySet() {
        integerSSHashMap.put(1, 1);
        Assertions.assertTrue(integerSSHashMap.values().contains(1));
    }

    @Test
    void shouldReturnFalseWhenContainsOnEntrySet() {
        integerSSHashMap.put(1, 1);
        Assertions.assertFalse(integerSSHashMap.values().contains(2));
    }

    @Test
    void shouldReturnZeroWhenEmptyMap() {
        Assertions.assertEquals(0, integerSSHashMap.values().size());
    }

    @Test
    void shouldBeEqualsWhenCompareCounterAndSizeAfterPut() {
        Collection<Integer> collection = integerSSHashMap.values();
        for (int i = 0; i < 1000; i++) {
            integerSSHashMap.put(i, i);
            Assertions.assertEquals(i, collection.size() - 1);
        }
    }

    @Test
    void shouldBeTrueWhenClear() {
        for (int i = 0; i < 1000; i++) {
            integerSSHashMap.put(i, i);
        }
        integerSSHashMap.values().clear();
        Assertions.assertTrue(integerSSHashMap.isEmpty());
    }

    @Test
    void shouldBeNullWhenGetAfterClear() {
        for (int i = 0; i < 1000; i++) {
            integerSSHashMap.put(i, i);
        }
        integerSSHashMap.values().clear();
        for (int i = 0; i < 1000; i++) {
            Assertions.assertNull(integerSSHashMap.get(i));
        }
    }

    @Test
    void shouldBeEqualsWhenCompareCounterAndSizeAfterRemove() {
        for (int i = 0; i < 1000; i++) {
            integerSSHashMap.put(i, i);
        }
        Collection<Integer> collection = integerSSHashMap.values();
        for (int i = 1000; i >= 0; i--) {
            collection.remove(i);
            Assertions.assertEquals(i, collection.size());
            Assertions.assertEquals(i, integerSSHashMap.size());
        }
    }

    @Test
    void shouldReturnNullWhenRemoveFromValuesCollection() {
        integerSSHashMap.put(1, 1);
        integerSSHashMap.values().remove(1);
        Assertions.assertNull(integerSSHashMap.get(1));
    }

    @Test
    void shouldReturnNullWhenRemoveElementsFromValuesCollection() {

        Map<String, String> sourceMap = new HashMap<>();
        for (int i = 0; i < 1000; i++) {
            sourceMap.put(stringGenerator(), stringGenerator());
        }

        for (Map.Entry<String, String> entry : sourceMap.entrySet()) {
            stringSSHashMap.put(entry.getKey(), entry.getValue());
        }
        Collection<String> collection = stringSSHashMap.values();
        for (Map.Entry<String, String> entry : sourceMap.entrySet()) {
            collection.remove(entry.getValue());
            Assertions.assertNull(stringSSHashMap.get(entry.getKey()));
        }
    }

    @Test
    void shouldReturnNullWhenRemoveElementsFromValuesIterator() {

        Map<String, String> sourceMap = new HashMap<>();
        for (int i = 0; i < 1000; i++) {
            sourceMap.put(stringGenerator(), stringGenerator());
        }

        for (Map.Entry<String, String> entry : sourceMap.entrySet()) {
            stringSSHashMap.put(entry.getKey(), entry.getValue());
        }
        Collection<String> collection = stringSSHashMap.values();
        Iterator<String> iterator = collection.iterator();
        while (iterator.hasNext()) {
            iterator.next();
            iterator.remove();
        }
        for (Map.Entry<String, String> entry : sourceMap.entrySet()) {
            Assertions.assertFalse(stringSSHashMap.containsValue(entry.getValue()));
        }
    }


    @Test
    void shouldReturnNullWhenRemoveAllValuesCollection() {

        Map<String, String> sourceMap = new HashMap<>();
        for (int i = 0; i < 10; i++) {
            sourceMap.put(stringGenerator(), stringGenerator());
        }

        for (Map.Entry<String, String> entry : sourceMap.entrySet()) {
            stringSSHashMap.put(entry.getKey(), entry.getValue());
        }
        stringSSHashMap.values().removeAll(sourceMap.values());
        for (Map.Entry<String, String> entry : sourceMap.entrySet()) {
            Assertions.assertNull(stringSSHashMap.get(entry.getKey()));
        }
        Assertions.assertEquals(0, stringSSHashMap.size());
    }

    @Test
    void shouldReturnNullWhenRetainAllValuesCollection() {

        Map<String, String> oddSourceMap = new HashMap<>();
        Map<String, String> evenSourceMap = new HashMap<>();
        for (int i = 0; i < 100; i++) {
            String key = stringGenerator();
            String value = stringGenerator();
            stringSSHashMap.put(key, value);
            if (i % 2 == 0) {
                oddSourceMap.put(key, value);
            }
            if (i % 2 != 0) {
                evenSourceMap.put(key, value);
            }
        }
        stringSSHashMap.values().retainAll(oddSourceMap.values());
        oddSourceMap.values().forEach(v -> {
            Assertions.assertTrue(stringSSHashMap.containsValue(v));
        });
        evenSourceMap.values().forEach(v -> {
            Assertions.assertFalse(stringSSHashMap.containsValue(v));
        });
        Assertions.assertEquals(50, stringSSHashMap.size());
    }


}
