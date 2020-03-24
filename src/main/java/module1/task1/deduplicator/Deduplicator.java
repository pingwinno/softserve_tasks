package module1.task1.deduplicator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

public class Deduplicator {
    public static List<Integer> deduplicate(List<Integer> list, boolean withBuffer) {
        if (withBuffer) {
            return deduplicateWithBuffer(list);
        } else {
            deduplicateWithoutBuffer(list);
            return list;
        }
    }


    private static void deduplicateWithoutBuffer(List<Integer> list) {
        for (int i = 0; i < list.size(); i++) {
            //flag detects first occurrence
            boolean flag = false;
            Iterator<Integer> iterator = list.iterator();
            while (iterator.hasNext()) {
                if (flag && list.get(i).equals(iterator.next())) {
                    iterator.remove();
                } else if (!flag && list.get(i).equals(iterator.next())) {
                    flag = true;
                }
            }
        }
    }

    private static List<Integer> deduplicateWithBuffer(List<Integer> list) {
        LinkedHashSet<Integer> hashSet = new LinkedHashSet<>(list);
        return new ArrayList<>(hashSet);
    }

}
