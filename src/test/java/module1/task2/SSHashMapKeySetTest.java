package module1.task2;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import static module1.task2.SSHashMapTest.stringGenerator;

public class SSHashMapKeySetTest {

    private SSHashMap<String, String> stringSSHashMap;
    private SSHashMap<Integer, Integer> integerSSHashMap;

    @BeforeEach
    void init() {
        stringSSHashMap = new SSHashMap<>();
        integerSSHashMap = new SSHashMap<>();
    }

    //Test positive case of SSHashMap.KeySet#contains method
    @Test
    void shouldReturnTrueWhenCallContainsOnKeySet() {
        integerSSHashMap.put(1, 1);
        Assertions.assertTrue(integerSSHashMap.keySet().contains(1));
    }

    //Test negative case of SSHashMap.KeySet#contains method
    @Test
    void shouldReturnFalseWhenCallContainsOnKeySet() {
        integerSSHashMap.put(1, 1);
        Assertions.assertFalse(integerSSHashMap.keySet().contains(2));
    }

    //Test work of SSHashMap.KeySet#remove method
    @Test
    void shouldReturnNullWhenCallRemoveFromKeySet() {
        integerSSHashMap.put(1, 1);
        integerSSHashMap.keySet().remove(1);
        Assertions.assertNull(integerSSHashMap.get(1));
    }

    //Test proper work of size changes after SSHashMap.KeySet#put
    @Test
    void shouldBeEqualsWhenCallCompareCounterAndSizeAfterPut() {
        Set<Integer> collection = integerSSHashMap.keySet();
        for (int i = 0; i < 1000; i++) {
            integerSSHashMap.put(i, i);
            Assertions.assertEquals(i, collection.size() - 1);
        }
    }

    //Test proper work of size changes after SSHashMap.KeySet#remove
    @Test
    void shouldBeEqualsWhenCompareCounterAndSizeAfterRemove() {
        for (int i = 0; i < 1000; i++) {
            integerSSHashMap.put(i, i);
        }
        Set<Integer> collection = integerSSHashMap.keySet();
        for (int i = 1000; i >= 0; i--) {
            collection.remove(i);
            Assertions.assertEquals(i, collection.size());
            Assertions.assertEquals(i, integerSSHashMap.size());
        }
    }

    //Test effect of SSHashMap.KeySet#clear on parent map
    @Test
    void shouldBeTrueWhenCallClear() {
        for (int i = 0; i < 1000; i++) {
            integerSSHashMap.put(i, i);
        }
        integerSSHashMap.keySet().clear();
        Assertions.assertTrue(integerSSHashMap.isEmpty());
    }

    //Test effect of SSHashMap.KeySet#clear on parent map
    @Test
    void shouldBeNullWhenCallGetAfterClear() {
        for (int i = 0; i < 1000; i++) {
            integerSSHashMap.put(i, i);
        }
        integerSSHashMap.keySet().clear();

        for (int i = 0; i < 1000; i++) {
            Assertions.assertNull(integerSSHashMap.get(i));
        }
    }

    //Test effect of SSHashMap.KeySet.Iterator#remove on parent map
    @Test
    void shouldReturnNullWhenCallRemoveFromKeyIterator() {

        Map<String, String> sourceMap = new HashMap<>();
        for (int i = 0; i < 100; i++) {
            sourceMap.put(stringGenerator(), stringGenerator());
        }

        for (Map.Entry<String, String> entry : sourceMap.entrySet()) {
            stringSSHashMap.put(entry.getKey(), entry.getValue());
        }
        Set<String> set = stringSSHashMap.keySet();
        Iterator<String> iterator = set.iterator();
        while (iterator.hasNext()) {
            iterator.next();
            iterator.remove();
        }
        for (Map.Entry<String, String> entry : sourceMap.entrySet()) {
            Assertions.assertFalse(stringSSHashMap.containsKey(entry.getKey()));
        }
        Assertions.assertEquals(0, stringSSHashMap.size());
    }

    //Test effect of SSHashMap.KeySet#removeAll on parent map
    @Test
    void shouldReturnNullWhenRemoveAllKeySet() {
        Map<String, String> sourceMap = new HashMap<>();
        for (int i = 0; i < 1000; i++) {
            sourceMap.put(stringGenerator(), stringGenerator());
        }

        for (Map.Entry<String, String> entry : sourceMap.entrySet()) {
            stringSSHashMap.put(entry.getKey(), entry.getValue());
        }
        stringSSHashMap.keySet().removeAll(sourceMap.keySet());
        for (Map.Entry<String, String> entry : sourceMap.entrySet()) {
            Assertions.assertNull(stringSSHashMap.get(entry.getKey()));
        }
        Assertions.assertEquals(0, stringSSHashMap.size());
    }


}
