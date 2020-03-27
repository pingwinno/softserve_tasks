package module1.task2;

import java.util.*;

public class SSHashMap<K, V> extends AbstractMap<K, V> implements Map<K, V> {

    private static final int DEFAULT_SIZE = 16;
    private static final long INTEGER_SIZE = 4294967296L;
    SSArrayList<Bucket> buckets = new SSArrayList<>(DEFAULT_SIZE);
    private int size = 0;
    private int currentBucketPoolSize;
    private KeySet keySet;
    private Values values;
    private EntrySet entries;

    public SSHashMap() {
        int startRange = Integer.MIN_VALUE;
        long offset = INTEGER_SIZE / DEFAULT_SIZE;
        currentBucketPoolSize = DEFAULT_SIZE;
        for (int i = 0; i < DEFAULT_SIZE; i++) {
            Bucket bucket = new Bucket();
            bucket.setHashRange(startRange, ((startRange += offset) - 1));
            buckets.add(bucket);
        }
    }


    private void ensureCapacity() {
        if (currentBucketPoolSize < Integer.MAX_VALUE) {
            currentBucketPoolSize *= 2;
            int startRange = Integer.MIN_VALUE;
            long offset = INTEGER_SIZE / currentBucketPoolSize;
            SSArrayList<Bucket> newBuckets = new SSArrayList<>(currentBucketPoolSize);
            for (int i = 0; i < currentBucketPoolSize; i++) {
                Bucket bucket = new Bucket();
                bucket.setHashRange(startRange, ((startRange += offset) - 1));
                newBuckets.add(bucket);
            }
            size = 0;
            HashIterator hashIterator = new HashIterator();
            while (hashIterator.hasNext()) {
                put(hashIterator.next(), newBuckets);
            }
            buckets = newBuckets;
            keySet = null;
            values = null;
            entries = null;
        }
    }

    private void put(SSEntry<K, V> entry, Collection<Bucket> buckets) {
        final int keyHash = entry.key.hashCode();
        for (Bucket bucket : buckets) {
            if (bucket.getStartHash() <= keyHash && keyHash <= bucket.endHash) {
                bucket.getEntries().add(entry);
                ++size;
            }
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        final int keyHash = key.hashCode();
        for (Bucket bucket : buckets) {
            if (bucket.getStartHash() <= keyHash && keyHash <= bucket.endHash) {
                if (getEntry(bucket, key) != null) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        for (Bucket bucket : buckets) {
            for (SSEntry<K, V> entry : bucket.getEntries()) {
                if (value.equals(entry.value)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public V get(Object key) {
        final int keyHash = key.hashCode();
        for (Bucket bucket : buckets) {
            if (bucket.getStartHash() <= keyHash && keyHash <= bucket.endHash) {
                SSEntry<K, V> entry = getEntry(bucket, key);
                if (entry != null) {
                    return entry.value;
                } else {
                    return null;
                }
            }
        }
        return null;
    }

    @Override
    public V put(K key, V value) {
        if (size > currentBucketPoolSize * 1000){
            ensureCapacity();
        }
            final int keyHash = key.hashCode();
        for (Bucket bucket : buckets) {
            if (bucket.getStartHash() <= keyHash && keyHash <= bucket.endHash) {
                SSEntry<K, V> entry = getEntry(bucket, key);
                if (entry != null) {
                    try {
                        return entry.value;
                    } finally {
                        entry.value = value;
                    }
                } else {
                    bucket.getEntries().add(new SSEntry<>(key, value));
                    ++size;
                    return null;
                }
            }
        }
        throw new IllegalStateException();
    }

    @Override
    public V remove(Object key) {
        final int keyHash = key.hashCode();
        for (Bucket bucket : buckets) {
            if (bucket.getStartHash() <= keyHash && keyHash <= bucket.endHash) {
                SSEntry<K, V> entry = getEntry(bucket, key);
                if (entry != null) {
                    try {
                        return entry.value;
                    } finally {
                        bucket.getEntries().remove(entry);
                        --size;
                    }
                } else {
                    return null;
                }
            }
        }
        return null;
    }

    private boolean removeValue(Object value) {
        for (Bucket bucket : buckets) {
            Iterator<SSEntry<K, V>> iterator = bucket.getEntries().iterator();
            while (iterator.hasNext()) {
                if (value.equals(iterator.next().value)) {
                    iterator.remove();
                    --size;
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        m.forEach(this::put);
    }

    @Override
    public void clear() {
        for (Bucket bucket : buckets) {
            bucket.getEntries().clear();
        }
        size = 0;
    }

    @Override
    public Set<K> keySet() {
        if (keySet == null)
            return keySet = new KeySet();
        return keySet;
    }

    @Override
    public Collection<V> values() {
        if (values == null)
            return values = new Values();
        return values;
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        if (entries == null) {
            return entries = new EntrySet();
        }
        return entries;
    }

    private SSEntry<K, V> getEntry(Bucket bucket, Object key) {
        final int keyHash = key.hashCode();
        for (SSEntry<K, V> entry : bucket.getEntries()) {
            if (keyHash == entry.getKey().hashCode()) {
                if (key.equals(entry.key)) {
                    return entry;
                }
            }
        }
        return null;
    }

    static class SSEntry<K, V> implements Map.Entry<K, V> {

        K key;
        V value;

        public SSEntry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public final boolean equals(Object o) {
            if (o == this)
                return true;
            if (o instanceof Map.Entry) {
                Map.Entry<?, ?> e = (Map.Entry<?, ?>) o;
                return Objects.equals(key, e.getKey()) &&
                        Objects.equals(value, e.getValue());
            }
            return false;
        }

        @Override
        public final int hashCode() {
            return Objects.hashCode(key) ^ Objects.hashCode(value);
        }

        public final String toString() {
            return key + "=" + value;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V value) {
            try {
                return value;
            } finally {
                this.value = value;
            }
        }
    }

    final class EntrySet extends AbstractSet<Map.Entry<K, V>> {

        @Override
        public Iterator<Map.Entry<K, V>> iterator() {
            return new HashIterator();
        }

        @Override
        public int size() {
            return size;
        }

        @Override
        public boolean isEmpty() {
            return SSHashMap.this.isEmpty();
        }

        @Override
        public boolean contains(Object o) {
            return super.contains(o);
        }

        @Override
        public boolean remove(Object o) {
            return super.remove(o);
        }


        @Override
        public void clear() {
            SSHashMap.this.clear();
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }

    final class Values extends AbstractCollection<V> {
        public final int size() {
            return size;
        }

        public final void clear() {
            SSHashMap.this.clear();
        }

        public final Iterator<V> iterator() {
            return new ValueIterator();
        }

        public final boolean contains(Object o) {
            return containsValue(o);
        }

        @Override
        public boolean remove(Object o) {
            return removeValue(o);
        }


    }

    private class Bucket {
        SSArrayList<SSEntry<K, V>> entries = new SSArrayList<>();
        int startHash;
        int endHash;

        private Bucket() {
        }

        private int getStartHash() {
            return startHash;
        }

        private void setHashRange(int startHash, int endHash) {
            this.startHash = startHash;
            this.endHash = endHash;
        }

        private SSArrayList<SSEntry<K, V>> getEntries() {
            return entries;
        }
    }

    final class KeySet extends AbstractSet<K> {

        @Override
        public boolean remove(Object o) {
            return SSHashMap.this.remove(o) != null;
        }

        @Override
        public Iterator<K> iterator() {
            return new KeyIterator();
        }


        @Override
        public void clear() {
            SSHashMap.this.clear();
        }

        @Override
        public int size() {
            return SSHashMap.this.size;
        }
    }

    final class KeyIterator implements Iterator<K> {

        HashIterator hashIterator = new HashIterator();

        @Override
        public boolean hasNext() {
            return hashIterator.hasNext();
        }

        public final K next() {
            return hashIterator.next().key;
        }

        @Override
        public void remove() {
            hashIterator.remove();
        }
    }

    final class ValueIterator implements Iterator<V> {
        HashIterator hashIterator = new HashIterator();

        @Override
        public boolean hasNext() {
            return hashIterator.hasNext();
        }

        @Override
        public V next() {
            return hashIterator.next().value;
        }

        @Override
        public void remove() {
            hashIterator.remove();
        }
    }


    final class HashIterator implements Iterator<Map.Entry<K, V>> {
        Iterator<SSEntry<K, V>> entryIterator;
        Iterator<Bucket> bucketIterator = buckets.iterator();

        public boolean hasNext() {
            if (bucketIterator.hasNext()) {
                while (entryIterator == null || !entryIterator.hasNext()) {
                    if (bucketIterator.hasNext()) {
                        entryIterator = bucketIterator.next().getEntries().iterator();
                    } else {
                        return false;
                    }

                }
            }
            return entryIterator.hasNext();
        }


        public final SSEntry<K, V> next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return entryIterator.next();
        }


        public void remove() {
            entryIterator.remove();
            --size;
        }
    }

}
