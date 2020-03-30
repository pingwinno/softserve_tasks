package module1.task2;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class SSArrayListSublistTest {

    private SSArrayList<Integer> arrayList;

    @BeforeEach
    void init() {
        arrayList = new SSArrayList<>();
        for (int i = 0; i < 10; i++) {
            arrayList.add(i);
        }
    }

    //Test correct work of isEmpty method on empty sublist
    @Test
    void shouldReturnTrueWhenCallIsEmpty() {
        List<Integer> subList = arrayList.subList(3, 8);
        subList.clear();
        Assertions.assertTrue(subList.isEmpty());
    }

    //Test correct work of isEmpty method on non empty sublist
    @Test
    void shouldReturnFalseWhenNotEmpty() {
        List<Integer> subList = arrayList.subList(3, 8);
        Assertions.assertFalse(subList.isEmpty());
    }

    //Test correct work of Sublist#get method
    @Test
    void shouldBeEqualsWhenAddAndGet() {
        List<Integer> subList = arrayList.subList(3, 8);
        Assertions.assertEquals(3, subList.get(0));
    }

    //Test correct work of Sublist#add method
    @Test
    void shouldBeEqualsWhenAddOnIndexAndGet() {
        List<Integer> subList = arrayList.subList(3, 8);
        subList.add(3, 100);
        Assertions.assertEquals(100, subList.get(3));
        Assertions.assertEquals(6, subList.size());
    }

    //Test detection of call SubList#get with wrong index
    @Test
    void shouldThrowIndexOutOfBoundsExceptionWhenIndexBiggerThanListSize() {
        List<Integer> sublist = arrayList.subList(3, 8);
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> sublist.get(6));
    }

    //Test list integrity after increase capacity
    @Test
    void shouldReturnSameObjectWhenEnsureCapacity() {
        List<Integer> subList = arrayList.subList(1, 2);
        subList.clear();
        for (int i = 0; i < 20; i++) {
            subList.add(Integer.valueOf(i));
            Assertions.assertEquals(i, subList.get(i));
        }
    }

    //Test positive case of call SubList#contains method
    @Test
    void shouldReturnTrueWhenContainsSameObject() {
        List<Integer> subList = arrayList.subList(1, 2);
        subList.add(1000);
        Assertions.assertTrue(subList.contains(1000));
    }

    //Test negative case of call SubList#contains method
    @Test
    void shouldReturnFalseWhenContainsNotSameObject() {
        List<Integer> subList = arrayList.subList(1, 2);
        subList.add(1000);
        Assertions.assertFalse(subList.contains(100));
    }

    //Test positive case of call SubList#contains method on null
    @Test
    void shouldReturnTrueWhenContainsNull() {
        List<Integer> subList = arrayList.subList(1, 2);
        subList.add(null);
        Assertions.assertTrue(subList.contains(null));
    }

    //Test correct work of SubList#toArray method and returned array indexing
    @Test
    void shouldBeEqualWhenToArray() {
        List<Integer> subList = arrayList.subList(1, 2);
        int poolSize = 100;
        for (int i = 0; i < poolSize; i++) {
            subList.add(i);
        }
        Object[] array = subList.toArray();
        for (int i = 0; i < poolSize; i++) {
            Assertions.assertEquals(subList.get(i), array[i]);
        }
    }


    //Test correct work of SubList#toArray method with passed array and returned array indexing
    @Test
    void shouldBeEqualWhenToArrayParametrized() {
        int poolSize = 100;
        List<Integer> subList = arrayList.subList(1, 2);
        for (int i = 0; i < poolSize; i++) {
            subList.add(i);
        }
        Integer[] array = new Integer[poolSize];
        array = subList.toArray(array);
        for (int i = 0; i < poolSize; i++) {
            Assertions.assertEquals(subList.get(i), array[i]);
        }
    }

    //Test correct work of SubList#toArray method with passed smaller than list array and returned array indexing
    @Test
    void shouldBeEqualWhenToArrayParametrizedWithSmallerArray() {
        int poolSize = 100;
        List<Integer> subList = arrayList.subList(1, 2);
        for (int i = 0; i < poolSize; i++) {
            subList.add(i);
        }
        Integer[] array = new Integer[poolSize - 1];
        array = subList.toArray(array);
        for (int i = 0; i < poolSize; i++) {
            Assertions.assertEquals(subList.get(i), array[i]);
        }
    }

    //Test correct work of toArray method with passed larger than list array and returned array indexing
    @Test
    void shouldBeEqualWhenToArrayParametrizedWithLargerArray() {
        int poolSize = 100;
        List<Integer> subList = arrayList.subList(1, 2);
        for (int i = 0; i < poolSize; i++) {
            subList.add(i);
        }
        Integer[] array = new Integer[poolSize + 1];
        array = subList.toArray(array);
        Assertions.assertNull(array[poolSize]);
    }

    //Test list integrity after remove one element in the middle
    @Test
    void shouldBeEqualsWhenRemoveOneElement() {
        int poolSize = 10;
        //sublist size = 1
        List<Integer> subList = arrayList.subList(1, 2);
        for (int i = 0; i < poolSize; i++) {
            subList.add(i);
        }
        subList.remove(4);
        Assertions.assertEquals(4, subList.get(4));
        Assertions.assertEquals(poolSize, subList.size());
    }

    //Test negative case of call remove method
    @Test
    void shouldReturnFalseWhenRemoveNonExistingElement() {
        arrayList.add(Integer.valueOf(20));
        List<Integer> subList = arrayList.subList(1, 2);
        subList.add(10);
        Assertions.assertFalse(subList.remove(Integer.valueOf(20)));
    }

    //Test positive case of call remove method
    @Test
    void shouldReturnTrueWhenRemoveElement() {
        List<Integer> subList = arrayList.subList(1, 2);
        subList.add(10);
        Assertions.assertTrue(subList.remove(Integer.valueOf(10)));
    }

    //Test proper size changing after removing one element by index from list with one element
    @Test
    void shouldReturnZeroSizeWhenRemoveElementByIndex() {
        List<Integer> subList = arrayList.subList(1, 2);
        subList.remove(0);
        Assertions.assertEquals(0, subList.size());
    }

    //Test value returning on remove by index call
    @Test
    void shouldReturnValueWhenRemoveElementByIndex() {
        List<Integer> subList = arrayList.subList(1, 2);
        subList.add(10);
        Assertions.assertEquals(10, subList.remove(1));
    }

    //Test remove by index on wrong index call
    @Test
    void shouldThrowIndexOutOfBoundsExceptionWhenRemoveElementByIndex() {
        List<Integer> subList = arrayList.subList(1, 2);
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> subList.remove(1));
    }

    //Test proper size changing after removing one element value index from one element list
    @Test
    void shouldReturnZeroWhenSizeAfterRemoveElement() {
        List<Integer> subList = arrayList.subList(1, 2);
        subList.clear();
        subList.add(10);
        subList.remove(Integer.valueOf(10));
        Assertions.assertEquals(0, subList.size());
    }

    //Test proper handling null remove from empty array
    @Test
    void shouldReturnFalseWhenRemoveNullOnEmptyArray() {
        List<Integer> subList = arrayList.subList(1, 2);
        Assertions.assertFalse(subList.remove(null));
    }

    //Test positive case containAll and addAll methods
    @Test
    void shouldReturnTrueWhenAddAllElements() {
        List<Integer> subList = arrayList.subList(1, 2);
        subList.clear();
        List<Integer> sourceList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            sourceList.add(i);
        }
        subList.addAll(sourceList);
        Assertions.assertTrue(sourceList.containsAll(subList));
    }

    //Test negative case of containAll and addAll methods
    @Test
    void shouldReturnFalseWhenAddAllElements() {
        List<Integer> subList = arrayList.subList(1, 2);
        List<Integer> sourceList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            subList.add(i);
        }
        subList.addAll(sourceList);
        Assertions.assertFalse(sourceList.containsAll(subList));
    }

    //Test proper work of containAll method on three different lists
    @Test
    void shouldReturnTrueWhenContainsAll() {
        List<Integer> subList = arrayList.subList(1, 2);
        subList.clear();
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
            subList.add(i);
            largerTargetList.add(i);
            largerTargetList.add(i);
        }
        Assertions.assertTrue(subList.containsAll(oddTargetList));
        Assertions.assertTrue(subList.containsAll(evenTargetList));
        Assertions.assertTrue(subList.containsAll(largerTargetList));
    }

    //Test negative case of containAll method
    @Test
    void shouldReturnFalseWhenContainsAll() {
        List<Integer> subList = arrayList.subList(1, 2);
        List<Integer> targetList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            subList.add(i);
            targetList.add(i);
        }
        //Additional element that arrayList doesn't contain.
        targetList.add(150);
        Assertions.assertFalse(subList.containsAll(targetList));
    }

    //Test proper work of removeAll method combined with containsAll
    @Test
    void shouldReturnTrueWhenContainsAllAfterRemoveAll() {
        List<Integer> subList = arrayList.subList(1, 2);
        List<Integer> oddTargetList = new ArrayList<>();
        List<Integer> evenTargetList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            if (i % 2 == 0) {
                oddTargetList.add(i);
            }
            if (i % 2 != 0) {
                evenTargetList.add(i);
            }
            subList.add(i);
        }
        subList.removeAll(oddTargetList);
        Assertions.assertTrue(evenTargetList.containsAll(subList));
    }

    //Test positive case of removeAll method
    @Test
    void shouldReturnTrueWhenRemoveAll() {
        List<Integer> subList = arrayList.subList(1, 2);
        List<Integer> oddTargetList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            if (i % 2 == 0) {
                oddTargetList.add(i);
            }
            subList.add(i);
        }
        Assertions.assertTrue(subList.removeAll(oddTargetList));
    }

    //Test negative case of removeAll method
    @Test
    void shouldReturnFalseWhenRemoveAll() {
        List<Integer> subList = arrayList.subList(1, 2);
        List<Integer> targetList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            targetList.add((i + 2) * 100);
            subList.add(i);
        }
        Assertions.assertFalse(subList.removeAll(targetList));
    }

    //Test proper work of retainAll method combined with containsAll
    @Test
    void shouldReturnTrueWhenContainsAllAfterRetainAll() {
        List<Integer> subList = arrayList.subList(1, 2);
        List<Integer> oddTargetList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            if (i % 2 == 0) {
                oddTargetList.add(i);
            }
            subList.add(i);
        }
        subList.retainAll(oddTargetList);
        Assertions.assertTrue(oddTargetList.containsAll(subList));
    }

    //Test positive case of retainAll method
    @Test
    void shouldReturnTrueWhenRetainAll() {
        List<Integer> subList = arrayList.subList(1, 2);
        List<Integer> oddTargetList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            if (i % 2 == 0) {
                oddTargetList.add(i);
            }
            subList.add(i);
        }
        Assertions.assertTrue(subList.retainAll(oddTargetList));
    }

    //Test negative case of retainAll method
    @Test
    void shouldReturnFalseAfterRetainAll() {
        List<Integer> subList = arrayList.subList(1, 2);
        List<Integer> targetList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            targetList.add(i);
            subList.add(i);
        }
        Assertions.assertFalse(subList.retainAll(targetList));
    }

    //Test proper work of addAll with index parameter method
    @Test
    void shouldBeTrueWhenCallAddAll() {
        List<Integer> subList = arrayList.subList(1, 2);
        SSArrayList<Integer> arrayList2 = new SSArrayList<>();
        for (int i = 0; i < 100; i++) {
            subList.add(i);
            arrayList2.add(i);
        }
        subList.addAll(80, arrayList2);
    }

    //Test proper work of set method and set returned value
    @Test
    void shouldBeEqualsWhenSet() {
        List<Integer> subList = arrayList.subList(1, 2);
        subList.clear();
        SSArrayList<Integer> arrayList2 = new SSArrayList<>();
        for (int i = 0; i < 100; i++) {
            subList.add(i);
            arrayList2.add(i);
        }
        for (int i = 0; i < 100; i++) {
            Assertions.assertEquals(i, subList.set(i, arrayList2.get(Math.abs(i - 100 + 1))));
            Assertions.assertEquals(Math.abs(i - 100 + 1), subList.set(i, arrayList2.get(i)));
        }
    }

    //Test proper work of indexOf method
    @Test
    void shouldBeEqualsWhenIndexOf() {
        List<Integer> subList = arrayList.subList(1, 1);
        subList.clear();
        for (int i = 0; i < 100; i++) {
            subList.add(i);
        }
        for (int i = 0; i < 100; i++) {
            Assertions.assertEquals(i, subList.indexOf(i));
        }
    }

    //Test proper work of lastIndexOf method
    @Test
    void shouldBeEqualsWhenLastIndexOf() {
        List<Integer> subList = arrayList.subList(1, 2);
        subList.clear();
        for (int i = 0; i < 100; i++) {
            subList.add(i);
        }
        for (int i = 0; i < 100; i++) {
            Assertions.assertEquals(i, subList.lastIndexOf(i));
        }
    }


}