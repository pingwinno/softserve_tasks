package module1.task2;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.ThrowingSupplier;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

class SSHashMapTest {

    SSHashMap<Integer, Integer> integerSSHashMap;
    SSHashMap<String, String> stringSSHashMap;

    static String stringGenerator() {
        return Integer.toString(new Random().nextInt());
    }

    @BeforeEach
    void init() {
        stringSSHashMap = new SSHashMap<>();
        integerSSHashMap = new SSHashMap<>();
    }

    @Test
    void constructorTest() {
        Assertions.assertDoesNotThrow((ThrowingSupplier<SSHashMap<Object, Object>>) SSHashMap::new);
    }

    @Test
    void shouldReturnNullWhenPut() {
        Assertions.assertNull(integerSSHashMap.put(1, 1));
    }

    @Test
    void shouldBeEqualsWhenToString() {
        integerSSHashMap.put(1, 1);
        for (Map.Entry<Integer, Integer> element : integerSSHashMap.entrySet()) {
            Assertions.assertEquals("1=1", element.toString());
        }
    }

    @Test
    void shouldReturnOldValueWhenPut() {
        integerSSHashMap.put(1, 1);
        Assertions.assertEquals(1, integerSSHashMap.put(1, 2));
    }

    @Test
    void shouldBeEqualsWhenPutNewValueWithSameKey() {

        Map<String, String> sourceMap = new HashMap<>();
        for (int i = 0; i < 1000; i++) {
            sourceMap.put(stringGenerator(), stringGenerator());
        }

        for (Map.Entry<String, String> entry : sourceMap.entrySet()) {
            stringSSHashMap.put(entry.getKey(), entry.getValue());
        }
        for (Map.Entry<String, String> entry : sourceMap.entrySet()) {
            Assertions.assertEquals(entry.getValue(), stringSSHashMap.put(entry.getKey(), stringGenerator()));
        }
    }

    @Test
    void shouldReturnTrueWhenContainsKey() {
        integerSSHashMap.put(1, 1);
        Assertions.assertTrue(integerSSHashMap.containsKey(1));
    }

    @Test
    void shouldReturnFalseWhenContainsKey() {
        integerSSHashMap.put(1, 1);
        Assertions.assertFalse(integerSSHashMap.containsKey(2));
    }

    @Test
    void shouldReturnValueWhenRemove() {
        integerSSHashMap.put(1, 1);
        Assertions.assertEquals(1, integerSSHashMap.remove(1));
    }

    @Test
    void shouldReturnTrueWhenIsEmptyOnEmptyMap() {
        Assertions.assertTrue(integerSSHashMap.isEmpty());
    }

    @Test
    void shouldReturnTrueWhenIsEmptyAfterRemove() {
        integerSSHashMap.put(1, 1);
        integerSSHashMap.remove(1);
        Assertions.assertTrue(integerSSHashMap.isEmpty());
    }

    @Test
    void shouldReturnFalseWhenRemove() {
        integerSSHashMap.put(1, 1);
        Assertions.assertFalse(integerSSHashMap.isEmpty());
    }

    @Test
    void shouldReturnNullWhenRemoveTwice() {
        integerSSHashMap.put(1, 1);
        integerSSHashMap.remove(1);
        Assertions.assertNull(integerSSHashMap.remove(1));
    }

    @Test
    void shouldReturnZeroWhenEmptyMap() {
        Assertions.assertEquals(0, integerSSHashMap.size());
    }

    @Test
    void shouldBeEqualsWhenCompareCounterAndSizeAfterPut() {
        for (int i = 0; i < 1000; i++) {
            integerSSHashMap.put(i, i);
            Assertions.assertEquals(i, integerSSHashMap.size() - 1);
        }
    }

    @Test
    void shouldBeEqualsWhenCompareCounterAndSizeAfterRemove() {
        for (int i = 0; i < 1000; i++) {
            integerSSHashMap.put(i, i);
        }
        for (int i = 1000; i >= 0; i--) {
            integerSSHashMap.remove(i);
            Assertions.assertEquals(i, integerSSHashMap.size());
        }
    }

    @Test
    void shouldBeTrueWhenClear() {
        for (int i = 0; i < 1000; i++) {
            integerSSHashMap.put(i, i);
        }
        integerSSHashMap.clear();
        Assertions.assertTrue(integerSSHashMap.isEmpty());
    }

    @Test
    void shouldBeEqualsWhenCompareOnEntry() {
        Map<Integer, Integer> sourceMap = new HashMap<>();
        sourceMap.put(1, 1);
        integerSSHashMap.put(1, 1);
        Map.Entry<Integer, Integer> sourceEntry = null;
        for (Map.Entry<Integer, Integer> element : sourceMap.entrySet()) {
            sourceEntry = element;
        }
        Map.Entry<Integer, Integer> entry = null;
        for (Map.Entry<Integer, Integer> element : integerSSHashMap.entrySet()) {
            entry = element;
        }
        Assertions.assertEquals(sourceEntry, entry);
        Assertions.assertEquals(entry, sourceEntry);
    }

    @Test
    void shouldBeEqualsWhenCompareOnEntryHash() {
        Map<Integer, Integer> sourceMap = new HashMap<>();
        sourceMap.put(1, 1);
        integerSSHashMap.put(1, 1);
        Map.Entry<Integer, Integer> sourceEntry = null;
        for (Map.Entry<Integer, Integer> element : sourceMap.entrySet()) {
            sourceEntry = element;
        }
        Map.Entry<Integer, Integer> entry = null;
        for (Map.Entry<Integer, Integer> element : integerSSHashMap.entrySet()) {
            entry = element;
        }
        Assertions.assertEquals(sourceEntry.hashCode(), entry.hashCode());
    }

    @Test
    void shouldReturnTrueWhenChangeValueOnEntry() {
        integerSSHashMap.put(1, 1);
        Map.Entry<Integer, Integer> entry = null;
        for (Map.Entry<Integer, Integer> element : integerSSHashMap.entrySet()) {
            entry = element;
        }
        entry.setValue(2);
        Assertions.assertTrue(integerSSHashMap.containsValue(2));
    }

    @Test
    void shouldBeNullWhenGetAfterClear() {
        for (int i = 0; i < 1000; i++) {
            integerSSHashMap.put(i, i);
        }
        integerSSHashMap.clear();
        for (int i = 0; i < 1000; i++) {
            Assertions.assertNull(integerSSHashMap.get(i));
        }
    }

    @Test
    void shouldBeEqualsWhenRemovesAndReturnElementsFromKeySet() {
        Map<String, String> sourceMap = new HashMap<>();
        for (int i = 0; i < 1000; i++) {
            sourceMap.put(stringGenerator(), stringGenerator());
        }

        for (Map.Entry<String, String> entry : sourceMap.entrySet()) {
            stringSSHashMap.put(entry.getKey(), entry.getValue());
        }
        for (Map.Entry<String, String> entry : sourceMap.entrySet()) {
            Assertions.assertEquals(entry.getValue(), stringSSHashMap.remove(entry.getKey()));
        }
    }

    @Test
    void shouldBeNullWhenRemovesElementsFromSourceMap() {

        Map<String, String> sourceMap = new HashMap<>();
        for (int i = 0; i < 1000; i++) {
            sourceMap.put(stringGenerator(), stringGenerator());
        }
        for (Map.Entry<String, String> entry : sourceMap.entrySet()) {
            stringSSHashMap.put(entry.getKey(), entry.getValue());
        }
        for (Map.Entry<String, String> entry : sourceMap.entrySet()) {
            stringSSHashMap.remove(entry.getKey());
        }
        for (Map.Entry<String, String> entry : sourceMap.entrySet()) {
            Assertions.assertNull(stringSSHashMap.remove(entry.getKey()));
        }
        Assertions.assertEquals(0, stringSSHashMap.size());
    }

    @Test
    void shouldReturnTrueWhenContainsValue() {
        integerSSHashMap.put(1, 1);
        Assertions.assertTrue(integerSSHashMap.containsValue(1));
    }

    @Test
    void shouldReturnFalseWhenContainsValue() {
        integerSSHashMap.put(1, 1);
        Assertions.assertFalse(integerSSHashMap.containsValue(2));
    }

    @Test
    void shouldReturnValueWhenGet() {
        integerSSHashMap.put(1, 1);
        Assertions.assertEquals(1, integerSSHashMap.get(1));
    }

    @Test
    void shouldReturnNullWhenGet() {
        Assertions.assertNull(integerSSHashMap.get(1));
    }

    @Test
    void shouldBeEqualsWhenGet() {

        Map<String, String> sourceMap = new HashMap<>();
        for (int i = 0; i < 1000; i++) {
            sourceMap.put(stringGenerator(), stringGenerator());
        }
        for (Map.Entry<String, String> entry : sourceMap.entrySet()) {
            stringSSHashMap.put(entry.getKey(), entry.getValue());
        }
        for (Map.Entry<String, String> entry : sourceMap.entrySet()) {
            Assertions.assertEquals(entry.getValue(), stringSSHashMap.get(entry.getKey()));
        }
    }

    @Test
    void shouldBeEqualsWhenPutAll() {

        Map<String, String> sourceMap = new HashMap<>();
        for (int i = 0; i < 1000; i++) {
            sourceMap.put(stringGenerator(), stringGenerator());
        }
        stringSSHashMap.putAll(sourceMap);
        for (Map.Entry<String, String> entry : sourceMap.entrySet()) {
            Assertions.assertEquals(entry.getValue(), stringSSHashMap.get(entry.getKey()));
        }
    }

    @Test
    void shouldBeEquals() {
        Map<Integer, Integer> stringSSHashMap1 = new SSHashMap<>();
        Map<Integer, Integer> stringSSHashMap2 = new SSHashMap<>();

        for (int i = 0; i < 1000; i++) {
            stringSSHashMap1.put(i, i);
            stringSSHashMap2.put(i, i);
        }
        Assertions.assertEquals(stringSSHashMap1, stringSSHashMap2);
        Assertions.assertEquals(stringSSHashMap2, stringSSHashMap1);
    }

    @Test
    void shouldReturnTrueWhenEquals() {
        Map<Integer, Integer> stringSSHashMap1 = new SSHashMap<>();
        Map<Integer, Integer> stringSSHashMap2 = new SSHashMap<>();

        for (int i = 0; i < 1000; i++) {
            stringSSHashMap1.put(i, i);
            stringSSHashMap2.put(i, i);
        }

        Assertions.assertEquals(stringSSHashMap2.equals(stringSSHashMap1), stringSSHashMap1.equals(stringSSHashMap2));
    }

}