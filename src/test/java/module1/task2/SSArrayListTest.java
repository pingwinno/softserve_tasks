package module1.task2;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.ThrowingSupplier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class SSArrayListTest {

    @Test
        //Default capacity = 10
    void shouldCreateWhenDefaultConstructor() {
        Assertions.assertDoesNotThrow((ThrowingSupplier<SSArrayList>) SSArrayList::new);
    }

    @Test
    void shouldCreateWhenDifferentCapacity() {
        for (int i = 0; i < 5; i++) {
            int capacity = i * 1000;
            Assertions.assertDoesNotThrow(() -> new SSArrayList(capacity));
        }
    }

    @Test
    void shouldThrowWhenNegativeCapacity() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new SSArrayList<>(-2));
    }

    @Test
    void shouldReturnTrueWhenEmpty() {
        SSArrayList arrayList = new SSArrayList();
        Assertions.assertTrue(arrayList.isEmpty());
    }

    @Test
    void shouldReturnFalseWhenNotEmpty() {
        SSArrayList<Object> arrayList = new SSArrayList<>();
        Object object = new Object();
        arrayList.add(object);
        Assertions.assertFalse(arrayList.isEmpty());
    }

    @Test
    void shouldBeEqualsWhenAddAndGet() {
        SSArrayList<Object> arrayList = new SSArrayList<>();
        Object object = new Object();
        arrayList.add(object);
        Assertions.assertEquals(object, arrayList.get(0));
    }

    @Test
    void shouldThrowWhenIndexBiggerThanListSize() {
        SSArrayList<Object> arrayList = new SSArrayList<>(0);
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> arrayList.get(1));
    }

    @Test
    void shouldReturnZeroWhenSizeAfterClear() {
        SSArrayList<Object> arrayList = new SSArrayList<>();
        Object object = new Object();
        arrayList.add(object);
        arrayList.clear();
        Assertions.assertEquals(0, arrayList.size());
    }

    @Test
    void shouldReturnNullWhenGetAfterClear() {
        SSArrayList<Object> arrayList = new SSArrayList<>();
        Object object = new Object();
        arrayList.add(object);
        arrayList.clear();
        Assertions.assertNull(arrayList.get(0));
    }

    @Test
    void shouldReturnSameObjectWhenEnsureCapacity() {
        SSArrayList<Object> arrayList = new SSArrayList<>(0);
        for (int i = 0; i < 20; i++) {
            Object object = new Object();
            arrayList.add(object);
            Assertions.assertEquals(object, arrayList.get(i));
        }
    }

    @Test
    void shouldReturnTrueWhenContainsSameObject() {
        SSArrayList<Integer> arrayList = new SSArrayList<>();
        arrayList.add(1000);
        Assertions.assertTrue(arrayList.contains(1000));
    }

    @Test
    void shouldReturnFalseWhenContainsNotSameObject() {
        SSArrayList<Integer> arrayList = new SSArrayList<>();
        arrayList.add(1000);
        Assertions.assertFalse(arrayList.contains(100));
    }

    @Test
    void shouldReturnTrueWhenContainsNull() {
        SSArrayList<Object> arrayList = new SSArrayList<>();
        arrayList.add(null);
        Assertions.assertTrue(arrayList.contains(null));
    }

    @Test
    void shouldBeEqualWhenToArray() {
        int poolSize = 100;
        SSArrayList<Integer> arrayList = new SSArrayList<>();
        for (int i = 0; i < poolSize; i++) {
            arrayList.add(i);
        }
        Object[] array = arrayList.toArray();
        for (int i = 0; i < poolSize; i++) {
            Assertions.assertEquals(arrayList.get(i), array[i]);
        }
    }

    @Test
    void shouldBeEqualWhenToArrayParametrized() {
        int poolSize = 100;
        SSArrayList<Integer> arrayList = new SSArrayList<>();
        for (int i = 0; i < poolSize; i++) {
            arrayList.add(i);
        }
        Integer[] array = new Integer[poolSize];
        arrayList.toArray(array);
        for (int i = 0; i < poolSize; i++) {
            Assertions.assertEquals(arrayList.get(i), array[i]);
        }
    }

    @Test
    void shouldBeEqualWhenToArrayParametrizedWithSmallerArray() {
        int poolSize = 100;
        SSArrayList<Integer> arrayList = new SSArrayList<>();
        for (int i = 0; i < poolSize; i++) {
            arrayList.add(i);
        }
        Integer[] array = new Integer[poolSize - 1];
        array = arrayList.toArray(array);
        for (int i = 0; i < poolSize; i++) {
            Assertions.assertEquals(arrayList.get(i), array[i]);
        }
    }


    @Test
    void shouldBeEqualWhenToArrayParametrizedWithLargerArray() {
        int poolSize = 100;
        SSArrayList<Integer> arrayList = new SSArrayList<>();
        for (int i = 0; i < poolSize; i++) {
            arrayList.add(i);
        }
        Integer[] array = new Integer[poolSize + 1];
        array = arrayList.toArray(array);
        Assertions.assertNull(array[poolSize]);
    }

    @Test
    void shouldBeEqualsWhenRemoveOneElement() {
        int poolSize = 10;
        SSArrayList<Integer> arrayList = new SSArrayList<>();
        for (int i = 0; i < poolSize; i++) {
            arrayList.add(i);
        }
        arrayList.remove(4);
        Assertions.assertEquals(5, arrayList.get(4));
        Assertions.assertEquals(poolSize - 1, arrayList.size());
    }

    @Test
    void shouldReturnFalseWhenRemoveNonExistingElement() {
        SSArrayList<Integer> arrayList = new SSArrayList<>();
        arrayList.add(10);
        Assertions.assertFalse(arrayList.remove(20));
    }

    @Test
    void shouldReturnTrueWhenRemoveElement() {
        SSArrayList<Integer> arrayList = new SSArrayList<>();
        arrayList.add(10);
        Assertions.assertTrue(arrayList.remove(10));
    }

    @Test
    void shouldReturnZeroWhenSizeAfterRemoveElement() {
        SSArrayList<Integer> arrayList = new SSArrayList<>();
        arrayList.add(10);
        arrayList.remove(10);
        Assertions.assertEquals(0, arrayList.size());
    }

    @Test
    void shouldReturnFalseWhenRemoveNullOnEmptyArray() {
        SSArrayList<Integer> arrayList = new SSArrayList<>();
        Assertions.assertFalse(arrayList.remove(null));
    }


    @Test
    void shouldReturnTrueWhenAddAllElements() {
        SSArrayList<Integer> arrayList = new SSArrayList<>();
        List<Integer> sourceList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            sourceList.add(i);
        }
        arrayList.addAll(sourceList);
        Assertions.assertTrue(sourceList.containsAll(arrayList));
    }

    @Test
    void shouldReturnFalseWhenAddAllElements() {
        SSArrayList<Integer> arrayList = new SSArrayList<>();
        List<Integer> sourceList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            arrayList.add(i);
        }
        arrayList.addAll(sourceList);
        Assertions.assertFalse(sourceList.containsAll(arrayList));
    }


    @Test
    void shouldReturnTrueWhenContainsAll() {
        SSArrayList<Integer> arrayList = new SSArrayList<>();
        List<Integer> oddTargetList = new ArrayList<>();
        List<Integer> evenTargetList = new ArrayList<>();
        List<Integer> largerTargetList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            if (i % 2 == 0) {
                oddTargetList.add(i);
            }
            if (i % 2 != 0) {
                evenTargetList.add(i);
            }
            arrayList.add(i);
            largerTargetList.add(i);
            largerTargetList.add(i);
        }
        Assertions.assertTrue(arrayList.containsAll(oddTargetList));
        Assertions.assertTrue(arrayList.containsAll(evenTargetList));
        Assertions.assertTrue(arrayList.containsAll(largerTargetList));
    }

    @Test
    void shouldReturnFalseWhenContainsAll() {
        SSArrayList<Integer> arrayList = new SSArrayList<>();
        List<Integer> targetList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            arrayList.add(i);
            targetList.add(i);
        }
        //Additional element that arrayList doesn't contain.
        targetList.add(150);
        Assertions.assertFalse(arrayList.containsAll(targetList));
    }

    @Test
    void shouldReturnTrueWhenContainsAllAfterRemoveAll() {
        SSArrayList<Integer> arrayList = new SSArrayList<>();
        List<Integer> oddTargetList = new ArrayList<>();
        List<Integer> evenTargetList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            if (i % 2 == 0) {
                oddTargetList.add(i);
            }
            if (i % 2 != 0) {
                evenTargetList.add(i);
            }
            arrayList.add(i);
        }
        arrayList.removeAll(oddTargetList);
        Assertions.assertTrue(evenTargetList.containsAll(arrayList));
    }

    @Test
    void shouldReturnTrueWhenRemoveAll() {
        SSArrayList<Integer> arrayList = new SSArrayList<>();
        List<Integer> oddTargetList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            if (i % 2 == 0) {
                oddTargetList.add(i);
            }
            arrayList.add(i);
        }
        Assertions.assertTrue(arrayList.removeAll(oddTargetList));
    }

    @Test
    void shouldReturnFalseWhenRemoveAll() {
        SSArrayList<Integer> arrayList = new SSArrayList<>();
        List<Integer> targetList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            targetList.add((i + 2) * 100);
            arrayList.add(i);
        }
        Assertions.assertFalse(arrayList.removeAll(targetList));
    }

    @Test
    void shouldReturnTrueWhenContainsAllAfterRetainAll() {
        SSArrayList<Integer> arrayList = new SSArrayList<>();
        List<Integer> oddTargetList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            if (i % 2 == 0) {
                oddTargetList.add(i);
            }
            arrayList.add(i);
        }
        arrayList.retainAll(oddTargetList);
        Assertions.assertTrue(oddTargetList.containsAll(arrayList));
    }

    @Test
    void shouldReturnTrueWhenRetainAll() {
        SSArrayList<Integer> arrayList = new SSArrayList<>();
        List<Integer> oddTargetList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            if (i % 2 == 0) {
                oddTargetList.add(i);
            }
            arrayList.add(i);
        }
        Assertions.assertTrue(arrayList.retainAll(oddTargetList));
    }

    @Test
    void shouldReturnFalseAfterRetainAll() {
        SSArrayList<Integer> arrayList = new SSArrayList<>();
        List<Integer> targetList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            targetList.add(i);
            arrayList.add(i);
        }
        Assertions.assertFalse(arrayList.retainAll(targetList));
    }

    @Test
    void shouldReturnTrueWhenEquals() {
        SSArrayList<Integer> arrayList1 = new SSArrayList<>();
        SSArrayList<Integer> arrayList2 = new SSArrayList<>();
        for (int i = 0; i < 100; i++) {
            arrayList1.add(i);
            arrayList2.add(i);
        }
        Assertions.assertEquals(arrayList1.equals(arrayList2), arrayList2.equals(arrayList1));
    }

    @Test
    void shouldReturnFalseWhenEquals() {
        List<Integer> oddList = new ArrayList<>();
        List<Integer> evenList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            if (i % 2 == 0) {
                oddList.add(i);
            }
            if (i % 2 != 0) {
                evenList.add(i);
            }
        }
        Assertions.assertEquals(oddList.equals(evenList), evenList.equals(oddList));
    }

    @Test
    void shouldBeTrueWhenHashCode() {
        SSArrayList<Integer> arrayList1 = new SSArrayList<>();
        SSArrayList<Integer> arrayList2 = new SSArrayList<>();
        for (int i = 0; i < 100; i++) {
            arrayList1.add(i);
            arrayList2.add(i);
        }
        Assertions.assertEquals(arrayList1.hashCode(), arrayList2.hashCode());
    }

    @Test
    void shouldBeEqualsWhenCompareToStringWithReference() {
        SSArrayList<Integer> arrayList = new SSArrayList<>();
        int[] referenceArray = new int[100];
        for (int i = 0; i < 100; i++) {
            referenceArray[i] = i;
            arrayList.add(i);
        }
        Assertions.assertEquals(Arrays.toString(referenceArray), arrayList.toString());
    }


}