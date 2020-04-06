package module1.task2;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class SSArrayListTest {

    //Test detection of negative capacity
    @Test
    void shouldThrowWhenNegativeCapacity() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new SSArrayList<>(-2));
    }
    //Test correct work of isEmpty method on empty list
    @Test
    void shouldReturnTrueWhenEmpty() {
        SSArrayList arrayList = new SSArrayList();
        Assertions.assertTrue(arrayList.isEmpty());
    }
    //Test correct work of isEmpty method on non empty list
    @Test
    void shouldReturnFalseWhenNotEmpty() {
        SSArrayList<Object> arrayList = new SSArrayList<>();
        Object object = new Object();
        arrayList.add(object);
        Assertions.assertFalse(arrayList.isEmpty());
    }
    //Test correct work of isEmpty method on non empty list
    @Test
    void shouldBeEqualsWhenAddAndGet() {
        SSArrayList<Object> arrayList = new SSArrayList<>();
        Object object = new Object();
        arrayList.add(object);
        Assertions.assertEquals(object, arrayList.get(0));
    }
    //Test correct work of get and add methods
    @Test
    void shouldBeEqualsWhenAddOnIndexAndGet() {
        SSArrayList<Object> arrayList = new SSArrayList<>();
        Object object = new Object();
        for (int i = 0; i < 100; i++) {
            arrayList.add(new Object());
        }
        arrayList.add(50, object);
        Assertions.assertEquals(object, arrayList.get(50));
    }

    //Test correct work of get and add to index methods and array index integrity
    @Test
    void shouldBeEqualsWhenAddOnIndexInTheMiddleAndGet() {
        SSArrayList<Integer> arrayList1 = new SSArrayList<>();
        for (int i = 0; i < 100; i++) {
            arrayList1.add(i);
        }
        int offset = 0;
        arrayList1.add(50, 150);
        for (int i = 0; i < 100; i++) {
            if (i == 50) {
                Assertions.assertEquals(150, arrayList1.get(i + offset));
                offset++;
            }
            Assertions.assertEquals(i, arrayList1.get(i + offset));
        }
    }

    //Test detection of call get with wrong index
    @Test
    void shouldThrowWhenIndexBiggerThanListSize() {
        SSArrayList<Object> arrayList = new SSArrayList<>(10);
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> arrayList.get(1));
    }

    //Test size reset after call clear method
    @Test
    void shouldReturnZeroWhenSizeAfterClear() {
        SSArrayList<Object> arrayList = new SSArrayList<>();
        Object object = new Object();
        arrayList.add(object);
        arrayList.clear();
        Assertions.assertEquals(0, arrayList.size());
    }

    //Test list integrity after increase capacity
    @Test
    void shouldReturnSameObjectWhenEnsureCapacity() {
        SSArrayList<Object> arrayList = new SSArrayList<>(0);
        for (int i = 0; i < 20; i++) {
            Object object = new Object();
            arrayList.add(object);
            Assertions.assertEquals(object, arrayList.get(i));
        }
    }

    //Test positive case of call contains method
    @Test
    void shouldReturnTrueWhenContainsSameObject() {
        SSArrayList<Integer> arrayList = new SSArrayList<>();
        arrayList.add(1000);
        Assertions.assertTrue(arrayList.contains(1000));
    }

    //Test negative case of call contains method
    @Test
    void shouldReturnFalseWhenContainsNotSameObject() {
        SSArrayList<Integer> arrayList = new SSArrayList<>();
        arrayList.add(1000);
        Assertions.assertFalse(arrayList.contains(100));
    }

    //Test positive case of call contains method on null
    @Test
    void shouldReturnTrueWhenContainsNull() {
        SSArrayList<Object> arrayList = new SSArrayList<>();
        arrayList.add(null);
        Assertions.assertTrue(arrayList.contains(null));
    }

    //Test correct work of toArray method and returned array indexing
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


    //Test correct work of toArray method with passed array and returned array indexing
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

    //Test correct work of toArray method with passed smaller than list array and returned array indexing
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

    //Test correct work of toArray method with passed larger than list array and returned array indexing
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

    //Test list integrity after remove one element in the middle
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

    //Test negative case of call remove method
    @Test
    void shouldReturnFalseWhenRemoveNonExistingElement() {
        SSArrayList<Integer> arrayList = new SSArrayList<>();
        arrayList.add(10);
        Assertions.assertFalse(arrayList.remove(Integer.valueOf(20)));
    }

    //Test positive case of call remove method
    @Test
    void shouldReturnTrueWhenRemoveElement() {
        SSArrayList<Integer> arrayList = new SSArrayList<>();
        arrayList.add(10);
        Assertions.assertTrue(arrayList.remove(Integer.valueOf(10)));
    }

    //Test proper size changing after removing one element by index from list with one element
    @Test
    void shouldReturnZeroSizeWhenRemoveElementByIndex() {
        SSArrayList<Integer> arrayList = new SSArrayList<>();
        arrayList.add(10);
        arrayList.remove(0);
        Assertions.assertEquals(0, arrayList.size());
    }

    //Test value returning on remove by index call
    @Test
    void shouldReturnValueWhenRemoveElementByIndex() {
        SSArrayList<Integer> arrayList = new SSArrayList<>();
        arrayList.add(10);
        Assertions.assertEquals(10, arrayList.remove(0));
    }

    //Test remove by index on wrong index call
    @Test
    void shouldThrowWhenRemoveElementByIndex() {
        SSArrayList<Integer> arrayList = new SSArrayList<>();
        arrayList.add(10);
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> arrayList.remove(1));
    }

    //Test proper size changing after removing one element value index from one element list
    @Test
    void shouldReturnZeroWhenSizeAfterRemoveElement() {
        SSArrayList<Integer> arrayList = new SSArrayList<>();
        arrayList.add(10);
        arrayList.remove(Integer.valueOf(10));
        Assertions.assertEquals(0, arrayList.size());
    }

    //Test proper handling null remove from empty array
    @Test
    void shouldReturnFalseWhenRemoveNullOnEmptyArray() {
        SSArrayList<Integer> arrayList = new SSArrayList<>();
        Assertions.assertFalse(arrayList.remove(null));
    }

    //Test positive case containAll and addAll methods
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

    //Test negative case of containAll and addAll methods
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

    //Test proper work of containAll method on three different lists
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

    //Test negative case of containAll method
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

    //Test proper work of removeAll method combined with containsAll
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

    //Test positive case of removeAll method
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

    //Test negative case of removeAll method
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

    //Test proper work of retainAll method combined with containsAll
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

    //Test positive case of retainAll method
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

    //Test negative case of retainAll method
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

    //Test positive case of equals method
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

    //Test negative case of retainAll method
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

    //Test proper work of hashCode method
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

    //Test positive case of retainAll method
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

    //Test proper work of addAll with index parameter method
    @Test
    void shouldBeTrueWhen() {
        SSArrayList<Integer> arrayList1 = new SSArrayList<>();
        SSArrayList<Integer> arrayList2 = new SSArrayList<>();
        for (int i = 0; i < 100; i++) {
            arrayList1.add(i);
            arrayList2.add(i);
        }
        arrayList1.addAll(80, arrayList2);
    }

    //Test proper work of set method and set returned value
    @Test
    void shouldBeEqualsWhenSet() {
        SSArrayList<Integer> arrayList1 = new SSArrayList<>();
        SSArrayList<Integer> arrayList2 = new SSArrayList<>();
        for (int i = 0; i < 100; i++) {
            arrayList1.add(i);
            arrayList2.add(i);
        }
        for (int i = 0; i < 100; i++) {
            Assertions.assertEquals(i, arrayList1.set(i, arrayList2.get(Math.abs(i - 100 + 1))));
            Assertions.assertEquals(Math.abs(i - 100 + 1), arrayList1.set(i, arrayList2.get(i)));
        }
    }

    //Test proper work of indexOf method
    @Test
    void shouldBeEqualsWhenIndexOf() {
        SSArrayList<Integer> arrayList1 = new SSArrayList<>();
        for (int i = 0; i < 100; i++) {
            arrayList1.add(i);
        }
        for (int i = 0; i < 100; i++) {
            Assertions.assertEquals(i,arrayList1.indexOf(i));
        }
    }

    //Test proper work of lastIndexOf method
    @Test
    void shouldBeEqualsWhenLastIndexOf() {
        SSArrayList<Integer> arrayList1 = new SSArrayList<>();
        for (int i = 0; i < 100; i++) {
            arrayList1.add(i);
        }
        for (int i = 0; i < 100; i++) {
            Assertions.assertEquals(i,arrayList1.lastIndexOf(i));
        }
    }


}