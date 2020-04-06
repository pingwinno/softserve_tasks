package module1.task2;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

class SSHashMapTest {

    private SSHashMap<Integer, Integer> integerSSHashMap;
    private SSHashMap<String, String> stringSSHashMap;

    static String stringGenerator() {
        return Integer.toString(new Random().nextInt());
    }

    @BeforeEach
    void init() {
        stringSSHashMap = new SSHashMap<>();
        integerSSHashMap = new SSHashMap<>();
    }

    //Test SSHashMap#put with empty map
    @Test
    void shouldReturnNullWhenCallPut() {
        Assertions.assertNull(integerSSHashMap.put(1, 1));
    }

    //Test SSHashMap#toString
    @Test
    void shouldBeEqualsWhenCallToString() {
        integerSSHashMap.put(1, 1);
        for (Map.Entry<Integer, Integer> element : integerSSHashMap.entrySet()) {
            Assertions.assertEquals("1=1", element.toString());
        }
    }

    //Test SSHashMap#put with non empty map
    @Test
    void shouldReturnOldValueWhenCallPut() {
        integerSSHashMap.put(1, 1);
        Assertions.assertEquals(1, integerSSHashMap.put(1, 2));
    }

    //Test SSHashMap#put with large data set
    @Test
    void shouldBeEqualsWhenCallPutNewValueWithSameKey() {
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

    //Test SSHashMap#containsKey positive case
    @Test
    void shouldReturnTrueWhenCallContainsKey() {
        integerSSHashMap.put(1, 1);
        Assertions.assertTrue(integerSSHashMap.containsKey(1));
    }

    //Test SSHashMap#containsKey negative case
    @Test
    void shouldReturnFalseWhenCallContainsKey() {
        integerSSHashMap.put(1, 1);
        Assertions.assertFalse(integerSSHashMap.containsKey(2));
    }

    //Test SSHashMap#remove return value
    @Test
    void shouldReturnValueWhenCallRemove() {
        integerSSHashMap.put(1, 1);
        Assertions.assertEquals(1, integerSSHashMap.remove(1));
    }

    //Test SSHashMap#isEmpty positive case with empty map
    @Test
    void shouldReturnTrueWhenCallIsEmptyOnEmptyMap() {
        Assertions.assertTrue(integerSSHashMap.isEmpty());
    }

    //Test SSHashMap#isEmpty positive case after remove element
    @Test
    void shouldReturnTrueWhenCallIsEmptyAfterRemove() {
        integerSSHashMap.put(1, 1);
        integerSSHashMap.remove(1);
        Assertions.assertTrue(integerSSHashMap.isEmpty());
    }

    //Test SSHashMap#isEmpty negative case on non empty map
    @Test
    void shouldReturnFalseWhenCallRemove() {
        integerSSHashMap.put(1, 1);
        Assertions.assertFalse(integerSSHashMap.isEmpty());
    }

    //Test two sequential call SSHashMap#remove on same key and return value on second call
    @Test
    void shouldReturnNullWhenCallRemoveTwice() {
        integerSSHashMap.put(1, 1);
        integerSSHashMap.remove(1);
        Assertions.assertNull(integerSSHashMap.remove(1));
    }

    //Test SSHashMap#size call on empty map
    @Test
    void shouldReturnZeroWhenCallSizeEmptyMap() {
        Assertions.assertEquals(0, integerSSHashMap.size());
    }

    //Test proper size change after adding elements
    @Test
    void shouldBeEqualsWhenCompareCounterAndSizeAfterPut() {
        for (int i = 0; i < 1000; i++) {
            integerSSHashMap.put(i, i);
            Assertions.assertEquals(i, integerSSHashMap.size() - 1);
        }
    }

    //Test proper size change after removing elements
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

    //Test SSHashMap#isEmpty after call SSHashMap#clear with large data set
    @Test
    void shouldBeTrueWhenClear() {
        for (int i = 0; i < 1000; i++) {
            integerSSHashMap.put(i, i);
        }
        integerSSHashMap.clear();
        Assertions.assertTrue(integerSSHashMap.isEmpty());
    }

    //Test SSHashMap.SSEntry#equals
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

    //Test SSHashMap.SSEntry#hashCode
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

    //Test SSHashMap#containsValue after changes via SSHashMap.SSEntry instance
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

    //Test SSHashMap#remove return value with large set
    @Test
    void shouldBeEqualsWhenRemovesAndReturnElementsFromSourceMap() {
        Map<String, String> sourceMap = new HashMap<>();
        for (int i = 0; i < 10; i++) {
            sourceMap.put(stringGenerator(), stringGenerator());
        }

        for (Map.Entry<String, String> entry : sourceMap.entrySet()) {
            stringSSHashMap.put(entry.getKey(), entry.getValue());
        }
        for (Map.Entry<String, String> entry : sourceMap.entrySet()) {
            Assertions.assertEquals(entry.getValue(), stringSSHashMap.remove(entry.getKey()));
        }
    }

    //Check map integrity after add and remove large data set
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

    //Test SSHashMap#containsValue positive case
    @Test
    void shouldReturnTrueWhenCallContainsValue() {
        integerSSHashMap.put(1, 1);
        Assertions.assertTrue(integerSSHashMap.containsValue(1));
    }

    //Test SSHashMap#containsValue negative case
    @Test
    void shouldReturnFalseWhenCallContainsValue() {
        integerSSHashMap.put(1, 1);
        Assertions.assertFalse(integerSSHashMap.containsValue(2));
    }

    //Test SSHashMap#get with non empty map
    @Test
    void shouldReturnValueWhenCallGet() {
        integerSSHashMap.put(1, 1);
        Assertions.assertEquals(1, integerSSHashMap.get(1));
    }

    //Test SSHashMap#get with empty map
    @Test
    void shouldReturnNullWhenCallGet() {
        Assertions.assertNull(integerSSHashMap.get(1));
    }

    //Test SSHashMap#get with large data set
    @Test
    void shouldBeEqualsWhenCallGet() {
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

    //Test SSHashMap#putAll
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

    //Test SSHashMap#equals with two similar maps
    @Test
    void shouldBeEqualsWhenMapsContainsSameData() {
        Map<Integer, Integer> stringSSHashMap1 = new SSHashMap<>();
        Map<Integer, Integer> stringSSHashMap2 = new SSHashMap<>();

        for (int i = 0; i < 1000; i++) {
            stringSSHashMap1.put(i, i);
            stringSSHashMap2.put(i, i);
        }
        Assertions.assertEquals(stringSSHashMap1, stringSSHashMap2);
        Assertions.assertEquals(stringSSHashMap2, stringSSHashMap1);
    }

    //Test SSHashMap#toString by comparing with reference from HashMap
    @Test
    void shouldBeEqualsWhenCompareToStringResult(){
        Map<String, String> sourceMap = new HashMap<>();
            sourceMap.put(stringGenerator(), stringGenerator());
        stringSSHashMap.putAll(sourceMap);
        Assertions.assertEquals(sourceMap.toString(),stringSSHashMap.toString());
    }
}