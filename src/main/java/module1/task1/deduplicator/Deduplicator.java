package module1.task1.deduplicator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

public class Deduplicator {

    public static <T> List<T> deduplicate(List<T> list, boolean withBuffer) {
        if (list == null) {
            throw new IllegalArgumentException("List is empty!!!");
        }
        if (withBuffer) {
            return deduplicateWithBuffer(list);
        } else {
            deduplicateWithoutBuffer(list);
            return list;
        }
    }


    private static <T> void deduplicateWithoutBuffer(List<T> list) {
        for (int i = 0; i < list.size(); i++) {
            //flag detects first occurrence
            boolean flag = false;
            Iterator<T> iterator = list.iterator();
            while (iterator.hasNext()) {
                if (flag && list.get(i).equals(iterator.next())) {
                    iterator.remove();
                } else if (!flag && list.get(i).equals(iterator.next())) {
                    flag = true;
                }
            }
        }
    }

    private static <T> List<T> deduplicateWithBuffer(List<T> list) {
        LinkedHashSet<T> hashSet = new LinkedHashSet<>(list);
        return new ArrayList<>(hashSet);
    }

}
