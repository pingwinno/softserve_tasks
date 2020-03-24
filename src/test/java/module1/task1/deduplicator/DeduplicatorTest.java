package module1.task1.deduplicator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class DeduplicatorTest {

    List<Integer> deduplicatedList = Arrays.asList(9, 0, 4, 5, 8, 7, 1, 3, 2, 6);

    List<Integer> sourceList;
    @BeforeEach
    void initEach(){
        sourceList = new ArrayList<>(Arrays.asList(9, 0, 4, 5, 9, 5, 4, 4, 8, 0, 5, 5, 7, 4, 9, 8, 1, 0, 3, 8, 8,
                0, 2, 8, 6, 0, 8, 5, 1, 6, 4, 1, 5, 8, 4, 1, 2, 2, 7, 2, 5, 9, 7, 9, 1, 5, 0, 5, 1, 1, 6, 9, 1, 1, 4,
                2, 9, 1, 4, 3, 6, 6, 4, 3, 4, 2, 6, 3, 9, 5, 3, 7, 7, 7, 5, 7, 8, 9, 2, 0, 5, 6, 6, 3, 2, 2, 6, 9, 3,
                7, 7, 9, 9, 9, 2, 9, 7, 6, 4, 5));
    }

    @Test
    void deduplicateWithoutBuffer() {
        Assertions.assertEquals(deduplicatedList,Deduplicator.deduplicate(sourceList,false));
    }

    @Test
    void deduplicateWithBuffer() {
        Assertions.assertEquals(deduplicatedList,Deduplicator.deduplicate(sourceList,true));
    }

    @Test
    void exceptionOnNull(){
        Assertions.assertThrows(IllegalArgumentException.class,()->Deduplicator.deduplicate(null,true));
    }
}